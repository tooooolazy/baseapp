package com.tooooolazy.domain;

import org.springframework.beans.factory.annotation.Autowired;

import com.tooooolazy.domain.components.PasswordManager;
import com.tooooolazy.ws.WsBaseDataHandler;


/**
 * This is where {@link WsBaseDataHandler} delegates everything (the base class anyway).
 * <p>Like {@link WsBaseDataHandler}, it should be overridden. Also '@Component' must be used on derived class. The derived class should be used to call {@link WsBaseDataHandler} constructor</p>
 * 
 * @author gpatoulas
 *
 */
//@Component
public abstract class DataBaseRepository extends AbstractJDBCRepository {
	public static String DEFAULT_USER_INSERT = "SYSTEM";

	@Autowired
	protected PasswordManager passwordManager;
}
