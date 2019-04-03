package com.tooooolazy.domain;

import java.util.Date;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.tooooolazy.domain.objects.AppLock;
import com.tooooolazy.domain.objects.AppLockId;
import com.tooooolazy.domain.objects.UserAccount;

@Repository("appLockRepository")
public class AppLockRepository extends AbstractRepository<AppLock, AppLockId>{
	
	public AppLock find(int type, Integer itemId) {
		AppLockId id = new AppLockId(type, itemId);
		AppLock il = find( id );
		
		return il;
	}

	public AppLock create(String fromIp, UserAccount ua, int lockType, int itemId) {
		AppLock il = new AppLock();
		AppLockId id = new AppLockId(lockType, itemId);

		il.setId( id );
		il.setLockedBy( ua );
		il.setTimeInsert( new Date() );

		persist( il );
		
		return il;
	}

	public void removeAll() {
		Query q = entityManager.createQuery("delete from AppLock");
		q.executeUpdate();
	}

	public void removeLocksOf(UserAccount ua) {
		Query q = entityManager.createQuery("delete from AppLock where lockedBy = :ua");
		q.setParameter("ua", ua);
		q.executeUpdate();
	}

	public Object count() {
		Query q = entityManager.createQuery("select count(*) from AppLock");
		Object res = q.getSingleResult();
		return res;
	}
}
