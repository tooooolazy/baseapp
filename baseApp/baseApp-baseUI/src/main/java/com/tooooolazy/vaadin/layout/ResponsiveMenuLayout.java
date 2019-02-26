package com.tooooolazy.vaadin.layout;

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
import com.tooooolazy.vaadin.ui.AppLayout;
import com.tooooolazy.vaadin.ui.BaseUI;
import com.tooooolazy.vaadin.ui.MenuItemKeys;
import com.vaadin.event.MouseEvents;
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

/**
 * Defines the Layout of the Application. With a responsive Menu and of course the content area
 * @author tooooolazy
 *
 */
public class ResponsiveMenuLayout extends HorizontalLayout implements AppLayout {

	/**
	 * Component that holds a {@link #menuTitle}, a {@link #menuItemsLayout} with all menu items that are basically links to the different App Views and a menuBar with language Toggle and login/logout icons defined in {@link #createSettingsMenuBar}
	 */
	protected CssLayout menu;
	/**
	 * Parent component for all menu items
	 */
	protected CssLayout menuItemsLayout;
	/**
	 * The 1st component to be added in 'this' layout. Will hold all menu related components (ie {@link #menu} component)
	 */
	protected CssLayout menuArea;
	/**
	 * Supports up to 2 Logo images and a Title
	 */
	protected HorizontalLayout menuTitle;

	/**
	 * The Component (container) where all App Views will be displayed
	 */
	protected CssLayout contentArea = new CssLayout();

	protected Resource logoResource, secLogoResource, loginResource, logoutResource;

//	protected boolean hasSecureContent;

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

		addLayoutComponents();
	}

	private void addLayoutComponents() {
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

		addLayoutComponents();
	}

	@Override
	public ComponentContainer getContentContainer() {
		return contentArea;
	}

	@Override
	public CssLayout getMenuItemsLayout() {
		return menuItemsLayout;
	}
	@Override
	public CssLayout getMenuArea() {
		return menuArea;
	}
	@Override
	public CssLayout getMenu() {
		return menu;
	}
	@Override
	public HorizontalLayout getMenuTitle() {
		return menuTitle;
	}

}
