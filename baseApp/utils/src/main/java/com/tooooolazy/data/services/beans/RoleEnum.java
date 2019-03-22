package com.tooooolazy.data.services.beans;


/**
 * Basic Roles. These (ideally) should match records in a DB table. If this enum does not meet the App needs a new One, more specific should be created and used with {@link UserBean} that also should be extended.
 * @author gpatoulas
 *
 */
public enum RoleEnum {
	DUMMY_ROLE(0), // required
	NOT_LOGGED_IN(1), // required - use this to make something secure (so that it's not even visible if no user is logged in)
	GOD(2),
	DEMO(3),
	LOW_LEVEL(4),
	HIGH_LEVEL(5),
	ADMIN(6); // show admin Views

	private int value;

	RoleEnum(int value) {
		this.value = value;
	}
	public int getValue() {
		return value;
	}

	public boolean isGod() {
		return this.equals(GOD);
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
