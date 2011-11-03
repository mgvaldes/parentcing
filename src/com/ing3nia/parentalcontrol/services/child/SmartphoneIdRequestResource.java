package com.ing3nia.parentalcontrol.services.child;

import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import com.google.appengine.api.datastore.KeyFactory;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.ing3nia.parentalcontrol.models.PCSmartphone;
import com.ing3nia.parentalcontrol.services.models.SmartphoneSerialModel;
import com.ing3nia.parentalcontrol.services.utils.ServiceUtils;

@Path("sph-id-req")
public class SmartphoneIdRequestResource {
	
	private static Logger logger = Logger.getLogger(SmartphoneIdRequestResource.class.getName());
	
	public SmartphoneIdRequestResource() {
		logger.addHandler(new ConsoleHandler());
	}
	
	//{'serial':'AX1-BBMPA2'}
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response doPost(String body) {		
		Gson jsonParser = new Gson();
		Type bodyType = new TypeToken<SmartphoneSerialModel>(){}.getType();
		
		String smartphoneKey = "";
		
		logger.info("[Smartphone Id Request Service] Parseando par‡metros de entrada.");
		SmartphoneSerialModel smartphoneSerial = jsonParser.fromJson(body, bodyType);

		smartphoneKey = getSmartphoneKey(smartphoneSerial.getSerial());
		
		JsonObject jsonObjectStatus = new JsonObject();
		
		if (smartphoneKey != null) {
			jsonObjectStatus.addProperty("code", "00");
			jsonObjectStatus.addProperty("verbose", "OK");
			jsonObjectStatus.addProperty("msg", "OK");
			jsonObjectStatus.addProperty("id", smartphoneKey);
		}
		else {
			jsonObjectStatus.addProperty("code", "01");
			jsonObjectStatus.addProperty("verbose", "INVALID_PHONE_SERIAL");
			jsonObjectStatus.addProperty("msg", "The supplied unique id is not valid");
		}
		
		ResponseBuilder rbuilder = Response.ok(jsonObjectStatus.toString(), MediaType.APPLICATION_JSON);
		
		return rbuilder.build();
	}
	
	public String getSmartphoneKey(String serial) {
		String smartphoneKey = null;
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
		
		logger.info("[Smartphone Id Request Service] Iniciando bœsqueda de smartphone registrado con serial: " + serial);

		Query query = pm.newQuery(PCSmartphone.class);
		
	    query.setFilter("serialNumber == serialNumberParam");
	    query.declareParameters("String serialNumberParam");
	    
	    try {
	    	logger.info("[Smartphone Id Request Service] Ejecutando query para buscar smartphone.");
	    	
	    	List<PCSmartphone> result = (List<PCSmartphone>)query.execute(serial);
	    	
	    	Iterator iter = result.iterator();
	    		    	
	    	if (iter.hasNext()) {
	    		PCSmartphone smartphone = (PCSmartphone)iter.next();
	    		smartphoneKey = KeyFactory.keyToString(smartphone.getKey());
	    	}
	    }
	    catch (IllegalArgumentException ex) {
			//TODO manejar exception y error
		}
	    finally {
	    	pm.close();
	    }
	    
	    return smartphoneKey;
	}
}
