package com.tooooolazy.vaadin.ui;

import java.util.Locale;

import com.tooooolazy.util.Messages;
import com.tooooolazy.vaadin.layout.ResponsiveMenuLayout;
import com.tooooolazy.vaadin.views.ErrorView;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.Resource;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
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
//		else
//			setLocale(new Locale("en"));

		Responsive.makeResponsive(this);
//		 getLogoResource(), getSecLogoResource()
		root = new ResponsiveMenuLayout();
		viewDisplay = root.getContentContainer();
		setContent(root);
		addStyleName(ValoTheme.UI_WITH_MENU);

		navigator = new Navigator(this, viewDisplay);
		navigator.setErrorView( ErrorView.class );
	}

	public static BaseUI get() {
        return (BaseUI) UI.getCurrent();
    }
	/**
	 * if null is returned no main Logo is added
	 * @return
	 */
	protected abstract Resource getLogoResource();
	protected abstract Resource getSecLogoResource();
	public Image getLogoImage() {
		if ( getLogoResource() == null )
			return null;

		Image logo = new Image( null, getLogoResource() );
		return logo;
	}
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
