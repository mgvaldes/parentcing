package com.ing3nia.parentalcontrol.client.models;

import com.ing3nia.parentalcontrol.models.PCOrganization;

public class OrganizationModel {
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
	
	public PCOrganization convertToPCOrganization() {
		PCOrganization organization = new PCOrganization();
		
		organization.setName(this.name);
		organization.setTitle(this.title);
		
		return organization;
	}
	
	public static OrganizationModel convertToOrganizationModel(PCOrganization organization) {
		return new OrganizationModel(organization.getName(), organization.getTitle());
	}
}
