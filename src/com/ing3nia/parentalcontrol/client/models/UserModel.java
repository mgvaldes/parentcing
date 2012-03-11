package com.ing3nia.parentalcontrol.client.models;

import java.io.Serializable;

/**
 * Represents parent user in central system.
 * Field names appear just as they will be passed in the web service call
 * 
 * @author Ing3nia
 * @author Javier Fernandez
 * @author Maria Gabriela Valdes
 * 
 * @version 1.0
 *
 */

public class UserModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The user's key.
	 */
	private String key;
	
	/**
	 * The user name of the parent
	 */
	private String usr;
	
	/**
	 * Specifies the password of the user.
	 */
	private String pass;

	/**
	 * The email of the parent
	 */
	private String email;
	
	/**
	 * The name of the parent
	 */
	
	private String name;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getUsr() {
		return usr;
	}

	public void setUsr(String usr) {
		this.usr = usr;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public String getUsername() {
		return usr;
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
	
}
