package com.tooooolazy.data.services.beans;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Defines WS needed parameters ie {@link DataHandlerClient}.
 * <ol>
 * <li><b>method</b>: the method the WS should call</li>
 * <li><b>methodParams</b>: the method's parameters</li>
 * </ol>
 * @author gpatoulas
 *
 */
public class OnlineBaseParams implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected Integer userCode;
	protected boolean userRequired;


	protected String method;
	protected Map<String, Object> methodParams;
	protected boolean logCurrentAction;

	public OnlineBaseParams() {}


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

	public boolean isUserRequired() {
		return userRequired;
	}

	public void setUserRequired(boolean userRequired) {
		this.userRequired = userRequired;
	}
}
