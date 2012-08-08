package com.ing3nia.parentalcontrol.server;

import java.util.ArrayList;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.appengine.api.memcache.MemcacheService.IdentifiableValue;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.ing3nia.parentalcontrol.client.models.ClientAdminUserModel;
import com.ing3nia.parentalcontrol.client.models.UserModel;
import com.ing3nia.parentalcontrol.client.models.cache.UserCacheParams;
import com.ing3nia.parentalcontrol.client.rpc.AdminUserListService;
import com.ing3nia.parentalcontrol.models.PCUser;
import com.ing3nia.parentalcontrol.services.utils.ServiceUtils;

public class AdminUserListServiceImpl extends RemoteServiceServlet implements
		AdminUserListService {

	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger
			.getLogger(AdminUserListServiceImpl.class.getName());

	public String NEW_WS = "new";
	public String OLD_WS = "old";
	public String ACTUAL = NEW_WS;

	public AdminUserListServiceImpl() {
	}

	@Override
	public ArrayList<ClientAdminUserModel> getAdminUserList(String userKey) {

		if (ACTUAL.equals(NEW_WS)) {
			return getAdminUserListNEW(userKey);
		}

		return getAdminUserListOLD(userKey);
	}

	public ArrayList<ClientAdminUserModel> getAdminUserListOLD(String userKey) {

		ArrayList<ClientAdminUserModel> admins = new ArrayList<ClientAdminUserModel>();
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();

		try {
			Key key = KeyFactory.stringToKey(userKey);

			PCUser user = (PCUser) pm.getObjectById(PCUser.class, key);
			ArrayList<Key> adminKeys = user.getAdmins();
			PCUser auxAdmin;
			ClientAdminUserModel auxClientUser;

			for (Key k : adminKeys) {
				auxAdmin = (PCUser) pm.getObjectById(PCUser.class, k);
				auxClientUser = new ClientAdminUserModel(
						auxAdmin.getUsername(), auxAdmin.getPassword());
				auxClientUser.setKey(KeyFactory.keyToString(k));
				admins.add(auxClientUser);
			}
		} catch (IllegalArgumentException ex) {
			logger.severe("Error retrieving user admin list: " + ex);
			admins = null;
		} catch (Exception ex) {
			logger.severe("Error retrieving user admin list: " + ex);
			admins = null;
		} finally {
			pm.close();
		}
		return admins;
	}

	public ArrayList<ClientAdminUserModel> getAdminUserListNEW(String userKey) {

		ArrayList<ClientAdminUserModel> admins = new ArrayList<ClientAdminUserModel>();
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();

		MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();

		logger.info("[AdminUserListServer] Searching for admin list in cache");
		IdentifiableValue ident = syncCache
				.getIdentifiable(UserCacheParams.USER + userKey
						+ UserCacheParams.ADMIN_LIST);

		if (ident == null) {
			logger.info("[AdminUserListServer] Admin list not found in cache, Getting from DS");
			try {
				Key key = KeyFactory.stringToKey(userKey);

				PCUser user = (PCUser) pm.getObjectById(PCUser.class, key);
				ArrayList<Key> adminKeys = user.getAdmins();
				PCUser auxAdmin;
				ClientAdminUserModel auxClientUser;

				for (Key k : adminKeys) {
					auxAdmin = (PCUser) pm.getObjectById(PCUser.class, k);
					auxClientUser = new ClientAdminUserModel(
							auxAdmin.getUsername(), auxAdmin.getPassword());
					auxClientUser.setKey(KeyFactory.keyToString(k));
					admins.add(auxClientUser);
				}
			} catch (IllegalArgumentException ex) {
				logger.severe("Error retrieving user admin list: " + ex);
				admins = null;
			} catch (Exception ex) {
				logger.severe("Error retrieving user admin list: " + ex);
				admins = null;
			} finally {
				pm.close();
			}
		} else {
			logger.info("[AdminUserListServer] Admin list Found in Cache, iterating list");
			ArrayList<UserModel> adminList = (ArrayList<UserModel>) ident.getValue();
			for(UserModel adminUser : adminList){
				ClientAdminUserModel auxClientUser;
					auxClientUser = new ClientAdminUserModel(adminUser.getUsername(), adminUser.getPass());
					auxClientUser.setKey(adminUser.getKey());
					admins.add(auxClientUser);
			}
		}
		return admins;
	}
}
