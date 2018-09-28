package com.tooooolazy.util;

/**
 * @author tooooolazy
 *
 */
public class RegistrationData extends Credentials {
	private String passwordVerify;
	private String email;

	public RegistrationData() {
		super();
	}
	
	public RegistrationData(Credentials userInfo) {
		super(userInfo);
	}
	public RegistrationData(RegistrationData userInfo) {
		super(userInfo);
		if (userInfo == null)
			return;
		passwordVerify = userInfo.getPasswordVerify();
		email = userInfo.getEmail();
	}
	public String getPasswordVerify() {
		return passwordVerify;
	}

	public void setPasswordVerify(String passwordVerify) {
		this.passwordVerify = passwordVerify;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		if (email != null && email.trim().length() > 3)
			this.email = email;
	}

	public Credentials getCredentials() {
		return new Credentials(this);
	}
	public String toString() {
		return Messages.getString("username") + ": " + getUsername() + "<br/>"
			 + Messages.getString("password") + ": " + getPassword();
	}
}
