package com.tooooolazy.data.services.beans;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class OnlineParams implements Serializable {
	protected Integer userCode;

	protected String method;
	protected Map<String, Object> methodParams;
	protected boolean logCurrentAction;

	public OnlineParams() {}


	public Integer getUserCode() {
		return userCode;
	}

	public void setUserCode(Integer userCode) {
		this.userCode = userCode;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public Map<String, Object> getMethodParams() {
		if (methodParams == null)
			methodParams = new HashMap<String, Object>();
		return methodParams;
	}

	public void setMethodParams(Map<String, Object> methodParams) {
		this.methodParams = methodParams;
	}

	public boolean isLogCurrentAction() {
		return logCurrentAction;
	}

	public void setLogAction(boolean logCurrentAction) {
		this.logCurrentAction = logCurrentAction;
	}


}
