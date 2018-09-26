package com.tooooolazy.util;


public enum BooleanStatus {
	FALSE(0), TRUE (1);

	private int value;

	BooleanStatus(int value) {
		this.value = value;
	}
	public int getValue() {
		return value;
	}
	public boolean getBooleanValue() {
		return getValue() == 0? false:true;
	}

	public String toString() {
		return Messages.getString(getClass(), name());
	}

}
