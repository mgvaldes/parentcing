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
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.appengine.api.memcache.MemcacheService.IdentifiableValue;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.ing3nia.parentalcontrol.client.models.UserModel;
import com.ing3nia.parentalcontrol.client.models.cache.UserCacheParams;
import com.ing3nia.parentalcontrol.client.utils.EncryptionUtils;
import com.ing3nia.parentalcontrol.models.PCUser;
import com.ing3nia.parentalcontrol.models.utils.WSStatus;
import com.ing3nia.parentalcontrol.services.exceptions.EncodingException;
import com.ing3nia.parentalcontrol.services.exceptions.SessionQueryException;
import com.ing3nia.parentalcontrol.services.utils.ServiceUtils;
import com.ing3nia.parentalcontrol.services.utils.SessionUtils;

@Path("user-key")
public class UserKeyResource {
	private static Logger logger = Logger.getLogger(UserKeyResource.class
			.getName());
	public String NEW_WS = "new";
	public String OLD_WS = "old";
	public String ACTUAL = NEW_WS;

	public UserKeyResource() {
		//logger.addHandler(new ConsoleHandler());
	}
	
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response doPost(String body) {
	
		if (ACTUAL.equals(NEW_WS)) {
            return newWS(body);
	}

    return oldWS(body);
		
	}
	
	public Response oldWS(String body){
		
		logger.info("[User Key] Processing user key request");
		
		//creating global variables
		Gson gson = new Gson();
		ResponseBuilder rbuilder;
		
		logger.info("[User Key] Parsing request message");
		
		//parsing json message from request
		Type userModelType = new TypeToken<UserModel>(){}.getType();
		UserModel user;
		
		try{
			logger.info("[User Key] Transforming json object " + body);
			user = gson.fromJson(body, userModelType);
		}
		catch(Exception e){
			logger.warning("[User Key] UserModel couldn't be created from message " + WSStatus.INTERNAL_SERVICE_ERROR.getMsg());
			
			rbuilder = Response.ok(WSStatus.INTERNAL_SERVICE_ERROR.getStatusAsJson().toString(), MediaType.APPLICATION_JSON);
			return rbuilder.build();
		}
		
		String userKey = null;
		
		//checking if entity exists first
		logger.info("[User Key] Veryfing if a user with the given username and password exists");
		String encryptedPass;
		
		try {
			logger.info("[User Key] Encrypting password in MD5");
			encryptedPass = EncryptionUtils.toMD5(user.getPass());
		} 
		catch (EncodingException ex) {
			logger.severe("[User Key] An error occurred when encrypting the supplied password");
			
			rbuilder = Response.ok(WSStatus.INVALID_PASSWORD_DATA.getStatusAsJson().toString(), MediaType.APPLICATION_JSON);
			return rbuilder.build();
		}
		
		PCUser pcuser;
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
		
		try {
			pcuser = SessionUtils.getPCUser(pm, user.getUsername(), encryptedPass);
			
			if(pcuser != null){
				userKey = KeyFactory.keyToString(pcuser.getKey());
				
				logger.info("Got Key: " + userKey);
			}
		}
		catch (SessionQueryException ex) {
			logger.severe("[User Key] An error occurred while searching for user.");
			
			rbuilder = Response.ok(WSStatus.NONEXISTING_USER.getStatusAsJson().toString(), MediaType.APPLICATION_JSON);
			return rbuilder.build();
		}
		catch (IllegalArgumentException ex) {
			logger.severe("[User Key] An error occurred while searching for user.");
			
			rbuilder = Response.ok(WSStatus.INTERNAL_SERVICE_ERROR.getStatusAsJson().toString(), MediaType.APPLICATION_JSON);
			return rbuilder.build();
		}
		finally {
			pm.close();
		}
		
		logger.info("[User Key] Ok Response.");
		
		JsonObject okResponse = WSStatus.OK.getStatusAsJson();
		okResponse.addProperty("key", userKey);
		
		rbuilder = Response.ok(okResponse.toString(), MediaType.APPLICATION_JSON);
		return rbuilder.build();
	}
	

	public Response newWS(String body){
		
		logger.info("[User Key] Processing user key request");
		
		//creating global variables
		Gson gson = new Gson();
		ResponseBuilder rbuilder;
		
		logger.info("[User Key] Parsing request message");
		
		//parsing json message from request
		Type userModelType = new TypeToken<UserModel>(){}.getType();
		UserModel user;
		
		try{
			logger.info("[User Key] Transforming json object " + body);
			user = gson.fromJson(body, userModelType);
		}
		catch(Exception e){
			logger.warning("[User Key] UserModel couldn't be created from message " + WSStatus.INTERNAL_SERVICE_ERROR.getMsg());
			
			rbuilder = Response.ok(WSStatus.INTERNAL_SERVICE_ERROR.getStatusAsJson().toString(), MediaType.APPLICATION_JSON);
			return rbuilder.build();
		}
		
		String userKey = null;
		
		//checking if entity exists first
		logger.info("[User Key] Veryfing if a user with the given username and password exists");
		String encryptedPass;
		
		try {
			logger.info("[User Key] Encrypting password in MD5");
			encryptedPass = EncryptionUtils.toMD5(user.getPass());
		} 
		catch (EncodingException ex) {
			logger.severe("[User Key] An error occurred when encrypting the supplied password");
			
			rbuilder = Response.ok(WSStatus.INVALID_PASSWORD_DATA.getStatusAsJson().toString(), MediaType.APPLICATION_JSON);
			return rbuilder.build();
		}
		
		PCUser pcuser;
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
		UserModel userCacheModel;
		
		MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
		IdentifiableValue ident = syncCache.getIdentifiable(UserCacheParams.USER + user.getUsername() + "-"+ encryptedPass);

		if (ident == null) {
			logger.info("User Key] User: " + user.getUsername()+ " not in cache. Getting from datastore");

			try {
				pcuser = SessionUtils.getPCUser(pm, user.getUsername(),
						encryptedPass);

				if (pcuser != null) {
					userKey = KeyFactory.keyToString(pcuser.getKey());

					logger.info("Got Key: " + userKey);
				}
			} catch (SessionQueryException ex) {
				logger.severe("[User Key] An error occurred while searching for user.");

				rbuilder = Response.ok(WSStatus.NONEXISTING_USER
						.getStatusAsJson().toString(),
						MediaType.APPLICATION_JSON);
				return rbuilder.build();
			} catch (IllegalArgumentException ex) {
				logger.severe("[User Key] An error occurred while searching for user.");

				rbuilder = Response.ok(WSStatus.INTERNAL_SERVICE_ERROR
						.getStatusAsJson().toString(),
						MediaType.APPLICATION_JSON);
				return rbuilder.build();
			} finally {
				pm.close();
			}

		} else {
			userCacheModel = (UserModel)ident.getValue();
			userKey = userCacheModel.getKey();
			logger.info("[User Key] User found in cache");
		}
		
		logger.info("[User Key] Ok Response.");
		
		JsonObject okResponse = WSStatus.OK.getStatusAsJson();
		okResponse.addProperty("key", userKey);
		
		rbuilder = Response.ok(okResponse.toString(), MediaType.APPLICATION_JSON);
		return rbuilder.build();
	}
	
}
