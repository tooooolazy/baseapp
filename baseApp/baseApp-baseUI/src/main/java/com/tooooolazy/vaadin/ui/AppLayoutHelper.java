package com.tooooolazy.vaadin.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.tooooolazy.data.ServiceLocator;
import com.tooooolazy.util.Messages;
import com.tooooolazy.vaadin.commands.LoginCommand;
import com.tooooolazy.vaadin.commands.LogoutCommand;
import com.tooooolazy.vaadin.commands.ToggleLocaleCommand;
import com.tooooolazy.vaadin.components.MenuBudgeButton;
import com.tooooolazy.vaadin.exceptions.NoLoginResourceException;
import com.tooooolazy.vaadin.exceptions.NoLogoutResourceException;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.Navigator;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.JavaScript;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.themes.ValoTheme;

import elemental.json.JsonArray;
import elemental.json.JsonObject;

/**
 * Helper class that holds code previously inside {@link AppLayout} interface. The problem (most likely) was the member variables. The result was that when the App was opened from another browser (or even another Tab) the original Tab stopped working as expected! Method comments are left in mentioned interface.
 * @author gpatoulas
 *
 * @see AppLayout
 */
public class AppLayoutHelper {
	protected AppLayout appLayout;

	protected LoginCommand lic;
	protected LogoutCommand loc;

	protected MenuItem login, logout;
	/**
	 * Used to 'mark' selected View
	 */
	public Map<Class, Component> viewSelectors = new HashMap<Class, Component>();

	/**
	 * Each App View has a class (eg com.tooooolazy.vaadin.views.AboutView) and each class an ID (defined in {@link BaseUI#getViewDefinitions} )
	 */
	public Map<Class, Integer> viewClassIds = new HashMap<Class, Integer>();
	/**
	 * the opposite of {@link #viewClassIds}
	 */
	public Map<Integer, Class> viewIdToClass = new HashMap<Integer, Class>();

	public Map<Integer, Component> viewIdToComponent = new HashMap<Integer, Component>();
	public Map<Component, Integer> componentToParentId = new HashMap<Component, Integer>();
	/**
	 * Holds the container of parent's sub elements
	 */
	public Map<Integer, Component> parentIdContainer = new HashMap<Integer, Component>();
	public Map<Integer, Integer> classIdToParentId = new HashMap<Integer, Integer>();

	public AppLayoutHelper(AppLayout appLayout) {
		this.appLayout = appLayout;
	}

	public void createMenuStructure(BaseUI ui) {
		appLayout.getMenu().addStyleName(ValoTheme.MENU_PART);
		appLayout.getMenuArea().addComponent( appLayout.getMenu() );

		if ( appLayout.getMenuTitle() != null ) {
			appLayout.getMenuTitle().setWidth("100%");
			appLayout.getMenuTitle().setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
			appLayout.getMenuTitle().addStyleName(ValoTheme.MENU_TITLE);
			appLayout.getMenu().addComponent( appLayout.getMenuTitle() );
		}

		Image logoi = ui.getLogoImage();
		if (logoi != null) {
			if ( appLayout.getMenuTitle() != null )
				appLayout.getMenuTitle().addComponent(logoi);
			logoi.addStyleName("logo");
			logoi.addStyleName("clickable");
			logoi.addClickListener( appLayout.getLogoClickListener() );
		}
		Image logoseci = ui.getLogoSecImage();
		if (logoseci != null) {
			if ( appLayout.getMenuTitle() != null ) {
				appLayout.getMenuTitle().addComponent(logoseci);
				appLayout.getMenuTitle().setExpandRatio(logoseci, 1);
			}
			logoseci.addStyleName("seclogo");
			logoseci.addStyleName("clickable");
			logoseci.addClickListener( appLayout.getLogoClickListener() );
		} else {
			String titleStr = ui.getTitleHtml();
			if (titleStr == null)
				titleStr = "No Title";
			final Label title = new Label(titleStr, ContentMode.HTML);
			if ( appLayout.getMenuTitle() != null ) {
				appLayout.getMenuTitle().addComponent(title);
				appLayout.getMenuTitle().setExpandRatio(title, 1);
			}
		}

		appLayout.createShowMenuButton();
	}

	/**
	 * @param viewDefinitions
	 * @param navigator
	 * 
	 * @see {@link AppLayout#createMenuItems(JsonArray, Navigator)}
	 */
	public void createMenuItems(JsonArray viewDefinitions, Navigator navigator) {

		appLayout.getMenuItemsLayout().setPrimaryStyleName("valo-menuitems");
		appLayout.getMenu().addComponent( appLayout.getMenuItemsLayout() );

		int toAdd = viewDefinitions.length();
		int added = 0;
		// first add parent items (those with parentId == 0)
		for (int i = 0; i < viewDefinitions.length(); i++) {
			JsonObject vd = viewDefinitions.getObject(i);

			String vc = vd.get(MenuItemKeys.VIEW_CLASS).asString();
			Class c = appLayout.getViewClass(vc);
			if (c == null) {
				toAdd--;
				continue;
			}

			Logger.getAnonymousLogger().info( "Handling: " + c.getSimpleName());

			int parentId = (int) (vd.get(MenuItemKeys.VIEW_CLASS_PARENT_ID) == null ? 0
					: vd.get(MenuItemKeys.VIEW_CLASS_PARENT_ID).asNumber());
			int classId = (int) (vd.get(MenuItemKeys.VIEW_CLASS_ID) == null ? 0
					: vd.get(MenuItemKeys.VIEW_CLASS_ID).asNumber());

			if ( parentId > 0 ) {
				classIdToParentId.put(classId, parentId);
				continue;
			}

			// add it
			appLayout.addMenuItem( c, classId, parentId, vd, navigator );
			added++;
		}
		// then start adding elements to each parent
		while ( added < toAdd ) {
			for (int i = 0; i < viewDefinitions.length(); i++) {
				JsonObject vd = viewDefinitions.getObject(i);

				String vc = vd.get(MenuItemKeys.VIEW_CLASS).asString();
				Class c = appLayout.getViewClass(vc);
				if (c == null) {
					toAdd--;
					continue;
				}

				Logger.getAnonymousLogger().info( "Handling 2: " + c.getSimpleName());

				int parentId = (int) (vd.get(MenuItemKeys.VIEW_CLASS_PARENT_ID) == null ? 0
						: vd.get(MenuItemKeys.VIEW_CLASS_PARENT_ID).asNumber());
				int classId = (int) (vd.get(MenuItemKeys.VIEW_CLASS_ID) == null ? 0
						: vd.get(MenuItemKeys.VIEW_CLASS_ID).asNumber());
				if ( parentId == 0 )
					continue;

				// add it
				appLayout.addMenuItem( c, classId, parentId, vd, navigator );
				added++;
			}
		}
		List<Integer> done = new ArrayList<Integer>();
		for (Integer parentId : parentIdContainer.keySet() ) {
			Component menuItem = viewIdToComponent.get( parentId );
			if ( parentId > 0 && !done.contains( parentId ) && menuItem instanceof Button) {
				int parentCount = appLayout.getParentCount( parentId );
				if ( appLayout.hasTopMenu() && parentCount <= 1 ) {
					// do nothing
				} else {
					done.add( parentId );
					CssLayout cl = (CssLayout)parentIdContainer.get( parentId );
					((Button)menuItem).addClickListener( appLayout.createToggleListener(parentId, cl) );
					((Button)menuItem).setIcon( VaadinIcons.MINUS );
				}
			}
		}

		appLayout.createSettingsMenuBar();
	}

	public void addMenuItem(Class c, int classId, int parentId, JsonObject vd, Navigator navigator) {
		boolean secure = vd.get(MenuItemKeys.VIEW_SECURE) == null ? false
				: vd.get(MenuItemKeys.VIEW_SECURE).asBoolean();
		boolean subTitle = vd.get(MenuItemKeys.VIEW_SUB_TITLE) == null ? false
				: vd.get(MenuItemKeys.VIEW_SUB_TITLE).asBoolean();
		
		int badge = (int) (vd.get(MenuItemKeys.VIEW_BADGE) == null ? 0
				: vd.get(MenuItemKeys.VIEW_BADGE).asNumber());

		Logger.getAnonymousLogger().info( "Adding: " + c.getSimpleName());

		if ( secure )
			BaseUI.get().setHasSecureContent();

		Component menuItem = null;

		int parentCount = appLayout.getParentCount( classId );
		// if layout has top menu add it there. if not add it in left menu
		if ( appLayout.hasTopMenu() && parentCount <= 1 ) {
			if ( parentCount == 0 ) {
				menuItem = appLayout.createTopMenuButton( navigator, badge, c);
				appLayout.getTopMenuLayout().addComponent( menuItem );
			} else {
				menuItem = appLayout.createSubMenuButton( navigator, badge, c);
				appLayout.getSubMenuLayout().addComponent( menuItem );
			}
			navigator.addView(c.getSimpleName(), c);
		} else {
	
			if (subTitle) {
				menuItem = appLayout.createMenuSubtitle(badge, c);
	
				if ( parentId == 0 )
					appLayout.getMenuItemsLayout().addComponent(menuItem);
				else {
					CssLayout cl = appLayout.getParentContainer(parentId);
					cl.addComponent(menuItem);
				}
			} else {
				menuItem = appLayout.createMenuButton(navigator, badge, c);
				navigator.addView(c.getSimpleName(), c);
	
				if ( parentId == 0 )
					appLayout.getMenuItemsLayout().addComponent(menuItem, appLayout.getInsertIndex( parentId ) );
				else {
					CssLayout cl = appLayout.getParentContainer(parentId);
					cl.addComponent(menuItem);
				}
			}
		}
//		viewClassIds.put(c, classId);
//		viewIdToClass.put(classId, c);

		viewSelectors.put(c, menuItem);
		viewIdToComponent.put(classId, menuItem);
		componentToParentId.put(menuItem, parentId);

		if ( BaseUI.get().getCurrentUser() == null )
			menuItem.setVisible( !ServiceLocator.getServices().getSecurityController().isSecure(BaseUI.get().getCurrentUser(), "enter", c) );
	}

	public LoginCommand getLoginCommand() {
		return lic;
	}
	public LogoutCommand getLogoutCommand() {
		return loc;
	}

	public MenuItem getLoginItem() {
		return login;
	}
	public MenuItem getLogoutItem() {
		return logout;
	}

	public void createSettingsMenuBar() {
		final MenuBar settings = new MenuBar();
		settings.addStyleName("user-menu");

		if (BaseUI.get().supportsLocaleSwitching()) {
			final MenuItem toggleLang = settings.addItem("", BaseUI.get().getLocalSwitchResource(),
					new ToggleLocaleCommand());
			toggleLang.setDescription(Messages.getString("toggleLang"));
		}

		if ( BaseUI.get().hasSecureContent() ) {
			lic = new LoginCommand();
			if (BaseUI.get().getLoginResource() == null) {
				throw new NoLoginResourceException();
			}
			if (BaseUI.get().getLogoutResource() == null) {
				throw new NoLogoutResourceException();
			}

			login = settings.addItem("", BaseUI.get().getLoginResource(), lic);
			login.setDescription(Messages.getString("InitiateLoginButton.loginTitle"));

			loc = new LogoutCommand();
			logout = settings.addItem("", BaseUI.get().getLogoutResource(), loc);
			logout.setDescription(Messages.getString("InitiateLoginButton.logoutTitle"));

			// decide which item to show!
			login.setVisible( BaseUI.get().getCurrentUser() == null );
			logout.setVisible( BaseUI.get().getCurrentUser() != null );

			lic.setLogoutItem(logout);
			loc.setLoginItem(login);
		}

//		final MenuItem settingsItem = settings.addItem("", VaadinIcons.USER, null);
//		settingsItem.addSeparator();
//		settingsItem.addItem("Sign Out", null);

		appLayout.addSettingsBar( settings );
	}

	public int getParentCount(int classId ) {
		Integer pId = classIdToParentId.get( classId );
		if ( pId != null)
			return getParentCount( pId ) + 1;

		return 0;
	}

	public CssLayout getParentContainer(int parentId) {
		CssLayout cl = (CssLayout)parentIdContainer.get( parentId );
		if ( cl == null ) {
			cl = new CssLayout();
			cl.setWidth("100%");
			cl.setId("mi_" + parentId );

			appLayout.setSubItemsPadding();

			parentIdContainer.put( parentId,  cl );
			
			Integer pId = classIdToParentId.get( parentId );
			if ( pId == null )
				appLayout.getMenuItemsLayout().addComponent(cl, appLayout.getInsertIndex( parentId ) );
			else {
				int parentCount = getParentCount( parentId );
				AbstractLayout pcl = null;
				if ( appLayout.hasTopMenu() && parentCount == 1 ) {
					pcl = appLayout.getMenuItemsLayout();
				} else {
					pcl = (CssLayout)parentIdContainer.get( pId );
				}
				pcl.addComponent( cl );
			}
		}
		return cl;
	}

	public int getInsertIndex(int parentId) {
		// get component from id: 
		Component parentComponent = viewIdToComponent.get(parentId);
		// get index of parent component
		int i = appLayout.getMenuItemsLayout().getComponentIndex(parentComponent)+1;

		for (int c=i; c<appLayout.getMenuItemsLayout().getComponentCount(); c++) {
			Component _c = appLayout.getMenuItemsLayout().getComponent( c );

			if ( componentToParentId.get( _c ) == parentId )
				i = c + 1;
		}
		
		return i;
	}

	public void createShowMenuButton() {
		final Button showMenu = new Button("", new ClickListener() {
			@Override
			public void buttonClick(final ClickEvent event) {
				if (appLayout.getMenu().getStyleName().contains("valo-menu-visible")) {
					appLayout.getMenu().removeStyleName("valo-menu-visible");
				} else {
					appLayout.getMenu().addStyleName("valo-menu-visible");
				}
			}
		});
//		showMenu.addStyleName(ValoTheme.BUTTON_PRIMARY);
		showMenu.addStyleName(ValoTheme.BUTTON_SMALL);
		showMenu.addStyleName("valo-menu-toggle");
		showMenu.setIcon(VaadinIcons.LINES_LIST);
		appLayout.getMenu().addComponent(showMenu);
	}
	public void clearStructureMaps() {
		classIdToParentId.clear();
		viewIdToComponent.clear();
		componentToParentId.clear();
		parentIdContainer.clear();
	}

	public ClickListener createToggleListener(int parentId, CssLayout cl) {
		return new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				cl.setVisible( !cl.isVisible() );
				MenuBudgeButton mbb = ((MenuBudgeButton)(viewIdToComponent.get(parentId)));
				mbb.setIcon( cl.isVisible() ? VaadinIcons.MINUS : VaadinIcons.PLUS );
				appLayout.setSubItemsPadding();
			}
		};
	}

	public void setSubItemsPadding() {

		for ( Integer classId : classIdToParentId.keySet() ) {
			int padding = 30;
			int cId = classId;

			while ( classIdToParentId.get( cId ) != null ) {
				cId = classIdToParentId.get( cId );

				// fix padding when topMenu enabled!
				int parentCount = appLayout.getParentCount( cId );
				if ( appLayout.hasTopMenu() && parentCount <= 1 || viewIdToComponent.get( cId ) == null)
					continue;

				if ( !(viewIdToComponent.get( cId ) instanceof Label) )
					JavaScript.getCurrent().execute("document.getElementById('mi_" + cId + "').style.paddingLeft='" + padding + "px'");
			}
		}
	}

	public Button createMenuButton(Navigator navigator, int badge, Class c) {
		MenuBudgeButton vb = new MenuBudgeButton(c, new ClickListener() {
			@Override
			public void buttonClick(final ClickEvent event) {
				// do that ONLY if it has no children
				Integer pId = viewClassIds.get( c );
				if ( pId != null ) { 
					if ( parentIdContainer.get( pId ) != null )
						return;
				}
				navigator.navigateTo(c.getSimpleName());
			}
		}, badge > 0 ? badge+"":null);
		vb.setPrimaryStyleName(ValoTheme.MENU_ITEM);

		return vb;
	}

	public Component createMenuSubtitle(int badge, Class c) {
		Component menuItem;
		Label label = new Label(Messages.getString(c, "page.menu.title"), ContentMode.HTML);
		label.setPrimaryStyleName(ValoTheme.MENU_SUBTITLE);
		label.setSizeUndefined();
		if (badge > 0) {
			label.setValue(label.getValue() + " <span class=\"valo-menu-badge\">" + badge + "</span>");
			label.setCaptionAsHtml(true);
		}
		menuItem = label;
		viewSelectors.put(c, menuItem);
		return menuItem;
	}

	public void setActiveView(Class viewClass, boolean clearFirst) {
		if ( clearFirst ) {
			for (Component button : viewSelectors.values()) {
				button.removeStyleName("selected");
			}
		}
		Component selected = viewSelectors.get(viewClass);
		Integer cId = viewClassIds.get( viewClass );
		if (cId == null)
			return;
		int parentCount = appLayout.getParentCount( cId );
		if (selected != null) {
			if ( appLayout.hasTopMenu() && parentCount <= 1 || clearFirst ) 
				selected.addStyleName("selected");
		}
		Integer pId = classIdToParentId.get( cId );
		if ( pId != null && pId > 0 ) {
			setActiveView( viewIdToClass.get( pId ), false );
		}
	}

	public void toggleMenuItem(Class c, boolean visible) {
		Component _c = viewSelectors.get( c );
		if ( _c != null ) {
			_c.setVisible( visible );
			Integer pId = viewClassIds.get( c );
			if ( pId != null ) {
				Component _pc = parentIdContainer.get( pId );
				if ( _pc != null ) {
					_pc.setVisible( visible );
				}
			}
		}
	}

	public void toggleChildMenuItems(Class c, boolean visible) {
		if ( appLayout.hasTopMenu() ) {
			Integer cId = viewClassIds.get( c );
			if ( cId == null ) {
				appLayout.toggleSubmenu(false, 0);
				return;
			}
			int parentCount = appLayout.getParentCount( cId );
			
			if ( parentCount == 0 ) {
				// siblings are ok - we need to toggle related items in submenu

				appLayout.toggleSubmenu(visible, cId);
				appLayout.toggleLeftMenu(!visible, cId);
			} else {
				while ( parentCount > 1) {
					cId = classIdToParentId.get( cId );
					parentCount = appLayout.getParentCount( cId );
				}
				// toggle left menu 
				appLayout.toggleLeftMenu(visible, cId);
				// toggle siblings
				appLayout.toggleSubmenu( visible, classIdToParentId.get( cId ) );
			}
			appLayout.setSubItemsPadding();
		}
	}

	public void toggleSubmenu(boolean visible, Integer cId) {
		// toggle submenu + toggle siblings' submenus
		
		List<Class> siblings = appLayout.getSiblings( 1 ); // ie items in submenu
		for ( Class _c : siblings ) {
			Integer _cId = viewClassIds.get( _c );

			appLayout.toggleMenuItem(_c, cId.equals( classIdToParentId.get( _cId ) ) );
		}
	}

	public void toggleLeftMenu(boolean visible, Integer cId) {
		List<Class> siblings = appLayout.getSiblings( 2 ); // ie items in left menu
		for ( Class _c : siblings ) {
			Integer _cId = viewClassIds.get( _c );

			Component _pc = parentIdContainer.get( _cId );
			if ( _pc != null ) {
				_pc.setVisible( visible );
			}
			_pc = parentIdContainer.get( classIdToParentId.get( _cId ) );
			if ( _pc != null ) {
				_pc.setVisible( visible );
			}

			Component _ic = viewIdToComponent.get( _cId );
			if ( _ic != null ) {
				appLayout.toggleMenuItem(_c, cId.equals( classIdToParentId.get( _cId ) ) );
			}
		}
	}

	public List<Class> getSiblings(int parentCount) {
		List<Class> siblings = new ArrayList<Class>();

		for ( Class _c : viewClassIds.keySet() ) {
			Integer cId = viewClassIds.get( _c );
			int _parentCount = appLayout.getParentCount( cId );
			if ( _parentCount == parentCount )
				siblings.add( _c );
		}
		return siblings;
	}

	public Class getParentViewClass(Class viewClass) {
		Class pClass = null;

		Integer cId = viewClassIds.get( viewClass );
		if ( cId != null ) {
			Integer pId = classIdToParentId.get( cId );
			if ( pId != null )
				pClass = viewIdToClass.get( pId );
		}

		return pClass;
	}
}
