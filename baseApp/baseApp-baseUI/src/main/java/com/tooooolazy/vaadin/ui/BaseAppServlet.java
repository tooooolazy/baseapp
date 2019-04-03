package com.tooooolazy.vaadin.ui;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;

import org.json.JSONObject;

import com.tooooolazy.data.ServiceLocator;
import com.tooooolazy.data.interfaces.WsMethods;
import com.tooooolazy.data.services.beans.OnlineResult;
import com.tooooolazy.data.services.beans.UserBean;
import com.vaadin.server.CustomizedSystemMessages;
import com.vaadin.server.SessionDestroyEvent;
import com.vaadin.server.SessionDestroyListener;
import com.vaadin.server.SystemMessages;
import com.vaadin.server.SystemMessagesInfo;
import com.vaadin.server.SystemMessagesProvider;
import com.vaadin.server.VaadinServlet;

/**
 * Extends standard VaadinServlet in order to override default SystemMessages and add a SessionDestroyListener to handle auto logout on session timeout.
 * <p>Shoud be used as base servlet in each App UI (see AppUI class in baseApp-ui module as an example)
 * @author gpatoulas
 *
 */
public class BaseAppServlet extends VaadinServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Thread applicationMonitor;

	@Override
	protected void servletInitialized() throws ServletException {
		getService().setSystemMessagesProvider(
				new SystemMessagesProvider() {
					private static final long serialVersionUID = 1L;

					@Override
					public SystemMessages getSystemMessages(SystemMessagesInfo systemMessagesInfo) {
						CustomizedSystemMessages customizedSystemMessages = new CustomizedSystemMessages();

						customizedSystemMessages.setSessionExpiredNotificationEnabled(false);
						customizedSystemMessages.setSessionExpiredMessage(null);
						customizedSystemMessages.setSessionExpiredCaption(null);
						customizedSystemMessages.setSessionExpiredURL(null);

						return customizedSystemMessages;
					}
				});
		System.out.println("Custom Mesages set...");

		getService().addSessionDestroyListener( new SessionDestroyListener() {
			@Override
			public void sessionDestroy(SessionDestroyEvent event) {
				// attributes should still be there!
				UserBean user = (UserBean)event.getSession().getAttribute("user");
				if ( user != null ) {
					try {
						JSONObject joParams = BaseUI.get().addMainLogActionParams(null);
						joParams.put("autoLogout", true);
						OnlineResult or = (OnlineResult) ServiceLocator.getServices().getDataHandler()
								.getData(WsMethods.LOGOUT_USER, user, false, joParams.toMap(), true);
						System.out.println( or.getFailCode() );
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} 
			}
		});
		System.out.println("Session Destroy Listener set...");

//		applicationMonitor = new Thread() {
//			@Override
//		    public void run() {
//				ApplicationLevelTracker alt;
//			}
//		};
		super.servletInitialized();
	}

}
