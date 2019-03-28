package com.tooooolazy.data.services;

import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Properties;

public class ServicesContext {
	public static String CONFIG_FILE = "config/services-config-{0}{1}.properties";

	static ServicesContext singleton;
	
	static Properties context = null;

	public static synchronized ServicesContext singleton() {
		return singleton;
	}

    public static String getProperty( String key ) {
    	if ( context==null ) {
    		try {
				initialize();
			} 
    		catch (Exception e) {
				e.printStackTrace();
			}
    	}
    	
    	return context.getProperty( key );
    }

    public static Integer getIntegerProperty( String key ) {
    	Integer value = null;

		try {
			String propertyValue = getProperty( key );
			value = Integer.parseInt( propertyValue );
		} 
		catch (Exception e) {
			e.printStackTrace();
		}

    	return value;
    }
    
    public static Boolean getBooleanProperty( String key ) {
    	Boolean value = null;

		try {
			String propertyValue = getProperty( key );
			value = Boolean.parseBoolean( propertyValue );
		} 
		catch (Exception e) {
			e.printStackTrace();
		}

    	return value;
    }

    public static synchronized void initialize() throws Exception {
    	String env = getEnvironment();
    	String dbType = System.getenv("db.type");
    	if ( env == null)
    		env = "LOCAL";
    	if ( dbType == null )
    		dbType = "";
    	System.out.println( "--> Environment: " + env );
    	
    	initializeContext( MessageFormat.format( CONFIG_FILE, env, dbType ) );
    }
    public static String getEnvironment() {
    	String env = System.getenv("deploy.environment");
    	return env;
    }
	
	public static void initializeContext( String propFileName ) throws Exception {
		System.out.println( "--> Initializing the Services Client, propFileName:" +propFileName );
		
		initializeProperties( propFileName );

		singleton = new ServicesContext();
	}
	
	protected static ClassLoader getCurrentClassLoader(){
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		return loader;
	}
	private static void initializeProperties( String filename ) throws Exception {
		Properties props = null;
		
		try {
			InputStream input = getCurrentClassLoader().getResourceAsStream( filename );
			props = new Properties();
			props.load( input );
		}
		catch (Exception e) {
			throw new Exception( "Failed to read " + filename + ".", e );
		}

		context = props;
	}
}
