package com.tooooolazy.data.services.beans;

import java.util.List;

public class UserRoleBean {

	protected Integer roleCode;
	protected Integer userCode;
	protected Boolean assigned;
	protected String username, firstName, lastName;
	protected List<UserRoleBean> userRoles;
	protected UserRoleBean parent;


	public UserRoleBean() {
	}
	public UserRoleBean(UserRoleBean parent) {
		this.parent = parent;
	}
	public UserRoleBean(Integer roleCode, String username) {
		this.roleCode = roleCode;
		this.username = username;
	}

	public Integer getUserCode() {
		return userCode;
	}
	public void setUserCode(Integer userCode) {
		this.userCode = userCode;
	}
	public Integer getRoleCode() {
		return roleCode;
	}
	public void setRoleCode(Integer roleCode) {
		this.roleCode = roleCode;
	}
	public Boolean getAssigned() {
		return assigned;
	}
	public void setAssigned(Boolean assigned) {
		this.assigned = assigned;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}

	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public List<UserRoleBean> getUserRoles() {
		return userRoles;
	}
	public void setUserRoles(List<UserRoleBean> userRoles) {
		this.userRoles = userRoles;
	}
	public UserRoleBean getParent() {
		return parent;
	}
	public void setParent(UserRoleBean parent) {
		this.parent = parent;
	}
}
