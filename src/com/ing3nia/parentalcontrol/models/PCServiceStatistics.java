package com.ing3nia.parentalcontrol.models;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

/**
 * 
 * 
 * @author Ing3nia
 * @author Javier Fernandez
 * @author Maria Gabriela Valdes
 * 
 * @version 1.0
 *
 */
@PersistenceCapable
public class PCServiceStatistics {
	/**
	 * Unique key that identifies the .
	 */
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;
	
	@Persistent
	private String serviceName;
	
	@Persistent
	private String serviceCode;
	
	@Persistent
	private Date date;
	
	@Persistent
	private Key smartphone;
	
	@Persistent
	private Key status;

	public PCServiceStatistics() {
		super();
	}

	public PCServiceStatistics(Key key, String serviceName, Date date, Key smartphone,
			Key status, String serviceCode) {
		super();
		this.key = key;
		this.serviceName = serviceName;
		this.date = date;
		this.smartphone = smartphone;
		this.status = status;
		this.serviceCode = serviceCode;
	}

	public Key getStatus() {
		return status;
	}

	public void setStatus(Key status) {
		this.status = status;
	}

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Key getSmartphone() {
		return smartphone;
	}

	public void setSmartphone(Key smartphone) {
		this.smartphone = smartphone;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}
}
