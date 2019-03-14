package com.tooooolazy.vaadin.ui;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.tooooolazy.util.Pair;

/**
 * http://blog.adeel.io/tag/vaadin/
 * <p>
 * Did not use the 'deRegisterAllTabsForThisVaadinSession' on user logout since we still want to know how many tabs are open AND userCode is always ZERO (we don't really care about it)
 * </p>
 * <p>
 * Might want to replace Pair with a custom one: --> DONE in module utils
 * https://stackoverflow.com/questions/521171/a-java-collection-of-value-pairs-tuples
 * </p>
 * @author gpatoulas
 *
 */
public class ApplicationLevelTracker {
	public static HashMap<String, Pair<Boolean, Long>> browserTabUniqueIDToIdleTimeHashMap = new HashMap<String, Pair<Boolean, Long>>();

	private String thisBrowserTabUniqueID;

	public ApplicationLevelTracker(int userID, String vaadinSessionID) {
		thisBrowserTabUniqueID = userID + ":" + vaadinSessionID + ":"
				+ UUID.randomUUID().toString();
		registerIdle(false);
	}

	public void registerIdle(boolean idle) {
		Date date = new Date();
		browserTabUniqueIDToIdleTimeHashMap.put(thisBrowserTabUniqueID,
				new Pair<Boolean, Long>(idle, date.getTime()));
	}

	public void deRegisterThisTab() {
		browserTabUniqueIDToIdleTimeHashMap.remove(thisBrowserTabUniqueID);
	}

	public void deRegisterAllTabsForThisVaadinSession() {
		String userID = thisBrowserTabUniqueID.split(":")[0];
		String vaadinSessionID = thisBrowserTabUniqueID.split(":")[1];
		Set<String> keySet = ApplicationLevelTracker.browserTabUniqueIDToIdleTimeHashMap
				.keySet();
		List<String> browserTabUniqueIDsToRemove = new ArrayList<String>();
		for (String key : keySet) {
			String _userID = key.split(":")[0];
			String _vaadinSessionID = key.split(":")[1];
			if (_userID.equals(userID)
					&& _vaadinSessionID.equals(vaadinSessionID))
				browserTabUniqueIDsToRemove.add(key);
		}
		for (String browserTabUniqueID : browserTabUniqueIDsToRemove) {
			browserTabUniqueIDToIdleTimeHashMap.remove(browserTabUniqueID);
		}
	}
	public int getUserTabs() {
		String userID = thisBrowserTabUniqueID.split(":")[0];
		String vaadinSessionID = thisBrowserTabUniqueID.split(":")[1];
		Set<String> keySet = ApplicationLevelTracker.browserTabUniqueIDToIdleTimeHashMap
				.keySet();
		List<String> browserTabUniqueIDsToRemove = new ArrayList<String>();
		for (String key : keySet) {
			String _userID = key.split(":")[0];
			String _vaadinSessionID = key.split(":")[1];
			if (_userID.equals(userID)
					&& _vaadinSessionID.equals(vaadinSessionID))
				browserTabUniqueIDsToRemove.add(key);
		}
		return browserTabUniqueIDsToRemove.size();
	}
}
