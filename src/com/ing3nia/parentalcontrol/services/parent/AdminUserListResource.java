package com.ing3nia.parentalcontrol.services.parent;

import java.lang.reflect.Type;
import java.util.ArrayList;
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
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.ing3nia.parentalcontrol.client.models.ClientAdminUserModel;
import com.ing3nia.parentalcontrol.models.PCUser;
import com.ing3nia.parentalcontrol.models.utils.WSStatus;
import com.ing3nia.parentalcontrol.services.exceptions.SessionQueryException;
import com.ing3nia.parentalcontrol.services.utils.ServiceUtils;

@Path("users")
public class AdminUserListResource {

private static Logger logger = Logger.getLogger(AdminUserListResource.class.getName());
	
	public AdminUserListResource() {
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response doPost(@QueryParam(value = "user") final String loggedUserKey) {
		ArrayList<ClientAdminUserModel> admins = new ArrayList<ClientAdminUserModel>();
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
		ResponseBuilder rbuilder;
		
		try {
			Key key = KeyFactory.stringToKey(loggedUserKey);			
			
			PCUser user = (PCUser)pm.getObjectById(PCUser.class, key);
			ArrayList<Key> adminKeys = user.getAdmins();
			PCUser auxAdmin;
			ClientAdminUserModel auxClientUser;
			
			for (Key k : adminKeys) {
				auxAdmin = (PCUser)pm.getObjectById(PCUser.class, k);
				auxClientUser = new ClientAdminUserModel(auxAdmin.getUsername(), auxAdmin.getPassword());
				auxClientUser.setKey(KeyFactory.keyToString(k));
				admins.add(auxClientUser);
			}
		}
		catch (IllegalArgumentException ex) {
			logger.warning("[Admin User List Service] An error ocurred while searching for user. " + ex.getMessage());
			
			rbuilder = Response.ok(WSStatus.INTERNAL_SERVICE_ERROR.getStatusAsJson().toString(), MediaType.APPLICATION_JSON);
			return rbuilder.build();
		}
		catch (Exception ex) {
			logger.warning("[Admin User List Service] Unexpected error occurred. " + ex.getMessage());
			
			rbuilder = Response.ok(WSStatus.UNEXPECTED_ERROR.getStatusAsJson().toString(), MediaType.APPLICATION_JSON);
			return rbuilder.build();
		}
		finally {
			pm.close();
		}
		
		logger.info("[Admin User List Service] Ok Response. Smartphone info succesfully sent.");
		
		JsonObject okResponse = WSStatus.OK.getStatusAsJson();
		Gson jsonBuilder = new Gson();
		JsonParser jsonParser = new JsonParser();
		Type type = new TypeToken<ArrayList<ClientAdminUserModel>>(){}.getType();
		JsonArray adminUsers = (JsonArray)jsonParser.parse(jsonBuilder.toJson(admins, type));
		okResponse.add("admins", adminUsers);
		
		rbuilder = Response.ok(okResponse.toString(), MediaType.APPLICATION_JSON);
		return rbuilder.build();
	}
	
	public PCUser getUser(String id, PersistenceManager pm) throws SessionQueryException, IllegalArgumentException {
		try {
			logger.info("[Tickets List Service] Converting id : " + id + " of user to Key.");
			Key userKey = KeyFactory.stringToKey(id);
						
			logger.info("[Tickets List Service] Searching for user in DB.");
			PCUser user = (PCUser)pm.getObjectById(PCUser.class, userKey);					
			
			return user;
		}
		catch (IllegalArgumentException ex) {
			throw ex;
		}
		catch (Exception ex) {
	    	logger.info("[Update Synchronization Service] An error ocurred while finding the PCSmartphone by key " + ex.getMessage());
	    	
			throw new SessionQueryException();
	    }
	}
}
