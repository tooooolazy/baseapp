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
@Table(name = "ACCOUNTROLE")
@JsonIgnoreProperties({"userAccount","userRole"})
public class AccountRole implements java.io.Serializable {

	@EmbeddedId
	@AttributeOverrides( {
		@AttributeOverride(name = "userCode", column = @Column(name = "USERCODE", nullable = false)),
		@AttributeOverride(name = "roleCode", column = @Column(name = "ROLECODE", nullable = false)) })
	private AccountRoleId id;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USERCODE", nullable = false, insertable = false, updatable = false)
	private UserAccount userAccount;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ROLECODE", nullable = false, insertable = false, updatable = false)
	private UserRole userRole;
	@Column(name = "USERINSERT", nullable = false, length = 30)
	private String userInsert;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TIMEINSERT", nullable = false, length = 26)
	private Date timeInsert;

	public AccountRole() {
	}

	public AccountRole(AccountRoleId id, UserAccount userAccount,
			UserRole userRole, String userInsert, Date timeInsert) {
		this.id = id;
		this.userAccount = userAccount;
		this.userRole = userRole;
		this.userInsert = userInsert;
		this.timeInsert = timeInsert;
	}

	public AccountRoleId getId() {
		return this.id;
	}

	public void setId(AccountRoleId id) {
		this.id = id;
	}

	public UserAccount getUserAccount() {
		return this.userAccount;
	}

	public void setUserAccount(UserAccount userAccount) {
		this.userAccount = userAccount;
	}

	public UserRole getUserRole() {
		return this.userRole;
	}

	public void setUserRole(UserRole userRole) {
		this.userRole = userRole;
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

}
