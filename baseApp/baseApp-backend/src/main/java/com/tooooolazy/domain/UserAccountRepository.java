package com.tooooolazy.domain;

import java.util.Date;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;

import org.springframework.stereotype.Repository;

import com.tooooolazy.domain.objects.UserAccount;
import com.tooooolazy.domain.objects.UserRole;
import com.tooooolazy.util.Credentials;

@Repository("userAccountRepository")
public class UserAccountRepository extends AbstractRepository<UserAccount, Integer> {

	public static String USER_NAME = "userName";
	public static String PASSWORD = "password";

	public UserAccount findByUsername(String username) {
		Query q = entityManager.createQuery("select ua from UserAccount ua where username = :username ");
		q.setParameter("username", username);
        return (UserAccount) q.getSingleResult();
	}
	public UserAccount createUserAccount(Credentials creds, String userInsert) {
		UserAccount ua = new UserAccount();

		ua.setUserInsert( userInsert );
		ua.setUserUpdate( userInsert );
		ua.setTimeInsert( new Date() );
		ua.setTimeUpdate( new Date() );
		ua.setPassword( creds.getPassword() );
		ua.setUsername( creds.getUsername() );

		persist( ua );

		return ua;
	}
	public int addRole(UserRole ur, int uc, String insertUser) {
		Query q = entityManager.createNativeQuery("insert into " + getSchema() + ".ACCOUNTROLE(ROLECODE, USERCODE, USERINSERT, TIMEINSERT) VALUES (:roleCode, :userCode, :userName, current_timestamp)");
		q.setParameter("roleCode", ur.getRoleCode());
		q.setParameter("userCode", uc);
		q.setParameter("userName", insertUser);

		return q.executeUpdate();
	}
	public int removeRole(UserRole ur, int uc) {
		Query q = entityManager.createNativeQuery("delete from " + getSchema() + ".ACCOUNTROLE where USERCODE = :userCode and ROLECODE = :roleCode");
		q.setParameter("roleCode", ur.getRoleCode());
		q.setParameter("userCode", uc);

		return q.executeUpdate();
	}
}
