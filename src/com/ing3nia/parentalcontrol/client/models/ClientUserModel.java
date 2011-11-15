package com.ing3nia.parentalcontrol.client.models;

public class ClientUserModel {
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
