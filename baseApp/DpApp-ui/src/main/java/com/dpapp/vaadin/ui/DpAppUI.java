package com.dpapp.vaadin.ui;

import java.util.Properties;

import javax.servlet.annotation.WebServlet;

import com.dpapp.vaadin.layout.DpAppLayout;
import com.tooooolazy.util.Messages;
import com.tooooolazy.vaadin.layout.TopAndLeftMenuLayout;
import com.tooooolazy.vaadin.resources.Resources;
import com.tooooolazy.vaadin.ui.BaseUI;
import com.tooooolazy.vaadin.ui.MenuItemKeys;
import com.tooooolazy.vaadin.views.MainView;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.Resource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;

import elemental.json.Json;
import elemental.json.JsonArray;
import elemental.json.JsonObject;

@Theme("baseTheme")
public class DpAppUI extends BaseUI<TopAndLeftMenuLayout> {

	static {
//		Messages.setSupportedLocales( new String[] {"en", "el", "bg"});
		try {
			Messages.addBundle(DpAppUI.class.getPackage().getName() + ".app");
		} catch(Exception e) {
			// no default resource bundles... but never mind
		}
	}

	@Override
	protected void init(VaadinRequest vaadinRequest) {
		super.init( vaadinRequest );
	}

	@Override
	protected DpAppLayout createRootLayout() {
		return new DpAppLayout();
	}

	@Override
	protected Class getMainViewClass() {
		return MainView.class;
	}

	@WebServlet(urlPatterns = "/*", name = "DpAppUIServlet", asyncSupported = true)
	@VaadinServletConfiguration(ui = DpAppUI.class, productionMode = false)
	public static class BaseUIServlet extends VaadinServlet {
	}

	@Override
	protected Resource getLogoResource() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Resource getSecLogoResource() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Properties getEmailConfigProperties() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean supportsLocaleSwitching() {
		return true;
	}

	@Override
	public Resource getLocalSwitchResource() {
		return Resources.getPng("img/actions/", "toggleLang3");
	}

	@Override
	public Resource getLoginResource() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Resource getLogoutResource() {
		// TODO Auto-generated method stub
		return null;
	}

	public JsonArray getViewMenuRelations() {
		// TODO get this structure from WS or DB
		JsonArray ja = Json.createArray();
		
		return ja;
	}
	protected JsonArray getViewDefinitions() {
		// TODO get this structure from WS or DB
		JsonArray ja = Json.createArray();

		JsonObject jo = Json.createObject();
		jo.put(MenuItemKeys.VIEW_CLASS, "com.dpapp.vaadin.views.trends.PerformanceView");
		jo.put(MenuItemKeys.VIEW_CLASS_ID, 1);
		jo.put(MenuItemKeys.VIEW_SECURE, false);
//		jo.put(MenuItemKeys.VIEW_BADGE, "2");
		jo.put(MenuItemKeys.VIEW_SUB_TITLE, true);
		ja.set(0, jo);

		jo = Json.createObject();
		jo.put(MenuItemKeys.VIEW_CLASS, "com.dpapp.vaadin.views.trends.SoldRevenueView");
		jo.put(MenuItemKeys.VIEW_CLASS_ID, 2);
		jo.put(MenuItemKeys.VIEW_CLASS_PARENT_ID, 1);
		jo.put(MenuItemKeys.VIEW_SECURE, false);
//		jo.put(MenuItemKeys.VIEW_BADGE, "2");
		ja.set(1, jo);

		jo = Json.createObject();
		jo.put(MenuItemKeys.VIEW_CLASS, "com.dpapp.vaadin.views.trends.OvertimeView");
		jo.put(MenuItemKeys.VIEW_CLASS_ID, 3);
		jo.put(MenuItemKeys.VIEW_CLASS_PARENT_ID, 2);
		jo.put(MenuItemKeys.VIEW_SECURE, false);
//		jo.put(MenuItemKeys.VIEW_BADGE, "2");
		ja.set(2, jo);

		jo = Json.createObject();
		jo.put(MenuItemKeys.VIEW_CLASS, "com.dpapp.vaadin.views.trends.BySkuView");
		jo.put(MenuItemKeys.VIEW_CLASS_ID, 3);
		jo.put(MenuItemKeys.VIEW_CLASS_PARENT_ID, 2);
		jo.put(MenuItemKeys.VIEW_SECURE, false);
//		jo.put(MenuItemKeys.VIEW_BADGE, "2");
		ja.set(3, jo);

		return ja;
	}
}
