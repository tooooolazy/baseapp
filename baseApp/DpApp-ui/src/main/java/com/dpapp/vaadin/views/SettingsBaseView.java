package com.dpapp.vaadin.views;

import com.dpapp.vaadin.views.system.AiConfigView;
import com.dpapp.vaadin.views.system.PricingConfigView;
import com.dpapp.vaadin.views.trends.PerformanceView;
import com.tooooolazy.vaadin.ui.AppLayout;
import com.tooooolazy.vaadin.views.BaseView;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;

public class SettingsBaseView extends BaseView {

	public void enter(ViewChangeEvent event) {
		// TODO need to check (and most likely) modify menuitems

		// get the abstact App Layout
		AppLayout al = (AppLayout)getUI().getContent();
		// hide what is not needed
		al.toggleMenuItem( TrendsView.class, false );
		al.toggleMenuItem( MarketsView.class, false );
		al.toggleMenuItem( SkusView.class, false );
		al.toggleMenuItem( ChannelsView.class, false );

		al.toggleMenuItem( SystemView.class, true );
		al.toggleMenuItem( UserView.class, true );

		al.toggleMenuItem( PerformanceView.class, false );

		al.toggleMenuItem( AiConfigView.class, false );
		al.toggleMenuItem( PricingConfigView.class, false );

		al.setSubItemsPadding();

		al.setActiveView( getClass().getSimpleName() );
	};

	@Override
	protected boolean showTitleInContent() {
		// TODO Auto-generated method stub
		return true;
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
