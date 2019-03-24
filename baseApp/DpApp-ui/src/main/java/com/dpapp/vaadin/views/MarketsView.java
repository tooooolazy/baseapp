package com.dpapp.vaadin.views;

import java.util.ArrayList;
import java.util.List;

import com.dpapp.data.beans.MarketBean;
import com.dpapp.data.beans.MarketSectionBean;
import com.dpapp.vaadin.components.MarketsGrid;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tooooolazy.vaadin.views.BaseView;
import com.vaadin.ui.AbstractSplitPanel.SplitterClickEvent;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Panel;

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
		HorizontalSplitPanel hsp = new HorizontalSplitPanel(mg, new Panel("test"));
		hsp.setSplitPosition(80); // percent
		vl.addComponent( hsp );
		ObjectMapper mapper = new ObjectMapper();
//		mapper.readValue(content, MarketBean.class);
		List<MarketBean> markets = new ArrayList<>();

		MarketBean mb = new MarketBean();
		mb.setMarket("MKT-001");
		mb.setConversion( new MarketSectionBean(0.30, 0.21, 0.11));
		mb.setScale( new MarketSectionBean(200, 3000, 2000));
		mb.setRevenue( new MarketSectionBean());
		mb.setRevenueToPassenger( new MarketSectionBean());
		markets.add( mb );

		mb = new MarketBean();
		mb.setMarket("MKT-002");
		mb.setConversion( new MarketSectionBean(0.34, 0.10, 0.21));
		mb.setScale( new MarketSectionBean(200, 3000, 2000));
		mb.setRevenue( new MarketSectionBean());
		mb.setRevenueToPassenger( new MarketSectionBean());
		markets.add( mb );
		
		mg.setItems( markets );
	}
}
