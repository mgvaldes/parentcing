package com.ing3nia.parentalcontrol.services.models;

import java.io.Serializable;

import com.ing3nia.parentalcontrol.client.models.UserModel;

public class AddAdminUserModel implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String key;
	
	private UserModel user;

	public AddAdminUserModel(String key, UserModel user) {
		super();
		this.key = key;
		this.user = user;
	}

	public AddAdminUserModel() {
		super();
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public UserModel getUser() {
		return user;
	}

	public void setUser(UserModel user) {
		this.user = user;
	}
}
