package com.tooooolazy.util;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import javax.persistence.Query;

public abstract class SearchCriteria<PK> implements Serializable {
	protected int resultsPerPage;
	protected int step;

	protected int curPage;
	protected long curTotal;

	protected boolean modified;

	protected List<PK> pks;
	protected BooleanStatus deleted;

	protected String orderBy;
	protected Set orderByFields;
	protected String[] fieldsForOrder;

	public SearchCriteria() {
		curPage = 1;
		step = 10;
		resultsPerPage = 50;
		deleted = BooleanStatus.FALSE; // should be null if entity does not have this column
		orderBy = "";
	}
	public boolean isModified() {
		return modified;
	}
	public void setModified(boolean modified) {
		this.modified = modified;
	}

	public boolean isValidPage(int page) {
		if (page > 0 && page <= getLastPage())
			return true;
		return false;
	}
	public int getLastPage() {
		int lp = (int) curTotal/resultsPerPage;
		if (lp * resultsPerPage < curTotal)
			lp++;
		return lp;
	}

	public void setResultsPerPage(int count) {
		resultsPerPage = count;
	}
	public int getResultsPerPage() {
		return resultsPerPage;
	}
	public void setCurPage(int count) {
		curPage = count;
	}
	public int getCurPage() {
		return curPage;
	}
	public void setCurTotal(long count) {
		curTotal = count;
	}
	public long getCurTotal() {
		return curTotal;
	}
	public void setStep(int step) {
		this.step = step;
	}
	public int getStep() {
		return step;
	}

	public String getRange() {
		if (getCurPage() < getLastPage())
			return (getCurPage()-1)*getResultsPerPage() + 1 + " - " + getCurPage()*getResultsPerPage();
		else
			return (getCurPage()-1)*getResultsPerPage() + 1 +" - " + getCurTotal();
	}


	public List<PK> getsPks() {
		return pks;
	}

	public void setPks(List<PK> pks) {
		this.pks = pks;
	}
	public BooleanStatus getDeleted() {
		return deleted;
	}

	public void setDeleted(BooleanStatus deleted) {
		this.deleted = deleted;
	}

	public abstract boolean hasNone();



	public StringBuffer createCountQuery() {
		return new StringBuffer();
	}
	public StringBuffer createFetchQuery() {
		return new StringBuffer();
	}
	public StringBuffer createWhereClause() {
		StringBuffer sb = new StringBuffer("where 1=1 "); // so we always use 'and'
		if (deleted != null) {
			sb.append("and s.isDeleted = :sDeleted ");
		}
		return sb;
	}
	public void setQueryParams(Query q) {
		if (deleted != null) {
			q.setParameter("sDeleted", deleted.getBooleanValue());
		}
	}
	public String getOrderByClause() {
		if (orderByFields != null && !orderByFields.isEmpty())
			return createOrderByClause();

		return orderBy;
	}
	public void setOrderByClause(String orderBy) {
		this.orderBy = orderBy;
	}

	public void setFieldsForOrder(String[] fields) {
		fieldsForOrder = fields;
	}
	public String[] getFieldsForOrder() {
		return fieldsForOrder;
	}
	public Set getOrderByFields() {
		return orderByFields;
	}
	public void setOrderByFields(Set fields) {
		orderByFields = fields;
	}
	protected String createOrderByClause() {
		if (orderByFields != null && !orderByFields.isEmpty()) {
			String ob = "order by ";
			for (Object o : orderByFields) {
				ob += "s."+o.toString() + ", ";
			}
			ob = ob.substring(0, ob.length()-2);
			return ob;
		}
		return "";
	}


	public Integer getMaxStep() {
		return null;
	}

	public Integer getMaxPerPage() {
		return null;
	}

}
