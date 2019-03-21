package com.dpapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dpapp.ws.WsDataHandler;
import com.dpapp.ws.beans.JobFailureCode;
import com.dpapp.ws.beans.OnlineParams;
import com.dpapp.ws.beans.OnlineResult;

@RestController
@RequestMapping("/dcs")
public class DataControllerService {
	@Autowired
	private WsDataHandler wsDataHandler;
//	@Autowired
//	private DataRepository dataRepository;

	@RequestMapping(value = "/execute", method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8" })
	public OnlineResult execute(@RequestBody OnlineParams params) {
		return wsDataHandler.execute(params);
	}
	@RequestMapping(value = "/executeUpdate", method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8" })
	public OnlineResult executeUpdate(@RequestBody OnlineParams params) {
		return wsDataHandler.executeUpdate(params);
	}

	@RequestMapping(value = "/test/{name}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8" })
	public OnlineResult test(@PathVariable String name) throws Exception {
		OnlineParams params = new OnlineParams();
		params.setMethod("convertUser");
//		params.setUserCode(15);


		OnlineResult or = new OnlineResult();
		or.setFailCode(JobFailureCode.GENERIC);
		return or;
	}
}
