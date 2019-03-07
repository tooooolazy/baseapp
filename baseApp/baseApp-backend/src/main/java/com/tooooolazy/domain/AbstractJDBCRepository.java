package com.tooooolazy.domain;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Copied (renamed and adjusted) from b2b-services-portal (AbstractJDBCController)
 * @author gpatoulas
 *
 */
public abstract class AbstractJDBCRepository {
	
	private JdbcTemplate 	jdbcTemplate	= null;
    private Properties 		queries			= null;

	@Autowired
    protected Environment env;


	/**
	 * Retrieves DB schema from Environment parameters. If there is none default value 'PCB_USER1' is used
	 * @return
	 */
	protected String getSchema() {
		String schema = env.getProperty("db.schema");
		if (schema == null)
			schema = "PCB_USER1";
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
	public String getDateFragment(String field) {
		if ( env.getProperty("db.type").equals("ORACLE") )
			return "to_date(to_char(" + field + ", 'DD-MON-RRRR'))";
		if ( env.getProperty("db.type").equals("MSSQL2012") )
			return "cast(" + field + " as date)";

		throw new RuntimeException("Looks like [db.type] env param is not defined OR unsupported.");
	}
	public String getYearMonthFragment(String field) {
		if ( env.getProperty("db.type").equals("ORACLE") )
			return "to_number(to_char(" + field + ",'RRRRMM'))";
		if ( env.getProperty("db.type").equals("MSSQL2012") )
			return "year(" + field + ")*100 + month(" + field + ")";

		throw new RuntimeException("Looks like [db.type] env param is not defined OR unsupported.");
	}

	public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
    
	public void setQueries(Properties queries) {
		this.queries = queries;
	}
	
	public JdbcTemplate getDataSource() {
		return this.jdbcTemplate;
	}
	
	public String getPropertyValue( String key ) {
		if ( this.queries == null || this.queries.isEmpty() || !this.queries.containsKey( key ) ) return null;
		
		return this.queries.getProperty( key );
	}

}
