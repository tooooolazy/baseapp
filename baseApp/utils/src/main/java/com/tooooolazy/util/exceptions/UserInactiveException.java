package com.tooooolazy.util.exceptions;

import com.tooooolazy.util.Credentials;

public class UserInactiveException extends Exception {
	private Credentials credentials;

	public UserInactiveException() {
		super("UserInactiveException");
	}
	public UserInactiveException(Credentials credentials) {
		super("UserInactiveException");
		setCredentials(credentials);
	}

	public Credentials getCredentials() {
		return credentials;
	}
	public void setCredentials(Credentials credentials) {
		this.credentials = credentials;
	}

}
