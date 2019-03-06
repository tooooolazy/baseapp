package com.tooooolazy.services.client;

import org.apache.wink.client.ClientResponse;

import com.tooooolazy.data.interfaces.DataHandlerClient;
import com.tooooolazy.data.services.beans.OnlineParams;
import com.tooooolazy.data.services.beans.OnlineResult;

public class DataHandlerClientImpl extends ClientBase implements DataHandlerClient {

	protected String dataHandlerEndpoint;

	protected DataHandlerClientImpl() {
	}

	public DataHandlerClientImpl(String dataHandlerEndpoint) {
		super();
		this.dataHandlerEndpoint = dataHandlerEndpoint;
	}

	@Override
	public OnlineResult execute(OnlineParams params) {
		ClientResponse response = post( dataHandlerEndpoint + "/execute", params );
		return handleResult(response, OnlineResult.class);	
	}

	@Override
	public OnlineResult executeUpdate(OnlineParams params) {
		ClientResponse response = post( dataHandlerEndpoint + "/executeUpdate", params );
		return handleResult(response, OnlineResult.class);	
	}

}
