package com.tooooolazy.util.exceptions;

public class AccessDeniedException extends RuntimeException {
	public AccessDeniedException() {
		super("AccessDeniedException");
	}

	public AccessDeniedException(Exception e) {
		super(e);
	}

}
