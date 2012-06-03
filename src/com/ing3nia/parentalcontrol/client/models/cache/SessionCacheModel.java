package com.ing3nia.parentalcontrol.client.models.cache;

import java.io.Serializable;

import com.ing3nia.parentalcontrol.client.models.UserModel;


/**
 * Represents a user session in central system for cache.
 * Field names appear just as they will be passed in the web service call
 * 
 * @author Ing3nia
 * @author Javier Fernandez
 * @author Maria Gabriela Valdes
 * 
 * @version 1.0
 *
 */

public class SessionCacheModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Unique key that identifies the session.
	 */
    private String key;
	
	/**
	 * Represents a unique session identifier assigned when logged in
	 */

	private String cookieId;
		
	private UserModel userModel;
	
	/**
	 * Specifies the date of the last update made to the session.
	 */
	private String lastUpdate;

	public SessionCacheModel(){
		
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getCookieId() {
		return cookieId;
	}

	public void setCookieId(String cookieId) {
		this.cookieId = cookieId;
	}

	public String getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(String lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public UserModel getUserModel() {
		return userModel;
	}

	public void setUserModel(UserModel userModel) {
		this.userModel = userModel;
	}

}
