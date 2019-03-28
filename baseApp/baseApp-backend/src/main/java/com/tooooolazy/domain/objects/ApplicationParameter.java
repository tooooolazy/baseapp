package com.tooooolazy.domain.objects;

// Generated 26 ��� 2011 2:44:39 �� by Hibernate Tools 3.4.0.CR1


import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;


@Entity
@Table(name = "APPLICATIONPARAMETER")
public class ApplicationParameter implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "PARAMETERCODE", unique = true, nullable = false)
	private int parameterCode;
	@Column(name = "MAPPINGTYPE", columnDefinition = "smallint", nullable = false)
	private int applicationParameterType;
	@Column(name = "DESCRIPTION", nullable = false, length = 128)
	private String description;
	@Column(name = "PARAMETERVALUE", columnDefinition = "decimal", precision = 15)
	private BigDecimal parameterValue;
	@Column(name = "VALUEFROM", columnDefinition = "decimal", precision = 15 )
	private BigDecimal valueFrom;
	@Column(name = "VALUETO", columnDefinition = "decimal", precision = 15 )
	private BigDecimal valueTo;
	@Column(name = "USERINSERT", nullable = false, length = 30)
	private String userInsert;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TIMEINSERT", nullable = false, length = 26)
	private Date timeInsert;
	@Column(name = "USERUPDATE", nullable = false, length = 30)
	private String userUpdate;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TIMEUPDATE", nullable = false, length = 26)
	private Date timeUpdate;
	@Column(name = "CATEGORY")
	private int category;
	@Column(name = "FAILCODE")
	private int failCode;
	@Column(name = "P_ORDER")
	private int order;
	@Column(name = "FAILMESSAGE", length = 500)
	private String failMessage;
	
	public ApplicationParameter() {
	}

	public ApplicationParameter(int parameterCode, String description, BigDecimal parameterValue, String userInsert, Date timeInsert,
			int applicationParameterType, int category, int failCode, BigDecimal valueFrom, BigDecimal valueTo, int order,
			String failMessage) {
		this.parameterCode = parameterCode;
		this.description = description;
		this.parameterValue = parameterValue;
		this.userInsert = userInsert;
		this.timeInsert = timeInsert;
		this.userUpdate = userInsert;
		this.timeUpdate = timeInsert;
		this.applicationParameterType = applicationParameterType;
		this.category = category;
		this.failCode = failCode;
		this.valueFrom = valueFrom;
		this.valueTo = valueTo;
		this.order = order;
		this.failMessage = failMessage;
	}

	public int getParameterCode() {
		return this.parameterCode;
	}

	public void setParameterCode(int parameterCode) {
		this.parameterCode = parameterCode;
	}

	public int getApplicationParameterType() {
		return applicationParameterType;
	}

	public void setApplicationParameterType(
			int applicationParameterType) {
		this.applicationParameterType = applicationParameterType;
	}
	
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getParameterValue() {
		return this.parameterValue;
	}
	
	@javax.persistence.Transient
	public void setParameterValueInt(BigDecimal parameterValue) {
		this.parameterValue = parameterValue;
	}
	@javax.persistence.Transient
	public Integer getParameterValueInt() {
		if (parameterValue != null)
			return this.parameterValue.intValue();
		return null;
	}

	public void setParameterValue(BigDecimal parameterValue) {
		this.parameterValue = parameterValue;
	}

	public String getUserInsert() {
		return this.userInsert;
	}

	public void setUserInsert(String userInsert) {
		this.userInsert = userInsert;
	}

	public Date getTimeInsert() {
		return this.timeInsert;
	}

	public void setTimeInsert(Date timeInsert) {
		this.timeInsert = timeInsert;
	}
	
	public String getUserUpdate() {
		return this.userUpdate;
	}
	
	public void setUserUpdate(String userUpdate) {
		this.userUpdate = userUpdate;
	}
	
	public Date getTimeUpdate() {
		return this.timeUpdate;
	}
	
	public void setTimeUpdate(Date timeUpdate) {
		this.timeUpdate = timeUpdate;
	}

	public int getCategory() {
		return category;
	}

	public void setCategory(int category) {
		this.category = category;
	}

	public int getFailCode() {
		return failCode;
	}

	public void setFailCode(int failCode) {
		this.failCode = failCode;
	}

	public String getFailMessage() {
		return failMessage;
	}

	public void setFailMessage(String failMessage) {
		this.failMessage = failMessage;
	}

	public BigDecimal getValueFrom() {
		return valueFrom;
	}

	public void setValueFrom(BigDecimal valueFrom) {
		this.valueFrom = valueFrom;
	}

	public BigDecimal getValueTo() {
		return valueTo;
	}

	public void setValueTo(BigDecimal valueTo) {
		this.valueTo = valueTo;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}


	@Transient
	public Date getValueFromAsDate() {
		if (getValueFrom() != null) {
			Date d = new Date(getValueFrom().longValue());
			return d;
		}
		return null;
	}
	public void setValueFromAsDate(Date date) {
		setValueFrom(new BigDecimal(date.getTime()));
	}
	@Transient
	public Date getValueToAsDate() {
		if (getValueTo() != null) {
			Date d = new Date(getValueTo().longValue());
			return d;
		}
		return null;
	}
	public void setValueToAsDate(Date date) {
		setValueTo(new BigDecimal(date.getTime()));
	}
}
