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
	 */
	@Persistent
	private ArrayList<PCFunctionality> disabledFunctionalities;
	
	/**
	 * Represents the creation date of a specific rule.
	 */
	@Persistent
	private Date creationDate;

	public PCRule() {
		super();
	}

	public PCRule(Key key, Date startDate, Date endDate,
			ArrayList<PCFunctionality> disabledFunctionalities,
			Date creationDate) {
		super();
		this.key = key;
		this.startDate = startDate;
		this.endDate = endDate;
		this.disabledFunctionalities = disabledFunctionalities;
		this.creationDate = creationDate;
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

	public ArrayList<PCFunctionality> getDisabledFunctionalities() {
		return disabledFunctionalities;
	}

	public void setDisabledFunctionalities(
			ArrayList<PCFunctionality> disabledFunctionalities) {
		this.disabledFunctionalities = disabledFunctionalities;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
}
