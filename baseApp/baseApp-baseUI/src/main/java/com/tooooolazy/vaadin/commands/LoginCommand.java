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
		// this will show Login component integrated in main view
		UI.getCurrent().getNavigator().navigateTo( "" );
		// this should be called once login is successful!
//		logout.setVisible( true );
		// this should be called once login is successful. Also should not be visible when Login Component is Visible (ie we are in MainView where LoginComponent appears)
		selectedItem.setVisible( false );
	}
}
