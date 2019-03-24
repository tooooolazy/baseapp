package com.dpapp.data.beans;

import java.io.Serializable;

public class MarketSectionBean implements Serializable {
	protected Double thisWeek;
	protected Double thisMonth;
	protected Double nextMonth;
	
	public MarketSectionBean() {
		// TODO Auto-generated constructor stub
	}
	
	public MarketSectionBean(double thisWeek, double thisMonth, double nextMonth) {
		super();
		this.thisWeek = thisWeek;
		this.thisMonth = thisMonth;
		this.nextMonth = nextMonth;
	}

	public Double getThisWeek() {
		return thisWeek;
	}
	public void setThisWeek(Double thisWeek) {
		this.thisWeek = thisWeek;
	}
	public Double getThisMonth() {
		return thisMonth;
	}
	public void setThisMonth(Double thisMonth) {
		this.thisMonth = thisMonth;
	}
	public Double getNextMonth() {
		return nextMonth;
	}
	public void setNextMonth(Double nextMonth) {
		this.nextMonth = nextMonth;
	}
}
