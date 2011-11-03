package com.ing3nia.parentalcontrol.models;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

/**
 * 
 * @author gaby
 *
 */
@PersistenceCapable
public class PCHelpdeskStatistics {
	/**
	 * Unique key that identifies the statistic.
	 */
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;
	
	/**
	 * 
	 */
	@Persistent
	private PCCategory category;
	
	/**
	 * 
	 */
	@Persistent
	private int counter;

	public PCHelpdeskStatistics() {
		super();
	}

	public PCHelpdeskStatistics(Key key, PCCategory category, int counter) {
		super();
		this.key = key;
		this.category = category;
		this.counter = counter;
	}

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	public PCCategory getCategory() {
		return category;
	}

	public void setCategory(PCCategory category) {
		this.category = category;
	}

	public int getCounter() {
		return counter;
	}

	public void setCounter(int counter) {
		this.counter = counter;
	}
}
