package com.ing3nia.parentalcontrol.services.models;

public class AnswerTicketModel {

	private static final long serialVersionUID = 1L;
	
	private String ticket;
	
	private TicketAnswerModel answer;

	public AnswerTicketModel(String ticket, TicketAnswerModel answer) {
		super();
		this.ticket = ticket;
		this.answer = answer;
	}

	public AnswerTicketModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public TicketAnswerModel getAnswer() {
		return answer;
	}

	public void setAnswer(TicketAnswerModel answer) {
		this.answer = answer;
	}
}
