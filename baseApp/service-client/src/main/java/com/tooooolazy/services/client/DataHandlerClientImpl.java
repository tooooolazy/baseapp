package com.tooooolazy.services.client;

import org.apache.wink.client.ClientResponse;

import com.tooooolazy.data.interfaces.DataHandlerClient;
import com.tooooolazy.data.services.beans.OnlineBaseParams;
import com.tooooolazy.data.services.beans.OnlineBaseResult;

public class DataHandlerClientImpl<OR extends OnlineBaseResult, OP extends OnlineBaseParams> extends ClientBase implements DataHandlerClient<OR, OP> {

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
	public OR execute(OP params) {
		ClientResponse response = post( dataHandlerEndpoint + "/execute", params );
		return handleResult(response, orClass);	
	}

	@Override
	public OR executeUpdate(OP params) {
		ClientResponse response = post( dataHandlerEndpoint + "/executeUpdate", params );
		return handleResult(response, orClass);	
	}

}
