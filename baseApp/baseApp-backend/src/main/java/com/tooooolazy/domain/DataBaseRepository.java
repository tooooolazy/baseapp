package com.tooooolazy.domain;

import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.tooooolazy.domain.components.PasswordManager;
import com.tooooolazy.ws.WsBaseDataHandler;


/**
 * This is where {@link WsBaseDataHandler} delegates everything (the base class anyway).
 * <p>Like {@link WsBaseDataHandler}, it should be overridden. Also '@Component' must be used on derived class. The derived class should be used to call {@link WsBaseDataHandler} constructor</p>
 * 
 * @author gpatoulas
 *
 */
//@Component
public abstract class DataBaseRepository extends AbstractJDBCRepository {
	public static String DEFAULT_USER_INSERT = "SYSTEM";

	@Autowired
	protected PasswordManager passwordManager;


	public Object getEnvironment(Map params) {
		JSONObject jo = new JSONObject();
		jo.put("environment", env.getProperty("deploy.environment") );

		return jo.toMap();
	}

	public boolean inProd() {
		return env.getProperty("pcb.deploy.environment").equals("PROD");
	}
	public boolean inUAT() {
		return env.getProperty("pcb.deploy.environment").equals("UAT");
	}

	/**
	 * Retrieves DB schema from Environment parameters. If there is none default value 'PCB_USER1' is used
	 * @return
	 */
	protected String getSchema() {
		String schema = env.getProperty("db.schema");
		if (schema == null)
			schema = "PCB_USER1";
		return schema;
	}
}
