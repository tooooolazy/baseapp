package com.tooooolazy.vaadin.views;

import com.tooooolazy.util.Messages;
import com.tooooolazy.util.SearchCriteria;
import com.tooooolazy.util.TLZUtils;
import com.tooooolazy.vaadin.ui.BaseUI;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewBeforeLeaveEvent;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public abstract class BaseView<C extends SearchCriteria, E> extends CustomComponent implements View {
	protected VerticalLayout vl;

	public BaseUI getUI() {
		return (BaseUI)super.getUI();
	}

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
	
	protected void init() {
		vl = new VerticalLayout();
		setCompositionRoot( vl );

		if ( showTitleInContent() ) {
			vl.addComponent( createTitleComponent() );
		}
	}

	protected Component createTitleComponent() {
		Label title = new Label( Messages.getString( getClass(), "page.title") );
		title.setWidth("100%");
		title.addStyleName( ValoTheme.LABEL_H1 );
		return title;
	}

	protected abstract void addJavascriptFunctions();

    public void enter(ViewChangeEvent event) {
    }
    public void beforeLeave(ViewBeforeLeaveEvent event) {
    	if ( verifyExit( event ) )
    		event.navigate();
    }

	protected abstract boolean verifyExit(ViewBeforeLeaveEvent event);


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
