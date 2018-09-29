package com.tooooolazy.vaadin.commands;

import java.util.Locale;

import com.tooooolazy.util.Messages;
import com.tooooolazy.vaadin.ui.BaseUI;
import com.vaadin.external.org.slf4j.LoggerFactory;
import com.vaadin.server.Page;
import com.vaadin.ui.UI;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;

/**
 * Simple command that toggles display language of UI. It requires the use of {@link Messages} class to do that
 * @author tooooolazy
 *
 */
public class ToggleLocaleCommand implements Command {

	@Override
    public void menuSelected(MenuItem selectedItem) {
    	String newLang = BaseUI.get().getLocale().getLanguage();
    	Messages.setLang(newLang);
    	newLang = Messages.toggleLocale();

    	LoggerFactory.getLogger(UI.class.getName()).info("Switching locale to: " + newLang);

    	BaseUI.get().setLocale(new Locale( newLang ));
		Page.getCurrent().reload();
    }

}
