package com.ing3nia.parentalcontrol.client.models;

import java.io.Serializable;

public class OrganizationModel implements Serializable {
	private static final long serialVersionUID = 1L;

	private String name;
	
	private String title;

	public OrganizationModel(String name, String title) {
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
