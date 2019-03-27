package com.tooooolazy.services.client;

import org.apache.wink.client.ClientResponse;


/**
 * Simple rest client to get Data from a URL (as json string)
 * @author gpatoulas
 *
 */
public class RestClient extends ClientBase {

	public RestClient() {
		super();
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
