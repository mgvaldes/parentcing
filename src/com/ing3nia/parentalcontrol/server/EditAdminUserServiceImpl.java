package com.ing3nia.parentalcontrol.server;

import java.util.logging.ConsoleHandler;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.ing3nia.parentalcontrol.client.rpc.EditAdminUserService;
import com.ing3nia.parentalcontrol.models.PCUser;
import com.ing3nia.parentalcontrol.services.utils.ServiceUtils;

public class EditAdminUserServiceImpl extends RemoteServiceServlet implements EditAdminUserService {

	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(EditAdminUserServiceImpl.class.getName());
	
	public EditAdminUserServiceImpl() {
		//logger.addHandler(new ConsoleHandler());
	}

	@Override
	public Boolean editAdminUser(String username, String password, String userKey) {
		boolean editResult = false;
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
		
		try {						
			PCUser user = (PCUser)pm.getObjectById(PCUser.class, userKey);
			user.setUsername(username);
			user.setPassword(password);					
			
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
