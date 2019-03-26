package com.tooooolazy.data.services;


import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.tooooolazy.data.ServiceLocator;
import com.tooooolazy.data.interfaces.DataHandlerService;
import com.tooooolazy.data.interfaces.SecurityControllerService;
import com.tooooolazy.data.services.beans.UserBean;
import com.tooooolazy.interfaces.HasPrimaryKey;


/**
 * Uses {@link DataHandler} to retrieve Security definitions. Also provides helper methods for how has access to what
 * @author gpatoulas
 *
 * @param <UB>
 * @param <RE>
 */
public abstract class SecurityController<UB extends UserBean<RE>, RE> implements SecurityControllerService<UB, RE> {
	protected static List<Object[]> methodSecurityDefs;

	public static void clearSecurityDefs() {
		if (methodSecurityDefs != null) {
			methodSecurityDefs.clear();
			methodSecurityDefs = null;
		}
	}

	public DataHandlerService getDataHandler() {
		return (DataHandlerService)ServiceLocator.get().lookupSrv(DataHandlerService.class);
	}

	@Override
	public RE getRole(HasPrimaryKey pk) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List getRoles() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List getMethodSecurityDefs() {
		if (methodSecurityDefs == null) {
			methodSecurityDefs = new ArrayList<Object[]>();

			try {
				JSONObject jo = getDataHandler().getMethodSecurityDefs();
				if (jo != null) {
//						LogManager.getLogger().info("MSLDs: " + tor.getResultObject().toString());
					JSONArray ja =  (JSONArray)jo.get("DATA");
					for (int i=0; i<ja.length(); i++) {
						JSONArray _ja = (JSONArray)ja.get(i);
						try {
							methodSecurityDefs.add(new Object[] {getRoleByValue( _ja.getInt(0) ), _ja.getString(1), _ja.getString(2), _ja.getBoolean(3) });
						} catch (Exception e) {
							// data from DB!!
							methodSecurityDefs.add(new Object[] {getRoleByValue( _ja.getInt(0) ), _ja.getString(1), _ja.getString(2), _ja.getInt(3)==1?true:false });
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return methodSecurityDefs;
	}

	@Override
	public List getObjectSecurityDefs() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasAccess(UB user, Method method, Class _class, Object[] params) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasAccess(UB user, String methodName, Class _class, Object[] params) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSecure(UB user, String methodName, Class methodClass) {
		// TODO Auto-generated method stub
		return false;
	}


}
