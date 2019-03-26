package com.tooooolazy.data.services;


import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
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
		if (user != null) {
			if (user.isGod())
				return true;
		}

		if ( hasMethodAccess (user, method, _class) )
			return hasObjectAccess(user, params);

		return false;
	}

	@Override
	public boolean hasAccess(UB user, String methodName, Class _class, Object[] params) {
		if (user != null) {
			if (user.isGod())
				return true;
		}

		if ( hasMethodAccess (user, methodName, _class.getName()) )
			return hasObjectAccess(user, params);

		return false;
	}

	@Override
	public boolean isSecure(UB user, String methodName, Class methodClass) {
		return getAnyUserMethodSecurityDefs(methodName, methodClass.getName()) != null;
	}



	public Object[] getAnyUserMethodSecurityDefs(String methodName, String className) {
		List<Object[]> mlsds = getMethodSecurityDefs();

		Object[] sDef = null;
	
		for (Object[] mlsd : mlsds) {
			if ( mlsd[1].equals(methodName) && mlsd[2].equals(className) && getNotLoggedInRole().equals(((RE)mlsd[0])) && !(Boolean)mlsd[3] ) {
				sDef = mlsd;
			}
		}

		return sDef;
	}

	protected boolean hasObjectAccess(UserBean user, Object[] params) {
		if (params == null)
			return true;
		// first see if we have an appropriate object
		HasPrimaryKey obj = null;
		for (int i=0; i<params.length; i++) {
			if (params[i] instanceof HasPrimaryKey) {
				obj = (HasPrimaryKey)params[i];
			}
		}

//		// if we have one check for access rights
//		if (obj != null) {
//			// check if any security is defined for the object first
//			/* 2 ways to do it:
//			 * - use a query (less code)
//			 * - go through data already retrieved form DB (should be faster?? and no extra query!)
//			*/
//			Query q = getEntityManager().createQuery("select osld from ObjectSecurityLevelDef osld where typeClass.className = :classname and id.objectPk = :pk ");
//			q.setParameter("classname", obj.getDbEntityClass());
//			q.setParameter("pk", SerializationUtils.serialize((Serializable)obj.getPK()) );
//	
//			// if there are none grand access
//			if (q.getResultList().size() == 0) {
//				LogManager.getLogger().info("No access rules defined for object: " + obj.getDbEntityClass() + ". Access Granted!");
//				return true;
//			}
//
//			// else for the user to have access he must have a security definition in his list for the given object!
//			/* 2 ways to do it:
//			 * - go through all of users security defs and check only those referencing given object
//			 * - retrieve only those defs referencing given object (loop will be smaller)
//			 */
//			List<ObjectSecurityLevelDef> osDefs = getUserObjectSecurityDefs(user, obj);
//
//			boolean hasAccess = false;
//			Iterator<ObjectSecurityLevelDef> it = osDefs.iterator();
//			while (it.hasNext()) {
//				ObjectSecurityLevelDef osld = it.next();
//				Object hpk = osld.getObjectPk();
//				if ( osld.getTypeClass().getClassName().equals(obj.getDbEntityClass()) )  {
//					if ( obj.getPK().equals( hpk ) ) {
//						// we might have conflicting rules, one saying allow and one say not allow
//						// so even one 'not allow' rule exist then access should not be granted
//						if (!osld.getAllow()) {
//							hasAccess =  false;
//							break;
//						} else
//							hasAccess = true;
//					}
//				}
//			}
//			return hasAccess;
//		}
		// else no need to check access rights
		return true;
	}
	protected boolean hasMethodAccess(UB user, Method method, Class _class) {
		if (method == null)
			return true;
		String methodName = method.getName();
		String methodClass = _class.getName();

		return hasMethodAccess(user, methodName, methodClass);
	}
	protected boolean hasMethodAccess(UB user, String methodName, String methodClass) {
		// check if any method security is defined first
		/* 2 ways to do it:
		 * - use a query (less code)
		 * - go through data already retrieved form DB (should be faster?? and no extra query!)
		*/
		List<Object[]> mlsds = getMethodSecurityDefs(user, methodName, methodClass, null);

		if (mlsds.isEmpty()) {
//			LogManager.getLogger().info("No access rules defined for method: " + methodName + " of class: " + methodClass + ". Access Granted!");
			return true;
		}

		List<Object[]> mlsds_deny = getMethodSecurityDefs(user, methodName, methodClass, false);
		List<Object[]> mlsds_allow = getMethodSecurityDefs(user, methodName, methodClass, true);

		boolean hasAccess = false;
		if (mlsds_allow.isEmpty() && !mlsds_deny.isEmpty()) { // the 2nd part of where is not really needed --> It is implied!
			// this means access is denied ONLY for roles where allow=false!!
			hasAccess = true;
//			LogManager.getLogger().info("Only NOT allow rules defined for method: " + methodName + " of class: " + methodClass + "....");
//			return true;
		}
//		// else for the user to have access he must have a security definition in his list for the given method!
//		/* 2 ways to do it:
//		 * - go through all of users security defs and check only those referencing given method
//		 * - retrieve only those defs referencing given method (loop will be smaller)
//		 */
		List<Object[]> umlsds = getUserMethodSecurityDefs(user);

		for (Object[] umsld : umlsds) {
			if (umsld[2].equals(methodClass) && umsld[1].equals( methodName ) ) {
				// we might have conflicting rules, one saying allow and one say not allow
				// so even one 'not allow' rule exist then access should not be granted
				if (!(Boolean)umsld[3]) {
					hasAccess =  false;
					break;
				} else
					hasAccess = true;
			}
		}
//		LogManager.getLogger().info("user " + (user.getCredentials()==null?"-":user.getCredentials().getUsername()) + " has access to method: " + methodName + " of class: " + methodClass + " --> " + hasAccess);

		return hasAccess;
	}
	protected List<Object[]> getMethodSecurityDefs(UB user, String methodName, String className, Boolean allow) {
		List<Object[]> mlsds = getMethodSecurityDefs();

		List<Object[]> sDefs = new ArrayList<Object[]>();

		for (Object[] mlsd : mlsds) {
			if ( mlsd[1].equals(methodName) && mlsd[2].equals(className) && !getNotLoggedInRole().equals(((RE)mlsd[0]))) {
				if (allow == null || allow.equals( (Boolean)mlsd[3] ))
					sDefs.add(mlsd);
			}
		}
		return sDefs;
	}

	protected List<Object[]> getUserMethodSecurityDefs(UserBean user) {
		List<Object[]> mlsds = getMethodSecurityDefs();

		List<Object[]> sDefs = new ArrayList<Object[]>();

		Iterator<RE> userRoles = user.getRoles().iterator();
		while (userRoles.hasNext()) {
			RE role = userRoles.next();
			for (Object[] mlsd : mlsds) {
				if ( role.equals(((RE)mlsd[0])) ) {
					sDefs.add(mlsd);
				}
			}
		}
		return sDefs;
	}
}
