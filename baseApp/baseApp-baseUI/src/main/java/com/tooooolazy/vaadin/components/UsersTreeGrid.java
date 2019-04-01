package com.tooooolazy.vaadin.components;

import com.tooooolazy.data.ServiceLocator;
import com.tooooolazy.data.services.beans.RoleEnum;
import com.tooooolazy.data.services.beans.UserRoleBean;
import com.tooooolazy.util.Messages;
import com.tooooolazy.util.TLZUtils;
import com.tooooolazy.vaadin.resources.Resources;
import com.tooooolazy.vaadin.ui.BaseUI;
import com.vaadin.data.HasValue.ValueChangeEvent;
import com.vaadin.data.HasValue.ValueChangeListener;
import com.vaadin.data.provider.TreeDataProvider;
import com.vaadin.ui.TextField;
import com.vaadin.ui.TreeGrid;
import com.vaadin.ui.components.grid.HeaderRow;
import com.vaadin.ui.renderers.ClickableRenderer.RendererClickEvent;
import com.vaadin.ui.renderers.ClickableRenderer.RendererClickListener;
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
				new ImageRenderer( createAsignmentClickListener() ) ).setCaption( Messages.getString( getClass(), "assigned" ) );
	}

	protected RendererClickListener createAsignmentClickListener() {
		return new RendererClickListener() {

			@Override
			public void click(RendererClickEvent event) {
				UserRoleBean urb = (UserRoleBean)event.getItem();
				if ( urb.getParent() == null ) {
					System.out.println("clicked " + urb.getRoleCode() + " of " + urb.getUsername());
					onUserClicked( urb );
				} else {
					System.out.println("clicked " + urb.getRoleCode() + " of " + urb.getParent().getUsername());
					onUserRoleClicked( urb );
				}
				
			}
			
		};
	}
	protected void onUserClicked(UserRoleBean urb) {
		// TODO Auto-generated method stub
		
	}
	protected void onUserRoleClicked(UserRoleBean urb) {
		// TODO Auto-generated method stub
		
	}

	protected String getRoleByValue(int rv) {
		Messages.setLang( BaseUI.get().getLocale().getLanguage() );
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
		HeaderRow filterHeader = appendHeaderRow();
		TextField tf = new TextField();
		tf.setWidth("100%");
		Messages.setLang( BaseUI.get().getLocale().getLanguage() );
		tf.setPlaceholder( Messages.getString(getClass(), "filter"));
		UsersTreeGrid utg = this;
		TreeDataProvider<UserRoleBean> dp = (TreeDataProvider<UserRoleBean>) getDataProvider();
		tf.addValueChangeListener( new ValueChangeListener<String>() {
			
			@Override
			public void valueChange(ValueChangeEvent<String> event) {
				// This will work ONLY if there is just ONE filter in Grid. If more are required this needs to be revised!!!
				if (event.getValue() == null)
					dp.setFilter( null );
				else {
					if (dp.getFilter() == null)
						dp.addFilter( UserRoleBean::getUsername, s -> caseInsensitiveContains(s, event.getValue()) );
					else
						dp.setFilter( UserRoleBean::getUsername, s -> caseInsensitiveContains(s, event.getValue()) );
				}
			}
		});
		filterHeader.getCell("username").setComponent( tf );
	}
	private boolean caseInsensitiveContains(String where, String what) {
        return TLZUtils.isEmpty( where ) || TLZUtils.caseInsensitiveContains(where, what);
    }
}
