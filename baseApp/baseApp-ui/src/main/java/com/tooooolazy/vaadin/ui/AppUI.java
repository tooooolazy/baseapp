package com.tooooolazy.vaadin.ui;

import java.util.Properties;

import javax.servlet.annotation.WebServlet;

import com.tooooolazy.util.Messages;
import com.tooooolazy.util.TLZUtils;
import com.tooooolazy.vaadin.resources.Resources;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;

/**
 *
 */
@Theme("baseTheme")
public class AppUI extends BaseUI {

//	private CrudService<Person> service = new CrudService<>();
//	private DataProvider<Person, String> dataProvider = new CallbackDataProvider<>(query -> service.findAll().stream(),
//			query -> service.findAll().size());

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


	@WebServlet(urlPatterns = "/*", name = "BaseUIServlet", asyncSupported = true)
	@VaadinServletConfiguration(ui = AppUI.class, productionMode = false)
	public static class BaseUIServlet extends VaadinServlet {
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
}
