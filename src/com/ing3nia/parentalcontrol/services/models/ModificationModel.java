package com.ing3nia.parentalcontrol.services.models;

import java.util.ArrayList;

import javax.jdo.PersistenceManager;

import com.google.appengine.api.datastore.KeyFactory;
import com.ing3nia.parentalcontrol.models.PCContact;
import com.ing3nia.parentalcontrol.models.PCEmergencyNumber;
import com.ing3nia.parentalcontrol.models.PCModification;
import com.ing3nia.parentalcontrol.models.PCProperty;
import com.ing3nia.parentalcontrol.models.PCRule;
import com.ing3nia.parentalcontrol.services.utils.ServiceUtils;

public class ModificationModel {
	private String key;
	
	private ArrayList<ContactModel> activeContacts;
	
	private ArrayList<ContactModel> inactiveContacts;
	
	private ArrayList<PropertyModel> properties;
	
	private ArrayList<RuleModel> rules;
	
	private ArrayList<EmergencyNumberModel> addedEmergencyNumbers;
	
	private ArrayList<EmergencyNumberModel> deletedEmergencyNumbers;

	public ModificationModel() {
		super();
	}

	public ModificationModel(String key, ArrayList<ContactModel> activeContacts,
			ArrayList<ContactModel> inactiveContacts,
			ArrayList<PropertyModel> properties, ArrayList<RuleModel> rules,
			ArrayList<EmergencyNumberModel> addedEmergencyNumbers,
			ArrayList<EmergencyNumberModel> deletedEmergencyNumbers) {
		super();
		this.key = key;
		this.activeContacts = activeContacts;
		this.inactiveContacts = inactiveContacts;
		this.properties = properties;
		this.rules = rules;
		this.addedEmergencyNumbers = addedEmergencyNumbers;
		this.deletedEmergencyNumbers = deletedEmergencyNumbers;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public ArrayList<ContactModel> getActiveContacts() {
		return activeContacts;
	}

	public void setActiveContacts(ArrayList<ContactModel> activeContacts) {
		this.activeContacts = activeContacts;
	}

	public ArrayList<ContactModel> getInactiveContacts() {
		return inactiveContacts;
	}

	public void setInactiveContacts(ArrayList<ContactModel> inactiveContacts) {
		this.inactiveContacts = inactiveContacts;
	}

	public ArrayList<PropertyModel> getProperties() {
		return properties;
	}

	public void setProperties(ArrayList<PropertyModel> properties) {
		this.properties = properties;
	}

	public ArrayList<RuleModel> getRules() {
		return rules;
	}

	public void setRules(ArrayList<RuleModel> rules) {
		this.rules = rules;
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
	
	public static ModificationModel convertToModificationModel(PCModification modification) {
		ModificationModel modificationModel = new ModificationModel();
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
		
		modificationModel.setKey(KeyFactory.keyToString(modification.getKey()));
		
		ArrayList<PCContact> pcActiveContacts = (ArrayList<PCContact>)pm.getObjectsById(modification.getActiveContacts());
		ArrayList<ContactModel> activeContacts = new ArrayList<ContactModel>();
		
		for (PCContact contact : pcActiveContacts) {
			activeContacts.add(ContactModel.convertToContactModel(contact));
		}
		
		modificationModel.setActiveContacts(activeContacts);
		
		ArrayList<PCContact> pcInactiveContacts = (ArrayList<PCContact>)pm.getObjectsById(modification.getInactiveContacts());
		ArrayList<ContactModel> inactiveContacts = new ArrayList<ContactModel>();
		
		for (PCContact contact : pcInactiveContacts) {
			inactiveContacts.add(ContactModel.convertToContactModel(contact));
		}
		
		modificationModel.setInactiveContacts(inactiveContacts);
		
		ArrayList<PCEmergencyNumber> pcAddedEmergencyNumbers = (ArrayList<PCEmergencyNumber>)pm.getObjectsById(modification.getAddedEmergencyNumbers());
		ArrayList<EmergencyNumberModel> addedEmergencyNumbers = new ArrayList<EmergencyNumberModel>();
		
		for (PCEmergencyNumber emergencyNumber : pcAddedEmergencyNumbers) {
			addedEmergencyNumbers.add(EmergencyNumberModel.convertToEmergencyNumberModel(emergencyNumber));
		}
		
		modificationModel.setAddedEmergencyNumbers(addedEmergencyNumbers);
		
		ArrayList<PCEmergencyNumber> pcDeletedEmergencyNumbers = (ArrayList<PCEmergencyNumber>)pm.getObjectsById(modification.getDeletedEmergencyNumbers());
		ArrayList<EmergencyNumberModel> deletedEmergencyNumbers = new ArrayList<EmergencyNumberModel>();
		
		for (PCEmergencyNumber emergencyNumber : pcDeletedEmergencyNumbers) {
			deletedEmergencyNumbers.add(EmergencyNumberModel.convertToEmergencyNumberModel(emergencyNumber));
		}
		
		modificationModel.setDeletedEmergencyNumbers(deletedEmergencyNumbers);
		
		ArrayList<PropertyModel> properties = new ArrayList<PropertyModel>();
		
		for (PCProperty property : modification.getProperties()) {
			properties.add(PropertyModel.convertToPropertyModel(property));
		}
		
		modificationModel.setProperties(properties);
		
		ArrayList<RuleModel> rules = new ArrayList<RuleModel>();
		
		for (PCRule rule : modification.getRules()) {
			rules.add(RuleModel.convertToRuleModel(rule));
		}
		
		modificationModel.setRules(rules);
		
		return modificationModel;
	}
}
