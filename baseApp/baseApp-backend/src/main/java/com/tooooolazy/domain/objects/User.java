package com.tooooolazy.domain.objects;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.tooooolazy.domain.enums.UserStatus;

/**
 * User generated by hbm2java
 */
@Entity
@Table(name = "USERS")
public class User implements java.io.Serializable {

	@Id 
	@Column(name = "USERCODE", unique = true, nullable = false)
	private int userCode;
	@Column(name = "LASTNAME", length = 40)
	private String lastName;
	@Column(name = "FIRSTNAME", length = 20)
	private String firstName;
	@Column(name = "STATUSCODE")
	private UserStatus status;
	@Column(name = "EMAIL", length = 80)
	private String email;
	@Column(name = "TELEPHONE", length = 20)
	private String telephone;
	@Column(name = "POSITION")
	private int position;
	@Column(name = "USERINSERT", nullable = false, length = 30)
	private String userInsert;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TIMEINSERT", nullable = false, length = 26)
	private Date timeInsert;
	@Column(name = "USERUPDATE", nullable = false, length = 30)
	private String userUpdate;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TIMEUPDATE", nullable = false, length = 26)
	private Date timeUpdate;
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn
	private UserAccount userAccount;

	public User() {
	}

	public User(int userCode, String lastName, String firstName,
			UserStatus status, String email, int position,
			String userInsert, Date timeInsert, String userUpdate,
			Date timeUpdate) {
		this.userCode = userCode;
		this.lastName = lastName;
		this.firstName = firstName;
		this.status = status;
		this.email = email;
		this.position = position;
		this.userInsert = userInsert;
		this.timeInsert = timeInsert;
		this.userUpdate = userUpdate;
		this.timeUpdate = timeUpdate;
	}

	public User(int userCode, String lastName, String firstName,
			UserStatus status, String email, String telephone, int position,
			String userInsert, Date timeInsert, String userUpdate,
			Date timeUpdate, UserAccount userAccount) {
		this.userCode = userCode;
		this.lastName = lastName;
		this.firstName = firstName;
		this.status = status;
		this.email = email;
		this.telephone = telephone;
		this.position = position;
		this.userInsert = userInsert;
		this.timeInsert = timeInsert;
		this.userUpdate = userUpdate;
		this.timeUpdate = timeUpdate;
		this.userAccount = userAccount;
	}

	public int getUserCode() {
		return this.userCode;
	}

	public void setUserCode(int userCode) {
		this.userCode = userCode;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public UserStatus getStatus() {
		return this.status;
	}

	public void setStatus(UserStatus status) {
		this.status = status;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelephone() {
		return this.telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public int getPosition() {
		return this.position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public String getUserInsert() {
		return this.userInsert;
	}

	public void setUserInsert(String userInsert) {
		this.userInsert = userInsert;
	}

	public Date getTimeInsert() {
		return this.timeInsert;
	}

	public void setTimeInsert(Date timeInsert) {
		this.timeInsert = timeInsert;
	}

	public String getUserUpdate() {
		return this.userUpdate;
	}

	public void setUserUpdate(String userUpdate) {
		this.userUpdate = userUpdate;
	}

	public Date getTimeUpdate() {
		return this.timeUpdate;
	}

	public void setTimeUpdate(Date timeUpdate) {
		this.timeUpdate = timeUpdate;
	}

	public UserAccount getUserAccount() {
		return this.userAccount;
	}

	public void setUserAccount(UserAccount userAccount) {
		this.userAccount = userAccount;
	}

}
