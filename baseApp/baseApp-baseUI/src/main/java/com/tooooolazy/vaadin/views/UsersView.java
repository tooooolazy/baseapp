package com.tooooolazy.vaadin.views;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tooooolazy.data.ServiceLocator;
import com.tooooolazy.data.interfaces.OnlineKeys;
import com.tooooolazy.data.interfaces.WsMethods;
import com.tooooolazy.data.services.beans.OnlineBaseResult;
import com.tooooolazy.data.services.beans.UserBean;
import com.tooooolazy.data.services.beans.UserRoleBean;
import com.tooooolazy.util.SearchCriteria;
import com.tooooolazy.vaadin.components.UsersTreeGrid;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;

public abstract class UsersView<C extends SearchCriteria, E, UB extends UserBean, OR extends OnlineBaseResult, JFC> extends BaseView<C, E, UB, OR, JFC> {

	protected UsersTreeGrid utg;

	@Override
	protected void addJavascriptFunctions() {
		// TODO Auto-generated method stub

	}

	@Override
	protected Class getCriteriaClass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addSearchCriteria(AbstractComponent ac) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected Component createContent() {
		// get WS data
		JSONObject usersJo = get_DataElement(0);
		JSONObject userRolesJo = get_DataElement(1);

		JSONArray usersJa = usersJo.optJSONArray(OnlineKeys.DATA);
		JSONArray userRolesJa = userRolesJo.optJSONArray(OnlineKeys.DATA);

		// convert them to related beans
		List<UserRoleBean> forGrid = new ArrayList<UserRoleBean>();
		try {
			ObjectMapper mapper = new ObjectMapper();


			UB[] ubs = convertToUserBean(mapper, usersJa.toString());
			for ( UB ub : ubs ) {
				UserRoleBean urb = new UserRoleBean(0, ub.getCredentials().getUsername());
				urb.setUserRoles( getAllRoles( ub.getCredentials().getUsername() ) );
				urb.setFirstName( ub.getFirstName() );
				urb.setLastName( ub.getLastName() );
				forGrid.add( urb );
			}


			UserRoleBean[] urbs = mapper.readValue(userRolesJa.toString(), UserRoleBean[].class );
			for ( UserRoleBean gurb : forGrid ) {
				for ( UserRoleBean urb : urbs ) {
					if ( gurb.getUsername().equals( urb.getUsername() ) ) {
						for ( UserRoleBean iurb : gurb.getUserRoles() ) {
							if ( iurb.getRoleCode().equals( urb.getRoleCode()) )
								iurb.setAssigned( true );
								iurb.setUsername( null ); // lets hide username --> it's the same as the parent!
						}
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// now use beans to generate and fill tree grid!
		utg = new UsersTreeGrid();
		utg.setWidth("100%");
		utg.setItems( forGrid, UserRoleBean::getUserRoles );
//		utg.setItems( forGrid );

		return utg;
	}

	protected List<UserRoleBean> getAllRoles(String username) {
		return new ArrayList<UserRoleBean>();
	}

	protected abstract UB[] convertToUserBean(ObjectMapper mapper, String ja) throws JsonParseException, JsonMappingException, IOException;
	protected abstract Object getRoleByValue(int roleCode);

	@Override
	protected OR[] getWSContents() {
		OR[] ors = create_OR_array(2);
		try {
			OR or = (OR) ServiceLocator.getServices().getDataHandler().getData(WsMethods.USERS, null, null);
			ors[0] = or;

			or = (OR) ServiceLocator.getServices().getDataHandler().getData(WsMethods.USERS_ROLES, null, null);
			ors[1] = or;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ors;
	}

}
