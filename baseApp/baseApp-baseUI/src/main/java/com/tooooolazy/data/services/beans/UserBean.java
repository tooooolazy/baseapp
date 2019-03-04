package com.tooooolazy.data.services.beans;

import java.util.ArrayList;
import java.util.List;

import com.tooooolazy.util.Credentials;
//import gr.icap.online.enums.BusinessUnit;

/**
 * @author gpatoulas
 *
 * @param <RE> - RoleEnum specific to each application
 */
public class UserBean<RE> {
	protected Credentials credentials;
	protected int userCode;
	protected List<RE> roles;
	private boolean isGod;
	protected boolean isDemo;

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

//	public boolean isGod() {
//		for (RoleEnum re : getRoles()) {
//			if (re != null && re.isGod()) {
//				isGod = true;
//				return true;
//			}
//		}
//		return false;
//	}
//	public boolean isAdmin() {
//		for (RoleEnum re : getRoles()) {
//			if (re.isAdmin())
//				return true;
//		}
//		return false;
//	}
}
