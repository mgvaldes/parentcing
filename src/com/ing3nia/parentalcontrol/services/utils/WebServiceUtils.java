package com.ing3nia.parentalcontrol.services.utils;

import javax.servlet.ServletRequest;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

public class WebServiceUtils {
	
	public static String JSON_CONTENT_TYPE = "application/json";
	
	public static Response setUTF8Encoding(String responseContentType, Response response){
		ResponseBuilder r = Response.fromResponse(response);
		r.header("Content-Type", responseContentType+";charset=utf-8");
		return r.build();
	}
	
	public static void setUTF8Encoding(String responseContentType, ResponseBuilder responseBuilder){
		responseBuilder.header("Content-Type", responseContentType+";charset=utf-8");
	}
	
}
