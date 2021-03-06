package com.tooooolazy.domain;

import java.util.Date;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.tooooolazy.data.interfaces.OnlineKeys;
import com.tooooolazy.domain.components.DataHandlerHelper;
import com.tooooolazy.domain.components.MenuEditor;
import com.tooooolazy.domain.components.PasswordManager;
import com.tooooolazy.domain.components.UserHelper;
import com.tooooolazy.domain.objects.AppSession;
import com.tooooolazy.domain.objects.User;
import com.tooooolazy.domain.objects.UserAccount;
import com.tooooolazy.util.Credentials;
import com.tooooolazy.util.exceptions.InvalidCredentialsException;
import com.tooooolazy.util.exceptions.MultipleLoginException;
import com.tooooolazy.util.exceptions.UserInactiveException;
import com.tooooolazy.ws.WsBaseDataHandler;


/**
 * This is where {@link WsBaseDataHandler} delegates everything (the base class anyway).
 * <p>Like {@link WsBaseDataHandler}, it should be overridden. Also '@Repository("dataRepository")' must be used on derived class. The derived class should be autowired in {@link WsBaseDataHandler}'s subclass</p>
 * <p>Here (and of course in every derived class) is where all the WS methods will be created</p>
 * @author gpatoulas
 *
 */
//@Repository("dataRepository")
public abstract class DataBaseRepository extends AbstractJDBCRepository {
	public static String DEFAULT_USER_INSERT = "SYSTEM";
	public static String DEFAULT_DEPLOY_ENV_KEY = "deploy.environment";

	@Autowired
	protected DataHandlerHelper dhh;

    @Autowired
	protected MenuEditor menuEditor;
	@Autowired
	protected UserHelper userHelper;
	@Autowired
	protected PasswordManager passwordManager;

	@Autowired
	protected AppSessionRepository appSessionRepository;
	@Autowired
	protected AppLockRepository appLockRepository;
	@Autowired
	protected UserAccountRepository userAccountRepository;


	public Object getEnvironment(Map params) {
		JSONObject jo = new JSONObject();
		jo.put("environment", env.getProperty( DEFAULT_DEPLOY_ENV_KEY ) );

		return jo.toMap();
	}

	public boolean inProd() {
		return env.getProperty( DEFAULT_DEPLOY_ENV_KEY ).equals("PROD");
	}
	public boolean inUAT() {
		return env.getProperty( DEFAULT_DEPLOY_ENV_KEY ).equals("UAT");
	}
	public boolean inPRE() {
		return env.getProperty( DEFAULT_DEPLOY_ENV_KEY ).equals("PRE");
	}


	public Object getClassTypes(Map params) {
		if (params != null) {
			return dhh.getClassTypes();
		}
		return "{}";
	}
	/////////////////////////
	// menu related methods
	public Object getMenuStructure(Map<String, Object> params) {
		return menuEditor.getMenuStructure(null, params);
	}

	/////////////////////////////////////
	// security related methods
	public Object getMSecLevelDefs(Map params) {
		return dhh.getMSecLevelDefs(null);
	}


	/**
	 * Default login action that assumes all user related data are stored in Application's database (ie no LDAP or anything like that). Also assumes Lock and Session support
	 * @param creds
	 * @return
	 * @throws InvalidCredentialsException
	 * @throws MultipleLoginException
	 * @throws UserInactiveException
	 */
	public User login(Credentials creds) throws InvalidCredentialsException, MultipleLoginException, UserInactiveException {
//		LogManager.getLogger().info( "Current sessions: " + irpSessionRepository.count() );
//		LogManager.getLogger().info( "Current Locks: " + irpLockRepository.count() );

    	String md5password = passwordManager.hashMC( creds.getPassword() );
		String schema = dhh.getSchema();

//		try {
//			Integer uc = getDataSource().queryForObject("select usercode from " + schema + ".USERACCOUNT where username = ? and password = ?", Integer.class, creds.getUsername(), md5password);
//			AppSession is = irpSessionRepository.find( uc );
//
//			if ( is == null ) {
//				ApplicationParameter ap = apr.findParam(ApplicationParameterType.GENERAL, 1, 6); // EVALUATOR param
//				User u = userRepository.find( uc );
//
//				if ( ap.getParameterValueInt() == 0 ) {
//					if ( u.getUserAccount().hasRole( 7 ) ) {
//						// Evaluators should not login!
//						LogManager.getLogger().warn("Evaluator Cannot login. Evaluator flow NOT enabled");
//						throw new UserInactiveException( creds );
//					}
//					if ( u.getPosition() > 0 &&
//							( u.getUserAccount().hasRole( 3 ) || u.getUserAccount().hasRole( 4 ) || u.getUserAccount().hasRole( 5 ) )) {
//						LogManager.getLogger().warn("Branch User Cannot login. Evaluator flow NOT enabled");
//						throw new UserInactiveException( creds );
//					}
//				} else {
//					if ( ap.getFailCode() != 1 ) {
//						if ( u.getPosition() > 0 &&
//								( u.getUserAccount().hasRole( 3 ) || u.getUserAccount().hasRole( 4 ) || u.getUserAccount().hasRole( 5 ) )) {
//							LogManager.getLogger().warn("Branch User Cannot login. Branches in Evaluator flow NOT enabled");
//							throw new UserInactiveException( creds );
//						}
//					}
//				}
//				irpSessionRepository.create(creds.getFromIp(), uc);
//				u.getUserAccount().setLastlogin( new Date() );
//				userAccountRepository.persist( u.getUserAccount() );
//				dhh.logLogin(creds.getUsername(), "AUTHENTICATION_SUCCEEDED", null, null, null, null);
//				return u;
//			} else {
//				throw new MultipleLoginException();
//			}
//		} catch (Exception e) {
//			if ( e instanceof UserInactiveException) {
//				dhh.logLogin(creds.getUsername(), "USER_INACTIVE", null, null, null, null);
//				throw e;
//			}
//			if ( e instanceof MultipleLoginException) {
//				dhh.logLogin(creds.getUsername(), "ALREADY_LOGGED_IN", null, null, null, null);
//				throw e;
//			}
//			dhh.logLogin(creds.getUsername(), "AUTHENTICATION_FAILED", null, null, null, null);
//			throw new InvalidCredentialsException(creds);
//		}
		return null;
	}
	public Object logoutUser(UserAccount ua, Map params) {
		LogManager.getLogger().info( "Sessions before logout: " + appSessionRepository.count() );
		LogManager.getLogger().info( "Locks before logout: " + appLockRepository.count() );

		AppSession as = appSessionRepository.find( ua.getUserCode() );
		appSessionRepository.remove( as );
		appLockRepository.removeLocksOf( ua );

		Boolean auto = (Boolean)params.get("autoLogout");
		dhh.logLogin(ua.getUsername(), auto ? "AUTO_LOGOUT_SUCCEEDED" : "LOGOUT_SUCCEEDED", params);

		return ua;
	}

	/**
	 * This will be called if username exists in local DB
	 * @param ua
	 * @param params
	 * @return
	 * @throws MultipleLoginException 
	 */
	public User loginUserExternal(UserAccount ua, Map params) throws MultipleLoginException {
		LogManager.getLogger().info( "Sessions before login: " + appSessionRepository.count() );
		LogManager.getLogger().info( "Locks before login: " + appLockRepository.count() );

		AppSession is = appSessionRepository.find( ua.getUserCode() );
		Date currentLastLogin = null;
		if ( is == null ) {
			appSessionRepository.create((String)params.getOrDefault("address", null), ua.getUserCode());
			currentLastLogin = ua.getLastlogin();
			ua.setLastlogin( new Date() );
			ua.setRealLastlogin(currentLastLogin); // so UI knows the actual last login
			userAccountRepository.persist( ua );
		} else {
			dhh.logLogin(ua.getUsername(), "ALREADY_LOGGED_IN", params);
			throw new MultipleLoginException();
		}
		dhh.logLogin(ua.getUsername(), "AUTHENTICATION_SUCCEEDED", params);
		User u = ua.getUser();
		return u;
	}
	/**
	 * This one will be called if username does not exist in local DB and needs to be created!
	 * @param params
	 * @return
	 * @throws MultipleLoginException 
	 */
	public User loginUserExternal(Map params) throws MultipleLoginException {
		String username = (String) params.get("username");
		params.put("externalLogin", true);
		int userCount = userHelper.getUserCount();
		if ( userCount == 0 ) {
			params.put("role", 2); // set as Admin
		}
		User u = userHelper.createNewUser(null, params);

		dhh.logLogin(username, "AUTO_USER_CREATE", params);
		u.getUserAccount().setUser( u );
		return loginUserExternal(u.getUserAccount(), params);
	}

	
	public Object getUsersRoles(Map params) {
		String username = null;

		if (params != null)
			username = (String) params.get("username");

		return userHelper.getUserRoles( username );
	}
	public Object getUsers(Map params) {
		return userHelper.getUsers();
	}
	public Object updateUserRole(UserAccount ua, Map params) throws IllegalAccessException {
		if ( params == null || params.isEmpty() )
			throw new IllegalAccessException("Required parameters not defined!");

		try {
			int userCode = (Integer)params.get("user.code");
			int roleCode = (Integer)params.get("role.code");
			boolean assigned = (Boolean)params.get("role.assigned");
			int res = userHelper.updateUserRole( userCode, roleCode, assigned, ua );
			if ( res > 0 ) {
				dhh.logLogin(ua.getUsername(), "updateUserRole-" + userCode + "-" + roleCode + "-" + assigned, params);
				return createAllOkKSON().toMap();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new IllegalAccessException( e.getMessage() );
		}
		throw new IllegalAccessException("Nothing to update for the given params");
	}
	public Object updateUser(UserAccount ua, Map params) {
		try {
			int userCode = (Integer)params.get("user.code");
			String firstName = (String)params.get("firstName");
			String lastName = (String)params.get("lastName");
			int res = userHelper.updateUser( userCode, firstName, lastName, ua );
			if ( res > 0 ) {
				return createAllOkKSON().toMap();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException( e.getMessage() );
		}
		throw new RuntimeException();
	}

	/**
	 * Helper for result object
	 * @return
	 */
	protected JSONObject createAllOkKSON() {
		JSONObject jo = new JSONObject();
		jo.put(OnlineKeys.RESULT, "OK");
		return jo;
	}

}
