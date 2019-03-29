package com.tooooolazy.data.services.beans;

import java.util.ArrayList;
import java.util.List;

import com.tooooolazy.util.Credentials;

/**
 * @author gpatoulas
 *
 * @param <RE> - RoleEnum specific to each application
 */
public abstract class UserBean<RE> {
	protected Credentials credentials;
	protected int userCode;
	protected List<RE> roles;
	private boolean isGod;
	protected boolean isDemo;

	protected String firstName, lastName;

	public boolean isDemo() {
		return isDemo;
	}
	public void setDemo(boolean isDemo) {
		this.isDemo = isDemo;
	}


	public List<RE> getRoles() {
		if (roles == null)
			roles = new ArrayList<RE>();
		return roles;
	}
	public void setRoles(List<RE> roles) {
		this.roles = roles;
	}
	public Credentials getCredentials() {
		return credentials;
	}
	public void setCredentials(Credentials credentials) {
		this.credentials = credentials;
	}
	
	public int getUserCode() {
		return userCode;
	}
	public void setUserCode(int userCode) {
		this.userCode = userCode;
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

	public boolean isGod() {
		for (RE re : getRoles()) {
			if ( isGodRole( re )) {
				isGod = true;
				return true;
			}
		}
		return false;
	}
	public boolean isAdmin() {
		for (RE re : getRoles()) {
			if ( isAdminRole( re ) ) {
				return true;
			}
		}
		return false;
	}
	protected abstract boolean isGodRole(RE re);
	protected abstract boolean isAdminRole(RE re);
}
