package com.ing3nia.parentalcontrol.server;

import java.util.logging.ConsoleHandler;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;

import com.google.appengine.api.datastore.KeyFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import com.ing3nia.parentalcontrol.client.rpc.UserKeyService;
import com.ing3nia.parentalcontrol.client.utils.EncryptionUtils;
import com.ing3nia.parentalcontrol.models.PCUser;
import com.ing3nia.parentalcontrol.services.exceptions.EncodingException;
import com.ing3nia.parentalcontrol.services.exceptions.SessionQueryException;
import com.ing3nia.parentalcontrol.services.utils.ServiceUtils;
import com.ing3nia.parentalcontrol.services.utils.SessionUtils;

public class UserKeyServiceImpl extends RemoteServiceServlet implements UserKeyService {

	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(UserKeyServiceImpl.class.getName());
	
	public UserKeyServiceImpl() {
		//logger.addHandler(new ConsoleHandler());
	}

	@Override
	public String getUserKey(String username, String password) {
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
