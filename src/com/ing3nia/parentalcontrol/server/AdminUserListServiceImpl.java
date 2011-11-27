package com.ing3nia.parentalcontrol.server;

import java.util.ArrayList;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.ing3nia.parentalcontrol.client.models.ClientAdminUserModel;
import com.ing3nia.parentalcontrol.client.rpc.AdminUserListService;
import com.ing3nia.parentalcontrol.models.PCUser;
import com.ing3nia.parentalcontrol.services.utils.ServiceUtils;

public class AdminUserListServiceImpl extends RemoteServiceServlet implements AdminUserListService {

	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(AdminUserListServiceImpl.class.getName());
	
	public AdminUserListServiceImpl() {
	}

	@Override
	public ArrayList<ClientAdminUserModel> getAdminUserList(String userKey) {
		ArrayList<ClientAdminUserModel> admins = new ArrayList<ClientAdminUserModel>();
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
		
		try {
			Key key = KeyFactory.stringToKey(userKey);			
			
			PCUser user = (PCUser)pm.getObjectById(PCUser.class, key);
			ArrayList<Key> adminKeys = user.getAdmins();
			PCUser auxAdmin;
			ClientAdminUserModel auxClientUser;
			
			for (Key k : adminKeys) {
				auxAdmin = (PCUser)pm.getObjectById(PCUser.class, k);
				auxClientUser = new ClientAdminUserModel(auxAdmin.getUsername(), auxAdmin.getPassword());
				auxClientUser.setKey(KeyFactory.keyToString(k));
				admins.add(auxClientUser);
			}
		}
		catch (IllegalArgumentException ex) {
			logger.severe("Error retrieving user admin list: "+ex);
			admins = null;
		}
		catch (Exception ex) {
			logger.severe("Error retrieving user admin list: "+ex);
			admins = null;
		}
		finally {
			pm.close();
		}
		return admins;
	}
}
