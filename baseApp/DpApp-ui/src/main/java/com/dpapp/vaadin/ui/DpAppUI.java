package com.dpapp.vaadin.ui;

import java.util.Properties;

import javax.servlet.annotation.WebServlet;

import org.json.JSONArray;
import org.json.JSONObject;

import com.dpapp.vaadin.layout.DpAppLayout;
import com.dpapp.ws.beans.DpAppUserBean;
import com.dpapp.ws.beans.JobFailureCode;
import com.dpapp.ws.beans.OnlineParams;
import com.dpapp.ws.beans.OnlineResult;
import com.dpapp.ws.beans.RoleEnum;
import com.tooooolazy.data.services.DataHandler;
import com.tooooolazy.data.services.SecurityController;
import com.tooooolazy.data.services.UserController;
import com.tooooolazy.util.Messages;
import com.tooooolazy.vaadin.resources.Resources;
import com.tooooolazy.vaadin.ui.BaseAppServlet;
import com.tooooolazy.vaadin.ui.BaseUI;
import com.tooooolazy.vaadin.views.MainView;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.Resource;
import com.vaadin.server.VaadinRequest;

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
		return new DataHandler<OnlineResult, OnlineParams>(OnlineResult.class) {

			@Override
			protected OnlineParams createOnlineParams() {
				return new OnlineParams();
			}
			@Override
			public JSONObject getMethodSecurityDefs() {
				JSONObject jo = new JSONObject();
				JSONArray ja = new JSONArray();
				jo.put("DATA", ja);
				// TODO
//				sb.append("select mld.rolecode, mld.method_name, tc.class_name, mld.allow, mld.DESCRIPTION from " + getSchema() + ".MSECLEVELDEFS mld inner join " + getSchema() + ".TYPECLASS tc on mld.class_id=tc.id");

				ja.put( new JSONArray("[1, 'enter', 'com.dpapp.vaadin.views.OverviewView', 0, '']") );
				return jo;
			}
		};
	}

	@Override
	protected SecurityController<DpAppUserBean, RoleEnum> createSecurityController() {
		return new SecurityController<DpAppUserBean, RoleEnum>() {

			@Override
			public RoleEnum getRoleByValue(int rv) {
				return RoleEnum.byValue( rv );
			}
			@Override
			public RoleEnum getNotLoggedInRole() {
				return RoleEnum.NOT_LOGGED_IN;
			}
		};
	}

	@Override
	protected UserController<DpAppUserBean, RoleEnum> createUserController() {
		return new UserController<DpAppUserBean, RoleEnum>() {
			
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
		return Resources.getPng( "img/logo/", "Deepair_logo_POS" );
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

	/**
	 * overridden to avoid WS call 
	 * @see com.tooooolazy.vaadin.ui.BaseUI#retrieveViewClasses()
	 */
	@Override
	protected JSONArray retrieveViewClasses() {
		JSONArray ja = new JSONArray();

		ja.put( new JSONObject("{'ID':201, 'groupId':1,'VALUE':'com.dpapp.vaadin.views.OverviewView'}") );
		ja.put( new JSONObject("{'ID':202, 'groupId':1,'VALUE':'com.dpapp.vaadin.views.TrendsView'}") );
		ja.put( new JSONObject("{'ID':301, 'groupId':1,'VALUE':'com.dpapp.vaadin.views.SettingsView'}") );
		ja.put( new JSONObject("{'ID':302, 'groupId':1,'VALUE':'com.dpapp.vaadin.views.SystemView'}") );
		ja.put( new JSONObject("{'ID':1, 'groupId':1,'VALUE':'com.dpapp.vaadin.views.trends.PerformanceView'}") );
		ja.put( new JSONObject("{'ID':2, 'groupId':1,'VALUE':'com.dpapp.vaadin.views.trends.SoldRevenueView'}") );
		ja.put( new JSONObject("{'ID':3, 'groupId':1,'VALUE':'com.dpapp.vaadin.views.trends.OverTimeSR_View'}") );
		ja.put( new JSONObject("{'ID':33, 'groupId':1,'VALUE':'com.dpapp.vaadin.views.trends.BySkuSR_View'}") );
		ja.put( new JSONObject("{'ID':4, 'groupId':1,'VALUE':'com.dpapp.vaadin.views.trends.ByChannelSR_View'}") );
		ja.put( new JSONObject("{'ID':5, 'groupId':1,'VALUE':'com.dpapp.vaadin.views.trends.ByTouchPointSR_View'}") );
		ja.put( new JSONObject("{'ID':6, 'groupId':1,'VALUE':'com.dpapp.vaadin.views.trends.ForwardRevenueView'}") );
		ja.put( new JSONObject("{'ID':7, 'groupId':1,'VALUE':'com.dpapp.vaadin.views.trends.OverTimeFR_View'}") );
		ja.put( new JSONObject("{'ID':8, 'groupId':1,'VALUE':'com.dpapp.vaadin.views.trends.BySkuFR_View'}") );
		ja.put( new JSONObject("{'ID':9, 'groupId':1,'VALUE':'com.dpapp.vaadin.views.trends.ByChannelFR_View'}") );
		ja.put( new JSONObject("{'ID':10, 'groupId':1,'VALUE':'com.dpapp.vaadin.views.trends.ByTouchPointFR_View'}") );
		ja.put( new JSONObject("{'ID':11, 'groupId':1,'VALUE':'com.dpapp.vaadin.views.trends.SoldVolumeView'}") );
		ja.put( new JSONObject("{'ID':12, 'groupId':1,'VALUE':'com.dpapp.vaadin.views.trends.OverTimeSV_View'}") );
		ja.put( new JSONObject("{'ID':13, 'groupId':1,'VALUE':'com.dpapp.vaadin.views.trends.BySkuSV_View'}") );
		ja.put( new JSONObject("{'ID':14, 'groupId':1,'VALUE':'com.dpapp.vaadin.views.trends.ByChannelSV_View'}") );
		ja.put( new JSONObject("{'ID':15, 'groupId':1,'VALUE':'com.dpapp.vaadin.views.trends.ByTouchPointSV_View'}") );
		ja.put( new JSONObject("{'ID':16, 'groupId':1,'VALUE':'com.dpapp.vaadin.views.trends.ConversionsView'}") );
		ja.put( new JSONObject("{'ID':100, 'groupId':1,'VALUE':'com.dpapp.vaadin.views.system.AiConfigView'}") );
		ja.put( new JSONObject("{'ID':101, 'groupId':1,'VALUE':'com.dpapp.vaadin.views.system.LearningRoteView'}") );
		ja.put( new JSONObject("{'ID':104, 'groupId':1,'VALUE':'com.dpapp.vaadin.views.system.TrainingActivityView'}") );
		ja.put( new JSONObject("{'ID':105, 'groupId':1,'VALUE':'com.dpapp.vaadin.views.system.TrainingRewardView'}") );
		ja.put( new JSONObject("{'ID':106, 'groupId':1,'VALUE':'com.dpapp.vaadin.views.system.LoggingLevelsVuew'}") );
		ja.put( new JSONObject("{'ID':102, 'groupId':1,'VALUE':'com.dpapp.vaadin.views.system.PricingConfigView'}") );
		ja.put( new JSONObject("{'ID':103, 'groupId':1,'VALUE':'com.dpapp.vaadin.views.system.DefaultPricingView'}") );
		ja.put( new JSONObject("{'ID':107, 'groupId':1,'VALUE':'com.dpapp.vaadin.views.system.PricingGuardrailsView'}") );
		ja.put( new JSONObject("{'ID':108, 'groupId':1,'VALUE':'com.dpapp.vaadin.views.system.CurrencySettingsView'}") );
		ja.put( new JSONObject("{'ID':109, 'groupId':1,'VALUE':'com.dpapp.vaadin.views.system.PriceChangeThresholdsView'}") );
		ja.put( new JSONObject("{'ID':400, 'groupId':1,'VALUE':'com.dpapp.vaadin.views.system.BusinessGoalsView'}") );
		ja.put( new JSONObject("{'ID':401, 'groupId':1,'VALUE':'com.dpapp.vaadin.views.system.SetGoalsView'}") );
		ja.put( new JSONObject("{'ID':402, 'groupId':1,'VALUE':'com.dpapp.vaadin.views.system.SystemAlertsView'}") );
		ja.put( new JSONObject("{'ID':303, 'groupId':1,'VALUE':'com.dpapp.vaadin.views.UserView'}") );
		ja.put( new JSONObject("{'ID':203, 'groupId':1,'VALUE':'com.dpapp.vaadin.views.MarketsView'}") );
		ja.put( new JSONObject("{'ID':204, 'groupId':1,'VALUE':'com.dpapp.vaadin.views.SkusView'}") );
		ja.put( new JSONObject("{'ID':205, 'groupId':1,'VALUE':'com.dpapp.vaadin.views.ChannelsView'}") );
		
		return ja;
	}
	/**
	 * overridden to avoid WS call
	 * @see com.tooooolazy.vaadin.ui.BaseUI#retrieveMenu()
	 */
	protected JSONArray retrieveMenu() {
		JSONArray ja = new JSONArray();

//		_ja = new JSONArray("[pid, 1, cid, subtitle, 1]");
		int so =1;
		ja.put( new JSONArray("[201, 1, 201, 0, " + so++ +"]") );
		ja.put( new JSONArray("[201, 1, 202, 0, " + so++ +"]") );
		ja.put( new JSONArray("[301, 1, 301, 0, " + so++ +"]") );
		ja.put( new JSONArray("[301, 1, 302, 0, " + so++ +"]") );
		ja.put( new JSONArray("[202, 1, 1, 1, " + so++ +"]") );
		ja.put( new JSONArray("[1, 1, 2, 0, " + so++ +"]") );
		ja.put( new JSONArray("[2, 1, 3, 0, " + so++ +"]") );
		ja.put( new JSONArray("[2, 1, 33, 0, " + so++ +"]") );
		ja.put( new JSONArray("[2, 1, 4, 0, " + so++ +"]") );
		ja.put( new JSONArray("[2, 1, 5, 0, " + so++ +"]") );
		ja.put( new JSONArray("[1, 1, 6, 0, " + so++ +"]") );
		ja.put( new JSONArray("[6, 1, 7, 0, " + so++ +"]") );
		ja.put( new JSONArray("[6, 1, 8, 0, " + so++ +"]") );
		ja.put( new JSONArray("[6, 1, 9, 0, " + so++ +"]") );
		ja.put( new JSONArray("[6, 1, 10, 0, " + so++ +"]") );
		ja.put( new JSONArray("[1, 1, 11, 0, " + so++ +"]") );
		ja.put( new JSONArray("[11, 1, 12, 0, " + so++ +"]") );
		ja.put( new JSONArray("[11, 1, 13, 0, " + so++ +"]") );
		ja.put( new JSONArray("[11, 1, 14, 0, " + so++ +"]") );
		ja.put( new JSONArray("[11, 1, 15, 0, " + so++ +"]") );
		ja.put( new JSONArray("[1, 1, 16, 0, " + so++ +"]") );
		ja.put( new JSONArray("[302, 1, 100, 0, " + so++ +"]") );
		ja.put( new JSONArray("[100, 1, 101, 0, " + so++ +"]") );
		ja.put( new JSONArray("[100, 1, 104, 0, " + so++ +"]") );
		ja.put( new JSONArray("[100, 1, 105, 0, " + so++ +"]") );
		ja.put( new JSONArray("[100, 1, 106, 0, " + so++ +"]") );
		ja.put( new JSONArray("[302, 1, 102, 0, " + so++ +"]") );
		ja.put( new JSONArray("[102, 1, 103, 0, " + so++ +"]") );
		ja.put( new JSONArray("[102, 1, 107, 0, " + so++ +"]") );
		ja.put( new JSONArray("[102, 1, 108, 0, " + so++ +"]") );
		ja.put( new JSONArray("[102, 1, 109, 0, " + so++ +"]") );
		ja.put( new JSONArray("[302, 1, 400, 0, " + so++ +"]") );
		ja.put( new JSONArray("[400, 1, 401, 0, " + so++ +"]") );
		ja.put( new JSONArray("[400, 1, 402, 0, " + so++ +"]") );
		ja.put( new JSONArray("[301, 1, 303, 0, " + so++ +"]") );
		ja.put( new JSONArray("[201, 1, 203, 0, " + so++ +"]") );
		ja.put( new JSONArray("[201, 1, 204, 0, " + so++ +"]") );
		ja.put( new JSONArray("[201, 1, 205, 0, " + so++ +"]") );

		return ja;
	}

	@Override
	protected boolean getAllowsMultipleTabs() {
		return true;
	}

	@Override
	public DpAppUserBean getDummyUser() {
		return new DpAppUserBean();
	}
}
