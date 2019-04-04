package com.tooooolazy.vaadin.components;

import com.tooooolazy.data.ServiceLocator;
import com.tooooolazy.data.services.beans.RoleEnum;
import com.tooooolazy.data.services.beans.UserRoleBean;
import com.tooooolazy.util.Messages;
import com.tooooolazy.util.TLZUtils;
import com.tooooolazy.vaadin.resources.Resources;
import com.tooooolazy.vaadin.ui.BaseUI;
import com.vaadin.data.Binder;
import com.vaadin.data.HasValue.ValueChangeEvent;
import com.vaadin.data.HasValue.ValueChangeListener;
import com.vaadin.data.provider.HierarchicalQuery;
import com.vaadin.data.provider.Query;
import com.vaadin.data.provider.TreeDataProvider;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;
import com.vaadin.ui.TreeGrid;
import com.vaadin.ui.components.grid.EditorOpenEvent;
import com.vaadin.ui.components.grid.EditorOpenListener;
import com.vaadin.ui.components.grid.EditorSaveEvent;
import com.vaadin.ui.components.grid.EditorSaveListener;
import com.vaadin.ui.components.grid.FooterCell;
import com.vaadin.ui.components.grid.FooterRow;
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
public abstract class UsersTreeGrid extends TreeGrid<UserRoleBean> {
	protected FooterRow fr;
	protected Binder<UserRoleBean> binder;

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

		getEditor().setBuffered( true );
		getEditor().setEnabled( true );

		getEditor().addOpenListener( new EditorOpenListener<UserRoleBean>() {

			@Override
			public void onEditorOpen(EditorOpenEvent<UserRoleBean> event) {
				if ( BaseUI.get().checkIfHasAccess("editUser", Button.class) ) {
				} else {
					event.getGrid().getEditor().cancel();
					BaseUI.get().notifyPermissionDenied();
				}
				if (event.getBean().getUsername() == null)
					event.getGrid().getEditor().cancel();
			}
			
		});
		
		getEditor().addSaveListener( new EditorSaveListener<UserRoleBean>() {

			@Override
			public void onEditorSave(EditorSaveEvent<UserRoleBean> event) {
				boolean saved = onInnerSaveClicked( event.getBean() );
				if ( !saved ) {
					Messages.setLang( BaseUI.get().getLocale().getLanguage() );
					Notification.show(Messages.getString("save.failed"), Messages.getString("refresh"), Type.ERROR_MESSAGE);
				}
			}
			
		});
	}

	public void attach() {
		super.attach();

		addColumns();
//		adjustMainHeader();
		setGroupHeaders();
		setGroupFooters();

	}

	protected void addColumns() {
		TextField tfDisabled = new TextField();
		tfDisabled.setEnabled( false );
		addColumn( "username" ).setCaption( Messages.getString( getClass(), "username" ) ).setEditorComponent( tfDisabled );

		addColumn( "firstName" ).setCaption( Messages.getString( getClass(), "firstName" ) ).setEditorComponent(new TextField()).setEditable( true );
		addColumn( "lastName" ).setCaption( Messages.getString( getClass(), "lastName" ) ).setEditorComponent(new TextField()).setEditable( true );

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
		if ( BaseUI.get().checkIfHasAccess("editUserRole", Button.class) ) {
			toggleUserRole( urb );
		} else {
			BaseUI.get().notifyPermissionDenied();
		}
	}

	protected void toggleUserRole(UserRoleBean urb) {
		int userCode = urb.getParent().getUserCode();
		int roleCode = urb.getRoleCode();
		boolean newAssignment = !urb.getAssigned();

		String failMsg = userRoleToggled( urb );
		if ( failMsg == null ) {
			urb.setAssigned( newAssignment );
			getDataProvider().refreshItem( urb );
		} else {
			Messages.setLang( BaseUI.get().getLocale().getLanguage());
			Notification.show(Messages.getString( getClass(), "save.failed"), failMsg, Type.ERROR_MESSAGE);
		}
	}

	/**
	 * should call related WS to update user role assignment.
	 * @param urb - the new role assignment
	 * @return WS fail message or null
	 */
	protected abstract String userRoleToggled(UserRoleBean urb);

	protected abstract boolean onInnerSaveClicked(UserRoleBean bean);

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
				HierarchicalQuery hq = new HierarchicalQuery<>(dp.getFilter(), null);
				long count = dp.fetch( hq ).count();
				Messages.setLang( BaseUI.get().getLocale().getLanguage() );
				fr.getCell("username").setText( count + " " + Messages.getString(getClass(), "users") );
			}
		});
		filterHeader.getCell("username").setComponent( tf );
	}
	private boolean caseInsensitiveContains(String where, String what) {
        return TLZUtils.isEmpty( where ) || TLZUtils.caseInsensitiveContains(where, what);
    }

	private void setGroupFooters() {
		fr = prependFooterRow();
		FooterCell fc = fr.getCell("username");
		
		TreeDataProvider<UserRoleBean> dp = (TreeDataProvider<UserRoleBean>) getDataProvider();

		fc.setText( dp.getTreeData().getRootItems().size() + " " + Messages.getString(getClass(), "users") );
	}
}
