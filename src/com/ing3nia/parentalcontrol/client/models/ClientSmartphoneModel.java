package com.ing3nia.parentalcontrol.client.models;

import java.util.ArrayList;

public class ClientSmartphoneModel {
	private String key;
	
	private ClientLocationModel location;
	
	private ArrayList<ClientContactModel> activeContacts;
	
	private String name;
	
	private ClientDeviceModel device;
	
	private String serialNumber;
	
	private String appVersion;
	
	private ArrayList<ClientContactModel> inactiveContacts;
	
	private ArrayList<ClientEmergencyNumberModel> addedEmergencyNumbers;
	
	private ArrayList<ClientEmergencyNumberModel> deletedEmergencyNumbers;
	
	private ArrayList<ClientRouteModel> routes;
	
	private ArrayList<ClientPropertyModel> properties;
	
	private ClientModificationModel modification;
	
	private ArrayList<ClientRuleModel> rules;
	
	public ClientSmartphoneModel() {
		super();
	}

	public ClientSmartphoneModel(String key, ClientLocationModel location,
			ArrayList<ClientContactModel> activeContacts, String name,
			ClientDeviceModel device, String serialNumber, String appVersion,
			ArrayList<ClientContactModel> inactiveContacts,
			ArrayList<ClientEmergencyNumberModel> addedEmergencyNumbers,
			ArrayList<ClientEmergencyNumberModel> deletedEmergencyNumbers,
			ArrayList<ClientRouteModel> routes, ArrayList<ClientPropertyModel> properties,
			ClientModificationModel modification, ArrayList<ClientRuleModel> rules) {
		super();
		this.key = key;
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

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public ClientLocationModel getLocation() {
		return location;
	}

	public void setLocation(ClientLocationModel location) {
		this.location = location;
	}

	public ArrayList<ClientContactModel> getActiveContacts() {
		return activeContacts;
	}

	public void setActiveContacts(ArrayList<ClientContactModel> activeContacts) {
		this.activeContacts = activeContacts;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ClientDeviceModel getDevice() {
		return device;
	}

	public void setDevice(ClientDeviceModel device) {
		this.device = device;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public ArrayList<ClientContactModel> getInactiveContacts() {
		return inactiveContacts;
	}

	public void setInactiveContacts(ArrayList<ClientContactModel> inactiveContacts) {
		this.inactiveContacts = inactiveContacts;
	}

	public ArrayList<ClientEmergencyNumberModel> getAddedEmergencyNumbers() {
		return addedEmergencyNumbers;
	}

	public void setAddedEmergencyNumbers(
			ArrayList<ClientEmergencyNumberModel> addedEmergencyNumbers) {
		this.addedEmergencyNumbers = addedEmergencyNumbers;
	}

	public ArrayList<ClientEmergencyNumberModel> getDeletedEmergencyNumbers() {
		return deletedEmergencyNumbers;
	}

	public void setDeletedEmergencyNumbers(
			ArrayList<ClientEmergencyNumberModel> deletedEmergencyNumbers) {
		this.deletedEmergencyNumbers = deletedEmergencyNumbers;
	}

	public ArrayList<ClientRouteModel> getRoutes() {
		return routes;
	}

	public void setRoutes(ArrayList<ClientRouteModel> routes) {
		this.routes = routes;
	}

	public ArrayList<ClientPropertyModel> getProperties() {
		return properties;
	}

	public void setProperties(ArrayList<ClientPropertyModel> properties) {
		this.properties = properties;
	}

	public ClientModificationModel getModification() {
		return modification;
	}

	public void setModification(ClientModificationModel modification) {
		this.modification = modification;
	}

	public ArrayList<ClientRuleModel> getRules() {
		return rules;
	}

	public void setRules(ArrayList<ClientRuleModel> rules) {
		this.rules = rules;
	}

	public String getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}
}
