package com.tooooolazy.domain.objects;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "APP_SESSIONS")
public class AppSession implements java.io.Serializable {

	@Id
	@Column(name = "USER_CODE", unique = true, nullable = false)
	private Integer userCode;

	@Column(name = "ADDRESS")
	private String ipAddress;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TIMEINSERT", nullable = false, length = 26)
	private Date timeInsert;

	public Integer getUserCode() {
		return userCode;
	}
	public void setUserCode(Integer userCode) {
		this.userCode = userCode;
	}

	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public Date getTimeInsert() {
		return timeInsert;
	}
	public void setTimeInsert(Date timeInsert) {
		this.timeInsert = timeInsert;
	}
}
