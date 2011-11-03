package com.ing3nia.parentalcontrol.models;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

/**
 * This class contains information about an administrator user.  
 * 
 * @author Ing3nia
 * @author Javier Fernandez
 * @author Maria Gabriela Valdes
 * 
 * @version 1.0
 *
 */
@PersistenceCapable
public class PCAdmin {
	/**
	 * Unique key that identifies the user.
	 */
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;
	
	/**
	 * Specifies the user name of the administrator, used to authenticate.
	 */
	@Persistent
	private String username;
	
	/**
	 * Specifies the password of the administrator, used to authenticate.
	 */
	@Persistent
	private String password;

	public PCAdmin() {
		super();
	}

	public PCAdmin(Key key, String username, String password) {
		super();
		this.key = key;
		this.username = username;
		this.password = password;
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
}
