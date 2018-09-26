package com.tooooolazy.vaadin.ui;

import javax.servlet.annotation.WebServlet;

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
		return new ThemeResource( "img/logo/mainLogo_59x57.png" );
	}
	@Override
	protected Resource getSecLogoResource() {
		return new ThemeResource( "img/logo/aitol_logo_name_250x50.png" );
	}
}
