package com.ing3nia.parentalcontrol.client.models;

import java.util.ArrayList;

public class ClientModificationModel {
	private String key;
	
	private String smartphoneName;
	
	private ArrayList<ClientSimpleContactModel> activeContacts;
	
	private ArrayList<ClientSimpleContactModel> inactiveContacts;
	
	private ArrayList<ClientPropertyModel> properties;
	
	private ArrayList<ClientRuleModel> rules;
	
	private ArrayList<ClientEmergencyNumberModel> addedEmergencyNumbers;
	
	private ArrayList<ClientEmergencyNumberModel> deletedEmergencyNumbers;
	
	private ArrayList<String> deletedRules;

	public ClientModificationModel() {
		super();
	}

	public ClientModificationModel(String key, String smartphoneName, ArrayList<ClientSimpleContactModel> activeContacts,
			ArrayList<ClientSimpleContactModel> inactiveContacts,
			ArrayList<ClientPropertyModel> properties, ArrayList<ClientRuleModel> rules,
			ArrayList<ClientEmergencyNumberModel> addedEmergencyNumbers,
			ArrayList<ClientEmergencyNumberModel> deletedEmergencyNumbers, ArrayList<String> deletedRules) {
		super();
		this.key = key;
		this.activeContacts = activeContacts;
		this.inactiveContacts = inactiveContacts;
		this.properties = properties;
		this.rules = rules;
		this.addedEmergencyNumbers = addedEmergencyNumbers;
		this.deletedEmergencyNumbers = deletedEmergencyNumbers;
		this.smartphoneName = smartphoneName;
		this.deletedRules = deletedRules;
	}
	
	public ClientModificationModel(String key, ArrayList<ClientSimpleContactModel> activeContacts,
			ArrayList<ClientSimpleContactModel> inactiveContacts,
			ArrayList<ClientPropertyModel> properties, ArrayList<ClientRuleModel> rules,
			ArrayList<ClientEmergencyNumberModel> addedEmergencyNumbers,
			ArrayList<ClientEmergencyNumberModel> deletedEmergencyNumbers, ArrayList<String> deletedRules) {
		super();
		this.key = key;
		this.activeContacts = activeContacts;
		this.inactiveContacts = inactiveContacts;
		this.properties = properties;
		this.rules = rules;
		this.addedEmergencyNumbers = addedEmergencyNumbers;
		this.deletedEmergencyNumbers = deletedEmergencyNumbers;
		this.deletedRules = deletedRules;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public ArrayList<ClientSimpleContactModel> getActiveContacts() {
		return activeContacts;
	}

	public void setActiveContacts(ArrayList<ClientSimpleContactModel> activeContacts) {
		this.activeContacts = activeContacts;
	}

	public ArrayList<ClientSimpleContactModel> getInactiveContacts() {
		return inactiveContacts;
	}

	public void setInactiveContacts(ArrayList<ClientSimpleContactModel> inactiveContacts) {
		this.inactiveContacts = inactiveContacts;
	}

	public ArrayList<ClientPropertyModel> getProperties() {
		return properties;
	}

	public void setProperties(ArrayList<ClientPropertyModel> properties) {
		this.properties = properties;
	}

	public ArrayList<ClientRuleModel> getRules() {
		return rules;
	}

	public void setRules(ArrayList<ClientRuleModel> rules) {
		this.rules = rules;
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
	
	public String getSmartphoneName() {
		return smartphoneName;
	}

	public void setSmartphoneName(String smartphoneName) {
		this.smartphoneName = smartphoneName;
	}

	public ArrayList<String> getDeletedRules() {
		return deletedRules;
	}

	public void setDeletedRules(ArrayList<String> deletedRules) {
		this.deletedRules = deletedRules;
	}	
}
