package com.ing3nia.parentalcontrol.client.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Class that represents a single ticket in the ticket list view.
 * 
 * @author Mar�a Gabriela Vald�s.
 * @author Javier Fern�ndez.
 * @author Ing3nia.
 *
 */
public class TicketModel implements Serializable {
	/**
	 * Represents the next id correspondeing to the next
	 * ticket created.
	 */
	private static final long serialVersionUID = 1L;	

	/**
	 * Ticket's key.
	 */
	private String key;
	
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
	
	private String comment;
	
	private ArrayList<TicketAnswerModel> answers;
	
	private String name;
	
	public TicketModel(){
		
	}
	
	public TicketModel(String key, String category, String subject, Date date, String comment, ArrayList<TicketAnswerModel> answers) {
		this.key = key;
		this.category = category;
		this.subject = subject;
		this.date = date;
//		this.id = getNextId();
		this.comment = comment;
		this.answers = answers;
		
//		setNextId(this.id);
	}
	
	public TicketModel(String key, String category, String subject, Date date, String comment, ArrayList<TicketAnswerModel> answers, String name) {
		this.key = key;
		this.category = category;
		this.subject = subject;
		this.date = date;
//		this.id = getNextId();
		this.comment = comment;
		this.answers = answers;
		this.name = name;
		
//		setNextId(this.id);
	}
	

//	public static int getNextId() {
//		return nextId;
//	}
//
//	public static void setNextId(int nextId) {
//		TicketModel.nextId = nextId;
//	}
//
//	public int getId() {
//		return id;
//	}
//
//	public void setId(int id) {
//		this.id = id;
//	}

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

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public ArrayList<TicketAnswerModel> getAnswers() {
		return answers;
	}

	public void setAnswers(ArrayList<TicketAnswerModel> answers) {
		this.answers = answers;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
