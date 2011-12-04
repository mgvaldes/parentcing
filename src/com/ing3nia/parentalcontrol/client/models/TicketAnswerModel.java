package com.ing3nia.parentalcontrol.client.models;

import java.io.Serializable;

public class TicketAnswerModel implements Serializable  {
	private String key;
	
	private String date;
	
	private String userKey;
	
	private String username;
	
	private String answer;

	public TicketAnswerModel() {
		
	}
	
	public TicketAnswerModel(String date, String userKey, String answer, String username) {
		super();
		this.date = date;
		this.userKey = userKey;
		this.answer = answer;
		this.username = username;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getUserKey() {
		return userKey;
	}

	public void setUserKey(String userKey) {
		this.userKey = userKey;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
