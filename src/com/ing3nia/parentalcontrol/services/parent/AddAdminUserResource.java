package com.ing3nia.parentalcontrol.services.parent;

import java.lang.reflect.Type;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
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
import com.ing3nia.parentalcontrol.client.utils.EncryptionUtils;
import com.ing3nia.parentalcontrol.models.PCUser;
import com.ing3nia.parentalcontrol.models.utils.WSStatus;
import com.ing3nia.parentalcontrol.services.exceptions.SessionQueryException;
import com.ing3nia.parentalcontrol.services.models.AddAdminUserModel;
import com.ing3nia.parentalcontrol.services.utils.ServiceUtils;
import com.ing3nia.parentalcontrol.services.utils.WebServiceUtils;

@Path("add-user")
public class AddAdminUserResource {
	
	private static Logger logger = Logger.getLogger(AddAdminUserResource.class.getName());
	
	public AddAdminUserResource() {
		//logger.addHandler(new ConsoleHandler());
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response doPost(String body) {
		Gson jsonParser = new Gson();
		Type bodyType = new TypeToken<AddAdminUserModel>(){}.getType();
		
		AddAdminUserModel addAdminUserModel;
		ResponseBuilder rbuilder;
		
		logger.info("[Add Admin User Service] Parsing input parameters: " + body);		
		
		try {
			addAdminUserModel = jsonParser.fromJson(body, bodyType);
		}
		catch (Exception e) {
			logger.warning("[Add Admin User Service] AddAdminUserModel couldn't be created from post input " + e.getMessage());
			
			rbuilder = Response.ok(WSStatus.INTERNAL_SERVICE_ERROR.getStatusAsJson().toString(), MediaType.APPLICATION_JSON);
			WebServiceUtils.setUTF8Encoding(WebServiceUtils.JSON_CONTENT_TYPE, rbuilder);
			return rbuilder.build();
		}
		
		try {
			String userKey = saveUser(addAdminUserModel);
			
			logger.info("[Add Admin User Service] Ok Response. Admin User saved succesfully.");
			
			JsonObject okResponse = WSStatus.OK.getStatusAsJson();
			okResponse.addProperty("key", userKey);
			
			rbuilder = Response.ok(okResponse.toString(), MediaType.APPLICATION_JSON);
			WebServiceUtils.setUTF8Encoding(WebServiceUtils.JSON_CONTENT_TYPE, rbuilder);
			return rbuilder.build();
		}
		catch (IllegalArgumentException ex) {
			logger.warning("[Add Admin User Service] An error ocurred while converting a Key to String. " + ex.getMessage());
			
			rbuilder = Response.ok(WSStatus.INTERNAL_SERVICE_ERROR.getStatusAsJson().toString(), MediaType.APPLICATION_JSON);
			WebServiceUtils.setUTF8Encoding(WebServiceUtils.JSON_CONTENT_TYPE, rbuilder);
			return rbuilder.build();
		}
		catch (Exception ex) {
			logger.warning("[Add Admin User Service] An error ocurred while finding the PCUser by key. " + ex.getMessage());
			
			rbuilder = Response.ok(WSStatus.INTERNAL_SERVICE_ERROR.getStatusAsJson().toString(), MediaType.APPLICATION_JSON);
			WebServiceUtils.setUTF8Encoding(WebServiceUtils.JSON_CONTENT_TYPE, rbuilder);
			return rbuilder.build();
		}
	}
	
	public String saveUser(AddAdminUserModel addAdminUserModel) throws SessionQueryException, IllegalArgumentException {
		String adminKey = null;
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
		
		logger.info("[Add Admin User Service] Creating new admin user from key: "+ addAdminUserModel.getKey());
		
		try {
			PCUser loggedUser = (PCUser)pm.getObjectById(PCUser.class, KeyFactory.stringToKey(addAdminUserModel.getKey()));
			
			PCUser admin = new PCUser();
			admin.setUsername(addAdminUserModel.getUser().getUsername());	    
			admin.setPassword(EncryptionUtils.toMD5(addAdminUserModel.getUser().getPass()));
			admin.setSmartphones(loggedUser.getSmartphones());
			admin.setEmail(addAdminUserModel.getUser().getUsername());

			pm.makePersistent(admin);
			adminKey = KeyFactory.keyToString(admin.getKey());	
			loggedUser.getAdmins().add(admin.getKey());
		}
		catch (IllegalArgumentException ex) {
			logger.severe("[Add Admin User Service] An error ocurred while creating the new admin user "+ex);
		}
		catch (Exception ex) {
			logger.severe("[Add Admin User Service] An unexpected while creating the new admin user "+ex);
		}
		finally {
			pm.close();
		}
		
		return adminKey;
	}
}
