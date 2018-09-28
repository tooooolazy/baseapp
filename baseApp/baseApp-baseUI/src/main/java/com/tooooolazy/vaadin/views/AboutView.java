package com.tooooolazy.vaadin.views;

import com.vaadin.navigator.ViewBeforeLeaveEvent;
import com.vaadin.ui.CustomLayout;

/**
 * @author tooooolazy
 *
 */
public class AboutView extends BaseView {

	@Override
	protected void addJavascriptFunctions() {
		// TODO Auto-generated method stub

	}
	
	@Override
	protected void init() {
		super.init();

		CustomLayout cl = new CustomLayout( getClass().getSimpleName() + "_" + getUI().getLocale().getLanguage());
		vl.addComponent( cl );
	}

	@Override
	protected boolean showTitleInContent() {
		return true;
	}

	@Override
	protected boolean verifyExit(ViewBeforeLeaveEvent event) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected Class getCriteriaClass() {
		// TODO Auto-generated method stub
		return null;
	}

}
