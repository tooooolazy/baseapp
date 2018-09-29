package com.tooooolazy.vaadin.exceptions;

public class NoLoginResourceException extends InvalidBaseAppParameterException {

	public NoLoginResourceException() {
		super("Login Resource cannot be null");
	}

}
