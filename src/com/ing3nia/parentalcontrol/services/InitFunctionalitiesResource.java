package com.ing3nia.parentalcontrol.services;

import javax.jdo.PersistenceManager;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import com.ing3nia.parentalcontrol.client.utils.FunctionalityTypeId;
import com.ing3nia.parentalcontrol.models.PCFunctionality;
import com.ing3nia.parentalcontrol.services.utils.ServiceUtils;

@Path("init-func")
public class InitFunctionalitiesResource {

	public InitFunctionalitiesResource() {
		
	}
	
	@GET
	public Response doGet() {
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
		
		PCFunctionality func;
		
		FunctionalityTypeId[] funcs = FunctionalityTypeId.values();
		
		for (FunctionalityTypeId f : funcs) {
			func = new PCFunctionality();
			func.setId(f.getId());
			func.setDescription(f.getDescription());
			pm.makePersistent(func);
		}
		
		pm.close();
		
		ResponseBuilder rbuilder;
		rbuilder = Response.ok("{}", MediaType.APPLICATION_JSON);
		
		return rbuilder.build();
	}
}
