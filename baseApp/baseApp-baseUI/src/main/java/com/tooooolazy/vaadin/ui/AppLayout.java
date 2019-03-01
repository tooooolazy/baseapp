package com.tooooolazy.vaadin.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tooooolazy.util.Messages;
import com.tooooolazy.vaadin.commands.LoginCommand;
import com.tooooolazy.vaadin.commands.LogoutCommand;
import com.tooooolazy.vaadin.commands.ToggleLocaleCommand;
import com.tooooolazy.vaadin.components.MenuBudgeButton;
import com.tooooolazy.vaadin.exceptions.NoLoginResourceException;
import com.tooooolazy.vaadin.exceptions.NoLogoutResourceException;
import com.vaadin.event.MouseEvents;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.Navigator;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
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

public interface AppLayout extends Component {

	public ComponentContainer getContentContainer();

	public CssLayout getMenuItemsLayout();
	public CssLayout getMenuArea();
	public CssLayout getMenu();
	public HorizontalLayout getMenuTitle();

	public default void createMenuStructure(BaseUI ui) {
		getMenu().addStyleName(ValoTheme.MENU_PART);
		getMenuArea().addComponent( getMenu() );

		if ( getMenuTitle() != null ) {
			getMenuTitle().setWidth("100%");
			getMenuTitle().setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
			getMenuTitle().addStyleName(ValoTheme.MENU_TITLE);
			getMenu().addComponent( getMenuTitle() );
		}

		Image logoi = ui.getLogoImage();
		if (logoi != null) {
			if ( getMenuTitle() != null )
				getMenuTitle().addComponent(logoi);
			logoi.addStyleName("logo");
			logoi.addStyleName("clickable");
			logoi.addClickListener( getLogoClickListener() );
		}
		Image logoseci = ui.getLogoSecImage();
		if (logoseci != null) {
			if ( getMenuTitle() != null ) {
				getMenuTitle().addComponent(logoseci);
				getMenuTitle().setExpandRatio(logoseci, 1);
			}
			logoseci.addStyleName("seclogo");
			logoseci.addStyleName("clickable");
			logoseci.addClickListener( getLogoClickListener() );
		} else {
			String titleStr = ui.getTitleHtml();
			if (titleStr == null)
				titleStr = "No Title";
			final Label title = new Label(titleStr, ContentMode.HTML);
			if ( getMenuTitle() != null ) {
				getMenuTitle().addComponent(title);
				getMenuTitle().setExpandRatio(title, 1);
			}
		}

		createShowMenuButton();
	}

	/**
	 * Creates a Menu based on the given viewDefinitions. {@link #clearStructureMaps()} should be called before this one. 
	 * @param viewDefinitions
	 * @param navigator
	 */
	public default void createMenuItems(JsonArray viewDefinitions, Navigator navigator) {

		getMenuItemsLayout().setPrimaryStyleName("valo-menuitems");
		getMenu().addComponent( getMenuItemsLayout() );

		int toAdd = viewDefinitions.length();
		int added = 0;
		// first add parent items (those with parentId == 0)
		for (int i = 0; i < viewDefinitions.length(); i++) {
			JsonObject vd = viewDefinitions.getObject(i);

			String vc = vd.get(MenuItemKeys.VIEW_CLASS).asString();
			Class c = getViewClass(vc);
			if (c == null) {
				toAdd--;
				continue;
			}

			int parentId = (int) (vd.get(MenuItemKeys.VIEW_CLASS_PARENT_ID) == null ? 0
					: vd.get(MenuItemKeys.VIEW_CLASS_PARENT_ID).asNumber());
			int classId = (int) (vd.get(MenuItemKeys.VIEW_CLASS_ID) == null ? 0
					: vd.get(MenuItemKeys.VIEW_CLASS_ID).asNumber());

			if ( parentId > 0 ) {
				classIdToParentId.put(classId, parentId);
				continue;
			}

			// add it
			addMenuItem( c, classId, parentId, vd, navigator );
			added++;
		}
		// then start adding elements to each parent
		while ( added < toAdd ) {
			for (int i = 0; i < viewDefinitions.length(); i++) {
				JsonObject vd = viewDefinitions.getObject(i);

				String vc = vd.get(MenuItemKeys.VIEW_CLASS).asString();
				Class c = getViewClass(vc);
				if (c == null) {
					toAdd--;
					continue;
				}

				int parentId = (int) (vd.get(MenuItemKeys.VIEW_CLASS_PARENT_ID) == null ? 0
						: vd.get(MenuItemKeys.VIEW_CLASS_PARENT_ID).asNumber());
				int classId = (int) (vd.get(MenuItemKeys.VIEW_CLASS_ID) == null ? 0
						: vd.get(MenuItemKeys.VIEW_CLASS_ID).asNumber());
				if ( parentId == 0 )
					continue;

				// add it
				addMenuItem( c, classId, parentId, vd, navigator );
				added++;
			}
		}
		List<Integer> done = new ArrayList<Integer>();
		for (Integer parentId : parentIdContainer.keySet() ) {
			Component menuItem = viewIdToComponent.get( parentId );
			if ( parentId > 0 && !done.contains( parentId ) && menuItem instanceof Button) {
				done.add( parentId );
				CssLayout cl = (CssLayout)parentIdContainer.get( parentId );
				((Button)menuItem).addClickListener( createToggleListener(parentId, cl) );
				((Button)menuItem).setIcon( VaadinIcons.MINUS );
			}
		}

		createSettingsMenuBar();
	}
	public default void addMenuItem(Class c, int classId, int parentId, JsonObject vd, Navigator navigator) {
		boolean secure = vd.get(MenuItemKeys.VIEW_SECURE) == null ? false
				: vd.get(MenuItemKeys.VIEW_SECURE).asBoolean();
		boolean subTitle = vd.get(MenuItemKeys.VIEW_SUB_TITLE) == null ? false
				: vd.get(MenuItemKeys.VIEW_SUB_TITLE).asBoolean();
		
		int badge = (int) (vd.get(MenuItemKeys.VIEW_BADGE) == null ? 0
				: vd.get(MenuItemKeys.VIEW_BADGE).asNumber());
		
		if ( secure )
			((BaseUI)getUI()).setHasSecureContent();

		Component menuItem = null;
		if (subTitle) {
			menuItem = createMenuSubtitle(badge, c);

			if ( parentId == 0 )
				getMenuItemsLayout().addComponent(menuItem);
			else {
				CssLayout cl = getParentContainer(parentId);
				cl.addComponent(menuItem);
			}
		} else {
			menuItem = createMenuButton(navigator, badge, c);
			navigator.addView(c.getSimpleName(), c);

			if ( parentId == 0 )
				getMenuItemsLayout().addComponent(menuItem, getInsertIndex( parentId ) );
			else {
				CssLayout cl = getParentContainer(parentId);
				cl.addComponent(menuItem);
			}
		}
		viewClassIds.put(c, classId);
		viewSelectors.put(c.getSimpleName(), menuItem);
		viewIdToComponent.put(classId, menuItem);
		viewIdToClass.put(classId, c);
		componentToParentId.put(menuItem, parentId);
	}
	/**
	 * Based on an App Parameter and Menu structure defined in {@link BaseUI#getViewDefinitions}, a simple menu bar is added with icons to toggle language and login/logout
	 */
	public default void createSettingsMenuBar() {
		final MenuBar settings = new MenuBar();
		settings.addStyleName("user-menu");

		if (BaseUI.get().supportsLocaleSwitching()) {
			final MenuItem toggleLang = settings.addItem("", BaseUI.get().getLocalSwitchResource(),
					new ToggleLocaleCommand());
			toggleLang.setDescription(Messages.getString("toggleLang"));
		}

		if ( ((BaseUI)getUI()).hasSecureContent() ) {
			LoginCommand lic = new LoginCommand();
			if (BaseUI.get().getLoginResource() == null) {
				throw new NoLoginResourceException();
			}
			if (BaseUI.get().getLogoutResource() == null) {
				throw new NoLogoutResourceException();
			}

			final MenuItem login = settings.addItem("", BaseUI.get().getLoginResource(), lic);
			login.setDescription(Messages.getString("InitiateLoginButton.loginTitle"));

			LogoutCommand loc = new LogoutCommand();
			final MenuItem logout = settings.addItem("", BaseUI.get().getLogoutResource(), loc);
			logout.setDescription(Messages.getString("InitiateLoginButton.logoutTitle"));
			logout.setVisible(false);

			lic.setLogoutItem(logout);
			loc.setLoginItem(login);
		}

//		final MenuItem settingsItem = settings.addItem("", VaadinIcons.USER, null);
//		settingsItem.addSeparator();
//		settingsItem.addItem("Sign Out", null);

		getMenu().addComponent(settings);
	}

	public default CssLayout getParentContainer(int parentId) {
		CssLayout cl = (CssLayout)parentIdContainer.get( parentId );
		if ( cl == null ) {
			cl = new CssLayout();
			cl.setWidth("100%");
			cl.setId("mi_" + parentId );

			setSubItemsPadding();

			parentIdContainer.put( parentId,  cl );
			
			Integer pId = classIdToParentId.get( parentId );
			if ( pId == null )
				getMenuItemsLayout().addComponent(cl, getInsertIndex( parentId ) );
			else {
				CssLayout pcl = (CssLayout)parentIdContainer.get( pId );
				pcl.addComponent( cl );
			}
		}
		return cl;
	}

	public default int getInsertIndex(int parentId) {
		// get component from id: 
		Component parentComponent = viewIdToComponent.get(parentId);
		// get index of parent component
		int i = getMenuItemsLayout().getComponentIndex(parentComponent)+1;

		for (int c=i; c<getMenuItemsLayout().getComponentCount(); c++) {
			Component _c = getMenuItemsLayout().getComponent( c );

			if ( componentToParentId.get( _c ) == parentId )
				i = c + 1;
		}
		
		return i;
	}

	public default void createShowMenuButton() {
		final Button showMenu = new Button("", new ClickListener() {
			@Override
			public void buttonClick(final ClickEvent event) {
				if (getMenu().getStyleName().contains("valo-menu-visible")) {
					getMenu().removeStyleName("valo-menu-visible");
				} else {
					getMenu().addStyleName("valo-menu-visible");
				}
			}
		});
//		showMenu.addStyleName(ValoTheme.BUTTON_PRIMARY);
		showMenu.addStyleName(ValoTheme.BUTTON_SMALL);
		showMenu.addStyleName("valo-menu-toggle");
		showMenu.setIcon(VaadinIcons.LINES_LIST);
		getMenu().addComponent(showMenu);
	}
	public default com.vaadin.event.MouseEvents.ClickListener getLogoClickListener() {
		return new MouseEvents.ClickListener() {
			@Override
			public void click(com.vaadin.event.MouseEvents.ClickEvent event) {
				getUI().getNavigator().navigateTo( "" );
			}
		};
	}
	/**
	 * Used to 'mark' selected View
	 */
	public Map<String, Component> viewSelectors = new HashMap<String, Component>();

	/**
	 * Each App View has a class (eg com.tooooolazy.vaadin.views.AboutView) and each class an ID (defined in {@link BaseUI#getViewDefinitions} )
	 */
	public Map<Class, Integer> viewClassIds = new HashMap<Class, Integer>();
	/**
	 * the oposite of {@link #viewClassIds}
	 */
	public Map<Integer, Class> viewIdToClass = new HashMap<Integer, Class>();
	public Map<Integer, Component> viewIdToComponent = new HashMap<Integer, Component>();
	public Map<Component, Integer> componentToParentId = new HashMap<Component, Integer>();
	/**
	 * Holds the container of parent's sub elements
	 */
	public Map<Integer, Component> parentIdContainer = new HashMap<Integer, Component>();
	public Map<Integer, Integer> classIdToParentId = new HashMap<Integer, Integer>();

	/**
	 * Clears Maps needed to generate the Menu.
	 */
	public default void clearStructureMaps() {
		classIdToParentId.clear();
		viewIdToComponent.clear();
		componentToParentId.clear();
		parentIdContainer.clear();
		classIdToParentId.clear();
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
		return new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				cl.setVisible( !cl.isVisible() );
				MenuBudgeButton mbb = ((MenuBudgeButton)(viewIdToComponent.get(parentId)));
				mbb.setIcon( cl.isVisible() ? VaadinIcons.MINUS : VaadinIcons.PLUS );
				setSubItemsPadding();
			}
		};
	}
	public default void setSubItemsPadding() {

		for ( Integer classId : classIdToParentId.keySet() ) {
			int padding = 30;
			int cId = classId;
			
			while ( classIdToParentId.get( cId ) != null ) {
				cId = classIdToParentId.get( cId );
				if ( !(viewIdToComponent.get( cId ) instanceof Label) )
					JavaScript.getCurrent().execute("document.getElementById('mi_" + cId + "').style.paddingLeft='" + padding + "px'");
			}
		}
	}

	public default Button createMenuButton(Navigator navigator, int badge, Class c) {
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
	public default Button createTopMenuButton(Navigator navigator, int badge, Class c) {
		MenuBudgeButton vb = new MenuBudgeButton(c, new ClickListener() {
			@Override
			public void buttonClick(final ClickEvent event) {
				navigator.navigateTo(c.getSimpleName());
			}
		}, null);
		navigator.addView(c.getSimpleName(), c);
		vb.setPrimaryStyleName(ValoTheme.MENU_ITEM);

		viewSelectors.put(c.getSimpleName(), vb);

		return vb;
	}
	public default Button createSubMenuButton(Navigator navigator, int badge, Class c) {
		MenuBudgeButton vb = new MenuBudgeButton(c, new ClickListener() {
			@Override
			public void buttonClick(final ClickEvent event) {
				navigator.navigateTo(c.getSimpleName());
			}
		}, null);
		navigator.addView(c.getSimpleName(), c);
		vb.setPrimaryStyleName(ValoTheme.MENU_ITEM);

		viewSelectors.put(c.getSimpleName(), vb);

		return vb;
	}

	public default Component createMenuSubtitle(int badge, Class c) {
		Component menuItem;
		Label label = new Label(Messages.getString(c, "page.title"), ContentMode.HTML);
		label.setPrimaryStyleName(ValoTheme.MENU_SUBTITLE);
		label.setSizeUndefined();
		if (badge > 0) {
			label.setValue(label.getValue() + " <span class=\"valo-menu-badge\">" + badge + "</span>");
			label.setCaptionAsHtml(true);
		}
		menuItem = label;
		viewSelectors.put(c.getSimpleName(), menuItem);
		return menuItem;
	}
	/**
	 * Highlights a view navigation button as the currently active view in the menu.
	 * This method does not perform the actual navigation.
	 *
	 * @param viewName the name of the view to show as active
	 */
	public default void setActiveView(String viewName) {
		for (Component button : viewSelectors.values()) {
			button.removeStyleName("selected");
		}
		Component selected = viewSelectors.get(viewName);
		if (selected != null) {
			selected.addStyleName("selected");
		}
	}
	/**
	 * Hides (or shows) a menu Items. It also affects their subitems (if any). ie hiding a menu item that has children will also hide the children
	 * @param c th class of the View to toggle visibility
	 * @param visible if true View will be shown
	 */
	public default void toggleMenuItem(Class c, boolean visible) {
		Component _c = viewSelectors.get( c.getSimpleName() );
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
	public default Class getParentViewClass(Class viewClass) {
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
