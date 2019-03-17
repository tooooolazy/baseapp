package com.tooooolazy.vaadin.commands;

import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.UI;

public class LoginCommand implements Command {
	protected MenuItem logout;

	public void setLogoutItem(MenuItem logoutItem) {
		logout = logoutItem;
	}
	@Override
    public void menuSelected(MenuItem selectedItem) {
//		UI.getCurrent().getNavigator().navigateTo( );
		logout.setVisible( true );
		selectedItem.setVisible( false );
	}
}
