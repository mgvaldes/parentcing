package com.ing3nia.parentalcontrol.services.parent;

import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.appengine.api.memcache.MemcacheService.IdentifiableValue;
//import com.google.appengine.api.datastore.Query;
import com.google.gson.JsonObject;
import com.ing3nia.parentalcontrol.client.models.UserModel;
import com.ing3nia.parentalcontrol.client.models.cache.SessionCacheModel;
import com.ing3nia.parentalcontrol.client.models.cache.SmartphoneCacheModel;
import com.ing3nia.parentalcontrol.client.models.cache.SmartphoneCacheParams;
import com.ing3nia.parentalcontrol.client.models.cache.UserCacheParams;
import com.ing3nia.parentalcontrol.models.PCSession;
import com.ing3nia.parentalcontrol.models.PCSmartphone;
import com.ing3nia.parentalcontrol.models.PCUser;
import com.ing3nia.parentalcontrol.models.utils.WSStatus;
import com.ing3nia.parentalcontrol.services.child.RegisterSmartphoneResource;
import com.ing3nia.parentalcontrol.services.exceptions.SessionQueryException;
import com.ing3nia.parentalcontrol.services.models.utils.WriteToCache;
import com.ing3nia.parentalcontrol.services.utils.ServiceUtils;
import com.ing3nia.parentalcontrol.services.utils.SessionUtils;
import com.ing3nia.parentalcontrol.services.utils.SmartphoneUtils;
import com.ing3nia.parentalcontrol.services.utils.WebServiceUtils;

/**
 * This class represents a restful web service to be called from the parent's application.
 * The POST method allows a parent to get detailed information 
 * 
 * @author Ing3nia
 * @author Javier Fernandez
 * @author Maria Gabriela Valdes
 * 
 * @version 1.0
 *
 */
@Path("smartphone-details")
public class ParentSmartphoneDetails {
	private static final Logger logger = Logger
	.getLogger(ParentSmartphoneDetails.class.getName());

	public String NEW_WS = "new";
    public String OLD_WS = "old";
    public String ACTUAL = NEW_WS;
	
	
	public ParentSmartphoneDetails(){
		//logger.addHandler(new ConsoleHandler());
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	/**
	 * Returns detailed information from a given children smartphone of a valid user
	 */
	public Response get(@QueryParam("cid") String cookie, @QueryParam("smid") String smid) {
        
		if (ACTUAL.equals(NEW_WS)) {
	            return newWS(cookie, smid);
	    }
	    return oldWS(cookie, smid);
		
	}
	
	public Response oldWS(String cookie, String smid){
		
		logger.info("[Parent Smartphone Details] Obtaining session from request");
		
		//creating global variables
		ResponseBuilder rbuilder;
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
		PCSession session = null;
		
		try {
			 session = SessionUtils.getPCSessionFromCookie(pm, cookie);
		} catch (SessionQueryException e) {
			logger.warning("[Parent Smartphone Details] No session exists for the given cookie. "+e.getMessage());
			rbuilder = Response.ok(WSStatus.NONEXISTING_SESSION.getStatusAsJson().toString(), MediaType.APPLICATION_JSON);
			WebServiceUtils.setUTF8Encoding(WebServiceUtils.JSON_CONTENT_TYPE, rbuilder);
			return rbuilder.build();
		}
		
		//TODO verify if session is active
		
		logger.info("[Parent Smartphone Details] Session found. Getting User from session");
		PCUser user = pm.getObjectById(PCUser.class, session.getUser());
		
		if(user==null){
			logger.severe("[Parent Smartphone Details] No user associated with a valid session");
			rbuilder = Response.ok(WSStatus.NONEXISTING_USER.getStatusAsJson().toString(), MediaType.APPLICATION_JSON);
			WebServiceUtils.setUTF8Encoding(WebServiceUtils.JSON_CONTENT_TYPE, rbuilder);
			return rbuilder.build();
		}
		pm.close();
		pm = ServiceUtils.PMF.getPersistenceManager();
		
		//TODO check if smartphone corresponds to USER
		
		// get smartphone from provided key
		logger.info("[Parent Smartphone Details] Obtaining smartphone from provided key "+smid);
		PCSmartphone smartphone;
		try{
			 smartphone = (PCSmartphone)pm.getObjectById(PCSmartphone.class, KeyFactory.stringToKey(smid));
			 
		} catch(IllegalArgumentException e){
			rbuilder = Response.ok(WSStatus.INVALID_SMARTPHONE.getStatusAsJson().toString(), MediaType.APPLICATION_JSON);
			WebServiceUtils.setUTF8Encoding(WebServiceUtils.JSON_CONTENT_TYPE, rbuilder);
			return rbuilder.build();
		}
		
		if(smartphone == null){
			logger.severe("[Parent Smartphone Details] No smartphone found from the given key "+smid);
			rbuilder = Response.ok(WSStatus.INVALID_SMARTPHONE.getStatusAsJson().toString(), MediaType.APPLICATION_JSON);
			WebServiceUtils.setUTF8Encoding(WebServiceUtils.JSON_CONTENT_TYPE, rbuilder);
			return rbuilder.build();
		}
		pm.close();
	
		logger.info("SMARTPHONE RULES: "+smartphone.getRules());
		logger.info("SMARTPHONE PROPS: "+smartphone.getProperties());
		
		logger.info("[Parent Smartphone Details] Obtaining smarpthone details");
		JsonObject smartphoneInfo = SmartphoneUtils.getSmartphoneDetails(smartphone);
		
		JsonObject responseMsg = WSStatus.OK.getStatusAsJson();
		responseMsg.add("smartphone", smartphoneInfo);
		
		rbuilder = Response.ok(responseMsg.toString(), MediaType.APPLICATION_JSON);
		WebServiceUtils.setUTF8Encoding(WebServiceUtils.JSON_CONTENT_TYPE, rbuilder);
		return rbuilder.build();
	}
	

	public Response newWS(String cookie, String smid){
		
		logger.info("[Parent Smartphone Details] Obtaining session from request");
		
		//creating global variables
		ResponseBuilder rbuilder;
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
		PCSession session = null;
		
	    MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
		SessionCacheModel sessionCacheModel = null;
	    IdentifiableValue ident = syncCache.getIdentifiable(UserCacheParams.SESSION+cookie);
	    
		if (ident == null) {

			try {
				session = SessionUtils.getPCSessionFromCookie(pm, cookie);
			} catch (SessionQueryException e) {
				logger.warning("[Parent Smartphone Details] No session exists for the given cookie. "
						+ e.getMessage());
				rbuilder = Response.ok(WSStatus.NONEXISTING_SESSION.getStatusAsJson().toString(),MediaType.APPLICATION_JSON);
				WebServiceUtils.setUTF8Encoding(WebServiceUtils.JSON_CONTENT_TYPE, rbuilder);
				return rbuilder.build();
			}
		} else {
			sessionCacheModel = (SessionCacheModel) ident.getValue();
			logger.info("Session: " + cookie + " found in cache");
		}
		
		
		//TODO verify if session is active
		
		PCUser user = null;

		if(sessionCacheModel == null){
			logger.info("[Parent Smartphone Details] Session found. Getting User from session");
			user = pm.getObjectById(PCUser.class, session.getUser());
		}
		
		if(user==null && sessionCacheModel.getUserModel() == null){
			logger.severe("[Parent Smartphone Details] No user associated with a valid session");
			rbuilder = Response.ok(WSStatus.NONEXISTING_USER.getStatusAsJson().toString(), MediaType.APPLICATION_JSON);
			WebServiceUtils.setUTF8Encoding(WebServiceUtils.JSON_CONTENT_TYPE, rbuilder);
			return rbuilder.build();
		}
		pm.close();
		pm = ServiceUtils.PMF.getPersistenceManager();
		
		//TODO check if smartphone corresponds to USER
		
		// get smartphone from provided key
		logger.info("[Parent Smartphone Details] Obtaining smartphone from provided key "+smid);
		PCSmartphone smartphone = null;
		SmartphoneCacheModel smphCacheModel = null;
		
		logger.info("[Parent Smartphone Details - Cache] Trying to get smartphone from cache first");
		ident = syncCache.getIdentifiable(SmartphoneCacheParams.SMARTPHONE+smid);
	    if(ident==null){
	    	logger.info("Smartphone: "+smid+" not in cache. Getting from datastore");
	    	
			try{
				 smartphone = (PCSmartphone)pm.getObjectById(PCSmartphone.class, KeyFactory.stringToKey(smid));
				 
			} catch(IllegalArgumentException e){
				rbuilder = Response.ok(WSStatus.INVALID_SMARTPHONE.getStatusAsJson().toString(), MediaType.APPLICATION_JSON);
				WebServiceUtils.setUTF8Encoding(WebServiceUtils.JSON_CONTENT_TYPE, rbuilder);
				return rbuilder.build();
			}
			
	    	logger.info("Setting smartphone to cache and continuing");
	    	WriteToCache.writeSmartphoneToCache(smartphone);
	    }else{
	    	smphCacheModel = (SmartphoneCacheModel) ident.getValue();
	    	logger.info("Smartphone: "+smid+" found in cache");
	    }
		
		logger.info("[Parent Smartphone Details] Obtaining smarpthone details");
		JsonObject smartphoneInfo = null;
	    
	    if(smphCacheModel == null && smartphone == null){
			logger.severe("[Parent Smartphone Details] No smartphone found from the given key "+smid);
			rbuilder = Response.ok(WSStatus.INVALID_SMARTPHONE.getStatusAsJson().toString(), MediaType.APPLICATION_JSON);
			WebServiceUtils.setUTF8Encoding(WebServiceUtils.JSON_CONTENT_TYPE, rbuilder);
			return rbuilder.build();
	    }
		
	    smartphoneInfo = SmartphoneUtils.getSmartphoneDetailsCache(smid, smartphone);

		pm.close();

		
		JsonObject responseMsg = WSStatus.OK.getStatusAsJson();
		responseMsg.add("smartphone", smartphoneInfo);
		
		rbuilder = Response.ok(responseMsg.toString(), MediaType.APPLICATION_JSON);
		WebServiceUtils.setUTF8Encoding(WebServiceUtils.JSON_CONTENT_TYPE, rbuilder);
		return rbuilder.build();
	}	
}