package com.tooooolazy.vaadin.views;

import com.tooooolazy.util.Messages;
import com.tooooolazy.util.SearchCriteria;
import com.tooooolazy.util.TLZUtils;
import com.tooooolazy.vaadin.ui.BaseUI;
import com.vaadin.external.org.slf4j.Logger;
import com.vaadin.external.org.slf4j.LoggerFactory;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewBeforeLeaveEvent;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public abstract class BaseView<C extends SearchCriteria, E> extends CustomComponent implements View {
	protected final Logger logger = LoggerFactory.getLogger(UI.class.getName());

	protected VerticalLayout vl;

	public BaseUI getUI() {
		return (BaseUI)super.getUI();
	}

	/** 
	 * Overridden in order to perform initialization that may require access to UI
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

	protected abstract boolean showTitleInContent();
	protected String getViewTitle() {
		return Messages.getString(getClass(), "application.title");
	}
	
	/**
	 * called inside {@link #attach()} method and is where main View initialization takes place.
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

	protected Component createTitleComponent() {
		// TODO also add Parent View name (if any AND if enabled)
		Label title = new Label( Messages.getString( getClass(), "page.title") );
		title.setWidth("100%");
		title.addStyleName( ValoTheme.LABEL_H1 );
		return title;
	}

	protected abstract void addJavascriptFunctions();

    public void enter(ViewChangeEvent event) {
    	getUI().getMenu().setActiveView( event.getNewView().getClass().getSimpleName() );
    	logger.info( "Entering " + getClass().getSimpleName() + " from ip: " + BaseUI.get().getClientIp()  + " =? " +  BaseUI.get().getUserRemoteAddress());
    	logger.info( "URI params: " + event.getParameters() );
    	logger.info( "URI params: " + event.getParameterMap("/") );
    }
    public void beforeLeave(ViewBeforeLeaveEvent event) {
    	if ( verifyExit( event ) )
    		event.navigate();
    	else
        	logger.info( "Cannot leave from " + getClass().getSimpleName() );
    }

	protected boolean verifyExit(ViewBeforeLeaveEvent event) {
		return true;
	}


	protected abstract Class<C> getCriteriaClass();
	protected C getCriteria() {
		if (getCriteriaClass() == null)
			return null;

		C sc = (C) getSession().getAttribute(getCriteriaClass());
		if (sc == null) {
			try {
				sc = (C)TLZUtils.loadObject(getCriteriaClass().getName());
				getSession().setAttribute(getCriteriaClass(), sc);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return sc;
	}
}
