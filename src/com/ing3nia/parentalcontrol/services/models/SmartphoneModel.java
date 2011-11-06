package com.ing3nia.parentalcontrol.services.models;

import java.util.ArrayList;
import java.util.HashMap;

import javax.jdo.PersistenceManager;

import com.google.appengine.api.datastore.Key;
import com.google.gson.JsonObject;

import com.ing3nia.parentalcontrol.models.PCApplication;
import com.ing3nia.parentalcontrol.models.PCContact;
import com.ing3nia.parentalcontrol.models.PCEmergencyNumber;
import com.ing3nia.parentalcontrol.models.PCModification;
import com.ing3nia.parentalcontrol.models.PCProperty;
import com.ing3nia.parentalcontrol.models.PCRoute;
import com.ing3nia.parentalcontrol.models.PCRule;
import com.ing3nia.parentalcontrol.models.PCSimpleContact;
import com.ing3nia.parentalcontrol.models.PCSmartphone;
import com.ing3nia.parentalcontrol.services.exceptions.SessionQueryException;
import com.ing3nia.parentalcontrol.services.utils.ModelLogger;
import com.ing3nia.parentalcontrol.services.utils.ServiceUtils;

public class SmartphoneModel {
	private String keyId;
	
	private String id;
	
	private LocationModel location;
	
	private ArrayList<ContactModel> activeContacts;
	
	private String name;
	
	private DeviceModel device;
	
	private String serialNumber;
	
	private String appVersion;
	
	private ArrayList<ContactModel> inactiveContacts;
	
	private ArrayList<EmergencyNumberModel> addedEmergencyNumbers;
	
	private ArrayList<EmergencyNumberModel> deletedEmergencyNumbers;
	
	private ArrayList<RouteModel> routes;
	
	private ArrayList<PropertyModel> properties;
	
	private ModificationModel modification;
	
	private ArrayList<RuleModel> rules;
	
	public SmartphoneModel() {
		super();
	}

	public SmartphoneModel(String id, LocationModel location,
			ArrayList<ContactModel> activeContacts, String name,
			DeviceModel device, String serialNumber, String appVersion,
			ArrayList<ContactModel> inactiveContacts,
			ArrayList<EmergencyNumberModel> addedEmergencyNumbers,
			ArrayList<EmergencyNumberModel> deletedEmergencyNumbers,
			ArrayList<RouteModel> routes, ArrayList<PropertyModel> properties,
			ModificationModel modification, ArrayList<RuleModel> rules) {
		super();
		this.id = id;
		this.location = location;
		this.activeContacts = activeContacts;
		this.name = name;
		this.device = device;
		this.serialNumber = serialNumber;
		this.appVersion = appVersion;
		this.inactiveContacts = inactiveContacts;
		this.addedEmergencyNumbers = addedEmergencyNumbers;
		this.deletedEmergencyNumbers = deletedEmergencyNumbers;
		this.routes = routes;
		this.properties = properties;
		this.modification = modification;
		this.rules = rules;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public LocationModel getLocation() {
		return location;
	}

	public void setLocation(LocationModel location) {
		this.location = location;
	}

	public ArrayList<ContactModel> getActiveContacts() {
		return activeContacts;
	}

	public void setActiveContacts(ArrayList<ContactModel> activeContacts) {
		this.activeContacts = activeContacts;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public DeviceModel getDevice() {
		return device;
	}

	public void setDevice(DeviceModel device) {
		this.device = device;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public ArrayList<ContactModel> getInactiveContacts() {
		return inactiveContacts;
	}

	public void setInactiveContacts(ArrayList<ContactModel> inactiveContacts) {
		this.inactiveContacts = inactiveContacts;
	}

	public ArrayList<EmergencyNumberModel> getAddedEmergencyNumbers() {
		return addedEmergencyNumbers;
	}

	public void setAddedEmergencyNumbers(
			ArrayList<EmergencyNumberModel> addedEmergencyNumbers) {
		this.addedEmergencyNumbers = addedEmergencyNumbers;
	}

	public ArrayList<EmergencyNumberModel> getDeletedEmergencyNumbers() {
		return deletedEmergencyNumbers;
	}

	public void setDeletedEmergencyNumbers(
			ArrayList<EmergencyNumberModel> deletedEmergencyNumbers) {
		this.deletedEmergencyNumbers = deletedEmergencyNumbers;
	}

	public ArrayList<RouteModel> getRoutes() {
		return routes;
	}

	public void setRoutes(ArrayList<RouteModel> routes) {
		this.routes = routes;
	}

	public ArrayList<PropertyModel> getProperties() {
		return properties;
	}

	public void setProperties(ArrayList<PropertyModel> properties) {
		this.properties = properties;
	}

	public ModificationModel getModification() {
		return modification;
	}

	public void setModification(ModificationModel modification) {
		this.modification = modification;
	}

	public ArrayList<RuleModel> getRules() {
		return rules;
	}

	public void setRules(ArrayList<RuleModel> rules) {
		this.rules = rules;
	}

	public String getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	public String getKeyId() {
		return keyId;
	}

	public void setKeyId(String keyId) {
		this.keyId = keyId;
	}

	public PCSmartphone convertToPCSmartphone() throws SessionQueryException {

		PCSmartphone smartphone = new PCSmartphone();
		
		ArrayList<Key> pcActiveContacts = new ArrayList<Key>();
		ArrayList<PCContact> pcOriginalContacts = new ArrayList<PCContact>();
		
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
		
		for (ContactModel contact : this.activeContacts) {
			pcOriginalContacts.add(contact.convertToPCContact());
			pcActiveContacts.addAll(contact.saveAsPCSimpleContact());
		}
		
		smartphone.setLocation(this.location.convertToGeoPt());
		smartphone.setActiveContacts(pcActiveContacts);
		smartphone.setInactiveContacts(new ArrayList<Key>());
		smartphone.setAddedEmergencyNumbers(new ArrayList<Key>());
		smartphone.setDeletedEmergencyNumbers(new ArrayList<Key>());
		smartphone.setRoutes(new ArrayList<PCRoute>());
		smartphone.setProperties(new ArrayList<PCProperty>());
		smartphone.setModification(new PCModification());
		smartphone.setRules(new ArrayList<PCRule>());
		smartphone.setNotifications(new ArrayList<Key>());
		smartphone.setName(this.name);
		smartphone.setDevice(this.device.convertToPCDevice());
		smartphone.setSerialNumber(this.serialNumber);
		smartphone.setApplication(ApplicationModel.findPCApplicationByAppVersion(this.appVersion, pm));
		smartphone.setOriginalContacts(pcOriginalContacts);
		
		pm.close();
		
		return smartphone;
	}
	
	public static SmartphoneModel convertToSmartphoneModel(PCSmartphone savedSmartphone, PersistenceManager pm) throws SessionQueryException {
		SmartphoneModel smartphoneModel = new SmartphoneModel();
		
//		try {
//			ArrayList<PCSimpleContact> pcActiveContacts = (ArrayList<PCSimpleContact>)pm.getObjectsById(savedSmartphone.getActiveContacts());
//			ArrayList<SimpleContactModel> activeContactsList = new ArrayList<SimpleContactModel>();
//			HashMap<String, ArrayList<PhoneModel>> auxContactsHashMap = new HashMap<String, ArrayList<PhoneModel>>(); 
//			
//			for (PCSimpleContact contact : pcActiveContacts) {
//				activeContacts.add(SimpleContactModel.convertToSimpleContactModel(contact));
//			}
//			
//			smartphoneModel.setActiveContacts(activeContacts);
//			
//			ArrayList<PCContact> pcInactiveContacts = (ArrayList<PCContact>)pm.getObjectsById(savedSmartphone.getInactiveContacts());
//			ArrayList<ContactModel> inactiveContacts = new ArrayList<ContactModel>();
//			
//			for (PCContact contact : pcInactiveContacts) {
//				inactiveContacts.add(ContactModel.convertToContactModel(contact));
//			}
//			
//			smartphoneModel.setInactiveContacts(inactiveContacts);
//			
//			ArrayList<PCEmergencyNumber> pcAddedEmergencyNumbers = (ArrayList<PCEmergencyNumber>)pm.getObjectsById(savedSmartphone.getAddedEmergencyNumbers());
//			ArrayList<EmergencyNumberModel> addedEmergencyNumbers = new ArrayList<EmergencyNumberModel>();
//			
//			for (PCEmergencyNumber emergencyNumber : pcAddedEmergencyNumbers) {
//				addedEmergencyNumbers.add(EmergencyNumberModel.convertToEmergencyNumberModel(emergencyNumber));
//			}
//			
//			smartphoneModel.setAddedEmergencyNumbers(addedEmergencyNumbers);
//			
//			ArrayList<PropertyModel> properties = new ArrayList<PropertyModel>();
//			
//			for (PCProperty property : savedSmartphone.getProperties()) {
//				properties.add(PropertyModel.convertToPropertyModel(property));
//			}
//			
//			smartphoneModel.setProperties(properties);
//			
//			ArrayList<RuleModel> rules = new ArrayList<RuleModel>();
//			
//			for (PCRule rule : savedSmartphone.getRules()) {
//				rules.add(RuleModel.convertToRuleModel(rule));
//			}
//			
//			smartphoneModel.setRules(rules);
//			smartphoneModel.setDevice(DeviceModel.convertToDeviceModel(savedSmartphone.getDevice()));
//			
//			PCApplication application = (PCApplication)pm.getObjectById(PCApplication.class, savedSmartphone.getApplication());
//			
//			smartphoneModel.setAppVersion(application.getAppInfo().getAppVersion());
//			smartphoneModel.setSerialNumber(savedSmartphone.getSerialNumber());
//			smartphoneModel.setName(savedSmartphone.getName());
//		}
//		catch (Exception ex) {
//	    	ModelLogger.logger.info("[Total Synchronization Service] An error ocurred while finding smartphone info en DB " + ex.getMessage());
//	    	
//			throw new SessionQueryException();
//	    }
		
		return smartphoneModel;
	}
	
	/**
	 * Returns the smartphone info as a json object
	 */
	public JsonObject getSmartphoneAsJson(){
		JsonObject smartphone = new JsonObject();
		
		return smartphone;
	}
}
