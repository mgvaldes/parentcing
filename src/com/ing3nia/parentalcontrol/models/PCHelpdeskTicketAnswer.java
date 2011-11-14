package com.ing3nia.parentalcontrol.models;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

public class PCHelpdeskTicketAnswer {
	/**
	 * Unique key.
	 */
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;
	
	@Persistent
	private PCAdmin admin;
	
	@Persistent
	private Date date;

	public PCHelpdeskTicketAnswer(Key key, PCAdmin admin, Date date) {
		super();
		this.key = key;
		this.admin = admin;
		this.date = date;
	}

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	public PCAdmin getAdmin() {
		return admin;
	}

	public void setAdmin(PCAdmin admin) {
		this.admin = admin;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
}
