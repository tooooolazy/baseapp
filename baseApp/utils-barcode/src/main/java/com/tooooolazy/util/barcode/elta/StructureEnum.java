package com.tooooolazy.util.barcode.elta;

public enum StructureEnum {
	Letters(1),
	Flats(2),
	Mix(3),
	Packets(4);

	private int value;

	StructureEnum(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public String toString() {
		return name();
	}

}
