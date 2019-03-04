package com.tooooolazy.data.interfaces;

import java.lang.reflect.Method;
import java.util.List;

import com.tooooolazy.interfaces.HasPrimaryKey;


/**
 * @author gpatoulas
 *
 * @param <U> the 'User' class
 * @param <M> the Method level security class
 * @param <O> the Object level security class
 * @param <R> the Role class
 */
public interface SecurityControllerService<U, M, O, R> {
	public R getRole(HasPrimaryKey pk);
	public List getRoles();
	public List getMethodSecurityDefs();
	public List getObjectSecurityDefs();

	public boolean hasAccess(U user, Method method, Class _class, Object[] params);
	public boolean hasAccess(U user, String methodName, Class _class, Object[] params);
	/**
	 * Added for Vaadin 7 secure Views
	 * @param methodName
	 * @param methodClass
	 * @return
	 */
	public boolean isSecure(U user, String methodName, Class methodClass );

}
