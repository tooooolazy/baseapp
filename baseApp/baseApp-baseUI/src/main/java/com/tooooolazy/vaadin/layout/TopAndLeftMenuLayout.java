package com.tooooolazy.vaadin.layout;

import com.tooooolazy.vaadin.ui.AppLayout;
import com.tooooolazy.vaadin.ui.BaseUI;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.Navigator;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ValoTheme;

import elemental.json.JsonArray;

public abstract class TopAndLeftMenuLayout extends GridLayout implements AppLayout {
	protected CssLayout contentArea = new CssLayout();
	protected GridLayout top_gl, sub_gl;

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
	 * Supports Logo image and a Title
	 */
	protected HorizontalLayout headerTitle;

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
		setRowExpandRatio(0, 0f);
		setRowExpandRatio(1, 0f);
		setRowExpandRatio(2, 1f);

		contentArea.setSizeFull();
		headerTitle = new HorizontalLayout();

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
	public HorizontalLayout getHeaderTitle() {
		return headerTitle;
	}

	public void createMenuItems(JsonArray viewDefinitions, Navigator navigator) {
		Component tmi = createTopMenuItems( navigator );
		if ( tmi != null )
			top_gl.addComponent( tmi, 1,0 );
		Component smi = createSubMenuItems( navigator );
		if ( smi != null )
			sub_gl.addComponent( smi, 1,0, 2,0 );

		AppLayout.super.createMenuItems(viewDefinitions, navigator);
		
	}
	protected void addHeader() {
		top_gl = createHeaderTop();
		sub_gl = createHeaderSubTop();

		addComponent( top_gl, 0,0, 3,0);
		addComponent( sub_gl, 0,1, 3,1);
	}

	protected GridLayout createHeaderTop() {
		GridLayout top_gl = new GridLayout(4,1);
		top_gl.setResponsive( true );
		top_gl.setSpacing( true );
		top_gl.setId("header_top");
		top_gl.addStyleName("header_top");
		top_gl.setWidth("100%");
		top_gl.setColumnExpandRatio(0, 0f);
		top_gl.setColumnExpandRatio(1, 1f);
		top_gl.setColumnExpandRatio(2, 0f);
		top_gl.setColumnExpandRatio(3, 0f);

		Image logoi = ((BaseUI)getUI()).getLogoImage();
		if (logoi != null) {
			if ( getHeaderTitle() != null ) {
				getHeaderTitle().addComponent(logoi);
				top_gl.addComponent( getHeaderTitle(), 0,0);
			}
			logoi.addStyleName("top_logo");
			logoi.addStyleName("clickable");
			logoi.addClickListener( getLogoClickListener() );

			String titleStr = ((BaseUI)getUI()).getTitlePlain();
			if (titleStr == null)
				titleStr = "No Title";
			final Label title = new Label(titleStr);
			title.addStyleName("header-title");
			if ( getHeaderTitle() != null ) {
				getHeaderTitle().addComponent(title);
				getHeaderTitle().setComponentAlignment(title, Alignment.MIDDLE_LEFT);
				getHeaderTitle().setExpandRatio(title, 1);
			}
		}

		Label bell = new Label( VaadinIcons.BELL.getHtml(), ContentMode.HTML );
		bell.addStyleName(ValoTheme.LABEL_LARGE);
//		bell.setWidth("20px");
		Label user = new Label( VaadinIcons.USER.getHtml(), ContentMode.HTML );
		user.addStyleName(ValoTheme.LABEL_LARGE);
//		user.setWidth("20px");
		top_gl.addComponent( bell, 2,0);
		top_gl.addComponent( user, 3,0);
		return top_gl;
	}

	protected GridLayout createHeaderSubTop() {
		GridLayout sub_gl = new GridLayout(3,1);
		sub_gl.setResponsive( true );
		sub_gl.setId("header_subtop");
		sub_gl.setSpacing( true );
		sub_gl.setWidth("100%");
		sub_gl.setColumnExpandRatio(0, 0f);
		sub_gl.setColumnExpandRatio(1, 1f);
		sub_gl.setColumnExpandRatio(2, 0f);
		Label el = new Label("");
		el.setWidth("100px");
		sub_gl.addComponent( el, 0,0);
		return sub_gl;
	}
	protected abstract Component createTopMenuItems(Navigator navigator);
	protected abstract Component createSubMenuItems(Navigator navigator);

	protected void removeHeader() {
		// removals should match additions in 'addHeader'
		removeComponent( 0,0 );
		removeComponent( 1,0 );
//		removeComponent( 1,1 );
	}

}
