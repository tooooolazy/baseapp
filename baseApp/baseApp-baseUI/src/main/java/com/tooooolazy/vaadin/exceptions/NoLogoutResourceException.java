package com.tooooolazy.vaadin.exceptions;

public class NoLogoutResourceException extends InvalidBaseAppParameterException {

	public NoLogoutResourceException() {
		super("Logout Resource cannot be null");
	}

}
