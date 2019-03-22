package com.dpapp.ws.beans;

import com.tooooolazy.data.services.beans.UserBean;

public class DpAppUserBean extends UserBean<RoleEnum> {

	@Override
	protected boolean isGodRole(RoleEnum re) {
		return false;
	}

	@Override
	protected boolean isAdminRole(RoleEnum re) {
		return re != null && re.isAdmin();
	}

}
