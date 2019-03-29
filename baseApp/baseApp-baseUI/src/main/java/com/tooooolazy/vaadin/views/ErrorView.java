package com.tooooolazy.vaadin.views;

import com.tooooolazy.data.services.beans.OnlineBaseResult;
import com.tooooolazy.util.Messages;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 * View shown when trying to navigate to a view that does not exist using
 * {@link com.vaadin.navigator.Navigator}.
 * 
 * 
 */
public class ErrorView extends BaseView {

	private Label explanation;

	public ErrorView() {
//		setMargin(true);
//		setSpacing(true);
	}

	@Override
	public void attach() {
		super.attach();

		Label header = new Label(Messages.getString( "view.not.found"));// "The view could not be found");
		header.addStyleName(ValoTheme.LABEL_H1);
		vl.addComponent(header);
		vl.addComponent(explanation = new Label());
	}

	@Override
	public void enter(ViewChangeListener.ViewChangeEvent event) {

		explanation.setValue(Messages.formatKey( "view.not.found.exp", event.getViewName()));
	}

	@Override
	protected void addSearchCriteria(AbstractComponent ac) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected Component createContent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected OnlineBaseResult[] getWSContents() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	protected OnlineBaseResult[] create_OR_array(int i) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addJavascriptFunctions() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected Class getCriteriaClass() {
		// TODO Auto-generated method stub
		return null;
	}
}
