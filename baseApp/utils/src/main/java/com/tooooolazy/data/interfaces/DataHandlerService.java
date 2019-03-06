package com.tooooolazy.data.interfaces;

import java.util.List;
import java.util.Map;

import com.tooooolazy.data.services.beans.OnlineResult;
import com.tooooolazy.data.services.beans.UserBean;
import com.tooooolazy.util.SearchCriteria;

/**
 * Basically the converter that transforms all data required by UI into JSONObjects.
 * @author gpatoulas
 *
 */
public interface DataHandlerService {
	public <C extends SearchCriteria> List getListData(C criteria, String orderBy);

	/**
	 * Helper delegate that assumes a 'DB transaction' is NOT needed AND 'blocks' itself if APP data is being updated
	 * @param dataType - the method to call
	 * @param user
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public OnlineResult getData(String dataType, UserBean user, Map params) throws Exception;
	/**
	 * Helper delegate that assumes a 'DB transaction' is NOT needed (Only select queries)
	 * @param dataType - the method to call
	 * @param user
	 * @param blockIfUpdating
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public OnlineResult getData(String dataType, UserBean user, boolean blockIfUpdating, Map params) throws Exception;
	/**
	 * Helper delegate that assumes a 'DB transaction' is needed (Only select queries)
	 * @param dataType - the method to call
	 * @param user
	 * @param params
	 * @param requiresTransaction
	 * @return
	 * @throws Exception
	 */
	public OnlineResult getData(String dataType, UserBean user, Map params, boolean requiresTransaction) throws Exception;
	/**
	 * Main delegate to retrieve TEP Data from WS
	 * @param dataType - the method to call
	 * @param user
	 * @param blockIfUpdating
	 * @param params
	 * @param requiresTransaction
	 * @return
	 * @throws Exception
	 */
	public OnlineResult getData(String dataType, UserBean user, boolean blockIfUpdating, Map params, boolean requiresTransaction) throws Exception;

}
