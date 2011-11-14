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
	private String subject;
	
	@Persistent
	private String question;
	
	@Persistent
	private Key status;
	
	@Persistent
	private ArrayList<PCHelpdeskTicketAnswer> answers;
	
	@Persistent
	private Key category;

	public PCHelpdeskTicket() {
		super();
	}

	public PCHelpdeskTicket(PCUser user, Date date, String subject, String question,
			Key status, ArrayList<PCHelpdeskTicketAnswer> answers, Key category) {
		super();
		this.user = user;
		this.date = date;
		this.subject = subject;
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

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
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

	public ArrayList<PCHelpdeskTicketAnswer> getAnswers() {
		return answers;
	}

	public void setAnswers(ArrayList<PCHelpdeskTicketAnswer> answers) {
		this.answers = answers;
	}

	public Key getCategory() {
		return category;
	}

	public void setCategory(Key category) {
		this.category = category;
	}
}
