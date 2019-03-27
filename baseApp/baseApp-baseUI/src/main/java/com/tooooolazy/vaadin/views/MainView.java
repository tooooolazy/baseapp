package com.tooooolazy.vaadin.views;

import com.tooooolazy.vaadin.components.LoginComponent;
import com.tooooolazy.vaadin.components.listeners.LoginButtonListener;
import com.tooooolazy.vaadin.ui.BaseUI;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;

public class MainView extends BaseView {

	public MainView() {
		super();
	}
	public MainView(String fragmentAndParameters) {
		super();
		this.fragmentAndParameters = fragmentAndParameters;
	}
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

    public void enter(ViewChangeEvent event) {
    	super.enter(event);
    	// always hide login menu item!
    	if ( BaseUI.get().hasSecureContent() )
    		BaseUI.get().getAppLayout().getLoginItem().setVisible( false );
    }
	@Override
	protected void addContent() {
		if ( BaseUI.get().hasSecureContent() && BaseUI.get().getCurrentUser() == null ) {
			
			LoginComponent lc = new LoginComponent( new LoginButtonListener() {
				
				@Override
				protected void loginSuccess(Object u) {
					getSession().setAttribute(BaseUI.SESSION_USER_KEY, u);
					
					BaseUI.get().refreshLayout();

					getUI().getNavigator().navigateTo(fragmentAndParameters != null ? fragmentAndParameters : "");
				}
				
				@Override
				protected String getChangePasswordViewName() {
					// TODO Auto-generated method stub
					return null;
				}
			}, this);
			vl.addComponent( lc );
		} else {
			
		}
	}
}
