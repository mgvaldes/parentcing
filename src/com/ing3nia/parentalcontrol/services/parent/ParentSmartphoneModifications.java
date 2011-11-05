package com.ing3nia.parentalcontrol.services.parent;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.logging.ConsoleHandler;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.repackaged.com.google.protobuf.Service;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.ing3nia.parentalcontrol.models.PCSession;
import com.ing3nia.parentalcontrol.models.PCUser;
import com.ing3nia.parentalcontrol.models.utils.WSStatus;
import com.ing3nia.parentalcontrol.services.exceptions.EncodingException;
import com.ing3nia.parentalcontrol.services.exceptions.SessionQueryException;
import com.ing3nia.parentalcontrol.services.models.UserModel;
import com.ing3nia.parentalcontrol.services.utils.EncryptionUtils;
import com.ing3nia.parentalcontrol.services.utils.ServiceUtils;
import com.ing3nia.parentalcontrol.services.utils.SessionUtils;
import com.ing3nia.parentalcontrol.services.utils.SmartphoneUtils;

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
@Path("smartphone-grl")
public class ParentSmartphoneModifications {
	
	private static final Logger logger = Logger
	.getLogger(ParentSmartphoneGeneral.class.getName());

	public ParentSmartphoneModifications(){
		logger.addHandler(new ConsoleHandler());
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	/**
	 * Processes the modifications made by father phone
	 */
	public Response post(String msg) {
		String responseMsg = "";
		ResponseBuilder rbuilder = Response.ok(responseMsg.toString(), MediaType.APPLICATION_JSON);
		return rbuilder.build();
	}
}
