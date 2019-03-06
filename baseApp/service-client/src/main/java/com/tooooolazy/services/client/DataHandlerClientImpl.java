package com.tooooolazy.services.client;

import org.apache.wink.client.ClientResponse;

import com.tooooolazy.data.interfaces.DataHandlerClient;
import com.tooooolazy.data.services.beans.OnlineParams;
import com.tooooolazy.data.services.beans.OnlineResult;

public class DataHandlerClientImpl<OR extends OnlineResult> extends ClientBase implements DataHandlerClient<OR> {

	protected String dataHandlerEndpoint;
	protected Class<OR> orClass; 

	protected DataHandlerClientImpl() {
	}

	public DataHandlerClientImpl(String dataHandlerEndpoint, Class<OR> _class) {
		super();
		this.dataHandlerEndpoint = dataHandlerEndpoint;
		this.orClass = _class;
	}

	@Override
	public OR execute(OnlineParams params) {
		ClientResponse response = post( dataHandlerEndpoint + "/execute", params );
		return handleResult(response, orClass);	
	}

	@Override
	public OR executeUpdate(OnlineParams params) {
		ClientResponse response = post( dataHandlerEndpoint + "/executeUpdate", params );
		return handleResult(response, orClass);	
	}

}
