package com.tooooolazy.domain.components;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tooooolazy.data.interfaces.OnlineKeys;
import com.tooooolazy.data.services.beans.UserBean;
import com.tooooolazy.data.services.beans.UserRoleBean;
import com.tooooolazy.domain.UserAccountRepository;
import com.tooooolazy.domain.UserRepository;
import com.tooooolazy.domain.UserRoleRepository;
import com.tooooolazy.domain.objects.UserAccount;
import com.tooooolazy.domain.objects.UserRole;
import com.tooooolazy.util.Credentials;

@Component
public class UserHelper {

//	@PersistenceContext
//    protected EntityManager entityManager;

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserRoleRepository userRoleRepository;
	@Autowired
	private UserAccountRepository userAccountRepository;

	public Object getUserRoles(String username) {
		JSONObject jo = getUserRolesJo(username);

		return jo.toMap();
	}
	public JSONObject getUserRolesJo(String username) {
		JSONObject jo = new JSONObject();

		JSONArray ja = new JSONArray();
		jo.put(OnlineKeys.DATA, ja);
		Query q = userRepository.createUserRolesQuery(username!=null?username.toLowerCase():null);
		List<Object[]> res = q.getResultList();
		for (Object[] ur : res) {
			UserRoleBean urb = new UserRoleBean( (Integer)ur[0], (String)ur[1] );
			ja.put( urb );
		}

		return jo;
	}


	public Object getUsers() {
		JSONObject jo = getUsersJo();

		return jo.toMap();
	}
	public JSONObject getUsersJo() {
		JSONObject jo = new JSONObject();

		JSONArray ja = new JSONArray();
		jo.put(OnlineKeys.DATA, ja);
		Query q = userRepository.createUsersQuery();
		List<Object[]> res = q.getResultList();
		for (Object[] ur : res) {
			UserBean ub = new UserBean() {

				@Override
				protected boolean isGodRole(Object re) {
					return false;
				}

				@Override
				protected boolean isAdminRole(Object re) {
					return false;
				}
				
			};
			ub.setUserCode( (Integer) ur[0] );
			ub.setCredentials( new Credentials( (String)ur[1], null) );
			ub.setLastName((String)ur[2]);
			ub.setFirstName((String)ur[3]);

			ja.put( ub );
		}

		return jo;
	}
	public int updateUserRole(int userCode, int roleCode, boolean assigned, UserAccount ua) {
		// TODO Auto-generated method stub
		UserRole ur = userRoleRepository.find( roleCode );
		if ( assigned )
			return userAccountRepository.addRole(ur, userCode, ua.getUsername());
		else
			return userAccountRepository.removeRole(ur, userCode);
	}

}
