package com.tooooolazy.domain.components;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

import com.tooooolazy.data.interfaces.OnlineKeys;
import com.tooooolazy.data.services.beans.UserBean;
import com.tooooolazy.data.services.beans.UserRoleBean;
import com.tooooolazy.domain.UserAccountRepository;
import com.tooooolazy.domain.UserRepository;
import com.tooooolazy.domain.UserRoleRepository;
import com.tooooolazy.domain.enums.UserStatus;
import com.tooooolazy.domain.objects.User;
import com.tooooolazy.domain.objects.UserAccount;
import com.tooooolazy.domain.objects.UserRole;
import com.tooooolazy.util.Credentials;

@Component
public class UserHelper {

	public static String DEFAULT_USER_INSERT = "SYSTEM";

	@PersistenceContext
	protected EntityManager entityManager;

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserRoleRepository userRoleRepository;
	@Autowired
	private UserAccountRepository userAccountRepository;

	@Autowired
	protected PasswordManager passwordManager;

	public User createNewUser(UserAccount ua, Map<String, Object> params) {
//		if (ua != null) {
			User nu = new User();
			nu.setFirstName( (String)params.getOrDefault(UserRepository.FIRST_NAME, "") );
			nu.setLastName( (String)params.getOrDefault(UserRepository.LAST_NAME, "") );
			nu.setEmail( (String)params.get(UserRepository.EMAIL) );
			nu.setTelephone( (String)params.getOrDefault(UserRepository.TELEPHONE, "") );

			boolean externalLogin = (boolean) params.getOrDefault("externalLogin", false);
			Integer roleCode = (Integer)params.getOrDefault("role", null);
			UserRole ur = null;
			if (roleCode != null)
				ur = userRoleRepository.find( roleCode );

			// who is creating the user. If it is not set "SYSTEM" will be used. We might need to through an exception instead
			nu.setUserInsert( (String)params.get(UserRepository.USER_INSERT) );
			
			Credentials creds = new Credentials( 
					(String)params.get(UserAccountRepository.USER_NAME), 
					(String)params.get(UserAccountRepository.PASSWORD)); // should be null if external login!
			if ( !externalLogin && creds.getPassword() == null) {
				if ( nu.getEmail() != null ) {
					creds.setPassword( passwordManager.createRandomPassword() );
					
					// TODO send email to user with new random password!
					nu.setStatus(UserStatus.RESET); // user should then change it
				} else {
					throw new InvalidParameterException("NO PASSWORD");
				}
			} else
				nu.setStatus(UserStatus.ACTIVE);

			if ( !externalLogin ) {
				creds.setPassword( passwordManager.hashMC( creds.getPassword() ) );
			} else
				nu.setUserInsert( "AUTO" ); // auto created by system - 1st time login

			String userInsert = nu.getUserInsert() == null ? DEFAULT_USER_INSERT : nu.getUserInsert();
			
			Query q = entityManager.createQuery("from UserAccount where userName = :username");
			q.setParameter("username", creds.getUsername());
			
			UserAccount nua = null;
			try {
				nua = (UserAccount) q.getSingleResult();
			} catch (NoResultException e) {
//				e.printStackTrace();
			}
			if (nua == null) {
				nua = userAccountRepository.createUserAccount( creds, userInsert );
				nu.setUserCode( nua.getUserCode() );
				nu.setUserAccount( nua );
				nu = userRepository.createUser( nu, userInsert );
			} else {
				throw new DuplicateKeyException( creds.getUsername() );
			}

			if (ur != null) {
				userAccountRepository.addRole(ur, nua.getUserCode(), userInsert);
				userRepository.refresh( nu );
			}
			return nu;
//		}

//		throw new AccessDeniedException();
	}

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

	public int getUserCount() {
		return userAccountRepository.findAll().size();
	}

}
