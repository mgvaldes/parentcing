package com.ing3nia.parentalcontrol.models;

import java.util.ArrayList;
import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public class PCHelpdeskTicket {
	/**
	 * Unique key that identifies the statistic.
	 */
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;
	
	@Persistent
	private PCUser user;
	
	@Persistent
	private Date date;
	
	@Persistent
	private String question;
	
	@Persistent
	private Key status;
	
	@Persistent
	private ArrayList<String> answers;
	
	@Persistent
	private Key category;

	public PCHelpdeskTicket() {
		super();
	}

	public PCHelpdeskTicket(PCUser user, Date date, String question,
			Key status, ArrayList<String> answers, Key category) {
		super();
		this.user = user;
		this.date = date;
		this.question = question;
		this.status = status;
		this.answers = answers;
		this.category = category;
	}

	public PCUser getUser() {
		return user;
	}

	public void setUser(PCUser user) {
		this.user = user;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public Key getStatus() {
		return status;
	}

	public void setStatus(Key status) {
		this.status = status;
	}

	public ArrayList<String> getAnswers() {
		return answers;
	}

	public void setAnswers(ArrayList<String> answers) {
		this.answers = answers;
	}

	public Key getCategory() {
		return category;
	}

	public void setCategory(Key category) {
		this.category = category;
	}
}
