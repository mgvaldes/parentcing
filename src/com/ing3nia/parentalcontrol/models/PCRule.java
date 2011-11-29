package com.ing3nia.parentalcontrol.models;

import java.util.ArrayList;
import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public class PCRule {
	/**
	 * Unique key that identifies the device.
	 */
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;
	
	/**
	 * Represents the start date of a specific rule
	 */
	@Persistent
	private Date startDate;
	
	/**
	 * Represents the end date of a specific rule
	 */	
	@Persistent
	private Date endDate;
	
	/**
	 * Represents a collection of functionalities that are going to 
	 * be disabled between the startDate and the endDate.
	 * Key references PCFunctionalities
	 */
	@Persistent
	private ArrayList<Key> disabledFunctionalities;
	
	/**
	 * Represents the creation date of a specific rule.
	 */
	@Persistent
	private Date creationDate;
	
	@Persistent
	private String name;
	
//	@Persistent
//	private PCSmartphone smartphone;

	public PCRule() {
	}

	public PCRule(Key key, Date startDate, Date endDate,
			ArrayList<Key> disabledFunctionalities,
			Date creationDate, String name) {

		this.key = key;
		this.startDate = startDate;
		this.endDate = endDate;
		this.disabledFunctionalities = disabledFunctionalities;
		this.creationDate = creationDate;
		this.name = name;
//		this.smartphone = smartphone;
	}

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public ArrayList<Key> getDisabledFunctionalities() {
		return disabledFunctionalities;
	}

	public void setDisabledFunctionalities(
			ArrayList<Key> disabledFunctionalities) {
		this.disabledFunctionalities = disabledFunctionalities;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

//	public PCSmartphone getSmartphone() {
//		return smartphone;
//	}
//
//	public void setSmartphone(PCSmartphone smartphone) {
//		this.smartphone = smartphone;
//	}
}
