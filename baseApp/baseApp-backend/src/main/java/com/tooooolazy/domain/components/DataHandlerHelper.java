package com.tooooolazy.domain.components;

import java.net.Inet4Address;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.tooooolazy.data.interfaces.OnlineKeys;

@Component
public class DataHandlerHelper {

	@Autowired
	protected Environment env;

	@PersistenceContext
	protected EntityManager entityManager;

	/**
	 * Retrieves DB schema from Environment parameters. If there is none default value 'PCB_USER1' is used
	 * @return
	 */
	public String getSchema() {
		String schema = env.getProperty("db.schema");
		return schema;
	}

	public Object getClassTypes() {
		JSONObject jo = getClassTypesJo();

		return jo.toString();
	}
	public JSONObject getClassTypesJo() {
		JSONObject jo = new JSONObject();

//		// we need to modify the Views list that the application will load based on FlatEvaluatorMode param being enabled or not
//		Map map = getFlatEvaluatorFlowParam( null );
//		ApplicationParameter fefp = (ApplicationParameter)map.get("flatEvaluatorFlow");
//		boolean flatEvaluatorFlow = fefp.getParameterValueInt() == 1;
//		String[] modes = fefp.getFailMessage().split("_"); // there should be 2 modes - 0: disabled, 1: enabled
//		List<String> _exViews = Arrays.asList( modes[ flatEvaluatorFlow ? 1 : 0 ].split(",") );  // this is the list of View codes that should be excluded!
//
		List<String> exViews = new ArrayList<String>();
//		exViews.addAll( _exViews );
//
//		Map map1 = getQualitativeDataSplitParam( null );
//		ApplicationParameter qdsp = (ApplicationParameter)map1.get("qualitativeDataSplit");
//		boolean qualitativeDataSplit = qdsp.getParameterValueInt() == 1;
//		if ( !qualitativeDataSplit )
//			exViews.add( qdsp.getFailCode()+"" );

		JSONArray ja = new JSONArray();
		jo.put("CLASS_TYPES", ja);
		Query q = entityManager.createNativeQuery("select * from " + getSchema() + ".TYPECLASS");
		List<Object[]> res = q.getResultList();
		for (Object[] lf : res) {
			if ( exViews.contains( lf[0].toString() ) )
				continue;

			JSONObject _jo = new JSONObject();
			_jo.put(OnlineKeys.ID, lf[0].toString());
			_jo.put(OnlineKeys.VALUE, lf[1].toString());
			_jo.put("groupId", lf[2].toString());
			ja.put(_jo);
		}

		return jo;
	}

	public Object getMSecLevelDefs(String username) {
		JSONObject jo = getMSecLevelDefsJo(username);

		return jo.toString();
	}
	public JSONObject getMSecLevelDefsJo(String username) {
		JSONObject jo = new JSONObject();

		JSONArray ja = new JSONArray();
		jo.put(OnlineKeys.DATA, ja);
		Query q = createMSecLevelDefsQuery();
		try {
			List<Object[]> res = q.getResultList();
			for (Object[] ur : res) {
				JSONArray _ja = new JSONArray();
				for (Object uro : ur) {
					_ja.put(uro);
				}
				ja.put(_ja);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return jo;
	}
	public Query createMSecLevelDefsQuery() {
		StringBuffer sb = new StringBuffer();
		sb.append("select mld.rolecode, mld.method_name, tc.class_name, mld.allow, mld.DESCRIPTION from " + getSchema() + ".MSECLEVELDEFS mld inner join " + getSchema() + ".TYPECLASS tc on mld.class_id=tc.id");

		Query q = entityManager.createNativeQuery(sb.toString());

		return q;
	}


	public Object logLogin(String username, String result, Map params) {
		return logLogin(username, result, (String)params.getOrDefault("browser", null), (Integer)params.getOrDefault("major", null), (Integer)params.getOrDefault("minor", null), (String)params.getOrDefault("address", null) );
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
