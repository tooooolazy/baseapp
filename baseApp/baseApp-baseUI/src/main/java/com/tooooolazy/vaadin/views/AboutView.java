package com.tooooolazy.vaadin.views;

import com.tooooolazy.data.services.beans.OnlineBaseResult;
import com.tooooolazy.data.services.beans.UserBean;
import com.tooooolazy.util.SearchCriteria;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Component;

/**
 * Assumes that a custom Layout file exists in Theme folder '/layouts' in the form of 'AboutView_[locale_lang].html' eg. AbountView_en.html
 * @author tooooolazy
 *
 */
public class AboutView<C extends SearchCriteria, E, UB extends UserBean, OR extends OnlineBaseResult, JFC> extends BaseView<C, E, UB, OR, JFC> {

	private static final long serialVersionUID = -3136395231608992697L;

	@Override
	protected void addJavascriptFunctions() {
		// TODO Auto-generated method stub

	}
	
//	@Override
//	protected void init() {
//		super.init();
//
//		CustomLayout cl = new CustomLayout( getClass().getSimpleName() + "_" + getUI().getLocale().getLanguage());
//		vl.addComponent( cl );
//	}

	@Override
	protected boolean showTitleInContent() {
		return true;
	}

	@Override
	protected boolean hasStaticContent() {
		return true;
	}

	@Override
	protected Class getCriteriaClass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addSearchCriteria(AbstractComponent ac) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected Component createContent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected OR[] getWSContents() {
		// TODO Auto-generated method stub
		return null;
	}

}
