package com.ing3nia.parentalcontrol.server;

import java.util.ArrayList;
import java.util.logging.ConsoleHandler;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.ing3nia.parentalcontrol.client.models.ClientUserModel;
import com.ing3nia.parentalcontrol.client.rpc.AdminUserListService;
import com.ing3nia.parentalcontrol.models.PCUser;
import com.ing3nia.parentalcontrol.services.utils.ServiceUtils;

public class AdminUserListServiceImpl extends RemoteServiceServlet implements AdminUserListService {

	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(AdminUserListServiceImpl.class.getName());
	
	public AdminUserListServiceImpl() {
		logger.addHandler(new ConsoleHandler());
	}

	@Override
	public ArrayList<ClientUserModel> getAdminUserList(String userKey) {
		ArrayList<ClientUserModel> admins = new ArrayList<ClientUserModel>();
		
		try {
			Key key = KeyFactory.stringToKey(userKey);
			PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
			
			PCUser user = (PCUser)pm.getObjectById(PCUser.class, key);
			ArrayList<Key> adminKeys = user.getAdmins();
			PCUser auxAdmin;
			ClientUserModel auxClientUser;
			
			for (Key k : adminKeys) {
				auxAdmin = (PCUser)pm.getObjectById(PCUser.class, k);
				auxClientUser = new ClientUserModel(auxAdmin.getUsername(), auxAdmin.getPassword());
				auxClientUser.setKey(KeyFactory.keyToString(k));
				admins.add(auxClientUser);
			}
		}
		catch (IllegalArgumentException ex) {
			admins = null;
		}
		catch (Exception ex) {
			admins = null;
		}
		
		return admins;
	}
}
