package com.tooooolazy.vaadin.views;

import com.tooooolazy.util.Messages;
import com.tooooolazy.util.SearchCriteria;
import com.tooooolazy.util.TLZUtils;
import com.tooooolazy.vaadin.ui.AppLayout;
import com.tooooolazy.vaadin.ui.BaseUI;
import com.vaadin.external.org.slf4j.Logger;
import com.vaadin.external.org.slf4j.LoggerFactory;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewBeforeLeaveEvent;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

public abstract class BaseView<C extends SearchCriteria, E> extends CustomComponent implements View {
	protected final Logger logger = LoggerFactory.getLogger(BaseView.class.getName());

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

		// also add Parent View name (if any AND if enabled)
		if ( showParentTitle() ) {
			AppLayout al = (AppLayout) getUI().getContent();
			Class pClass = al.getParentViewClass(getClass());

			if ( pClass != null )
				_title = Messages.getString(pClass, "page.title") + " - " + _title;
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
			BaseUI.get().getAppLayout().getLogoutItem().setVisible(BaseUI.get().getUserObject() != null);
			BaseUI.get().getAppLayout().getLoginItem().setVisible(BaseUI.get().hasSecureContent() && BaseUI.get().getUserObject() == null);
		}

		AppLayout al = (AppLayout) getUI().getContent();
		al.toggleChildMenuItems(getClass(), true);
	}

	public void beforeLeave(ViewBeforeLeaveEvent event) {
		if (verifyExit(event)) {
			event.navigate();

			Navigator navigator = (Navigator) event.getSource();
			// remove any open popup windows when changing views for two reasons:
			// - language will not change (assuming toggle language button is presses, which triggers a view change event)
			// - the new view would most likely shouldn't have these popups! (if we don't mind keeping the popups we should use a custom Window class where we define all the extra functionality we need and can use to decide whether to close it or not )

			// UI seems to be null over here
			Window[] ws = navigator.getUI().getWindows().toArray(new Window[] {});
			for (Window w : ws)
				navigator.getUI().removeWindow(w);
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
	protected boolean verifyExit(ViewBeforeLeaveEvent event) {
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

	public void onInactivity() {

	}
}
