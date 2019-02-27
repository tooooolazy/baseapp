package com.tooooolazy.vaadin.views;

import com.tooooolazy.vaadin.components.LoginComponent;
import com.tooooolazy.vaadin.components.listeners.LoginButtonListener;

public class MainView extends BaseView {

	@Override
	protected boolean showTitleInContent() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	protected void addJavascriptFunctions() {
		// TODO Auto-generated method stub

	}

	@Override
	protected Class getCriteriaClass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addContent() {
		// TODO Auto-generated method stub
		LoginComponent lc = new LoginComponent( new LoginButtonListener() {
			
			@Override
			protected void loginSuccess(Object u) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			protected String getChangePasswordViewName() {
				// TODO Auto-generated method stub
				return null;
			}
		}, this);
		vl.addComponent( lc );
	}
}
