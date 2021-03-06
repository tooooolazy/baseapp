package com.tooooolazy.data.interfaces;

import java.util.List;

import com.tooooolazy.util.Credentials;

/**
 * @author gpatoulas
 *
 * @param <RE> - RoleEnum class
 * @param <UB> - UserBean class
 */
public interface UserControllerService<RE, UB> {
	public UB login(Credentials credentials) throws Exception;
	public void getUserRoles(UB ub) throws Exception;
	
	/**
	 * The idea is to return the User that just logged out
	 * @param user
	 * @return
	 */
	public Object logout(Object userPK);

	public List getUsers();

	public UB createUserBean(Credentials credentials);
}
