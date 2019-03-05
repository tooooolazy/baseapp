package com.tooooolazy.ui.services.beans;

import java.io.Serializable;
import java.util.LinkedHashMap;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"asJSON"})
public class OnlineResult implements Serializable {
	private static final long serialVersionUID = 1L;

//	protected JobFailureCode failCode;
	protected Object resultObject;

	public OnlineResult() {}

//	public JobFailureCode getFailCode() {
//		return failCode;
//	}

//	public void setFailCode(JobFailureCode failCode) {
//		this.failCode = failCode;
//	}

	public Object getResultObject() {
		return resultObject;
	}

	public void setResultObject(Object resultObject) {
		this.resultObject = resultObject;
	}
//	public JSONObject getAsJSON() {
//		if (resultObject != null && resultObject instanceof LinkedHashMap)
//			return new JSONObject( (LinkedHashMap)getResultObject() );
//		if (resultObject != null && resultObject instanceof String)
//			return new JSONObject( resultObject.toString() );
//
//		return null;
//	}
}
