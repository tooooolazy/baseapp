package com.tooooolazy.data.interfaces;

import com.tooooolazy.data.services.beans.OnlineParams;
import com.tooooolazy.data.services.beans.OnlineResult;

public interface DataHandlerClient<OR extends OnlineResult> {

    public OR execute( OnlineParams params ) ;

    public OR executeUpdate( OnlineParams params ) ;

}
