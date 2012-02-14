package com.ing3nia.parentalcontrol.services.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PhoneNumber;
import com.ing3nia.parentalcontrol.client.models.EmergencyNumberModel;
import com.ing3nia.parentalcontrol.client.models.ModificationModel;
import com.ing3nia.parentalcontrol.client.models.PropertyModel;
import com.ing3nia.parentalcontrol.client.models.RuleModel;
import com.ing3nia.parentalcontrol.client.models.SimpleContactModel;
import com.ing3nia.parentalcontrol.client.utils.ModelLogger;
import com.ing3nia.parentalcontrol.models.PCEmergencyNumber;
import com.ing3nia.parentalcontrol.models.PCFunctionality;
import com.ing3nia.parentalcontrol.models.PCModification;
import com.ing3nia.parentalcontrol.models.PCProperty;
import com.ing3nia.parentalcontrol.models.PCRule;
import com.ing3nia.parentalcontrol.models.PCSmartphone;
import com.ing3nia.parentalcontrol.models.PCUser;
import com.ing3nia.parentalcontrol.services.exceptions.ModificationParsingException;
import com.ing3nia.parentalcontrol.services.exceptions.SessionQueryException;

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
	 * @throws ModificationParsingException 
	 */
	public static void ProcessParentModifications(PersistenceManager pm, PCSmartphone pcsmartphone, ModificationModel modifications) throws ModificationParsingException{		
		PCModification pcmodification = pm.getObjectById(PCModification.class, pcsmartphone.getModification());
		Logger logger = ModelLogger.logger;

		logger.info("[ParentModifications] Applying parent modifications");
		if (pcmodification == null) {
			try {
				logger.info("[ParentModifications] Create new parent modification");
				createNewParentModification(pm, pcsmartphone, modifications);
			} catch (ModificationParsingException e) {
				logger.severe("[ParentModifications] New modification could not be created ");
				throw e;
			}
		} else {
			try {
				logger.info("[ParentModifications] Updating parent modification");
				updateParentModification(pm, pcsmartphone, pcmodification, modifications);
			} catch (ModificationParsingException e) {
				logger.severe("[ParentModifications] Modification could not be updated ");
				throw e;
			}
		}
	}
	
	private static void createNewParentModification(PersistenceManager pm, PCSmartphone pcSmartphone, ModificationModel modifications) throws ModificationParsingException{
		PCModification pcmodification = new PCModification();
		updateParentModification(pm, pcSmartphone, pcmodification, modifications);
		
		pm.makePersistent(pcmodification);
		
		pcSmartphone.setModification(pcmodification.getKey());
	}
	
	private static void updateParentModification(PersistenceManager pm, PCSmartphone pcsmartphone, PCModification pcmodification, ModificationModel modifications) throws ModificationParsingException{
		
		Logger logger = ModelLogger.logger;
		
		String smartphoneName = modifications.getSmartphoneName();
		ArrayList<SimpleContactModel> activeContacts = modifications.getActiveContacts();
		ArrayList<SimpleContactModel> inactiveContacts = modifications.getInactiveContacts();
		ArrayList<EmergencyNumberModel> addedEmergencyNumbers = modifications.getAddedEmergencyNumbers();
		ArrayList<EmergencyNumberModel> deletedEmergencyNumbers = modifications.getDeletedEmergencyNumbers();
		ArrayList<PropertyModel> properties = modifications.getProperties();
		ArrayList<RuleModel> rules = modifications.getRules();
		ArrayList<String> deletedRules = modifications.getDeletedRules();
		
		ArrayList<Key> pcModActiveContacts = pcmodification.getActiveContacts();
		ArrayList<Key> pcModInactiveContacts = pcmodification.getInactiveContacts();
		ArrayList<Key> pcModAddedEmergencyNumbers = pcmodification.getAddedEmergencyNumbers();
		ArrayList<Key> pcModDeletedEmergencyNumbers = pcmodification.getDeletedEmergencyNumbers();
		ArrayList<Key> pcModProperties = pcmodification.getProperties();
		ArrayList<Key> pcModRules = pcmodification.getRules();
		ArrayList<String> pcModDeletedRules = pcmodification.getDeletedRules();
		
		if(smartphoneName != null){
			pcsmartphone.setName(smartphoneName);
		}
		
		// setting to empty lists null modification attributes
		if (activeContacts == null) {
			modifications.setActiveContacts(new ArrayList<SimpleContactModel>());
			activeContacts = modifications.getActiveContacts();
		}
		if (inactiveContacts == null){
			modifications.setInactiveContacts(new ArrayList<SimpleContactModel>());
			inactiveContacts = modifications.getInactiveContacts();
		}
		if (addedEmergencyNumbers == null){
			modifications.setAddedEmergencyNumbers(new ArrayList<EmergencyNumberModel>());
			addedEmergencyNumbers = modifications.getAddedEmergencyNumbers();
		}
		if (deletedEmergencyNumbers == null){
			modifications.setDeletedEmergencyNumbers(new ArrayList<EmergencyNumberModel>());
			deletedEmergencyNumbers = modifications.getDeletedEmergencyNumbers();
		}
		if (properties == null){
			modifications.setProperties(new ArrayList<PropertyModel>());
			properties = modifications.getProperties();
		}
		if (rules == null){
			modifications.setRules(new ArrayList<RuleModel>());
			rules = modifications.getRules();
		}
		
		if(deletedRules == null){
			modifications.setDeletedRules(new ArrayList<String>());
			deletedRules = modifications.getDeletedRules();
		}
		
		// setting to empty lists null PCModification attributes
		if (pcModActiveContacts == null) {
			pcmodification.setActiveContacts(new ArrayList<Key>());
			pcModActiveContacts = pcmodification.getActiveContacts();
		}
		if (pcModInactiveContacts == null){
			pcmodification.setInactiveContacts(new ArrayList<Key>());
			pcModInactiveContacts = pcmodification.getInactiveContacts();
		}
		if (pcModAddedEmergencyNumbers == null){
			pcmodification.setAddedEmergencyNumbers(new ArrayList<Key>());
			pcModAddedEmergencyNumbers = pcmodification.getAddedEmergencyNumbers();
		}
		if (pcModDeletedEmergencyNumbers == null){
			pcmodification.setDeletedEmergencyNumbers(new ArrayList<Key>());
			pcModDeletedEmergencyNumbers = pcmodification.getDeletedEmergencyNumbers();
		}
		if (pcModProperties == null){
			pcmodification.setProperties(new ArrayList<Key>());
			pcModProperties = pcmodification.getProperties();
		}
		if (pcModRules == null){
			pcmodification.setRules(new ArrayList<Key>());
			pcModRules = pcmodification.getRules();
		}
		if(pcModDeletedRules == null){
			pcmodification.setDeletedRules(new ArrayList<String>());
			pcModDeletedRules = pcmodification.getDeletedRules();
		}

		logger.info("[ParentModifications] Adding active contacts modifications");
		// if the parent enabled any contact, add it to smartphone and modifications
		ArrayList<Key> modActiveList = pcmodification.getActiveContacts();
		ArrayList<Key> modInactiveList = pcmodification.getInactiveContacts();
		for (SimpleContactModel contact : activeContacts) {
			// remove contact from inactive and add to active in smartphone
			Key contactKey = KeyFactory.stringToKey(contact.getKeyId());
			pcsmartphone.getInactiveContacts().remove(contactKey);
			if(!pcsmartphone.getActiveContacts().contains(contactKey)){
				pcsmartphone.getActiveContacts().add(contactKey);
			}

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
		
		logger.info("[ParentModifications] Adding inactive contacts modifications");
		// if the parent disabled any contact, add it to smartphone and
		// modifications
		modActiveList = pcmodification.getActiveContacts();
		modInactiveList = pcmodification.getInactiveContacts();
		for (SimpleContactModel contact : inactiveContacts) {
			// remove contact from active and add to inactive in smartphone
			Key contactKey = KeyFactory.stringToKey(contact.getKeyId());
			pcsmartphone.getActiveContacts().remove(contactKey);
			if(!pcsmartphone.getInactiveContacts().contains(contactKey)){
				pcsmartphone.getInactiveContacts().add(contactKey);
			}

			// check if inactive contact existed in modification, if active
			// then remove it and add it to inactive, otherwise add it to
			// inactive list
			if (modInactiveList.contains(contactKey)) {
				// skip
			} else if (modActiveList.contains(contactKey)) {
				modActiveList.remove(contactKey);
				modInactiveList.add(contactKey);
			} else {
				modInactiveList.add(contactKey);
			}
		}
		
		logger.info("[ParentModifications] Adding emergency contacts modifications");
		// if the parent enabled any emergency number, add it to smartphone and
		// modifications

		ArrayList<Key> modAddedEmergency = pcmodification.getAddedEmergencyNumbers();
		ArrayList<Key> modDeletedEmergency = pcmodification.getDeletedEmergencyNumbers();
		for (EmergencyNumberModel emergencyContact : addedEmergencyNumbers) {
			if (emergencyContact.getKeyId() == null) {
				//New emergency contact. First save. Then process.
				PCEmergencyNumber newEmergencyNumber = new PCEmergencyNumber();
				newEmergencyNumber.setCountry(emergencyContact.getCountry());
				newEmergencyNumber.setNumber(new PhoneNumber(emergencyContact.getNumber()));
				newEmergencyNumber.setDescription(emergencyContact.getDescription());
				
				pm.makePersistent(newEmergencyNumber);
				
				pcsmartphone.getAddedEmergencyNumbers().add(newEmergencyNumber.getKey());
				modAddedEmergency.add(newEmergencyNumber.getKey());
			}
			else {
				// remove contact from deleted emergency number and add to added
				// emergency in smartphone
				Key emergencyKey = KeyFactory.stringToKey(emergencyContact.getKeyId());
				pcsmartphone.getDeletedEmergencyNumbers().remove(emergencyKey);
				
				if(!pcsmartphone.getAddedEmergencyNumbers().contains(emergencyKey)){
					pcsmartphone.getAddedEmergencyNumbers().add(emergencyKey);
				}

				// check if added emergency contact existed in modification, if
				// deleted
				// then remove it and add it to added, otherwise add it to added
				// emergency list
				if (modAddedEmergency.contains(emergencyKey)) {
					// skip
				} 
				else if (modDeletedEmergency.contains(emergencyKey)) {
					modDeletedEmergency.remove(emergencyKey);
					modAddedEmergency.add(emergencyKey);
				} 
				else {
					modAddedEmergency.add(emergencyKey);
				}
			}
		}
		
		logger.info("[ParentModifications] Adding deleted emergency contacts modifications");
		// if the parent disabled any emergency number, add it to smartphone and
		// modifications
		modAddedEmergency = pcmodification.getAddedEmergencyNumbers();
		modDeletedEmergency = pcmodification.getDeletedEmergencyNumbers();
		for (EmergencyNumberModel emergencyContact : deletedEmergencyNumbers) {
			// remove contact from added emergency number and add to deleted
			// emergency in smartphone
			Key emergencyKey = KeyFactory.stringToKey(emergencyContact
					.getKeyId());
			pcsmartphone.getAddedEmergencyNumbers().remove(emergencyKey);
			if(!pcsmartphone.getDeletedEmergencyNumbers().contains(emergencyKey)){
				pcsmartphone.getDeletedEmergencyNumbers().add(emergencyKey);
			}

			// check if emergency contact existed in modification, if added
			// then remove it and add it to deleted, otherwise add it to deleted
			// emergency list

			if (modDeletedEmergency.contains(emergencyKey)) {
				// skip
			} else if (modAddedEmergency.contains(emergencyKey)) {
				modAddedEmergency.remove(emergencyKey);
				modDeletedEmergency.add(emergencyKey);
			} else {
				modDeletedEmergency.add(emergencyKey);
			}
		}
			
		// parsing property modifications
		logger.info("[ParentModifications] Adding properties modifications");
		for (PropertyModel propmodel : modifications.getProperties()) {
			PCProperty property;
			try{
				property= pm.getObjectById(PCProperty.class,KeyFactory.stringToKey(propmodel.getKeyId()));
			} catch(Exception e){
				logger.severe("[ParentModifications] Error while obtaining property from key: "+propmodel.getKeyId());
				throw new ModificationParsingException(e.getMessage());
			}
			property.setValue(propmodel.getValue());
			pcModProperties.add(property.getKey());
		}
		
		// parsing rule modifications
		logger.info("[ParentModifications] Adding rules modifications");
		for (RuleModel ruleModel : rules) {
			PCRule rule;
			
			if (ruleModel.getKeyId() == null) {
				rule = new PCRule();
			}
			else {
				try {
					rule = pm.getObjectById(PCRule.class, KeyFactory.stringToKey(ruleModel.getKeyId()));
				} 
				catch (Exception e) {
					throw new ModificationParsingException("Could not get rule from key: " + ruleModel.getKeyId() + " " + e.getMessage());
				}
			}
			
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
			Date date;
			
			try {
				date = sdf.parse(ruleModel.getCreationDate());
			} 
			catch (ParseException e) {
				logger.severe("[ParentModifications] Error while parsing rule creating date: " + ruleModel.getCreationDate());
				throw new ModificationParsingException("Date parsing error: " + ruleModel.getCreationDate() + " " + e.getMessage());
			}
			
			rule.setCreationDate(date);
			
			try {
				date = sdf.parse(ruleModel.getStartDate());
			} 
			catch (ParseException e) {
				logger.severe("[ParentModifications] Error while parsing rule start date: " + ruleModel.getStartDate());
				throw new ModificationParsingException("Date parsing error: " + ruleModel.getStartDate() + " " + e.getMessage()); 
			}
			
			rule.setStartDate(date);
			
			try {
				date = sdf.parse(ruleModel.getEndDate());
			} 
			catch (ParseException e) {
				logger.severe("[ParentModifications] Error while parsing rule end date: " + ruleModel.getEndDate());
				throw new ModificationParsingException("Date parsing error: " + ruleModel.getEndDate() + " " + e.getMessage());
			}
			
			rule.setEndDate(date);
			rule.setName(ruleModel.getName());
			
			logger.info("[ParentModifications] Setting new funcionalities to rules");
			ArrayList<Key> newDisabledFuncionalities = getNewFuncionalitiesAsKeys(pm, ruleModel);
			rule.setDisabledFunctionalities(newDisabledFuncionalities);		
			
			if (ruleModel.getKeyId() == null) {
				pm.makePersistent(rule);
			}
			
			pcModRules.add(rule.getKey());
		}
		
		// adding deleted rules
		logger.info("[ParentModifications] Setting deleted rules");
		for(String deletedRuleId : modifications.getDeletedRules()){
			if(!pcModDeletedRules.contains(deletedRuleId)){
				pcModDeletedRules.add(deletedRuleId);
			}
		}
		
		logger.severe("RULES ADDED: "+pcModRules.size()+" "+pcmodification.getRules());
		
//		pcsmartphone.setModification(pcmodification);
		//pm.makePersistent(pcsmartphone);
		//pm.makePersistent(pcmodification);	
	}
	
	private static ArrayList<Key> getNewFuncionalitiesAsKeys(PersistenceManager pm,
			RuleModel ruleModel) throws ModificationParsingException {
		Logger logger = ModelLogger.logger;

		Query query = pm.newQuery(PCFunctionality.class);
		query.setFilter("id == id_param");
		query.declareParameters("int id_param");
		query.setRange(0, 1);

		int idFuncionality;

		ArrayList<Key> newDisabledFuncionalities = new ArrayList<Key>();
		for (Integer idFunc : ruleModel.getDisabledFunctionalities()) {
			PCFunctionality funcionality;

			idFuncionality = idFunc;
			logger.info("[ParentModification] Finding funcionality by id: "
					+ idFuncionality);
			try {
				List<PCFunctionality> results = (List<PCFunctionality>) query.execute(idFuncionality);
				if (!results.isEmpty()) {
					logger.info("[ParentModification] Returning found PCFuncionality");
					funcionality = results.get(0);
				} else {
					logger.severe("[ParentModification] No funcionality with id: "
							+ idFuncionality + " was found ");
					throw new ModificationParsingException();
				}
			} catch (Exception e) {
				logger.info("[ParentModifications] Could not find funcionality from id: "
						+ idFuncionality + " " + e.getMessage());
				throw new ModificationParsingException();
			}
			newDisabledFuncionalities.add(funcionality.getKey());
		}
		return newDisabledFuncionalities;
	}
	
	

}


