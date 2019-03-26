package com.dpapp.vaadin.views;

import java.util.ArrayList;
import java.util.List;

import com.dpapp.data.beans.MarketBean;
import com.dpapp.data.beans.MarketSectionBean;
import com.dpapp.vaadin.components.MarketsGrid;
import com.dpapp.vaadin.ui.DpAppUI;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tooooolazy.vaadin.views.BaseView;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

public class MarketsView extends BaseView {

	@Override
	protected void addJavascriptFunctions() {
		// TODO Auto-generated method stub

	}

	@Override
	protected Class getCriteriaClass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addContent() {
		// TODO Auto-generated method stub
		MarketsGrid mg = new MarketsGrid();
		mg.setSizeFull();

		HorizontalLayout hl = new HorizontalLayout();
		hl.setSizeFull();
		hl.setMargin( false );

		VerticalLayout rightVl = new VerticalLayout(new Panel("WatchList and Alerts"), new Panel("Top Channel"));
		rightVl.setMargin(false);
		HorizontalSplitPanel hsp = new HorizontalSplitPanel(mg, rightVl );
		hsp.setSplitPosition(80); // percent

		VerticalLayout leftVl = new VerticalLayout(new Panel("Top Markets"), new Panel("Top Stock Categories"));
		leftVl.setMargin(false);
		leftVl.setSizeFull();
		leftVl.setWidth("200px");

		hl.addComponents(leftVl, hsp);
		hl.setExpandRatio(hsp, 1f);

		DpAppUI.get().addIfHasAccess("enter", MarketBean.class, hl, vl);
//		vl.addComponent( hl );

		ObjectMapper mapper = new ObjectMapper();
//		mapper.readValue(content, MarketBean.class);
		List<MarketBean> markets = new ArrayList<>();

		MarketBean mb = new MarketBean();
		mb.setMarket("MKT-001");
		mb.setConversion( new MarketSectionBean(0.30, 0.21, 0.11));
		mb.setScale( new MarketSectionBean(200, 3000, 2000));
		mb.setRevenue( new MarketSectionBean(2000, 30000, 25000));
		mb.setRevenueToPassenger( new MarketSectionBean(10, 10, 14));
		markets.add( mb );

		mb = new MarketBean();
		mb.setMarket("MKT-002");
		mb.setConversion( new MarketSectionBean(0.34, 0.10, 0.21));
		mb.setScale( new MarketSectionBean(200, 3000, 2000));
		mb.setRevenue( new MarketSectionBean(1000, 23000, 45000));
		mb.setRevenueToPassenger( new MarketSectionBean(21, 12, 13));
		markets.add( mb );
		
		mg.setItems( markets );
	}
}
