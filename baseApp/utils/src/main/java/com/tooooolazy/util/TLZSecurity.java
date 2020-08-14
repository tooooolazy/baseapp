package com.tooooolazy.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class TLZSecurity {

	/**
	 * From a base 64 representation, returns the corresponding byte[]
	 * 
	 * @param data
	 *            String The base64 representation
	 * @return byte[]
	 * @throws IOException
	 */
	public static byte[] base64ToByte(String data) throws IOException {
		BASE64Decoder decoder = new BASE64Decoder();
		return decoder.decodeBuffer(data);
	}

	/**
	 * From a byte[] returns a base 64 representation
	 * 
	 * @param data
	 *            byte[]
	 * @return String
	 * @throws IOException
	 */
	public static String byteToBase64(byte[] data) {
		BASE64Encoder endecoder = new BASE64Encoder();
		return endecoder.encode(data);
	}
	/**
	 * From a password, a number of iterations and a salt, returns the corresponding digest
	 * 
	 * @param iterationNb
	 *            int The number of iterations of the algorithm
	 * @param password
	 *            String The password to encrypt
	 * @param salt
	 *            byte[] The salt
	 * @return byte[] The digested password
	 * @throws NoSuchAlgorithmException
	 *             If the algorithm doesn't exist
	 * @throws UnsupportedEncodingException
	 */
	public static byte[] getHash(int iterationNb, String password, byte[] salt) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		MessageDigest digest = MessageDigest.getInstance("SHA-1");
		digest.reset();
		digest.update(salt);
		byte[] input = digest.digest(password.getBytes("UTF-8"));
		for (int i = 0; i < iterationNb; i++) {
			digest.reset();
			input = digest.digest(input);
		}
		return input;
	}

	public static byte[] createSalt(int bytes) throws NoSuchAlgorithmException {
		byte[] bSalt = new byte[8];
		SecureRandom random;
		random = SecureRandom.getInstance("SHA1PRNG");
		random.nextBytes(bSalt);
		return bSalt;
	}

	public static String encodePassword(String pw, byte[] bSalt, int iterations) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		// Digest computation
		byte[] bDigest = getHash(iterations, pw, bSalt);
		String sDigest = byteToBase64(bDigest);
		return sDigest;
	}
	/**
	 * Verifies that 'password' matches 'against' when using 'withSalt' and 'iterations'
	 * @param password stored password in DB
	 * @param against password we want to verify
	 * @param withSalt salt also saved in DB
	 * @param iterations should match the number of iterations used to create the password
	 * @return true/false
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 */
	public static boolean verify(String password, String against, String withSalt, int iterations) throws IOException, NoSuchAlgorithmException {
		boolean verified = false;

		byte[] bDigest = base64ToByte(password);
		byte[] bSalt = base64ToByte(withSalt);

		// Compute the new DIGEST
		byte[] proposedDigest = getHash(iterations, against, bSalt);

		if (Arrays.equals(proposedDigest, bDigest))
			verified = true;

		return verified;
	}

	public static String hmacSHA256(String data, String key) throws Exception {
		SecretKeySpec secretKey = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
		Mac mac = Mac.getInstance("HmacSHA256");
		mac.init(secretKey);
		byte[] hmacData = mac.doFinal(data.getBytes("UTF-8"));
		String dec = new String(hmacData);
		return dec;
	}
}
