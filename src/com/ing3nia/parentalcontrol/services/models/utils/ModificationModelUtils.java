package com.ing3nia.parentalcontrol.services.models.utils;

import java.util.ArrayList;

import javax.jdo.PersistenceManager;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.ing3nia.parentalcontrol.client.models.EmergencyNumberModel;
import com.ing3nia.parentalcontrol.client.models.ModificationModel;
import com.ing3nia.parentalcontrol.client.models.PropertyModel;
import com.ing3nia.parentalcontrol.client.models.RuleModel;
import com.ing3nia.parentalcontrol.client.models.SimpleContactModel;
import com.ing3nia.parentalcontrol.models.PCEmergencyNumber;
import com.ing3nia.parentalcontrol.models.PCModification;
import com.ing3nia.parentalcontrol.models.PCProperty;
import com.ing3nia.parentalcontrol.models.PCRule;
import com.ing3nia.parentalcontrol.models.PCSimpleContact;
import com.ing3nia.parentalcontrol.services.exceptions.SessionQueryException;
import com.ing3nia.parentalcontrol.services.utils.ModelLogger;
import com.ing3nia.parentalcontrol.services.utils.ServiceUtils;

public class ModificationModelUtils {

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
					pcContacts.add(SimpleContactModelUtils.convertToSimpleContactModel(auxSimpleContact));
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
					pcContacts.add(SimpleContactModelUtils.convertToSimpleContactModel(auxSimpleContact));
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
					emergencyNumbers.add(EmergencyNumberModelUtils.convertToEmergencyNumberModel(auxEmergencyNumber));
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
					emergencyNumbers.add(EmergencyNumberModelUtils.convertToEmergencyNumberModel(auxEmergencyNumber));
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
					properties.add(PropertyModelUtils.convertToPropertyModel(auxProperty));
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
					rules.add(RuleModelUtils.convertToRuleModel(auxRule));
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
