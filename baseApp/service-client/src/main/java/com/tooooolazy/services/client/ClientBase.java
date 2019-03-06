package com.tooooolazy.services.client;


import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.MultivaluedMap;


//import org.apache.commons.lang3.StringUtils;
//import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.wink.client.ClientConfig;
import org.apache.wink.client.ClientResponse;
import org.apache.wink.client.ClientRuntimeException;
import org.apache.wink.client.ClientWebException;
import org.apache.wink.client.Resource;
import org.apache.wink.client.RestClient;
import org.apache.wink.common.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import com.tooooolazy.data.services.beans.JobFailureCode;
import com.tooooolazy.services.client.exceptions.RestServiceException;
import com.tooooolazy.services.client.exceptions.ServiceException;


/**
 * Base abstract class for our REST web service clients.
 * <p>
 * Please make sure that concrete subclasses are Spring beans. Furthermore, it is advisable that any client
 * responses are handled by {@link #handleResult(ClientResponse, Class)} to generate the response beans
 * as well as handle any error messages and generate the appropriate exceptions.
 * 
 * @author Dimitris Batis
 */
public abstract class ClientBase {
    
	protected ClientBase() {
	    javax.ws.rs.core.Application app = new javax.ws.rs.core.Application() {
	        public Set<Class<?>> getClasses() {
	            Set<Class<?>> classes = new HashSet<Class<?>>();
	            classes.add(JacksonJaxbJsonProvider.class);
	            return classes;
	        }

	    };

        ClientConfig c = new ClientConfig().applications( app );
        c.connectTimeout(500000);
        c.readTimeout(500000);
        client = new RestClient(c); 
	}
    /**
     * Content type for requests and responses.
     */
    protected String contentType = "application/json;charset=utf-8";
    
    /**
     * Apache Wink REST client
     */
    protected RestClient client;
    
    
    /**
     * Keep in mind that this should never be a static logger, for inheritance.
     */
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    //</editor-fold>
    
    //<editor-fold desc="Helper methods">
    
    /**
     * Makes a GET request to the REST web service.
     * 
     * @param url full path of URL to call
     * 
     * @return client response
     * 
     * @throws ClientWebException if the response code represents an error code
     * @throws ClientRuntimeException if there are other exceptions that the Wink client encounters
     * 
     */
    protected ClientResponse get(String url, String token) {
        return doRequest("GET", url, null, null, token);
    }
    
    protected ClientResponse get(String url) {
        return get( url, "" );
    }
    
    /**
     * Makes a GET request to the REST web service.
     * 
     * @param url full path of URL to call
     * @param querystringParameters optional querystring parameters. Null if none exist
     * 
     * @return client response
     * 
     * @throws ClientWebException if the response code represents an error code
     * @throws ClientRuntimeException if there are other exceptions that the Wink client encounters
     * 
     */
    protected ClientResponse get(String url, MultivaluedMap<String,String> querystringParameters, String token) {
        return doRequest("GET", url, null, querystringParameters, token);
    }
    
    protected ClientResponse get(String url, MultivaluedMap<String,String> querystringParameters) {
        return get( url, querystringParameters, null );
    }
    
    /**
     * Makes a DELETE request to the REST web service.
     * 
     * @param url full path of URL to call
     * 
     * @return client response
     * 
     * @throws ClientWebException if the response code represents an error code
     * @throws ClientRuntimeException if there are other exceptions that the Wink client encounters
     * 
     */
    protected ClientResponse delete(String url, String token) {
        return doRequest("DELETE", url, null, null, token);
    }
    
    protected ClientResponse delete(String url) {
        return delete( url, "" );
    }
    
    /**
     * Makes a DELETE request to the REST web service.
     * 
     * @param url full path of URL to call
     * @param querystringParameters optional querystring parameters. Null if none exist
     * 
     * @return client response
     * 
     * @throws ClientWebException if the response code represents an error code
     * @throws ClientRuntimeException if there are other exceptions that the Wink client encounters
     * 
     */
    protected ClientResponse delete(String url, MultivaluedMap<String,String> querystringParameters, String token) {
        return doRequest("DELETE", url, null, querystringParameters, token);
    }
    
    protected ClientResponse delete(String url, MultivaluedMap<String,String> querystringParameters) {
        return delete( url, querystringParameters, null );
    }
    
    /**
     * Makes a POST request to the REST web service.
     * 
     * @param url full path of URL to call
     * @param input HTTP request body as a native object that will be converted to the appropriate content type.
     * 
     * @return client response
     * 
     * @throws ClientWebException if the response code represents an error code
     * @throws ClientRuntimeException if there are other exceptions that the Wink client encounters
     * 
     */
    protected ClientResponse post(String url, Object input, String token) {
        return doRequest("POST", url, input, null, token);
    }
    
    protected ClientResponse post(String url, Object input) {
        return post( url, input, "" );
    }
    
    /**
     * Makes a POST request to the REST web service.
     * 
     * @param url full path of URL to call
     * @param input HTTP request body as a native object that will be converted to the appropriate content type.
     * @param querystringParameters optional querystring parameters. Null if none exist
     * 
     * @return client response
     * 
     * @throws ClientWebException if the response code represents an error code
     * @throws ClientRuntimeException if there are other exceptions that the Wink client encounters
     * 
     */
    protected ClientResponse post(String url, Object input, MultivaluedMap<String,String> querystringParameters, String token) {
        return doRequest("POST", url, input, querystringParameters, token);
    }
    
    protected ClientResponse post(String url, Object input, MultivaluedMap<String,String> querystringParameters) {
        return post( url, input, querystringParameters, null );
    }
    
    /**
     * Makes a PUT request to the REST web service.
     * 
     * @param url full path of URL to call
     * @param input HTTP request body as a native object that will be converted to the appropriate content type.
     * 
     * @return client response
     * 
     * @throws ClientWebException if the response code represents an error code
     * @throws ClientRuntimeException if there are other exceptions that the Wink client encounters
     * 
     */    
    protected ClientResponse put(String url, Object input, String token) {
        return doRequest("PUT", url, input, null, token);
    }
    
    protected ClientResponse put(String url, Object input) {
        return put( url, input, "" );
    }
    
    /**
     * Makes a PUT request to the REST web service.
     * 
     * @param url full path of URL to call
     * @param input HTTP request body as a native object that will be converted to the appropriate content type.
     * @param querystringParameters optional querystring parameters. Null if none exist
     * 
     * @return client response
     * 
     * @throws ClientWebException if the response code represents an error code
     * @throws ClientRuntimeException if there are other exceptions that the Wink client encounters
     * 
     */
    protected ClientResponse put(String url, Object input, MultivaluedMap<String,String> querystringParameters, String token) {
        return doRequest("PUT", url, input, querystringParameters, token);
    }
    
    protected ClientResponse put(String url, Object input, MultivaluedMap<String,String> querystringParameters) {
        return put( url, input, querystringParameters, "" );
    }
    
    /**
     * Makes an RPC-style POST request which follows certain conventions.
     * <p>
     * Unlike {@link #post(String, Object)} and other generic helper methods, this one expects that the
     * following conventions are observed, for an RPC-style patternt where the URL indicates the method
     * that will be executed:
     * <ul>
     *  <li>
     *      URLs will be formed as <code>&lt;endpoint&gt;/&lt;method&gt;</code>
     *  </li>
     *  <li>
     *      All calls are POST requests, even if they just retrieve content.
     *  </li>
     *  <li>
     *      All method arguments are placed in the HTTP request body object
     *  </li>
     * </ul>
     * 
     * @param endpoint base URL
     * @param method method name (URL part that will be appended to base URL)
     * @param input method input
     * 
     * @return method output
     * 
     * @throws ClientWebException if the response code represents an error code
     * @throws ClientRuntimeException if there are other exceptions that the Wink client encounters
     */
    protected ClientResponse rpcCall(String endpoint, String method, Object input, String token) {
        
        String properEndpoint = endpoint;
        String properMethod = method;
        if (endpoint.charAt(endpoint.length() - 1) == '/') { // make sure to remove trailing slash
            properEndpoint = properEndpoint.substring(0, properEndpoint.length() - 1);
        }
        if (method.charAt(0) == '/') { // remove leading slash
            properMethod = properMethod.substring(1);
        }
        
        return doRequest("POST", properEndpoint + "/" + properMethod, input, null, token);
    }
    
    protected ClientResponse rpcCall(String endpoint, String method, Object input) {
    	return rpcCall( endpoint, method, input, null );
    }
    
    /**
     * Makes a call to the REST web service.
     * 
     * @param verb "GET", "POST", "DELETE", "PUT" are the only accepted values
     * @param url full path of URL to call
     * @param input HTTP request body as a native object that will be converted to the appropriate content type.
     *              This value will NOT be used in GET and DELETE requests.
     * @param querystringParameters optional querystring parameters. Null if none exist
     * 
     * @return client response
     * 
     * @throws ClientWebException if the response code represents an error code
     * @throws ClientRuntimeException if there are other exceptions that the Wink client encounters
     * 
     */
    private ClientResponse doRequest(String verb, String url, Object input, MultivaluedMap<String,String> querystringParameters, String token ) {
        
        if (logger.isDebugEnabled()) {
            logger.debug("Sending HTTP request of type " + verb + " to " + url);
        }
        
        Resource resource = client.resource(url);
        resource = resource.contentType(contentType).accept(contentType);
        
        if (querystringParameters != null) {
            resource = resource.queryParams(querystringParameters);
        }
        
        if ( !org.apache.commons.lang.StringUtils.isBlank( token ) ) {
        	resource.header( "Authorization", "Bearer " + token );
        }
        
        ClientResponse response = null;
        if ("GET".equalsIgnoreCase(verb)) {
            response = resource.get();
        } else if ("POST".equalsIgnoreCase(verb)) {
            if (logger.isTraceEnabled()) {
                logger.trace("Request payload:" + org.apache.commons.lang.builder.ToStringBuilder.reflectionToString(input));
            }
            response = resource.post(input);
        } else if ("PUT".equalsIgnoreCase(verb)) {
            if (logger.isTraceEnabled()) {
                logger.trace("Request payload:" + org.apache.commons.lang.builder.ToStringBuilder.reflectionToString(input));
            }
            response = resource.put(input);
        } else if ("DELETE".equalsIgnoreCase(verb)) {
            response = resource.delete();
        } else {
            throw new IllegalArgumentException("Unsupported verb " + verb);
        }
        
        //NOTE: Handle connection issues here? Or rely on REST client default exceptions?
        
        return response;
    }
    

    /**
     * Handles an HTTP response, returning either the contained entity if everything went OK, or throws
     * a runtime exception which corresponds to the retrieved error code.
     * <ul>
     *  <li>
     *      If a 200 status code, then the payload is returned as a native entity.
     *  </li>
     *  <li>
     *      If a 201 status code, then null is returned.
     *  </li>
     *  <li>
     *      If a 400 status code and the content type is JSON, then a ValidationException is thrown which contains
     *      the returned validation messages.
     *  </li>
     *  <li>
     *      If a 500 status code, then the server-side exception type is checked and the appropriate exception
     *      from the <code>gr.icap.b2b.clients.exceptions</code> is thrown as a runtime exception. If there
     *      is no direct equivalent, a PortalServiceException is thrown.
     *  </li>
     *  <li>
     *      All other cases throw a ClientRuntimeException.
     *  </li>
     * </ul>
     * 
     * @param response HTTP response
     * @param cls entity type
     * @param <T> any object that is XML/JSON serializable (mainly from services-core package).
     * 
     * @return response payload as a native entity
     * 
     * @throws ServiceException if a 400 or 500 error was returned
     * 
     */
    protected <T> T handleResult( ClientResponse response, Class<T> cls ) throws ServiceException {
        if (response.getStatusCode() == HttpStatus.OK.getCode()) {
            
            if (cls != null) {
                return response.getEntity(cls);
            } else {
                return null; // no-content response
            }
            
        } 
        else if (response.getStatusCode() == HttpStatus.UNAUTHORIZED.getCode()) {

            throw handleServerErrorResponse(response);
            
        }
        else if (response.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR.getCode()) {

            throw handleServerErrorResponse(response);
            
        } 
        else if (response.getStatusCode() == HttpStatus.NOT_FOUND.getCode()) {

            throw handleServerErrorResponse(response);
            
        } 
        else if (response.getStatusCode() == HttpStatus.BAD_REQUEST.getCode() ) {
        	throw handleServerErrorResponse(response);
            
        } 
        else {
            // unhandled type, throw a ClientRuntimeException
            
            throw new ClientRuntimeException("Unhandled HTTP status code " + response.getStatusCode());
        }

    }
    
    /**
     * Translate a 500 response to a meaningful exception, if applicable.
     * 
     * @param response HTTP response
     * 
     * @return exception to be thrown by handleResult
     * 
     * @throws ClientRuntimeException if the response does not contain a PortalServiceFault
     */
    private RestServiceException handleServerErrorResponse(ClientResponse response) {

        try {
//        	Object errorResponse = response.getEntity(Object.class);
            
        	return new RestServiceException( response.getMessage(), JobFailureCode.SERVICE_PROBLEM );
            
        } catch(Exception e) {
            throw new ClientRuntimeException(e.getMessage(), e);
        }
        
    }

    /**
     * @return logger with subclass name
     */
    protected Logger getLogger() {
        
        return logger;
        
    }
    
}
