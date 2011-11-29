package com.ing3nia.parentalcontrol.models;

import java.util.ArrayList;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.GeoPt;
import com.google.appengine.api.datastore.Key;

/**
 * This class contains information about a specific smartphone.  
 * 
 * @author Ing3nia
 * @author Javier Fernandez
 * @author Maria Gabriela Valdes
 * 
 * @version 1.0
 *
 */
@PersistenceCapable
public class PCSmartphone {
	/**
	 * Unique key that identifies the smartphone.
	 */
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;
	
	/**
	 * Represents the current location of the smartphone.
	 */
	@Persistent
	private GeoPt location;
	
	/**
	 * Represents a collection of active contacts currently saved 
	 * in a specific smartphone.
	 */
	@Persistent
	//private ArrayList<ArrayList<PCContact>> contacts;
	//private ArrayList<PCActiveContact> activeContacts;
	private ArrayList<Key> activeContacts;
	
	/**
	 * Represents a collection of inactive contacts currently not 
	 * saved in a specific smartphone.
	 */
	@Persistent
	//private ArrayList<PCInactiveContact> inactiveContacts;
	private ArrayList<Key> inactiveContacts;
	
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
	 * Represents a collection of routes of a specific smartphon registered
	 * by the application. 
	 */
	@Persistent(mappedBy = "smartphone")
	private ArrayList<PCRoute> routes;
	
	/**
	 * Represents a collection of properties currently available in the 
	 * smartphone.
	 */
	@Persistent(mappedBy = "smartphone")
	private ArrayList<PCProperty> properties;
	
	/**
	 * Represents the current modification that has to be applied to a specific
	 * smartphone.
	 */
	@Persistent(mappedBy = "smartphone")
	private PCModification modification;
	
	/**
	 * Represents a collection of rules currently disabled in a specific
	 * smartphone.
	 */
	@Persistent(mappedBy = "smartphone")
	private ArrayList<PCRule> rules;
	
	/**
	 * Represents a collection of notifications sent to the smartphone
	 */
	@Persistent
	private ArrayList<Key> notifications;
	
	/**
	 * Specifies the name associated to a smartphone.
	 */
	@Persistent
	private String name;
	
	/**
	 * Specifies the device information of a specific smartphone.
	 */
	@Persistent(mappedBy = "smartphone")
	private PCDevice device;
	
	/**
	 * Specifies the serial number of a smartphone.
	 */
	@Persistent
	private String serialNumber;

	/**
	 * Key of PCApplication
	 */
	@Persistent
	private Key application;
	
	@Persistent(mappedBy = "smartphone")
	private ArrayList<PCContact> originalContacts;
	
	public PCSmartphone() {
		super();
	}

	public PCSmartphone(Key key, GeoPt location,
			ArrayList<Key> activeContacts,
			ArrayList<Key> inactiveContacts,
			ArrayList<Key> addedEmergencyNumbers,
			ArrayList<Key> deletedEmergencyNumbers,
			ArrayList<PCRoute> routes, ArrayList<PCProperty> properties,
			PCModification modification, ArrayList<PCRule> rules, String name,
			PCDevice device, String serialNumber, Key application, ArrayList<Key> notifications,
			ArrayList<PCContact> originalContacts) {
		super();
		this.key = key;
		this.location = location;
		this.activeContacts = activeContacts;
		this.inactiveContacts = inactiveContacts;
		this.addedEmergencyNumbers = addedEmergencyNumbers;
		this.deletedEmergencyNumbers = deletedEmergencyNumbers;
		this.routes = routes;
		this.properties = properties;
		this.modification = modification;
		this.rules = rules;
		this.name = name;
		this.device = device;
		this.serialNumber = serialNumber;
		this.application = application;
		this.notifications = notifications;
		this.originalContacts = originalContacts;
	}

//	public PCSmartphone(Key key, GeoPt location,
//			ArrayList<ArrayList<PCContact>> contacts,
//			ArrayList<ArrayList<PCEmergencyNumber>> emergencyNumbers,
//			ArrayList<PCRoute> routes, ArrayList<PCProperty> properties,
//			PCModification modification, ArrayList<PCRule> rules, String name,
//			PCDevice device, String serialNumber, PCApplication application) {
//		super();
//		this.key = key;
//		this.location = location;
//		this.contacts = contacts;
//		this.emergencyNumbers = emergencyNumbers;
//		this.routes = routes;
//		this.properties = properties;
//		this.modification = modification;
//		this.rules = rules;
//		this.name = name;
//		this.device = device;
//		this.serialNumber = serialNumber;
//		this.application = application;
//	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	public GeoPt getLocation() {
		return location;
	}

	public void setLocation(GeoPt location) {
		this.location = location;
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
//
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

	public ArrayList<PCRoute> getRoutes() {
		return routes;
	}

	public void setRoutes(ArrayList<PCRoute> routes) {
		this.routes = routes;
	}

	public ArrayList<PCProperty> getProperties() {
		return properties;
	}

	public void setProperties(ArrayList<PCProperty> properties) {
		this.properties = properties;
	}

	public PCModification getModification() {
		return modification;
	}
	
	public void setModification(PCModification modification) {
		this.modification = modification;
	}


	public ArrayList<PCRule> getRules() {
		return rules;
	}

	public void setRules(ArrayList<PCRule> rules) {
		this.rules = rules;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public PCDevice getDevice() {
		return device;
	}

	public void setDevice(PCDevice device) {
		this.device = device;
	}

	public Key getApplication() {
		return application;
	}

	public void setApplication(Key application) {
		this.application = application;
	}

	public ArrayList<Key> getNotifications() {
		return notifications;
	}

	public void setNotifications(ArrayList<Key> notifications) {
		this.notifications = notifications;
	}
	
	public ArrayList<PCContact> getOriginalContacts() {
		return originalContacts;
	}

	public void setOriginalContacts(ArrayList<PCContact> originalContacts) {
		this.originalContacts = originalContacts;
	}
}
