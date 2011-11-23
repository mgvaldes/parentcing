package com.ing3nia.parentalcontrol.client.models.utils;

import com.ing3nia.parentalcontrol.client.models.OrganizationModel;
import com.ing3nia.parentalcontrol.models.PCOrganization;

public class OrganizationModelUtils {
	public static PCOrganization convertToPCOrganization(OrganizationModel org) {
		PCOrganization organization = new PCOrganization();
		
		organization.setName(org.getName());
		organization.setTitle(org.getTitle());
		
		return organization;
	}
	
	public static OrganizationModel convertToOrganizationModel(PCOrganization organization) {
		return new OrganizationModel(organization.getName(), organization.getTitle());
	}
}
