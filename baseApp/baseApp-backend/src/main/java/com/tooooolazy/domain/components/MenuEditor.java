package com.tooooolazy.domain.components;


import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.tooooolazy.data.interfaces.OnlineKeys;
import com.tooooolazy.domain.objects.ApplicationParameter;
import com.tooooolazy.domain.objects.UserAccount;

/**
 * Helper to manipulate menu structures!
 * @author gpatoulas
 *
 */
@Component
public class MenuEditor {
//	@Autowired
//	private JobHandler jobHandler;
    @PersistenceContext
    protected EntityManager entityManager;
	@Autowired
    protected Environment env;
    @Autowired
	protected DataHandlerHelper dhh;


//	public Object lockMenu(UserAccount ua, Map params) {
//		int category = (Integer)params.get("category");
//		boolean lock = (Boolean)params.get("lock");
//		String username = ua.getUsername();
//		int uc = ua.getUserCode();
//
//		JSONObject jo = new JSONObject();
//		if (lock) {
//			if (jobHandler.isJobRunning(MenuEditor.class)) {
//				jo.put(OnlineKeys.RESULT, -1); // already locked
//			} else {
//				if (uc > 0) {
//					jobHandler.jobStarted(MenuEditor.class, new DefJobParams(uc));
//					jo.put(OnlineKeys.RESULT, 0); // now locked
//				} else {
//					jo.put(OnlineKeys.RESULT, -2); // no user found
//				}
//			}
//		} else { // should unlock
//			if (uc > 0) {
//				jobHandler.jobEnded(MenuEditor.class, new DefJobParams(uc));
//				jo.put(OnlineKeys.RESULT, 0);
//			} else {
//				jo.put(OnlineKeys.RESULT, -2); // no user found
//			}
//		}
//		return jo.toString();
//	}

//	public Object moveMenuItem(UserAccount ua, Map params) {
//		String username = ua.getUsername();
//		int uc = ua.getUserCode();
//	
//		String source = (String)params.get("source");
//		String target = (String)params.get("target");
//
//		String[] s = source.split("_");
//		String[] t = target.split("_");
//
//		int catFrom = Integer.parseInt(s[0]);
//		int catTo = Integer.parseInt(t[0]);
//
//		int pIdFrom = Integer.parseInt(s[1]);
//		int pIdTo = Integer.parseInt(t[1]);
//
//		int cIdFrom = Integer.parseInt(s[2]);
//		int cIdTo = Integer.parseInt(t[2]);
//
//		int orderFrom = Integer.parseInt(s[3]);
//		int orderTo = Integer.parseInt(t[3]);
//
//		int totalFrom = Integer.parseInt(s[4]);
//		int totalTo = Integer.parseInt(t[4]);
//
//		String hdl = t[5];
//		String vdl = t[6];
//
//		int result = 0;
//		int jobId = jobHandler.getJobId(MenuEditor.class);
//		if (jobId == uc)
//			moveMenuItem(catFrom, pIdFrom, cIdFrom, orderFrom, totalFrom, 
//						 catTo,	  pIdTo,   cIdTo,   orderTo,   totalTo,
//						 hdl, vdl, username);
//		else {
//			LogManager.getLogger().info("Locked by another User");
//			result = jobId>0?jobId:-1;
//		}
//
//		JSONObject jo = getMenuStructureJSON(catFrom);
//		jo.put(OnlineKeys.RESULT, result);
//
//		return jo.toString();
//	}
	/**
	 * Moves a given menu item (**From) to a new location (**To), even in a totally different menu!!
	 * @param catFrom
	 * @param pIdFrom
	 * @param cIdFrom
	 * @param orderFrom
	 * @param totalFrom
	 * @param catTo
	 * @param pIdTo
	 * @param cIdTo
	 * @param orderTo
	 * @param totalTo
	 * @param hdl - LEFT, RIGHT: do nothing, CENTER: do something
	 * @param vdl - TOP: move before target, BOTTOM: move below target, MIDDLE: move inside target (submenu)
	 */
	private void moveMenuItem(int catFrom, int pIdFrom, int cIdFrom, int orderFrom, int totalFrom, 
							  int catTo,   int pIdTo,   int cIdTo,   int orderTo,   int totalTo,
							  String hdl, String vdl, String username) {

		// general idea
		// 1. delete source
		// 2. shift children of source's parent to close the gap (if any)
		// 3. shift children of target's parent to create gap for source
		// 4. recreate and move the item to empty space
		if (catFrom == catTo) { // fuck movement from menu to menu for now! - we only have one dynamic menu (main TEP menu) so far anyway!
			// 1. delete source
			// Two possibilities:
			// - simple item
			// - item with sub items. This one also has 2 possibilities:
			//		a. Main menu header
			//		b. inner menu header
			// In both cases a single delete is enough - 'remember' parameterCode so that we can reuse it!!
			// For the second case, after step 4, and depending on target and in case of case (b.), we need to either modify the header item OR create a new one!
			int pc = deleteItem(catFrom, pIdFrom, cIdFrom, orderFrom);

			// 2. shift children of source's parent to close the gap (if any)
			// Again we have to possibilities:
			// - source is an inner item (pIdFrom != cIdFrom) --> shift children
			// - source is a top item (pIdFrom == cIdFrom) --> shift parents
			int shifted = 0;
			if (pIdFrom != cIdFrom)
				shifted = shiftChildren(catFrom, pIdFrom, orderFrom, true);
			else
				shifted = shiftParents(catFrom, orderFrom, true);

//			entityManager.flush();

			// after shifting we also need to check whether source's parent has any items left.
			// if there is NOTHING LEFT and parent is also an INNER menu then we also need to delete the header item!!!
			
			// 3. shift children of target's parent to create gap for source
			// This depends on drop location!! AND menu category (basically whether menu is horizontal or vertical)
			int[] res = createMenuGap(catTo, pIdTo, cIdTo, orderTo, hdl, vdl, pc);

			// 4. recreate and move the item to empty space (or as last in section)
			if (res[1] > totalTo 
					&& pIdTo == pIdFrom  // Fix to avoid gap when moving as last 
					&& pIdTo != cIdTo) // Fix to avoid setting order=max(order) - instead of max(order)+1 - when moving an inner item as last main section
				res[1] = totalTo;
			createItem(pc, catTo, res[2]>0?res[2]:cIdFrom, cIdFrom, res[1] > 0? res[1]:null, username);
			boolean headerExists = itemExists(catTo, res[0], res[0], 0);
			if (res[0] > 0) {
				if (!headerExists) // FIX v3.10.6c: as long as header DOES NOT exist!
					createItem(0, catTo, res[0], res[0], 0, username);
			}
			if (totalFrom == 1 && res[0] != pIdFrom) // if there is NOTHING LEFT and parent is also an INNER menu then we also need to delete the header item!!! (FIX v3.10.6c: as long we are moving item into a new parent!!)
				deleteItem(catFrom, pIdFrom, pIdFrom, 0);
		}
	}

	private boolean itemExists(int cat, int vFrom, int vTo, int order) {
		Query q = entityManager.createNativeQuery("select PARAMETERCODE from " + dhh.getSchema() + ".APPLICATIONPARAMETER where category= :catFrom and valueFrom= :pIdFrom and valueTo= :cIdFrom and p_order= :orderFrom");
		q.setParameter("catFrom", cat);
		q.setParameter("pIdFrom",vFrom);
		q.setParameter("cIdFrom", vTo);
		q.setParameter("orderFrom", order);

		try {
			int pc = (Integer) q.getSingleResult();
		} catch (Exception e) {
			return false;
		}

		return true;
	}

	private void createItem(int pc, int catTo, int cIdTo, int cIdFrom, Integer ord, String username) {
		Query q = null;
		String ordSql = ":ord";
		if (ord == null) {
			ordSql = "(select coalesce(max(p_order),0)+1 from " + dhh.getSchema() + ".APPLICATIONPARAMETER ap where mappingtype=24 and category= :catTo and VALUEFROM<>VALUETO and VALUEFROM = :cIdTo)";
		}
		if (pc == 0)
			q = entityManager.createNativeQuery("insert into " + dhh.getSchema() + ".APPLICATIONPARAMETER Values ( (select max(parametercode)+1 from " + dhh.getSchema() + ".APPLICATIONPARAMETER), 24, '', 1, :catTo, 0, null, :cIdTo, :cIdFrom, " + ordSql + ",:username,:timeInsert,:username,:timeInsert )");
		else
			q = entityManager.createNativeQuery("insert into " + dhh.getSchema() + ".APPLICATIONPARAMETER Values ( :pc, 24, '', 1, :catTo, 0, null, :cIdTo, :cIdFrom, " + ordSql + ", :username, :timeInsert ,:username,:timeInsert )");

		if (pc > 0)
			q.setParameter("pc", pc);
		q.setParameter("catTo", catTo);
		q.setParameter("cIdTo", cIdTo);
		q.setParameter("cIdFrom", cIdFrom);
		q.setParameter("username", username);
		q.setParameter("timeInsert", new Date());
		if (ord != null)
			q.setParameter("ord", ord);
		q.executeUpdate();
	}

	/**
	 * Creates a gap for a new menu item by shifting existing ones downwards (order = order+1). Also dictates if a new header item should be created.
	 * @param catTo
	 * @param pIdTo
	 * @param cIdTo
	 * @param orderTo
	 * @param hdl
	 * @param vdl
	 * @param pc should NOT use this parameter code if we need to create a section. It is reserved for item recreation.
	 * @return { 0/x: no header/header's vfrom , 0/y: as last/specific order , pIdTo/cIdTo: who is the end parent }
	 */
	private int[] createMenuGap(int catTo, int pIdTo, int cIdTo, int orderTo, String hdl, String vdl, int pc) {
		int[] res = new int[] {0, 0, pIdTo};

		if (catTo == 1) {
			if (vdl.equals("MIDDLE")) { // gap in cIdTo section --> might need to convert to a section as well
				res[2] = cIdTo;
				// don't really need to create gap since it will be added as last!!!
				// BUT we still might need to create a section item!!
				if (pIdTo != cIdTo) {
					boolean isEmpty = hasNoChildren(catTo, cIdTo);
					if (isEmpty) {
						 // need to create section catTo,cIdTo,cIdTo,0
						res[0] = cIdTo;
					} else {
						 // no need to create section
					}
				} else {
					// no need to create section
				}
			} else { // gap in pIdTo section UNLESS pIdTo=cIdTo in which case gap in main sections starting from orderTo OR (orderTo + 1) depending on vdl
				boolean bottom = (vdl.equals("BOTTOM"));
				res[1] = orderTo + (bottom?1:0);

				if (pIdTo == cIdTo) {
					shiftParents(catTo, orderTo + (bottom?1:0), false);
					res[2] = 0;
				} else
					shiftChildren(catTo, pIdTo, orderTo + (bottom?1:0), false);
			}
		}
		return res;
	}

	/**
	 * Deletes a menuItem and returns the parameter code it used (so it can be reused) 
	 * @param catFrom
	 * @param pIdFrom
	 * @param cIdFrom
	 * @param orderFrom
	 * @return
	 */
	private int deleteItem(int catFrom, int pIdFrom, int cIdFrom, int orderFrom) {
		Query q = entityManager.createNativeQuery("select PARAMETERCODE from " + dhh.getSchema() + ".APPLICATIONPARAMETER where category= :catFrom and valueFrom= :pIdFrom and valueTo= :cIdFrom and p_order= :orderFrom");
		q.setParameter("catFrom", catFrom);
		q.setParameter("pIdFrom", pIdFrom);
		q.setParameter("cIdFrom", cIdFrom);
		q.setParameter("orderFrom", orderFrom);
		try {
			int pc = (Integer) q.getSingleResult();

			q = entityManager.createNativeQuery("delete from " + dhh.getSchema() + ".APPLICATIONPARAMETER where PARAMETERCODE= :pc");
			q.setParameter("pc", pc);
			if (q.executeUpdate() > 0)
				return pc;
			else
				return 0;
		} catch (Exception e) {
			return 0;
		}
	}
	/**
	 * Modifies TOP LEVEL header elements' order (-1)
	 * @param category
	 * @param order
	 */
	private int shiftParents(int category, int order, boolean up) {
		Query q;
		q = entityManager.createNativeQuery("update " + dhh.getSchema() + ".APPLICATIONPARAMETER set p_order = p_order " + (up?"-":"+") + " 1 where mappingtype=24 and category= :category and VALUEFROM=VALUETO and p_order>= :order");
		q.setParameter("order", order);
		q.setParameter("category", category);
		int shifted = q.executeUpdate();

		return shifted;
	}
	/**
	 * Modifies elements' order (-1 or +1) so there are no gaps in element ordering
	 * @param category
	 * @param vFrom
	 * @param order
	 * @param up
	 * @return
	 */
	private int shiftChildren(int category, int vFrom, int order, boolean up) {
		Query q;
		q = entityManager.createNativeQuery("update " + dhh.getSchema() + ".APPLICATIONPARAMETER set p_order = p_order " + (up?"-":"+") + " 1 where mappingtype=24 and category= :category and valuefrom= :vFrom and VALUEFROM<>VALUETO and p_order>= :order");
		q.setParameter("order", order);
		q.setParameter("category", category);
		q.setParameter("vFrom", vFrom);
		int shifted = q.executeUpdate();

		return shifted;
	}
	private boolean hasNoChildren(int category, int vTo) {
		Query q;
		q = entityManager.createNativeQuery("select count(*) from " + dhh.getSchema() + ".APPLICATIONPARAMETER ap where mappingtype=24 and category= :category and VALUEFROM=:vTo and VALUEFROM<>VALUETO ");
		q.setParameter("category", category);
		q.setParameter("vTo", vTo);
		int elCount = (Integer) q.getSingleResult();
		boolean isEmpty = (elCount==0);
		return isEmpty;
	}

	public Object getMenuStructure(UserAccount ua, Map params) {
		int category = 1;
		try {
			category = (Integer)params.get("category");
		} catch (Exception e) {
		}

		return getMenuStructure(category).toString();
	}
	protected JSONObject getMenuStructure(int category) {
		return getMenuStructureJSON(category);
	}
	public JSONObject getMenuStructureJSON(int category) {
		int MENU_GROUPS = 24;
		JSONObject jo = new JSONObject();
		JSONArray ja = new JSONArray();
		jo.put(OnlineKeys.DATA, ja);

		Query q = entityManager.createQuery("select app from ApplicationParameter app where applicationParameterType=:type and category= :category and valueFrom<>valueTo order by app.order");
		q.setParameter("type", MENU_GROUPS);
		q.setParameter("category", category);
		List<ApplicationParameter> res = q.getResultList();

		for (ApplicationParameter ap : res) {
			JSONArray _ja = new JSONArray();
			_ja.put(ap.getValueFrom().intValue());
			_ja.put(ap.getCategory());
			_ja.put(ap.getValueTo().intValue());
			_ja.put(ap.getParameterValueInt());
			_ja.put(ap.getFailCode());
			_ja.put(ap.getOrder());

			ja.put(_ja);
		}
		q = entityManager.createQuery("select app from ApplicationParameter app where applicationParameterType=:type and category= :category and valueFrom=valueTo order by app.order");
		q.setParameter("type", MENU_GROUPS);
		q.setParameter("category", category);
		res = q.getResultList();

		for (ApplicationParameter ap : res) {
			JSONArray _ja = new JSONArray();
			_ja.put(ap.getValueFrom().intValue());
			_ja.put(ap.getCategory());
			_ja.put(ap.getValueTo().intValue());
			_ja.put(ap.getParameterValueInt());
			_ja.put(ap.getFailCode());
			_ja.put(ap.getOrder());

			ja.put(_ja);
		}

		return jo;
	}
}
