package com.dpapp.data.beans;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonGetter;

public class MarketMetricsBean implements Serializable {
	protected double currentShopNumber;
	protected double intakeShopNumberOneWeek;
	protected double currentRevenue;
	protected double intakeRevenueOneWeek;
	protected double currentOrderNumber;
	protected double intakeOrderNumberOneWeek;

	public MarketMetricsBean() {
		
	}
	@JsonGetter("CurrentShopNumber")
	public double getCurrentShopNumber() {
		return currentShopNumber;
	}
	public void setCurrentShopNumber(double currentShopNumber) {
		this.currentShopNumber = currentShopNumber;
	}
	@JsonGetter("IntakeShopNumberOneWeek")
	public double getIntakeShopNumberOneWeek() {
		return intakeShopNumberOneWeek;
	}
	public void setIntakeShopNumberOneWeek(double intakeShopNumberOneWeek) {
		this.intakeShopNumberOneWeek = intakeShopNumberOneWeek;
	}
	@JsonGetter("CurrentRevenue")
	public double getCurrentRevenue() {
		return currentRevenue;
	}
	public void setCurrentRevenue(double currentRevenue) {
		this.currentRevenue = currentRevenue;
	}
	@JsonGetter("IntakeRevenueOneWeek")
	public double getIntakeRevenueOneWeek() {
		return intakeRevenueOneWeek;
	}
	public void setIntakeRevenueOneWeek(double intakeRevenueOneWeek) {
		this.intakeRevenueOneWeek = intakeRevenueOneWeek;
	}
	@JsonGetter("CurrentOrderNumber")
	public double getCurrentOrderNumber() {
		return currentOrderNumber;
	}
	public void setCurrentOrderNumber(double currentOrderNumber) {
		this.currentOrderNumber = currentOrderNumber;
	}
	@JsonGetter("IntakeOrderNumberOneWeek")
	public double getIntakeOrderNumberOneWeek() {
		return intakeOrderNumberOneWeek;
	}
	public void setIntakeOrderNumberOneWeek(double intakeOrderNumberOneWeek) {
		this.intakeOrderNumberOneWeek = intakeOrderNumberOneWeek;
	}
}
