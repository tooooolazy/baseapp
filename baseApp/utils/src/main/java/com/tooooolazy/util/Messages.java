package com.tooooolazy.util;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Provides multilingual support based on Resource bundles.
 * Currently supports Greek and English.<br>
 * The default resource bundle files should be located in the same package as this class named: messages_en/el.properties.<br>
 * Also provides a helper function to add an extra resource bundle.
 * If the same key is defined in both bundles then the one in 'extra' is used.<br>
 * A class parameter can be used to get a message key which provides an 'override' system for multilingual messages.
 * <p>
 * {@link #setMainBundle(String)} must be called to set a resource bundle (there is no default bundle here)
 * </p>
 * @author tooooolazy
 *
 */
public class Messages {
	private static final ThreadLocal<String> _lang = new ThreadLocal<String>() {
		@Override
		protected String initialValue() {
			return "en";
		}
	};
	public static String getLang() {
		return _lang.get();
	}

	public static void setLang(String newLang) {
		_lang.set(newLang);
	}

	protected static final String BUNDLE_NAME_EN = Messages.class.getPackage().getName() + ".messages";
//	public static String lang = "en";

	protected static final Map<String, ResourceBundle> RESOURCE_BUNDLES = new HashMap<String, ResourceBundle>();
	protected static final ArrayList<Map<String, ResourceBundle>> EXTRA_BUNDLES = new ArrayList<Map<String,ResourceBundle>>();

	static {
		try {
//			if (RESOURCE_BUNDLES.isEmpty()) // needed so extension classes 
				setMainBundle(BUNDLE_NAME_EN);
		} catch(Exception e) {
			// no default resource bundles... but never mind
		}
	}

	/**
	 * Sets the main bundle that this class should use.
	 * @param bundleName 
	 * 
	 */
	public static void setMainBundle(String bundleName) {
		RESOURCE_BUNDLES.put("en", ResourceBundle.getBundle(bundleName, new Locale("en")));
		RESOURCE_BUNDLES.put("el", ResourceBundle.getBundle(bundleName, new Locale("el")));
	}

	protected Messages() {
	}

	public static void addBundle(String bundle) {
		Map newMap = new HashMap<String, ResourceBundle>();
		try {
			newMap.put("en", ResourceBundle.getBundle(bundle, new Locale("en")));
			newMap.put("el", ResourceBundle.getBundle(bundle, new Locale("el")));
			EXTRA_BUNDLES.add(newMap);
		} catch (Exception e) {
			newMap.put("en", ResourceBundle.getBundle(bundle));
			newMap.put("el", ResourceBundle.getBundle(bundle));
			e.printStackTrace();
		}
	}

	public static void changeLocale(String newLang) {
		setLang(newLang);
	}

	public static String getString(String key) {
		for (int i=EXTRA_BUNDLES.size(); i>0; i--) {
			Map<String, ResourceBundle> map = EXTRA_BUNDLES.get(i-1);
			try {
				return map.get(getLang()).getString(key);
			} catch (Exception ee) {
			}
		}
		try {
			return RESOURCE_BUNDLES.get(getLang()).getString(key);
		} catch (Exception ee) {
			return '!' + key + '!';
		}
	}
	public static String getString(Class cl, String key) {
		Class _cl = cl;
		while (_cl != null) {
			String str = getString(_cl.getSimpleName(), key);
			if (str != null)
				return str;
			else
				_cl = _cl.getSuperclass();		
		}
//		for (int i=EXTRA_BUNDLES.size(); i>0; i--) {
//			Map<String, ResourceBundle> map = EXTRA_BUNDLES.get(i-1);
//			try {
//				return map.get(getLang()).getString(key);
//			} catch (Exception ee) {
//			}
//		}
//		try {
//			return RESOURCE_BUNDLES.get(getLang()).getString(key);
//		} catch (Exception e2) {
//		}
//
//		return '!' + cl.getSimpleName() + " . " + key + '!';
		return getString( key );
	}
	private static String getString(String cl, String key) {
		for (int i=EXTRA_BUNDLES.size(); i>0; i--) {
			Map<String, ResourceBundle> map = EXTRA_BUNDLES.get(i-1);
			try {
				return map.get(getLang()).getString(cl + "." + key);
			} catch (Exception ee) {
			}
		}
		try {
			return RESOURCE_BUNDLES.get(getLang()).getString(cl + "." + key);
		} catch (Exception e2) {
			return null;
		}
	}

	public static String toggleLocale() {
		if (getLang().equals("el"))
			setLang("en");
		else
			setLang("el");

		return getLang();
	}
	public static String format(String msg, Object ... params) {
		return MessageFormat.format(msg, params);
	}
	public static String formatKey(String key, Object ... params) {
		return MessageFormat.format(getString(key), params);
	}
	public static String formatKey(Class _class, String key, Object ... params) {
		return MessageFormat.format(getString(_class, key), params);
	}
}
