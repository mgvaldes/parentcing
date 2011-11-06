package com.ing3nia.parentalcontrol.services.parent;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import com.google.appengine.api.datastore.Email;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.reflect.TypeToken;
import com.ing3nia.parentalcontrol.models.PCSession;
import com.ing3nia.parentalcontrol.models.PCSmartphone;
import com.ing3nia.parentalcontrol.models.PCUser;
import com.ing3nia.parentalcontrol.models.utils.WSStatus;
import com.ing3nia.parentalcontrol.services.exceptions.EncodingException;
import com.ing3nia.parentalcontrol.services.exceptions.SessionQueryException;
import com.ing3nia.parentalcontrol.services.models.UserModel;
import com.ing3nia.parentalcontrol.services.utils.EncryptionUtils;
import com.ing3nia.parentalcontrol.services.utils.ServiceUtils;
import com.ing3nia.parentalcontrol.services.utils.SessionUtils;

/**
 * This class represents a restful web service to be called from the parent's application.
 * The POST method allows a parent to login in the central system of the application, given the 
 * its credentials. 
 * 
 * @author Ing3nia
 * @author Javier Fernandez
 * @author Maria Gabriela Valdes
 * 
 * @version 1.0
 *
 */
@Path("login")
public class ParentLoginResource {
	
	private static final Logger logger = Logger
	.getLogger(ParentTestResource.class.getName());

	
	public ParentLoginResource() {
		logger.addHandler(new ConsoleHandler());
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	
	/**
	 * Logs in a parent user in the central system
	 */
	public Response post(String msg) {
		
		logger.info("[Parent Login] Processing login request");
	
		//creating global variables
		Gson gson = new Gson();
		ResponseBuilder rbuilder;
		
		logger.info("[Parent Login] Parsing request message");
		//parsing json message from request
		Type userModelType = new TypeToken<UserModel>(){}.getType();
		UserModel ut;
		try{
		logger.info("[Parent Login] Transforming json object "+msg);
		 ut = gson.fromJson(msg, userModelType);
		}catch(Exception e){
			logger.warning("[Parent Login] UserModel couldn't be created from message "+WSStatus.INTERNAL_SERVICE_ERROR.getMsg());
			rbuilder = Response.ok(WSStatus.INTERNAL_SERVICE_ERROR.getStatusAsJson().toString(), MediaType.APPLICATION_JSON);
			return rbuilder.build();
		}
		
		//TODO Validacion de parametros (existencia previa, mal formados,etc)

		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
		
		//checking if entity exists first
		logger.info("[Parent Login] Veryfing if a user with the given username and password exists");
		String username_param = ut.getUsername();
		String pass_param = null;
		try {
			logger.info("[Parent Login] Encrypting password in MD5");
			pass_param = EncryptionUtils.toMD5(ut.getPassword());
		} catch (EncodingException e1) {
			logger.severe("[Parent Login] An error occurred when encrypting the supplied password");
			rbuilder = Response.ok(WSStatus.INVALID_PASSWORD_DATA.getStatusAsJson().toString(), MediaType.APPLICATION_JSON);
			return rbuilder.build();
		}
		
		PCUser user;
		try {
			user = SessionUtils.getPCUser(pm, username_param,pass_param);
		} catch (SessionQueryException e2) {
			logger.warning("[Parent Login] An error ocurred while searching for username and email. "+e2.getMessage());
			rbuilder = Response.ok(WSStatus.INTERNAL_SERVICE_ERROR.getStatusAsJson().toString(), MediaType.APPLICATION_JSON);
			return rbuilder.build();
		}
		
		if(user==null){
			logger.info("[Parent Login] No user exists for the provided credentials");
			rbuilder = Response.ok(WSStatus.NONEXISTING_USER
					.getStatusAsJson().toString(),
					MediaType.APPLICATION_JSON);
			return rbuilder.build();
		}

		logger.info("[Parent Login] Looking for preexisting session");
		PCSession session = null;
		try {
			session = SessionUtils.getPCSessionFromUser(pm, user.getKey());
			
		} catch (SessionQueryException e2) {
			logger.warning("[Parent Login] An error occurred while looking for session from user key");
			rbuilder = Response.ok(WSStatus.INTERNAL_SERVICE_ERROR.getStatusAsJson().toString(), MediaType.APPLICATION_JSON);
			return rbuilder.build();
		}
		
		if(session!=null){
			logger.info("[Parent Login] Updating last activity from found session");
			//updating last activity
			session.setLastUpdate(new Date());
			pm.close();
			
			// ok response. user succesfully registered
			logger.info("[Parent Login] Ok Response. User succesfully logged in");
			JsonObject okResponse = WSStatus.OK.getStatusAsJson();
			okResponse.addProperty("cid", session.getCookieId());
			rbuilder = Response.ok(okResponse.toString(), MediaType.APPLICATION_JSON);
			return rbuilder.build();
		}
		    
		logger.info("[Parent Login] Creating session object");
		//register model in datastore
		session =  new PCSession();
		session.setUser(user.getKey());
		session.setLastUpdate(new Date());
		try {
			session.setCookieId(SessionUtils.calculateSessionCookie());
		} catch (EncodingException e1) {
			logger.warning("[Parent Login] An error ocurred while creating the cookie hash.");
			rbuilder = Response.ok(WSStatus.INTERNAL_SERVICE_ERROR.getStatusAsJson().toString(), MediaType.APPLICATION_JSON);
			return rbuilder.build();
		}

		logger.info("[Parent Login] Persisting new session in datastore");
		try {
			pm.makePersistent(session);
		} catch(Exception e){
			logger.warning("[Parent Login] An error ocurred while persisting the session data. "+e.getMessage());
			rbuilder = Response.ok(WSStatus.INTERNAL_SERVICE_ERROR.getStatusAsJson().toString(), MediaType.APPLICATION_JSON);
			return rbuilder.build();
		} 
		finally {
			pm.close();
		}
		
		// ok response. user succesfully logged in
		logger.info("[Parent Login] Ok Response. User succesfully logged in");
		JsonObject okResponse = WSStatus.OK.getStatusAsJson();
		okResponse.addProperty("cid", session.getCookieId());
		rbuilder = Response.ok(okResponse.toString(), MediaType.APPLICATION_JSON);
		return rbuilder.build();
	}
}
