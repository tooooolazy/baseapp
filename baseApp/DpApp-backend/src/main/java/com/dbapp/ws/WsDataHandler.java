package com.dbapp.ws;


import org.springframework.stereotype.Component;

import com.dpapp.ws.beans.JobFailureCode;
import com.dpapp.ws.beans.OnlineParams;
import com.dpapp.ws.beans.OnlineResult;
import com.tooooolazy.domain.DataBaseRepository;
import com.tooooolazy.ws.WsBaseDataHandler;

@Component
public class WsDataHandler extends WsBaseDataHandler<OnlineResult, OnlineParams> {

	public WsDataHandler(DataBaseRepository dataRepository) {
		super(dataRepository);
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

}
