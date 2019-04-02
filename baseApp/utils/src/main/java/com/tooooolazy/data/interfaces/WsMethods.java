package com.tooooolazy.data.interfaces;

/**
 * This interface defines the Base Ws methods that an application should support 
 * @author gpatoulas
 *
 */
public interface WsMethods {

	public static String ENVIRONMENT = "getEnvironment";

	public static String CLASS_TYPES = "getClassTypes";
	public static String MENU_STRUCTURE = "getMenuStructure";

	public static String MSECLEVELDEFS = "getMSecLevelDefs"; // security data

	public static String USERS = "getUsers";
	public static String USERS_ROLES = "getUsersRoles";
	public static String USER_ROLE_UPDATE = "updateUserRole";
	/**
	 * Use this to call a WS to perform DB related cleanup
	 */
	public static String LOGOUT_USER = "logoutUser";
	public static String LOGIN_USER = "loginUser";
	public static String LOGIN_USER_EXTERNAL = "loginUserExternal";
}
