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
import com.ing3nia.parentalcontrol.models.utils.WSStatus;
import com.ing3nia.parentalcontrol.services.exceptions.EncodingException;
import com.ing3nia.parentalcontrol.services.exceptions.SessionQueryException;
import com.ing3nia.parentalcontrol.services.models.RegisterSmartphoneModel;
import com.ing3nia.parentalcontrol.services.models.SmartphoneModel;
import com.ing3nia.parentalcontrol.services.models.UserModel;
import com.ing3nia.parentalcontrol.services.utils.EncryptionUtils;
import com.ing3nia.parentalcontrol.services.utils.ServiceUtils;
import com.ing3nia.parentalcontrol.services.utils.SessionUtils;

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
		ResponseBuilder rbuilder;
		RegisterSmartphoneModel registerSmartphone;
		
		logger.info("[Register Smartphone Service] Parsing input parameters.");
		
		try {
			registerSmartphone = jsonParser.fromJson(body, bodyType);
		}
		catch (Exception e) {
			logger.warning("[Register Smartphone Service] RegisterSmartphoneModel couldn't be created from post input " + WSStatus.INTERNAL_SERVICE_ERROR.getMsg());
			
			rbuilder = Response.ok(WSStatus.INTERNAL_SERVICE_ERROR.getStatusAsJson().toString(), MediaType.APPLICATION_JSON);
			return rbuilder.build();
		}

		try {
			registeredSmartphoneKey = checkUserCredentialsAndRegisterSmartphone(registerSmartphone);
		} 
		catch (EncodingException e) {
			logger.severe("[Register Smartphone Service] An error occurred when encrypting the supplied password");
			
			rbuilder = Response.ok(WSStatus.INVALID_PASSWORD_DATA.getStatusAsJson().toString(), MediaType.APPLICATION_JSON);
			return rbuilder.build();
		}
		catch (SessionQueryException ex) {
			logger.warning("[Register Smartphone Service] An error ocurred while searching for username and password or searching for application. " + ex.getMessage());
			
			rbuilder = Response.ok(WSStatus.INTERNAL_SERVICE_ERROR.getStatusAsJson().toString(), MediaType.APPLICATION_JSON);
			return rbuilder.build();
		}
		catch (IllegalArgumentException ex) {
			logger.warning("[Register Smartphone Service] An error ocurred while converting the smartohone Key to String. " + ex.getMessage());
			
			rbuilder = Response.ok(WSStatus.INTERNAL_SERVICE_ERROR.getStatusAsJson().toString(), MediaType.APPLICATION_JSON);
			return rbuilder.build();
		}
		
		if (registeredSmartphoneKey == null) {
			logger.info("[Register Smartphone Service] No user exists for the provided credentials");
			
			rbuilder = Response.ok(WSStatus.NONEXISTING_USER.getStatusAsJson().toString(), MediaType.APPLICATION_JSON);
			return rbuilder.build();
		}
		else {
			logger.info("[Register Smartphone Service] Ok Response. User succesfully registered smartphone.");
			
			JsonObject okResponse = WSStatus.OK.getStatusAsJson();
			okResponse.addProperty("id", registeredSmartphoneKey);
			
			rbuilder = Response.ok(okResponse.toString(), MediaType.APPLICATION_JSON);
			return rbuilder.build();
		}		
	}
	
	public String checkUserCredentialsAndRegisterSmartphone(RegisterSmartphoneModel registerSmartphoneModel) throws EncodingException, SessionQueryException, IllegalArgumentException {
		logger.info("[Register Smartphone Service] Veryfing if a user with the given username and password exists.");
		
		PCUser user = null;
		String registeredSmartphoneKey = null;
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager(); 
		Query query = pm.newQuery(PCUser.class);
		
		logger.info("[Register Smartphone Service] Applying filters to look for user with username: " + registerSmartphoneModel.getUsr() + " and password: " + registerSmartphoneModel.getPass());
		
	    query.setFilter("username == usernameParam && password == passwordParam");
	    query.declareParameters("String usernameParam, String passwordParam");
	    query.setRange(0, 1);
	    
    	logger.info("[Register Smartphone Service] Encrypting password in MD5.");
    	
    	//String encryptedPass = EncryptionUtils.toMD5(registerSmartphoneModel.getPass());    	
    	String encryptedPass = registerSmartphoneModel.getPass();
    	
    	logger.info("[Register Smartphone Service] Finding PCUser with usr, pass: "+registerSmartphoneModel.getUsr()+" "+encryptedPass+" from: "+registerSmartphoneModel.getPass());
    	user = SessionUtils.getPCUser(pm, registerSmartphoneModel.getUsr(), encryptedPass);
    	
    	
    	if (user == null) {
    		return null;
    	}
    	else {
    		logger.info("[Register Smartphone Service] Creating PCSmartphone with input smartphone info.");
    		
    		PCSmartphone newSmartphone = registerSmartphoneModel.getSmartphone().convertToPCSmartphone();    		
    		
    		logger.info("[Register Smartphone Service] Saving PCSmartphone with input smartphone info.");
    		pm.makePersistent(newSmartphone);
    		
    		logger.info("[Register Smartphone Service] Assigning new smartphone to user.");
    		
    		ArrayList<PCSmartphone> userSmartphones = user.getSmartphones();
    		userSmartphones.add(newSmartphone);    		
    		user.setSmartphones(userSmartphones);
    		
    		registeredSmartphoneKey = KeyFactory.keyToString(newSmartphone.getKey());
    	}
	    	
    	pm.close();
	    
	    return registeredSmartphoneKey;
	}
}
