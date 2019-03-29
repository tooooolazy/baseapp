package com.tooooolazy.vaadin.components;

import com.tooooolazy.data.services.beans.UserRoleBean;
import com.tooooolazy.util.Messages;
import com.vaadin.ui.TreeGrid;


public class UsersTreeGrid extends TreeGrid<UserRoleBean> {

	public UsersTreeGrid() {
		super();
		setId("UsersTreeGrid");

		init();
	}

	protected void init(){
		setBeanType(UserRoleBean.class);
	}

	public void attach() {
		super.attach();

		addColumns();
//		adjustMainHeader();
		setGroupHeaders();
	}

	protected void addColumns() {
		addColumn( "username" ).setCaption( Messages.getString( getClass(), "username" ) );

		addColumn( "firstName" ).setCaption( Messages.getString( getClass(), "firstName" ) );
		addColumn( "lastName" ).setCaption( Messages.getString( getClass(), "lastName" ) );

		addColumn( "roleCode" ).setCaption( Messages.getString( getClass(), "role" ) );
		addColumn( "assigned" ).setCaption( Messages.getString( getClass(), "assigned" ) );
	}

	private void setGroupHeaders() {
//		HeaderRow groupingHeader = prependHeaderRow();
	}
}
