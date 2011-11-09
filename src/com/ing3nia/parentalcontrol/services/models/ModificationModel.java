package com.ing3nia.parentalcontrol.services.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.jdo.PersistenceManager;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.ing3nia.parentalcontrol.models.PCEmergencyNumber;
import com.ing3nia.parentalcontrol.models.PCModification;
import com.ing3nia.parentalcontrol.models.PCPhone;
import com.ing3nia.parentalcontrol.models.PCProperty;
import com.ing3nia.parentalcontrol.models.PCRule;
import com.ing3nia.parentalcontrol.models.PCSimpleContact;
import com.ing3nia.parentalcontrol.services.exceptions.SessionQueryException;
import com.ing3nia.parentalcontrol.services.utils.ModelLogger;
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
	
	private ArrayList<String> deletedRules;

	public ModificationModel() {
		super();
	}

	public ModificationModel(String key, String smartphoneName, ArrayList<SimpleContactModel> activeContacts,
			ArrayList<SimpleContactModel> inactiveContacts,
			ArrayList<PropertyModel> properties, ArrayList<RuleModel> rules,
			ArrayList<EmergencyNumberModel> addedEmergencyNumbers,
			ArrayList<EmergencyNumberModel> deletedEmergencyNumbers, ArrayList<String> deletedRules) {
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
	
	public ModificationModel(String key, ArrayList<SimpleContactModel> activeContacts,
			ArrayList<SimpleContactModel> inactiveContacts,
			ArrayList<PropertyModel> properties, ArrayList<RuleModel> rules,
			ArrayList<EmergencyNumberModel> addedEmergencyNumbers,
			ArrayList<EmergencyNumberModel> deletedEmergencyNumbers, ArrayList<String> deletedRules) {
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

	public ArrayList<String> getDeletedRules() {
		return deletedRules;
	}

	public void setDeletedRules(ArrayList<String> deletedRules) {
		this.deletedRules = deletedRules;
	}
	
	public static ModificationModel convertToModificationModel(PCModification modification) throws IllegalArgumentException, SessionQueryException {
		ModificationModel modificationModel = new ModificationModel();
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
		ArrayList<Key> auxKeyList;
		PCSimpleContact auxSimpleContact;
		ArrayList<SimpleContactModel> pcContacts;
		PCEmergencyNumber auxEmergencyNumber;
		ArrayList<EmergencyNumberModel> emergencyNumbers;
		
		try {
			//------------------------------------------------------------
			// Setting modification key;
			//------------------------------------------------------------
			modificationModel.setKey(KeyFactory.keyToString(modification.getKey()));
			
			//------------------------------------------------------------
			// Loading active contacts
			//------------------------------------------------------------
			auxKeyList = modification.getActiveContacts();
			pcContacts = new ArrayList<SimpleContactModel>();
			
			if (auxKeyList != null) {
				for (Key key : auxKeyList) {
					auxSimpleContact = (PCSimpleContact)pm.getObjectById(PCSimpleContact.class, key);
					pcContacts.add(SimpleContactModel.convertToSimpleContactModel(auxSimpleContact));
				}
			}			
			
			modificationModel.setActiveContacts(pcContacts);
			
			//------------------------------------------------------------
			// Loading inactive contacts
			//------------------------------------------------------------
			auxKeyList = modification.getInactiveContacts();
			pcContacts = new ArrayList<SimpleContactModel>();
			
			if (auxKeyList != null) {
				for (Key key : auxKeyList) {
					auxSimpleContact = (PCSimpleContact)pm.getObjectById(PCSimpleContact.class, key);
					pcContacts.add(SimpleContactModel.convertToSimpleContactModel(auxSimpleContact));
				}
			}
			
			modificationModel.setInactiveContacts(pcContacts);
			
			//------------------------------------------------------------
			// Loading added emergency numbers
			//------------------------------------------------------------
			auxKeyList = modification.getAddedEmergencyNumbers();
			emergencyNumbers = new ArrayList<EmergencyNumberModel>();
			
			if (auxKeyList != null) {
				for (Key key : auxKeyList) {
					auxEmergencyNumber = (PCEmergencyNumber)pm.getObjectById(PCEmergencyNumber.class, key);
					emergencyNumbers.add(EmergencyNumberModel.convertToEmergencyNumberModel(auxEmergencyNumber));
				}
			}
			
			modificationModel.setAddedEmergencyNumbers(emergencyNumbers);
			
			//------------------------------------------------------------
			// Loading deleted emergency numbers
			//------------------------------------------------------------
			auxKeyList = modification.getDeletedEmergencyNumbers();
			emergencyNumbers = new ArrayList<EmergencyNumberModel>();
			
			if (auxKeyList != null) {
				for (Key key : auxKeyList) {
					auxEmergencyNumber = (PCEmergencyNumber)pm.getObjectById(PCEmergencyNumber.class, key);
					emergencyNumbers.add(EmergencyNumberModel.convertToEmergencyNumberModel(auxEmergencyNumber));
				}
			}
			
			modificationModel.setDeletedEmergencyNumbers(emergencyNumbers);
			
			//------------------------------------------------------------
			// Loading properties
			//------------------------------------------------------------
			ArrayList<PropertyModel> properties = new ArrayList<PropertyModel>();
			ArrayList<Key> pcProperties = modification.getProperties();
			PCProperty auxProperty;
			
			if (auxKeyList != null) {
				for (Key property : pcProperties) {
					auxProperty = (PCProperty)pm.getObjectById(PCProperty.class, property);
					properties.add(PropertyModel.convertToPropertyModel(auxProperty));
				}
			}
			
			modificationModel.setProperties(properties);
			
			//------------------------------------------------------------
			// Loading rules
			//------------------------------------------------------------
			ArrayList<RuleModel> rules = new ArrayList<RuleModel>();
			ArrayList<Key> pcRules = modification.getRules();
			PCRule auxRule;
			
			if (auxKeyList != null) {
				for (Key rule : pcRules) {
					auxRule = (PCRule)pm.getObjectById(PCRule.class, rule);
					rules.add(RuleModel.convertToRuleModel(auxRule));
				}
			}
			
			modificationModel.setRules(rules);
			
			ArrayList<String> deletedRules = modification.getDeletedRules();
			
			if (deletedRules != null) {
				modificationModel.setDeletedRules(deletedRules);
			}
			else {
				modificationModel.setDeletedRules(new ArrayList<String>());
			}
			
		} 		
		catch (IllegalArgumentException ex) {
			throw ex;
		}
		catch (Exception ex) {
	    	ModelLogger.logger.info("[Update Synchronization Service] An error ocurred while finding contacts or emergency numbers by key " + ex.getMessage());
	    	
			throw new SessionQueryException();
	    }
		
		pm.close();
		
		return modificationModel;
	}
}
