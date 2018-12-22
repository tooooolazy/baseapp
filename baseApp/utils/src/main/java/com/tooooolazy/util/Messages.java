package com.tooooolazy.util;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.management.RuntimeErrorException;

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
	protected static String[] supportedLocales = new String[] {"en", "el"};
	/**
	 * @param locales
	 */
	public static void setSupportedLocales( String[] locales ) {
		supportedLocales = locales;
		setMainBundle(BUNDLE_NAME_EN);
	}

	private static final ThreadLocal<String> _lang = new ThreadLocal<String>() {
		@Override
		protected String initialValue() {
			return supportedLocales[0];
		}
	};
	public static String getLang() {
		return _lang.get();
	}

	public static void setLang(String newLang) {
		if ( Arrays.asList(supportedLocales).contains( newLang ) ) {
			_lang.set(newLang);
		} else
			throw new RuntimeErrorException( new Error("Lang " + newLang + " not supported") );
	}

	protected static final String BUNDLE_NAME_EN = Messages.class.getPackage().getName() + ".messages";

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
		RESOURCE_BUNDLES.clear();

		for (int i=0; i<supportedLocales.length; i++) {
			RESOURCE_BUNDLES.put(supportedLocales[i], ResourceBundle.getBundle(bundleName, new Locale( supportedLocales[i] )));
		}
	}

	protected Messages() {
	}

	public static void addBundle(String bundle) {
		Map newMap = new HashMap<String, ResourceBundle>();
		try {
			for (int i=0; i<supportedLocales.length; i++) {
				newMap.put(supportedLocales[i], ResourceBundle.getBundle(bundle, new Locale( supportedLocales[i] )));
			}
			EXTRA_BUNDLES.add(newMap);
		} catch (Exception e) {
			for (int i=0; i<supportedLocales.length; i++) {
				newMap.put(supportedLocales[i], ResourceBundle.getBundle(bundle));
			}
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
			return null; //'!' + key + '!';
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
		String str = getString( key );
		if (str == null)
			str = '!' + cl.getSimpleName() + " . " + key + '!';
		return str;
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
		int ni = 0;
		for (int i=0; i<supportedLocales.length; i++) {
			if ( getLang().equals( supportedLocales[i] ) ) {
				ni = i+1;
				break;
			}
		}
		if (ni == supportedLocales.length)
			ni = 0;
		setLang( supportedLocales[ni] );

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
