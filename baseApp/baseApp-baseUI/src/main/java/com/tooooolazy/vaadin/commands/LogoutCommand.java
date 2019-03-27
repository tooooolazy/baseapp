package com.tooooolazy.vaadin.commands;

import com.tooooolazy.vaadin.ui.BaseUI;
import com.tooooolazy.vaadin.views.BaseView;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;

public class LogoutCommand implements Command {
	protected MenuItem login;

	public void setLoginItem(MenuItem loginItem) {
		login = loginItem;
	}
	@Override
    public void menuSelected(MenuItem selectedItem) {
		((BaseView)BaseUI.get().getNavigator().getCurrentView()).performLogout();
	}
}
