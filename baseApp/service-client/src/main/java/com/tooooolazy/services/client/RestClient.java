package com.tooooolazy.services.client;

import org.apache.wink.client.ClientResponse;


/**
 * Simple rest client to get Data from a URL (as json string). Can subclass in order to modify timeouts by overriding methods {@link #getConnectionTimeout()} and {@link #getReadTimeout()}
 * @author gpatoulas
 *
 */
public class RestClient extends ClientBase {

	public RestClient() {
		super();
	}

	protected int getConnectionTimeout() {
		return 15000;
	}
	protected int getReadTimeout() {
		return 15000;
	}

	/**
	 * @param url - the url to get data from
	 * @return - json string of data
	 */
	public String getData(String url) {
		ClientResponse response = super.get(url);
		return handleResult(response, String.class);
	}
}
