package com.ing3nia.parentalcontrol.services.child;

import java.util.List;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import com.google.appengine.api.datastore.KeyFactory;
import com.google.gson.JsonObject;

import com.ing3nia.parentalcontrol.models.PCSmartphone;
import com.ing3nia.parentalcontrol.models.utils.WSStatus;
import com.ing3nia.parentalcontrol.services.exceptions.SessionQueryException;
import com.ing3nia.parentalcontrol.services.utils.ServiceUtils;
import com.ing3nia.parentalcontrol.services.utils.WebServiceUtils;

@Path("sph-id-req")
public class SmartphoneIdRequestResource {
	
	private static Logger logger = Logger.getLogger(SmartphoneIdRequestResource.class.getName());
	
	public SmartphoneIdRequestResource() {
		//logger.addHandler(new ConsoleHandler());
	}
	
	//{'serial':'AX1-BBMPA2'}
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response doPost(@QueryParam(value = "serial") final String serial) {		
		String smartphoneKey = null;
		ResponseBuilder rbuilder;
		
		try {
			smartphoneKey = getSmartphoneKey(serial);
		}
		catch (SessionQueryException ex) {
			logger.warning("[Smartphone Id Request Service] An error ocurred while searching for smartphone. " + ex.getMessage());
			
			rbuilder = Response.ok(WSStatus.INTERNAL_SERVICE_ERROR.getStatusAsJson().toString(), MediaType.APPLICATION_JSON);
			WebServiceUtils.setUTF8Encoding(WebServiceUtils.JSON_CONTENT_TYPE, rbuilder);
			return rbuilder.build();
		}
		catch (IllegalArgumentException ex) {
			logger.warning("[Smartphone Id Request Service] An error ocurred while converting the smartohone Key to String. " + ex.getMessage());
			
			rbuilder = Response.ok(WSStatus.INTERNAL_SERVICE_ERROR.getStatusAsJson().toString(), MediaType.APPLICATION_JSON);
			WebServiceUtils.setUTF8Encoding(WebServiceUtils.JSON_CONTENT_TYPE, rbuilder);
			return rbuilder.build();
		}
		
		if (smartphoneKey == null) {
			logger.info("[Smartphone Id Request Service] No smartohone found for the provided key");
			
			rbuilder = Response.ok(WSStatus.INVALID_SMARTPHONE.getStatusAsJson().toString(), MediaType.APPLICATION_JSON);
			WebServiceUtils.setUTF8Encoding(WebServiceUtils.JSON_CONTENT_TYPE, rbuilder);
			return rbuilder.build();
		}
		else {
			logger.info("[Smartphone Id Request Service] Ok Response. Smartphone id succesfully requested.");
			
			JsonObject okResponse = WSStatus.OK.getStatusAsJson();
			okResponse.addProperty("id", smartphoneKey);
			
			rbuilder = Response.ok(okResponse.toString(), MediaType.APPLICATION_JSON);
			WebServiceUtils.setUTF8Encoding(WebServiceUtils.JSON_CONTENT_TYPE, rbuilder);
			return rbuilder.build();
		}
	}
	
	public String getSmartphoneKey(String serial) throws SessionQueryException, IllegalArgumentException {
		String smartphoneKey = null;
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
		
		logger.info("[Smartphone Id Request Service] Searching for smartphone registered with serial: " + serial);

		Query query = pm.newQuery(PCSmartphone.class);		
	    query.setFilter("serialNumber == serialNumberParam");
	    query.declareParameters("String serialNumberParam");
	    query.setRange(0, 1);
	    
	    try {
	    	List<PCSmartphone> result = (List<PCSmartphone>)query.execute(serial);
	    	
	    	if (!result.isEmpty()) {
	    		smartphoneKey = KeyFactory.keyToString(result.get(0).getKey());
	    	}
	    	else {
	    		smartphoneKey = null;
	    	}
	    }
	    catch (IllegalArgumentException ex) {
			throw ex;
		}
	    catch (Exception ex) {
	    	logger.info("[Smartphone Id Request Service] An error ocurred while finding the PCSmartphone by serial " + ex.getMessage());
	    	
			throw new SessionQueryException();
	    }

	    pm.close();
	    
	    return smartphoneKey;
	}
}
