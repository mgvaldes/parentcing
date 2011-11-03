package com.ing3nia.parentalcontrol.models;

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
public class PCModificationStatistics {
	/**
	 * Unique key that identifies the .
	 */
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;
	
	@Persistent
	private PCProperty property;
	
	@Persistent
	private PCFunctionality functionality;
	
	@Persistent
	private int addedContacts;
	
	@Persistent
	private int deletedContacts;

	public PCModificationStatistics() {
		super();
	}

	public PCModificationStatistics(Key key, PCProperty property,
			PCFunctionality functionality, int addedContacts,
			int deletedContacts) {
		super();
		this.key = key;
		this.property = property;
		this.functionality = functionality;
		this.addedContacts = addedContacts;
		this.deletedContacts = deletedContacts;
	}

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	public PCProperty getProperty() {
		return property;
	}

	public void setProperty(PCProperty property) {
		this.property = property;
	}

	public PCFunctionality getFunctionality() {
		return functionality;
	}

	public void setFunctionality(PCFunctionality functionality) {
		this.functionality = functionality;
	}

	public int getAddedContacts() {
		return addedContacts;
	}

	public void setAddedContacts(int addedContacts) {
		this.addedContacts = addedContacts;
	}

	public int getDeletedContacts() {
		return deletedContacts;
	}

	public void setDeletedContacts(int deletedContacts) {
		this.deletedContacts = deletedContacts;
	}
}
