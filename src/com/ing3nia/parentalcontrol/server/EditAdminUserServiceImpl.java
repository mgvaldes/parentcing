package com.ing3nia.parentalcontrol.server;

import javax.jdo.PersistenceManager;

import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.appengine.api.memcache.MemcacheService.IdentifiableValue;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.ing3nia.parentalcontrol.client.models.UserModel;
import com.ing3nia.parentalcontrol.client.models.cache.UserCacheParams;
import com.ing3nia.parentalcontrol.client.rpc.EditAdminUserService;
import com.ing3nia.parentalcontrol.client.utils.EncryptionUtils;
import com.ing3nia.parentalcontrol.models.PCUser;
import com.ing3nia.parentalcontrol.services.models.utils.WriteToCache;
import com.ing3nia.parentalcontrol.services.utils.ServiceUtils;

public class EditAdminUserServiceImpl extends RemoteServiceServlet implements EditAdminUserService {

	private static final long serialVersionUID = 1L;
	
	public String NEW_WS = "new";
	public String OLD_WS = "old";
	public String ACTUAL = NEW_WS;
	
	public EditAdminUserServiceImpl() {
		//logger.addHandler(new ConsoleHandler());
	}

	@Override
	public Boolean editAdminUser(String username, String password, String userKey) {
		if (ACTUAL.equals(OLD_WS)) {
			return oldEditAdminUser(username, password, userKey);
		}
		else {
			return newEditAdminUser(username, password, userKey);
		}
	}
	
	public Boolean newEditAdminUser(String username, String password, String userKey) {
		boolean editResult = false;
		UserModel adminUser;
		
		MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
		IdentifiableValue ident = syncCache.getIdentifiable(UserCacheParams.USER + username + "-" + password);
		
		if (ident == null) {
			PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
			
			try {
				PCUser user = (PCUser)pm.getObjectById(PCUser.class, userKey);
				user.setUsername(username);
				user.setPassword(EncryptionUtils.toMD5(password));
				
				WriteToCache.writeUserToCache(user);
				
				editResult = true;
			}
			catch (Exception ex) {
				editResult = false;
			}
			finally {
				pm.close();
			}
		} 
		else {
			try {
				adminUser = (UserModel) ident.getValue();
				adminUser.setUsr(username);
				adminUser.setPass(EncryptionUtils.toMD5(password));
				
				editResult = true;
			}
			catch (Exception ex) {
				editResult = false;
			}
		}
		
		return editResult;
	}
	
	public Boolean oldEditAdminUser(String username, String password, String userKey) {
		boolean editResult = false;
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
		
		try {						
			PCUser user = (PCUser)pm.getObjectById(PCUser.class, userKey);
			user.setUsername(username);
			user.setPassword(EncryptionUtils.toMD5(password));						
			editResult = true;
		}
		catch (Exception ex) {
			editResult = false;
		}
		finally {
			pm.close();
		}
		
		return editResult;
	}
}
