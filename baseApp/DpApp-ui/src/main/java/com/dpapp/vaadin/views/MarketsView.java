package com.dpapp.vaadin.views;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.dpapp.data.beans.MarketDataBean;
import com.dpapp.data.beans.searchcriteria.MarketDataCriteria;
import com.dpapp.vaadin.components.MarketDataGrid;
import com.dpapp.vaadin.components.searchcriteria.MarketDataCriteriaComponent;
import com.dpapp.ws.beans.OnlineResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tooooolazy.data.ServiceLocator;
import com.tooooolazy.data.interfaces.OnlineKeys;
import com.tooooolazy.services.client.RestClient;
import com.tooooolazy.vaadin.components.listeners.InputComponentButtonListener;
import com.tooooolazy.vaadin.ui.BaseUI;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.AbstractComponentContainer;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

public class MarketsView extends DpAppBaseView<MarketDataCriteria, MarketDataBean> {

	protected MarketDataGrid mdg;
	protected MarketDataCriteriaComponent mdcc;
	protected VerticalLayout mvl;

	@Override
	protected Class getCriteriaClass() {
		return MarketDataCriteria.class;
	}
	@Override
	protected AbstractComponent createSearchCriteria() {
		
		mdcc = new MarketDataCriteriaComponent(new InputComponentButtonListener() {

			@Override
			protected Object submit() throws Exception {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			protected void onSuccess(Object res) {
				BaseUI.get().getNavigator().navigateTo( getThisView() );
			}
			
		}, this);
		return mdcc;
	}

	@Override
	protected void addSearchCriteria(AbstractComponent ac) {
		mvl.addComponent( ac );
	}

	@Override
	protected void createMainContentLayout() {
		mdg = new MarketDataGrid();
		mdg.setSizeFull();

		HorizontalLayout hl = new HorizontalLayout();
		hl.setSizeFull();
		hl.setMargin( false );
		vl.addComponent(hl);

		mvl = new VerticalLayout(); // this is where criteria should be added
		
		VerticalLayout rightVl = new VerticalLayout(new Panel("WatchList and Alerts"), new Panel("Top Channel"));
		rightVl.setMargin(false);
		HorizontalSplitPanel hsp = new HorizontalSplitPanel( mvl, rightVl );
		hsp.setSplitPosition(80); // percent

		VerticalLayout leftVl = new VerticalLayout(new Panel("Top Markets"), new Panel("Top Stock Categories"));
		leftVl.setMargin(false);
		leftVl.setSizeFull();
		leftVl.setWidth("200px");

		hl.addComponents(leftVl, hsp);
		hl.setExpandRatio(hsp, 1f);
	}
	@Override
	protected AbstractComponentContainer getGeneratedContentContainer() {
		return mvl;
	}

	@Override
	protected OnlineResult[] getWSContents() {
		RestClient rc = (RestClient)ServiceLocator.get().lookupSrv(RestClient.class);
//		String data = rc.getData("http://40.114.226.148:8090/deepair/metrics/getMarketWeekProductInfo?marketCategory=ROUTE&departureWeek=16&departureYear=2019");
		String data = rc.getData("http://40.114.226.148:8090/deepair/metrics/getMarketWeekProductInfo?marketCategory=ROUTE&" + getCriteria().createUrlParams() );
		data = "{'DATA':" + data + "}"; 
		OnlineResult[] ors = new OnlineResult[1];
		ors[0] = new OnlineResult();
		ors[0].setResultObject( data );

		return ors;
	}

	@Override
	protected Component createContent(BaseUI ui) {

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

		return mdg;
	}
}
