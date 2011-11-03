package com.ing3nia.parentalcontrol.models;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

/**
 * This class contains information about the session of a user in a smartphone.  
 * 
 * @author Ing3nia
 * @author Javier Fernandez
 * @author Maria Gabriela Valdes
 * 
 * @version 1.0
 *
 */
@PersistenceCapable
public class PCSession {
	/**
	 * Unique key that identifies the session.
	 */
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;
	
	/**
	 * 
	 */
	@Persistent
	private String coockieId;
	
	/**
	 * Represents the user that opened and is using the session.
	 */
	private PCUser user;
	
	/**
	 * Specifies the date of the last update made to the session.
	 */
	private Date lastUpdate;

	public PCSession() {
		super();
	}

	public PCSession(Key key, String coockieId, PCUser user, Date lastUpdate) {
		super();
		this.key = key;
		this.coockieId = coockieId;
		this.user = user;
		this.lastUpdate = lastUpdate;
	}

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	public String getCoockieId() {
		return coockieId;
	}

	public void setCoockieId(String coockieId) {
		this.coockieId = coockieId;
	}

	public PCUser getUser() {
		return user;
	}

	public void setUser(PCUser user) {
		this.user = user;
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
}
