package com.tooooolazy.vaadin.commands;

import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;

public class LoginCommand implements Command {
	protected MenuItem logout;

	public void setLogoutItem(MenuItem logoutItem) {
		logout = logoutItem;
	}
	@Override
    public void menuSelected(MenuItem selectedItem) {
		selectedItem.setVisible( false );
		logout.setVisible( true );
	}
}
