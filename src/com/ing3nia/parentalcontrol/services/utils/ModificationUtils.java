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
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheService.IdentifiableValue;
import com.google.gson.Gson;
import com.ing3nia.parentalcontrol.client.models.EmergencyNumberModel;
import com.ing3nia.parentalcontrol.client.models.ModificationModel;
import com.ing3nia.parentalcontrol.client.models.PropertyModel;
import com.ing3nia.parentalcontrol.client.models.RuleModel;
import com.ing3nia.parentalcontrol.client.models.SimpleContactModel;
import com.ing3nia.parentalcontrol.client.models.cache.SmartphoneCacheModel;
import com.ing3nia.parentalcontrol.client.models.cache.SmartphoneCacheParams;
import com.ing3nia.parentalcontrol.client.utils.ModelLogger;
import com.ing3nia.parentalcontrol.models.PCEmergencyNumber;
import com.ing3nia.parentalcontrol.models.PCFunctionality;
import com.ing3nia.parentalcontrol.models.PCModification;
import com.ing3nia.parentalcontrol.models.PCPhone;
import com.ing3nia.parentalcontrol.models.PCProperty;
import com.ing3nia.parentalcontrol.models.PCRule;
import com.ing3nia.parentalcontrol.models.PCSimpleContact;
import com.ing3nia.parentalcontrol.models.PCSmartphone;
import com.ing3nia.parentalcontrol.services.exceptions.ModificationParsingException;
import com.ing3nia.parentalcontrol.services.exceptions.SessionQueryException;
import com.ing3nia.parentalcontrol.services.models.utils.EmergencyNumberModelUtils;
import com.ing3nia.parentalcontrol.services.models.utils.PropertyModelUtils;
import com.ing3nia.parentalcontrol.services.models.utils.RuleModelUtils;
import com.ing3nia.parentalcontrol.services.models.utils.SimpleContactModelUtils;
import com.ing3nia.parentalcontrol.services.models.utils.WriteToCache;

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
	public static void ProcessParentModificationsOLD(PersistenceManager pm, PCSmartphone pcsmartphone, ModificationModel modifications) throws ModificationParsingException{		
		PCModification pcmodification = pm.getObjectById(PCModification.class, pcsmartphone.getModification());
		Logger logger = ModelLogger.logger;

		logger.info("[ParentModifications] Applying parent modifications");
		if (pcmodification == null) {
			try {
				logger.info("[ParentModifications] Create new parent modification");
				createNewParentModificationOLD(pm, pcsmartphone, modifications);
			} catch (ModificationParsingException e) {
				logger.severe("[ParentModifications] New modification could not be created ");
				throw e;
			}
		} else {
			try {
				logger.info("[ParentModifications] Updating parent modification");
				updateParentModificationOLD(pm, pcsmartphone, pcmodification, modifications);
			} catch (ModificationParsingException e) {
				logger.severe("[ParentModifications] Modification could not be updated ");
				throw e;
			}
		}
	}
	
	/**
	 * Processes the modifications made by the parent user on a child smartphone
	 * @throws ModificationParsingException 
	 * @throws SessionQueryException 
	 * @throws IllegalArgumentException 
	 */
	public static void ProcessParentModificationsNEW(PersistenceManager pm, MemcacheService syncCache, PCSmartphone pcsmartphone, ModificationModel modifications, SmartphoneCacheModel cacheSmartphone) throws ModificationParsingException, IllegalArgumentException, SessionQueryException{		
		PCModification pcmodification = pm.getObjectById(PCModification.class, pcsmartphone.getModification());
		Logger logger = ModelLogger.logger;

		logger.info("[ParentModifications - Cache Version] Applying parent modifications");
		
		if (pcmodification == null) {
			try {
				logger.info("[ParentModifications - Cache Version] Create new parent modification");
				createNewParentModificationNEW(pm, syncCache, pcsmartphone, modifications, cacheSmartphone);
			} 
			catch (ModificationParsingException e) {
				logger.severe("[ParentModifications - Cache Version] New modification could not be created ");
				throw e;
			}
		} 
		else {
			try {
				logger.info("[ParentModifications - Cache Version] Updating parent modification");
				
				String modificationCacheKey = cacheSmartphone.getKeyId() + SmartphoneCacheParams.MODIFICATION;
				IdentifiableValue cacheIdentModification = (IdentifiableValue) syncCache.getIdentifiable(modificationCacheKey);
				ModificationModel cacheModification = null;
				
				if (cacheIdentModification == null) {
					logger.info("[ParentModifications - Cache Version] ModificationModel not in cache. Saving it!");
					cacheModification = WriteToCache.writeSmartphoneModificationToCache(cacheSmartphone.getKeyId(), pcmodification);
					
					//cacheIdentModification = syncCache.getIdentifiable(modificationCacheKey);
					//cacheModification = (ModificationModel) cacheIdentModification.getValue();
				}
				else {
					logger.info("[ParentModifications - Cache Version] Obtaining ModificationModel from cache.");
					cacheModification = (ModificationModel) cacheIdentModification.getValue();
				}
				
				updateParentModificationNEW(pm, syncCache, pcsmartphone, pcmodification, modifications, cacheSmartphone, cacheModification);
				
				WriteToCache.writeSmartphoneModificationToCache(cacheSmartphone.getKeyId(), cacheModification);
				
				Gson gson = new Gson();
				System.out.println(gson.toJson(cacheModification, ModificationModel.class));
			} 
			catch (ModificationParsingException e) {
				logger.severe("[ParentModifications - Cache Version] Modification could not be updated ");
				throw e;
			}
		}
	}
	
	private static void createNewParentModificationOLD(PersistenceManager pm, PCSmartphone pcSmartphone, ModificationModel modifications) throws ModificationParsingException{
		PCModification pcmodification = new PCModification();
		updateParentModificationOLD(pm, pcSmartphone, pcmodification, modifications);
		
		pm.makePersistent(pcmodification);
		
		pcSmartphone.setModification(pcmodification.getKey());
	}
	
	private static void createNewParentModificationNEW(PersistenceManager pm, MemcacheService syncCache, PCSmartphone pcSmartphone, ModificationModel modifications, SmartphoneCacheModel cacheSmartphone) throws ModificationParsingException, IllegalArgumentException, SessionQueryException{
		PCModification pcmodification = new PCModification();
		
		String modificationCacheKey = cacheSmartphone.getKeyId() + SmartphoneCacheParams.MODIFICATION;
		IdentifiableValue cacheIdentModification = (IdentifiableValue) syncCache.getIdentifiable(modificationCacheKey);
		ModificationModel cacheModification = null;
		
		if (cacheIdentModification == null) {
			cacheModification = WriteToCache.writeSmartphoneModificationToCache(cacheSmartphone.getKeyId(), pcmodification);
		}
		else {
			cacheModification = (ModificationModel) cacheIdentModification.getValue();
		}
		
		updateParentModificationNEW(pm, syncCache, pcSmartphone, pcmodification, modifications, cacheSmartphone, cacheModification);
		
		pm.makePersistent(pcmodification);
		
		pcSmartphone.setModification(pcmodification.getKey());
		
		WriteToCache.writeSmartphoneModificationToCache(cacheSmartphone.getKeyId(), cacheModification);
		
		Gson gson = new Gson();
		System.out.println(gson.toJson(cacheModification, ModificationModel.class));
	}
	
	private static void updateParentModificationNEW(PersistenceManager pm, MemcacheService syncCache, PCSmartphone pcsmartphone, PCModification pcmodification, ModificationModel modifications, SmartphoneCacheModel cacheSmartphone, ModificationModel cacheModification) throws ModificationParsingException, IllegalArgumentException, SessionQueryException {
		
		Logger logger = ModelLogger.logger;
		
		String smartphoneKey = cacheSmartphone.getKeyId();
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
		
		ArrayList<SimpleContactModel> cacheModActiveContacts = cacheModification.getActiveContacts();
		ArrayList<SimpleContactModel> cacheModInactiveContacts = cacheModification.getInactiveContacts();
		ArrayList<EmergencyNumberModel> cacheModAddedEmergencyNumbers = cacheModification.getAddedEmergencyNumbers();
		ArrayList<EmergencyNumberModel> cacheModDeletedEmergencyNumbers = cacheModification.getDeletedEmergencyNumbers();
		ArrayList<PropertyModel> cacheModProperties = cacheModification.getProperties();
		ArrayList<RuleModel> cacheModRules = cacheModification.getRules();
		ArrayList<String> cacheModDeletedRules = cacheModification.getDeletedRules();
		
		if (smartphoneName != null) {
			pcsmartphone.setName(smartphoneName);
			cacheSmartphone.setName(smartphoneName);
		}
		
		// setting to empty lists null modification attributes
		if (activeContacts == null) {
			modifications.setActiveContacts(new ArrayList<SimpleContactModel>());
			activeContacts = modifications.getActiveContacts();
		}
		
		if (inactiveContacts == null) {
			modifications.setInactiveContacts(new ArrayList<SimpleContactModel>());
			inactiveContacts = modifications.getInactiveContacts();
		}
		
		if (addedEmergencyNumbers == null) {
			modifications.setAddedEmergencyNumbers(new ArrayList<EmergencyNumberModel>());
			addedEmergencyNumbers = modifications.getAddedEmergencyNumbers();
		}
		
		if (deletedEmergencyNumbers == null) {
			modifications.setDeletedEmergencyNumbers(new ArrayList<EmergencyNumberModel>());
			deletedEmergencyNumbers = modifications.getDeletedEmergencyNumbers();
		}
		
		if (properties == null) {
			modifications.setProperties(new ArrayList<PropertyModel>());
			properties = modifications.getProperties();
		}
		
		if (rules == null) {
			modifications.setRules(new ArrayList<RuleModel>());
			rules = modifications.getRules();
		}
		
		if (deletedRules == null) {
			modifications.setDeletedRules(new ArrayList<String>());
			deletedRules = modifications.getDeletedRules();
		}
		
		// setting to empty lists null PCModification attributes
		if (pcModActiveContacts == null) {
			pcmodification.setActiveContacts(new ArrayList<Key>());
			pcModActiveContacts = pcmodification.getActiveContacts();
		}
		
		if (cacheModActiveContacts == null) {
			cacheModification.setActiveContacts(new ArrayList<SimpleContactModel>());
			cacheModActiveContacts = cacheModification.getActiveContacts();
		}
		
		if (pcModInactiveContacts == null){
			pcmodification.setInactiveContacts(new ArrayList<Key>());
			pcModInactiveContacts = pcmodification.getInactiveContacts();
		}
		
		if (cacheModInactiveContacts == null) {
			cacheModification.setInactiveContacts(new ArrayList<SimpleContactModel>());
			cacheModInactiveContacts = cacheModification.getInactiveContacts();
		}
		
		if (pcModAddedEmergencyNumbers == null) {
			pcmodification.setAddedEmergencyNumbers(new ArrayList<Key>());
			pcModAddedEmergencyNumbers = pcmodification.getAddedEmergencyNumbers();
		}
		
		if (cacheModAddedEmergencyNumbers == null) {
			cacheModification.setAddedEmergencyNumbers(new ArrayList<EmergencyNumberModel>());
			cacheModAddedEmergencyNumbers = cacheModification.getAddedEmergencyNumbers();
		}
		
		if (pcModDeletedEmergencyNumbers == null) {
			pcmodification.setDeletedEmergencyNumbers(new ArrayList<Key>());
			pcModDeletedEmergencyNumbers = pcmodification.getDeletedEmergencyNumbers();
		}
		
		if (cacheModDeletedEmergencyNumbers == null) {
			cacheModification.setDeletedEmergencyNumbers(new ArrayList<EmergencyNumberModel>());
			cacheModDeletedEmergencyNumbers = cacheModification.getDeletedEmergencyNumbers();
		}
		
		if (pcModProperties == null) {
			pcmodification.setProperties(new ArrayList<Key>());
			pcModProperties = pcmodification.getProperties();
		}
		
		if (cacheModProperties == null) {
			cacheModification.setProperties(new ArrayList<PropertyModel>());
			cacheModProperties = cacheModification.getProperties();
		}
		
		if (pcModRules == null) {
			pcmodification.setRules(new ArrayList<Key>());
			pcModRules = pcmodification.getRules();
		}
		
		if (cacheModRules == null) {
			cacheModification.setRules(new ArrayList<RuleModel>());
			cacheModRules = cacheModification.getRules();
		}
		
		if (pcModDeletedRules == null) {
			pcmodification.setDeletedRules(new ArrayList<String>());
			pcModDeletedRules = pcmodification.getDeletedRules();
		}
		
		if (cacheModDeletedRules == null) {
			cacheModification.setDeletedRules(new ArrayList<String>());
			cacheModDeletedRules = cacheModification.getDeletedRules();
		}

		logger.info("[ParentModifications - Cache Version] Adding active contacts modifications");
		
		// if the parent enabled any contact, add it to smartphone and modifications
//		ArrayList<Key> modActiveList = pcmodification.getActiveContacts();
//		ArrayList<Key> modInactiveList = pcmodification.getInactiveContacts();
		
		String cacheSmartphoneActiveContactsKey = cacheSmartphone.getKeyId() + SmartphoneCacheParams.ACTIVE_CONTACTS;
		IdentifiableValue cacheIdentActiveContacts = syncCache.getIdentifiable(cacheSmartphoneActiveContactsKey);
		ArrayList<SimpleContactModel> cacheActiveContacts;
		
		String cacheSmartphoneInactiveContactsKey = cacheSmartphone.getKeyId() + SmartphoneCacheParams.INACTIVE_CONTACTS;
		IdentifiableValue cacheIdentInactiveContacts = syncCache.getIdentifiable(cacheSmartphoneInactiveContactsKey);
		ArrayList<SimpleContactModel> cacheInactiveContacts;
		
		//Loading from DB PCSimpleContacts from smartphone.
		ArrayList<PCSimpleContact> pcActiveSimpleContacts = new ArrayList<PCSimpleContact>();
		ArrayList<PCPhone> pcActivePhones = new ArrayList<PCPhone>();
		ArrayList<PCSimpleContact> pcInactiveSimpleContacts = new ArrayList<PCSimpleContact>();
		ArrayList<PCPhone> pcInactivePhones = new ArrayList<PCPhone>();
		
		PCSimpleContact auxSC = null;
		SimpleContactModel auxSCModel = null;
		PCPhone auxPhone;
		

		if (cacheIdentActiveContacts == null) {
			ArrayList<Key> pcSCKeys = pcsmartphone.getActiveContacts();
			
			for (Key key : pcSCKeys) {
				auxSC = pm.getObjectById(PCSimpleContact.class, key);
				pcActiveSimpleContacts.add(auxSC);
				
				auxPhone = pm.getObjectById(PCPhone.class, auxSC.getPhone());
				pcActivePhones.add(auxPhone);
			}
			
			cacheActiveContacts = WriteToCache.writeSmartphoneActiveContactsToCache(cacheSmartphone.getKeyId(), pcActiveSimpleContacts, pcActivePhones);
			
			//cacheIdentActiveContacts = syncCache.getIdentifiable(cacheSmartphoneActiveContactsKey);
			//cacheActiveContacts = (ArrayList<SimpleContactModel>) cacheIdentActiveContacts.getValue();			
		}
		else {
			cacheActiveContacts = (ArrayList<SimpleContactModel>) cacheIdentActiveContacts.getValue();
		}
		
		if (cacheIdentInactiveContacts == null) {
			ArrayList<Key> pcSCKeys = pcsmartphone.getInactiveContacts();
			
			for (Key key : pcSCKeys) {
				auxSC = pm.getObjectById(PCSimpleContact.class, key);
				pcInactiveSimpleContacts.add(auxSC);
				
				auxPhone = pm.getObjectById(PCPhone.class, auxSC.getPhone());
				pcInactivePhones.add(auxPhone);
			}
			cacheInactiveContacts = WriteToCache.writeSmartphoneInactiveContactsToCache(cacheSmartphone.getKeyId(), pcInactiveSimpleContacts, pcInactivePhones);

			//cacheIdentInactiveContacts = syncCache.getIdentifiable(cacheSmartphoneInactiveContactsKey);
			//cacheInactiveContacts = (ArrayList<SimpleContactModel>) cacheIdentInactiveContacts.getValue();			
		}
		else {
			cacheInactiveContacts = (ArrayList<SimpleContactModel>) cacheIdentInactiveContacts.getValue();
		}
		
		int pos; 
		
		
		for (SimpleContactModel contact : activeContacts) {
			// remove contact from inactive and add to active in smartphone
			Key contactKey = KeyFactory.stringToKey(contact.getKeyId());
			pos = pcsmartphone.getInactiveContacts().indexOf(contactKey);
			pcsmartphone.getInactiveContacts().remove(contactKey);
			
			if (pos != -1) {
				cacheInactiveContacts.remove(pos);
			}
			

//			SimpleContactModelUtils.removeSimpleContact(cacheInactiveContacts, contact.getKeyId());
			
			if (!pcsmartphone.getActiveContacts().contains(contactKey)){
				
				auxSC = pm.getObjectById(PCSimpleContact.class, contactKey);
				auxSCModel = SimpleContactModelUtils.convertToSimpleContactModel(auxSC);
				
				pcsmartphone.getActiveContacts().add(contactKey);
				cacheActiveContacts.add(auxSCModel);
				
				auxSC = null;
				auxSCModel = null;
				
//				pos = pcsmartphone.getActiveContacts().indexOf(contactKey);
//				
//				auxSC = pcActiveSimpleContacts.get(pos);
//				auxPhone = pcActivePhones.get(pos);
//				
//				auxSCModel = new SimpleContactModel();
//				auxSCModel.setFirstName(auxSC.getFirstName());
//				auxSCModel.setLastName(auxSC.getLastName());
//				auxSCModel.setKeyId(KeyFactory.keyToString(auxSC.getKey()));
//				auxSCModel.setPhones(new ArrayList<PhoneModel>());
//				
//				auxPhoneModel = new PhoneModel();
//				auxPhoneModel.setPhoneNumber(auxPhone.getPhoneNumber());
//				auxPhoneModel.setType(auxPhone.getType());
//				
//				auxSCModel.getPhones().add(auxPhoneModel);
//				
//				cacheActiveContacts.add(auxSCModel);

//				SimpleContactModelUtils.addSimpleContact(pm, cacheActiveContacts, contactKey);
			}

			// check if active contact existed in modification, if inactive
			// then remove
			// it and add it to active, otherwise add it to active list
			if (pcModActiveContacts.contains(contactKey)) {
				// skip
			} 
			else if (pcModInactiveContacts.contains(contactKey)) {
				pcModInactiveContacts.remove(contactKey);
				SimpleContactModel foundContact = SimpleContactModelUtils.removeSimpleContact(cacheModInactiveContacts, contact.getKeyId());
				
				if(foundContact == null){
					auxSC = pm.getObjectById(PCSimpleContact.class, contactKey);
					auxSCModel = SimpleContactModelUtils.convertToSimpleContactModel(auxSC);
					
					pcModActiveContacts.add(contactKey);
					cacheModActiveContacts.add(auxSCModel);
				}else{
					cacheModActiveContacts.add(foundContact);
				}
//				SimpleContactModelUtils.addSimpleContact(pm, cacheModActiveContacts, contactKey);
			} 
			else {
				
				if(auxSCModel == null){
					auxSC = pm.getObjectById(PCSimpleContact.class, contactKey);
					auxSCModel = SimpleContactModelUtils.convertToSimpleContactModel(auxSC);
				}
				
				pcModActiveContacts.add(contactKey);
				cacheModActiveContacts.add(auxSCModel);
//				SimpleContactModelUtils.addSimpleContact(pm, cacheModActiveContacts, contactKey);
			}
		}
		
		logger.info("[ParentModifications - Cache Version] Adding inactive contacts modifications");
		// if the parent disabled any contact, add it to smartphone and
		// modifications
//		modActiveList = pcmodification.getActiveContacts();
//		modInactiveList = pcmodification.getInactiveContacts();
		
		for (SimpleContactModel contact : inactiveContacts) {
			// remove contact from active and add to inactive in smartphone
			Key contactKey = KeyFactory.stringToKey(contact.getKeyId());
			pos = pcsmartphone.getActiveContacts().indexOf(contactKey);
			pcsmartphone.getActiveContacts().remove(contactKey);
			
			if (pos != -1) {
				cacheActiveContacts.remove(pos);
			}			
			
//			SimpleContactModelUtils.removeSimpleContact(cacheActiveContacts, contact.getKeyId());
			
			if(!pcsmartphone.getInactiveContacts().contains(contactKey)){
				
				auxSC = pm.getObjectById(PCSimpleContact.class, contactKey);
				auxSCModel = SimpleContactModelUtils.convertToSimpleContactModel(auxSC);
				
				pcsmartphone.getInactiveContacts().add(contactKey);
				cacheInactiveContacts.add(auxSCModel);
				
				auxSC = null;
				auxSCModel = null;
				
//				pos = pcsmartphone.getInactiveContacts().indexOf(contactKey);				
//				
//				auxSC = pcInactiveSimpleContacts.get(pos);
//				auxPhone = pcInactivePhones.get(pos);
//				
//				auxSCModel = new SimpleContactModel();
//				auxSCModel.setFirstName(auxSC.getFirstName());
//				auxSCModel.setLastName(auxSC.getLastName());
//				auxSCModel.setKeyId(KeyFactory.keyToString(auxSC.getKey()));
//				auxSCModel.setPhones(new ArrayList<PhoneModel>());
//				
//				auxPhoneModel = new PhoneModel();
//				auxPhoneModel.setPhoneNumber(auxPhone.getPhoneNumber());
//				auxPhoneModel.setType(auxPhone.getType());
//				
//				auxSCModel.getPhones().add(auxPhoneModel);
//				
//				cacheInactiveContacts.add(auxSCModel);
				
//				SimpleContactModelUtils.addSimpleContact(pm, cacheInactiveContacts, contactKey);
			}

			// check if inactive contact existed in modification, if active
			// then remove it and add it to inactive, otherwise add it to
			// inactive list
			if (pcModInactiveContacts.contains(contactKey)) {
				// skip
			} 
			else if (pcModActiveContacts.contains(contactKey)) {
				pcModActiveContacts.remove(contactKey);
				SimpleContactModel foundContact = SimpleContactModelUtils.removeSimpleContact(cacheModActiveContacts, contact.getKeyId());
				
				if(foundContact == null){
					pcModInactiveContacts.add(contactKey);
					cacheModInactiveContacts.add(auxSCModel);
				}else{
					cacheModInactiveContacts.add(foundContact);
				}	
				
//				SimpleContactModelUtils.addSimpleContact(pm, cacheModInactiveContacts, contactKey);
			} 
			else {
				if(auxSCModel == null){
					auxSC = pm.getObjectById(PCSimpleContact.class, contactKey);
					auxSCModel = SimpleContactModelUtils.convertToSimpleContactModel(auxSC);
				}
								
				pcModInactiveContacts.add(contactKey);
				cacheModInactiveContacts.add(auxSCModel);
//				SimpleContactModelUtils.addSimpleContact(pm, cacheModInactiveContacts, contactKey);
			}
		}
		
		logger.info("[ParentModifications - Cache Version] Adding emergency contacts modifications");
		// if the parent enabled any emergency number, add it to smartphone and
		// modifications

//		ArrayList<Key> modAddedEmergency = pcmodification.getAddedEmergencyNumbers();
//		ArrayList<Key> modDeletedEmergency = pcmodification.getDeletedEmergencyNumbers();
		
		String cacheSmartphoneAddedEmergencyNumsKey = cacheSmartphone.getKeyId() + SmartphoneCacheParams.ADD_EMERGENCY_NUMBERS;
		IdentifiableValue cacheIdentAddedEmergencyNums = syncCache.getIdentifiable(cacheSmartphoneAddedEmergencyNumsKey);
		ArrayList<EmergencyNumberModel> cacheAddedEmergencyNums;
		
		String cacheSmartphoneDeletedEmergencyNumsKey = cacheSmartphone.getKeyId() + SmartphoneCacheParams.DELETE_EMERGENCY_NUMBERS;
		IdentifiableValue cacheIdentDeletedEmergencyNums = syncCache.getIdentifiable(cacheSmartphoneDeletedEmergencyNumsKey);
		ArrayList<EmergencyNumberModel> cacheDeletedEmergencyNums;
		
		ArrayList<PCEmergencyNumber> pcAddedEmergencyNums = new ArrayList<PCEmergencyNumber>();
		ArrayList<PCEmergencyNumber> pcDeletedEmergencyNums = new ArrayList<PCEmergencyNumber>();
		
		PCEmergencyNumber auxEM = null;
		EmergencyNumberModel auxENModel = null;
		
		
		if (cacheIdentAddedEmergencyNums == null) {
			ArrayList<Key> pcENKeys = pcsmartphone.getAddedEmergencyNumbers();
			
			for (Key key : pcENKeys) {
				auxEM = pm.getObjectById(PCEmergencyNumber.class, key);
				pcAddedEmergencyNums.add(auxEM);
			}

			//cacheAddedEmergencyNums = WriteToCache.writeSmartphoneActiveContactsToCache(cacheSmartphone.getKeyId(), pcActiveSimpleContacts, pcActivePhones);
			cacheAddedEmergencyNums = WriteToCache.writeSmartphoneAddedEmergencyNumbersToCache(cacheSmartphone.getKeyId(), pcAddedEmergencyNums);
			
			//cacheIdentAddedEmergencyNums = syncCache.getIdentifiable(cacheSmartphoneActiveContactsKey);
			//cacheAddedEmergencyNums = (ArrayList<EmergencyNumberModel>) cacheIdentAddedEmergencyNums.getValue();			
		}
		else {
			cacheAddedEmergencyNums = (ArrayList<EmergencyNumberModel>) cacheIdentAddedEmergencyNums.getValue();
		}
		
		if (cacheIdentDeletedEmergencyNums == null) {
			ArrayList<Key> pcENKeys = pcsmartphone.getDeletedEmergencyNumbers();
			
			for (Key key : pcENKeys) {
				auxEM = pm.getObjectById(PCEmergencyNumber.class, key);
				pcDeletedEmergencyNums.add(auxEM);
			}

			//cacheDeletedEmergencyNums = WriteToCache.writeSmartphoneInactiveContactsToCache(cacheSmartphone.getKeyId(), pcInactiveSimpleContacts, pcInactivePhones);
			cacheDeletedEmergencyNums = WriteToCache.writeSmartphoneDeletedEmergencyNumbersToCache(cacheSmartphone.getKeyId(), pcDeletedEmergencyNums);

			//cacheIdentDeletedEmergencyNums = syncCache.getIdentifiable(cacheSmartphoneInactiveContactsKey);
			//cacheDeletedEmergencyNums = (ArrayList<EmergencyNumberModel>) cacheIdentDeletedEmergencyNums.getValue();			

		}
		else {
			cacheDeletedEmergencyNums = (ArrayList<EmergencyNumberModel>) cacheIdentDeletedEmergencyNums.getValue();
		}
		
		for (EmergencyNumberModel emergencyContact : addedEmergencyNumbers) {
			if (emergencyContact.getKeyId() == null) {
				//New emergency contact. First save. Then process.
				PCEmergencyNumber newEmergencyNumber = new PCEmergencyNumber();
				newEmergencyNumber.setCountry(emergencyContact.getCountry());
				newEmergencyNumber.setNumber(emergencyContact.getNumber());
				newEmergencyNumber.setDescription(emergencyContact.getDescription());
				
				pm.makePersistent(newEmergencyNumber);
				
				auxENModel = EmergencyNumberModelUtils.convertToEmergencyNumberModel(newEmergencyNumber);
				
				pcsmartphone.getAddedEmergencyNumbers().add(newEmergencyNumber.getKey());
				cacheAddedEmergencyNums.add(auxENModel);
//				EmergencyNumberModelUtils.addEmergencyNumber(pm, cacheAddedEmergencyNums, newEmergencyNumber.getKey());
				
				pcModAddedEmergencyNumbers.add(newEmergencyNumber.getKey());
				cacheModAddedEmergencyNumbers.add(auxENModel);
//				EmergencyNumberModelUtils.addEmergencyNumber(pm, cacheModAddedEmergencyNumbers, newEmergencyNumber.getKey());
			}
			else {
//				PCEmergencyNumber savedEmergencyNumber = pm.getObjectById(PCEmergencyNumber.class,KeyFactory.stringToKey(emergencyContact.getKeyId()));
//				savedEmergencyNumber.setCountry(emergencyContact.getCountry());
//				savedEmergencyNumber.setNumber(emergencyContact.getNumber());
//				savedEmergencyNumber.setDescription(emergencyContact.getDescription());
				
				// remove contact from deleted emergency number and add to added
				// emergency in smartphone
				Key emergencyKey = KeyFactory.stringToKey(emergencyContact.getKeyId());
				pos = pcsmartphone.getDeletedEmergencyNumbers().indexOf(emergencyKey);
				pcsmartphone.getDeletedEmergencyNumbers().remove(emergencyKey);
				
				if (pos != -1) {
					cacheDeletedEmergencyNums.remove(pos);
				}
				
//				if(pos>=0 && cacheDeletedEmergencyNums.size()>pos){
//					cacheDeletedEmergencyNums.remove(pos);
//				}
				

//				EmergencyNumberModelUtils.removeEmergencyNumber(cacheDeletedEmergencyNums, emergencyContact.getKeyId());
				
				if (!pcsmartphone.getAddedEmergencyNumbers().contains(emergencyKey)) {
					
					auxEM = pm.getObjectById(PCEmergencyNumber.class, emergencyKey);
					auxENModel = EmergencyNumberModelUtils.convertToEmergencyNumberModel(auxEM);
										
					pcsmartphone.getAddedEmergencyNumbers().add(emergencyKey);
					cacheAddedEmergencyNums.add(auxENModel);
					
					auxEM = null;
					auxENModel = null;
					
//					EmergencyNumberModelUtils.addEmergencyNumber(pm, cacheAddedEmergencyNums, emergencyKey);
				}

				// check if added emergency contact existed in modification, if
				// deleted
				// then remove it and add it to added, otherwise add it to added
				// emergency list
				if (pcModAddedEmergencyNumbers.contains(emergencyKey)) {
					// skip
				} 
				else if (pcModDeletedEmergencyNumbers.contains(emergencyKey)) {
					pcModDeletedEmergencyNumbers.remove(emergencyKey);
					EmergencyNumberModel emergencyNumber = EmergencyNumberModelUtils.removeEmergencyNumber(cacheModDeletedEmergencyNumbers, emergencyContact.getKeyId());
					
					if(emergencyNumber == null){
						pcModAddedEmergencyNumbers.add(emergencyKey);
						cacheModAddedEmergencyNumbers.add(auxENModel);
					} else{
						cacheModAddedEmergencyNumbers.add(emergencyNumber);
					}
//					EmergencyNumberModelUtils.addEmergencyNumber(pm, cacheModAddedEmergencyNumbers, emergencyKey);
				} 
				else {
					if(auxENModel==null){
						auxEM = pm.getObjectById(PCEmergencyNumber.class, emergencyKey);
						auxENModel = EmergencyNumberModelUtils.convertToEmergencyNumberModel(auxEM);
					} else{
						pcModAddedEmergencyNumbers.add(emergencyKey);
						cacheModAddedEmergencyNumbers.add(auxENModel);
					}
//					EmergencyNumberModelUtils.addEmergencyNumber(pm, cacheModAddedEmergencyNumbers, emergencyKey);
				}
			}
		}
		
		logger.info("[ParentModifications - Cache Version] Adding deleted emergency contacts modifications");
		// if the parent disabled any emergency number, add it to smartphone and
		// modifications
//		modAddedEmergency = pcmodification.getAddedEmergencyNumbers();
//		modDeletedEmergency = pcmodification.getDeletedEmergencyNumbers();
		
		logger.info("[ModificationUtils] Iterating deleted emergency numbers");
		for (EmergencyNumberModel emergencyContact : deletedEmergencyNumbers) {
			if (emergencyContact.getKeyId() == null) {
				//New emergency contact. First save. Then process.
				PCEmergencyNumber newEmergencyNumber = new PCEmergencyNumber();
				newEmergencyNumber.setCountry(emergencyContact.getCountry());
				newEmergencyNumber.setNumber(emergencyContact.getNumber());
				newEmergencyNumber.setDescription(emergencyContact.getDescription());
				
				pm.makePersistent(newEmergencyNumber);
				
				auxENModel = EmergencyNumberModelUtils.convertToEmergencyNumberModel(newEmergencyNumber);
				
				pcsmartphone.getDeletedEmergencyNumbers().add(newEmergencyNumber.getKey());
				cacheDeletedEmergencyNums.add(auxENModel);
//				EmergencyNumberModelUtils.addEmergencyNumber(pm, cacheDeletedEmergencyNums, newEmergencyNumber.getKey());
				
				pcModDeletedEmergencyNumbers.add(newEmergencyNumber.getKey());
				cacheModDeletedEmergencyNumbers.add(auxENModel);
//				EmergencyNumberModelUtils.addEmergencyNumber(pm, cacheModDeletedEmergencyNumbers, newEmergencyNumber.getKey());
			}
			else {
				logger.info("[ModificationUtils] KeyId is not null, changing from added to deleted");
//				PCEmergencyNumber savedEmergencyNumber = pm.getObjectById(PCEmergencyNumber.class,KeyFactory.stringToKey(emergencyContact.getKeyId()));
//				savedEmergencyNumber.setCountry(emergencyContact.getCountry());
//				savedEmergencyNumber.setNumber(emergencyContact.getNumber());
//				savedEmergencyNumber.setDescription(emergencyContact.getDescription());
				
				// remove contact from added emergency number and add to deleted
				// emergency in smartphone
				Key emergencyKey = KeyFactory.stringToKey(emergencyContact.getKeyId());
				pos = pcsmartphone.getAddedEmergencyNumbers().indexOf(emergencyKey);
				boolean removed = pcsmartphone.getAddedEmergencyNumbers().remove(emergencyKey);
				
				logger.info("[ModificationUtils] POS: "+pos+" Cache Added Size: "+cacheAddedEmergencyNums.size()+", removed from smartphone added: "+removed);
				if (pos != -1) {
					cacheAddedEmergencyNums.remove(pos);
				}
				
//				if(pos>=0 && cacheAddedEmergencyNums.size()>pos){
//					cacheAddedEmergencyNums.remove(pos);
//				}
				
				auxEM = pm.getObjectById(PCEmergencyNumber.class, emergencyKey);
				auxENModel = EmergencyNumberModelUtils.convertToEmergencyNumberModel(auxEM);
				
				if (!pcsmartphone.getDeletedEmergencyNumbers().contains(emergencyKey)) {
					pcsmartphone.getDeletedEmergencyNumbers().add(emergencyKey);
					cacheDeletedEmergencyNums.add(auxENModel);
//					EmergencyNumberModelUtils.addEmergencyNumber(pm, cacheDeletedEmergencyNums, emergencyKey);
				}
				
				auxEM = null;
				auxENModel = null;
	
				// check if emergency contact existed in modification, if added
				// then remove it and add it to deleted, otherwise add it to deleted
				// emergency list
	
				if (pcModDeletedEmergencyNumbers.contains(emergencyKey)) {
					// skip
				} 
				else if (pcModAddedEmergencyNumbers.contains(emergencyKey)) {
					pcModAddedEmergencyNumbers.remove(emergencyKey);
					EmergencyNumberModel foundEmergency = EmergencyNumberModelUtils.removeEmergencyNumber(cacheModAddedEmergencyNumbers, emergencyContact.getKeyId());
					
					if(foundEmergency == null){
						auxEM = pm.getObjectById(PCEmergencyNumber.class, emergencyKey);
						auxENModel = EmergencyNumberModelUtils.convertToEmergencyNumberModel(auxEM);
						
						pcModDeletedEmergencyNumbers.add(emergencyKey);
						cacheModDeletedEmergencyNumbers.add(auxENModel);
					}else{
						cacheModDeletedEmergencyNumbers.add(foundEmergency);
					}
//					EmergencyNumberModelUtils.addEmergencyNumber(pm, cacheModDeletedEmergencyNumbers, emergencyKey);
				} 
				else {
					
					if(auxENModel == null){
						auxEM = pm.getObjectById(PCEmergencyNumber.class, emergencyKey);
						auxENModel = EmergencyNumberModelUtils.convertToEmergencyNumberModel(auxEM);
					}
					pcModDeletedEmergencyNumbers.add(emergencyKey);
					cacheModDeletedEmergencyNumbers.add(auxENModel);
//					EmergencyNumberModelUtils.addEmergencyNumber(pm, cacheModDeletedEmergencyNumbers, emergencyKey);
				}
			}
		}
			
		// parsing property modifications
		logger.info("[ParentModifications - Cache Version] Adding properties modifications size: " + properties.size());
		PropertyModel auxProperty;
		
		for (PropertyModel propmodel : properties) {
			PCProperty property;
			
			try {
				property = pm.getObjectById(PCProperty.class,KeyFactory.stringToKey(propmodel.getKeyId()));
				auxProperty = PropertyModelUtils.convertToPropertyModel(property);
			} 
			catch(Exception e) {
				logger.severe("[ParentModifications] Error while obtaining property from key: "+propmodel.getKeyId());
				throw new ModificationParsingException(e.getMessage());
			}
			
			property.setValue(propmodel.getValue());
			auxProperty.setValue(propmodel.getValue());
			
			pcModProperties.add(property.getKey());
			cacheModProperties.add(auxProperty);
//			PropertyModelUtils.addProperty(pm, cacheModProperties, property.getKey());
		}
		
		// parsing rule modifications
		logger.info("[ParentModifications - Cache Version] Adding rules modifications");
		RuleModel auxRule;
		
		String cacheSmartphoneRulesKey = cacheSmartphone.getKeyId() + SmartphoneCacheParams.RULES;
		IdentifiableValue cacheIdentRules = syncCache.getIdentifiable(cacheSmartphoneRulesKey);
		ArrayList<RuleModel> cacheRules;
		
		if (cacheIdentRules == null) {
			ArrayList<PCRule> pcRules = new ArrayList<PCRule>();
			ArrayList<Key> pcRuleKeys = pcsmartphone.getRules();
			PCRule auxPCRule;
			
			for (Key key : pcRuleKeys) {
				auxPCRule = pm.getObjectById(PCRule.class, key);
				pcRules.add(auxPCRule);
			}
			
			cacheRules = WriteToCache.writeSmartphoneRulesToCache(cacheSmartphone.getKeyId(), pcRules);
			
			//cacheIdentRules = syncCache.getIdentifiable(cacheSmartphoneActiveContactsKey);
			//cacheRules = (ArrayList<RuleModel>) cacheIdentRules.getValue();			
		}
		else {
			cacheRules = (ArrayList<RuleModel>) cacheIdentRules.getValue();
		}
		
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
				logger.severe("[ParentModifications - Cache Version] Error while parsing rule creating date: " + ruleModel.getCreationDate());
				throw new ModificationParsingException("Date parsing error: " + ruleModel.getCreationDate() + " " + e.getMessage());
			}
			
			rule.setCreationDate(date);
			
			try {
				date = sdf.parse(ruleModel.getStartDate());
			} 
			catch (ParseException e) {
				logger.severe("[ParentModifications - Cache Version] Error while parsing rule start date: " + ruleModel.getStartDate());
				throw new ModificationParsingException("Date parsing error: " + ruleModel.getStartDate() + " " + e.getMessage()); 
			}
			
			rule.setStartDate(date);
			
			try {
				date = sdf.parse(ruleModel.getEndDate());
			} 
			catch (ParseException e) {
				logger.severe("[ParentModifications - Cache Version] Error while parsing rule end date: " + ruleModel.getEndDate());
				throw new ModificationParsingException("Date parsing error: " + ruleModel.getEndDate() + " " + e.getMessage());
			}
			
			rule.setEndDate(date);
			
			rule.setName(ruleModel.getName());
			
			logger.info("[ParentModifications - Cache Version] Setting new funcionalities to rules");
			
			ArrayList<Key> newDisabledFuncionalities = getNewFuncionalitiesAsKeys(pm, ruleModel);
			rule.setDisabledFunctionalities(newDisabledFuncionalities);		
			
			auxRule = RuleModelUtils.convertToRuleModel(rule);
			
			if (ruleModel.getKeyId() == null) {
				pm.makePersistent(rule);
				pcsmartphone.getRules().add(rule.getKey());
				
				auxRule.setKeyId(KeyFactory.keyToString(rule.getKey()));
				//auxRule = RuleModelUtils.convertToRuleModel(rule);				
			}
			
			int cacheRulesSize = pcModRules.size();
			boolean found = false;
			
			for (int i = 0; i < cacheRulesSize; i++) {
				if (pcModRules.get(i).equals(rule.getKey())) {
					found = true;
					break;
				}
			}
			
			if (!found) {
				pcModRules.add(rule.getKey());
				cacheModRules.add(auxRule);
			}
			
			cacheRulesSize = cacheRules.size();
			found = false;
			
			for (int i = 0; i < cacheRulesSize; i++) {
				if (cacheRules.get(i).getKeyId().equals(auxRule.getKeyId())) {
					cacheRules.set(i, auxRule);
					found = true;
				}
			}
			
			if (!found) {
				cacheRules.add(auxRule);
			}
			
//			RuleModelUtils.addRule(pm, cacheModRules, rule.getKey());
		}
		
		// adding deleted rules
		logger.info("[ParentModifications - Cache Version] Setting deleted rules");
		
		boolean found = false;
		int rulePos = 0;
		
		for (String deletedRuleId : deletedRules) {
			for (String drid : pcModDeletedRules) {
				if (deletedRuleId.equals(drid)) {
					found = true;
					break;
				}
			}
			
			if (!found) {
				pcModDeletedRules.add(deletedRuleId);
				cacheModDeletedRules.add(deletedRuleId);
			}
			
			found = false;
			
//			if (!pcModDeletedRules.contains(deletedRuleId)) {
//				pcModDeletedRules.add(deletedRuleId);
//				cacheModDeletedRules.add(deletedRuleId);
//			}
			
			for (RuleModel cacheR : cacheRules) {
				if (cacheR.getKeyId().equals(deletedRuleId)) {
					found = true;
					break;
				}
				else {
					rulePos += 1;
				}								
			}
			
			if (found) {
				cacheRules.remove(rulePos);
			}
		}
		
		//Saving all the the things used in this method.
		WriteToCache.writeSmartphoneActiveContactsToCache(smartphoneKey, cacheActiveContacts);
		WriteToCache.writeSmartphoneInactiveContactsToCache(smartphoneKey, cacheInactiveContacts);
		WriteToCache.writeSmartphoneAddedEmergencyNumbersToCache(cacheAddedEmergencyNums, smartphoneKey);
		WriteToCache.writeSmartphoneDeletedEmergencyNumbersToCache(cacheDeletedEmergencyNums, smartphoneKey);
		WriteToCache.writeSmartphoneRulesToCache(cacheRules, smartphoneKey);	
		
//		pcsmartphone.setModification(pcmodification);
		//pm.makePersistent(pcsmartphone);
		//pm.makePersistent(pcmodification);
	}
	
	private static void updateParentModificationOLD(PersistenceManager pm, PCSmartphone pcsmartphone, PCModification pcmodification, ModificationModel modifications) throws ModificationParsingException {
		
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
				newEmergencyNumber.setNumber(emergencyContact.getNumber());
				newEmergencyNumber.setDescription(emergencyContact.getDescription());
				
				pm.makePersistent(newEmergencyNumber);
				
				pcsmartphone.getAddedEmergencyNumbers().add(newEmergencyNumber.getKey());
				modAddedEmergency.add(newEmergencyNumber.getKey());
			}
			else {
				PCEmergencyNumber savedEmergencyNumber = pm.getObjectById(PCEmergencyNumber.class,KeyFactory.stringToKey(emergencyContact.getKeyId()));
				savedEmergencyNumber.setCountry(emergencyContact.getCountry());
				savedEmergencyNumber.setNumber(emergencyContact.getNumber());
				savedEmergencyNumber.setDescription(emergencyContact.getDescription());
				
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
			if (emergencyContact.getKeyId() == null) {
				//New emergency contact. First save. Then process.
				PCEmergencyNumber newEmergencyNumber = new PCEmergencyNumber();
				newEmergencyNumber.setCountry(emergencyContact.getCountry());
				newEmergencyNumber.setNumber(emergencyContact.getNumber());
				newEmergencyNumber.setDescription(emergencyContact.getDescription());
				
				pm.makePersistent(newEmergencyNumber);
				
				pcsmartphone.getDeletedEmergencyNumbers().add(newEmergencyNumber.getKey());
				modDeletedEmergency.add(newEmergencyNumber.getKey());
			}
			else {
				PCEmergencyNumber savedEmergencyNumber = pm.getObjectById(PCEmergencyNumber.class,KeyFactory.stringToKey(emergencyContact.getKeyId()));
				savedEmergencyNumber.setCountry(emergencyContact.getCountry());
				savedEmergencyNumber.setNumber(emergencyContact.getNumber());
				savedEmergencyNumber.setDescription(emergencyContact.getDescription());
				
				// remove contact from added emergency number and add to deleted
				// emergency in smartphone
				Key emergencyKey = KeyFactory.stringToKey(emergencyContact.getKeyId());
				pcsmartphone.getAddedEmergencyNumbers().remove(emergencyKey);
				if(!pcsmartphone.getDeletedEmergencyNumbers().contains(emergencyKey)){
					pcsmartphone.getDeletedEmergencyNumbers().add(emergencyKey);
				}
	
				// check if emergency contact existed in modification, if added
				// then remove it and add it to deleted, otherwise add it to deleted
				// emergency list
	
				if (modDeletedEmergency.contains(emergencyKey)) {
					// skip
				} 
				else if (modAddedEmergency.contains(emergencyKey)) {
					modAddedEmergency.remove(emergencyKey);
					modDeletedEmergency.add(emergencyKey);
				} 
				else {
					modDeletedEmergency.add(emergencyKey);
				}
			}
		}
		
		// parsing property modifications
		logger.info("[ParentModifications] Adding properties modifications");
		
		for (PropertyModel propmodel : modifications.getProperties()) {
			PCProperty property;
			
			try{
				property= pm.getObjectById(PCProperty.class,KeyFactory.stringToKey(propmodel.getKeyId()));
			} 
			catch (Exception e) {
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
				pcsmartphone.getRules().add(rule.getKey());
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
	
	private static ArrayList<RuleModel> convertRulesToCacheModel(ArrayList<PCRule> pcRules){
		ArrayList<RuleModel> rules = new ArrayList<RuleModel>();
		RuleModel ruleModel;

		for (PCRule pcRule : pcRules) {
			ruleModel = RuleModelUtils.convertToRuleModel(pcRule);
			rules.add(ruleModel);
		}
		
		return rules;
		
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


