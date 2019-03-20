package com.tooooolazy.data.interfaces;

import com.tooooolazy.data.services.beans.OnlineBaseParams;
import com.tooooolazy.data.services.beans.OnlineBaseResult;

/**
 * What a WS must must provide in order to:
 * <ol>
 * <li>select data: 'execute' - does not use a 'transaction'</li>
 * <li>update/create data: 'executeUpdate' - uses 'transaction'</li>
 * </ol>
 * What method to call is defined in {@link OnlineBaseParams}
 * @author gpatoulas
 *
 * @param <OR>
 */
public interface DataHandlerClient<OR extends OnlineBaseResult, OP extends OnlineBaseParams> {

    public OR execute( OP params ) ;

    public OR executeUpdate( OP params ) ;

}
