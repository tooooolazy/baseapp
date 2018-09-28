package com.tooooolazy.util.exceptions;

import com.tooooolazy.util.RegistrationData;

public class UsernameExistsException extends Exception {
	protected RegistrationData rd;

	public UsernameExistsException() {
		super("UsernameExistsException");
	}
	public UsernameExistsException(Exception trace) {
		super(trace);
	}

	public UsernameExistsException(RegistrationData rd) {
		this();
		this.rd = rd;
	}
}
