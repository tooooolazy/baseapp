package com.tooooolazy.vaadin.components.listeners;

import com.tooooolazy.vaadin.components.LoginComponent;
import com.tooooolazy.vaadin.ui.BaseUI;
import com.vaadin.ui.UI;

public abstract class LoginButtonListener extends InputComponentButtonListener {
//	protected UserControllerService uc;

	@Override
	protected void init() {
//		uc = ((BaseUI)UI.getCurrent()).getUsersController();
	}
	@Override
	protected Object submit() throws Exception {
//		return uc.login(((LoginComponent)ic).getBean());
		return null;
	}
	@Override
	protected void onError(Exception e) {
//		if (e instanceof PasswordExpiredException && getChangePasswordViewName() != null) {
//			((BaseUI)UI.getCurrent()).setCredentials(((PasswordExpiredException)e).getCredentials());
//			UI.getCurrent().getNavigator().navigateTo( getChangePasswordViewName() ); // go
//		} else {
			LoginComponent lc = ((LoginComponent)ic);
			lc.setComponentError(createError(lc.getClass(), e));
			lc.enableSubmit();
			lc.focus();
//		}
	}
	protected abstract String getChangePasswordViewName();

	@Override
	protected void onSuccess(Object res) {
		loginSuccess(res);
	}

	protected abstract void loginSuccess(Object u);
}
