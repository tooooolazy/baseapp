package com.tooooolazy.vaadin.views;

import com.vaadin.navigator.ViewBeforeLeaveEvent;
import com.vaadin.ui.CustomLayout;

/**
 * Assumes that a custom Layout file exists in Theme folder '/layouts' in the form of 'AboutView_[locale_lang].html' eg. AbountView_en.html
 * @author tooooolazy
 *
 */
public class AboutView extends BaseView {

	private static final long serialVersionUID = -3136395231608992697L;

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
