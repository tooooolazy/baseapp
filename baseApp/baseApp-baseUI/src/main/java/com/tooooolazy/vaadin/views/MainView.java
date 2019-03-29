package com.tooooolazy.vaadin.views;

import com.tooooolazy.data.services.beans.OnlineBaseResult;
import com.tooooolazy.data.services.beans.UserBean;
import com.tooooolazy.util.SearchCriteria;
import com.tooooolazy.vaadin.components.LoginComponent;
import com.tooooolazy.vaadin.components.listeners.LoginButtonListener;
import com.tooooolazy.vaadin.ui.BaseUI;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Component;

public class MainView<C extends SearchCriteria, E, UB extends UserBean, OR extends OnlineBaseResult, JFC> extends BaseView<C, E, UB, OR, JFC> {

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
	/**
	 * Overridden since this view needs no dynamic content!
	 * @see com.tooooolazy.vaadin.views.BaseView#addDynamicContent()
	 */
	@Override
	protected void addDynamicContent() {
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
	@Override
	protected void addSearchCriteria(AbstractComponent ac) {
		// TODO Auto-generated method stub
		
	}
	@Override
	protected Component createContent(BaseUI ui) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	protected OR[] getWSContents() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	protected OR[] create_OR_array(int i) {
		// TODO Auto-generated method stub
		return null;
	}
}
