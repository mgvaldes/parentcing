package com.ing3nia.parentalcontrol.models;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public class PCHelpdeskTicketAnswer {
	/**
	 * Unique key.
	 */
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;
	
	@Persistent
	private Key admin;
	
	@Persistent
	private Key user;
	
	@Persistent
	private Date date;
	
	@Persistent
	private String answer;

	public PCHelpdeskTicketAnswer(Key key, Key admin, Date date, Key user, String answer) {
		super();
		this.key = key;
		this.admin = admin;
		this.date = date;
		this.user = user;
		this.answer = answer;
	}
	
	public PCHelpdeskTicketAnswer() {
		super();
	}

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	public Key getAdmin() {
		return admin;
	}

	public void setAdmin(Key admin) {
		this.admin = admin;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Key getUser() {
		return user;
	}

	public void setUser(Key user) {
		this.user = user;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}
}
