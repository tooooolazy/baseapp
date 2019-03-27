package com.tooooolazy.data.services;

import java.util.List;

import com.tooooolazy.data.ServiceLocator;
import com.tooooolazy.data.interfaces.DataHandlerService;
import com.tooooolazy.data.interfaces.UserControllerService;
import com.tooooolazy.data.services.beans.UserBean;
import com.tooooolazy.util.Credentials;

public abstract class UserController<UB extends UserBean<RE>, RE> implements UserControllerService<RE, UB> {

	public DataHandlerService getDataHandler() {
		return (DataHandlerService)ServiceLocator.get().lookupSrv(DataHandlerService.class);
	}

	@Override
	public UB login(Credentials credentials) throws Exception {
		// TODO Auto-generated method stub
//		getDataHandler().login( credentials );
		return null;
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

}
