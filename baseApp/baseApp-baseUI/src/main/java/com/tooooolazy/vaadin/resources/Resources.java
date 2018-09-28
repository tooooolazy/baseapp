package com.tooooolazy.vaadin.resources;

import java.util.HashMap;
import java.util.Map;

import com.vaadin.server.ThemeResource;

public class Resources {
	protected static Map<String, ThemeResource> resourceMap = new HashMap<String, ThemeResource>();

	/**
	 * icon should have extension "png"
	 * @param pathInTheme eg "img/actions/"
	 * @param key
	 * @return
	 */
	public static ThemeResource getPng(String pathInTheme, String key) {
		ThemeResource img = resourceMap.get(key);
		if (img != null)
			return img;
		img = new ThemeResource( pathInTheme + key + ".png" );
		resourceMap.put(key, img);

		return img;
	}

	public static ThemeResource get(String pathInTheme, String key) {
		ThemeResource img = resourceMap.get(key);
		if (img != null)
			return img;
		img = new ThemeResource( pathInTheme + key );
		resourceMap.put(key, img);

		return img;
	}
}
