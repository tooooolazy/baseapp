package com.tooooolazy.util.barcode.elta;

public enum ProcessingSpaceEnum {
	LCPM(1),
	LSM(2),
	CFSM(3),
	BBSM(4),
	Man(5),
	LargeMailers(6);

	private int value;

	ProcessingSpaceEnum(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public String toString() {
		return name();
	}
}
