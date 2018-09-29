package com.tooooolazy.vaadin.layout;

import com.tooooolazy.util.Messages;
import com.tooooolazy.vaadin.commands.LoginCommand;
import com.tooooolazy.vaadin.commands.LogoutCommand;
import com.tooooolazy.vaadin.commands.ToggleLocaleCommand;
import com.tooooolazy.vaadin.exceptions.NoLoginResourceException;
import com.tooooolazy.vaadin.exceptions.NoLogoutResourceException;
import com.tooooolazy.vaadin.ui.BaseUI;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Resource;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.themes.ValoTheme;

public class ResponsiveMenuLayout extends HorizontalLayout {

	protected CssLayout menu;
	protected CssLayout menuItemsLayout;
	protected CssLayout menuArea;
	protected HorizontalLayout menuTitle;

	protected CssLayout contentArea = new CssLayout();

	protected Resource	logoResource, secLogoResource,
						loginResource, logoutResource;

	public ResponsiveMenuLayout() {
		setSizeFull();
		setWidth("100%");

		contentArea.setPrimaryStyleName("valo-content");
		contentArea.addStyleName("v-scrollable");
		contentArea.setSizeFull();

		addComponent( contentArea);
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

		createMenuStructure( BaseUI.get() );
		
		createMenuItems();
		
		createSettingsMenuBar();
	}
	
	public void refresh() {
		removeComponent( menuArea );

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
			if ( titleStr == null )
				titleStr = "No Title";
			final Label title = new Label( titleStr, ContentMode.HTML );
			menuTitle.addComponent(title);
			menuTitle.setExpandRatio(title, 1);	
		}

		createShowMenuButton();
	}

	protected void createSettingsMenuBar() {
		final MenuBar settings = new MenuBar();
		settings.addStyleName("user-menu");

		if (BaseUI.get().supportsLocaleSwitching()) {
			final MenuItem toggleLang = settings.addItem("", BaseUI.get().getLocalSwitchResource(), new ToggleLocaleCommand() );
			toggleLang.setDescription( Messages.getString("toggleLang") );
		}

		if (BaseUI.get().hasSecureContent()) {
			LoginCommand lic = new LoginCommand();
			if (BaseUI.get().getLoginResource() == null) {
	            throw new NoLoginResourceException();
	        }
			if (BaseUI.get().getLogoutResource() == null) {
	            throw new NoLogoutResourceException();
	        }

			final MenuItem login = settings.addItem("", BaseUI.get().getLoginResource(), lic);
			login.setDescription( Messages.getString("InitiateLoginButton.loginTitle") );

			LogoutCommand loc = new LogoutCommand();
			final MenuItem logout = settings.addItem("", BaseUI.get().getLogoutResource(), loc);
			logout.setDescription( Messages.getString("InitiateLoginButton.logoutTitle") );
			logout.setVisible( false );

			lic.setLogoutItem( logout );
			loc.setLoginItem( login );
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

	protected void createMenuItems() {
		menuItemsLayout.setPrimaryStyleName("valo-menuitems");
		menu.addComponent(menuItemsLayout);

		Label label = new Label("Components", ContentMode.HTML);
		label.setPrimaryStyleName(ValoTheme.MENU_SUBTITLE);
		label.setSizeUndefined();
		menuItemsLayout.addComponent(label);

		final Button b = new Button(Messages.getString("toggleLang"), new ClickListener() {
			@Override
			public void buttonClick(final ClickEvent event) {
//                navigator.navigateTo(item.getKey()); 
			}
		});
		b.setCaption(b.getCaption() + " <span class=\"valo-menu-badge\">123</span>");
		b.setCaptionAsHtml(true);
		b.setPrimaryStyleName(ValoTheme.MENU_ITEM);
		menuItemsLayout.addComponent(b);
	}
}
