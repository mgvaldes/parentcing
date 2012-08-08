package com.ing3nia.parentalcontrol.server;

import java.util.List;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.appengine.api.memcache.MemcacheService.IdentifiableValue;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.ing3nia.parentalcontrol.client.models.UserModel;
import com.ing3nia.parentalcontrol.client.models.cache.UserCacheParams;
import com.ing3nia.parentalcontrol.client.rpc.AdminLoginService;
import com.ing3nia.parentalcontrol.models.PCAdmin;
import com.ing3nia.parentalcontrol.services.models.utils.WriteToCache;
import com.ing3nia.parentalcontrol.services.utils.ServiceUtils;

public class AdminLoginServiceImpl extends RemoteServiceServlet implements AdminLoginService {

	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(AdminLoginServiceImpl.class.getName());
	
	public String NEW_WS = "new";
	public String OLD_WS = "old";
	public String ACTUAL = NEW_WS;
	
	public AdminLoginServiceImpl() {
		//logger.addHandler(new ConsoleHandler());
	}

	@Override
	public String adminLogin(String username, String password) {
		if (ACTUAL.equals(OLD_WS)) {
			return oldAdminLogin(username, password);
		}
		else {
			return newAdminLogin(username, password);
		}
	}
	
	public String newAdminLogin(String username, String password) {
		UserModel adminUser;
		
		MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
		IdentifiableValue ident = syncCache.getIdentifiable(UserCacheParams.ADMIN + username + "-" + password);
		
		if (ident == null) {
			logger.info("[Admin Login] Admin: " + username + " not in cache. Getting from datastore");

			String adminKey = "";
			PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
			
			Query query = pm.newQuery(PCAdmin.class);
		    query.setFilter("username == username_param && password == pass_param");
		    query.declareParameters("String username_param, String pass_param");
		    query.setRange(0, 1);

		    logger.info("[AdminLoginService] Finding admin user by username and password");
			try {
				List<PCAdmin> results = (List<PCAdmin>) query.execute(username, password);
				
				if (!results.isEmpty()) {
					logger.info("[AdminLoginService] Returning found PCAdmin key");
					adminKey = KeyFactory.keyToString(results.get(0).getKey());
					
					WriteToCache.writeAdminUserToCache(results.get(0));					
				}
				else{
					logger.info("[AdminLoginService] No admin user with the username " + username + ", and password " + password + " was found.");
					adminKey = null;
				}
			} 
			catch (Exception e) {
				logger.info("[AdminLoginService] An error ocurred while finding the PCAdmin by username and password "+ e.getMessage());
				adminKey = null;
			}
			
			return adminKey;
		} 
		else {
			adminUser = (UserModel) ident.getValue();
			logger.info("Admin: " + username + " found in cache.");
			
			return adminUser.getKey();
		}
	}
	
	public String oldAdminLogin(String username, String password) {
		String adminKey = "";
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
		
		Query query = pm.newQuery(PCAdmin.class);
	    query.setFilter("username == username_param && password == pass_param");
	    query.declareParameters("String username_param, String pass_param");
	    query.setRange(0, 1);

	    logger.info("[AdminLoginService] Finding admin user by username and password");
		try {
			List<PCAdmin> results = (List<PCAdmin>) query.execute(username, password);
			
			if (!results.isEmpty()) {
				logger.info("[AdminLoginService] Returning found PCAdmin key");
				adminKey = KeyFactory.keyToString(results.get(0).getKey());
			}
			else{
				logger.info("[AdminLoginService] No admin user with the username " + username + ", and password " + password + " was found.");
				adminKey = null;
			}
		} 
		catch (Exception e) {
			logger.info("[AdminLoginService] An error ocurred while finding the PCAdmin by username and password "+ e.getMessage());
			adminKey = null;
		}
		
		return adminKey;
	}
}
