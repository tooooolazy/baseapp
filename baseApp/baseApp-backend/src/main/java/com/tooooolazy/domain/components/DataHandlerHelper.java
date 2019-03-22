package com.tooooolazy.domain.components;

import java.net.Inet4Address;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class DataHandlerHelper {

	@Autowired
	protected Environment env;

//	@PersistenceContext
	protected EntityManager entityManager;

	/**
	 * Retrieves DB schema from Environment parameters. If there is none default value 'PCB_USER1' is used
	 * @return
	 */
	public String getSchema() {
		String schema = env.getProperty("db.schema");
		return schema;
	}

	public Object logLogin(String username, String result, String browser, Integer major, Integer minor, String address) {
		if (address == null) 
			address = getIp();
		int count = 0;
		try {
			Query q = entityManager.createNativeQuery("insert into " + getSchema() + ".USER_LOG (USER_NAME, RESULT, CR_DATE, BROWSER, MAJOR, MINOR, ADDRESS) values (:un, :result, :d, :browser, :major, :minor, :address)");
			q.setParameter("un", username);
			q.setParameter("result", result);
			q.setParameter("d", new Date());
			q.setParameter("browser", browser!=null?browser:"");
			q.setParameter("major", major!=null?major:0);
			q.setParameter("minor", minor!=null?minor:0);
			q.setParameter("address", address);
			count = q.executeUpdate();
		} catch (Exception e) {
			LogManager.getLogger().info("Could not execure new insert query with IP!!!");

		}
		return "{'count':" + count + "}";
	}
	public String getIp() {
		String addr = null;
		try {
			addr = Inet4Address.getLocalHost().getHostAddress();
		} catch (Exception e) {
			LogManager.getLogger().info(e.getMessage());
		}
		return addr;
	}

}
