package com.dpapp.vaadin.components;

import java.text.NumberFormat;

import com.dpapp.data.beans.MarketBean;
import com.dpapp.data.beans.MarketSectionBean;
import com.tooooolazy.util.Messages;
import com.vaadin.ui.Grid;
import com.vaadin.ui.components.grid.HeaderCell;
import com.vaadin.ui.components.grid.HeaderRow;
import com.vaadin.ui.renderers.NumberRenderer;

public class MarketsGrid extends Grid<MarketBean> {

	public MarketsGrid() {
		super();

		init();
	}

	protected void init() {
		setBeanType(MarketBean.class);

		addColumns();
		setGroupHeaders();
	}

	private void addColumns() {
		addColumn( "market" ).setCaption( Messages.getString( getClass(), "market" ) );

		addColumn( "conversion.thisWeek", new NumberRenderer( NumberFormat.getPercentInstance() ) ).setCaption( Messages.getString( getClass(), "thisWeek" ) );
		addColumn( "conversion.thisMonth", new NumberRenderer( NumberFormat.getPercentInstance() )  ).setCaption( Messages.getString( getClass(), "thisMonth" ) );
		addColumn( "conversion.nextMonth", new NumberRenderer( NumberFormat.getPercentInstance() )  ).setCaption( Messages.getString( getClass(), "nextMonth" ) );

		addColumn( "scale.thisWeek" ).setCaption( Messages.getString( getClass(), "thisWeek" ) );
		addColumn( "scale.thisMonth" ).setCaption( Messages.getString( getClass(), "thisMonth" ) );
		addColumn( "scale.nextMonth" ).setCaption( Messages.getString( getClass(), "nextMonth" ) );

		addColumn( "revenue.thisWeek" ).setCaption( Messages.getString( getClass(), "thisWeek" ) );
		addColumn( "revenue.thisMonth" ).setCaption( Messages.getString( getClass(), "thisMonth" ) );
		addColumn( "revenue.nextMonth" ).setCaption( Messages.getString( getClass(), "nextMonth" ) );

		addColumn( "revenueToPassenger.thisWeek" ).setCaption( Messages.getString( getClass(), "thisWeek" ) );
		addColumn( "revenueToPassenger.thisMonth" ).setCaption( Messages.getString( getClass(), "thisMonth" ) );
		addColumn( "revenueToPassenger.nextMonth" ).setCaption( Messages.getString( getClass(), "nextMonth" ) );
	}

	private void setGroupHeaders() {
		HeaderRow groupingHeader = prependHeaderRow();
		HeaderCell conversionCell = groupingHeader.join(
			    groupingHeader.getCell("conversion.thisWeek"),
			    groupingHeader.getCell("conversion.thisMonth"),
			    groupingHeader.getCell("conversion.nextMonth"));
		conversionCell.setText( Messages.getString( getClass(), "conversion" ) );

		HeaderCell scaleCell = groupingHeader.join(
			    groupingHeader.getCell("scale.thisWeek"),
			    groupingHeader.getCell("scale.thisMonth"),
			    groupingHeader.getCell("scale.nextMonth"));
		scaleCell.setText( Messages.getString( getClass(), "scale" ) );

		HeaderCell revenueCell = groupingHeader.join(
			    groupingHeader.getCell("revenue.thisWeek"),
			    groupingHeader.getCell("revenue.thisMonth"),
			    groupingHeader.getCell("revenue.nextMonth"));
		revenueCell.setText( Messages.getString( getClass(), "revenue" ) );

		HeaderCell revenueToPassengerCell = groupingHeader.join(
			    groupingHeader.getCell("revenueToPassenger.thisWeek"),
			    groupingHeader.getCell("revenueToPassenger.thisMonth"),
			    groupingHeader.getCell("revenueToPassenger.nextMonth"));
		revenueToPassengerCell.setText( Messages.getString( getClass(), "revenueToPassenger" ) );
	}

	public void attach() {
		super.attach();
	}
}
