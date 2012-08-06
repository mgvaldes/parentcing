package com.ing3nia.parentalcontrol.server;

import java.util.logging.ConsoleHandler;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.appengine.api.memcache.MemcacheService.IdentifiableValue;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import com.ing3nia.parentalcontrol.client.models.UserModel;
import com.ing3nia.parentalcontrol.client.models.cache.UserCacheParams;
import com.ing3nia.parentalcontrol.client.rpc.UserKeyService;
import com.ing3nia.parentalcontrol.client.utils.EncryptionUtils;
import com.ing3nia.parentalcontrol.models.PCUser;
import com.ing3nia.parentalcontrol.models.utils.WSStatus;
import com.ing3nia.parentalcontrol.services.exceptions.EncodingException;
import com.ing3nia.parentalcontrol.services.exceptions.SessionQueryException;
import com.ing3nia.parentalcontrol.services.models.utils.WriteToCache;
import com.ing3nia.parentalcontrol.services.utils.ServiceUtils;
import com.ing3nia.parentalcontrol.services.utils.SessionUtils;

public class UserKeyServiceImpl extends RemoteServiceServlet implements UserKeyService {

	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(UserKeyServiceImpl.class.getName());

	public String NEW_WS = "new";
	public String OLD_WS = "old";
	public String ACTUAL = NEW_WS;

	public UserKeyServiceImpl() {
		//logger.addHandler(new ConsoleHandler());
	}

	@Override
	public String getUserKey(String username, String password) {
		if (ACTUAL.equals(NEW_WS)) {
            return newWS(username, password);
	}

    return oldWS(username, password);	
		
	}
	
	public String newWS(String username, String password){
		String userKey = null;
		
		//checking if entity exists first
		logger.info("[UserKeyService] Veryfing if a user with the given username and password exists");
		String encryptedPass;
		
		try {
			logger.info("[UserKeyService] Encrypting password in MD5");
			encryptedPass = EncryptionUtils.toMD5(password);
		} 
		catch (EncodingException ex) {
			return userKey;
		}
		
		PCUser pcuser;
		UserModel userCacheModel = null;
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
		MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
		IdentifiableValue ident = syncCache.getIdentifiable(UserCacheParams.USER + username + "-"+ encryptedPass);

		
		if (ident == null) {
			logger.info("User Key] User: " + username+ " "+encryptedPass+" not in cache. Getting from datastore");

			try {
				pcuser = SessionUtils.getPCUser(pm, username,
						encryptedPass);

				
				IdentifiableValue ident2 = syncCache.getIdentifiable(UserCacheParams.USERKEY + KeyFactory.keyToString(pcuser.getKey()));
				if(ident2==null & pcuser!=null){
					logger.info("User not in cache, neither by Usr/pass nor Key");
					userKey = KeyFactory.keyToString(pcuser.getKey());
					logger.info("Got Key from DS: " + userKey);
					logger.info("[User Key] Writing user: " + username+ " to cache");
					WriteToCache.writeUserToCache(pcuser);
				}
				
				if(ident2!=null && pcuser!=null){
					UserModel userModelCache = (UserModel)ident2.getValue();
					userKey = userModelCache.getKey();
					logger.info("Got Updated User by USERKEY: " + userKey);
				}
				
			} catch (SessionQueryException ex) {
				logger.severe("[User Key] An error occurred while searching for user.");
				return null;
			} catch (IllegalArgumentException ex) {
				return null;
			} finally {
				pm.close();
			}

		} else {
			userCacheModel = (UserModel)ident.getValue();
			userKey = userCacheModel.getKey();
			logger.info("[User Key] User found in cache");
		}
		
		return userKey;
	}	
	
	public String oldWS(String username, String password){
		String userKey = null;
		
		//checking if entity exists first
		logger.info("[UserKeyService] Veryfing if a user with the given username and password exists");
		String encryptedPass;
		
		try {
			logger.info("[UserKeyService] Encrypting password in MD5");
			encryptedPass = EncryptionUtils.toMD5(password);
		} 
		catch (EncodingException ex) {
			return userKey;
		}
		
		PCUser user;
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
		
		try {
			user = SessionUtils.getPCUser(pm, username, encryptedPass);
			if(user!=null){
				userKey = KeyFactory.keyToString(user.getKey());
				logger.info("Got Key: "+userKey);
				return userKey;
			}
		}
		catch (SessionQueryException ex) {
			return userKey;
		}
		catch (IllegalArgumentException ex) {
			return userKey;
		}
		finally {
			pm.close();
		}
		
		return userKey;
	}
}
