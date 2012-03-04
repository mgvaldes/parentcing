package com.ing3nia.parentalcontrol.server;

import java.util.logging.ConsoleHandler;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;

import org.apache.geronimo.mail.util.SessionUtil;

import com.google.appengine.api.datastore.KeyFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.ing3nia.parentalcontrol.client.models.ClientUserModel;
import com.ing3nia.parentalcontrol.client.models.UserModel;
import com.ing3nia.parentalcontrol.client.rpc.AddAdminUserService;
import com.ing3nia.parentalcontrol.client.rpc.GetUserSessionCredentialsService;
import com.ing3nia.parentalcontrol.client.utils.EncryptionUtils;
import com.ing3nia.parentalcontrol.models.PCSession;
import com.ing3nia.parentalcontrol.models.PCUser;
import com.ing3nia.parentalcontrol.services.exceptions.SessionQueryException;
import com.ing3nia.parentalcontrol.services.utils.ServiceUtils;
import com.ing3nia.parentalcontrol.services.utils.SessionUtils;

public class GetUserSessionCredentialsServiceImpl extends RemoteServiceServlet implements GetUserSessionCredentialsService {

	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(GetUserSessionCredentialsServiceImpl.class.getName());
	
	public GetUserSessionCredentialsServiceImpl() {
		logger.addHandler(new ConsoleHandler());
	}

	@Override
	public ClientUserModel getSessionCredentials(String cookieId)
			throws IllegalArgumentException {
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
		PCSession pcsession;
		logger.info("[GetSessionCredentials] obtaining session from Cookie "+cookieId);
		try {
			pcsession = SessionUtils.getPCSessionFromCookie(pm, cookieId);
		} catch (SessionQueryException e) {
			return null;
		}
		logger.info("[GetSessionCredentials] obtaining session from user key "+pcsession.getUser());
		
		PCUser pcuser = (PCUser)pm.getObjectById(PCUser.class, pcsession.getUser());
		
		logger.info("[GetSessionCredentials] Creating usermodel from PCUser");
		ClientUserModel usermodel = new ClientUserModel();
		usermodel.setKey(KeyFactory.keyToString(pcuser.getKey()));
		usermodel.setUsername(pcuser.getUsername());
		usermodel.setPassword(pcuser.getPassword());

		logger.info("[GetSessionCredentials] Returning user model");
		return usermodel;
		
	}
}
