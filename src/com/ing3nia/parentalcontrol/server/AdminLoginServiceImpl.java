package com.ing3nia.parentalcontrol.server;

import java.util.List;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.google.appengine.api.datastore.KeyFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.ing3nia.parentalcontrol.client.rpc.AdminLoginService;
import com.ing3nia.parentalcontrol.models.PCAdmin;
import com.ing3nia.parentalcontrol.models.PCUser;
import com.ing3nia.parentalcontrol.services.exceptions.SessionQueryException;
import com.ing3nia.parentalcontrol.services.utils.ServiceUtils;

public class AdminLoginServiceImpl extends RemoteServiceServlet implements AdminLoginService {

	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(AdminLoginServiceImpl.class.getName());
	
	public AdminLoginServiceImpl() {
		//logger.addHandler(new ConsoleHandler());
	}

	@Override
	public String adminLogin(String username, String password) {
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
