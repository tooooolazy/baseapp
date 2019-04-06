package com.dpapp.vaadin.components.searchcriteria;

import com.dpapp.data.beans.searchcriteria.MarketDataCriteria;
import com.tooooolazy.util.Messages;
import com.tooooolazy.util.TLZUtils;
import com.tooooolazy.vaadin.components.InputComponent;
import com.tooooolazy.vaadin.components.WeekComboBox;
import com.tooooolazy.vaadin.views.BaseView;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.TextField;

public class MarketDataCriteriaComponent extends InputComponent<MarketDataCriteria> {
//marketCategory=ROUTE&departureWeek=16&departureYear=2019
	protected WeekComboBox week;
	protected TextField year;
	
	public MarketDataCriteriaComponent(ClickListener listener, BaseView baseView) {
		super(listener, baseView);
	}

	@Override
	protected int getMaxColumns() {
		return 3;
	}

	@Override
	protected void createBinder() {
		super.createBinder();

		binder.forField( week )
			.asRequired( Messages.getString( _class, "Form.requiredMessage") )
			.bind( MarketDataCriteria::getDepartureWeek, MarketDataCriteria::setDepartureWeek );
		binder.forField( year ).withConverter( new StringToIntegerConverter("error"))
			.asRequired( Messages.getString( _class, "Form.requiredMessage") )
			.bind( MarketDataCriteria::getDepartureYear, MarketDataCriteria::setDepartureYear );
	}

	@Override
	protected MarketDataCriteria setBinderBean() {
		bean = (MarketDataCriteria) baseView.getCriteria();
		if (bean.getDepartureYear() == null)
			bean.setDepartureYear( TLZUtils.getCurrentYear() );
		if (bean.getDepartureWeek() == null)
			bean.setDepartureWeek( TLZUtils.getCurrentWeek() );
		binder.setBean(bean);

		return bean;
	}

	@Override
	public Component[] getFieldsArray() {
		return new Component[] {week, year};
	}
	@Override
	protected boolean showSubInfo() {
		return false;
	}
	@Override
	protected boolean showClearButton() {
		return false;
	}
	@Override
	protected void createInputFields() {
		week = new WeekComboBox( Messages.getString( getClass(), "week" ) );
		year = new TextField( Messages.getString( getClass(), "year" ) );
	}


}
