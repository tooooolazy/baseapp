package com.dpapp.ws.beans;

/**
 * Basically a modified copy of enum in Utils module.
 * @author gpatoulas
 *
 */
public enum RoleEnum {
	DUMMY_ROLE(0),
	NOT_LOGGED_IN(1), // use this to make something secure (so that it's not even visible if no user is logged in)
	ADMIN(2), // show admin Views
	DEMO(3),
	LOW_LEVEL(4),
	HIGH_LEVEL(5);

	private int value;

	RoleEnum(int value) {
		this.value = value;
	}
	public int getValue() {
		return value;
	}

	public boolean isGod() {
		return false;
	}
	public boolean isAdmin() {
		return this.equals(ADMIN);
	}

	public static RoleEnum byValue(int value) {
		RoleEnum[] values = values();
		
		for (int i = 0; i < values.length; i++) {
			if (values[i].getValue() == value){
				return values[i];
			}
		}
		
		return null;
	}

}
