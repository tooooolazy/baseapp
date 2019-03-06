package com.tooooolazy.services.client.exceptions;

import com.tooooolazy.data.services.beans.JobFailureCode;


/**
 * Base class for our exceptions. Holds the service fault information that was generated server-side.
 * 
 * @author Dimitris Batis
 *
 */
public abstract class ClientExceptionBase extends RuntimeException {
    
    private static final long serialVersionUID = 1L;
    
    
    private JobFailureCode serverFault;
    
    
    //<editor-fold desc="constructors">
    public ClientExceptionBase() {
        super();
    }

    public ClientExceptionBase(String msg, JobFailureCode cause) {
        super(msg);
        serverFault = cause;
    }

    public ClientExceptionBase(String msg) {
        super(msg);
    }

    public ClientExceptionBase(JobFailureCode cause) {
        super();
        serverFault = cause;
    }
    //</editor-fold>
    
    
    /**
     * @return server-side fault information
     */
    public JobFailureCode getServerFault() {
        return serverFault;
    }

}
