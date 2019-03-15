package com.tooooolazy.vaadin.ui;

import java.util.List;

import com.tooooolazy.vaadin.components.MenuBudgeButton;
import com.vaadin.event.MouseEvents;
import com.vaadin.navigator.Navigator;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.themes.ValoTheme;

import elemental.json.JsonArray;
import elemental.json.JsonObject;

/**
 * Abstract (Interface) for a generic App Layout
 * @author gpatoulas
 *
 */
public interface AppLayout extends Component {

	public ComponentContainer getContentContainer();

	public CssLayout getMenuItemsLayout();
	public CssLayout getMenuArea();
	public CssLayout getMenu();
	public HorizontalLayout getMenuTitle();

	public AbstractLayout getTopMenuLayout();
	public AbstractLayout getSubMenuLayout();

	public AppLayoutHelper getHelper();

	public default void createMenuStructure(BaseUI ui) {
		getHelper().createMenuStructure( ui );
//		getMenu().addStyleName(ValoTheme.MENU_PART);
//		getMenuArea().addComponent( getMenu() );
//
//		if ( getMenuTitle() != null ) {
//			getMenuTitle().setWidth("100%");
//			getMenuTitle().setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
//			getMenuTitle().addStyleName(ValoTheme.MENU_TITLE);
//			getMenu().addComponent( getMenuTitle() );
//		}
//
//		Image logoi = ui.getLogoImage();
//		if (logoi != null) {
//			if ( getMenuTitle() != null )
//				getMenuTitle().addComponent(logoi);
//			logoi.addStyleName("logo");
//			logoi.addStyleName("clickable");
//			logoi.addClickListener( getLogoClickListener() );
//		}
//		Image logoseci = ui.getLogoSecImage();
//		if (logoseci != null) {
//			if ( getMenuTitle() != null ) {
//				getMenuTitle().addComponent(logoseci);
//				getMenuTitle().setExpandRatio(logoseci, 1);
//			}
//			logoseci.addStyleName("seclogo");
//			logoseci.addStyleName("clickable");
//			logoseci.addClickListener( getLogoClickListener() );
//		} else {
//			String titleStr = ui.getTitleHtml();
//			if (titleStr == null)
//				titleStr = "No Title";
//			final Label title = new Label(titleStr, ContentMode.HTML);
//			if ( getMenuTitle() != null ) {
//				getMenuTitle().addComponent(title);
//				getMenuTitle().setExpandRatio(title, 1);
//			}
//		}
//
//		createShowMenuButton();
	}

	/**
	 * Creates a Menu based on the given viewDefinitions. {@link #clearStructureMaps()} should be called before this one. 
	 * @param viewDefinitions
	 * @param navigator
	 */
	public default void createMenuItems(JsonArray viewDefinitions, Navigator navigator) {
		getHelper().createMenuItems(viewDefinitions, navigator);

//		getMenuItemsLayout().setPrimaryStyleName("valo-menuitems");
//		getMenu().addComponent( getMenuItemsLayout() );
//
//		int toAdd = viewDefinitions.length();
//		int added = 0;
//		// first add parent items (those with parentId == 0)
//		for (int i = 0; i < viewDefinitions.length(); i++) {
//			JsonObject vd = viewDefinitions.getObject(i);
//
//			String vc = vd.get(MenuItemKeys.VIEW_CLASS).asString();
//			Class c = getViewClass(vc);
//			if (c == null) {
//				toAdd--;
//				continue;
//			}
//
//			Logger.getAnonymousLogger().info( "Handling: " + c.getSimpleName());
//
//			int parentId = (int) (vd.get(MenuItemKeys.VIEW_CLASS_PARENT_ID) == null ? 0
//					: vd.get(MenuItemKeys.VIEW_CLASS_PARENT_ID).asNumber());
//			int classId = (int) (vd.get(MenuItemKeys.VIEW_CLASS_ID) == null ? 0
//					: vd.get(MenuItemKeys.VIEW_CLASS_ID).asNumber());
//
//			if ( parentId > 0 ) {
//				classIdToParentId.put(classId, parentId);
//				continue;
//			}
//
//			// add it
//			addMenuItem( c, classId, parentId, vd, navigator );
//			added++;
//		}
//		// then start adding elements to each parent
//		while ( added < toAdd ) {
//			for (int i = 0; i < viewDefinitions.length(); i++) {
//				JsonObject vd = viewDefinitions.getObject(i);
//
//				String vc = vd.get(MenuItemKeys.VIEW_CLASS).asString();
//				Class c = getViewClass(vc);
//				if (c == null) {
//					toAdd--;
//					continue;
//				}
//
//				Logger.getAnonymousLogger().info( "Handling 2: " + c.getSimpleName());
//
//				int parentId = (int) (vd.get(MenuItemKeys.VIEW_CLASS_PARENT_ID) == null ? 0
//						: vd.get(MenuItemKeys.VIEW_CLASS_PARENT_ID).asNumber());
//				int classId = (int) (vd.get(MenuItemKeys.VIEW_CLASS_ID) == null ? 0
//						: vd.get(MenuItemKeys.VIEW_CLASS_ID).asNumber());
//				if ( parentId == 0 )
//					continue;
//
//				// add it
//				addMenuItem( c, classId, parentId, vd, navigator );
//				added++;
//			}
//		}
//		List<Integer> done = new ArrayList<Integer>();
//		for (Integer parentId : parentIdContainer.keySet() ) {
//			Component menuItem = viewIdToComponent.get( parentId );
//			if ( parentId > 0 && !done.contains( parentId ) && menuItem instanceof Button) {
//				int parentCount = getParentCount( parentId );
//				if ( hasTopMenu() && parentCount <= 1 ) {
//					// do nothing
//				} else {
//					done.add( parentId );
//					CssLayout cl = (CssLayout)parentIdContainer.get( parentId );
//					((Button)menuItem).addClickListener( createToggleListener(parentId, cl) );
//					((Button)menuItem).setIcon( VaadinIcons.MINUS );
//				}
//			}
//		}
//
//		createSettingsMenuBar();
	}
	public boolean hasTopMenu();

	public default void addMenuItem(Class c, int classId, int parentId, JsonObject vd, Navigator navigator) {
		getHelper().addMenuItem(c, classId, parentId, vd, navigator);

//		boolean secure = vd.get(MenuItemKeys.VIEW_SECURE) == null ? false
//				: vd.get(MenuItemKeys.VIEW_SECURE).asBoolean();
//		boolean subTitle = vd.get(MenuItemKeys.VIEW_SUB_TITLE) == null ? false
//				: vd.get(MenuItemKeys.VIEW_SUB_TITLE).asBoolean();
//		
//		int badge = (int) (vd.get(MenuItemKeys.VIEW_BADGE) == null ? 0
//				: vd.get(MenuItemKeys.VIEW_BADGE).asNumber());
//
//		Logger.getAnonymousLogger().info( "Adding: " + c.getSimpleName());
//
//		if ( secure )
//			((BaseUI)getUI()).setHasSecureContent();
//
//		Component menuItem = null;
//
//		int parentCount = getParentCount( classId );
//		// if layout has top menu add it there. if not add it in left menu
//		if ( hasTopMenu() && parentCount <= 1 ) {
//			if ( parentCount == 0 ) {
//				menuItem = createTopMenuButton( navigator, badge, c);
//				getTopMenuLayout().addComponent( menuItem );
//			} else {
//				menuItem = createSubMenuButton( navigator, badge, c);
//				getSubMenuLayout().addComponent( menuItem );
//			}
//			navigator.addView(c.getSimpleName(), c);
//		} else {
//	
//			if (subTitle) {
//				menuItem = createMenuSubtitle(badge, c);
//	
//				if ( parentId == 0 )
//					getMenuItemsLayout().addComponent(menuItem);
//				else {
//					CssLayout cl = getParentContainer(parentId);
//					cl.addComponent(menuItem);
//				}
//			} else {
//				menuItem = createMenuButton(navigator, badge, c);
//				navigator.addView(c.getSimpleName(), c);
//	
//				if ( parentId == 0 )
//					getMenuItemsLayout().addComponent(menuItem, getInsertIndex( parentId ) );
//				else {
//					CssLayout cl = getParentContainer(parentId);
//					cl.addComponent(menuItem);
//				}
//			}
//		}
//		viewClassIds.put(c, classId);
//		viewSelectors.put(c, menuItem);
//		viewIdToComponent.put(classId, menuItem);
//		viewIdToClass.put(classId, c);
//		componentToParentId.put(menuItem, parentId);
	}
	/**
	 * Based on an App Parameter and Menu structure defined in {@link BaseUI#getViewDefinitions}, a simple menu bar is added with icons to toggle language and login/logout
	 */
	public default void createSettingsMenuBar() {
		getHelper().createSettingsMenuBar();

//		final MenuBar settings = new MenuBar();
//		settings.addStyleName("user-menu");
//
//		if (BaseUI.get().supportsLocaleSwitching()) {
//			final MenuItem toggleLang = settings.addItem("", BaseUI.get().getLocalSwitchResource(),
//					new ToggleLocaleCommand());
//			toggleLang.setDescription(Messages.getString("toggleLang"));
//		}
//
//		if ( ((BaseUI)getUI()).hasSecureContent() ) {
//			LoginCommand lic = new LoginCommand();
//			if (BaseUI.get().getLoginResource() == null) {
//				throw new NoLoginResourceException();
//			}
//			if (BaseUI.get().getLogoutResource() == null) {
//				throw new NoLogoutResourceException();
//			}
//
//			final MenuItem login = settings.addItem("", BaseUI.get().getLoginResource(), lic);
//			login.setDescription(Messages.getString("InitiateLoginButton.loginTitle"));
//
//			LogoutCommand loc = new LogoutCommand();
//			final MenuItem logout = settings.addItem("", BaseUI.get().getLogoutResource(), loc);
//			logout.setDescription(Messages.getString("InitiateLoginButton.logoutTitle"));
//			logout.setVisible(false);
//
//			lic.setLogoutItem(logout);
//			loc.setLoginItem(login);
//		}
//
////		final MenuItem settingsItem = settings.addItem("", VaadinIcons.USER, null);
////		settingsItem.addSeparator();
////		settingsItem.addItem("Sign Out", null);
//
//		getMenu().addComponent(settings);
	}

	/**
	 * Helper to count how many parents a view has. Needed in constructing a combination of top and side menus
	 * @param classId
	 * @return
	 */
	public default int getParentCount(int classId ) {
		return getHelper().getParentCount( classId );

//		Integer pId = classIdToParentId.get( classId );
//		if ( pId != null)
//			return getParentCount( pId ) + 1;
//
//		return 0;
	}

	public default CssLayout getParentContainer(int parentId) {
		return getHelper().getParentContainer(parentId);

//		CssLayout cl = (CssLayout)parentIdContainer.get( parentId );
//		if ( cl == null ) {
//			cl = new CssLayout();
//			cl.setWidth("100%");
//			cl.setId("mi_" + parentId );
//
//			setSubItemsPadding();
//
//			parentIdContainer.put( parentId,  cl );
//			
//			Integer pId = classIdToParentId.get( parentId );
//			if ( pId == null )
//				getMenuItemsLayout().addComponent(cl, getInsertIndex( parentId ) );
//			else {
//				int parentCount = getParentCount( parentId );
//				AbstractLayout pcl = null;
//				if ( hasTopMenu() && parentCount == 1 ) {
//					pcl = getMenuItemsLayout();
//				} else {
//					pcl = (CssLayout)parentIdContainer.get( pId );
//				}
//				pcl.addComponent( cl );
//			}
//		}
//		return cl;
	}

	public default int getInsertIndex(int parentId) {
		return getHelper().getInsertIndex(parentId);

//		// get component from id: 
//		Component parentComponent = viewIdToComponent.get(parentId);
//		// get index of parent component
//		int i = getMenuItemsLayout().getComponentIndex(parentComponent)+1;
//
//		for (int c=i; c<getMenuItemsLayout().getComponentCount(); c++) {
//			Component _c = getMenuItemsLayout().getComponent( c );
//
//			if ( componentToParentId.get( _c ) == parentId )
//				i = c + 1;
//		}
//		
//		return i;
	}

	public default void createShowMenuButton() {
		getHelper().createShowMenuButton();

//		final Button showMenu = new Button("", new ClickListener() {
//			@Override
//			public void buttonClick(final ClickEvent event) {
//				if (getMenu().getStyleName().contains("valo-menu-visible")) {
//					getMenu().removeStyleName("valo-menu-visible");
//				} else {
//					getMenu().addStyleName("valo-menu-visible");
//				}
//			}
//		});
////		showMenu.addStyleName(ValoTheme.BUTTON_PRIMARY);
//		showMenu.addStyleName(ValoTheme.BUTTON_SMALL);
//		showMenu.addStyleName("valo-menu-toggle");
//		showMenu.setIcon(VaadinIcons.LINES_LIST);
//		getMenu().addComponent(showMenu);
	}
	public default com.vaadin.event.MouseEvents.ClickListener getLogoClickListener() {
		return new MouseEvents.ClickListener() {
			@Override
			public void click(com.vaadin.event.MouseEvents.ClickEvent event) {
				getUI().getNavigator().navigateTo( "" );
			}
		};
	}
//	/**
//	 * Used to 'mark' selected View
//	 */
//	public Map<Class, Component> viewSelectors = new HashMap<Class, Component>();
//
//	/**
//	 * Each App View has a class (eg com.tooooolazy.vaadin.views.AboutView) and each class an ID (defined in {@link BaseUI#getViewDefinitions} )
//	 */
//	public Map<Class, Integer> viewClassIds = new HashMap<Class, Integer>();
//	/**
//	 * the oposite of {@link #viewClassIds}
//	 */
//	public Map<Integer, Class> viewIdToClass = new HashMap<Integer, Class>();
//	public Map<Integer, Component> viewIdToComponent = new HashMap<Integer, Component>();
//	public Map<Component, Integer> componentToParentId = new HashMap<Component, Integer>();
//	/**
//	 * Holds the container of parent's sub elements
//	 */
//	public Map<Integer, Component> parentIdContainer = new HashMap<Integer, Component>();
//	public Map<Integer, Integer> classIdToParentId = new HashMap<Integer, Integer>();

	/**
	 * Clears Maps needed to generate the Menu.
	 */
	public default void clearStructureMaps() {
		getHelper().clearStructureMaps();

//		classIdToParentId.clear();
//		viewIdToComponent.clear();
//		componentToParentId.clear();
//		parentIdContainer.clear();
//		classIdToParentId.clear();
	}

	public default Class getViewClass(String vc) {
		Class c = null;
		try {
			c = Class.forName(vc);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return c;
	}

	public default ClickListener createToggleListener(int parentId, CssLayout cl) {
		return getHelper().createToggleListener(parentId, cl);
//		return new ClickListener() {
//			
//			@Override
//			public void buttonClick(ClickEvent event) {
//				cl.setVisible( !cl.isVisible() );
//				MenuBudgeButton mbb = ((MenuBudgeButton)(viewIdToComponent.get(parentId)));
//				mbb.setIcon( cl.isVisible() ? VaadinIcons.MINUS : VaadinIcons.PLUS );
//				setSubItemsPadding();
//			}
//		};
	}
	/**
	 * Call it once all left menu items are created (and their children) in order to set children's correct padding. 
	 * Should also be called if a menu item becomes visible.
	 */
	public default void setSubItemsPadding() {
		getHelper().setSubItemsPadding();

//		for ( Integer classId : classIdToParentId.keySet() ) {
//			int padding = 30;
//			int cId = classId;
//			
//			while ( classIdToParentId.get( cId ) != null ) {
//				cId = classIdToParentId.get( cId );
//				if ( !(viewIdToComponent.get( cId ) instanceof Label) )
//					JavaScript.getCurrent().execute("document.getElementById('mi_" + cId + "').style.paddingLeft='" + padding + "px'");
//			}
//		}
	}

	/**
	 * Creates a menu item to place inside Left side menu
	 * @param navigator
	 * @param badge
	 * @param c
	 * @return
	 */
	public default Button createMenuButton(Navigator navigator, int badge, Class c) {
		return getHelper().createMenuButton(navigator, badge, c);

//		MenuBudgeButton vb = new MenuBudgeButton(c, new ClickListener() {
//			@Override
//			public void buttonClick(final ClickEvent event) {
//				// do that ONLY if it has no children
//				Integer pId = viewClassIds.get( c );
//				if ( pId != null ) { 
//					if ( parentIdContainer.get( pId ) != null )
//						return;
//				}
//				navigator.navigateTo(c.getSimpleName());
//			}
//		}, badge > 0 ? badge+"":null);
//		vb.setPrimaryStyleName(ValoTheme.MENU_ITEM);
//
//		return vb;
	}
	/**
	 * Creates a menu item to place inside Header's top menu
	 * @param navigator
	 * @param badge
	 * @param c
	 * @return
	 */
	public default Button createTopMenuButton(Navigator navigator, int badge, Class c) {
		MenuBudgeButton vb = new MenuBudgeButton(c, new ClickListener() {
			@Override
			public void buttonClick(final ClickEvent event) {
				navigator.navigateTo(c.getSimpleName());
			}
		}, null);
//		navigator.addView(c.getSimpleName(), c);
		vb.setPrimaryStyleName(ValoTheme.MENU_ITEM);
		vb.addStyleName("top-menu-item");

//		viewSelectors.put(c.getSimpleName(), vb);

		return vb;
	}
	/**
	 * Creates a menu item to place inside Header's sub menu
	 * @param navigator
	 * @param badge
	 * @param c
	 * @return
	 */
	public default Button createSubMenuButton(Navigator navigator, int badge, Class c) {
		MenuBudgeButton vb = new MenuBudgeButton(c, new ClickListener() {
			@Override
			public void buttonClick(final ClickEvent event) {
				navigator.navigateTo(c.getSimpleName());
			}
		}, null);
//		navigator.addView(c.getSimpleName(), c);
		vb.setPrimaryStyleName(ValoTheme.MENU_ITEM);
		vb.addStyleName("sub-menu");

//		viewSelectors.put(c.getSimpleName(), vb);

		return vb;
	}

	public default Component createMenuSubtitle(int badge, Class c) {
		return getHelper().createMenuSubtitle(badge, c);

//		Component menuItem;
//		Label label = new Label(Messages.getString(c, "page.title"), ContentMode.HTML);
//		label.setPrimaryStyleName(ValoTheme.MENU_SUBTITLE);
//		label.setSizeUndefined();
//		if (badge > 0) {
//			label.setValue(label.getValue() + " <span class=\"valo-menu-badge\">" + badge + "</span>");
//			label.setCaptionAsHtml(true);
//		}
//		menuItem = label;
//		viewSelectors.put(c, menuItem);
//		return menuItem;
	}
	/**
	 * Highlights a view navigation button as the currently active view in the menu.
	 * This method does not perform the actual navigation.
	 *
	 * @param viewClass the class of the view to show as active
	 */
	public default void setActiveView(Class viewClass, boolean clearFirst) {
		getHelper().setActiveView(viewClass, clearFirst);

//		if ( clearFirst ) {
//			for (Component button : viewSelectors.values()) {
//				button.removeStyleName("selected");
//			}
//		}
//		Component selected = viewSelectors.get(viewClass);
//		int cId = viewClassIds.get( viewClass );
//		int parentCount = getParentCount( cId );
//		if (selected != null) {
//			if ( hasTopMenu() && parentCount <= 2 || clearFirst ) 
//				selected.addStyleName("selected");
//		}
//		Integer pId = classIdToParentId.get( cId );
//		if ( pId != null && pId > 0 ) {
//			setActiveView( viewIdToClass.get( pId ), false );
//		}
	}
	/**
	 * Hides (or shows) a menu Items. It also affects their subitems (if any). ie hiding a menu item that has children will also hide the children
	 * @param c th class of the View to toggle visibility
	 * @param visible if true View will be shown
	 */
	public default void toggleMenuItem(Class c, boolean visible) {
		getHelper().toggleMenuItem(c, visible);

//		Component _c = viewSelectors.get( c );
//		if ( _c != null ) {
//			_c.setVisible( visible );
//			Integer pId = viewClassIds.get( c );
//			if ( pId != null ) {
//				Component _pc = parentIdContainer.get( pId );
//				if ( _pc != null ) {
//					_pc.setVisible( visible );
//				}
//			}
//		}
	}
	public default void toggleChildMenuItems(Class c, boolean visible) {
		getHelper().toggleChildMenuItems(c, visible);

//		if ( hasTopMenu() ) {
//			Integer cId = viewClassIds.get( c );
//			int parentCount = getParentCount( cId );
//			
//			if ( parentCount == 0 ) {
//				// siblings are ok - we need to toggle related items in submenu
//
//				toggleSubmenu(visible, cId);
//				toggleLeftMenu(!visible, cId);
//			} else {
//				while ( parentCount > 1) {
//					cId = classIdToParentId.get( cId );
//					parentCount = getParentCount( cId );
//				}
//				// toggle left menu 
//				toggleLeftMenu(visible, cId);
//				// toggle siblings
//				toggleSubmenu( visible, classIdToParentId.get( cId ) );
//			}
//			setSubItemsPadding();
//		}
	}

	public default void toggleSubmenu(boolean visible, Integer cId) {
		getHelper().toggleSubmenu(visible, cId);

//		// toggle submenu + toggle siblings' submenus
//		
//		List<Class> siblings = getSiblings( 1 ); // ie items in submenu
//		for ( Class _c : siblings ) {
//			Integer _cId = viewClassIds.get( _c );
//
//			toggleMenuItem(_c, cId.equals( classIdToParentId.get( _cId ) ) );
//		}
	}

	public default void toggleLeftMenu(boolean visible, Integer cId) {
		getHelper().toggleLeftMenu(visible, cId);

//		List<Class> siblings = getSiblings( 2 ); // ie items in left menu
//		for ( Class _c : siblings ) {
//			Integer _cId = viewClassIds.get( _c );
//
//			Component _pc = parentIdContainer.get( _cId );
//			if ( _pc != null ) {
//				_pc.setVisible( visible );
//			}
//			_pc = parentIdContainer.get( classIdToParentId.get( _cId ) );
//			if ( _pc != null ) {
//				_pc.setVisible( visible );
//			}
//
//			Component _ic = viewIdToComponent.get( _cId );
//			if ( _ic != null ) {
//				toggleMenuItem(_c, cId.equals( classIdToParentId.get( _cId ) ) );
//			}
//		}
	}

	public default List<Class> getSiblings(int parentCount) {
		return getHelper().getSiblings(parentCount);

//		List<Class> siblings = new ArrayList<Class>();
//
//		for ( Class _c : viewClassIds.keySet() ) {
//			Integer cId = viewClassIds.get( _c );
//			int _parentCount = getParentCount( cId );
//			if ( _parentCount == parentCount )
//				siblings.add( _c );
//		}
//		return siblings;
	}

	/**
	 * @param viewClass
	 * @return null if given view has no parent
	 */
	public default Class getParentViewClass(Class viewClass) {
		return getHelper().getParentViewClass(viewClass);

//		Class pClass = null;
//
//		Integer cId = viewClassIds.get( viewClass );
//		if ( cId != null ) {
//			Integer pId = classIdToParentId.get( cId );
//			if ( pId != null )
//				pClass = viewIdToClass.get( pId );
//		}
//
//		return pClass;
	}
}
