package com.dpapp.vaadin.components;

import java.text.NumberFormat;

import com.dpapp.data.beans.MarketDataBean;
import com.tooooolazy.util.Messages;
import com.tooooolazy.vaadin.components.BaseGrid;
import com.tooooolazy.vaadin.ui.BaseUI;
import com.vaadin.ui.renderers.NumberRenderer;

public class MarketDataGrid extends BaseGrid<MarketDataBean> {

	public MarketDataGrid() {
		super();
		setId("MarketsGrid");

		init();
	}
	protected void init() {
		setBeanType(MarketDataBean.class);
	}

	public void attach() {
		super.attach();

		addColumns();
//		adjustMainHeader();
//		setGroupHeaders();
	}

	private void addColumns() {
		addColumn( "market" ).setCaption( Messages.getString( getClass(), "market" ) );
		setFrozenColumnCount( 1 );

		addColumn( "marketCategory" ).setCaption( Messages.getString( getClass(), "marketCategory" ) );
		addColumn( "productCategory" ).setCaption( Messages.getString( getClass(), "productCategory" ) );
		addColumn( "product" ).setCaption( Messages.getString( getClass(), "product" ) );
		addColumn( "departureWeek", new NumberRenderer( NumberFormat.getNumberInstance( BaseUI.get().getLocale() ) ) ).setCaption( Messages.getString( getClass(), "departureWeek" ) );
		addColumn( "departureYear", new NumberRenderer( NumberFormat.getNumberInstance( BaseUI.get().getLocale() ) ) ).setCaption( Messages.getString( getClass(), "departureYear" ) );
		addColumn( "departureTimeBucket" ).setCaption( Messages.getString( getClass(), "departureTimeBucket" ) );

		addColumn( "marketMetrics.currentShopNumber", new NumberRenderer( NumberFormat.getNumberInstance( BaseUI.get().getLocale() ) ) ).setCaption( Messages.getString( getClass(), "currentShopNumber" ) );
		addColumn( "marketMetrics.intakeShopNumberOneWeek", new NumberRenderer( NumberFormat.getNumberInstance( BaseUI.get().getLocale() ) ) ).setCaption( Messages.getString( getClass(), "intakeShopNumberOneWeek" ) );
		addColumn( "marketMetrics.currentRevenue", new NumberRenderer( NumberFormat.getNumberInstance( BaseUI.get().getLocale() ) ) ).setCaption( Messages.getString( getClass(), "currentRevenue" ) );
		addColumn( "marketMetrics.intakeRevenueOneWeek", new NumberRenderer( NumberFormat.getNumberInstance( BaseUI.get().getLocale() ) ) ).setCaption( Messages.getString( getClass(), "intakeRevenueOneWeek" ) );
		addColumn( "marketMetrics.currentOrderNumber", new NumberRenderer( NumberFormat.getNumberInstance( BaseUI.get().getLocale() ) ) ).setCaption( Messages.getString( getClass(), "currentOrderNumber" ) );
		addColumn( "marketMetrics.intakeOrderNumberOneWeek", new NumberRenderer( NumberFormat.getNumberInstance( BaseUI.get().getLocale() ) ) ).setCaption( Messages.getString( getClass(), "intakeOrderNumberOneWeek" ) );
	}
//	private void adjustMainHeader() {
//		HeaderRow mainHeader = getDefaultHeaderRow();
//		makeTwoLineHeader(mainHeader, "conversion.thisWeek", "thisWeek");
//		makeTwoLineHeader(mainHeader, "conversion.thisMonth", "thisMonth");
//		makeTwoLineHeader(mainHeader, "conversion.nextMonth", "nextMonth");
//
//		makeTwoLineHeader(mainHeader, "scale.thisWeek", "thisWeek");
//		makeTwoLineHeader(mainHeader, "scale.thisMonth", "thisMonth");
//		makeTwoLineHeader(mainHeader, "scale.nextMonth", "nextMonth");
//
//		makeTwoLineHeader(mainHeader, "revenue.thisWeek", "thisWeek");
//		makeTwoLineHeader(mainHeader, "revenue.thisMonth", "thisMonth");
//		makeTwoLineHeader(mainHeader, "revenue.nextMonth", "nextMonth");
//
//		makeTwoLineHeader(mainHeader, "revenueToPassenger.thisWeek", "thisWeek");
//		makeTwoLineHeader(mainHeader, "revenueToPassenger.thisMonth", "thisMonth");
//		makeTwoLineHeader(mainHeader, "revenueToPassenger.nextMonth", "nextMonth");
//	}
}
