package com.tooooolazy.util.exceptions;

import com.tooooolazy.util.Credentials;

/**
 * @author tooooolazy
 *
 */
public class InvalidCredentialsException extends Exception {
	private Credentials credentials;

	public InvalidCredentialsException() {
		super("InvalidCredentialsException");
	}
	public InvalidCredentialsException(Credentials credentials) {
		super("InvalidCredentialsException");
		setCredentials(credentials);
	}

	public Credentials getCredentials() {
		return credentials;
	}
	public void setCredentials(Credentials credentials) {
		this.credentials = credentials;
	}

}
