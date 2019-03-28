package com.dpapp.vaadin.views;

import com.dpapp.ws.beans.DpAppUserBean;
import com.dpapp.ws.beans.JobFailureCode;
import com.dpapp.ws.beans.OnlineResult;
import com.tooooolazy.util.SearchCriteria;
import com.tooooolazy.vaadin.views.BaseView;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Component;

public class DpAppBaseView<C extends SearchCriteria, E> extends BaseView<C, E, DpAppUserBean, OnlineResult, JobFailureCode> {

	@Override
	protected boolean showTitleInContent() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	protected void addSearchCriteria(AbstractComponent ac) {
		// TODO Auto-generated method stub

	}


	@Override
	protected void addJavascriptFunctions() {
		// TODO Auto-generated method stub

	}

	@Override
	protected Class getCriteriaClass() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	protected Component createContent() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	protected OnlineResult[] getWSContents() {
		// TODO Auto-generated method stub
		return null;
	}

}
