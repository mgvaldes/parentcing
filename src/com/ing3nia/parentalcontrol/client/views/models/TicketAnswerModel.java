package com.ing3nia.parentalcontrol.client.views.models;

public class TicketAnswerModel {
	private String date;
	
	private String user;
	
	private String answer;

	public TicketAnswerModel(String date, String user, String answer) {
		super();
		this.date = date;
		this.user = user;
		this.answer = answer;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}
}
