package com.ing3nia.parentalcontrol.server;

import java.util.logging.ConsoleHandler;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;

import com.google.appengine.api.datastore.KeyFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.ing3nia.parentalcontrol.client.rpc.AddAdminUserService;
import com.ing3nia.parentalcontrol.models.PCUser;
import com.ing3nia.parentalcontrol.services.utils.ServiceUtils;

public class AddAdminUserServiceImpl extends RemoteServiceServlet implements AddAdminUserService {

	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(AddAdminUserServiceImpl.class.getName());
	
	public AddAdminUserServiceImpl() {
		logger.addHandler(new ConsoleHandler());
	}

	@Override
	public String addAdminUser(String username, String password, String loggedUserKey) {
		String adminKey = null;
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
		
		try {
			PCUser loggedUser = (PCUser)pm.getObjectById(PCUser.class, KeyFactory.stringToKey(loggedUserKey));
			
			PCUser admin = new PCUser();
			admin.setUsername(username);
			admin.setPassword(password);
			admin.setSmartphones(loggedUser.getSmartphones());
			
			pm.makePersistent(admin);
			
			adminKey = KeyFactory.keyToString(admin.getKey());
		}
		catch (IllegalArgumentException ex) {
			
		}
		catch (Exception ex) {
			
		}
		
		return adminKey;
	}
}
