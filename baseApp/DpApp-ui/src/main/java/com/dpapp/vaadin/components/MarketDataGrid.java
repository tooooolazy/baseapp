package com.dpapp.vaadin.components;

import java.text.NumberFormat;

import com.dpapp.data.beans.MarketDataBean;
import com.tooooolazy.util.Messages;
import com.tooooolazy.vaadin.components.BaseGrid;
import com.tooooolazy.vaadin.ui.BaseUI;
import com.vaadin.ui.components.grid.HeaderCell;
import com.vaadin.ui.components.grid.HeaderRow;
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
		setGroupHeaders();
	}

	private void addColumns() {
		addColumn( "market" ).setCaption( Messages.getString( getClass(), "market" ) );

		addColumn( "marketCategory" ).setCaption( Messages.getString( getClass(), "category" ) );
		addColumn( "productCategory" ).setCaption( Messages.getString( getClass(), "category" ) );
		addColumn( "product" ).setCaption( Messages.getString( getClass(), "product" ) );
		addColumn( "departureWeek", new NumberRenderer( NumberFormat.getNumberInstance( BaseUI.get().getLocale() ) ) ).setCaption( Messages.getString( getClass(), "week" ) );
		addColumn( "departureYear", new NumberRenderer( NumberFormat.getNumberInstance( BaseUI.get().getLocale() ) ) ).setCaption( Messages.getString( getClass(), "year" ) );
		addColumn( "departureTimeBucket" ).setCaption( Messages.getString( getClass(), "departureTimeBucket" ) );

		addColumn( "marketMetrics.currentShopNumber", new NumberRenderer( NumberFormat.getNumberInstance( BaseUI.get().getLocale() ) ) ).setCaption( Messages.getString( getClass(), "currentShopNumber" ) );
		addColumn( "marketMetrics.intakeShopNumberOneWeek", new NumberRenderer( NumberFormat.getNumberInstance( BaseUI.get().getLocale() ) ) ).setCaption( Messages.getString( getClass(), "intakeShopNumberOneWeek" ) );
		addColumn( "marketMetrics.currentRevenue", new NumberRenderer( NumberFormat.getNumberInstance( BaseUI.get().getLocale() ) ) ).setCaption( Messages.getString( getClass(), "currentRevenue" ) );
		addColumn( "marketMetrics.intakeRevenueOneWeek", new NumberRenderer( NumberFormat.getNumberInstance( BaseUI.get().getLocale() ) ) ).setCaption( Messages.getString( getClass(), "intakeRevenueOneWeek" ) );
		addColumn( "marketMetrics.currentOrderNumber", new NumberRenderer( NumberFormat.getNumberInstance( BaseUI.get().getLocale() ) ) ).setCaption( Messages.getString( getClass(), "currentOrderNumber" ) );
		addColumn( "marketMetrics.intakeOrderNumberOneWeek", new NumberRenderer( NumberFormat.getNumberInstance( BaseUI.get().getLocale() ) ) ).setCaption( Messages.getString( getClass(), "intakeOrderNumberOneWeek" ) );

		addColumn( "marketMetricsDeepair.currentShopNumber", new NumberRenderer( NumberFormat.getNumberInstance( BaseUI.get().getLocale() ) ) ).setCaption( Messages.getString( getClass(), "currentShopNumber" ) );
		addColumn( "marketMetricsDeepair.intakeShopNumberOneWeek", new NumberRenderer( NumberFormat.getNumberInstance( BaseUI.get().getLocale() ) ) ).setCaption( Messages.getString( getClass(), "intakeShopNumberOneWeek" ) );
		addColumn( "marketMetricsDeepair.currentRevenue", new NumberRenderer( NumberFormat.getNumberInstance( BaseUI.get().getLocale() ) ) ).setCaption( Messages.getString( getClass(), "currentRevenue" ) );
		addColumn( "marketMetricsDeepair.intakeRevenueOneWeek", new NumberRenderer( NumberFormat.getNumberInstance( BaseUI.get().getLocale() ) ) ).setCaption( Messages.getString( getClass(), "intakeRevenueOneWeek" ) );
		addColumn( "marketMetricsDeepair.currentOrderNumber", new NumberRenderer( NumberFormat.getNumberInstance( BaseUI.get().getLocale() ) ) ).setCaption( Messages.getString( getClass(), "currentOrderNumber" ) );
		addColumn( "marketMetricsDeepair.intakeOrderNumberOneWeek", new NumberRenderer( NumberFormat.getNumberInstance( BaseUI.get().getLocale() ) ) ).setCaption( Messages.getString( getClass(), "intakeOrderNumberOneWeek" ) );

		setFrozenColumnCount( 2 );
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
	private void setGroupHeaders() {
		HeaderRow groupingHeader = prependHeaderRow();

		HeaderCell marketInfoCell = groupingHeader.join(
			    groupingHeader.getCell("market"),
			    groupingHeader.getCell("marketCategory"));
		marketInfoCell.setText( Messages.getString( getClass(), "marketInfo" ) );
		marketInfoCell.setStyleName("centered");

		HeaderCell productInfoCell = groupingHeader.join(
			    groupingHeader.getCell("productCategory"),
			    groupingHeader.getCell("product"));
		productInfoCell.setText( Messages.getString( getClass(), "productInfo" ) );
		productInfoCell.setStyleName("centered");

		HeaderCell departureInfoCell = groupingHeader.join(
			    groupingHeader.getCell("departureWeek"),
			    groupingHeader.getCell("departureYear"),
			    groupingHeader.getCell("departureTimeBucket"));
		departureInfoCell.setText( Messages.getString( getClass(), "departureInfo" ) );
		departureInfoCell.setStyleName("centered");

		HeaderCell shopNumberCell = groupingHeader.join(
			    groupingHeader.getCell("marketMetrics.currentShopNumber"),
			    groupingHeader.getCell("marketMetrics.intakeShopNumberOneWeek"));
		shopNumberCell.setText( Messages.getString( getClass(), "shopNumber" ) );
		shopNumberCell.setStyleName("centered");

		HeaderCell revenueCell = groupingHeader.join(
			    groupingHeader.getCell("marketMetrics.currentRevenue"),
			    groupingHeader.getCell("marketMetrics.intakeRevenueOneWeek"));
		revenueCell.setText( Messages.getString( getClass(), "revenue" ) );
		revenueCell.setStyleName("centered");

		HeaderCell orderNumberCell = groupingHeader.join(
			    groupingHeader.getCell("marketMetrics.currentOrderNumber"),
			    groupingHeader.getCell("marketMetrics.intakeOrderNumberOneWeek"));
		orderNumberCell.setText( Messages.getString( getClass(), "orderNumber" ) );
		orderNumberCell.setStyleName("centered");


		HeaderCell shopNumberDpCell = groupingHeader.join(
			    groupingHeader.getCell("marketMetricsDeepair.currentShopNumber"),
			    groupingHeader.getCell("marketMetricsDeepair.intakeShopNumberOneWeek"));
		shopNumberDpCell.setText( Messages.getString( getClass(), "shopNumber" ) );
		shopNumberDpCell.setStyleName("centered");

		HeaderCell revenueDpCell = groupingHeader.join(
			    groupingHeader.getCell("marketMetricsDeepair.currentRevenue"),
			    groupingHeader.getCell("marketMetricsDeepair.intakeRevenueOneWeek"));
		revenueDpCell.setText( Messages.getString( getClass(), "revenue" ) );
		revenueDpCell.setStyleName("centered");

		HeaderCell orderNumberDpCell = groupingHeader.join(
			    groupingHeader.getCell("marketMetricsDeepair.currentOrderNumber"),
			    groupingHeader.getCell("marketMetricsDeepair.intakeOrderNumberOneWeek"));
		orderNumberDpCell.setText( Messages.getString( getClass(), "orderNumber" ) );
		orderNumberDpCell.setStyleName("centered");

		HeaderRow dpHeader = prependHeaderRow();

		HeaderCell emptyCell = dpHeader.join(
			    groupingHeader.getCell("market"),
			    groupingHeader.getCell("marketCategory"));

		HeaderCell emptyCell2 = dpHeader.join(
			    groupingHeader.getCell("productCategory"),
			    groupingHeader.getCell("product"),
			    groupingHeader.getCell("departureWeek"),
			    groupingHeader.getCell("departureYear"),
			    groupingHeader.getCell("departureTimeBucket"));

		HeaderCell totalCell = dpHeader.join(
			    groupingHeader.getCell("marketMetrics.currentShopNumber"),
			    groupingHeader.getCell("marketMetrics.intakeShopNumberOneWeek"),
			    groupingHeader.getCell("marketMetrics.currentRevenue"),
			    groupingHeader.getCell("marketMetrics.intakeRevenueOneWeek"),
			    groupingHeader.getCell("marketMetrics.currentOrderNumber"),
			    groupingHeader.getCell("marketMetrics.intakeOrderNumberOneWeek"));
//		totalCell.setText( Messages.getString( getClass(), "total" ) );
//		totalCell.setStyleName("centered");

		HeaderCell totalDpCell = dpHeader.join(
			    groupingHeader.getCell("marketMetricsDeepair.currentShopNumber"),
			    groupingHeader.getCell("marketMetricsDeepair.intakeShopNumberOneWeek"),
			    groupingHeader.getCell("marketMetricsDeepair.currentRevenue"),
			    groupingHeader.getCell("marketMetricsDeepair.intakeRevenueOneWeek"),
			    groupingHeader.getCell("marketMetricsDeepair.currentOrderNumber"),
			    groupingHeader.getCell("marketMetricsDeepair.intakeOrderNumberOneWeek"));
		totalDpCell.setText( Messages.getString( getClass(), "totalDp" ) );
		totalDpCell.setStyleName("centered");
	}
}
