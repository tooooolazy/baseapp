package com.dpapp.vaadin.ui;

import java.util.Properties;

import javax.servlet.annotation.WebServlet;

import com.dpapp.vaadin.layout.DpAppLayout;
import com.dpapp.ws.beans.DpAppUserBean;
import com.dpapp.ws.beans.JobFailureCode;
import com.dpapp.ws.beans.OnlineParams;
import com.dpapp.ws.beans.OnlineResult;
import com.tooooolazy.data.services.DataHandler;
import com.tooooolazy.util.Messages;
import com.tooooolazy.vaadin.resources.Resources;
import com.tooooolazy.vaadin.ui.BaseAppServlet;
import com.tooooolazy.vaadin.ui.BaseUI;
import com.tooooolazy.vaadin.ui.MenuItemKeys;
import com.tooooolazy.vaadin.views.MainView;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.Resource;
import com.vaadin.server.VaadinRequest;

import elemental.json.Json;
import elemental.json.JsonArray;
import elemental.json.JsonObject;

/**
 * DpAppUserBean, OnlineResult, JobFailureCode are all defined in DpApp-common module
 * @author gpatoulas
 *
 */
@Theme("baseTheme")
public class DpAppUI extends BaseUI<DpAppLayout, DpAppUserBean, OnlineResult, JobFailureCode> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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
	protected Object generateService(Class srvClass) {
		return super.generateService( srvClass );
	}

	@Override
	protected DataHandler<OnlineResult, OnlineParams> createDataHandler() {
		return new DataHandler(OnlineResult.class) {

			@Override
			protected OnlineParams createOnlineParams() {
				return new OnlineParams();
			}

		};
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
	public static class BaseUIServlet extends BaseAppServlet {
	}

	@Override
	protected Resource getLogoResource() {
		return Resources.get( "img/logo/", "Deepair_logo_POS.jpg" );
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
		return Resources.getPng("img/actions/", "System-Login-icon");
	}

	@Override
	public Resource getLogoutResource() {
		return Resources.getPng("img/actions/", "System-Logout-icon");
	}

	public JsonArray getViewMenuRelations() {
		// TODO get this structure from WS or DB
		JsonArray ja = Json.createArray();
		
		return ja;
	}
	protected JsonArray getViewDefinitions() {
		// TODO get this structure from WS or DB
		JsonArray ja = Json.createArray();

		int index = 0;

		JsonObject jo = Json.createObject();

		// parent elements MUST be declared first!!

		jo = Json.createObject();
		jo.put(MenuItemKeys.VIEW_CLASS, "com.dpapp.vaadin.views.OverviewView");
		jo.put(MenuItemKeys.VIEW_CLASS_ID, 201);
		jo.put(MenuItemKeys.VIEW_SECURE, true);
//		jo.put(MenuItemKeys.VIEW_CLASS_PARENT_ID, 102);
		ja.set(index++, jo);

		jo = Json.createObject();
		jo.put(MenuItemKeys.VIEW_CLASS, "com.dpapp.vaadin.views.TrendsView");
		jo.put(MenuItemKeys.VIEW_CLASS_ID, 202);
		jo.put(MenuItemKeys.VIEW_SECURE, false);
		jo.put(MenuItemKeys.VIEW_CLASS_PARENT_ID, 201);
		ja.set(index++, jo);


		jo = Json.createObject();
		jo.put(MenuItemKeys.VIEW_CLASS, "com.dpapp.vaadin.views.SettingsView");
		jo.put(MenuItemKeys.VIEW_CLASS_ID, 301);
		jo.put(MenuItemKeys.VIEW_SECURE, false);
		ja.set(index++, jo);

		jo = Json.createObject();
		jo.put(MenuItemKeys.VIEW_CLASS, "com.dpapp.vaadin.views.SystemView");
		jo.put(MenuItemKeys.VIEW_CLASS_ID, 302);
		jo.put(MenuItemKeys.VIEW_CLASS_PARENT_ID, 301);
		jo.put(MenuItemKeys.VIEW_SECURE, false);
		ja.set(index++, jo);

		jo = Json.createObject();
		jo.put(MenuItemKeys.VIEW_CLASS, "com.dpapp.vaadin.views.trends.PerformanceView");
		jo.put(MenuItemKeys.VIEW_CLASS_ID, 1);
		jo.put(MenuItemKeys.VIEW_SECURE, false);
		jo.put(MenuItemKeys.VIEW_CLASS_PARENT_ID, 202);
		jo.put(MenuItemKeys.VIEW_SUB_TITLE, true);
		ja.set(index++, jo);

		jo = Json.createObject();
		jo.put(MenuItemKeys.VIEW_CLASS, "com.dpapp.vaadin.views.trends.SoldRevenueView");
		jo.put(MenuItemKeys.VIEW_CLASS_ID, 2);
		jo.put(MenuItemKeys.VIEW_CLASS_PARENT_ID, 1);
		jo.put(MenuItemKeys.VIEW_SECURE, false);
//		jo.put(MenuItemKeys.VIEW_BADGE, "2");
		ja.set(index++, jo);

		jo = Json.createObject();
		jo.put(MenuItemKeys.VIEW_CLASS, "com.dpapp.vaadin.views.trends.OverTimeSR_View");
		jo.put(MenuItemKeys.VIEW_CLASS_ID, 3);
		jo.put(MenuItemKeys.VIEW_CLASS_PARENT_ID, 2);
		jo.put(MenuItemKeys.VIEW_SECURE, false);
//		jo.put(MenuItemKeys.VIEW_BADGE, "2");
		ja.set(index++, jo);

		jo = Json.createObject();
		jo.put(MenuItemKeys.VIEW_CLASS, "com.dpapp.vaadin.views.trends.BySkuSR_View");
		jo.put(MenuItemKeys.VIEW_CLASS_ID, 3);
		jo.put(MenuItemKeys.VIEW_CLASS_PARENT_ID, 2);
		jo.put(MenuItemKeys.VIEW_SECURE, false);
//		jo.put(MenuItemKeys.VIEW_BADGE, "2");
		ja.set(index++, jo);

		jo = Json.createObject();
		jo.put(MenuItemKeys.VIEW_CLASS, "com.dpapp.vaadin.views.trends.ByChannelSR_View");
		jo.put(MenuItemKeys.VIEW_CLASS_ID, 4);
		jo.put(MenuItemKeys.VIEW_CLASS_PARENT_ID, 2);
		jo.put(MenuItemKeys.VIEW_SECURE, false);
//		jo.put(MenuItemKeys.VIEW_BADGE, "2");
		ja.set(index++, jo);

		jo = Json.createObject();
		jo.put(MenuItemKeys.VIEW_CLASS, "com.dpapp.vaadin.views.trends.ByTouchPointSR_View");
		jo.put(MenuItemKeys.VIEW_CLASS_ID, 5);
		jo.put(MenuItemKeys.VIEW_CLASS_PARENT_ID, 2);
		jo.put(MenuItemKeys.VIEW_SECURE, false);
//		jo.put(MenuItemKeys.VIEW_BADGE, "2");
		ja.set(index++, jo);

		jo = Json.createObject();
		jo.put(MenuItemKeys.VIEW_CLASS, "com.dpapp.vaadin.views.trends.ForwardRevenueView");
		jo.put(MenuItemKeys.VIEW_CLASS_ID, 6);
		jo.put(MenuItemKeys.VIEW_CLASS_PARENT_ID, 1);
		jo.put(MenuItemKeys.VIEW_SECURE, false);
//		jo.put(MenuItemKeys.VIEW_BADGE, "2");
		ja.set(index++, jo);

		jo = Json.createObject();
		jo.put(MenuItemKeys.VIEW_CLASS, "com.dpapp.vaadin.views.trends.OverTimeFR_View");
		jo.put(MenuItemKeys.VIEW_CLASS_ID, 7);
		jo.put(MenuItemKeys.VIEW_CLASS_PARENT_ID, 6);
		jo.put(MenuItemKeys.VIEW_SECURE, false);
//		jo.put(MenuItemKeys.VIEW_BADGE, "2");
		ja.set(index++, jo);

		jo = Json.createObject();
		jo.put(MenuItemKeys.VIEW_CLASS, "com.dpapp.vaadin.views.trends.BySkuFR_View");
		jo.put(MenuItemKeys.VIEW_CLASS_ID, 8);
		jo.put(MenuItemKeys.VIEW_CLASS_PARENT_ID, 6);
		jo.put(MenuItemKeys.VIEW_SECURE, false);
//		jo.put(MenuItemKeys.VIEW_BADGE, "2");
		ja.set(index++, jo);

		jo = Json.createObject();
		jo.put(MenuItemKeys.VIEW_CLASS, "com.dpapp.vaadin.views.trends.ByChannelFR_View");
		jo.put(MenuItemKeys.VIEW_CLASS_ID, 9);
		jo.put(MenuItemKeys.VIEW_CLASS_PARENT_ID, 6);
		jo.put(MenuItemKeys.VIEW_SECURE, false);
//		jo.put(MenuItemKeys.VIEW_BADGE, "2");
		ja.set(index++, jo);

		jo = Json.createObject();
		jo.put(MenuItemKeys.VIEW_CLASS, "com.dpapp.vaadin.views.trends.ByTouchPointFR_View");
		jo.put(MenuItemKeys.VIEW_CLASS_ID, 10);
		jo.put(MenuItemKeys.VIEW_CLASS_PARENT_ID, 6);
		jo.put(MenuItemKeys.VIEW_SECURE, false);
//		jo.put(MenuItemKeys.VIEW_BADGE, "2");
		ja.set(index++, jo);

		jo = Json.createObject();
		jo.put(MenuItemKeys.VIEW_CLASS, "com.dpapp.vaadin.views.trends.SoldVolumeView");
		jo.put(MenuItemKeys.VIEW_CLASS_ID, 11);
		jo.put(MenuItemKeys.VIEW_CLASS_PARENT_ID, 1);
		jo.put(MenuItemKeys.VIEW_SECURE, false);
//		jo.put(MenuItemKeys.VIEW_BADGE, "2");
		ja.set(index++, jo);

		jo = Json.createObject();
		jo.put(MenuItemKeys.VIEW_CLASS, "com.dpapp.vaadin.views.trends.OverTimeSV_View");
		jo.put(MenuItemKeys.VIEW_CLASS_ID, 12);
		jo.put(MenuItemKeys.VIEW_CLASS_PARENT_ID, 11);
		jo.put(MenuItemKeys.VIEW_SECURE, false);
//		jo.put(MenuItemKeys.VIEW_BADGE, "2");
		ja.set(index++, jo);

		jo = Json.createObject();
		jo.put(MenuItemKeys.VIEW_CLASS, "com.dpapp.vaadin.views.trends.BySkuSV_View");
		jo.put(MenuItemKeys.VIEW_CLASS_ID, 13);
		jo.put(MenuItemKeys.VIEW_CLASS_PARENT_ID, 11);
		jo.put(MenuItemKeys.VIEW_SECURE, false);
//		jo.put(MenuItemKeys.VIEW_BADGE, "2");
		ja.set(index++, jo);

		jo = Json.createObject();
		jo.put(MenuItemKeys.VIEW_CLASS, "com.dpapp.vaadin.views.trends.ByChannelSV_View");
		jo.put(MenuItemKeys.VIEW_CLASS_ID, 14);
		jo.put(MenuItemKeys.VIEW_CLASS_PARENT_ID, 11);
		jo.put(MenuItemKeys.VIEW_SECURE, false);
//		jo.put(MenuItemKeys.VIEW_BADGE, "2");
		ja.set(index++, jo);

		jo = Json.createObject();
		jo.put(MenuItemKeys.VIEW_CLASS, "com.dpapp.vaadin.views.trends.ByTouchPointSV_View");
		jo.put(MenuItemKeys.VIEW_CLASS_ID, 15);
		jo.put(MenuItemKeys.VIEW_CLASS_PARENT_ID, 11);
		jo.put(MenuItemKeys.VIEW_SECURE, false);
//		jo.put(MenuItemKeys.VIEW_BADGE, "2");
		ja.set(index++, jo);


		jo = Json.createObject();
		jo.put(MenuItemKeys.VIEW_CLASS, "com.dpapp.vaadin.views.trends.ConversionsView");
		jo.put(MenuItemKeys.VIEW_CLASS_ID, 16);
		jo.put(MenuItemKeys.VIEW_CLASS_PARENT_ID, 1);
		jo.put(MenuItemKeys.VIEW_SECURE, false);
//		jo.put(MenuItemKeys.VIEW_BADGE, "2");
		ja.set(index++, jo);



		jo = Json.createObject();
		jo.put(MenuItemKeys.VIEW_CLASS, "com.dpapp.vaadin.views.system.AiConfigView");
		jo.put(MenuItemKeys.VIEW_CLASS_ID, 100);
		jo.put(MenuItemKeys.VIEW_SECURE, false);
		jo.put(MenuItemKeys.VIEW_CLASS_PARENT_ID, 302);
		ja.set(index++, jo);

		jo = Json.createObject();
		jo.put(MenuItemKeys.VIEW_CLASS, "com.dpapp.vaadin.views.system.LearningRoteView");
		jo.put(MenuItemKeys.VIEW_CLASS_ID, 101);
		jo.put(MenuItemKeys.VIEW_SECURE, false);
		jo.put(MenuItemKeys.VIEW_CLASS_PARENT_ID, 100);
		ja.set(index++, jo);

		jo = Json.createObject();
		jo.put(MenuItemKeys.VIEW_CLASS, "com.dpapp.vaadin.views.system.TrainingActivityView");
		jo.put(MenuItemKeys.VIEW_CLASS_ID, 104);
		jo.put(MenuItemKeys.VIEW_SECURE, false);
		jo.put(MenuItemKeys.VIEW_CLASS_PARENT_ID, 100);
		ja.set(index++, jo);

		jo = Json.createObject();
		jo.put(MenuItemKeys.VIEW_CLASS, "com.dpapp.vaadin.views.system.TrainingRewardView");
		jo.put(MenuItemKeys.VIEW_CLASS_ID, 105);
		jo.put(MenuItemKeys.VIEW_SECURE, false);
		jo.put(MenuItemKeys.VIEW_CLASS_PARENT_ID, 100);
		ja.set(index++, jo);

		jo = Json.createObject();
		jo.put(MenuItemKeys.VIEW_CLASS, "com.dpapp.vaadin.views.system.LoggingLevelsVuew");
		jo.put(MenuItemKeys.VIEW_CLASS_ID, 106);
		jo.put(MenuItemKeys.VIEW_SECURE, false);
		jo.put(MenuItemKeys.VIEW_CLASS_PARENT_ID, 100);
		ja.set(index++, jo);

		jo = Json.createObject();
		jo.put(MenuItemKeys.VIEW_CLASS, "com.dpapp.vaadin.views.system.PricingConfigView");
		jo.put(MenuItemKeys.VIEW_CLASS_ID, 102);
		jo.put(MenuItemKeys.VIEW_SECURE, false);
		jo.put(MenuItemKeys.VIEW_CLASS_PARENT_ID, 302);
		ja.set(index++, jo);

		jo = Json.createObject();
		jo.put(MenuItemKeys.VIEW_CLASS, "com.dpapp.vaadin.views.system.DefaultPricingView");
		jo.put(MenuItemKeys.VIEW_CLASS_ID, 103);
		jo.put(MenuItemKeys.VIEW_SECURE, false);
		jo.put(MenuItemKeys.VIEW_CLASS_PARENT_ID, 102);
		ja.set(index++, jo);

		jo = Json.createObject();
		jo.put(MenuItemKeys.VIEW_CLASS, "com.dpapp.vaadin.views.system.PricingGuardrailsView");
		jo.put(MenuItemKeys.VIEW_CLASS_ID, 107);
		jo.put(MenuItemKeys.VIEW_SECURE, false);
		jo.put(MenuItemKeys.VIEW_CLASS_PARENT_ID, 102);
		ja.set(index++, jo);

		jo = Json.createObject();
		jo.put(MenuItemKeys.VIEW_CLASS, "com.dpapp.vaadin.views.system.CurrencySettingsView");
		jo.put(MenuItemKeys.VIEW_CLASS_ID, 108);
		jo.put(MenuItemKeys.VIEW_SECURE, false);
		jo.put(MenuItemKeys.VIEW_CLASS_PARENT_ID, 102);
		ja.set(index++, jo);

		jo = Json.createObject();
		jo.put(MenuItemKeys.VIEW_CLASS, "com.dpapp.vaadin.views.system.PriceChangeThresholdsView");
		jo.put(MenuItemKeys.VIEW_CLASS_ID, 109);
		jo.put(MenuItemKeys.VIEW_SECURE, false);
		jo.put(MenuItemKeys.VIEW_CLASS_PARENT_ID, 102);
		ja.set(index++, jo);


		jo = Json.createObject();
		jo.put(MenuItemKeys.VIEW_CLASS, "com.dpapp.vaadin.views.system.BusinessGoalsView");
		jo.put(MenuItemKeys.VIEW_CLASS_ID, 400);
		jo.put(MenuItemKeys.VIEW_SECURE, false);
		jo.put(MenuItemKeys.VIEW_CLASS_PARENT_ID, 302);
		ja.set(index++, jo);

		jo = Json.createObject();
		jo.put(MenuItemKeys.VIEW_CLASS, "com.dpapp.vaadin.views.system.SetGoalsView");
		jo.put(MenuItemKeys.VIEW_CLASS_ID, 401);
		jo.put(MenuItemKeys.VIEW_SECURE, false);
		jo.put(MenuItemKeys.VIEW_CLASS_PARENT_ID, 400);
		ja.set(index++, jo);

		jo = Json.createObject();
		jo.put(MenuItemKeys.VIEW_CLASS, "com.dpapp.vaadin.views.system.SystemAlertsView");
		jo.put(MenuItemKeys.VIEW_CLASS_ID, 402);
		jo.put(MenuItemKeys.VIEW_SECURE, false);
		jo.put(MenuItemKeys.VIEW_CLASS_PARENT_ID, 400);
		ja.set(index++, jo);


		jo = Json.createObject();
		jo.put(MenuItemKeys.VIEW_CLASS, "com.dpapp.vaadin.views.UserView");
		jo.put(MenuItemKeys.VIEW_CLASS_ID, 303);
		jo.put(MenuItemKeys.VIEW_CLASS_PARENT_ID, 301);
		jo.put(MenuItemKeys.VIEW_SECURE, false);
		ja.set(index++, jo);


		jo = Json.createObject();
		jo.put(MenuItemKeys.VIEW_CLASS, "com.dpapp.vaadin.views.MarketsView");
		jo.put(MenuItemKeys.VIEW_CLASS_ID, 203);
		jo.put(MenuItemKeys.VIEW_SECURE, false);
		jo.put(MenuItemKeys.VIEW_CLASS_PARENT_ID, 201);
		ja.set(index++, jo);

		jo = Json.createObject();
		jo.put(MenuItemKeys.VIEW_CLASS, "com.dpapp.vaadin.views.SkusView");
		jo.put(MenuItemKeys.VIEW_CLASS_ID, 204);
		jo.put(MenuItemKeys.VIEW_SECURE, false);
		jo.put(MenuItemKeys.VIEW_CLASS_PARENT_ID, 201);
		ja.set(index++, jo);

		jo = Json.createObject();
		jo.put(MenuItemKeys.VIEW_CLASS, "com.dpapp.vaadin.views.ChannelsView");
		jo.put(MenuItemKeys.VIEW_CLASS_ID, 205);
		jo.put(MenuItemKeys.VIEW_SECURE, false);
		jo.put(MenuItemKeys.VIEW_CLASS_PARENT_ID, 201);
		ja.set(index++, jo);


		return ja;
	}

	@Override
	protected boolean getAllowsMultipleTabs() {
		return true;
	}
}
