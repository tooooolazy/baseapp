package com.tooooolazy.data.interfaces;

import java.util.List;

import com.tooooolazy.data.services.beans.UserBean;
import com.tooooolazy.util.Credentials;
import com.vaadin.server.SessionExpiredException;

/**
 * @author gpatoulas
 *
 * @param <RE> - RoleEnum class
 * @param <UB> - UserBean class
 */
public interface UserControllerService<RE, UB> {
	public UB login(Credentials credentials) throws Exception;
	public void getUserRoles(UserBean<RE> ub) throws Exception;
	
	/**
	 * The idea is to return the User that just logged out
	 * @param user
	 * @return
	 * @throws SessionExpiredException 
	 */
	public Object logout(Object userPK) throws SessionExpiredException;

	public List getUsers();


}
