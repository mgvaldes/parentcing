package com.ing3nia.parentalcontrol.services.utils;

import java.util.ArrayList;

import javax.jdo.PersistenceManager;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.ing3nia.parentalcontrol.models.PCModification;
import com.ing3nia.parentalcontrol.models.PCProperty;
import com.ing3nia.parentalcontrol.models.PCSmartphone;
import com.ing3nia.parentalcontrol.services.models.EmergencyNumberModel;
import com.ing3nia.parentalcontrol.services.models.ModificationModel;
import com.ing3nia.parentalcontrol.services.models.PropertyModel;
import com.ing3nia.parentalcontrol.services.models.RuleModel;
import com.ing3nia.parentalcontrol.services.models.SimpleContactModel;

/**
 * Contain a series of utils methods to manipulate and query smarpthone modifications
 * 
 * @author Ing3nia
 * @author Javier Fernandez
 * @author Maria Gabriela Valdes
 * 
 * @version 1.0
 *
 */
public class ModificationUtils {
	/**
	 * Processes the modifications made by the parent user on a child smartphone
	 */
	public PCModification ProcessParentModifications(PCSmartphone pcsmartphone, ModificationModel modifications){
		PCModification pcmodification = pcsmartphone.getModification();
		if(pcmodification == null){
			pcmodification = createNewParentModification(modifications);
			return pcmodification;
		}
		return null;
	}
	
	private PCModification createNewParentModification(ModificationModel modifications){
		return null;
	}
	
	private void updateParentModification(PCSmartphone pcsmartphone, PCModification pcmodification, ModificationModel modifications){
		String smartphoneName = modifications.getSmartphoneName();
		ArrayList<SimpleContactModel> activeContacts = modifications.getActiveContacts();
		ArrayList<SimpleContactModel> inactiveContacts = modifications.getInactiveContacts();
		ArrayList<EmergencyNumberModel> addedEmergencyNumbers = modifications.getAddedEmergencyNumbers();
		ArrayList<EmergencyNumberModel> deletedEmergencyNumbers = modifications.getDeletedEmergencyNumbers();
		ArrayList<PropertyModel> properties = modifications.getProperties();
		ArrayList<RuleModel> rules = modifications.getRules();
		
		if(smartphoneName != null){
			pcsmartphone.setName(smartphoneName);
		}
		
		// if the parent enabled any contact, add it to smartphone and modifications
		if (activeContacts != null) {
			ArrayList<Key> modActiveList = pcmodification.getActiveContacts();
			ArrayList<Key> modInactiveList = pcmodification.getInactiveContacts();
			for (SimpleContactModel contact : activeContacts) {
				// remove contact from inactive and add to active in smartphone
				Key contactKey = KeyFactory.stringToKey(contact.getKeyId());
				pcsmartphone.getInactiveContacts().remove(contactKey);
				pcsmartphone.getActiveContacts().add(contactKey);

				// check if active contact existed in modification, if inactive
				// then remove
				// it and add it to active, otherwise add it to active list
				if (modActiveList.contains(contactKey)) {
					// skip
				} else if (modInactiveList.contains(contactKey)) {
					modInactiveList.remove(contactKey);
					modActiveList.add(contactKey);
				} else {
					modActiveList.add(contactKey);
				}
			}
		}
		
		// if the parent disabled any contact, add it to smartphone and modifications
		if (inactiveContacts != null) {
			ArrayList<Key> modActiveList = pcmodification.getActiveContacts();
			ArrayList<Key> modInactiveList = pcmodification.getInactiveContacts();
			for (SimpleContactModel contact : inactiveContacts) {
				// remove contact from active and add to inactive in smartphone
				Key contactKey = KeyFactory.stringToKey(contact.getKeyId());
				pcsmartphone.getActiveContacts().remove(contactKey);
				pcsmartphone.getInactiveContacts().add(contactKey);

				// check if inactive contact existed in modification, if active
				// then remove it and add it to inactive, otherwise add it to inactive list
				if (modInactiveList.contains(contactKey)) {
					// skip
				} else if (modActiveList.contains(contactKey)) {
					modActiveList.remove(contactKey);
					modInactiveList.add(contactKey);
				} else {
					modInactiveList.add(contactKey);
				}
			}
		}
		
		// if the parent enabled any emergency number, add it to smartphone and modifications
		if (addedEmergencyNumbers != null) {
			ArrayList<Key> modAddedEmergency = pcmodification.getAddedEmergencyNumbers();
			ArrayList<Key> modDeletedEmergency = pcmodification.getDeletedEmergencyNumbers();
			for (EmergencyNumberModel emergencyContact : addedEmergencyNumbers) {
				// remove contact from deleted emergency number and add to added emergency in smartphone
				Key emergencyKey = KeyFactory.stringToKey(emergencyContact.getKeyId());
				pcsmartphone.getDeletedEmergencyNumbers().remove(emergencyKey);
				pcsmartphone.getActiveContacts().add(emergencyKey);

				// check if added emergency contact existed in modification, if deleted
				// then remove it and add it to added, otherwise add it to added emergency list
				if (modAddedEmergency.contains(emergencyKey)) {
					// skip
				} else if (modDeletedEmergency.contains(emergencyKey)) {
					modDeletedEmergency.remove(emergencyKey);
					modAddedEmergency.add(emergencyKey);
				} else {
					modAddedEmergency.add(emergencyKey);
				}
			}
		}
		
		// if the parent disabled any emergency number, add it to smartphone and modifications
		if (deletedEmergencyNumbers != null) {
			ArrayList<Key> modAddedEmergency = pcmodification.getAddedEmergencyNumbers();
			ArrayList<Key> modDeletedEmergency = pcmodification.getDeletedEmergencyNumbers();
			for (EmergencyNumberModel emergencyContact : deletedEmergencyNumbers) {
				// remove contact from added emergency number and add to deleted emergency in smartphone
				Key emergencyKey = KeyFactory.stringToKey(emergencyContact.getKeyId());
				pcsmartphone.getAddedEmergencyNumbers().remove(emergencyKey);
				pcsmartphone.getDeletedEmergencyNumbers().add(emergencyKey);

				// check if  emergency contact existed in modification, if added
				// then remove it and add it to deleted, otherwise add it to deleted emergency list

				if (modDeletedEmergency.contains(emergencyKey)) {
					// skip
				} else if (modAddedEmergency.contains(emergencyKey)) {
					modAddedEmergency.remove(emergencyKey);
					modDeletedEmergency.add(emergencyKey);
				} else {
					modDeletedEmergency.add(emergencyKey);
				}
			}
		}
		
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
		
		// parsing property modifications
		for (PropertyModel propmodel : modifications.getProperties()) {
			PCProperty property = pm.getObjectById(PCProperty.class,
					KeyFactory.stringToKey(propmodel.getKeyId()));
			property.setValue(propmodel.getValue());
		}
		
		// parsing property modifications
		for (PropertyModel propmodel : modifications.getProperties()) {
			PCProperty property = pm.getObjectById(PCProperty.class,
					KeyFactory.stringToKey(propmodel.getKeyId()));
			property.setValue(propmodel.getValue());
		}
		
		// parsing property modifications
		for (PropertyModel propmodel : modifications.getProperties()) {
			PCProperty property = pm.getObjectById(PCProperty.class,
					KeyFactory.stringToKey(propmodel.getKeyId()));
			property.setValue(propmodel.getValue());
		}
		
		
		

		pm.close();
	}
	
}


