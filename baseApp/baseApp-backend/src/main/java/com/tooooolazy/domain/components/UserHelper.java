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
import com.tooooolazy.domain.UserRepository;

//@Component
public class UserHelper {

	@PersistenceContext
    protected EntityManager entityManager;

	@Autowired
	private UserRepository userRepository;

	public Object getUserRoles(String username) {
		JSONObject jo = getUserRolesJo(username);

		return jo.toString();
	}
	public JSONObject getUserRolesJo(String username) {
		JSONObject jo = new JSONObject();

		JSONArray ja = new JSONArray();
		jo.put(OnlineKeys.DATA, ja);
		Query q = userRepository.createUserRolesQuery(username!=null?username.toLowerCase():null);
		List<Object[]> res = q.getResultList();
		for (Object[] ur : res) {
			JSONObject _jo = new JSONObject();
			_jo.put(OnlineKeys.ID, ur[0].toString());
			if (username == null)
				_jo.put("username", ur[1].toString());
			ja.put(_jo);
		}

		return jo;
	}


	public Object getUsers() {
		JSONObject jo = getUsersJo();

		return jo.toString();
	}
	public JSONObject getUsersJo() {
		JSONObject jo = new JSONObject();

		JSONArray ja = new JSONArray();
		jo.put(OnlineKeys.DATA, ja);
		Query q = userRepository.createUsersQuery();
		List<Object[]> res = q.getResultList();
		for (Object[] ur : res) {
			JSONArray data = new JSONArray();
			for (Object o : ur) {
				data.put(o);
			}
			ja.put(data);
		}

		return jo;
	}

}
