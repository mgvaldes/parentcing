package com.ing3nia.parentalcontrol.services.models;

import java.util.ArrayList;
import javax.jdo.annotations.Persistent;
import com.google.appengine.api.datastore.Email;
import com.ing3nia.parentalcontrol.models.PCSmartphone;

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

public class UserModel {
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

	public String getUsername() {
		return usr;
	}

	public void setUsername(String username) {
		this.usr = username;
	}

	public String getPassword() {
		return pass;
	}

	public void setPassword(String password) {
		this.pass = password;
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
