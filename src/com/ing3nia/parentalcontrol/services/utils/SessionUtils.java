package com.ing3nia.parentalcontrol.services.utils;

import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.appengine.api.datastore.Key;
import com.ing3nia.parentalcontrol.models.PCSession;
import com.ing3nia.parentalcontrol.models.PCUser;
import com.ing3nia.parentalcontrol.models.utils.WSStatus;
import com.ing3nia.parentalcontrol.services.exceptions.EncodingException;
import com.ing3nia.parentalcontrol.services.exceptions.NonexistingUserException;
import com.ing3nia.parentalcontrol.services.exceptions.SessionQueryException;
import com.ing3nia.parentalcontrol.services.parent.ParentTestResource;

/**
 * Class defining a series of utils methods for session handling
 * 
 * @author Ing3nia
 * @author Javier Fernandez
 * @author Maria Gabriela Valdes
 * 
 * @version 1.0
 *
 */
public class SessionUtils {
	
	private static final Logger logger = Logger
	.getLogger(ParentTestResource.class.getName());


	/**
	 * Returns the PCUser matching the provided username or null otherwise
	 */
	public static PCUser getPCUser(PersistenceManager pm, String username_param) throws SessionQueryException{
		Query query = pm.newQuery(PCUser.class);
	    query.setFilter("username == username_param");
	    query.declareParameters("String username_param");
	    query.setRange(0, 1);

	    logger.info("[SessionUtils] Finding user by username");
		try {
			List<PCUser> results = (List<PCUser>) query.execute(username_param);
			if (!results.isEmpty()) {
				logger.info("[SessionUtils] Returning found PCuser");
				return results.get(0);
			}else{
				logger.info("[SessionUtils] No user with the username "+username_param+" was found.");
				return null;
			}
		} catch (Exception e) {
			logger.info("[SessionUtils] An error ocurred while finding the PCUser by username "+ e.getMessage());
			throw new SessionQueryException();
		}  
	}
	
	/**
	 * Returns the PCUser matching the provided username and password, or null otherwise
	 */
	public static PCUser getPCUser(PersistenceManager pm, String username_param, String pass_param) throws SessionQueryException{
		Query query = pm.newQuery(PCUser.class);
	    query.setFilter("username == username_param && password == pass_param");
	    query.declareParameters("String username_param, String pass_param");
	    query.setRange(0, 1);

	    logger.info("[SessionUtils] Finding user by username and password");
		try {
			List<PCUser> results = (List<PCUser>) query.execute(username_param, pass_param);
			if (!results.isEmpty()) {
				logger.info("[SessionUtils] Returning found PCuser");
				return results.get(0);
			}else{
				logger.info("[SessionUtils] No user with the username "+username_param+", and password "+pass_param+" was found.");
				return null;
			}
		} catch (Exception e) {
			logger.info("[SessionUtils] An error ocurred while finding the PCUser by username and password "+ e.getMessage());
			throw new SessionQueryException();
		}  
	}
	
	/**
	 * Returns the PCSession matching the provided user Key or null otherwise
	 * @throws SessionQueryException 
	 */
	public static PCSession getPCSessionFromUser(PersistenceManager pm, Key userKey) throws SessionQueryException{
		
		Query query = pm.newQuery(PCSession.class);
		query.setFilter("user == userKey");
	    query.declareParameters(Key.class.getName()+" userKey");
	    query.setRange(0, 1);

	    logger.info("[SessionUtils] Finding user session by user key");
		try {
			List<PCSession> results = (List<PCSession>) query.execute(userKey);
			if (!results.isEmpty()) {
				logger.info("[SessionUtils] Returning found PCSession");
				return results.get(0);
			}else{
				logger.info("[SessionUtils] No session for the user key "+userKey+" was found.");
				return null;
			}
		} catch (Exception e) {
			logger.info("[SessionUtils] An error ocurred while finding the PCSession by user key "+ e.getMessage());
			throw new SessionQueryException();
		}  
	}
	
	/**
	 * Returns the PCSession matching the provided user Key or null otherwise
	 * @throws SessionQueryException 
	 */
	public static PCSession getPCSessionFromCookie(PersistenceManager pm, String cookie) throws SessionQueryException{
		
		Query query = pm.newQuery(PCSession.class);
		query.setFilter("cookieId == cookie");
	    query.declareParameters("String cookie");
	    query.setRange(0, 1);

	    logger.info("[SessionUtils] Finding user session by cookie");
		try {
			List<PCSession> results = (List<PCSession>) query.execute(cookie);
			if (!results.isEmpty()) {
				logger.info("[SessionUtils] Returning found PCSession");
				return results.get(0);
			}else{
				logger.info("[SessionUtils] No session for the cookie "+cookie+" was found.");
				throw new SessionQueryException();
			}
		} catch (Exception e) {
			logger.info("[SessionUtils] An error ocurred while finding the PCSession by cookie "+ e.getMessage());
			throw new SessionQueryException();
		}  
	}
	
	
	/**
	 * Calculates a unique session cookie hash
	 * @throws EncodingException 
	 */
	public static String calculateSessionCookie() throws EncodingException{
		Date d = new Date();
		String cookieString;
		String timeStr = String.valueOf(d.getTime());
		Random r = new Random();
		String rStr = String.valueOf(r.nextInt(999999));
		
		cookieString = timeStr+rStr;
		String cookie = EncryptionUtils.toMD5(cookieString);
		return cookie;
		
	}
	

	
}
