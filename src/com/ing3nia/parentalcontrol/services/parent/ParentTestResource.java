package com.ing3nia.parentalcontrol.services.parent;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import com.google.appengine.repackaged.org.json.JSONObject;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.reflect.TypeToken;

@SuppressWarnings("serial")
@Path("hello")
public class ParentTestResource {

	private static final Logger logger = Logger
			.getLogger(ParentTestResource.class.getName());
	
	public ParentTestResource(){
	}

	@GET
	// The Java method will produce content identified by the MIME Media
	// type "text/plain"
	@Produces(MediaType.APPLICATION_JSON)
	public Response getClichedMessage() {
		// Return some cliched textual content
		JsonObject rjson = new JsonObject();
		rjson.addProperty("username","ingenia");
		rjson.addProperty("id", 3);
		
		JsonArray array = new JsonArray();
		
		array.add(new JsonPrimitive("dad"));
		array.add(new JsonPrimitive("mother"));
		array.add(new JsonPrimitive("child"));
		rjson.add("family", array);
		
		Gson gson = new Gson();
		JsonObject jobject;
		//Type parsingClass = new TypeToken<UserTest>(){}.getType();
		//UserTest ut = gson.fromJson(rjson.toString(), parsingClass);
		
		JsonObject robj = new JsonObject();
		JsonParser parser = new JsonParser();
		//robj = (JsonObject)parser.parse(gson.toJson(ut, parsingClass));
		robj.addProperty("worked", true);
		
		
		ResponseBuilder rbuilder = Response.ok(robj.toString(), MediaType.APPLICATION_JSON);
		return rbuilder.build();	
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response post(String msg) {

		Gson gson = new Gson();
		JsonObject jobject;
		
		JsonParser parser = new JsonParser();
		jobject = (JsonObject)parser.parse(msg);
		
		/*Type parsingClass = new TypeToken<UserTest>(){}.getType();
		UserTest ut = gson.fromJson(jobject.toString(), parsingClass);
		*/
		JsonObject robj = new JsonObject();
		//robj = (JsonObject)parser.parse(gson.toJson(ut, parsingClass));
		
		robj.addProperty("worked", true);
		
		ResponseBuilder rbuilder = Response.ok(robj.toString(), MediaType.APPLICATION_JSON);
		return rbuilder.build();
	}
}

