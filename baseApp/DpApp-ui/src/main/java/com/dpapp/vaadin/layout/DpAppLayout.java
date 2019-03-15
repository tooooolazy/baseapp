package com.dpapp.vaadin.layout;

import com.dpapp.vaadin.views.ChannelsView;
import com.dpapp.vaadin.views.MarketsView;
import com.dpapp.vaadin.views.OverviewView;
import com.dpapp.vaadin.views.SettingsView;
import com.dpapp.vaadin.views.SkusView;
import com.dpapp.vaadin.views.SystemView;
import com.dpapp.vaadin.views.TrendsView;
import com.dpapp.vaadin.views.UserView;
import com.tooooolazy.vaadin.layout.TopAndLeftMenuLayout;
import com.vaadin.navigator.Navigator;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;

public class DpAppLayout extends TopAndLeftMenuLayout {

	protected Component createTopMenuItems(Navigator navigator) {
		HorizontalLayout hl = new HorizontalLayout();
		hl.setResponsive( true );

//		Button b = createTopMenuButton( navigator, 0, OverviewView.class);
//		hl.addComponent( b );
//
//		Button b2 = createTopMenuButton( navigator, 0, SettingsView.class);
//		hl.addComponent( b2 );

		return hl;
	}
	protected Component createSubMenuItems(Navigator navigator) {
		HorizontalLayout hl = new HorizontalLayout();
		hl.setResponsive( true );

//		Button b = createSubMenuButton( navigator, 0, TrendsView.class);
//		hl.addComponent( b );
//
//		Button b2 = createSubMenuButton( navigator, 0, MarketsView.class);
//		hl.addComponent( b2 );
//
//		Button b3 = createSubMenuButton( navigator, 0, SkusView.class);
//		hl.addComponent( b3 );
//
//		Button b4 = createSubMenuButton( navigator, 0, ChannelsView.class);
//		hl.addComponent( b4 );
//
//		Button b5 = createSubMenuButton( navigator, 0, SystemView.class);
//		hl.addComponent( b5 );
//
//		Button b6 = createSubMenuButton( navigator, 0, UserView.class);
//		hl.addComponent( b6 );

		return hl;
	}

}
