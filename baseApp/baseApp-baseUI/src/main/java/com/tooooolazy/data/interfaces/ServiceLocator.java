package com.tooooolazy.data.interfaces;



import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Used to create and cache required 'Service controllers' neede by the App
 * @author gpatoulas
 *
 */
public class ServiceLocator {
	private Map cache;
	private static ServiceLocator ourInstance = new ServiceLocator();

	public static ServiceLocator get() {
		return ourInstance;
	}

	private ServiceLocator() {
		this.cache = Collections.synchronizedMap(new HashMap());
	}

	public Object lookupSrv(Class srvClass) {
		if (this.cache.containsKey(srvClass) && this.cache.get(srvClass) != null) {
			return this.cache.get(srvClass);
		} else {
			try {
				Object srvRef = createService( srvClass );
				this.cache.put(srvClass, srvRef);
				return srvRef;
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	private Object createService(Class srvClass) {
//		if (srvClass.equals(DataHandlerService.class))
//			return new DataHandler();
//
//		if (srvClass.equals(UserControllerService.class))
//			return new UserController();
//
//		if (srvClass.equals(SecurityControllerService.class))
//			return new SecurityController();
//
//		if (srvClass.equals(Icap_ControllerService.class))
//			return new Icap_Controller();

		return null;
	}

	public SecurityControllerService getSecurityController() {
		return (SecurityControllerService) get().lookupSrv(SecurityControllerService.class);
	}
	public UserControllerService getUserController() {
		return (UserControllerService) get().lookupSrv(UserControllerService.class);
	}
	public DataHandlerService getDataHandler() {
		return (DataHandlerService) get().lookupSrv(DataHandlerService.class);
	}
}
