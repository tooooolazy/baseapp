package com.dpapp.vaadin.views;

import com.dpapp.vaadin.views.trends.PerformanceView;
import com.tooooolazy.vaadin.ui.AppLayout;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;

public class TrendsBaseView extends OverviewBaseView {

	@Override
	public void enter(ViewChangeEvent event) {
		super.enter(event);

		AppLayout al = (AppLayout)getUI().getContent();
		// show what is needed

		al.toggleMenuItem( PerformanceView.class, true );

		al.setSubItemsPadding();

		al.setActiveView( getClass().getSimpleName() );
	};

}
