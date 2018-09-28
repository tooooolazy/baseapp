package com.tooooolazy.util.exceptions;

import com.tooooolazy.util.Credentials;

public class UserNotVerifiedException extends Exception {
	private Credentials credentials;

	public UserNotVerifiedException() {
		super("UserNotVerifiedException");
	}
	public UserNotVerifiedException(Credentials credentials) {
		super("UserNotVerifiedException");
		setCredentials(credentials);
	}

	public Credentials getCredentials() {
		return credentials;
	}
	public void setCredentials(Credentials credentials) {
		this.credentials = credentials;
	}

}
