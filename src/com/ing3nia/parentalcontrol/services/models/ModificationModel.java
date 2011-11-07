package com.ing3nia.parentalcontrol.services.models;

import java.util.ArrayList;

import javax.jdo.PersistenceManager;

import com.google.appengine.api.datastore.KeyFactory;
import com.ing3nia.parentalcontrol.models.PCContact;
import com.ing3nia.parentalcontrol.models.PCEmergencyNumber;
import com.ing3nia.parentalcontrol.models.PCModification;
import com.ing3nia.parentalcontrol.models.PCProperty;
import com.ing3nia.parentalcontrol.models.PCRule;
import com.ing3nia.parentalcontrol.models.PCSimpleContact;
import com.ing3nia.parentalcontrol.services.utils.ServiceUtils;

public class ModificationModel {
	private String key;
	
	private String smartphoneName;
	
	private ArrayList<SimpleContactModel> activeContacts;
	
	private ArrayList<SimpleContactModel> inactiveContacts;
	
	private ArrayList<PropertyModel> properties;
	
	private ArrayList<RuleModel> rules;
	
	private ArrayList<EmergencyNumberModel> addedEmergencyNumbers;
	
	private ArrayList<EmergencyNumberModel> deletedEmergencyNumbers;

	public ModificationModel() {
		super();
	}

	public ModificationModel(String key, String smartphoneName, ArrayList<SimpleContactModel> activeContacts,
			ArrayList<SimpleContactModel> inactiveContacts,
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
		this.smartphoneName = smartphoneName;
	}
	
	public ModificationModel(String key, ArrayList<SimpleContactModel> activeContacts,
			ArrayList<SimpleContactModel> inactiveContacts,
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

	public ArrayList<SimpleContactModel> getActiveContacts() {
		return activeContacts;
	}

	public void setActiveContacts(ArrayList<SimpleContactModel> activeContacts) {
		this.activeContacts = activeContacts;
	}

	public ArrayList<SimpleContactModel> getInactiveContacts() {
		return inactiveContacts;
	}

	public void setInactiveContacts(ArrayList<SimpleContactModel> inactiveContacts) {
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
	
	public String getSmartphoneName() {
		return smartphoneName;
	}

	public void setSmartphoneName(String smartphoneName) {
		this.smartphoneName = smartphoneName;
	}

	public static ModificationModel convertToModificationModel(PCModification modification) {
		ModificationModel modificationModel = new ModificationModel();
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
		
		modificationModel.setKey(KeyFactory.keyToString(modification.getKey()));
		
		ArrayList<PCSimpleContact> pcActiveContacts = (ArrayList<PCSimpleContact>)pm.getObjectsById(modification.getActiveContacts());
		ArrayList<SimpleContactModel> activeContacts = new ArrayList<SimpleContactModel>();
		
		for (PCSimpleContact contact : pcActiveContacts) {
			activeContacts.add(SimpleContactModel.convertToSimpleContactModel(contact));
		}
		
		modificationModel.setActiveContacts(activeContacts);
		
		ArrayList<PCSimpleContact> pcInactiveContacts = (ArrayList<PCSimpleContact>)pm.getObjectsById(modification.getInactiveContacts());
		ArrayList<SimpleContactModel> inactiveContacts = new ArrayList<SimpleContactModel>();
		
		for (PCSimpleContact contact : pcInactiveContacts) {
			inactiveContacts.add(SimpleContactModel.convertToSimpleContactModel(contact));
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
		
		ArrayList<PCProperty> propertyList = (ArrayList<PCProperty>)pm.getObjectById(modification.getProperties());
		for (PCProperty property : propertyList) {
			properties.add(PropertyModel.convertToPropertyModel(property));
		}
		
		modificationModel.setProperties(properties);
		
		ArrayList<RuleModel> rules = new ArrayList<RuleModel>();
		ArrayList<PCRule> rulesList = (ArrayList<PCRule>)pm.getObjectById(modification.getRules());
		for (PCRule rule : rulesList) {
			rules.add(RuleModel.convertToRuleModel(rule));
		}
		
		modificationModel.setRules(rules);
		
		pm.close();
		
		return modificationModel;
	}
}
