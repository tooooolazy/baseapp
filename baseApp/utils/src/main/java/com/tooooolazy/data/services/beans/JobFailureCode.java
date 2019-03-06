/**
 * 
 */
package com.tooooolazy.data.services.beans;

import java.io.Serializable;

/**
 * @author gpatoulas
 *
 */
public enum JobFailureCode implements Serializable {
	GENERIC( 0 ),
	SERVICE_PROBLEM( 1 ),
	UPDATE_IN_PROGRESS( 2 ),
	LOGIN_FAILED( 3 ),
	USER_EXISTS( 4 ),
	DATA_EXISTS( 5 ),
	LOGIN_MULTIPLE( 6 ),
	ITEM_LOCKED( 7 ),
	USER_INACTIVE( 8 )
	;

	private int value;
	
	JobFailureCode( int aValue ) {
		value = aValue;
	}
	
	public int getValue() {
		return value;
	}

	public static JobFailureCode getEnumByValue( int value ) {
		JobFailureCode[] values = values();
		
		for (int i = 0; i < values.length; i++) {
			if (values[i].getValue() == value){
				return values[i];
			}
		}
		
		return null;
	}
}
