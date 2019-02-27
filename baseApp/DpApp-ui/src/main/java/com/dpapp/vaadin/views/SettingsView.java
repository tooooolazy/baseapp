package com.dpapp.vaadin.views;

import com.tooooolazy.vaadin.ui.AppLayout;
import com.tooooolazy.vaadin.views.BaseView;
import com.tooooolazy.vaadin.views.Dummy1View;
import com.tooooolazy.vaadin.views.Dummy2View;
import com.tooooolazy.vaadin.views.Dummy4View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;

public class SettingsView extends BaseView {

	public void enter(ViewChangeEvent event) {
		// TODO need to check (and most likely) modify menuitems

		// get the abstact App Layout
		AppLayout al = (AppLayout)getUI().getContent();
		// hide what is not needed
		al.toggleMenuItem( TrendsView.class, false );
		al.toggleMenuItem( MarketsView.class, false );
		al.toggleMenuItem( SkusView.class, false );
		al.toggleMenuItem( ChannelsView.class, false );

		al.toggleMenuItem( Dummy4View.class, true );
		al.toggleMenuItem( Dummy2View.class, true );
		al.toggleMenuItem( Dummy1View.class, true );

		al.setSubItemsPadding();

		al.setActiveView( getClass().getSimpleName() );
	};

	@Override
	protected boolean showTitleInContent() {
		// TODO Auto-generated method stub
		return false;
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
