package com.dpapp.vaadin.views;

import com.dpapp.vaadin.views.trends.PerformanceView;
import com.tooooolazy.vaadin.ui.AppLayout;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;

public class MarketsView extends OverviewBaseView {

	@Override
	public void enter(ViewChangeEvent event) {
		super.enter(event);

		AppLayout al = (AppLayout)getUI().getContent();
		// show what is needed

		al.toggleMenuItem( PerformanceView.class, false );

//		al.setSubItemsPadding();

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
