package com.tooooolazy.data.services.beans;

/**
 * Extends Base class in order to specify {@link JobFailureCode} enum class. Each App UI might need to declare their own 'OnlineResult' class and their own 'JobFailureCode' enum.
 * <p>Make sure services respond with the same object</p> 
 * @author gpatoulas
 *
 */
public class OnlineResult extends OnlineBaseResult<JobFailureCode> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
