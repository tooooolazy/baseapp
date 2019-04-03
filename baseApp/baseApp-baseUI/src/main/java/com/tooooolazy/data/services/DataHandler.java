package com.tooooolazy.data.services;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.tooooolazy.data.interfaces.AbstractDataHandler;
import com.tooooolazy.data.interfaces.DataHandlerClient;
import com.tooooolazy.data.interfaces.OnlineKeys;
import com.tooooolazy.data.interfaces.WsMethods;
import com.tooooolazy.data.services.beans.OnlineBaseParams;
import com.tooooolazy.data.services.beans.OnlineBaseResult;
import com.tooooolazy.data.services.beans.UserBean;
import com.tooooolazy.services.client.DataHandlerClientImpl;
import com.tooooolazy.vaadin.ui.BaseUI;

/**
 * Helper class that creates a {@link DataHandlerClient} used to retrieve data from a WS
 * @author gpatoulas
 *.
 * @param <OR>
 * @param <OP>
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

		if ( user != null ) {
			op.setUserCode( user.getUserCode() );
			op.setUsername( user.getCredentials().getUsername() );
		}

		if (!requiresTransaction)
			res = dataHandlerClient.execute(op);
		else
			res = dataHandlerClient.executeUpdate(op);

		return res;
	}
	protected abstract OP createOnlineParams();

	@Override
	public JSONObject getMethodSecurityDefs() {
		Map<String, Object> params = new HashMap<String, Object>();
		
		try {
			OR tor = getData(WsMethods.MSECLEVELDEFS, null, false, params);
			if (tor != null) {
				Object jfc = tor.getFailCode();
				if (jfc != null) {
					// TODO need to do something to avoid everything being visible!!!!
				} else {
					JSONObject jo = tor.getAsJSON();

					if ( BaseUI.get().requiresSecDefs() && jo.optJSONArray(OnlineKeys.DATA) == null ) {
						throw new SecurityException("No Security Definitions defined in DB");
					}
						
					return jo;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
}
