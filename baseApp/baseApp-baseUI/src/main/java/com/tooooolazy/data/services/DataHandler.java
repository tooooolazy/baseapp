package com.tooooolazy.data.services;

import java.util.HashMap;
import java.util.Map;

import com.tooooolazy.data.interfaces.AbstractDataHandler;
import com.tooooolazy.data.services.beans.OnlineParams;
import com.tooooolazy.data.services.beans.OnlineResult;
import com.tooooolazy.data.services.beans.UserBean;

public class DataHandler extends AbstractDataHandler {

//	protected DataHandlerClient dataHandlerClient;
    /**
     * Default constructor. 
     */
//    public DataHandler() {
//    	dataHandlerClient = new DataHandlerClientImpl( ServicesContext.singleton().getProperty( "pcb.services.rest.endpoint.dataHandler" ) );
//    }
	@Override
	public OnlineResult getData(String dataType, UserBean user, boolean blockIfUpdating, Map params, boolean requiresTransaction) throws Exception {
		OnlineResult res = null;

		OnlineParams op = new OnlineParams();

		if (params == null)
			params = new HashMap();
		op.setMethodParams( params );
		params.put("blockIfUpdating", blockIfUpdating);

		op.setMethod( dataType );

		if ( user != null )
			op.setUserCode( user.getUserCode() );

//		if (!requiresTransaction)
//			res = dataHandlerClient.execute(op);
//		else
//			res = dataHandlerClient.executeUpdate(op);

		return res;
	}

}
