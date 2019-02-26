package com.tooooolazy.vaadin.layout;

import java.util.ArrayList;
import java.util.List;

import com.tooooolazy.vaadin.ui.AppLayout;
import com.tooooolazy.vaadin.ui.BaseUI;
import com.tooooolazy.vaadin.ui.MenuItemKeys;
import com.vaadin.event.MouseEvents;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.Navigator;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ValoTheme;

import elemental.json.JsonArray;
import elemental.json.JsonObject;

public class TopAndLeftMenuLayout extends GridLayout implements AppLayout {
	protected CssLayout contentArea = new CssLayout();

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

	protected boolean hasSecureContent;

	public TopAndLeftMenuLayout() {
		super(4, 3);
		setId("AppLayout");
		init();
	}

	protected void init() {
		setSizeFull();
		setColumnExpandRatio(0, 0f);
		setColumnExpandRatio(1, .001f);
		setColumnExpandRatio(2, 1f);
		setColumnExpandRatio(3, .1f);
		setRowExpandRatio(0, .01f);
		setRowExpandRatio(1, .01f);
		setRowExpandRatio(2, 1f);

		contentArea.setSizeFull();

		addComponent( contentArea, 2,2, 3,2);
	}

	public void attach() {
		super.attach();

		addLayoutComponents();
	}
	private void addLayoutComponents() {
		addHeader();

		menu = new CssLayout();
		menuItemsLayout = new CssLayout();
		menuArea = new CssLayout();
//		menuTitle = new HorizontalLayout();
		menuArea.setPrimaryStyleName(ValoTheme.MENU_ROOT);

		addComponent( menuArea, 0, 2, 1,2);
		createMenuStructure(BaseUI.get());
	}

	public void refresh() {
		removeHeader();

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
		return null;
	}

	protected void addHeader() {
		addComponent( new Label("logo"), 0,0);
		addComponent( new Label("top menu"), 1,0, 3,0);
		addComponent( new Label("sub menu"), 1,1, 3,1);
	}
	protected void removeHeader() {
		removeComponent( 0,0 );
		removeComponent( 1,0 );
		removeComponent( 1,1 );
	}

}
