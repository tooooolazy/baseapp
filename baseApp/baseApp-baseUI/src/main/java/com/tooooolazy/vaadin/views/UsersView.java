package com.tooooolazy.vaadin.views;

import java.io.IOException;
import java.util.ArrayList;
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
import com.tooooolazy.util.Messages;
import com.tooooolazy.util.SearchCriteria;
import com.tooooolazy.vaadin.components.UsersTreeGrid;
import com.tooooolazy.vaadin.ui.BaseUI;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Component;

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
	protected Component createContent(BaseUI ui) {
		Messages.setLang( ui.getLocale().getLanguage() );
		// get WS data
		JSONObject usersJo = get_DataElement(0);
		JSONObject userRolesJo = get_DataElement(1);

		JSONArray usersJa = usersJo.optJSONArray(OnlineKeys.DATA);
		JSONArray userRolesJa = userRolesJo.optJSONArray(OnlineKeys.DATA);

		utg = new UsersTreeGrid();
		utg.setWidth("100%");
		if (usersJa == null || userRolesJa == null)
			return utg;
		
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
						}
					}
				}
				for ( UserRoleBean iurb : gurb.getUserRoles() ) {
					iurb.setUsername( null ); // lets hide username --> it's the same as the parent!
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// now use beans to generate and fill tree grid!
		utg.setItems( forGrid, UserRoleBean::getUserRoles );

		return utg;
	}

	protected List<UserRoleBean> getAllRoles(String username) {
		List<UserRoleBean> urbs = new ArrayList<UserRoleBean>();
		
		for (Enum e : getRoleEnumValues() ) {
			int rev = getRoleEnumValue( e );
			if (rev == 0)
				continue;
			UserRoleBean urb = new UserRoleBean( getRoleEnumValue( e ), username );
			urb.setAssigned( false );
			urb.setUserRoles(new ArrayList<UserRoleBean>() );
			urbs.add( urb );
			
		}
		return urbs;
	}

	protected abstract Enum[] getRoleEnumValues();
	protected abstract Integer getRoleEnumValue(Enum e);

	protected abstract UB[] convertToUserBean(ObjectMapper mapper, String ja) throws JsonParseException, JsonMappingException, IOException;
	protected abstract Object getRoleByValue(int roleCode);

	@Override
	protected OR[] getWSContents() {
		OR[] ors = create_OR_array(2);
		try {
			OR or = (OR) ServiceLocator.getServices().getDataHandler().getData(WsMethods.USERS, null, null);
			ors[0] = or;
// {DATA=[{credentials={username=TEST_USER1, password=null, rememberMe=null, fromIp=null}, userCode=1, roles=[], firstName=Test_Name1, lastName=Test_LastName1, demo=false, admin=false, god=false}, {credentials={username=TEST_USER2, password=null, rememberMe=null, fromIp=null}, userCode=2, roles=[], firstName=Test_Name2, lastName=Test_LastName2, demo=false, admin=false, god=false}, {credentials={username=TEST_USER3, password=null, rememberMe=null, fromIp=null}, userCode=3, roles=[], firstName=Test_Name3, lastName=Test_LastName3, demo=false, admin=false, god=false}, {credentials={username=TEST_USER5, password=null, rememberMe=null, fromIp=null}, userCode=5, roles=[], firstName=Test_Name5, lastName=Test_LastName5, demo=false, admin=false, god=false}, {credentials={username=TEST_USER6, password=null, rememberMe=null, fromIp=null}, userCode=6, roles=[], firstName=Test_Name6, lastName=Test_LastName6, demo=false, admin=false, god=false}, {credentials={username=axera, password=null, rememberMe=null, fromIp=null}, userCode=7, roles=[], firstName=Αθανασία, lastName=Ξέρα, demo=false, admin=false, god=false}, {credentials={username=mesoglou, password=null, rememberMe=null, fromIp=null}, userCode=8, roles=[], firstName=Μελίνα, lastName=Εσόγλου, demo=false, admin=false, god=false}, {credentials={username=mdimitriou, password=null, rememberMe=null, fromIp=null}, userCode=9, roles=[], firstName=Μαρία, lastName=Δημητρίου, demo=false, admin=false, god=false}, {credentials={username=mflotsiou, password=null, rememberMe=null, fromIp=null}, userCode=10, roles=[], firstName=Μαρία, lastName=Φλώτσιου, demo=false, admin=false, god=false}, {credentials={username=tkipiotis, password=null, rememberMe=null, fromIp=null}, userCode=11, roles=[], firstName=Θανάσης, lastName=Κηπιώτης, demo=false, admin=false, god=false}, {credentials={username=emitsi, password=null, rememberMe=null, fromIp=null}, userCode=12, roles=[], firstName=Έφη, lastName=Μήτση, demo=false, admin=false, god=false}, {credentials={username=pkounougeri, password=null, rememberMe=null, fromIp=null}, userCode=63, roles=[], firstName=Παναγιώτα, lastName=Κουνουγέρη, demo=false, admin=false, god=false}, {credentials={username=mchnari, password=null, rememberMe=null, fromIp=null}, userCode=64, roles=[], firstName=Μαρία, lastName=Χνάρη, demo=false, admin=false, god=false}, {credentials={username=gpatoulas, password=null, rememberMe=null, fromIp=null}, userCode=66, roles=[], firstName=g, lastName=p, demo=false, admin=false, god=false}, {credentials={username=credscoring, password=null, rememberMe=null, fromIp=null}, userCode=69, roles=[], firstName=L, lastName=K, demo=false, admin=false, god=false}, {credentials={username=teponline01, password=null, rememberMe=null, fromIp=null}, userCode=71, roles=[], firstName=TEP, lastName=ONLINE1, demo=false, admin=false, god=false}, {credentials={username=EKOUZINAKI, password=null, rememberMe=null, fromIp=null}, userCode=72, roles=[], firstName=ΕΙΡΗΝΗ, lastName=ΚΟΥΖΙΝΑΚΗ, demo=false, admin=false, god=false}, {credentials={username=teponline02, password=null, rememberMe=null, fromIp=null}, userCode=73, roles=[], firstName=TEP, lastName=ONLINE2, demo=false, admin=false, god=false}, {credentials={username=giliopoulos, password=null, rememberMe=null, fromIp=null}, userCode=79, roles=[], firstName=G, lastName=I, demo=false, admin=false, god=false}, {credentials={username=akoleva, password=null, rememberMe=null, fromIp=null}, userCode=80, roles=[], firstName=A, lastName=K, demo=false, admin=false, god=false}, {credentials={username=ICP1939402, password=null, rememberMe=null, fromIp=null}, userCode=81, roles=[], firstName=ΧΡΙΣΤΙΝΑ, lastName=ΠΑΝΑΓΟΔΗΜΟΥ, demo=false, admin=false, god=false}, {credentials={username=ktzonis, password=null, rememberMe=null, fromIp=null}, userCode=82, roles=[], firstName=K, lastName=Tzonis, demo=false, admin=false, god=false}, {credentials={username=test, password=null, rememberMe=null, fromIp=null}, userCode=83, roles=[], firstName=Melina, lastName=Esoglou, demo=false, admin=false, god=false}, {credentials={username=Mela, password=null, rememberMe=null, fromIp=null}, userCode=84, roles=[], firstName=Melina, lastName=Esoglou, demo=false, admin=false, god=false}]}
			or = (OR) ServiceLocator.getServices().getDataHandler().getData(WsMethods.USERS_ROLES, null, null);
			ors[1] = or;
// {DATA=[{roleCode=1, assigned=null, username=axera, firstName=null, lastName=null, userRoles=null}, {roleCode=2, assigned=null, username=axera, firstName=null, lastName=null, userRoles=null}, {roleCode=3, assigned=null, username=axera, firstName=null, lastName=null, userRoles=null}, {roleCode=6, assigned=null, username=axera, firstName=null, lastName=null, userRoles=null}, {roleCode=7, assigned=null, username=axera, firstName=null, lastName=null, userRoles=null}, {roleCode=16, assigned=null, username=axera, firstName=null, lastName=null, userRoles=null}, {roleCode=1, assigned=null, username=mesoglou, firstName=null, lastName=null, userRoles=null}, {roleCode=2, assigned=null, username=mesoglou, firstName=null, lastName=null, userRoles=null}, {roleCode=3, assigned=null, username=mesoglou, firstName=null, lastName=null, userRoles=null}, {roleCode=6, assigned=null, username=mesoglou, firstName=null, lastName=null, userRoles=null}, {roleCode=7, assigned=null, username=mesoglou, firstName=null, lastName=null, userRoles=null}, {roleCode=11, assigned=null, username=mesoglou, firstName=null, lastName=null, userRoles=null}, {roleCode=12, assigned=null, username=mesoglou, firstName=null, lastName=null, userRoles=null}, {roleCode=13, assigned=null, username=mesoglou, firstName=null, lastName=null, userRoles=null}, {roleCode=15, assigned=null, username=mesoglou, firstName=null, lastName=null, userRoles=null}, {roleCode=16, assigned=null, username=mesoglou, firstName=null, lastName=null, userRoles=null}, {roleCode=2, assigned=null, username=tkipiotis, firstName=null, lastName=null, userRoles=null}, {roleCode=3, assigned=null, username=tkipiotis, firstName=null, lastName=null, userRoles=null}, {roleCode=7, assigned=null, username=tkipiotis, firstName=null, lastName=null, userRoles=null}, {roleCode=8, assigned=null, username=tkipiotis, firstName=null, lastName=null, userRoles=null}, {roleCode=2, assigned=null, username=pkounougeri, firstName=null, lastName=null, userRoles=null}, {roleCode=3, assigned=null, username=pkounougeri, firstName=null, lastName=null, userRoles=null}, {roleCode=6, assigned=null, username=pkounougeri, firstName=null, lastName=null, userRoles=null}, {roleCode=7, assigned=null, username=pkounougeri, firstName=null, lastName=null, userRoles=null}, {roleCode=8, assigned=null, username=pkounougeri, firstName=null, lastName=null, userRoles=null}, {roleCode=2, assigned=null, username=mchnari, firstName=null, lastName=null, userRoles=null}, {roleCode=1, assigned=null, username=gpatoulas, firstName=null, lastName=null, userRoles=null}, {roleCode=2, assigned=null, username=gpatoulas, firstName=null, lastName=null, userRoles=null}, {roleCode=3, assigned=null, username=gpatoulas, firstName=null, lastName=null, userRoles=null}, {roleCode=6, assigned=null, username=gpatoulas, firstName=null, lastName=null, userRoles=null}, {roleCode=7, assigned=null, username=gpatoulas, firstName=null, lastName=null, userRoles=null}, {roleCode=8, assigned=null, username=gpatoulas, firstName=null, lastName=null, userRoles=null}, {roleCode=10, assigned=null, username=gpatoulas, firstName=null, lastName=null, userRoles=null}, {roleCode=16, assigned=null, username=gpatoulas, firstName=null, lastName=null, userRoles=null}, {roleCode=2, assigned=null, username=credscoring, firstName=null, lastName=null, userRoles=null}, {roleCode=3, assigned=null, username=credscoring, firstName=null, lastName=null, userRoles=null}, {roleCode=6, assigned=null, username=credscoring, firstName=null, lastName=null, userRoles=null}, {roleCode=7, assigned=null, username=credscoring, firstName=null, lastName=null, userRoles=null}, {roleCode=12, assigned=null, username=teponline01, firstName=null, lastName=null, userRoles=null}, {roleCode=16, assigned=null, username=teponline01, firstName=null, lastName=null, userRoles=null}, {roleCode=9, assigned=null, username=EKOUZINAKI, firstName=null, lastName=null, userRoles=null}, {roleCode=15, assigned=null, username=EKOUZINAKI, firstName=null, lastName=null, userRoles=null}, {roleCode=16, assigned=null, username=teponline02, firstName=null, lastName=null, userRoles=null}, {roleCode=3, assigned=null, username=giliopoulos, firstName=null, lastName=null, userRoles=null}, {roleCode=6, assigned=null, username=giliopoulos, firstName=null, lastName=null, userRoles=null}, {roleCode=7, assigned=null, username=giliopoulos, firstName=null, lastName=null, userRoles=null}, {roleCode=8, assigned=null, username=giliopoulos, firstName=null, lastName=null, userRoles=null}, {roleCode=4, assigned=null, username=akoleva, firstName=null, lastName=null, userRoles=null}, {roleCode=2, assigned=null, username=ktzonis, firstName=null, lastName=null, userRoles=null}, {roleCode=3, assigned=null, username=ktzonis, firstName=null, lastName=null, userRoles=null}, {roleCode=6, assigned=null, username=ktzonis, firstName=null, lastName=null, userRoles=null}, {roleCode=7, assigned=null, username=ktzonis, firstName=null, lastName=null, userRoles=null}, {roleCode=9, assigned=null, username=ktzonis, firstName=null, lastName=null, userRoles=null}, {roleCode=13, assigned=null, username=ktzonis, firstName=null, lastName=null, userRoles=null}]}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ors;
	}

}
