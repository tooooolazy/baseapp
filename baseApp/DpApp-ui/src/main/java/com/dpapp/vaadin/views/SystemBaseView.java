package com.dpapp.vaadin.views;

import com.dpapp.vaadin.views.system.AiConfigView;
import com.dpapp.vaadin.views.system.PricingConfigView;
import com.dpapp.vaadin.views.trends.PerformanceView;
import com.tooooolazy.vaadin.ui.AppLayout;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;

public class SystemBaseView extends SettingsBaseView {
	@Override
	public void enter(ViewChangeEvent event) {
		super.enter(event);

		AppLayout al = (AppLayout)getUI().getContent();
		// show what is needed

		al.toggleMenuItem( PerformanceView.class, false );
		al.toggleMenuItem( AiConfigView.class, true );
		al.toggleMenuItem( PricingConfigView.class, true );
	};

}
