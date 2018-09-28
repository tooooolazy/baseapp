package com.tooooolazy.util.exceptions;

/**
 * Thrown when a verified user tries to Verify himself again or any other verification error occurs
 * @author tooooolazy
 *
 */
public class UserVerificationException extends Exception {
	private String username;

	public UserVerificationException() {
		super("UserVerifiedException");
	}
	public UserVerificationException(String username) {
		super("UserVerifiedException");
		setUsername(username);
	}

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
}
