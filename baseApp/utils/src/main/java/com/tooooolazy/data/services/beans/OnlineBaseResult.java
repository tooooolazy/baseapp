package com.tooooolazy.data.services.beans;

import java.io.Serializable;
import java.util.LinkedHashMap;

import org.json.JSONObject;

/**
 * Extend this Base class in order to specify {@link JobFailureCode} enum class. Each App UI need to declare their own 'OnlineResult' class and their own 'JobFailureCode' enum.
 * <p>Make sure services respond with the same object</p>
 *  
 * @author gpatoulas
 *
 * @param <JFC> - JobFailure Enum class. can use default {@link JobFailureCode} or a custom one.
 */
//@JsonIgnoreProperties({"asJSON"})
public abstract class OnlineBaseResult<JFC> implements Serializable {
	private static final long serialVersionUID = 1L;

	protected JFC failCode;
	protected Object resultObject;

	public OnlineBaseResult() {}

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
