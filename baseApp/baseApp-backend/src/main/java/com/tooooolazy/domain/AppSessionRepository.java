package com.tooooolazy.domain;

import java.util.Date;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.tooooolazy.domain.objects.AppSession;

@Repository("appSessionRepository")
public class AppSessionRepository extends AbstractRepository<AppSession, Integer>{

	public AppSession create(String fromIp, Integer uc) {
		AppSession is = new AppSession();

		is.setUserCode( uc );
		is.setIpAddress( fromIp );
		is.setTimeInsert( new Date() );

		persist( is );
		
		return is;
	}

	public void removeAll() {
		Query q = entityManager.createQuery("delete from AppSession");
		q.executeUpdate();
	}

	public Object count() {
		Query q = entityManager.createQuery("select count(*) from AppSession ");
		Object res = q.getSingleResult();
		return res;
	}
}
