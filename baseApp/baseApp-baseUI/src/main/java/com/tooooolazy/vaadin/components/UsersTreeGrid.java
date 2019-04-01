package com.tooooolazy.vaadin.components;

import com.tooooolazy.data.ServiceLocator;
import com.tooooolazy.data.services.beans.RoleEnum;
import com.tooooolazy.data.services.beans.UserRoleBean;
import com.tooooolazy.util.Messages;
import com.tooooolazy.vaadin.resources.Resources;
import com.vaadin.ui.TreeGrid;
import com.vaadin.ui.renderers.ImageRenderer;
import com.vaadin.ui.renderers.TextRenderer;


/**
 * <ol>Creates a TreeGrid the displays User Role assignment. It requires 3 icons defined by the following methods:
 * <li>{@link #getUserIcon() }</li>
 * <li>{@link #getTrueIcon() }</li>
 * <li>{@link #getFalseIcon() }</li>
 * </ol>
 * The icons are retrieved from theme folder defined by {@link #getIconPath()}. Make sure the folder and the icons exist!
 * @author gpatoulas
 *
 */
public class UsersTreeGrid extends TreeGrid<UserRoleBean> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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

//		addColumn( "roleCode" ).setCaption( Messages.getString( getClass(), "role" ) );
		addColumn( u -> u.getRoleCode() > 0 ? getRoleByValue( u.getRoleCode() ) : "",
				new TextRenderer() ).setCaption( Messages.getString( getClass(), "role" ) );

		addColumn( u -> Resources.getPng(getIconPath(), 
				u.getRoleCode() == 0 ? getUserIcon() : ((u.getAssigned() != null && u.getAssigned())?getTrueIcon():getFalseIcon())),
				new ImageRenderer() ).setCaption( Messages.getString( getClass(), "assigned" ) );
	}

	protected String getRoleByValue(int rv) {
		return Messages.getString( RoleEnum.class, ((Enum)ServiceLocator.getServices().getSecurityController().getRoleByValue( rv )).name() );
	}
	protected String getIconPath() {
		return "img/actions/";
	}
	/**
	 * default icon for user
	 * @return
	 */
	protected String getUserIcon() {
		return "user-icon_20x20";
	}
	/**
	 * default icon for 'true'
	 * @return
	 */
	protected String getTrueIcon() {
		return "true";
	}
	/**
	 * default icon for false
	 * @return
	 */
	protected String getFalseIcon() {
		return "false";
	}

	private void setGroupHeaders() {
//		HeaderRow groupingHeader = prependHeaderRow();
	}
}
