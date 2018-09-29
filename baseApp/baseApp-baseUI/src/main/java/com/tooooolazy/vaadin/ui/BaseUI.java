package com.tooooolazy.vaadin.ui;

import java.util.Locale;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.servlet.http.HttpServletRequest;

import com.tooooolazy.util.Messages;
import com.tooooolazy.util.TLZMail;
import com.tooooolazy.vaadin.exceptions.InvalidBaseAppParameterException;
import com.tooooolazy.vaadin.layout.ResponsiveMenuLayout;
import com.tooooolazy.vaadin.views.AboutView;
import com.tooooolazy.vaadin.views.ErrorView;
import com.vaadin.external.org.slf4j.Logger;
import com.vaadin.external.org.slf4j.LoggerFactory;
import com.vaadin.navigator.Navigator;
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

/**
 *
 */
public abstract class BaseUI extends UI {
	protected final Logger logger = LoggerFactory.getLogger(UI.class.getName());

	protected final String SESSION_USER_KEY = "_user";

	protected ResponsiveMenuLayout root;
	protected ComponentContainer viewDisplay;
	protected Navigator navigator;

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

		getPage().addPopStateListener(new PopStateListener() {

			@Override
			public void uriChanged(PopStateEvent event) {
				// TODO Auto-generated method stub
				logger.info( event.getUri() );
//				ViewChangeEvent vce = new ViewChangeEvent(navigator, navigator.getCurrentView(), navigator.getCurrentView(), navigator.getCurrentView().getClass().getSimpleName(), event.getUri());
//				navigator.getCurrentView().enter( vce );
			}
		});

		setErrorHandler(new DefaultErrorHandler() {
			@Override
			public void error(com.vaadin.server.ErrorEvent event) {
				if (event.getThrowable() instanceof InvalidBaseAppParameterException) {
					Notification.show(getMessageString(BaseUI.class, "error"), getMessageString(event.getThrowable().getClass(), "msg"), Type.ERROR_MESSAGE);
//					Notification.show(getMessageString(BaseUI.class, "error"), event.getThrowable().getMessage(), Type.ERROR_MESSAGE);
				} else
					super.error(event);
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
		root = getRootLayout();
		viewDisplay = getContentContainer();
		setContent(root);
		addStyleName(ValoTheme.UI_WITH_MENU);

		navigator = new Navigator(this, viewDisplay);
		navigator.setErrorView(ErrorView.class);

		navigator.addView(AboutView.class.getSimpleName(), AboutView.class);
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
	 * By default a VALO responsive layout with menu is created.
	 * 
	 * @return
	 */
	protected ResponsiveMenuLayout getRootLayout() {
		return new ResponsiveMenuLayout();
	}

	/**
	 * By default returns the content layout defined in ResponsiveMenuLayout created
	 * by {@link #getRootLayout()}
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
		return viewDisplay;
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
	 * If true is returned, then 'login' and 'logout' functionality should be added (ie through actions in menu)
	 * @return
	 */
	public abstract boolean hasSecureContent();
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

	public Object getCurrentUser() {
		try {
			return getSession().getAttribute( SESSION_USER_KEY );
		} catch (Exception e) {
			return null;
		}
	}
}
