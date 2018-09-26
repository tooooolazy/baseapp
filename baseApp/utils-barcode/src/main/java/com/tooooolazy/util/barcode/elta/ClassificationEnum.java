package com.tooooolazy.util.barcode.elta;

public enum ClassificationEnum {
	A(1),
	B(2),
	Mix(3);

	private int value;

	ClassificationEnum(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public String toString() {
		return name();
	}
}
