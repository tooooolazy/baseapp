package com.tooooolazy.vaadin.views;

import com.tooooolazy.util.Messages;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 * View shown when trying to navigate to a view that does not exist using
 * {@link com.vaadin.navigator.Navigator}.
 * 
 * 
 */
public class ErrorView extends VerticalLayout implements View {

	private Label explanation;

	public ErrorView() {
		setMargin(true);
		setSpacing(true);
	}

	@Override
	public void attach() {
		super.attach();

		Label header = new Label(Messages.getString( "view.not.found"));// "The view could not be found");
		header.addStyleName(ValoTheme.LABEL_H1);
		addComponent(header);
		addComponent(explanation = new Label());
	}

	@Override
	public void enter(ViewChangeListener.ViewChangeEvent event) {

		explanation.setValue(Messages.formatKey( "view.not.found.exp", event.getViewName()));
	}
}
