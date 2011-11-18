package com.ing3nia.parentalcontrol.client.models;

public class ClientOrganizationModel {
	private String name;
	
	private String title;

	public ClientOrganizationModel(String name, String title) {
		super();
		this.name = name;
		this.title = title;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
