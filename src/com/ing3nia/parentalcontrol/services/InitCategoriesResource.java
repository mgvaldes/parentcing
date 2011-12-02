package com.ing3nia.parentalcontrol.services;

import javax.jdo.PersistenceManager;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import com.ing3nia.parentalcontrol.client.utils.CategoryType;
import com.ing3nia.parentalcontrol.models.PCCategory;
import com.ing3nia.parentalcontrol.services.utils.ServiceUtils;

@Path("init-cat")
public class InitCategoriesResource {

	public InitCategoriesResource() {
		
	}
	
	@GET
	public Response doGet() {
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
		
		PCCategory categ;
		
		CategoryType[] categories = CategoryType.values();
		
		for (CategoryType c : categories) {
			categ = new PCCategory();
			categ.setId(c.getId());
			categ.setDescription(c.getDescription());
			pm.makePersistent(categ);
		}
		
		pm.close();
		
		ResponseBuilder rbuilder;
		rbuilder = Response.ok("{}", MediaType.APPLICATION_JSON);
		
		return rbuilder.build();
	}
}
