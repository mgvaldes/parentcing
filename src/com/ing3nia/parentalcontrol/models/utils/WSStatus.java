package com.ing3nia.parentalcontrol.models.utils;

/**
 * 
 * @author gaby
 *
 */
public enum WSStatus {
	OK("00", "OK", "OK"),
	INVALID_PHONE_ID("01", "INVALID_PHONE_ID", "The supplied unique id is not valid");
	//TODO Agregar todos tipos de status
	
	/**
	 * 
	 */
	private String code;
	
	/**
	 * 
	 */
	private String verbose;
	
	/**
	 * 
	 */
	private String msg;	
	
	WSStatus(String code, String verbose, String msg) {
		this.code = code;
		this.verbose = verbose;
		this.msg = msg;
	}
}
