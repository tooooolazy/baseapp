package com.tooooolazy.domain;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import java.lang.reflect.ParameterizedType;

/**
 * Copied and renamed from b2b-services-portal (AbstractController)
 * @author gpatoulas
 *
 * @param <T>
 */
public abstract class AbstractRepository<T, PK> {

    @PersistenceContext
    protected EntityManager entityManager;

	/**
	 * Retrieves DB schema from Environment parameters. If there is none default value '' is used
	 * @return
	 */
	public String getSchema() {
		String schema = env.getProperty("db.schema");
		if (schema == null)
			schema = "";
		return schema;
	}
	public String getConcatChar() {
		if ( env.getProperty("db.type").equals("ORACLE") )
			return "||";
		if ( env.getProperty("db.type").equals("MSSQL2012") )
			return "+";

		throw new RuntimeException("Looks like [db.type] env param is not defined OR unsupported.");
	}
	public String escapeChar(String chr) {
		if ( env.getProperty("db.type").equals("ORACLE") )
			return "\\" + chr;
		if ( env.getProperty("db.type").equals("MSSQL2012") )
			return "[" + chr + "]";

		throw new RuntimeException("Looks like [db.type] env param is not defined OR unsupported.");
	}
	public String getNativeRatingOrScoreFragment() {
		if ( env.getProperty("db.type").equals("ORACLE") )
			return "    CASE E.EVAL_CAT when 1 THEN E.RATING ELSE E.SCORE || '' END SCORE, ";
		if ( env.getProperty("db.type").equals("MSSQL2012") )
			return "    CASE E.EVAL_CAT when 1 THEN E.RATING ELSE cast(E.SCORE as varchar(2)) END SCORE, ";

		throw new RuntimeException("Looks like [db.type] env param is not defined OR unsupported.");
	}
	public String getDateFragment(String field) {
		if ( env.getProperty("db.type").equals("ORACLE") )
			return "to_date(to_char(" + field + ", 'DD-MON-RRRR'))";
		if ( env.getProperty("db.type").equals("MSSQL2012") )
			return field;
//			return "convert(date, " + field + ")";

		throw new RuntimeException("Looks like [db.type] env param is not defined OR unsupported.");
	}
	public String getDateOnlyFragment(String field) {
		if ( env.getProperty("db.type").equals("ORACLE") )
			return "to_date(to_char(" + field + ", 'DD-MON-RRRR'))";
		if ( env.getProperty("db.type").equals("MSSQL2012") )
//			return field;
			return "convert(date, " + field + ")";

		throw new RuntimeException("Looks like [db.type] env param is not defined OR unsupported.");
	}
	public String getNowDateOnlyFragment() {
		if ( env.getProperty("db.type").equals("ORACLE") )
			return "to_date(to_char( sysdate + , 'DD-MON-RRRR'))";
		if ( env.getProperty("db.type").equals("MSSQL2012") )
			return "convert(date, getDate())";

		throw new RuntimeException("Looks like [db.type] env param is not defined OR unsupported.");
	}
	public String getYearMonthFragment(String field) {
		if ( env.getProperty("db.type").equals("ORACLE") )
			return "to_number(to_char(" + field + ",'RRRRMM'))";
		if ( env.getProperty("db.type").equals("MSSQL2012") )
			return "year(" + field + ")*100 + month(" + field + ")";

		throw new RuntimeException("Looks like [db.type] env param is not defined OR unsupported.");
	}
	public String get3digitNace(String field) {
		if ( env.getProperty("db.type").equals("ORACLE") )
			return field; // FIXME
		if ( env.getProperty("db.type").equals("MSSQL2012") )
			return "substring( replace(" + field + ",'.',''), 0, 4)";

		throw new RuntimeException("Looks like [db.type] env param is not defined OR unsupported.");
	}

	@Autowired
    protected Environment env;

    private Class<T> entityClass;

    public AbstractRepository() {
    }
    
    /**
     * Get enclosed type on initialization.
     * 
     * @author Dimitris Batis
     */
    @SuppressWarnings("unchecked")
    @PostConstruct
    public void init() {
        
         // Get parameterized type. Blame type erasure for this bad code
         entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        
    }

    protected EntityManager getEntityManager() {
        return this.entityManager;
    }

    public void persist(T entity) {
        this.entityManager.persist(entity);
    }

    public void refresh(T entity) {
        this.entityManager.refresh(entity);
    }

    public void merge(T entity) {
        this.entityManager.merge(entity);
    }

    public void remove(T entity) {
        this.entityManager.remove(this.entityManager.merge(entity));
    }

    public T find(PK primaryKey) {
        return this.entityManager.find(entityClass, primaryKey);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
	public List<T> findAll() {
        CriteriaQuery cq = this.entityManager.getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        return this.entityManager.createQuery(cq).getResultList();
    }
	
}
