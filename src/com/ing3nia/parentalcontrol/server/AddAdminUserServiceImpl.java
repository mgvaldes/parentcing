package com.ing3nia.parentalcontrol.server;

import java.util.logging.ConsoleHandler;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;

import com.google.appengine.api.datastore.KeyFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.ing3nia.parentalcontrol.client.rpc.AddAdminUserService;
import com.ing3nia.parentalcontrol.models.PCUser;
import com.ing3nia.parentalcontrol.services.utils.EncryptionUtils;
import com.ing3nia.parentalcontrol.services.utils.ServiceUtils;

public class AddAdminUserServiceImpl extends RemoteServiceServlet implements AddAdminUserService {

	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(AddAdminUserServiceImpl.class.getName());
	
	public AddAdminUserServiceImpl() {
		//logger.addHandler(new ConsoleHandler());
	}

	@Override
	public String addAdminUser(String username, String password, String loggedUserKey) {
		String adminKey = null;
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
		
		logger.info("[Add Admin User Service] Creating new admin user from key: "+ loggedUserKey);
		try {
			PCUser loggedUser = (PCUser)pm.getObjectById(PCUser.class, KeyFactory.stringToKey(loggedUserKey));
			
			PCUser admin = new PCUser();
			admin.setUsername(username);	    
			admin.setPassword(EncryptionUtils.toMD5(password));
			admin.setSmartphones(loggedUser.getSmartphones());
			admin.setEmail(username);

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
