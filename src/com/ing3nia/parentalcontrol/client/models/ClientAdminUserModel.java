package com.ing3nia.parentalcontrol.client.models;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ClientAdminUserModel implements IsSerializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * String that represents the key of this PCAdmin object.
	 */
	String key;
	
	/**
	 * Admin user's username.
	 */
	String username;
	
	/**
	 * Admin user's password.
	 */
	String password;
	
	/**
	 * Admin help desk tickets
	 */
	ArrayList<TicketModel> openTickets;
	
	ArrayList<TicketModel> closedTickets;
	
	public ClientAdminUserModel(){
		
	}
	
	public ClientAdminUserModel(String key, String username, String password) {
		this.key = key;
		this.username = username;
		this.password = password;
	}
	
	public ClientAdminUserModel(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public ArrayList<TicketModel> getOpenTickets() {
		return openTickets;
	}

	public void setOpenTickets(ArrayList<TicketModel> openTickets) {
		this.openTickets = openTickets;
	}

	public ArrayList<TicketModel> getClosedTickets() {
		return closedTickets;
	}

	public void setClosedTickets(ArrayList<TicketModel> closedTickets) {
		this.closedTickets = closedTickets;
	}
}
