package com.tooooolazy.domain.enums;


public enum UserStatus {

	NONE( 0 ),
	ACTIVE( 1 ), 
	INACTIVE( 2 ),
	RESET( 3 );

	private int value;
	
	UserStatus( int aValue ) {
		value = aValue;
	}
	
	public int getValue() {
		return value;
	}
	
	public String toString() {
	    return String.valueOf( value );
	}

	public Integer toInteger() {
		return Integer.valueOf(value);
	}
	
}
