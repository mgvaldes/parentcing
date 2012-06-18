package com.ing3nia.parentalcontrol.services.parent;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
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

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.appengine.api.memcache.MemcacheService.IdentifiableValue;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.ing3nia.parentalcontrol.client.models.UserModel;
import com.ing3nia.parentalcontrol.client.models.cache.SmartphoneCacheParams;
import com.ing3nia.parentalcontrol.client.models.cache.UserCacheParams;
import com.ing3nia.parentalcontrol.client.utils.EncryptionUtils;
import com.ing3nia.parentalcontrol.models.PCUser;
import com.ing3nia.parentalcontrol.models.utils.WSStatus;
import com.ing3nia.parentalcontrol.services.exceptions.SessionQueryException;
import com.ing3nia.parentalcontrol.services.models.AddAdminUserModel;
import com.ing3nia.parentalcontrol.services.models.utils.WriteToCache;
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
		
		logger.warning("[Add Admin User Service] Checking if PCUser exists with same username");
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
		
		Query query = pm.newQuery(PCUser.class);
	    query.setFilter("username == username_param");
	    query.declareParameters("String username_param");
	    query.setRange(0, 1);

		try {
			List<PCUser> results = (List<PCUser>) query.execute(addAdminUserModel.getUser().getUsername());

			if (results.isEmpty()) {
				String userKey = saveUser(addAdminUserModel, pm);
				
				logger.info("[Add Admin User Service] Ok Response. Admin User saved succesfully.");
				
				JsonObject okResponse = WSStatus.OK.getStatusAsJson();
				okResponse.addProperty("key", userKey);
				
				rbuilder = Response.ok(okResponse.toString(), MediaType.APPLICATION_JSON);
				WebServiceUtils.setUTF8Encoding(WebServiceUtils.JSON_CONTENT_TYPE, rbuilder);
				return rbuilder.build();
			}
			else{
				logger.warning("[Add Admin User Service] A PCUser already exists with this username.");
				
				rbuilder = Response.ok(WSStatus.PREEXISTING_USER.getStatusAsJson().toString(), MediaType.APPLICATION_JSON);
				WebServiceUtils.setUTF8Encoding(WebServiceUtils.JSON_CONTENT_TYPE, rbuilder);
				return rbuilder.build();
			}						
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
		finally {
			pm.close();
		}
	}
	
	public String saveUser(AddAdminUserModel addAdminUserModel, PersistenceManager pm) throws SessionQueryException, IllegalArgumentException {
		String adminKey = null;		
		
		logger.info("[Add Admin User Service] Creating new admin user from key: "+ addAdminUserModel.getKey());
		
		try {
			PCUser loggedUser = (PCUser)pm.getObjectById(PCUser.class, KeyFactory.stringToKey(addAdminUserModel.getKey()));
			
			MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
			String adminUsersCacheKey = UserCacheParams.USER + addAdminUserModel.getKey() + UserCacheParams.ADMIN_LIST;
			IdentifiableValue cacheIdentAdminUsers = (IdentifiableValue) syncCache.getIdentifiable(adminUsersCacheKey);
			ArrayList<UserModel> cacheAdminUsers = null;
			ArrayList<Key> adminUserKeys = loggedUser.getAdmins();
			
			if (cacheIdentAdminUsers == null) {
				ArrayList<PCUser> adminUsers = new ArrayList<PCUser>();
				PCUser auxAdminUser;
				
				for (Key key : adminUserKeys) {
					auxAdminUser = pm.getObjectById(PCUser.class, key);
					adminUsers.add(auxAdminUser);
				}
				
				WriteToCache.writeAdminUsersToCache(addAdminUserModel.getKey(), adminUsers);
				
				cacheIdentAdminUsers = syncCache.getIdentifiable(adminUsersCacheKey);
				cacheAdminUsers = (ArrayList<UserModel>) cacheIdentAdminUsers.getValue();
			}
			else {
				cacheAdminUsers = (ArrayList<UserModel>) cacheIdentAdminUsers.getValue();
			}
			
			PCUser admin = new PCUser();
			admin.setUsername(addAdminUserModel.getUser().getUsername());	    
			admin.setPassword(EncryptionUtils.toMD5(addAdminUserModel.getUser().getPass()));
			admin.setSmartphones(loggedUser.getSmartphones());
			admin.setEmail(addAdminUserModel.getUser().getUsername());
			admin.setName(addAdminUserModel.getUser().getName());

			pm.makePersistent(admin);
			adminKey = KeyFactory.keyToString(admin.getKey());	
			loggedUser.getAdmins().add(admin.getKey());
			
			WriteToCache.writeUserToCache(admin);
			
			UserModel newAdmin = new UserModel();
			newAdmin.setEmail(admin.getEmail());
			newAdmin.setKey(adminKey);
			newAdmin.setName(admin.getName());
			newAdmin.setPass(admin.getPassword());
			newAdmin.setUsr(admin.getUsername());
			
			ArrayList<Key> keys = admin.getSmartphones();
			ArrayList<String> smartphoneKeys = new ArrayList<String>();
			
			for (Key sphKey : keys) {
				smartphoneKeys.add(KeyFactory.keyToString(sphKey));
			}
			
			newAdmin.setSmartphoneKeys(smartphoneKeys);
			
			cacheAdminUsers.add(newAdmin);
			
			WriteToCache.writeAdminUsersToCache(cacheAdminUsers, addAdminUserModel.getKey());
		}
		catch (IllegalArgumentException ex) {
			logger.severe("[Add Admin User Service] An error ocurred while creating the new admin user "+ex);
		}
		catch (Exception ex) {
			logger.severe("[Add Admin User Service] An unexpected while creating the new admin user "+ex);
		}
		
		return adminKey;
	}
}
