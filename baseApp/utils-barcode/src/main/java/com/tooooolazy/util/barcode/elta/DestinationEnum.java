package com.tooooolazy.util.barcode.elta;

import com.tooooolazy.util.Messages;

public enum DestinationEnum {
	Domestic,
	International;

	public String toString() {
		return Messages.getString(getClass(), name());
	}
}
