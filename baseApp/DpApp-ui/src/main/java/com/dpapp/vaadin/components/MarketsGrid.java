package com.dpapp.vaadin.components;

import java.text.NumberFormat;

import com.dpapp.data.beans.MarketBean;
import com.tooooolazy.util.Messages;
import com.tooooolazy.vaadin.components.BaseGrid;
import com.tooooolazy.vaadin.ui.BaseUI;
import com.vaadin.ui.components.grid.HeaderCell;
import com.vaadin.ui.components.grid.HeaderRow;
import com.vaadin.ui.renderers.NumberRenderer;

public class MarketsGrid extends BaseGrid<MarketBean> {

	public MarketsGrid() {
		super();
		setId("MarketsGrid");

		init();
	}

	protected void init() {
		setBeanType(MarketBean.class);
	}

	public void attach() {
		super.attach();

		addColumns();
		adjustMainHeader();
		setGroupHeaders();
	}

	private void adjustMainHeader() {
		HeaderRow mainHeader = getDefaultHeaderRow();
		makeTwoLineHeader(mainHeader, "conversion.thisWeek", "thisWeek");
		makeTwoLineHeader(mainHeader, "conversion.thisMonth", "thisMonth");
		makeTwoLineHeader(mainHeader, "conversion.nextMonth", "nextMonth");

		makeTwoLineHeader(mainHeader, "scale.thisWeek", "thisWeek");
		makeTwoLineHeader(mainHeader, "scale.thisMonth", "thisMonth");
		makeTwoLineHeader(mainHeader, "scale.nextMonth", "nextMonth");

		makeTwoLineHeader(mainHeader, "revenue.thisWeek", "thisWeek");
		makeTwoLineHeader(mainHeader, "revenue.thisMonth", "thisMonth");
		makeTwoLineHeader(mainHeader, "revenue.nextMonth", "nextMonth");

		makeTwoLineHeader(mainHeader, "revenueToPassenger.thisWeek", "thisWeek");
		makeTwoLineHeader(mainHeader, "revenueToPassenger.thisMonth", "thisMonth");
		makeTwoLineHeader(mainHeader, "revenueToPassenger.nextMonth", "nextMonth");
	}

	private void addColumns() {
		addColumn( "market" ).setCaption( Messages.getString( getClass(), "market" ) );

		addColumn( "conversion.thisWeek", new NumberRenderer( NumberFormat.getPercentInstance( BaseUI.get().getLocale() ) ) ).setStyleGenerator(market -> "red");
		addColumn( "conversion.thisMonth", new NumberRenderer( NumberFormat.getPercentInstance( BaseUI.get().getLocale() ) ) ).setCaption( Messages.getString( getClass(), "thisMonth" ) ).setStyleGenerator(market -> "red");
		addColumn( "conversion.nextMonth", new NumberRenderer( NumberFormat.getPercentInstance( BaseUI.get().getLocale() ) ) ).setCaption( Messages.getString( getClass(), "nextMonth" ) ).setStyleGenerator(market -> "green");

		addColumn( "scale.thisWeek", new NumberRenderer( NumberFormat.getIntegerInstance( BaseUI.get().getLocale() ) ) ).setCaption( Messages.getString( getClass(), "thisWeek" ) ).setStyleGenerator(market -> "red");
		addColumn( "scale.thisMonth", new NumberRenderer( NumberFormat.getIntegerInstance( BaseUI.get().getLocale() ) ) ).setCaption( Messages.getString( getClass(), "thisMonth" ) ).setStyleGenerator(market -> "red");
		addColumn( "scale.nextMonth", new NumberRenderer( NumberFormat.getIntegerInstance( BaseUI.get().getLocale() ) ) ).setCaption( Messages.getString( getClass(), "nextMonth" ) ).setStyleGenerator(market -> "green");

		addColumn( "revenue.thisWeek", new NumberRenderer( NumberFormat.getIntegerInstance( BaseUI.get().getLocale() ) ) ).setCaption( Messages.getString( getClass(), "thisWeek" ) ).setStyleGenerator(market -> "red");
		addColumn( "revenue.thisMonth", new NumberRenderer( NumberFormat.getIntegerInstance( BaseUI.get().getLocale() ) ) ).setCaption( Messages.getString( getClass(), "thisMonth" ) ).setStyleGenerator(market -> "red");
		addColumn( "revenue.nextMonth", new NumberRenderer( NumberFormat.getIntegerInstance( BaseUI.get().getLocale() ) ) ).setCaption( Messages.getString( getClass(), "nextMonth" ) ).setStyleGenerator(market -> "green");

		addColumn( "revenueToPassenger.thisWeek", new NumberRenderer( NumberFormat.getIntegerInstance( BaseUI.get().getLocale() ) ) ).setCaption( Messages.getString( getClass(), "thisWeek" ) ).setStyleGenerator(market -> "red");
		addColumn( "revenueToPassenger.thisMonth", new NumberRenderer( NumberFormat.getIntegerInstance( BaseUI.get().getLocale() ) ) ).setCaption( Messages.getString( getClass(), "thisMonth" ) ).setStyleGenerator(market -> "red");
		addColumn( "revenueToPassenger.nextMonth", new NumberRenderer( NumberFormat.getIntegerInstance( BaseUI.get().getLocale() ) ) ).setCaption( Messages.getString( getClass(), "nextMonth" ) ).setStyleGenerator(market -> "green");
	}

	private void setGroupHeaders() {
		HeaderRow groupingHeader = prependHeaderRow();
		HeaderCell conversionCell = groupingHeader.join(
			    groupingHeader.getCell("conversion.thisWeek"),
			    groupingHeader.getCell("conversion.thisMonth"),
			    groupingHeader.getCell("conversion.nextMonth"));
		conversionCell.setText( Messages.getString( getClass(), "conversion" ) );
		conversionCell.setStyleName("centered");

		HeaderCell scaleCell = groupingHeader.join(
			    groupingHeader.getCell("scale.thisWeek"),
			    groupingHeader.getCell("scale.thisMonth"),
			    groupingHeader.getCell("scale.nextMonth"));
		scaleCell.setText( Messages.getString( getClass(), "scale" ) );
		scaleCell.setStyleName("centered");

		HeaderCell revenueCell = groupingHeader.join(
			    groupingHeader.getCell("revenue.thisWeek"),
			    groupingHeader.getCell("revenue.thisMonth"),
			    groupingHeader.getCell("revenue.nextMonth"));
		revenueCell.setText( Messages.getString( getClass(), "revenue" ) );
		revenueCell.setStyleName("centered");

		HeaderCell revenueToPassengerCell = groupingHeader.join(
			    groupingHeader.getCell("revenueToPassenger.thisWeek"),
			    groupingHeader.getCell("revenueToPassenger.thisMonth"),
			    groupingHeader.getCell("revenueToPassenger.nextMonth"));
		revenueToPassengerCell.setText( Messages.getString( getClass(), "revenueToPassenger" ) );
		revenueToPassengerCell.setStyleName("centered");
	}
}
