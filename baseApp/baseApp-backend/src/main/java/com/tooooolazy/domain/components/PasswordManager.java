package com.tooooolazy.domain.components;

import java.security.MessageDigest;

import org.apache.commons.codec.binary.Hex;
import org.springframework.stereotype.Component;

import com.Ostermiller.util.RandPass;

@Component
public class PasswordManager {
	private String digestAlgorithm = "MD5";
	private String charset = "UTF-8";
	private String charsetMC = "unicode";

	public String getDigestAlgorithm() {
		return this.digestAlgorithm;
	}
	public void setDigestAlgorithm(String algorithm) {
		this.digestAlgorithm = algorithm;
	}

	public String getCharset() {
		return this.charset;
	}
	public void setCharset(String charset) {
		this.charset = charset;
	}

	public String hash(String plainTextPassword) {
		try {
			MessageDigest digest =
				MessageDigest.getInstance(digestAlgorithm);
			digest.update(plainTextPassword.getBytes(charset));
			byte[] rawHash = digest.digest();
			return new String( Hex.encodeHex(rawHash) );
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * To be used for login of other Countries. Encrypts given password. Based on .Net code 'charset' is unicode and 'algorithm' MD5.
	 * Java 'getBytes' produces 2 extra bytes in the front of the array that are excluded from the bytes set in MessageDigest.
	 * @param plainTextPassword
	 * @return
	 */
	public String hashMC(String plainTextPassword) {
		try {
			MessageDigest digest = MessageDigest.getInstance(digestAlgorithm);
			byte b[] = plainTextPassword.getBytes(charsetMC);
			// remove 2 extra initial bytes that do not exist in .NET 'getBytes' 
			byte nb[] = new byte[b.length-2];
			for (int i=0; i< nb.length; i++) {
				nb[i] = b[i+2];
			}
			digest.update(nb);
			byte[] rawHash = digest.digest();

			String p = new String( Hex.encodeHex(rawHash) );

			// add dashes between hex bytes and convert to upper case like .NET does
			String np = "";
			for (int i=0; i<p.length(); i++) {
				np += p.substring(i, i+2) + "-";
				i++;
			}
			np = np.substring(0, np.length()-1).toUpperCase();

			return np;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public String generateGUID() {
		
		long rn = (long)( Math.random() * ( Long.MAX_VALUE ) );

		String srn = Long.toHexString( rn );
		while ( srn.length()<16 ) {
			srn = "0"  +srn;
		}
		
		long tm = System.currentTimeMillis();
		
		String stm = Long.toHexString( tm );
		while ( stm.length()<16 ) {
			stm = "0" + stm;
		}
		
		String guid = srn + stm;
		
		StringBuffer guidBuf = new StringBuffer();
		String DL = "-";
		guidBuf.append( guid.substring(0,8) );
		guidBuf.append( DL ).append( guid.substring(8,12) ).append( DL ).append( guid.substring(12,16) );
		guidBuf.append( DL ).append( guid.substring(16,20) ).append( DL ).append( guid.substring(20) );

		return guidBuf.toString();
	}

	public String createRandomPassword() {
		String randomPassword = null;
		
		randomPassword = new RandPass().getPass( 8 );
		
		System.out.println("******* New Random Password is : " + randomPassword + " *******");
		
		return randomPassword;
	}
}
