package com.ing3nia.parentalcontrol.models;

import java.util.ArrayList;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

/**
 * This class contains information about all the modifications
 * that have to be applied to a specific smartphone.  
 * 
 * @author Ing3nia
 * @author Javier Fernandez
 * @author Maria Gabriela Valdes
 * 
 * @version 1.0
 *
 */
@PersistenceCapable
public class PCModification {
	/**
	 * Unique key that identifies the modification.
	 */
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;
	
	/**
	 * Represents a collection of active contacts to be saved in a 
	 * specific smartphone.
	 */
	@Persistent
	//private ArrayList<ArrayList<PCContact>> contacts;
	private ArrayList<Key> activeContacts;
	
	/**
	 * Represents a collection of inactive contacts to be saved in a 
	 * specific smartphone.
	 */
	@Persistent
	private ArrayList<Key> inactiveContacts;
	
	/**
	 * Represents a collection of properties to be applied to the a specific
	 * smartphone.
	 * Key references PCProperty
	 */
	@Persistent
	private ArrayList<Key> properties;
	
	/**
	 * Represents a collection of rules to be applied to the a specific
	 * smartphone. 
	 * Key references PCRule
	 */
	@Persistent
	private ArrayList<Key> rules;
	
	/**
	 * Represents a collection of emergency numbers added to a
	 * specific smartphone.
	 */
	@Persistent
	//private ArrayList<ArrayList<PCEmergencyNumber>> emergencyNumbers;
	private ArrayList<Key> addedEmergencyNumbers;
	
	/**
	 * Represents a collection of emergency numbers deleted to a
	 * specific smartphone.
	 */
	@Persistent
	private ArrayList<Key> deletedEmergencyNumbers;

	/**
	 * Represents a collection of reference keys as string of deleted rules 
	 */
	@Persistent
	private ArrayList<String>  deletedRules;
	
	//@Persistent(defaultFetchGroup = "true")
	//private PCSmartphone smartphone;
	
	public PCModification() {
		super();
	}

	public PCModification(Key key, ArrayList<Key> activeContacts,
			ArrayList<Key> inactiveContacts,
			ArrayList<Key> properties, ArrayList<Key> rules,
			ArrayList<Key> addedEmergencyNumbers,
			ArrayList<Key> deletedEmergencyNumbers) {
		super();
		this.key = key;
		this.activeContacts = activeContacts;
		this.inactiveContacts = inactiveContacts;
		this.properties = properties;
		this.rules = rules;
		this.addedEmergencyNumbers = addedEmergencyNumbers;
		this.deletedEmergencyNumbers = deletedEmergencyNumbers;
		//this.smartphone = smartphone;
	}

//	public PCModification(Key key, ArrayList<ArrayList<PCContact>> contacts,
//			ArrayList<PCProperty> properties, ArrayList<PCRule> rules,
//			ArrayList<ArrayList<PCEmergencyNumber>> emergencyNumbers) {
//		super();
//		this.key = key;
//		this.contacts = contacts;
//		this.properties = properties;
//		this.rules = rules;
//		this.emergencyNumbers = emergencyNumbers;
//	}

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	public ArrayList<Key> getActiveContacts() {
		return activeContacts;
	}

	public void setActiveContacts(ArrayList<Key> activeContacts) {
		this.activeContacts = activeContacts;
	}

	public ArrayList<Key> getInactiveContacts() {
		return inactiveContacts;
	}

	public void setInactiveContacts(ArrayList<Key> inactiveContacts) {
		this.inactiveContacts = inactiveContacts;
	}

//	public ArrayList<ArrayList<PCContact>> getContacts() {
//		return contacts;
//	}
//
//	public void setContacts(ArrayList<ArrayList<PCContact>> contacts) {
//		this.contacts = contacts;
//	}

	public ArrayList<Key> getProperties() {
		return properties;
	}

	public void setProperties(ArrayList<Key> properties) {
		this.properties = properties;
	}

	public ArrayList<Key> getRules() {
		return rules;
	}

	public void setRules(ArrayList<Key> rules) {
		this.rules = rules;
	}

//	public ArrayList<ArrayList<PCEmergencyNumber>> getEmergencyNumbers() {
//		return emergencyNumbers;
//	}
//
//	public void setEmergencyNumbers(
//			ArrayList<ArrayList<PCEmergencyNumber>> emergencyNumbers) {
//		this.emergencyNumbers = emergencyNumbers;
//	}
	
	public ArrayList<Key> getAddedEmergencyNumbers() {
		return addedEmergencyNumbers;
	}

	public void setAddedEmergencyNumbers(ArrayList<Key> addedEmergencyNumbers) {
		this.addedEmergencyNumbers = addedEmergencyNumbers;
	}

	public ArrayList<Key> getDeletedEmergencyNumbers() {
		return deletedEmergencyNumbers;
	}

	public void setDeletedEmergencyNumbers(
			ArrayList<Key> deletedEmergencyNumbers) {
		this.deletedEmergencyNumbers = deletedEmergencyNumbers;
	}

	public ArrayList<String> getDeletedRules() {
		return deletedRules;
	}

	public void setDeletedRules(ArrayList<String> deletedRules) {
		this.deletedRules = deletedRules;
	}

}
