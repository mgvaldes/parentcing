package com.ing3nia.parentalcontrol.services.parent;

import java.lang.reflect.Type;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheService.IdentifiableValue;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ing3nia.parentalcontrol.client.models.ModificationModel;
import com.ing3nia.parentalcontrol.client.models.cache.SmartphoneCacheModel;
import com.ing3nia.parentalcontrol.client.models.cache.SmartphoneCacheParams;
import com.ing3nia.parentalcontrol.models.PCSession;
import com.ing3nia.parentalcontrol.models.PCSmartphone;
import com.ing3nia.parentalcontrol.models.PCUser;
import com.ing3nia.parentalcontrol.models.utils.WSStatus;
import com.ing3nia.parentalcontrol.services.exceptions.ModificationParsingException;
import com.ing3nia.parentalcontrol.services.exceptions.SessionQueryException;
import com.ing3nia.parentalcontrol.services.models.ParentModificationsModel;
import com.ing3nia.parentalcontrol.services.models.utils.WriteToCache;
import com.ing3nia.parentalcontrol.services.utils.ModificationUtils;
import com.ing3nia.parentalcontrol.services.utils.ServiceUtils;
import com.ing3nia.parentalcontrol.services.utils.SessionUtils;
import com.ing3nia.parentalcontrol.services.utils.WebServiceUtils;

/**
 * This class represents a restful web service to be called from the parent's application.
 * The POST method allows a parent to sent the modifications made over a children smartphone configuration
 * 
 * @author Ing3nia
 * @author Javier Fernandez
 * @author Maria Gabriela Valdes
 * 
 * @version 1.0
 *
 */
@Path("parent-mod")
public class ParentSmartphoneModifications {
	
	private static final Logger logger = Logger.getLogger(ParentSmartphoneModifications.class.getName());
	public String NEW_WS = "new";
	public String OLD_WS = "old";
	public String ACTUAL = NEW_WS;

	public ParentSmartphoneModifications(){
		//logger.addHandler(new ConsoleHandler());
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	/**
	 * Processes the modifications made by the parent
	 */
	public Response post(String msg) {
		if (ACTUAL.equals(NEW_WS)) {
			return newWS(msg);
		}
		
		return oldWS(msg);
	}
	
	public Response newWS(String msg) {
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
		PCSession session = null;
		ResponseBuilder rbuilder = null;
		Gson gson = new Gson();
		
		logger.info("[Parent Modifications - Cache Version] Parsing modifications request message");
		//parsing json message from request
		Type modificationType = new TypeToken<ParentModificationsModel>(){}.getType();
		ParentModificationsModel mt;
		
		try {
			logger.info("[Parent Modifications - Cache Version] Transforming json object " + msg);
			mt = gson.fromJson(msg, modificationType);
		} 
		catch (Exception e) {
			logger.warning("[Parent Modifications - Cache Version] ModificationsModel couldn't be created from message " + WSStatus.INTERNAL_SERVICE_ERROR.getMsg());
			rbuilder = Response.ok(WSStatus.INTERNAL_SERVICE_ERROR.getStatusAsJson().toString(), MediaType.APPLICATION_JSON);
			WebServiceUtils.setUTF8Encoding(WebServiceUtils.JSON_CONTENT_TYPE, rbuilder);
			return rbuilder.build();
		}
		
		String cookie;
		String smartphoneId;
		
		if (mt.getCid() != null && mt.getSmid() != null) {
			cookie = mt.getCid();
			smartphoneId = mt.getSmid();
		}
		else {
			logger.warning("[Parent Modifications - Cache Version] The modifications parameters are not valid "+WSStatus.INVALID_DATA.getMsg());
			rbuilder = Response.ok(WSStatus.INVALID_DATA.getStatusAsJson().toString(), MediaType.APPLICATION_JSON);
			WebServiceUtils.setUTF8Encoding(WebServiceUtils.JSON_CONTENT_TYPE, rbuilder);
			return rbuilder.build();
		}
		
		logger.info("[Parent Modifications - Cache Version] Finding user session from cookie");
		
		//TODO Cambiar manejo de sesion contra el cache
		//------------------------------------------------------------------------------
		try {
			 session = SessionUtils.getPCSessionFromCookie(pm, cookie);
		} 
		catch (SessionQueryException e) {
			logger.warning("[Parent Modifications - Cache Version] No session exists for the given cookie. "+e.getMessage());
			rbuilder = Response.ok(WSStatus.NONEXISTING_SESSION.getStatusAsJson().toString(), MediaType.APPLICATION_JSON);
			WebServiceUtils.setUTF8Encoding(WebServiceUtils.JSON_CONTENT_TYPE, rbuilder);
			return rbuilder.build();
		}
		
		//TODO verify if session is active		
		logger.info("[Parent Modifications - Cache Version] Session found. Getting User from session");
		PCUser user = pm.getObjectById(PCUser.class, session.getUser());
		
		if (user == null) {
			logger.severe("[Parent Modifications - Cache Version] No user associated with a valid session");
			rbuilder = Response.ok(WSStatus.NONEXISTING_USER.getStatusAsJson().toString(), MediaType.APPLICATION_JSON);
			WebServiceUtils.setUTF8Encoding(WebServiceUtils.JSON_CONTENT_TYPE, rbuilder);
			return rbuilder.build();
		}
		
		pm.close();
		//------------------------------------------------------------------------------
		
		PersistenceManager pmMod = ServiceUtils.PMF.getPersistenceManager();
		// Using the synchronous cache
	    MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();	    
		
		//TODO check if smartphone corresponds to USER
		
		// Get smartphone from provided key
		logger.info("[Parent Modifications - Cache Version] Obtaining smartphone from provided key " + smartphoneId);
		PCSmartphone smartphone = (PCSmartphone)pmMod.getObjectById(PCSmartphone.class, KeyFactory.stringToKey(smartphoneId));
		String smartphoneCacheKey = SmartphoneCacheParams.SMARTPHONE + smartphoneId;
		IdentifiableValue cacheIdentSmartphone = (IdentifiableValue) syncCache.getIdentifiable(smartphoneCacheKey);
		SmartphoneCacheModel cacheSmartphone = null;
		
		if (smartphone == null) {
			logger.severe("[Parent Modifications - Cache Version] No smartphone found from the given key "+smartphoneId);
			rbuilder = Response.ok(WSStatus.INVALID_SMARTPHONE.getStatusAsJson().toString(), MediaType.APPLICATION_JSON);
			WebServiceUtils.setUTF8Encoding(WebServiceUtils.JSON_CONTENT_TYPE, rbuilder);
			return rbuilder.build();
		}
		
		if (cacheIdentSmartphone == null) {
			logger.info("[Parent Modifications - Cache Version] SmartphoneCacheModel not in cache. Saving it!");
			cacheSmartphone = WriteToCache.writeSmartphoneToCache(smartphone);
			
			//cacheIdentSmartphone = (IdentifiableValue) syncCache.getIdentifiable(smartphoneCacheKey);
			//cacheSmartphone = (SmartphoneCacheModel) cacheIdentSmartphone.getValue();
		}
		else {
			logger.info("[Parent Modifications - Cache Version] Obtaining SmartphoneCacheModel from cache.");
			cacheSmartphone = (SmartphoneCacheModel) cacheIdentSmartphone.getValue();
		}
		
		logger.info("[Parent Modifications - Cache Version] Getting Modifications");
		ModificationModel modifications = mt.getModifications();
		
		if (modifications == null) {
			logger.info("[Parent Modifications - Cache Version] Modifications are null, no change to commit");
			rbuilder = Response.ok(WSStatus.OK.getStatusAsJson().toString(), MediaType.APPLICATION_JSON);
			WebServiceUtils.setUTF8Encoding(WebServiceUtils.JSON_CONTENT_TYPE, rbuilder);
			return rbuilder.build();
		}
		
		logger.info("[Parent Modifications - Cache Version] Processing Parent Modifications");
		
		try {
			ModificationUtils.ProcessParentModificationsNEW(pmMod, syncCache, smartphone, modifications, cacheSmartphone);
		} 
		catch (ModificationParsingException e) {
			logger.severe("[Parent Modifications - Cache Version] An error occurred when processing modifications "+e.getMessage());
			rbuilder = Response.ok(WSStatus.INTERNAL_SERVICE_ERROR.getStatusAsJson().toString(), MediaType.APPLICATION_JSON);
			WebServiceUtils.setUTF8Encoding(WebServiceUtils.JSON_CONTENT_TYPE, rbuilder);
			return rbuilder.build();
		} 
		catch (IllegalArgumentException e) {
			logger.severe("[Parent Modifications - Cache Version] An error occurred when processing modifications "+e.getMessage());
			rbuilder = Response.ok(WSStatus.INTERNAL_SERVICE_ERROR.getStatusAsJson().toString(), MediaType.APPLICATION_JSON);
			WebServiceUtils.setUTF8Encoding(WebServiceUtils.JSON_CONTENT_TYPE, rbuilder);
			return rbuilder.build();
		} 
		catch (SessionQueryException e) {
			logger.severe("[Parent Modifications - Cache Version] An error occurred when processing modifications "+e.getMessage());
			rbuilder = Response.ok(WSStatus.INTERNAL_SERVICE_ERROR.getStatusAsJson().toString(), MediaType.APPLICATION_JSON);
			WebServiceUtils.setUTF8Encoding(WebServiceUtils.JSON_CONTENT_TYPE, rbuilder);
			return rbuilder.build();
		}
		
		pmMod.close();
		
		WriteToCache.writeSmartphoneToCache(cacheSmartphone);
		
		rbuilder = Response.ok(WSStatus.OK.getStatusAsJson().toString(), MediaType.APPLICATION_JSON);
		WebServiceUtils.setUTF8Encoding(WebServiceUtils.JSON_CONTENT_TYPE, rbuilder);
		return rbuilder.build();
	}
	
	public Response oldWS(String msg) {
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
		PCSession session = null;
		ResponseBuilder rbuilder = null;
		Gson gson = new Gson();
		
		logger.info("[Parent Modifications] Parsing modifications request message");
		//parsing json message from request
		Type modificationType = new TypeToken<ParentModificationsModel>(){}.getType();
		ParentModificationsModel mt;
		
		try {
			logger.info("[Parent Modifications] Transforming json object " + msg);
			mt = gson.fromJson(msg, modificationType);
		} 
		catch (Exception e) {
			logger.warning("[Parent Modifications] ModificationsModel couldn't be created from message " + WSStatus.INTERNAL_SERVICE_ERROR.getMsg());
			rbuilder = Response.ok(WSStatus.INTERNAL_SERVICE_ERROR.getStatusAsJson().toString(), MediaType.APPLICATION_JSON);
			WebServiceUtils.setUTF8Encoding(WebServiceUtils.JSON_CONTENT_TYPE, rbuilder);
			return rbuilder.build();
		}
		
		String cookie;
		String smartphoneId;
		
		if (mt.getCid() != null && mt.getSmid() != null) {
			cookie = mt.getCid();
			smartphoneId = mt.getSmid();
		}
		else {
			logger.warning("[Parent Modifications] The modifications parameters are not valid "+WSStatus.INVALID_DATA.getMsg());
			rbuilder = Response.ok(WSStatus.INVALID_DATA.getStatusAsJson().toString(), MediaType.APPLICATION_JSON);
			WebServiceUtils.setUTF8Encoding(WebServiceUtils.JSON_CONTENT_TYPE, rbuilder);
			return rbuilder.build();
		}
		
		logger.info("[Parent Modifications] Finding user session from cookie");
		
		try {
			 session = SessionUtils.getPCSessionFromCookie(pm, cookie);
		} 
		catch (SessionQueryException e) {
			logger.warning("[Parent Modifications] No session exists for the given cookie. "+e.getMessage());
			rbuilder = Response.ok(WSStatus.NONEXISTING_SESSION.getStatusAsJson().toString(), MediaType.APPLICATION_JSON);
			WebServiceUtils.setUTF8Encoding(WebServiceUtils.JSON_CONTENT_TYPE, rbuilder);
			return rbuilder.build();
		}
		
		//TODO verify if session is active
		
		logger.info("[Parent Modifications] Session found. Getting User from session");
		PCUser user = pm.getObjectById(PCUser.class, session.getUser());
		
		if (user == null) {
			logger.severe("[Parent Modifications] No user associated with a valid session");
			rbuilder = Response.ok(WSStatus.NONEXISTING_USER.getStatusAsJson().toString(), MediaType.APPLICATION_JSON);
			WebServiceUtils.setUTF8Encoding(WebServiceUtils.JSON_CONTENT_TYPE, rbuilder);
			return rbuilder.build();
		}
		
		pm.close();
		//pm = ServiceUtils.PMF.getPersistenceManager();
		PersistenceManager pmMod = ServiceUtils.PMF.getPersistenceManager();
		
		//TODO check if smartphone corresponds to USER
		
		// get smartphone from provided key
		logger.info("[Parent Modifications] Obtaining smartphone from provided key "+smartphoneId);
		PCSmartphone smartphone = (PCSmartphone)pmMod.getObjectById(PCSmartphone.class, KeyFactory.stringToKey(smartphoneId));
		
		if (smartphone == null) {
			logger.severe("[Parent Modifications] No smartphone found from the given key "+smartphoneId);
			rbuilder = Response.ok(WSStatus.INVALID_SMARTPHONE.getStatusAsJson().toString(), MediaType.APPLICATION_JSON);
			WebServiceUtils.setUTF8Encoding(WebServiceUtils.JSON_CONTENT_TYPE, rbuilder);
			return rbuilder.build();
		}
		
		logger.info("[Parent Modifications] Getting Modifications");
		ModificationModel modifications = mt.getModifications();
		
		if (modifications == null) {
			logger.info("[Parent Modifications] Modifications are null, no change to commit");
			rbuilder = Response.ok(WSStatus.OK.getStatusAsJson().toString(), MediaType.APPLICATION_JSON);
			WebServiceUtils.setUTF8Encoding(WebServiceUtils.JSON_CONTENT_TYPE, rbuilder);
			return rbuilder.build();
		}
		
		logger.info("[Parent Modifications] Processing Parent Modifications");
		
		try {
			ModificationUtils.ProcessParentModificationsOLD(pmMod, smartphone, modifications);
		} 
		catch (ModificationParsingException e) {
			logger.severe("[Parent Modifications] An error occurred when processing modifications "+e.getMessage());
			rbuilder = Response.ok(WSStatus.INTERNAL_SERVICE_ERROR.getStatusAsJson().toString(), MediaType.APPLICATION_JSON);
			WebServiceUtils.setUTF8Encoding(WebServiceUtils.JSON_CONTENT_TYPE, rbuilder);
			return rbuilder.build();
		}
		
		pmMod.close();
		rbuilder = Response.ok(WSStatus.OK.getStatusAsJson().toString(), MediaType.APPLICATION_JSON);
		WebServiceUtils.setUTF8Encoding(WebServiceUtils.JSON_CONTENT_TYPE, rbuilder);
		return rbuilder.build();
	}
}





