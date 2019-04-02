package com.tooooolazy.domain.objects;

import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "APP_LOCKS")
@JsonIgnoreProperties({"lockedBy"})
public class AppLock implements java.io.Serializable {

	@EmbeddedId
	@AttributeOverrides( {
		@AttributeOverride(name = "lockType", column = @Column(name = "LOCK_TYPE", nullable = false)),
		@AttributeOverride(name = "lockItemId", column = @Column(name = "LOCK_ITEM_ID", nullable = false)) })
	private AppLockId id;

//	@Column(name = "USER_CODE", nullable = false)
//	private int userCode;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_CODE", nullable = false, insertable = true, updatable = true)
	private UserAccount lockedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TIMEINSERT", nullable = false, length = 26)
	private Date timeInsert;

	public AppLockId getId() {
		return id;
	}
	public void setId(AppLockId id) {
		this.id = id;
	}
//
//	public int getUserCode() {
//		return userCode;
//	}
//	public void setUserCode(int userCode) {
//		this.userCode = userCode;
//	}

	public UserAccount getLockedBy() {
		return lockedBy;
	}
	public void setLockedBy(UserAccount lockedBy) {
		this.lockedBy = lockedBy;
	}

	public Date getTimeInsert() {
		return timeInsert;
	}
	public void setTimeInsert(Date timeInsert) {
		this.timeInsert = timeInsert;
	}

}
