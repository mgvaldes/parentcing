package com.ing3nia.parentalcontrol.models.utils;

import com.google.gson.JsonObject;

/**
 * 
 * 
 *
 */
public enum WSStatus {
	OK("00", "OK", "OK"),
	INVALID_PHONE_ID("01", "INVALID_PHONE_ID", "The supplied unique id is not valid"),
	NONEXISTING_USER("09","NONEXISTING_USER","The supplied credentials do not match with any existing user"),
	PREEXISTING_USER("10","PREEXISTING_USER_CREDENTIALS","The supplied username and/or email already exists"),
	INVALID_PASSWORD("11","INVALID_SUPPLIED_PASSWORD","The supplied password does not correspond to the given user"),
	INVALID_PASSWORD_DATA("12","INVALID_PASSWORD_DATA","The supplied password is not valid. Please insert a different one"),
	NONEXISTING_SESSION("13","NONEXISTING_USER_SESSION","You are not logged in"),
	INVALID_DATA("14","INVALID_DATA_GENERAL","Some of the supplied data is not valid"),
	INVALID_SMARTPHONE("15","INVALID_SMARTPHONE","The supplied smartphone is not valid"),
	INTERNAL_SERVICE_ERROR("98","INTERNAL_SERVICE_ERROR","An internal service error has ocurred. Check warn, error, and fatal logs for code bugs"),
	UNEXPECTED_ERROR("99","UNEXPECTED_ERROR","An unexepected error has ocurred");
	//TODO Agregar todos tipos de status
	
	/**
	 * A numeric code for the status as String
	 */
	private String code;
	
	/**
	 * A verbose description code of the status
	 */
	private String verbose;
	
	/**
	 * A message associated with the given status
	 */
	private String msg;	
	
	WSStatus(String code, String verbose, String msg) {
		this.code = code;
		this.verbose = verbose;
		this.msg = msg;
	}
	
	/**
	 * Returns the status info as a json object
	 */
	public JsonObject getStatusAsJson(){
		JsonObject json = new JsonObject();
		JsonObject status = new JsonObject();
		
		status.addProperty("code", this.getCode());
		status.addProperty("verbose", this.getVerbose());
		status.addProperty("msg", this.getMsg());
		
		json.add("status", status);
		return json;
	}
	
	/**
	 * Returns the status info as a json object, overriding the
	 * default status message
	 */
	public JsonObject getStatusAsJson(String statusMessage){
		JsonObject json = new JsonObject();
		JsonObject status = new JsonObject();
		
		status.addProperty("code", this.getCode());
		status.addProperty("verbose", this.getVerbose());
		status.addProperty("msg", statusMessage);
		
		json.add("status", status);
		return json;
	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getVerbose() {
		return verbose;
	}

	public void setVerbose(String verbose) {
		this.verbose = verbose;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
