package com.tooooolazy.data.interfaces;

import com.tooooolazy.data.services.beans.OnlineParams;
import com.tooooolazy.data.services.beans.OnlineBaseResult;

/**
 * What a WS must must provide in order to:
 * <ol>
 * <li>select data: 'execute' - does not use a 'transaction'</li>
 * <li>update/create data: 'executeUpdate' - uses 'transaction'</li>
 * </ol>
 * What method to call is defined in {@link OnlineParams}
 * @author gpatoulas
 *
 * @param <OR>
 */
public interface DataHandlerClient<OR extends OnlineBaseResult> {

    public OR execute( OnlineParams params ) ;

    public OR executeUpdate( OnlineParams params ) ;

}
