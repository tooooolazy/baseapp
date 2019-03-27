package com.tooooolazy.vaadin.views;

import com.tooooolazy.data.services.beans.UserBean;
import com.tooooolazy.util.Messages;
import com.tooooolazy.util.SearchCriteria;
import com.tooooolazy.util.TLZUtils;
import com.tooooolazy.vaadin.ui.AppLayout;
import com.tooooolazy.vaadin.ui.BaseUI;
import com.vaadin.external.org.slf4j.Logger;
import com.vaadin.external.org.slf4j.LoggerFactory;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewBeforeLeaveEvent;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public abstract class BaseView<C extends SearchCriteria, E, UB extends UserBean> extends CustomComponent implements View {
	protected final Logger logger = LoggerFactory.getLogger(BaseView.class.getName());

	protected String fragmentAndParameters;

	protected VerticalLayout vl;

	public BaseUI getUI() {
		return (BaseUI) super.getUI();
	}

	/**
	 * Overridden in order to perform initialization that may require access to UI
	 * 
	 * @see com.vaadin.ui.AbstractComponent#attach()
	 */
	@Override
	public void attach() {
		super.attach();
		setBrowserTitle();

		init();

		addJavascriptFunctions();
	}

	protected void setBrowserTitle() {
		Messages.setLang(getUI().getLocale().getLanguage());
		Page.getCurrent().setTitle( getViewTitle() );
	}

	protected boolean showTitleInContent() {
		return true;
	}
	protected String getViewTitle() {
		return Messages.getString(getClass(), "application.title");
	}
	public String getThisView() {
		return getClass().getSimpleName();
	}

	/**
	 * called inside {@link #attach()} method and is where main View initialization
	 * takes place.
	 */
	protected void init() {
		vl = new VerticalLayout();
		setCompositionRoot( vl );

		if ( showTitleInContent() ) {
			vl.addComponent( createTitleComponent() );
		}
		addContent();
	}

	protected void addContent() {
		// TODO Auto-generated method stub

	}

	/**
	 * if true, the Title of parent view will be displayed with current view title.
	 * Refers ONLY to Views in left menu.
	 * 
	 * @return
	 */
	protected boolean showParentTitle() {
		return true;
	}
	protected Component createTitleComponent() {
		String _title = Messages.getString(getClass(), "page.title");
		if ( _title.indexOf("!") > -1 )
			_title = Messages.getString(getClass(), "page.menu.title");

		// also add Parent View name (if any AND if enabled)
		if ( showParentTitle() ) {
			AppLayout al = (AppLayout) getUI().getContent();
			Class pClass = al.getParentViewClass(getClass());

			if ( pClass != null ) {
				String __title = Messages.getString(pClass, "page.title");
				if ( __title.indexOf("!") > -1 )
					__title = Messages.getString(pClass, "page.menu.title");
				_title = __title + " - " + _title;
			}
		}

		Label title = new Label( _title );
		title.setWidth("100%");
		title.addStyleName( ValoTheme.LABEL_H1 );
		return title;
	}

	protected abstract void addJavascriptFunctions();

	public void enter(ViewChangeEvent event) {
		getUI().getAppLayout().setActiveView(event.getNewView().getClass(), true);
		logger.info("Entering " + getClass().getSimpleName() + " from ip: " + BaseUI.get().getClientIp() + " =? " + BaseUI.get().getUserRemoteAddress());
		logger.info("URI params: " + event.getParameters());
		logger.info("URI params: " + event.getParameterMap("/"));

		if ( BaseUI.get().hasSecureContent() ) {
			BaseUI.get().getAppLayout().getLogoutItem().setVisible(BaseUI.get().getCurrentUser() != null);
			BaseUI.get().getAppLayout().getLoginItem().setVisible(BaseUI.get().hasSecureContent() && BaseUI.get().getCurrentUser() == null);
		}

		AppLayout al = (AppLayout) getUI().getContent();
		al.toggleChildMenuItems(getClass(), true);

		handleCriteria( event.getOldView() );
	}

	public void beforeLeave(ViewBeforeLeaveEvent event) {
		if (verifyExit(event)) {
			event.navigate();

		} else
			logger.info("Cannot leave from " + getClass().getSimpleName());
	}

	/**
	 * Automatically called when User navigates out of current View
	 * 
	 * @param event
	 * @return - false if Navigation should NOT take place!! (ie user needs to save
	 *         data first)
	 */
	public boolean verifyExit(ViewBeforeLeaveEvent event) {
		boolean canExit = true;
		return canExit;
	}

	protected abstract Class<C> getCriteriaClass();
	protected C getCriteria() {
		if (getCriteriaClass() == null)
			return null;

		C sc = (C) getSession().getAttribute(getCriteriaClass());
		if (sc == null) {
			try {
				sc = (C) TLZUtils.loadObject(getCriteriaClass().getName());
				getSession().setAttribute(getCriteriaClass(), sc);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return sc;
	}
	/**
	 * Default functionality that clears search criteria when entering a page from a different one
	 */
	protected void handleCriteria(View fromView) {
		if (getCriteriaClass() != null) {
			// clear criteria when entering the page from another page (if criteria exist)
			// could do it depending on a parameter!!
			if (fromView != null && fromView.getClass().equals(getClass())) {
				// retain criteria
			} else {
				getSession().setAttribute(getCriteriaClass(), null);
			}
		}
	}
	/**
	 * Override to create a custom Search criteria component
	 * @return
	 */
	protected AbstractComponent createSearchCriteria() {
		return null;
	}
	protected boolean hideCriteria() {
		return false;
	}

	public void onInactivity() {

	}

	public void performLogout() {
		if (!verifyExit(null))
			return;

		onInactivity(); // should also trigger auto save!

		try {
			Class[] types = new Class[] {Object.class};
			UB user = (UB) BaseUI.get().getCurrentUser();

			cleanupUser( user );
		} catch (Exception e) {
			e.printStackTrace();
		}

		// "Logout" the user
		getSession().setAttribute(BaseUI.SESSION_USER_KEY, null);
		getUI().setCredentials(null);
		logger.info("log out clicked...");

		// Refresh this view, should redirect to login view
		getUI().refreshLayout();
		getUI().getNavigator().navigateTo("");
	}
	protected void cleanupUser(UB user) {
		
	}
}
