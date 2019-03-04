package com.tooooolazy.vaadin.ui;

import java.lang.reflect.Method;
import java.util.Locale;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import com.tooooolazy.util.Credentials;
import com.tooooolazy.util.Messages;
import com.tooooolazy.util.TLZMail;
import com.tooooolazy.vaadin.exceptions.InvalidBaseAppParameterException;
import com.tooooolazy.vaadin.layout.ResponsiveMenuLayout;
import com.tooooolazy.vaadin.views.ErrorView;
import com.vaadin.external.org.slf4j.Logger;
import com.vaadin.external.org.slf4j.LoggerFactory;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.CustomizedSystemMessages;
import com.vaadin.server.DefaultErrorHandler;
import com.vaadin.server.Page.PopStateEvent;
import com.vaadin.server.Page.PopStateListener;
import com.vaadin.server.Resource;
import com.vaadin.server.Responsive;
import com.vaadin.server.SystemMessages;
import com.vaadin.server.SystemMessagesInfo;
import com.vaadin.server.SystemMessagesProvider;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinServletService;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Image;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;

import elemental.json.Json;
import elemental.json.JsonArray;
import elemental.json.JsonObject;

/**
 * <L> - Layout Class
 */
public abstract class BaseUI<L extends AppLayout> extends UI {
	protected final Logger logger = LoggerFactory.getLogger(UI.class.getName());

	protected final String SESSION_USER_KEY = "_user";

	protected L root;
	protected boolean hasSecureContent;

	// might not be needed
	protected ViewChangeListener vcl;

	static {
		try {
//			Messages.setMainBundle(Messages.class.getPackage().getName() + ".messages");
			Messages.addBundle(BaseUI.class.getPackage().getName() + ".messages");
		} catch (Exception e) {
			// no default resource bundles... but never mind
		}
	}

	@Override
	public void setLocale(Locale locale) {
		super.setLocale(locale);
		getSession().setLocale(locale); // needed so UI remembers the locale on browser refresh
		Messages.setLang(getUI().getLocale().getLanguage());
	}

	protected boolean useBrowserLocale() {
		return true;
	}

	protected Credentials cd;
	public void setCredentials(Credentials cd) {
		this.cd = cd;
	}
	public Credentials getCredentials() {
		return cd;
	}

	@Override
	protected void init(VaadinRequest vaadinRequest) {
		logger.info("Initializing BaseUI");

//    	VaadinSession.getCurrent().getSession().setMaxInactiveInterval(20);
		setupSystemMessages();

		setupErrorHandling();

		getPage().addPopStateListener(new PopStateListener() {

			@Override
			public void uriChanged(PopStateEvent event) {
				// TODO Auto-generated method stub
				logger.info( event.getPage().getUriFragment());
				
				ViewChangeEvent vce = new ViewChangeEvent(getNavigator(), getNavigator().getCurrentView(), getNavigator().getCurrentView(), getNavigator().getCurrentView().getClass().getSimpleName(), event.getUri());
//				navigator.getCurrentView().enter( vce );
			}
		});

		if (useBrowserLocale())
			try {
				Messages.setLang(getUI().getLocale().getLanguage());
			} catch (Exception e) {
				logger.info(getUI().getLocale().getLanguage() + " is not supported");
				setLocale( new Locale("en"));
			}

		Responsive.makeResponsive(this);
		root = createRootLayout();
//		viewDisplay = getContentContainer();
		setContent(root);
		addStyleName(ValoTheme.UI_WITH_MENU);

		setupNavigator();
	}

	public JsonArray getViewMenuRelations() {
		// TODO get this structure from WS or DB
		JsonArray ja = Json.createArray();
		
		return ja;
	}
	protected JsonArray getViewDefinitions() {
		// TODO get this structure from WS or DB
		JsonArray ja = Json.createArray();

		JsonObject jo = Json.createObject();
		jo.put(MenuItemKeys.VIEW_CLASS, "com.tooooolazy.vaadin.views.Dummy4View");
		jo.put(MenuItemKeys.VIEW_CLASS_ID, 1);
		jo.put(MenuItemKeys.VIEW_SECURE, false);
		jo.put(MenuItemKeys.VIEW_BADGE, "2");
		jo.put(MenuItemKeys.VIEW_SUB_TITLE, true);
		ja.set(0, jo);

		jo = Json.createObject();
		jo.put(MenuItemKeys.VIEW_CLASS, "com.tooooolazy.vaadin.views.AboutView");
		jo.put(MenuItemKeys.VIEW_CLASS_ID, 2);
		jo.put(MenuItemKeys.VIEW_CLASS_PARENT_ID, 3);
		jo.put(MenuItemKeys.VIEW_SECURE, false);
		jo.put(MenuItemKeys.VIEW_BADGE, "2");
		ja.set(1, jo);

		jo = Json.createObject();
		jo.put(MenuItemKeys.VIEW_CLASS, "com.tooooolazy.vaadin.views.Dummy1View");
		jo.put(MenuItemKeys.VIEW_CLASS_ID, 3);
//		jo.put(MenuItemKeys.VIEW_CLASS_PARENT_ID, 2);
		jo.put(MenuItemKeys.VIEW_BADGE, "22");
		jo.put(MenuItemKeys.VIEW_SECURE, false);
		ja.set(2, jo);
/////////////////////////////////
		
		jo = Json.createObject();
		jo.put(MenuItemKeys.VIEW_CLASS, "com.tooooolazy.vaadin.views.Dummy2View");
		jo.put(MenuItemKeys.VIEW_CLASS_ID, 4);
//		jo.put(MenuItemKeys.VIEW_CLASS_PARENT_ID, 2);
		jo.put(MenuItemKeys.VIEW_SECURE, false);
		jo.put(MenuItemKeys.VIEW_BADGE, "2");
		jo.put(MenuItemKeys.VIEW_SUB_TITLE, true);
		ja.set(3, jo);

		jo = Json.createObject();
		jo.put(MenuItemKeys.VIEW_CLASS, "com.tooooolazy.vaadin.views.Dummy3View");
		jo.put(MenuItemKeys.VIEW_CLASS_ID, 5);
		jo.put(MenuItemKeys.VIEW_CLASS_PARENT_ID, 4);
		jo.put(MenuItemKeys.VIEW_SECURE, false);
		jo.put(MenuItemKeys.VIEW_BADGE, "2");
		ja.set(4, jo);


		return ja;
	}
	/**
	 * Sets up the Navigator AND adds the app's Views in the Menu
	 */
	protected void setupNavigator() {
		Navigator navigator = new Navigator(this, getContentContainer());
		setNavigator( navigator );

		root.clearStructureMaps();
		root.createMenuItems( getViewDefinitions(), getNavigator() );

		getNavigator().addView( "", getMainViewClass() );
		getNavigator().setErrorView(ErrorView.class);

		//
		// We use a view change handler to ensure the user is always redirected
		// to the login view if the user is not logged in.
		//
//		vcl = new ViewChangeListener() {
//			@Override
//			public boolean beforeViewChange(ViewChangeEvent event) {
//				if (event.getOldView() != null) {
//					if (!((BaseView)event.getOldView()).verifyExit(event))
//						return false;
//				}
//				// remove any open popup windows when changing views for two reasons:
//				// - language will not change (assuming toggle language button is presses, which triggers a view change event)
//				// - the new view would most likely shouldn't have these popups! (if we don't mind keeping the popups we should use a custom Window class where we define all the extra functionality we need and can use to decide whether to close it or not )
//				Window[] ws = getUI().getWindows().toArray(new Window[]{});
//				for (Window w : ws)
//					removeWindow(w);
//
//				// Check if a user has logged in
//				boolean isLoggedIn = getUserObject() != null;
//				boolean isLoginView = event.getNewView() instanceof DefaultLoginView;
//				boolean isSecureView = isViewSecure(event.getNewView().getClass());
//				boolean isViewHidden = isViewHidden(event.getNewView().getClass());
//				String fragmentAndParameters = event.getViewName();
//
//				if (isViewHidden)
//					fragmentAndParameters = "";
//
//				if (event.getNewView() instanceof ErrorView && (event.getOldView() instanceof ErrorView || getError() == null)) {
//					getNavigator().navigateTo( getMainView( false ) );
//					return false;
//				}
//				if (!isLoggedIn && !isLoginView) {
//					logger.info("Not logged in...");
//					if (isSecureView) {
//						logger.info("Trying to go to a secure view: " + event.getNewView().getClass().getSimpleName());
////						Notification.show(Messages.getString(getClass(), "permission.denied"), Type.ERROR_MESSAGE);
//						// Redirect to login view always if a user has not yet
//						// logged in AND a secured view is requested
//	            		if (event.getParameters() != null) {
//	            			fragmentAndParameters += "/";
//	            			fragmentAndParameters += event.getParameters();
//	            		}
//	            		goToLogin(fragmentAndParameters);
//	            		return false;
//					} else {
//						logger.info("Trying to go to a NON secure view: " + event.getNewView().getClass().getSimpleName());
//						if (event.getOldView() != null)
//							((TlzView)event.getOldView()).exit(event);
//
//						return true;
//					}
//				} else if (isLoggedIn && isLoginView) {
//					logger.info("Logged in and trying to go to login view. Redirecting to main View");
//					// If someone tries to access to login view while logged in,
//					// then cancel
//					getNavigator().navigateTo( getMainView( false ) );
//					return false;
//				}
//				if (isLoggedIn && !hasUserPermission("enter", event.getNewView().getClass(), null)) {
//					if (event.getOldView() == null)
//						getNavigator().navigateTo( "" );
//					else
//					if (event.getOldView().getClass().equals( event.getNewView().getClass() ))
//						getNavigator().navigateTo( "" );
//					else
//						getNavigator().navigateTo( ((DefaultBaseView)event.getOldView()).getThisView() );
//					Notification.show(Messages.getString(((DefaultBaseView)event.getNewView()).getClass(), "title"), "\n" + Messages.getString(getClass(), "page.permission.denied"), Type.ERROR_MESSAGE);
//					
//					return false;
//				}
//
//				logger.info("Logged in (or not required). Will go to view " + event.getNewView().getClass().getSimpleName());
//				if (event.getOldView() != null) {
//					((DefaultBaseView)event.getNewView()).setFrom(event.getOldView());
//					((TlzView)event.getOldView()).exit(event);
//				}
//
//				return true;
//			}
//
//			@Override
//			public void afterViewChange(ViewChangeEvent event) {
//
//			}
//		};
//		getNavigator().addViewChangeListener( vcl );
	}

	protected abstract Class getMainViewClass();

	protected void setupErrorHandling() {
		setErrorHandler(new DefaultErrorHandler() {
			@Override
			public void error(com.vaadin.server.ErrorEvent event) {
				if (event.getThrowable() instanceof InvalidBaseAppParameterException) {
					Notification.show(getMessageString(BaseUI.class, "error"), getMessageString(event.getThrowable().getClass(), "msg"), Type.ERROR_MESSAGE);
				} else
					super.error(event);
			}

		});
	}

	/**
	 * Override to modify default System messages
	 */
	protected void setupSystemMessages() {
		VaadinService.getCurrent().setSystemMessagesProvider(new SystemMessagesProvider() {
			@Override
			public SystemMessages getSystemMessages(SystemMessagesInfo systemMessagesInfo) {
				CustomizedSystemMessages messages = new CustomizedSystemMessages();

				messages.setSessionExpiredNotificationEnabled(false);

				return messages;
			}
		});
	}

	/**
	 * Could use a VALO responsive layout with menu {@link ResponsiveMenuLayout}.
	 * 
	 * @return
	 */
	protected abstract L createRootLayout();

	/**
	 * By default returns the content layout defined in ResponsiveMenuLayout created
	 * by {@link #createRootLayout()}
	 * 
	 * @return
	 */
	protected ComponentContainer getContentContainer() {
		return root.getContentContainer();
	}

	/**
	 * Returns the content container. By default this is the same as
	 * {@link #getContentContainer()
	 * 
	 * @return
	 */
	public ComponentContainer getViewDisplay() {
		return getContentContainer();
	}
	public L getMenu() {
		return root;
	}

	public static BaseUI get() {
		return (BaseUI) UI.getCurrent();
	}

	/**
	 * If null is returned no main Logo is added
	 * 
	 * @return
	 */
	protected abstract Resource getLogoResource();

	/**
	 * If null is returned no secondary Logo is added
	 * 
	 * @return
	 */
	protected abstract Resource getSecLogoResource();

	/**
	 * If null is returned no main Logo is added. Uses {@link #getLogoResource()} to
	 * create the Image
	 * 
	 * @return
	 */
	public Image getLogoImage() {
		if (getLogoResource() == null)
			return null;

		Image logo = new Image(null, getLogoResource());
		return logo;
	}

	/**
	 * If null is returned no secondary Logo is added. Uses
	 * {@link #getSecLogoResource()} to create the Image
	 * 
	 * @return
	 */
	public Image getLogoSecImage() {
		if (getSecLogoResource() == null)
			return null;

		Image logo = new Image(null, getSecLogoResource());
		logo.setHeight(getMainLogoHeight() + "px");
		return logo;
	}

	/**
	 * By default its 37 in order to fit inside the TOP title section of VALO
	 * responsive layout on small screens.<br>
	 * If overridden to a grea ter value VALO theme will need to be adjusted in
	 * order to wrap around it.
	 * 
	 * @param logo
	 */
	protected int getMainLogoHeight() {
		return 37;
	}

	/**
	 * Used as menu title if no secondary logo image is defined (ie
	 * {@link #getLogoSecImage()} returns null)
	 * 
	 * @return
	 */
	public String getTitleHtml() {
		return "<h3>" + Messages.getString(getClass(), "application.title") + "</h3>";
	}

	/**
	 * Helper to get a bundle message by using UI's current locale.
	 * @param c
	 * @param key
	 * @return
	 */
	public String getMessageString(Class c, String key) {
		Messages.setLang(getLocale().getLanguage());
		String str = Messages.getString(c, key);
		return str;
	}

	public void sendNotificationEmailToTooooolazy(final String subject, final String msg) {
		sendNotificationEmailTo(subject, msg, "gpatou@tooooolazy.com");
	}
	/**
	 * Helper to send an email Notification.
	 * @param subject
	 * @param msg
	 * @param to
	 */
	public void sendNotificationEmailTo(final String subject, final String msg, String to) {
		Runnable r = new Runnable() {
			
			@Override
			public void run() {
				try {
					TLZMail.sendHTMLEmail(getEmailConfigProperties(), to, subject, msg);
				} catch (AddressException e) {
					e.printStackTrace();
				} catch (MessagingException e) {
					e.printStackTrace();
				}
			}
		};
		Thread t = new Thread(r);
		t.start();
	}
	/**
	 * <ul>
	 * properties file required must include:
	 * <li>mail.smtp.host</li>
	 * <li>mail.smtp.port</li>
	 * <li>mail.smtp.auth</li>
	 * <li>mail.smtp.auth.user</li>
	 * <li>mail.smtp.auth.password</li>
	 * <li>mail.sender</li>
	 * </ul>
	 * 
	 * @return
	 */
	protected abstract Properties getEmailConfigProperties();

	public abstract boolean supportsLocaleSwitching();
	public abstract Resource getLocalSwitchResource();

	/**
	 * if {@link #hasSecureContent()} returns true, this must return a valid resource
	 * @return
	 */
	public abstract Resource getLoginResource();
	/**
	 * if {@link #hasSecureContent()} returns true, this must return a valid resource
	 * @return
	 */
	public abstract Resource getLogoutResource();

	public static String getClientIp() {
		HttpServletRequest hsr = VaadinServletService.getCurrentServletRequest();
		String ip = hsr.getHeader("x-forwarded-for");
		if (ip == null)
			ip = hsr.getRemoteAddr();
		return ip;
	}

	public String getUserRemoteAddress() {
		VaadinRequest currentRequest = VaadinService.getCurrentRequest();

		String userRemoteAddress = null;

		if (currentRequest != null) {
			String xForwardedFor = currentRequest.getHeader("X_FORWARDED_FOR");
			if (StringUtils.isNotBlank(xForwardedFor)) {
				String[] strings = xForwardedFor.split(",");
				String userAddressFromHeader = StringUtils.trimToEmpty(strings[strings.length - 1]);

				if (StringUtils.isNotEmpty(userAddressFromHeader)) {
					userRemoteAddress = userAddressFromHeader;
				} else {
					userRemoteAddress = currentRequest.getRemoteAddr();
				}
			} else {
				userRemoteAddress = currentRequest.getRemoteAddr();
			}
		}

		return userRemoteAddress;
	}

	public Object getCurrentUser() {
		try {
			return getSession().getAttribute( SESSION_USER_KEY );
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * If true is returned, then 'login' and 'logout' functionality should be added
	 * (ie through actions in menu).
	 * 
	 * @return
	 */
	public boolean hasSecureContent() {
		return hasSecureContent;
	}
	public void setHasSecureContent() {
		hasSecureContent = true;
	}
	/**
	 * Retrieves the user object from session
	 * @return
	 */
	public Object getUserObject() {
		return getSession().getAttribute( SESSION_USER_KEY );
	}
	public boolean hasUserPermission(String methodName, Class _class, Object[] params) {
		Method method = null;
		try {
			method = _class.getMethod(methodName, ViewChangeEvent.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return hasUserPermission(method, _class, params);
	}
	public boolean hasUserPermission(Method method, Class _class, Object[] params) {
//		if (getSecurityController() == null) {
//			if (getUserObject() == null)
//				return false;
//			return true;
//		}
//		return getSecurityController().hasAccess(getUserObject(), method, _class, params);
		return true; // remove when done
	}
}
