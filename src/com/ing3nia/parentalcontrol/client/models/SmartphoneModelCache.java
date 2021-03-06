package com.ing3nia.parentalcontrol.client.models;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class SmartphoneModelCache implements Serializable {
	private static final long serialVersionUID = 1L;

	private String keyId;
	
	private String id;
	
	private LocationModel location;
	
	private ArrayList<ContactModel> active_cts;
	
	private String name;
	
	private DeviceModel device;
	
	private String serialNumber;
	
	private String appVersion;
	
	private ArrayList<ContactModel> inactive_cts;
	
	private ArrayList<EmergencyNumberModel> addedEmergencyNumbers;
	
	private ArrayList<EmergencyNumberModel> deletedEmergencyNumbers;
	
	private ArrayList<RouteModel> routes;
	
	private ArrayList<PropertyModel> properties;
	
	private ModificationModel modification;
	
	private ArrayList<RuleModel> rules;
	
	private ArrayList<NotificationModel> alerts;
	
	public SmartphoneModelCache() {
	}

	public SmartphoneModelCache(String id, LocationModel location,
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
		this.active_cts = activeContacts;
		this.name = name;
		this.device = device;
		this.serialNumber = serialNumber;
		this.appVersion = appVersion;
		this.inactive_cts = inactiveContacts;
		this.addedEmergencyNumbers = addedEmergencyNumbers;
		this.deletedEmergencyNumbers = deletedEmergencyNumbers;
		this.routes = routes;
		this.properties = properties;
		this.modification = modification;
		this.rules = rules;
		this.alerts = alerts;
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
		return (ArrayList<ContactModel>)active_cts;
	}

	public void setActiveContacts(ArrayList<ContactModel> activeContacts) {
		this.active_cts = activeContacts;
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
		return (ArrayList<ContactModel>)inactive_cts;
	}

	public void setInactiveContacts(ArrayList<ContactModel> inactiveContacts) {
		this.inactive_cts = inactiveContacts;
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

	public void setDeletedEmergencyNumbers(ArrayList<EmergencyNumberModel> deletedEmergencyNumbers) {
		this.deletedEmergencyNumbers = deletedEmergencyNumbers;
	}

	public ArrayList<RouteModel> getRoutes() {
		return (ArrayList<RouteModel>)routes;
	}

	public void setRoutes(ArrayList<RouteModel> routes) {
		this.routes = routes;
	}

	public ArrayList<PropertyModel> getProperties() {
		return (ArrayList<PropertyModel>)properties;
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
		return (ArrayList<RuleModel>)rules;
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
		return (ArrayList<NotificationModel>)alerts;
	}

	public void setAlerts(ArrayList<NotificationModel> alerts) {
		this.alerts = alerts;
	}
	
	
	public static ArrayList<AlertModel> getUserAlertList(SmartphoneModel smart) throws ParseException {
		ArrayList<AlertModel> alerts = new ArrayList<AlertModel>();
		AlertModel auxAlert;
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy kk:mm:ss a");

		for (NotificationModel not : smart.getAlerts()) {
			auxAlert = new AlertModel(formatter.parse(not.getDate()), smart.getName(), PCNotificationTypeId.getNotificationMessageFromType(not.getType()));
			alerts.add(auxAlert);
		}

		return alerts;
	}
}
