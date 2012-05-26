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

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.ing3nia.parentalcontrol.models.PCSession;
import com.ing3nia.parentalcontrol.models.PCUser;
import com.ing3nia.parentalcontrol.models.utils.WSStatus;
import com.ing3nia.parentalcontrol.services.exceptions.SessionQueryException;
import com.ing3nia.parentalcontrol.services.utils.ServiceUtils;
import com.ing3nia.parentalcontrol.services.utils.SessionUtils;
import com.ing3nia.parentalcontrol.services.utils.SmartphoneUtils;
import com.ing3nia.parentalcontrol.services.utils.WebServiceUtils;

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
@Path("smartphone-grl")
public class ParentSmartphoneGeneral {
	public String NEW_WS = "new";
    public String OLD_WS = "old";
    public String ACTUAL = NEW_WS;
	
	
	private static final Logger logger = Logger
	.getLogger(ParentSmartphoneGeneral.class.getName());

	public ParentSmartphoneGeneral(){
		//logger.addHandler(new ConsoleHandler());
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	/**
	 * Returns general information of the children smartphones associated to a given user
	 */
	public Response get(@QueryParam(value = "cid") final String cookie) {
        
		if (ACTUAL.equals(NEW_WS)) {
	            return newWS(cookie);
	    }
	    return oldWS(cookie);
	}	
	
	public Response oldWS(String cookie){
		
		logger.info("[Parent Smartphone General] Obtaining session from request");
		
		//creating global variables
		ResponseBuilder rbuilder;
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
		PCSession session = null;
		
		try {
			 session = SessionUtils.getPCSessionFromCookie(pm, cookie);
		} catch (SessionQueryException e) {
			logger.warning("[Parent Smartphone General] No session exists for the given cookie. "+e.getMessage());
			rbuilder = Response.ok(WSStatus.NONEXISTING_SESSION.getStatusAsJson().toString(), MediaType.APPLICATION_JSON);
			WebServiceUtils.setUTF8Encoding(WebServiceUtils.JSON_CONTENT_TYPE, rbuilder);
			return rbuilder.build();
		}
		
		//TODO verify if session is active
		
		logger.info("[Parent Smartphone General] Session found. Getting User from session");
		PCUser user = pm.getObjectById(PCUser.class, session.getUser());
		
		if(user==null){
			logger.severe("[Parent Smartphone General] No user associated with a valid session");
			rbuilder = Response.ok(WSStatus.NONEXISTING_USER.getStatusAsJson().toString(), MediaType.APPLICATION_JSON);
			WebServiceUtils.setUTF8Encoding(WebServiceUtils.JSON_CONTENT_TYPE, rbuilder);
			return rbuilder.build();
		}
		
		JsonArray smpGeneralInfoArray = SmartphoneUtils.getChildrenSmartphonesInfo(user);
		
		JsonObject responseMsg = WSStatus.OK.getStatusAsJson();
		responseMsg.add("smartphones", smpGeneralInfoArray);
		
		rbuilder = Response.ok(responseMsg.toString(), MediaType.APPLICATION_JSON);
		WebServiceUtils.setUTF8Encoding(WebServiceUtils.JSON_CONTENT_TYPE, rbuilder);
		return rbuilder.build();
	}
}
