package com.ing3nia.parentalcontrol.models;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

/**
 * 
 * 
 * @author Ing3nia
 * @author Javier Fernandez
 * @author Maria Gabriela Valdes
 * 
 * @version 1.0
 *
 */
@PersistenceCapable
public class PCServiceStatistics {
	/**
	 * Unique key that identifies the .
	 */
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;
	
	@Persistent
	private String service;
	
	@Persistent
	private Date date;
	
	@Persistent
	private PCUser user;
	
	@Persistent
	private PCWSStatus status;

	public PCServiceStatistics() {
		super();
	}

	public PCServiceStatistics(Key key, String service, Date date, PCUser user,
			PCWSStatus status) {
		super();
		this.key = key;
		this.service = service;
		this.date = date;
		this.user = user;
		this.status = status;
	}

	public PCWSStatus getStatus() {
		return status;
	}

	public void setStatus(PCWSStatus status) {
		this.status = status;
	}

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public PCUser getUser() {
		return user;
	}

	public void setUser(PCUser user) {
		this.user = user;
	}
}
