package com.dpapp.vaadin.views;

import com.dpapp.vaadin.views.trends.PerformanceView;
import com.tooooolazy.vaadin.ui.AppLayout;
import com.tooooolazy.vaadin.views.BaseView;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;

public class OverviewView extends BaseView {

	public void enter(ViewChangeEvent event) {
		// TODO need to check (and most likely) modify menuitems

		// get the abstact App Layout
		AppLayout al = (AppLayout)getUI().getContent();
		// show what is needed
		al.toggleMenuItem( TrendsView.class, true );
		al.toggleMenuItem( MarketsView.class, true );
		al.toggleMenuItem( SkusView.class, true );
		al.toggleMenuItem( ChannelsView.class, true );

		al.toggleMenuItem( PerformanceView.class, true );

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
