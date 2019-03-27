package com.tooooolazy.vaadin.ui;

import java.util.Properties;

import javax.servlet.annotation.WebServlet;

import com.tooooolazy.data.services.DataHandler;
import com.tooooolazy.data.services.SecurityController;
import com.tooooolazy.data.services.UserController;
import com.tooooolazy.data.services.beans.JobFailureCode;
import com.tooooolazy.data.services.beans.OnlineBaseParams;
import com.tooooolazy.data.services.beans.OnlineBaseResult;
import com.tooooolazy.data.services.beans.RoleEnum;
import com.tooooolazy.data.services.beans.UserBean;
import com.tooooolazy.util.Messages;
import com.tooooolazy.util.TLZUtils;
import com.tooooolazy.vaadin.layout.ResponsiveMenuLayout;
import com.tooooolazy.vaadin.resources.Resources;
import com.tooooolazy.vaadin.views.MainView;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.Resource;
import com.vaadin.server.VaadinRequest;

@Theme("baseTheme")
public class AppUI extends BaseUI<ResponsiveMenuLayout, UserBean<RoleEnum>, OnlineBaseResult, JobFailureCode> {

	static {
//		Messages.setSupportedLocales( new String[] {"en", "el", "bg"});
		try {
			Messages.addBundle(AppUI.class.getPackage().getName() + ".app");
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
	protected DataHandler<OnlineBaseResult, OnlineBaseParams> createDataHandler() {
		return new DataHandler(OnlineBaseResult.class) {

			@Override
			protected OnlineBaseParams createOnlineParams() {
				return new OnlineBaseParams();
			}

		};
	}

	@Override
	protected SecurityController<UserBean<RoleEnum>, RoleEnum> createSecurityController() {
		return new SecurityController<UserBean<RoleEnum>, RoleEnum>() {

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
	protected UserController createUserController() {
		return new UserController<UserBean<RoleEnum>, RoleEnum>() {
			
		};
	}

	@Override
	protected ResponsiveMenuLayout createRootLayout() {
		return new ResponsiveMenuLayout();
	}

	protected Class getMainViewClass() {
		return MainView.class;
	}

	@WebServlet(urlPatterns = "/*", name = "BaseUIServlet", asyncSupported = true)
	@VaadinServletConfiguration(ui = AppUI.class, productionMode = false)
	public static class BaseUIServlet extends BaseAppServlet {
	}


	@Override
	protected Resource getLogoResource() {
		return Resources.getPng( "img/logo/", "mainLogo_59x57" );
	}
	@Override
	protected Resource getSecLogoResource() {
		return Resources.getPng( "img/logo/", "aitol_logo_name_250x50" );
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

	@Override
	protected Properties getEmailConfigProperties() {
		return TLZUtils.loadProperties( "config.properties" );
	}

	@Override
	protected boolean getAllowsMultipleTabs() {
		return true;
	}

	@Override
	public UserBean<RoleEnum> getDummyUser() {
		return new UserBean<RoleEnum>(){

			@Override
			protected boolean isGodRole(RoleEnum re) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			protected boolean isAdminRole(RoleEnum re) {
				// TODO Auto-generated method stub
				return false;
			}};
	}
}
