package com.dpapp.ws.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tooooolazy.data.services.beans.OnlineBaseResult;

@JsonIgnoreProperties({"asJSON"})
public class OnlineResult extends OnlineBaseResult<JobFailureCode> {

}
