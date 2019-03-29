package com.tooooolazy.data.services.beans;

import java.util.List;

public class UserRoleBean {

	protected Integer roleCode;
	protected Boolean assigned;
	protected String username, firstName, lastName;
	protected List<UserRoleBean> userRoles;


	public UserRoleBean() {
	}
	public UserRoleBean(Integer roleCode, String username) {
		this.roleCode = roleCode;
		this.username = username;
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
}
