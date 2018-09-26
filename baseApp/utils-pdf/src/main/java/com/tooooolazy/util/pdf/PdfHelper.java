package com.tooooolazy.util.pdf;

import java.lang.reflect.InvocationTargetException;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.servlet.ServletOutputStream;

import org.json.JSONException;
import org.json.JSONObject;


import com.itextpdf.text.Document;
import com.tooooolazy.util.TLZUtils;

public class PdfHelper extends ExportHelper {
	public static final String DEFAULT_PACKAGE = PdfHelper.class.getPackage().getName() + ".";

	public static ResourceBundle bundle;// = ResourceBundle.getBundle("com/icap/attikis/common/pdf/domain/reports_el", new Locale("el_GR"));
	public static void reloadBundle() {
		String s = PdfHelper.class.getPackage().getName();
		s = s.replace(".", "/");
		bundle.clearCache();
		bundle = ResourceBundle.getBundle(s + "/reports_el", new Locale("el_GR"), new ResourceBundle.Control() {
			@Override
			public boolean needsReload(String baseName, Locale locale,
					String format, ClassLoader loader, ResourceBundle bundle,
					long loadTime) {
				return true;
			}
		});
	}
	

	protected PdfHelper() {
		super();
		if (bundle == null)
			reloadBundle();
	}
	public PdfHelper(int reqId, ServletOutputStream out, String reportName) {
		this();
		try {
			params = new JSONObject(getFromBundle(reportName));
			params.put("name", reportName);
			params.put("reqId", reqId);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.out = out;
	}
	public Document createReport() throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
		String reportName = params.optString("name", "");
		PdfReport pdf;
		try {
			pdf = (PdfReport)TLZUtils.loadObject(DEFAULT_PACKAGE + "PdfReport_" + reportName);
		} catch (Exception e) {
			log.info("PdfReport_" + reportName + " not found. Using default.");
			pdf = new PdfReport();
		}
		return pdf.create(out, params);
	}

	/**
	 * Retrieves a string from resource bundle.
	 * <ol>Three attempts are made until the key is found:
	 * <li>look for <b><code>[reportName].[part].key</code></b></li>
	 * <li>look for <b><code>part.[part].key</code></b></li>
	 * <li>look for <b><code>part.key</code></b></li>
	 * </ol>
	 * @param reportName
	 * @param part
	 * @param key
	 * @return
	 */
	public static String getFromBundle(String reportName, String part, String key) {
		String string = null;
		try {
			string = getFromBundle(reportName + "." + part + "." + key);
		} catch (MissingResourceException e) {
			try {
				string = getFromBundle("part." + part + "." + key);
			} catch (MissingResourceException e1) {
				try {
					string = getFromBundle("part." + key);
				} catch (MissingResourceException e2) {
					// TODO Auto-generated catch block
					string = "!" + key + "!";
				}
			}
		}
		return string;
	}
	public static String getFromBundle(String string) {
		return bundle.getString(string);
	}
	public static String[] getOrderedPartElements(String reportName, String part) {
		String ordering = getFromBundle(reportName, part, "ordering");
		if (ordering != null && !ordering.startsWith("!"))
			return ordering.split(",");

		return new String[] {};
	}
}
