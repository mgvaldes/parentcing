package com.ing3nia.parentalcontrol.client.models;

/**
 * Class that represents a single input in the admin user list view.
 * 
 * @author María Gabriela Valdés.
 * @author Javier Fernández.
 * @author Ing3nia.
 *
 */
public class AdminUserModel {
	/**
	 * Contains de username of a particulae admin user.
	 */
	private String username;
	
	public AdminUserModel(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
