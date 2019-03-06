package com.tooooolazy.data.services.beans;

import java.io.Serializable;
import java.util.LinkedHashMap;

import org.json.JSONObject;

/**
 * @author gpatoulas
 *
 * @param <JFC> - JobFailure Enum class. can use default {@link JobFailureCode} or a custom one.
 * @param <JO> - json Object class.
 */
//@JsonIgnoreProperties({"asJSON"})
public class OnlineResult<JFC> implements Serializable {
	private static final long serialVersionUID = 1L;

	protected JFC failCode;
	protected Object resultObject;

	public OnlineResult() {}

	public JFC getFailCode() {
		return failCode;
	}

	public void setFailCode(JFC failCode) {
		this.failCode = failCode;
	}

	public Object getResultObject() {
		return resultObject;
	}

	public void setResultObject(Object resultObject) {
		this.resultObject = resultObject;
	}
	public JSONObject getAsJSON() {
		if (resultObject != null && resultObject instanceof LinkedHashMap)
			return new JSONObject( (LinkedHashMap)getResultObject() );
		if (resultObject != null && resultObject instanceof String)
			return new JSONObject( resultObject.toString() );

		return null;
	}
}
