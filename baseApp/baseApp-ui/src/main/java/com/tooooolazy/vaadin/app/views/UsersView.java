package com.tooooolazy.vaadin.app.views;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tooooolazy.data.services.beans.OnlineBaseResult;
import com.tooooolazy.data.services.beans.RoleEnum;
import com.tooooolazy.data.services.beans.UserBean;

public class UsersView extends com.tooooolazy.vaadin.views.UsersView {

	@Override
	protected Enum[] getRoleEnumValues() {
		return RoleEnum.values();
	}

	@Override
	protected Integer getRoleEnumValue(Enum e) {
		return ((RoleEnum)e).getValue();
	}

	@Override
	protected UserBean[] convertToUserBean(ObjectMapper mapper, String ja)
			throws JsonParseException, JsonMappingException, IOException {
		return mapper.readValue(ja, UserBean[].class);
	}

	@Override
	protected Object getRoleByValue(int roleCode) {
		return RoleEnum.byValue( roleCode );
	}

	@Override
	protected OnlineBaseResult[] create_OR_array(int i) {
		return new OnlineBaseResult[i];
	}

}
