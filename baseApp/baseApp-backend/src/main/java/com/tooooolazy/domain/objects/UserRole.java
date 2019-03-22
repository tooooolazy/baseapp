package com.tooooolazy.domain.objects;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "USERROLE")
@JsonIgnoreProperties({"accountRoles"})
public class UserRole implements java.io.Serializable {

	private static final long serialVersionUID = -6219077232944541739L;

	@Id
	@Column(name = "ROLECODE", unique = true, nullable = false)
	private Integer roleCode;
	@Column(name = "DESCRIPTION", nullable = false, length = 30)
	private String description;
	@Column(name = "USERINSERT", nullable = false, length = 30)
	private String userInsert;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TIMEINSERT", nullable = false, length = 26)
	private Date timeInsert;
//	@OneToMany(fetch = FetchType.LAZY, mappedBy = "userRole")
//	private Set<AccountRole> accountRoles = new HashSet<AccountRole>(0);

	public UserRole() {
	}

	public UserRole(String description, String userInsert, Date timeInsert) {
		this.description = description;
		this.userInsert = userInsert;
		this.timeInsert = timeInsert;
	}

	public Integer getRoleCode() {
		return this.roleCode;
	}

	public void setRoleCode(Integer roleCode) {
		this.roleCode = roleCode;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
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
