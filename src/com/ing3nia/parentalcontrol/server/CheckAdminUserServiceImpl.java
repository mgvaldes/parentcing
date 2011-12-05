package com.ing3nia.parentalcontrol.server;

import java.util.List;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.ing3nia.parentalcontrol.client.rpc.CheckAdminUserService;
import com.ing3nia.parentalcontrol.models.PCAdmin;
import com.ing3nia.parentalcontrol.models.PCUser;
import com.ing3nia.parentalcontrol.services.exceptions.SessionQueryException;
import com.ing3nia.parentalcontrol.services.utils.ServiceUtils;

public class CheckAdminUserServiceImpl extends RemoteServiceServlet implements CheckAdminUserService {

	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(CheckAdminUserServiceImpl.class.getName());
	
	public CheckAdminUserServiceImpl() {
		//logger.addHandler(new ConsoleHandler());
	}

	@Override
	public Boolean checkAdminUser(String username, String password) {
		Boolean isAdmin = false;
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
		
		Query query = pm.newQuery(PCAdmin.class);
	    query.setFilter("username == username_param && password == pass_param");
	    query.declareParameters("String username_param, String pass_param");
	    query.setRange(0, 1);

	    logger.info("[CheckAdminUserService] Finding admin user by username and password");
	    
		try {
			List<PCAdmin> results = (List<PCAdmin>)query.execute(username, password);
			
			if (!results.isEmpty()) {
				logger.info("[CheckAdminUserService] Found PCAdmin");
				isAdmin = true;
			}
			else{
				logger.info("[CheckAdminUserService] No admin user with the username " + username + ", and password " + password + " was found.");
				isAdmin = false;
			}
		} 
		catch (Exception e) {
			logger.info("[CheckAdminUserService] An error ocurred while finding the PCAdmin by username and password "+ e.getMessage());
			isAdmin = null;
		}
		
		return isAdmin;
	}
}
