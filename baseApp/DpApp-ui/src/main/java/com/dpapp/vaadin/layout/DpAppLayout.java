package com.dpapp.vaadin.layout;

import com.tooooolazy.vaadin.layout.TopAndLeftMenuLayout;
import com.vaadin.navigator.Navigator;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;

public class DpAppLayout extends TopAndLeftMenuLayout {

	protected Component createTopMenuItems(Navigator navigator) {
		HorizontalLayout hl = new HorizontalLayout();
		hl.setResponsive( true );

		return hl;
	}
	protected Component createSubMenuItems(Navigator navigator) {
		HorizontalLayout hl = new HorizontalLayout();
		hl.setResponsive( true );

		return hl;
	}

}
