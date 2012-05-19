package com.ing3nia.parentalcontrol.services.child;

import java.lang.reflect.Type;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import com.ing3nia.parentalcontrol.client.models.SmartphoneModel;
import com.ing3nia.parentalcontrol.models.PCSmartphone;
import com.ing3nia.parentalcontrol.models.utils.WSStatus;
import com.ing3nia.parentalcontrol.services.exceptions.SessionQueryException;
import com.ing3nia.parentalcontrol.services.models.utils.SmartphoneModelUtils;
import com.ing3nia.parentalcontrol.services.utils.ServiceUtils;
import com.ing3nia.parentalcontrol.services.utils.WebServiceUtils;

@Path("total-sync")
public class TotalSynchronizationResource {
	
	private static Logger logger = Logger.getLogger(TotalSynchronizationResource.class.getName());
	
	public TotalSynchronizationResource() {
		//logger.addHandler(new ConsoleHandler());
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response doGet(@QueryParam(value = "id") final String id) {
		ResponseBuilder rbuilder;

		logger.info("[Total Synchronization Service] Buscando smartphone asociado a id: " + id);
		SmartphoneModel smartphone = null;
		
		try {
			smartphone = getSmartphone(id);
		}
		catch (SessionQueryException ex) {
			logger.warning("[Total Synchronization Service] An error ocurred while searching for smartphone. " + ex.getMessage());
			
			rbuilder = Response.ok(WSStatus.INTERNAL_SERVICE_ERROR.getStatusAsJson().toString(), MediaType.APPLICATION_JSON);
			WebServiceUtils.setUTF8Encoding(WebServiceUtils.JSON_CONTENT_TYPE, rbuilder);
			return rbuilder.build();
		}
		
		logger.info("[Total Synchronization Service] Ok Response. Smartphone info succesfully sent.");
		
		JsonObject okResponse = WSStatus.OK.getStatusAsJson();
		Gson jsonBuilder = new Gson();
		JsonParser jsonParser = new JsonParser();
		Type type = new TypeToken<SmartphoneModel>(){}.getType();
		JsonObject smartphoneObject = (JsonObject)jsonParser.parse(jsonBuilder.toJson(smartphone, type));
		okResponse.add("smartphone", smartphoneObject);
		
		rbuilder = Response.ok(okResponse.toString(), MediaType.APPLICATION_JSON);
		WebServiceUtils.setUTF8Encoding(WebServiceUtils.JSON_CONTENT_TYPE, rbuilder);
		return rbuilder.build();
	}
	
	public SmartphoneModel getSmartphone(String id) throws SessionQueryException, IllegalArgumentException {
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
		SmartphoneModel smartphoneModel = null;
		
		try {
			logger.info("[Total Synchronization Service] Converting smartphone id: " + id + " to Key.");
			Key smartphoneKey = KeyFactory.stringToKey(id);
			
			logger.info("[Total Synchronization Service] Searching for PCSmartphone by key.");
			PCSmartphone savedSmartphone = pm.getObjectById(PCSmartphone.class, smartphoneKey);
			
			logger.info("[Total Synchronization Service] Converting PCSmartphone en SmartphoneModel.");
			smartphoneModel = SmartphoneModelUtils.convertToSmartphoneModel(savedSmartphone, pm);
		}
		catch (IllegalArgumentException ex) {
			throw ex;
		}
		catch (Exception ex) {
	    	logger.info("[Total Synchronization Service] An error ocurred while finding the PCSmartphone by key " + ex.getMessage());
	    	
			throw new SessionQueryException();
	    }
		
		pm.close();		
		
		return smartphoneModel;
	}
}
