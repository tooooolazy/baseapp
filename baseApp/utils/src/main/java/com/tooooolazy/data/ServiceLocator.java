package com.tooooolazy.data;


import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Used to create and cache required 'Service controllers' needed by the App.
 * 
 * @author gpatoulas
 *
 */
public class ServiceLocator {
	private Map cache;
	private static ServiceLocator ourInstance = new ServiceLocator();
	private ServiceGenerator sg;

	public static ServiceLocator get() {
		return ourInstance;
	}
	public void setGenerator(ServiceGenerator sg) {
		this.sg = sg;
	}

	private ServiceLocator() {
		this.cache = Collections.synchronizedMap(new HashMap());
	}

	public Object lookupSrv(Class srvClass) {
		if ( sg == null ) {
			throw new RuntimeException("You must set the ServiceGenerator first by using 'setGenerator'");
		}
		if (this.cache.containsKey(srvClass) && this.cache.get(srvClass) != null) {
			return this.cache.get(srvClass);
		} else {
			try {
				Object srvRef = sg.createService( srvClass );
				this.cache.put(srvClass, srvRef);
				return srvRef;
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

}
