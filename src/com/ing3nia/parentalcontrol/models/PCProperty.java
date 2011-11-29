package com.ing3nia.parentalcontrol.models;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

/**
 * This class represents a property that can be set to a smartphone.
 * 
 * @author Ing3nia
 * @author Javier Fernandez
 * @author Maria Gabriela Valdes
 * 
 * @version 1.0
 *
 */
@PersistenceCapable
public class PCProperty {
	/**
	 * Unique key that identifies the property.
	 */
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;
	
	/**
	 * 
	 */
	@Persistent
	private String description;
	
	@Persistent
	private String value;
	
	@Persistent
	private int id;
	
	@Persistent
	private Date creationDate;
	
	@Persistent
	private PCSmartphone smartphone;

	public PCProperty() {
		super();
	}

	public PCProperty(Key key, String description, String value, int id,
			Date creationDate, PCSmartphone smartphone) {
		super();
		this.key = key;
		this.description = description;
		this.value = value;
		this.id = id;
		this.creationDate = creationDate;
		this.smartphone = smartphone;
	}

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public PCSmartphone getSmartphone() {
		return smartphone;
	}

	public void setSmartphone(PCSmartphone smartphone) {
		this.smartphone = smartphone;
	}
}
