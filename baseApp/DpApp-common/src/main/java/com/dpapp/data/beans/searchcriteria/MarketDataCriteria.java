package com.dpapp.data.beans.searchcriteria;

import com.dpapp.data.beans.MarketDataBean;
import com.tooooolazy.util.SearchCriteria;

public class MarketDataCriteria extends SearchCriteria {
	protected MarketDataBean mdb = new MarketDataBean();

	@Override
	public boolean hasNone() {
		// TODO Auto-generated method stub
		return false;
	}

	public Integer getDepartureWeek() {
		return mdb.getDepartureWeek();
	}
	public void setDepartureWeek(Integer departureWeek) {
		mdb.setDepartureWeek(departureWeek);
	}
	public Integer getDepartureYear() {
		return mdb.getDepartureYear();
	}
	public void setDepartureYear(Integer departureYear) {
		mdb.setDepartureYear(departureYear);
	}

	public String createUrlParams() {
		return "departureWeek=" + getDepartureWeek() + "&departureYear=" + getDepartureYear();
	}
}
