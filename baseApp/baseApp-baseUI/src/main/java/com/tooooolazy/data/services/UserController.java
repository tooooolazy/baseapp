package com.tooooolazy.data.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.tooooolazy.data.ServiceLocator;
import com.tooooolazy.data.interfaces.DataHandlerService;
import com.tooooolazy.data.interfaces.SecurityControllerService;
import com.tooooolazy.data.interfaces.UserControllerService;
import com.tooooolazy.data.services.beans.UserBean;
import com.tooooolazy.util.Credentials;

public abstract class UserController<UB extends UserBean<RE>, RE> implements UserControllerService<RE, UB> {

	public DataHandlerService getDataHandler() {
		return (DataHandlerService)ServiceLocator.get().lookupSrv(DataHandlerService.class);
	}
	public SecurityControllerService<UB, RE> getSecurityHandler() {
		return (SecurityControllerService)ServiceLocator.get().lookupSrv(SecurityControllerService.class);
	}

	@Override
	public UB login(Credentials credentials) throws Exception {
		// TODO Auto-generated method stub
//		getDataHandler().login( credentials );
		UB ub = createUserBean(credentials);
		ub.setCredentials(credentials);
		// TODO get roles too and set them in UB
		return ub;
	}

	@Override
	public void getUserRoles(UB ub) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object logout(Object userPK) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List getUsers() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Helper to update UserBean with data retrieved from WS
	 * @param ub
	 * @param userJo
	 * @return
	 */
	protected UB updateUserBean(UB ub, JSONObject userJo) {
		JSONObject ua = userJo.optJSONObject("userAccount");

		ub.setUserCode( userJo.optInt("userCode") );
		ub.setFirstName( userJo.optString("firstName"));
		ub.setLastName( userJo.optString("lastName"));
		ub.setLastLogin( new Date( ua.optLong("realLastlogin") ) );

		setUserRoles(ub, ua.optJSONArray("accountRoles"));

		return ub;
	}

	protected void setUserRoles(UB ub, JSONArray ar) {
		List<RE> roles = new ArrayList<RE>();

		for (int r=0; r<ar.length(); r++) {
			JSONObject jo = ar.optJSONObject( r );
			roles.add( getSecurityHandler().getRoleByValue( jo.optJSONObject("id").optInt("roleCode")) );
		}
		ub.setRoles(roles);
	}
}
