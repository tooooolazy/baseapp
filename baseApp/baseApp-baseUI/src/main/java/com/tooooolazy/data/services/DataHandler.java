package com.tooooolazy.data.services;

import java.util.HashMap;
import java.util.Map;

import com.tooooolazy.data.interfaces.AbstractDataHandler;
import com.tooooolazy.data.interfaces.DataHandlerClient;
import com.tooooolazy.data.services.beans.OnlineBaseParams;
import com.tooooolazy.data.services.beans.OnlineBaseResult;
import com.tooooolazy.data.services.beans.UserBean;
import com.tooooolazy.services.client.DataHandlerClientImpl;

/**
 * Helper class that creates a {@link DataHandlerClient} used to retrieve data from a WS
 * @author gpatoulas
 *.
 * @param <OR>
 */
public abstract class DataHandler<OR extends OnlineBaseResult, OP extends OnlineBaseParams> extends AbstractDataHandler<OR, OP> {

	protected DataHandlerClient<OR, OP> dataHandlerClient;
//	protected Class<OR> orClass; 

	/**
     * Default constructor. 
     */
	public DataHandler(Class<OR> _orClass) {
		String endPoint = ServicesContext.singleton().getProperty( "services.rest.endpoint.dataHandler") ;
//		this.orClass = _class;
		dataHandlerClient = new DataHandlerClientImpl( endPoint, _orClass );
	}
	@Override
	public OR getData(String dataType, UserBean user, boolean blockIfUpdating, Map params, boolean requiresTransaction) throws Exception {
		OR res = null;

		OP op = createOnlineParams();

		if (params == null)
			params = new HashMap();
		op.setMethodParams( params );
		params.put("blockIfUpdating", blockIfUpdating);

		op.setMethod( dataType );

		if ( user != null )
			op.setUserCode( user.getUserCode() );

		if (!requiresTransaction)
			res = dataHandlerClient.execute(op);
		else
			res = dataHandlerClient.executeUpdate(op);

		return res;
	}
	protected abstract OP createOnlineParams();

}
