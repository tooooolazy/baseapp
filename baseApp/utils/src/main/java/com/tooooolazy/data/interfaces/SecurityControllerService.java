package com.tooooolazy.data.interfaces;

import java.lang.reflect.Method;
import java.util.List;

import com.tooooolazy.data.services.beans.UserBean;
import com.tooooolazy.interfaces.HasPrimaryKey;


/**
 * @author gpatoulas
 *
 * @param <UB> the 'User' class
 * @param <M> the Method level security class
 * @param <O> the Object level security class
 * @param <RE> the Role class
 */
public interface SecurityControllerService<UB extends UserBean<RE>, RE> {
	public RE getRole(HasPrimaryKey pk);
	public RE getRoleByValue(int rv);
	public List getRoles();
	public List getMethodSecurityDefs();
	public List getObjectSecurityDefs();

	public boolean hasAccess(UB user, Method method, Class _class, Object[] params);
	public boolean hasAccess(UB user, String methodName, Class _class, Object[] params);
	/**
	 * Added for Vaadin 7 secure Views
	 * @param methodName
	 * @param methodClass
	 * @return
	 */
	public boolean isSecure(UB user, String methodName, Class methodClass );

}
