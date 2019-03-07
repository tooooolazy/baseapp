package com.tooooolazy.data;

import com.tooooolazy.data.interfaces.DataHandlerService;
import com.tooooolazy.data.interfaces.SecurityControllerService;
import com.tooooolazy.data.interfaces.UserControllerService;

/**
 * Used by {@link ServiceLocator} in order to generate the required Services.
 * Should be overridden in order to implement the service creation method
 *  
 * @author gpatoulas
 *
 */
public abstract class ServiceGenerator {

	/**
	 * Override this to create required services!!!
	 * @param srvClass
	 * @return
	 */
	public abstract Object createService(Class srvClass);// {
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

//		return null;
//	}

	public SecurityControllerService getSecurityController() {
		return (SecurityControllerService) ServiceLocator.get().lookupSrv(SecurityControllerService.class);
	}
	public UserControllerService getUserController() {
		return (UserControllerService) ServiceLocator.get().lookupSrv(UserControllerService.class);
	}
	public DataHandlerService getDataHandler() {
		return (DataHandlerService) ServiceLocator.get().lookupSrv(DataHandlerService.class);
	}
}
