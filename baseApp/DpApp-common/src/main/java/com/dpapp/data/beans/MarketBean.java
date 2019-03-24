package com.dpapp.data.beans;

import java.io.Serializable;

public class MarketBean implements Serializable {

	protected String market;
	protected MarketSectionBean conversion;
	protected MarketSectionBean scale;
	protected MarketSectionBean revenue;
	protected MarketSectionBean revenueToPassenger;

	public String getMarket() {
		return market;
	}
	public void setMarket(String market) {
		this.market = market;
	}
	public MarketSectionBean getConversion() {
		return conversion;
	}
	public void setConversion(MarketSectionBean conversion) {
		this.conversion = conversion;
	}
	public MarketSectionBean getScale() {
		return scale;
	}
	public void setScale(MarketSectionBean scale) {
		this.scale = scale;
	}
	public MarketSectionBean getRevenue() {
		return revenue;
	}
	public void setRevenue(MarketSectionBean revenue) {
		this.revenue = revenue;
	}
	public MarketSectionBean getRevenueToPassenger() {
		return revenueToPassenger;
	}
	public void setRevenueToPassenger(MarketSectionBean revenueToPassenger) {
		this.revenueToPassenger = revenueToPassenger;
	}

}
