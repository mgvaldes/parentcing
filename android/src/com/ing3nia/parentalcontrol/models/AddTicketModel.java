package com.ing3nia.parentalcontrol.models;

import java.io.Serializable;

public class AddTicketModel implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String key;
	
	private TicketModel ticket;

	public AddTicketModel() {
		super();
	}

	public AddTicketModel(String key, TicketModel ticket) {
		super();
		this.key = key;
		this.ticket = ticket;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public TicketModel getTicket() {
		return ticket;
	}

	public void setTicket(TicketModel ticket) {
		this.ticket = ticket;
	}
}
