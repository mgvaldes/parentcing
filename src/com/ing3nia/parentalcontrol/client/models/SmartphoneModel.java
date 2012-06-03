package com.ing3nia.parentalcontrol.client.models;

import java.io.Serializable;
import java.util.ArrayList;

import com.google.gwt.i18n.client.DateTimeFormat;

public class SmartphoneModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String keyId;
	
	private String id;
	
	//private int detailsSynced;
	
	private LocationModel location;
	
	private ArrayList<ContactModel> activeContacts; //
	
	private String name; //
	
	private DeviceModel device; //
	
	private String serialNumber; //
	
	private String appVersion; //
	
	private ArrayList<ContactModel> inactiveContacts; //
	
	private ArrayList<EmergencyNumberModel> addedEmergencyNumbers; //
	
	private ArrayList<EmergencyNumberModel> deletedEmergencyNumbers; //
	
	private ArrayList<RouteModel> routes;
	
	private ArrayList<PropertyModel> properties; //
	
	private ModificationModel modification;
	
	private ArrayList<RuleModel> rules; //
	
	private ArrayList<NotificationModel> alerts; //
	
	public SmartphoneModel() {
	}

	public SmartphoneModel(String id, LocationModel location,
			ArrayList<ContactModel> activeContacts, String name,
			DeviceModel device, String serialNumber, String appVersion,
			ArrayList<ContactModel> inactiveContacts,
			ArrayList<EmergencyNumberModel> addedEmergencyNumbers,
			ArrayList<EmergencyNumberModel> deletedEmergencyNumbers,
			ArrayList<RouteModel> routes, ArrayList<PropertyModel> properties,
			ModificationModel modification, ArrayList<RuleModel> rules, ArrayList<NotificationModel> alerts) {
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
		this.alerts = alerts;
		//this.detailsSynced = 0;
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

	public void setAddedEmergencyNumbers(ArrayList<EmergencyNumberModel> addedEmergencyNumbers) {
		this.addedEmergencyNumbers = addedEmergencyNumbers;
	}

	public ArrayList<EmergencyNumberModel> getDeletedEmergencyNumbers() {
		return deletedEmergencyNumbers;
	}

	public void setDeletedEmergencyNumbers(ArrayList<EmergencyNumberModel> deletedEmergencyNumbers) {
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

	public ArrayList<NotificationModel> getAlerts() {
		return alerts;
	}

	public void setAlerts(ArrayList<NotificationModel> alerts) {
		this.alerts = alerts;
	}

/*
	public int getDetailsSynced() {
		return detailsSynced;
	}

	public void setDetailsSynced(int detailsSynced) {
		this.detailsSynced = detailsSynced;
	}
	*/

	public static ArrayList<AlertModel> getUserAlertList(SmartphoneModel smart) {
		ArrayList<AlertModel> alerts = new ArrayList<AlertModel>();
		AlertModel auxAlert;
		DateTimeFormat formatter = DateTimeFormat
				.getFormat("dd/MM/yyyy hh:mm:ss a");

		for (NotificationModel not : smart.getAlerts()) {	
			auxAlert = new AlertModel(formatter.parse(not.getDate()),
					smart.getName(),
					PCNotificationTypeId.getNotificationMessageFromType(not
							.getType()));
			alerts.add(auxAlert);
		}

		return alerts;
	}

}
