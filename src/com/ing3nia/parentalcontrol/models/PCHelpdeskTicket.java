package com.ing3nia.parentalcontrol.models;

import java.util.ArrayList;
import java.util.Date;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

@PersistenceCapable
public class PCHelpdeskTicket {
	@Persistent
	private PCUser user;
	
	@Persistent
	private Date date;
	
	@Persistent
	private String question;
	
	@Persistent
	private PCStatus status;
	
	@Persistent
	private ArrayList<String> answers;
	
	@Persistent
	private PCCategory category;

	public PCHelpdeskTicket() {
		super();
	}

	public PCHelpdeskTicket(PCUser user, Date date, String question,
			PCStatus status, ArrayList<String> answers, PCCategory category) {
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

	public PCStatus getStatus() {
		return status;
	}

	public void setStatus(PCStatus status) {
		this.status = status;
	}

	public ArrayList<String> getAnswers() {
		return answers;
	}

	public void setAnswers(ArrayList<String> answers) {
		this.answers = answers;
	}

	public PCCategory getCategory() {
		return category;
	}

	public void setCategory(PCCategory category) {
		this.category = category;
	}
}
