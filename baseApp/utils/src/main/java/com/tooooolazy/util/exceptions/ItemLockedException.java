package com.tooooolazy.util.exceptions;

import org.json.JSONObject;

public class ItemLockedException extends RuntimeException {
	protected JSONObject lockedBy;

	public ItemLockedException() {
		super();
	}

	public ItemLockedException( JSONObject lockedBy) {
		this();
		this.lockedBy = lockedBy;
	}

	public JSONObject getLockedBy() {
		return lockedBy;
	}

	public void setLockedBy(JSONObject lockedBy) {
		this.lockedBy = lockedBy;
	}
}
