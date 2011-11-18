package com.ing3nia.parentalcontrol.client.models;

public class ClientAdminUserModel {
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
}
