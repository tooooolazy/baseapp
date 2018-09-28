package com.tooooolazy.vaadin.ui;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import com.tooooolazy.util.Messages;
import com.tooooolazy.vaadin.layout.ResponsiveMenuLayout;
import com.tooooolazy.vaadin.views.AboutView;
import com.tooooolazy.vaadin.views.ErrorView;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.Resource;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServletService;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Image;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;

/**
 *
 */
public abstract class BaseUI extends UI {
//	public static final String ctxPath = VaadinServlet.getCurrent()
//		    .getServletContext().getContextPath() + VaadinServletService 
//		    .getCurrentServletRequest().getServletPath();

	protected ResponsiveMenuLayout root;
	protected ComponentContainer viewDisplay;
	protected Navigator navigator;

	static {
		try {
			Messages.setMainBundle(Messages.class.getPackage().getName() + ".messages");
			Messages.addBundle(BaseUI.class.getPackage().getName() + ".messages");
		} catch(Exception e) {
			// no default resource bundles... but never mind
		}
	}

	public static String getClientIp() {
		HttpServletRequest hsr = VaadinServletService.getCurrentServletRequest();
		String ip = hsr.getHeader("x-forwarded-for"); if(ip == null) ip = hsr.getRemoteAddr();
		return ip;
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
		if ( useBrowserLocale() )
			Messages.setLang(getUI().getLocale().getLanguage());

		Responsive.makeResponsive(this);
		root = getRootLayout();
		viewDisplay = getContentContainer();
		setContent(root);
		addStyleName(ValoTheme.UI_WITH_MENU);

		navigator = new Navigator(this, viewDisplay);
		navigator.setErrorView( ErrorView.class );

		navigator.addView(AboutView.class.getSimpleName(), AboutView.class);
	}


	/**
	 * By default a VALO responsive layout with menu is created.
	 * @return
	 */
	protected ResponsiveMenuLayout getRootLayout() {
		return new ResponsiveMenuLayout();
	}
	/**
	 * By default returns the content layout defined in ResponsiveMenuLayout created by {@link #getRootLayout()}
	 * @return
	 */
	protected ComponentContainer getContentContainer() {
		return root.getContentContainer();
	}

	/**
	 * Returns the content container. By default this is the same as {@link #getContentContainer()
	 * @return
	 */
	public ComponentContainer getViewDisplay() {
    	return viewDisplay;
    }
	public static BaseUI get() {
        return (BaseUI) UI.getCurrent();
    }
	/**
	 * if null is returned no main Logo is added
	 * @return
	 */
	protected abstract Resource getLogoResource();
	/**
	 * if null is returned no secondary Logo is added
	 * @return
	 */
	protected abstract Resource getSecLogoResource();

	/**
	 * if null is returned no main Logo is added. Uses {@link #getLogoResource()} to create the Image
	 * @return
	 */
	public Image getLogoImage() {
		if ( getLogoResource() == null )
			return null;

		Image logo = new Image( null, getLogoResource() );
		return logo;
	}
	/**
	 * if null is returned no secondary Logo is added. Uses {@link #getSecLogoResource()} to create the Image
	 * @return
	 */
	public Image getLogoSecImage() {
		if ( getSecLogoResource() == null )
			return null;

		Image logo = new Image( null, getSecLogoResource() );
		logo.setHeight("37px");
		return logo;
	}

	/**
	 * used as menu title if no secondary logo image is defined (ie {@link #getLogoSecImage()} returns null)
	 * @return
	 */
	public String getTitleHtml() {
		return "<h3>Vaadin <strong>Valo Theme</strong></h3>";
	}
}
