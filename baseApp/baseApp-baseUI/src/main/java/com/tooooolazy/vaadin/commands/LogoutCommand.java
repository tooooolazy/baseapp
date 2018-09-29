package com.tooooolazy.vaadin.commands;

import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;

public class LogoutCommand implements Command {
	protected MenuItem login;

	public void setLoginItem(MenuItem loginItem) {
		login = loginItem;
	}
	@Override
    public void menuSelected(MenuItem selectedItem) {
		selectedItem.setVisible( false );
		login.setVisible( true );
	}
}
