package com.tooooolazy.interfaces;

/**
 * Copied from 'vaadin-base' project<br><br>
 * Any DTO that represents a database entity for which we want to have object level security must implement this.
 * Needed for DTOs so we know how to match one with a DB entity. 
 * @author tooooolazy
 *
 * @param <T> the Class of the primary key (could be a hibernate generated class)
 */public interface HasPrimaryKey<T> {
	public T getPK();
	public void setPK(T pk);
	public String getDbEntityClass();
}
