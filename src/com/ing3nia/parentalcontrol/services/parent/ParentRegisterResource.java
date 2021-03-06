package com.ing3nia.parentalcontrol.services.parent;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;


import com.google.appengine.api.datastore.Key;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ing3nia.parentalcontrol.client.models.UserModel;
import com.ing3nia.parentalcontrol.client.utils.EncryptionUtils;
import com.ing3nia.parentalcontrol.models.PCUser;
import com.ing3nia.parentalcontrol.models.utils.WSStatus;
import com.ing3nia.parentalcontrol.services.exceptions.EncodingException;
import com.ing3nia.parentalcontrol.services.exceptions.SessionQueryException;
import com.ing3nia.parentalcontrol.services.models.utils.WriteToCache;
import com.ing3nia.parentalcontrol.services.utils.ServiceUtils;
import com.ing3nia.parentalcontrol.services.utils.SessionUtils;
import com.ing3nia.parentalcontrol.services.utils.WebServiceUtils;

/**
 * This class represents a restful web service to be called from the parent's application.
 * The POST method allows a parent to register in the central system of the application, given the 
 * parent's credentials. 
 * 
 * @author Ing3nia
 * @author Javier Fernandez
 * @author Maria Gabriela Valdes
 * 
 * @version 1.0
 *
 *
 * Test query: 
 * {'usr':'jhonwayne','pass':'imjhon','email':'jhon@gmail.com','name':'John Wayne'}
 */
@Path("register")
public class ParentRegisterResource {
	
	private static final Logger logger = Logger.getLogger(ParentRegisterResource.class.getName());
	public String NEW_WS = "new";
    public String OLD_WS = "old";
    public String ACTUAL = "new";	
	
	public ParentRegisterResource() {
		//logger.addHandler(new ConsoleHandler());
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	
	/**
	 * Registers a parent user in the central system
	 */
	public Response post(String msg) {
	      if (ACTUAL.equals(NEW_WS)) {
	            return newWS(msg);
	        }
	        return oldWS(msg);
	}


	public Response newWS(String msg){
		logger.info("[Parent Register] Processing register request");
		
		//creating global variables
		Gson gson = new Gson();
		ResponseBuilder rbuilder;
		String encrypted_pass;
		
		logger.info("[Parent Register] Parsing request message");
		//parsing json message from request
		Type userModelType = new TypeToken<UserModel>(){}.getType();
		UserModel ut;
		try{
		logger.info("[Parent Register] Transforming json object "+msg);
		 ut = gson.fromJson(msg, userModelType);
		}catch(Exception e){
			logger.warning("[ParentRegisterResource] UserModel couldn't be created from message "+WSStatus.INTERNAL_SERVICE_ERROR.getMsg());
			rbuilder = Response.ok(WSStatus.INTERNAL_SERVICE_ERROR.getStatusAsJson().toString(), MediaType.APPLICATION_JSON);
			WebServiceUtils.setUTF8Encoding(WebServiceUtils.JSON_CONTENT_TYPE, rbuilder);
			return rbuilder.build();
		}
		
		//TODO Validacion de parametros (existencia previa, mal formados,etc)

		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
		
		//checking if entity exists first
		logger.info("[Parent Register] Veryfing if another user with the same username and email exists");
		String username_param = ut.getUsername();

		PCUser user;
		try {
			user = SessionUtils.getPCUser(pm, username_param);
		} catch (SessionQueryException e2) {
			logger.warning("[Parent Register] An error ocurred while searching for username and email. "+e2.getMessage());
			rbuilder = Response.ok(WSStatus.INTERNAL_SERVICE_ERROR.getStatusAsJson().toString(), MediaType.APPLICATION_JSON);
			WebServiceUtils.setUTF8Encoding(WebServiceUtils.JSON_CONTENT_TYPE, rbuilder);
			return rbuilder.build();
		}
		
		if(user!=null){
			logger.info("[Parent Register] A user already exists for the provided credentials");
			rbuilder = Response.ok(WSStatus.PREEXISTING_USER
					.getStatusAsJson().toString(),
					MediaType.APPLICATION_JSON);
			WebServiceUtils.setUTF8Encoding(WebServiceUtils.JSON_CONTENT_TYPE, rbuilder);
			return rbuilder.build();
		}

		logger.info("[Parent Register] Creating model object from message");
		//register model in datastore
		
		try {
			logger.info("[Parent Register] Encrypting password in MD5");
			encrypted_pass = EncryptionUtils.toMD5(ut.getPass());
		} catch (EncodingException e1) {
			logger.severe("[Parent Register] An error occurred when encrypting the supplied password");
			rbuilder = Response.ok(WSStatus.INVALID_PASSWORD_DATA.getStatusAsJson().toString(), MediaType.APPLICATION_JSON);
			WebServiceUtils.setUTF8Encoding(WebServiceUtils.JSON_CONTENT_TYPE, rbuilder);
			return rbuilder.build();
		}
		
		PCUser pcuser = new PCUser();
		pcuser.setName(ut.getName());
		pcuser.setEmail(ut.getEmail());
		pcuser.setPassword(encrypted_pass);
		pcuser.setSmartphones(new ArrayList<Key>()); //must be added from child app
		pcuser.setUsername(ut.getUsername());
		
		logger.info("[Parent Register] Persisting new user in datastore");
		try {
			pm.makePersistent(pcuser);
		} catch(Exception e){
			logger.warning("[ParentRegister] An error ocurred while persisting the user data. "+e.getMessage());
			rbuilder = Response.ok(WSStatus.INTERNAL_SERVICE_ERROR.getStatusAsJson().toString(), MediaType.APPLICATION_JSON);
			WebServiceUtils.setUTF8Encoding(WebServiceUtils.JSON_CONTENT_TYPE, rbuilder);
			return rbuilder.build();
		} 
		finally {
			pm.close();
		}
		
		logger.info("[Parent Register Cache] Writing User to cache");
		WriteToCache.writeUserToCache(pcuser);
		
		// ok response. user succesfully registered
		logger.info("[Parent Register] Ok Response. User succesfully registered");
		rbuilder = Response.ok(WSStatus.OK.getStatusAsJson().toString(), MediaType.APPLICATION_JSON);
		WebServiceUtils.setUTF8Encoding(WebServiceUtils.JSON_CONTENT_TYPE, rbuilder);
		return rbuilder.build();
	}
	
	
	public Response oldWS(String msg){
		logger.info("[Parent Register] Processing register request");
		
		//creating global variables
		Gson gson = new Gson();
		ResponseBuilder rbuilder;
		String encrypted_pass;
		
		logger.info("[Parent Register] Parsing request message");
		//parsing json message from request
		Type userModelType = new TypeToken<UserModel>(){}.getType();
		UserModel ut;
		try{
		logger.info("[Parent Register] Transforming json object "+msg);
		 ut = gson.fromJson(msg, userModelType);
		}catch(Exception e){
			logger.warning("[ParentRegisterResource] UserModel couldn't be created from message "+WSStatus.INTERNAL_SERVICE_ERROR.getMsg());
			rbuilder = Response.ok(WSStatus.INTERNAL_SERVICE_ERROR.getStatusAsJson().toString(), MediaType.APPLICATION_JSON);
			WebServiceUtils.setUTF8Encoding(WebServiceUtils.JSON_CONTENT_TYPE, rbuilder);
			return rbuilder.build();
		}
		
		//TODO Validacion de parametros (existencia previa, mal formados,etc)

		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
		
		//checking if entity exists first
		logger.info("[Parent Register] Veryfing if another user with the same username and email exists");
		String username_param = ut.getUsername();

		PCUser user;
		try {
			user = SessionUtils.getPCUser(pm, username_param);
		} catch (SessionQueryException e2) {
			logger.warning("[Parent Register] An error ocurred while searching for username and email. "+e2.getMessage());
			rbuilder = Response.ok(WSStatus.INTERNAL_SERVICE_ERROR.getStatusAsJson().toString(), MediaType.APPLICATION_JSON);
			WebServiceUtils.setUTF8Encoding(WebServiceUtils.JSON_CONTENT_TYPE, rbuilder);
			return rbuilder.build();
		}
		
		if(user!=null){
			logger.info("[Parent Register] A user already exists for the provided credentials");
			rbuilder = Response.ok(WSStatus.PREEXISTING_USER
					.getStatusAsJson().toString(),
					MediaType.APPLICATION_JSON);
			WebServiceUtils.setUTF8Encoding(WebServiceUtils.JSON_CONTENT_TYPE, rbuilder);
			return rbuilder.build();
		}

		logger.info("[Parent Register] Creating model object from message");
		//register model in datastore
		
		try {
			logger.info("[Parent Register] Encrypting password in MD5");
			encrypted_pass = EncryptionUtils.toMD5(ut.getPass());
		} catch (EncodingException e1) {
			logger.severe("[Parent Register] An error occurred when encrypting the supplied password");
			rbuilder = Response.ok(WSStatus.INVALID_PASSWORD_DATA.getStatusAsJson().toString(), MediaType.APPLICATION_JSON);
			WebServiceUtils.setUTF8Encoding(WebServiceUtils.JSON_CONTENT_TYPE, rbuilder);
			return rbuilder.build();
		}
		
		PCUser pcuser = new PCUser();
		pcuser.setName(ut.getName());
		pcuser.setEmail(ut.getEmail());
		pcuser.setPassword(encrypted_pass);
		pcuser.setSmartphones(new ArrayList<Key>()); //must be added from child app
		pcuser.setUsername(ut.getUsername());

		logger.info("[Parent Register] Persisting new user in datastore");
		try {
			pm.makePersistent(pcuser);
		} catch(Exception e){
			logger.warning("[ParentRegister] An error ocurred while persisting the user data. "+e.getMessage());
			rbuilder = Response.ok(WSStatus.INTERNAL_SERVICE_ERROR.getStatusAsJson().toString(), MediaType.APPLICATION_JSON);
			WebServiceUtils.setUTF8Encoding(WebServiceUtils.JSON_CONTENT_TYPE, rbuilder);
			return rbuilder.build();
		} 
		finally {
			pm.close();
		}
		
		// ok response. user succesfully registered
		logger.info("[Parent Register] Ok Response. User succesfully registered");
		rbuilder = Response.ok(WSStatus.OK.getStatusAsJson().toString(), MediaType.APPLICATION_JSON);
		WebServiceUtils.setUTF8Encoding(WebServiceUtils.JSON_CONTENT_TYPE, rbuilder);
		return rbuilder.build();
	}
}
