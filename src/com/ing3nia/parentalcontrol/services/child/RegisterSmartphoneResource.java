package com.ing3nia.parentalcontrol.services.child;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import com.ing3nia.parentalcontrol.models.PCContact;
import com.ing3nia.parentalcontrol.models.PCSmartphone;
import com.ing3nia.parentalcontrol.models.PCUser;
import com.ing3nia.parentalcontrol.services.models.RegisterSmartphoneModel;
import com.ing3nia.parentalcontrol.services.utils.ServiceUtils;

@Path("reg-sph")
public class RegisterSmartphoneResource {
	
	private static Logger logger = Logger.getLogger(RegisterSmartphoneResource.class.getName());
	
	public RegisterSmartphoneResource() {
		logger.addHandler(new ConsoleHandler());
	}
	
	//{'usr':'mgvaldes','pass':'mgv4ld3s','smartphone':{'location':{'latitude':'10.23','longitude':'12.87'},'activeContacts':[{'firstName':'Pedro','lastName':'Perez','phones':[{'type':1,'phoneNumber':'02129762345'}]},{'firstName':'Maria','lastName':'Vicentini','phones':[{'type':2,'phoneNumber':'04123456789'}]}],'name':'PPSmart','device':{'model':'9000','version':'5.2','type':3},'serialNumber':'AX1-BBMPA2','appVersion':'1.0.0'}}
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response doPost(String body) {		
		Gson jsonParser = new Gson();
		Type bodyType = new TypeToken<RegisterSmartphoneModel>(){}.getType();
		
		String registeredSmartphoneKey = null;
		
		logger.info("[Register Smartphone Service] Parseando par‡metros de entrada.");
		RegisterSmartphoneModel registerSmartphone = jsonParser.fromJson(body, bodyType);

		registeredSmartphoneKey = checkUserCredentialsAndRegisterSmartphone(registerSmartphone);
		
		JsonObject jsonObjectStatus = new JsonObject();
		
		if (registeredSmartphoneKey != null) {
			jsonObjectStatus.addProperty("code", "00");
			jsonObjectStatus.addProperty("verbose", "OK");
			jsonObjectStatus.addProperty("msg", "OK");
			jsonObjectStatus.addProperty("id", registeredSmartphoneKey);
		}
		else {
			jsonObjectStatus.addProperty("code", "01");
			jsonObjectStatus.addProperty("verbose", "INVALID_PHONE_SERIAL");
			jsonObjectStatus.addProperty("msg", "The supplied unique id is not valid");
		}
		
		ResponseBuilder rbuilder = Response.ok(jsonObjectStatus.toString(), MediaType.APPLICATION_JSON);
		
		return rbuilder.build();
	}
	
	@GET
	public String doGet() {
		Key key = KeyFactory.stringToKey("aglub19hcHBfaWRyHgsSBlBDVXNlchgFDAsSDFBDU21hcnRwaG9uZRhEDA");
		
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
		
		PCSmartphone smart = (PCSmartphone)pm.getObjectById(PCSmartphone.class, key);
		
		if (smart.getActiveContacts() == null) {
			return "NULL";
		}
		else {
			PCContact newContact = new PCContact();
			newContact.setFirstName("Conejo");
			newContact.setLastName("Remay");
			
			pm.makePersistent(newContact);
			
			ArrayList<Key> contacts = smart.getActiveContacts();
			contacts.add(newContact.getKey());
			
			smart.setActiveContacts(contacts);
			
			pm.close();
			
			return "YES";
		}
		
	}
	
	public String checkUserCredentialsAndRegisterSmartphone(RegisterSmartphoneModel registerSmartphoneModel) {
		PCUser user = null;
		String registeredSmartphoneKey = null;
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager(); 
		
		logger.info("[Register Smartphone Service] Validando usuario y password del usuario.");

		Query query = pm.newQuery(PCUser.class);
		
		logger.info("[Register Smartphone Service] Aplicando filtro de bœsqueda para buscar usuario con nombre de usuario: " + registerSmartphoneModel.getUsr() + " y password: " + registerSmartphoneModel.getPass());
		
	    query.setFilter("username == usernameParam && password == passwordParam");
	    query.declareParameters("String usernameParam, String passwordParam");
	    query.setRange(0, 1);
	    
	    try {
	    	logger.info("[Register Smartphone Service] Ejecutando query para buscar usuario.");
	    	
	    	List<PCUser> result = (List<PCUser>)query.execute(registerSmartphoneModel.getUsr(), registerSmartphoneModel.getPass());
	    	
	    	Iterator iter = result.iterator();
	    		    	
	    	if (iter.hasNext()) {
	    		user = (PCUser)iter.next();
	    		
	    		logger.info("[Register Smartphone Service] Creando y guardando PCSmartphone a partir de datos enviados por el usuario.");
	    		
	    		PCSmartphone newSmartphone = registerSmartphoneModel.getSmartphone().convertToPCSmartphone();
	    		
	    		pm.makePersistent(newSmartphone);
	    		
	    		logger.info("[Register Smartphone Service] Asignando nuevo smartphone a usuario.");
	    		
	    		ArrayList<PCSmartphone> userSmartphones = user.getSmartphones();
	    		userSmartphones.add(newSmartphone);
	    		
	    		user.setSmartphones(userSmartphones);
	    		
	    		try {
	    			registeredSmartphoneKey = KeyFactory.keyToString(newSmartphone.getKey());
	    		}
	    		catch (IllegalArgumentException ex) {
	    			//TODO manejar exception y error
	    		}	    		
	    	}
	    }
	    finally {
	    	pm.close();
	    }
	    
	    return registeredSmartphoneKey;
	}
}
