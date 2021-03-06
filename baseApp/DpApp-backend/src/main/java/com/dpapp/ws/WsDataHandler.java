package com.dpapp.ws;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dpapp.domain.DataRepository;
import com.dpapp.ws.beans.JobFailureCode;
import com.dpapp.ws.beans.OnlineParams;
import com.dpapp.ws.beans.OnlineResult;
import com.tooooolazy.ws.WsBaseDataHandler;

/**
 * @author gpatoulas
 *
 * @see {@link WsBaseDataHandler}
 */
@Component
public class WsDataHandler extends WsBaseDataHandler<DataRepository, OnlineResult, OnlineParams> {

	@Autowired
	protected DataRepository dataRepository;


	@Override
	protected DataRepository getDataRepository() {
		return dataRepository;
	}

	@Override
	protected OnlineResult createResultObject() {
		return new OnlineResult();
	}

	@Override
	protected Object getGenericFailCode() {
		return JobFailureCode.GENERIC;
	}

	@Override
	protected Object getItemLockedFailCode() {
		return JobFailureCode.ITEM_LOCKED;
	}

	@Override
	protected boolean userVerified(Integer userCode) {
		return false;
	}

	@Override
	protected Object getMultipleLoginFailCode() {
		return JobFailureCode.LOGIN_MULTIPLE;
	}

}
