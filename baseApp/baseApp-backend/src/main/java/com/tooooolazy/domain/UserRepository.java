package com.tooooolazy.domain;

import java.util.Date;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.tooooolazy.domain.objects.User;

@Repository("userRepository")
public class UserRepository extends AbstractRepository<User, Integer> {

	public static String USER_INSERT = "userInsert";
	public static String FIRST_NAME = "firstName";
	public static String LAST_NAME = "lastName";
	public static String EMAIL = "email";
	public static String TELEPHONE = "telephone";

	public User createUser(User u, String userInsert) {
		u.setUserInsert( userInsert );
		u.setUserUpdate( userInsert );
		u.setTimeInsert( new Date() );
		u.setTimeUpdate( new Date() );

		persist( u );

		return u;
	}

	public Query createUserRolesQuery(String username) {
		return createUserRolesQuery(username, null);
	}
	public Query createUserRolesQuery(String username, Integer roleCode) {
		StringBuffer sb = new StringBuffer();
		sb.append("select ar.ROLECODE, ua.USERNAME from " + getSchema() + ".ACCOUNTROLE ar ");
		sb.append("inner join " + getSchema() + ".USERACCOUNT ua on ua.USERCODE=ar.USERCODE ");
		sb.append("where 1=1 ");
		if (username != null)
			sb.append("and lower(ua.username) = lower(:username) ");
		if (roleCode != null)
			sb.append("and ROLECODE = :roleCode ");

		Query q = entityManager.createNativeQuery(sb.toString());
		if (username != null)
			q.setParameter("username", username);
		if (roleCode != null)
			q.setParameter("roleCode", roleCode);

		return q;
	}




	public Query createUsersQuery() {
		StringBuffer sb = new StringBuffer();
		sb.append("select u.USERCODE, USERNAME, LASTNAME, FIRSTNAME, u.EMAIL from " + getSchema() + ".USERS u ");
		sb.append("inner join " + getSchema() + ".USERACCOUNT ua on u.USERCODE=ua.USERCODE ");

		Query q = entityManager.createNativeQuery(sb.toString());

		return q;
	}

}
