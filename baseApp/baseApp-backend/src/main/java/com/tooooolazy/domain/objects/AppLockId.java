package com.tooooolazy.domain.objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class AppLockId implements java.io.Serializable {

	@Column(name = "LOCK_TYPE", nullable = false)
	private int lockType;
	@Column(name = "LOCK_ITEM_ID", nullable = false)
	private int lockItemId;

	public AppLockId() {
		
	}
	public AppLockId(int lockType, int lockItemId) {
		this.lockType = lockType;
		this.lockItemId = lockItemId;
	}

	public int getLockType() {
		return lockType;
	}
	public void setLockType(int lockType) {
		this.lockType = lockType;
	}
	public int getLockItemId() {
		return lockItemId;
	}
	public void setLockItemId(int lockItemId) {
		this.lockItemId = lockItemId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof AppLockId))
			return false;
		AppLockId castOther = (AppLockId) other;

		return (this.getLockType() == castOther.getLockType() )
				&& (this.getLockItemId() == castOther.getLockItemId());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getLockType();
		result = 37 * result + this.getLockItemId();
		return result;
	}
}
