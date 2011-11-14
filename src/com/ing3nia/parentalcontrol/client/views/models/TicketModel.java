package com.ing3nia.parentalcontrol.client.views.models;

import java.util.Date;

import sun.rmi.runtime.NewThreadAction;

/**
 * Class that represents a single ticket in the ticket list view.
 * 
 * @author María Gabriela Valdés.
 * @author Javier Fernández.
 * @author Ing3nia.
 *
 */
public class TicketModel {
	/**
	 * Represents the next id correspondeing to the next
	 * ticket created.
	 */
	private static int nextId = 0;
	
	/**
	 * Ticket's key.
	 */
	private String key;
	
	/**
	 * Represents the id of a ticket.
	 */
	private int id;
	
	/**
	 * Indicates the category associated to a ticket.
	 */
	private String category;
	
	/**
	 * Represents the subject or question of a tiket.
	 */
	private String subject;
	
	/**
	 * Represents the date when the ticket was created.
	 */
	private Date date;

	public TicketModel(String category, String subject, Date date) {
		this.category = category;
		this.subject = subject;
		this.date = date;
		this.id = getNextId();
		
		setNextId(this.id);
	}

	public static int getNextId() {
		return nextId;
	}

	public static void setNextId(int nextId) {
		TicketModel.nextId = nextId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
}
