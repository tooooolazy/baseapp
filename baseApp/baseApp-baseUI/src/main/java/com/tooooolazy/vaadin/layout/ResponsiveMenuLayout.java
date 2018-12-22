package com.tooooolazy.vaadin.layout;

import java.util.HashMap;
import java.util.Map;

import com.tooooolazy.util.Messages;
import com.tooooolazy.vaadin.commands.LoginCommand;
import com.tooooolazy.vaadin.commands.LogoutCommand;
import com.tooooolazy.vaadin.commands.ToggleLocaleCommand;
import com.tooooolazy.vaadin.exceptions.NoLoginResourceException;
import com.tooooolazy.vaadin.exceptions.NoLogoutResourceException;
import com.tooooolazy.vaadin.ui.BaseUI;
import com.tooooolazy.vaadin.ui.MenuItemKeys;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.Resource;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.themes.ValoTheme;

import elemental.json.JsonArray;
import elemental.json.JsonObject;

public class ResponsiveMenuLayout extends HorizontalLayout {

	protected CssLayout menu;
	protected CssLayout menuItemsLayout;
	protected CssLayout menuArea;
	protected HorizontalLayout menuTitle;

	protected CssLayout contentArea = new CssLayout();

	protected Resource logoResource, secLogoResource, loginResource, logoutResource;

	/**
	 * Used to 'mark' selected View
	 */
	private Map<String, Component> viewSelectors = new HashMap<String, Component>();

	private Map<String, Integer> viewClassIds = new HashMap<String, Integer>();
	private Map<Integer, String> viewIdToClass = new HashMap<Integer, String>();
	private Map<Integer, Component> viewIdToComponent = new HashMap<Integer, Component>();
	private Map<Component, Integer> componentToParentId = new HashMap<Component, Integer>();

	protected JsonArray viewDefinitions;
	protected JsonObject menuDefinitions;
	protected boolean hasSecureContent;

	public ResponsiveMenuLayout() {
		setSizeFull();
		setWidth("100%");

		contentArea.setPrimaryStyleName("valo-content");
		contentArea.addStyleName("v-scrollable");
		contentArea.setSizeFull();

		addComponent(contentArea);
		setExpandRatio(contentArea, 1);
	}

	public void attach() {
		super.attach();

		addComponents();
	}

	private void addComponents() {
		menu = new CssLayout();
		menuItemsLayout = new CssLayout();
		menuArea = new CssLayout();
		menuTitle = new HorizontalLayout();
		menuArea.setPrimaryStyleName(ValoTheme.MENU_ROOT);
//		menuArea.addStyleName(ValoTheme.MENU_APPEAR_ON_HOVER);

		addComponent(menuArea, 0);

		createMenuStructure(BaseUI.get());
	}

	public void refresh() {
		removeComponent(menuArea);

		addComponents();
	}

	public ComponentContainer getContentContainer() {
		return contentArea;
	}

	protected void createMenuStructure(BaseUI ui) {
		menu.addStyleName(ValoTheme.MENU_PART);
		menuArea.addComponent(menu);

		menuTitle.setWidth("100%");
		menuTitle.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
		menuTitle.addStyleName(ValoTheme.MENU_TITLE);
		menu.addComponent(menuTitle);

		Image logoi = ui.getLogoImage();
		if (logoi != null) {
			menuTitle.addComponent(logoi);
			logoi.addStyleName("logo");
		}
		Image logoseci = ui.getLogoSecImage();
		if (logoseci != null) {
			logoseci.addStyleName("seclogo");
			menuTitle.addComponent(logoseci);
			menuTitle.setExpandRatio(logoseci, 1);
		} else {
			String titleStr = ui.getTitleHtml();
			if (titleStr == null)
				titleStr = "No Title";
			final Label title = new Label(titleStr, ContentMode.HTML);
			menuTitle.addComponent(title);
			menuTitle.setExpandRatio(title, 1);
		}

		createShowMenuButton();
	}

	protected void createSettingsMenuBar() {
		final MenuBar settings = new MenuBar();
		settings.addStyleName("user-menu");

		if (BaseUI.get().supportsLocaleSwitching()) {
			final MenuItem toggleLang = settings.addItem("", BaseUI.get().getLocalSwitchResource(),
					new ToggleLocaleCommand());
			toggleLang.setDescription(Messages.getString("toggleLang"));
		}

		if (hasSecureContent()) {
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

		menu.addComponent(settings);
	}

	protected void createShowMenuButton() {
		final Button showMenu = new Button("", new ClickListener() {
			@Override
			public void buttonClick(final ClickEvent event) {
				if (menu.getStyleName().contains("valo-menu-visible")) {
					menu.removeStyleName("valo-menu-visible");
				} else {
					menu.addStyleName("valo-menu-visible");
				}
			}
		});
//		showMenu.addStyleName(ValoTheme.BUTTON_PRIMARY);
		showMenu.addStyleName(ValoTheme.BUTTON_SMALL);
		showMenu.addStyleName("valo-menu-toggle");
		showMenu.setIcon(VaadinIcons.LINES_LIST);
		menu.addComponent(showMenu);
	}

	public void createMenuItems(JsonArray viewDefinitions, Navigator navigator) {
		this.viewDefinitions = viewDefinitions;

		menuItemsLayout.setPrimaryStyleName("valo-menuitems");
		menu.addComponent(menuItemsLayout);

		for (int i = 0; i < viewDefinitions.length(); i++) {
			JsonObject vd = viewDefinitions.getObject(i);

			String vc = vd.get(MenuItemKeys.VIEW_CLASS).asString();
			boolean secure = vd.get(MenuItemKeys.VIEW_SECURE) == null ? false
					: vd.get(MenuItemKeys.VIEW_SECURE).asBoolean();
			boolean subTitle = vd.get(MenuItemKeys.VIEW_SUB_TITLE) == null ? false
					: vd.get(MenuItemKeys.VIEW_SUB_TITLE).asBoolean();

			int badge = (int) (vd.get(MenuItemKeys.VIEW_BADGE) == null ? 0
					: vd.get(MenuItemKeys.VIEW_BADGE).asNumber());
			int classId = (int) (vd.get(MenuItemKeys.VIEW_CLASS_ID) == null ? 0
					: vd.get(MenuItemKeys.VIEW_CLASS_ID).asNumber());
			int parentId = (int) (vd.get(MenuItemKeys.VIEW_CLASS_PARENT_ID) == null ? 0
					: vd.get(MenuItemKeys.VIEW_CLASS_PARENT_ID).asNumber());

			Class c = getViewClass(vc);
			if (c == null)
				continue;

			hasSecureContent = hasSecureContent || secure;

			Component menuItem = null;
			if (subTitle) {
				menuItem = createMenuSubtitle(badge, c);

				menuItemsLayout.addComponent(menuItem);
			} else {
				menuItem = createMenuButton(navigator, badge, c);
				navigator.addView(c.getSimpleName(), c);

				menuItemsLayout.addComponent(menuItem, getInsertIndex( parentId ) );
			}
			viewClassIds.put(c.getSimpleName(), classId);
			viewSelectors.put(c.getSimpleName(), menuItem);
			viewIdToComponent.put(classId, menuItem);
			viewIdToClass.put(classId, c.getSimpleName());
			componentToParentId.put(menuItem, parentId);
		}
		createSettingsMenuBar();
	}

	private Button createMenuButton(Navigator navigator, int badge, Class c) {
		Button vb = new Button(Messages.getString(c, "page.title"), new ClickListener() {
			@Override
			public void buttonClick(final ClickEvent event) {
				navigator.navigateTo(c.getSimpleName());
			}
		});
		vb.setPrimaryStyleName(ValoTheme.MENU_ITEM);

		if (badge > 0) {
			vb.setCaption(vb.getCaption() + " <span class=\"valo-menu-badge\">" + badge + "</span>");
			vb.setCaptionAsHtml(true);
		}
		return vb;
	}

	private Component createMenuSubtitle(int badge, Class c) {
		Component menuItem;
		Label label = new Label(Messages.getString(c, "page.title"), ContentMode.HTML);
		label.setPrimaryStyleName(ValoTheme.MENU_SUBTITLE);
		label.setSizeUndefined();
		if (badge > 0) {
			label.setValue(label.getValue() + " <span class=\"valo-menu-badge\">" + badge + "</span>");
			label.setCaptionAsHtml(true);
		}
		menuItem = label;
		return menuItem;
	}

	private Class getViewClass(String vc) {
		Class c = null;
		try {
			c = Class.forName(vc);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return c;
	}

	protected int getInsertIndex(int parentId) {
		// get component from id: 
		Component parentComponent = viewIdToComponent.get(parentId);
		// get index of parent component
		int i = menuItemsLayout.getComponentIndex(parentComponent)+1;

		for (int c=i; c<menuItemsLayout.getComponentCount(); c++) {
			Component _c = menuItemsLayout.getComponent( c );

			if ( componentToParentId.get( _c ) == parentId )
				i = c + 1;
		}
		
		return i;
	}

	/**
	 * Highlights a view navigation button as the currently active view in the menu.
	 * This method does not perform the actual navigation.
	 *
	 * @param viewName the name of the view to show as active
	 */
	public void setActiveView(String viewName) {
		for (Component button : viewSelectors.values()) {
			button.removeStyleName("selected");
		}
		Component selected = viewSelectors.get(viewName);
		if (selected != null) {
			selected.addStyleName("selected");
		}
	}

	/**
	 * If true is returned, then 'login' and 'logout' functionality should be added
	 * (ie through actions in menu).
	 * 
	 * @return
	 */
	public boolean hasSecureContent() {
		return hasSecureContent;
	}
}
