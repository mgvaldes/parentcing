package com.ing3nia.parentalcontrol.models;

import java.util.ArrayList;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

/**
 * This class contains information about a user that uses de Parental
 * Control Application  
 * 
 * @author Ing3nia
 * @author Javier Fernandez
 * @author Maria Gabriela Valdes
 * 
 * @version 1.0
 *
 */
@PersistenceCapable
public class PCUser {
	
	public PCUser(){
	}
	
	/**
	 * Unique key that identifies the user.
	 */
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;
	
	/**
	 * Specifies the username of the user.
	 */
	@Persistent
	private String username;
	
	/**
	 * Specifies the password of the user.
	 */
	@Persistent
	private String password;
	
	/**
	 * Represents a collection of smartphones associated to a specific 
	 * user.
	 */
	@Persistent
//	private ArrayList<PCSmartphone> smartphones;
	private ArrayList<Key> smartphones;
	
	/**
	 * 
	 */
	@Persistent
	private String email;
	
	/**
	 * 
	 */
	@Persistent
	private String name;
	
	/**
	 * 
	 */
	private ArrayList<Key> admins;
	
	private ArrayList<Key> openTickets;
	
	private ArrayList<Key> closedTickets;

	public PCUser(Key key, String username, String password,
			ArrayList<Key> smartphones, String email, String name, ArrayList<Key> admins) {
		super();
		this.key = key;
		this.username = username;
		this.password = password;
		this.smartphones = smartphones;
		this.email = email;
		this.name = name;
		this.admins = admins;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
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

	public ArrayList<Key> getSmartphones() {
		return smartphones;
	}

	public void setSmartphones(ArrayList<Key> smartphones) {
		this.smartphones = smartphones;
	}

	public ArrayList<Key> getAdmins() {
		return admins;
	}

	public void setAdmins(ArrayList<Key> admins) {
		this.admins = admins;
	}

	public ArrayList<Key> getOpenTickets() {
		return openTickets;
	}

	public void setOpenTickets(ArrayList<Key> openTickets) {
		this.openTickets = openTickets;
	}

	public ArrayList<Key> getClosedTickets() {
		return closedTickets;
	}

	public void setClosedTickets(ArrayList<Key> closedTickets) {
		this.closedTickets = closedTickets;
	}
}
