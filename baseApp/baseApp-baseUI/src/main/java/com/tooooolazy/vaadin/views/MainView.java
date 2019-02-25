package com.tooooolazy.vaadin.views;

import com.tooooolazy.vaadin.components.ForgotPasswordComponent;
import com.tooooolazy.vaadin.components.listeners.InputComponentButtonListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Notification;

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
		ForgotPasswordComponent fpc = new ForgotPasswordComponent( new InputComponentButtonListener() {
			
			@Override
			protected Object submit() throws Exception {
				return null;
			}
			@Override
			protected void onError(Exception e) {
				super.onError( e );
			}
			@Override
			protected void onSuccess(Object res) {
				
			}
		}, this);
		vl.addComponent( fpc );
	}
}
