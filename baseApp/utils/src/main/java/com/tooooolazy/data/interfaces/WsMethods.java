package com.tooooolazy.data.interfaces;

/**
 * This interface defines the Base Ws methods that an application should support 
 * @author gpatoulas
 *
 */
public interface WsMethods {

	public static String ENVIRONMENT = "getEnvironment";

	public static String CLASS_TYPES = "getClassTypes";

	public static String MSECLEVELDEFS = "getMSecLevelDefs"; // security data

	public static String USERS = "getUsers";
	public static String USERS_ROLES = "getUsersRoles";
}
