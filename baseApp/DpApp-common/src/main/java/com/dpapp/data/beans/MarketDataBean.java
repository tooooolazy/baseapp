package com.dpapp.data.beans;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonGetter;

public class MarketDataBean implements Serializable {

	protected String market;
	protected String marketCategory;
	protected String departureTimeBucket;
	protected String productCategory;
	protected String product;
	protected Integer departureWeek;
	protected Integer departureYear;
	protected Boolean active;

	protected MarketMetricsBean marketMetrics;
	protected MarketMetricsBean marketMetricsDeepair;

	@JsonGetter("Market")
	public String getMarket() {
		return market;
	}
	public void setMarket(String market) {
		this.market = market;
	}
	@JsonGetter("MarketCategory")
	public String getMarketCategory() {
		return marketCategory;
	}
	public void setMarketCategory(String marketCategory) {
		this.marketCategory = marketCategory;
	}
	@JsonGetter("DepartureTimeBucket")
	public String getDepartureTimeBucket() {
		return departureTimeBucket;
	}
	public void setDepartureTimeBucket(String departureTimeBucket) {
		this.departureTimeBucket = departureTimeBucket;
	}
	@JsonGetter("ProductCategory")
	public String getProductCategory() {
		return productCategory;
	}
	public void setProductCategory(String productCategory) {
		this.productCategory = productCategory;
	}
	@JsonGetter("Product")
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}
	@JsonGetter("DepartureWeek")
	public Integer getDepartureWeek() {
		return departureWeek;
	}
	public void setDepartureWeek(Integer departureWeek) {
		this.departureWeek = departureWeek;
	}
	@JsonGetter("DepartureYear")
	public Integer getDepartureYear() {
		return departureYear;
	}
	public void setDepartureYear(Integer departureYear) {
		this.departureYear = departureYear;
	}
	@JsonGetter("Active")
	public Boolean getActive() {
		return active;
	}
	public void setActive(Boolean active) {
		this.active = active;
	}
	@JsonGetter("MarketMetrics")
	public MarketMetricsBean getMarketMetrics() {
		return marketMetrics;
	}
	public void setMarketMetrics(MarketMetricsBean marketMetrics) {
		this.marketMetrics = marketMetrics;
	}
	@JsonGetter("MarketMetricsDeepair")
	public MarketMetricsBean getMarketMetricsDeepair() {
		return marketMetricsDeepair;
	}
	public void setMarketMetricsDeepair(MarketMetricsBean marketMetricsDeepair) {
		this.marketMetricsDeepair = marketMetricsDeepair;
	}
}
