package com.dpapp.vaadin.views;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.dpapp.data.beans.MarketDataBean;
import com.dpapp.vaadin.components.MarketDataGrid;
import com.dpapp.ws.beans.OnlineResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tooooolazy.data.ServiceLocator;
import com.tooooolazy.data.interfaces.OnlineKeys;
import com.tooooolazy.services.client.RestClient;
import com.tooooolazy.vaadin.ui.BaseUI;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

public class MarketsView extends DpAppBaseView {

	protected MarketDataGrid mdg;

	@Override
	protected Class getCriteriaClass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected OnlineResult[] getWSContents() {
		// TODO Auto-generated method stub
		RestClient rc = (RestClient)ServiceLocator.get().lookupSrv(RestClient.class);
		String data = rc.getData("http://40.114.226.148:8090/deepair/metrics/getMarketWeekProductInfo?marketCategory=ROUTE&departureWeek=16&departureYear=2019");
		data = "{'DATA':" + data + "}"; 
		OnlineResult[] ors = new OnlineResult[1];
		ors[0] = new OnlineResult();
		ors[0].setResultObject( data );

		return ors;
	}

	@Override
	protected Component createContent(BaseUI ui) {
		mdg = new MarketDataGrid();
		mdg.setSizeFull();

		HorizontalLayout hl = new HorizontalLayout();
		hl.setSizeFull();
		hl.setMargin( false );

		VerticalLayout rightVl = new VerticalLayout(new Panel("WatchList and Alerts"), new Panel("Top Channel"));
		rightVl.setMargin(false);
		HorizontalSplitPanel hsp = new HorizontalSplitPanel(mdg, rightVl );
		hsp.setSplitPosition(80); // percent

		VerticalLayout leftVl = new VerticalLayout(new Panel("Top Markets"), new Panel("Top Stock Categories"));
		leftVl.setMargin(false);
		leftVl.setSizeFull();
		leftVl.setWidth("200px");

		hl.addComponents(leftVl, hsp);
		hl.setExpandRatio(hsp, 1f);

		JSONObject wsData = get_DataElement(0);
		JSONArray ja = wsData.optJSONArray(OnlineKeys.DATA);
		String data = ja.toString();
//		DpAppUI.get().addIfHasAccess("enter", MarketBean.class, hl, vl);

		List<MarketDataBean> marketsData = new ArrayList<>();
		ObjectMapper mapper = new ObjectMapper();
		try {
			MarketDataBean[] mdbs = mapper.readValue(data, MarketDataBean[].class);
			marketsData.addAll( Arrays.asList( mdbs ));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mdg.setItems( marketsData );

		return hl;
	}

//	@Override
//	protected Component generateContent() throws Exception {
//		// TODO Auto-generated method stub
////		MarketsGrid mg = new MarketsGrid();
////		mg.setSizeFull();
//		RestClient rc = (RestClient)ServiceLocator.get().lookupSrv(RestClient.class);
//		String data = rc.getData("http://40.114.226.148:8090/deepair/metrics/getMarketWeekProductInfo?marketCategory=ROUTE&departureWeek=16&departureYear=2019");
//		
//		MarketDataGrid mdg = new MarketDataGrid();
//		mdg.setSizeFull();
//
//		HorizontalLayout hl = new HorizontalLayout();
//		hl.setSizeFull();
//		hl.setMargin( false );
//
//		VerticalLayout rightVl = new VerticalLayout(new Panel("WatchList and Alerts"), new Panel("Top Channel"));
//		rightVl.setMargin(false);
//		HorizontalSplitPanel hsp = new HorizontalSplitPanel(mdg, rightVl );
//		hsp.setSplitPosition(80); // percent
//
//		VerticalLayout leftVl = new VerticalLayout(new Panel("Top Markets"), new Panel("Top Stock Categories"));
//		leftVl.setMargin(false);
//		leftVl.setSizeFull();
//		leftVl.setWidth("200px");
//
//		hl.addComponents(leftVl, hsp);
//		hl.setExpandRatio(hsp, 1f);
//
////		DpAppUI.get().addIfHasAccess("enter", MarketBean.class, hl, vl);
//
//		List<MarketDataBean> marketsData = new ArrayList<>();
//		String str = "[{\"MarketCategory\":\"ROUTE\",\"Market\":\"NAP-LPA\",\"DepartureTimeBucket\":\"MORNING\",\"ProductCategory\":\"ANCILLARIES\",\"Product\":\"BAGS\",\"DepartureWeek\":16,\"DepartureYear\":2019,\"Active\":true,\"MarketMetrics\":{\"CurrentShopNumber\":2,\"IntakeShopNumberOneWeek\":1,\"CurrentRevenue\":0.0,\"IntakeRevenueOneWeek\":0.0,\"CurrentOrderNumber\":0,\"IntakeOrderNumberOneWeek\":0},\"MarketMetricsDeepair\":{\"CurrentShopNumber\":0,\"IntakeShopNumberOneWeek\":0,\"CurrentRevenue\":0.0,\"IntakeRevenueOneWeek\":0.0,\"CurrentOrderNumber\":0,\"IntakeOrderNumberOneWeek\":0}},{\"MarketCategory\":\"ROUTE\",\"Market\":\"PMI-LPA\",\"DepartureTimeBucket\":\"MORNING\",\"ProductCategory\":\"ANCILLARIES\",\"Product\":\"BAGS\",\"DepartureWeek\":16,\"DepartureYear\":2019,\"Active\":true,\"MarketMetrics\":{\"CurrentShopNumber\":5,\"IntakeShopNumberOneWeek\":1,\"CurrentRevenue\":0.0,\"IntakeRevenueOneWeek\":0.0,\"CurrentOrderNumber\":0,\"IntakeOrderNumberOneWeek\":0},\"MarketMetricsDeepair\":{\"CurrentShopNumber\":0,\"IntakeShopNumberOneWeek\":0,\"CurrentRevenue\":0.0,\"IntakeRevenueOneWeek\":0.0,\"CurrentOrderNumber\":0,\"IntakeOrderNumberOneWeek\":0}}]";
//		ObjectMapper mapper = new ObjectMapper();
//		try {
//			MarketDataBean[] mdbs = mapper.readValue(data, MarketDataBean[].class);
//			marketsData.addAll( Arrays.asList( mdbs ));
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		mdg.setItems( marketsData );
////		List<MarketBean> markets = new ArrayList<>();
//
////		MarketBean mb = new MarketBean();
////		mb.setMarket("MKT-001");
////		mb.setConversion( new MarketSectionBean(0.30, 0.21, 0.11));
////		mb.setScale( new MarketSectionBean(200, 3000, 2000));
////		mb.setRevenue( new MarketSectionBean(2000, 30000, 25000));
////		mb.setRevenueToPassenger( new MarketSectionBean(10, 10, 14));
////		markets.add( mb );
////
////		mb = new MarketBean();
////		mb.setMarket("MKT-002");
////		mb.setConversion( new MarketSectionBean(0.34, 0.10, 0.21));
////		mb.setScale( new MarketSectionBean(200, 3000, 2000));
////		mb.setRevenue( new MarketSectionBean(1000, 23000, 45000));
////		mb.setRevenueToPassenger( new MarketSectionBean(21, 12, 13));
////		markets.add( mb );
////		
////		mg.setItems( markets );
//		return hl;
//	}

	@Override
	protected void addSearchCriteria(AbstractComponent ac) {
		// TODO Auto-generated method stub
		
	}
}
