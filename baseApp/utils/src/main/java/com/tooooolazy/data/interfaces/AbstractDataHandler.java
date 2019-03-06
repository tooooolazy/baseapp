package com.tooooolazy.data.interfaces;

import java.util.List;
import java.util.Map;

import com.tooooolazy.data.services.beans.OnlineResult;
import com.tooooolazy.data.services.beans.UserBean;
import com.tooooolazy.util.SearchCriteria;

public abstract class AbstractDataHandler<OR extends OnlineResult> implements DataHandlerService<OR> {

	@Override
	public <C extends SearchCriteria> List getListData(C criteria, String orderBy) {
		return null;
	}

	@Override
	public OR getData(String dataType, UserBean user, Map params) throws Exception {
		return getData(dataType, user, true, params, false);
	}

	@Override
	public OR getData(String dataType, UserBean user, boolean blockIfUpdating, Map params) throws Exception {
		return getData(dataType, user, blockIfUpdating, params, false);
	}

	@Override
	public OR getData(String dataType, UserBean user, Map params, boolean requiresTransaction) throws Exception {
		return getData(dataType, user, true, params, requiresTransaction);
	}

}
