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
	}

	/**
	 * Creates a Menu based on the given viewDefinitions. {@link #clearStructureMaps()} should be called before this one. 
	 * @param viewDefinitions
	 * @param navigator
	 */
	public default void createMenuItems(JsonArray viewDefinitions, Navigator navigator) {
		getHelper().createMenuItems(viewDefinitions, navigator);
	}
	public boolean hasTopMenu();

	public default void addMenuItem(Class c, int classId, int parentId, JsonObject vd, Navigator navigator) {
		getHelper().addMenuItem(c, classId, parentId, vd, navigator);
	}
	/**
	 * Based on an App Parameter and Menu structure defined in {@link BaseUI#getViewDefinitions}, a simple menu bar is added with icons to toggle language and login/logout
	 */
	public default void createSettingsMenuBar() {
		getHelper().createSettingsMenuBar();
	}

	/**
	 * Helper to count how many parents a view has. Needed in constructing a combination of top and side menus
	 * @param classId
	 * @return
	 */
	public default int getParentCount(int classId ) {
		return getHelper().getParentCount( classId );
	}

	public default CssLayout getParentContainer(int parentId) {
		return getHelper().getParentContainer(parentId);
	}

	public default int getInsertIndex(int parentId) {
		return getHelper().getInsertIndex(parentId);
	}

	public default void createShowMenuButton() {
		getHelper().createShowMenuButton();
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
	 * Clears Maps needed to generate the Menu.
	 */
	public default void clearStructureMaps() {
		getHelper().clearStructureMaps();
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
	}
	/**
	 * Call it once all left menu items are created (and their children) in order to set children's correct padding. 
	 * Should also be called if a menu item becomes visible.
	 */
	public default void setSubItemsPadding() {
		getHelper().setSubItemsPadding();
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
		vb.setPrimaryStyleName(ValoTheme.MENU_ITEM);
		vb.addStyleName("top-menu-item");

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
		vb.setPrimaryStyleName(ValoTheme.MENU_ITEM);
		vb.addStyleName("sub-menu");

		return vb;
	}

	public default Component createMenuSubtitle(int badge, Class c) {
		return getHelper().createMenuSubtitle(badge, c);
	}
	/**
	 * Highlights a view navigation button as the currently active view in the menu.
	 * This method does not perform the actual navigation.
	 *
	 * @param viewClass the class of the view to show as active
	 */
	public default void setActiveView(Class viewClass, boolean clearFirst) {
		getHelper().setActiveView(viewClass, clearFirst);
	}
	/**
	 * Hides (or shows) a menu Items. It also affects their subitems (if any). ie hiding a menu item that has children will also hide the children
	 * @param c th class of the View to toggle visibility
	 * @param visible if true View will be shown
	 */
	public default void toggleMenuItem(Class c, boolean visible) {
		getHelper().toggleMenuItem(c, visible);
	}
	public default void toggleChildMenuItems(Class c, boolean visible) {
		getHelper().toggleChildMenuItems(c, visible);
	}

	public default void toggleSubmenu(boolean visible, Integer cId) {
		getHelper().toggleSubmenu(visible, cId);
	}

	public default void toggleLeftMenu(boolean visible, Integer cId) {
		getHelper().toggleLeftMenu(visible, cId);
	}

	public default List<Class> getSiblings(int parentCount) {
		return getHelper().getSiblings(parentCount);
	}

	/**
	 * @param viewClass
	 * @return null if given view has no parent
	 */
	public default Class getParentViewClass(Class viewClass) {
		return getHelper().getParentViewClass(viewClass);
	}
}
