package com.ing3nia.parentalcontrol.models;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

/**
 * This class contains information the status that web services can return in a call
 * 
 * @author Ing3nia
 * @author Javier Fernandez
 * @author Maria Gabriela Valdes
 * 
 * @version 1.0
 *
 */
@PersistenceCapable
public class PCWSStatus {
	/**
	 * Unique key that identifies the status.
	 */
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;
	
	/**
	 * Specifies the code related to a certain status.
	 */
	@Persistent
	private String code;
	
	/**
	 * Specifies the concept or short message related to a certain status.
	 */
	@Persistent
	private String verbose;
	
	/**
	 * Specifies the long message showed to the user related to a certain status.
	 */
	@Persistent
	private String message;

	public PCWSStatus() {
		super();
	}

	public PCWSStatus(Key key, String code, String verbose, String message) {
		super();
		this.key = key;
		this.code = code;
		this.verbose = verbose;
		this.message = message;
	}

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
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

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
