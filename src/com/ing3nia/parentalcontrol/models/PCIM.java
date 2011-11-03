package com.ing3nia.parentalcontrol.models;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

/**
 * 
 */
@PersistenceCapable
public class PCIM {
	/**
	 * Unique key that identifies the IM.
	 */
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;
	
	/**
	 * 
	 */
	@Persistent
	private int type;
	
	/**
	 * 
	 */
	@Persistent
	private String username;

	public PCIM() {
		super();
	}

	public PCIM(Key key, int type, String username) {
		super();
		this.key = key;
		this.type = type;
		this.username = username;
	}

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
