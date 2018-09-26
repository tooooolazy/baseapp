package com.tooooolazy.util.pdf;

import java.util.Map;
import java.util.ResourceBundle;
import java.util.Vector;
import java.util.logging.Logger;

import javax.servlet.ServletOutputStream;

import org.json.JSONObject;

public class ExportHelper {
	protected static Logger log = Logger.getAnonymousLogger();
	protected static ResourceBundle resources;
	public static final String BASE_RESOURCE_CLASS = "com.icap.web.db.DbResources";


	protected ServletOutputStream out;
	protected JSONObject params; 

	protected ExportHelper() {
		resources = ResourceBundle.getBundle(BASE_RESOURCE_CLASS);
	}

	public static Vector<Map> getVectorData(String query) {


		Vector<Map> data = new Vector<Map>();

		return data;
	}
}
