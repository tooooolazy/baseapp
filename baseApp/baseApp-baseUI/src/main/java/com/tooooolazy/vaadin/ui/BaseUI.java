package com.tooooolazy.vaadin.ui;

import java.util.Locale;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import com.tooooolazy.util.Messages;
import com.tooooolazy.util.TLZMail;
import com.tooooolazy.vaadin.exceptions.InvalidBaseAppParameterException;
import com.tooooolazy.vaadin.layout.ResponsiveMenuLayout;
import com.tooooolazy.vaadin.views.ErrorView;
import com.vaadin.external.org.slf4j.Logger;
import com.vaadin.external.org.slf4j.LoggerFactory;
import com.vaadin.navigator.Navigator;
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
}
