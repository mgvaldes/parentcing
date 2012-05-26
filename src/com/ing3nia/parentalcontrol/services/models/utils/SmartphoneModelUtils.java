package com.ing3nia.parentalcontrol.services.models.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.jdo.PersistenceManager;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.ing3nia.parentalcontrol.client.models.AlertModel;
import com.ing3nia.parentalcontrol.client.models.ContactModel;
import com.ing3nia.parentalcontrol.client.models.DeviceModel;
import com.ing3nia.parentalcontrol.client.models.EmergencyNumberModel;
import com.ing3nia.parentalcontrol.client.models.NotificationModel;
import com.ing3nia.parentalcontrol.client.models.PCNotificationTypeId;
import com.ing3nia.parentalcontrol.client.models.PhoneModel;
import com.ing3nia.parentalcontrol.client.models.PropertyModel;
import com.ing3nia.parentalcontrol.client.models.RouteModel;
import com.ing3nia.parentalcontrol.client.models.RuleModel;
import com.ing3nia.parentalcontrol.client.models.SimpleContactModel;
import com.ing3nia.parentalcontrol.client.models.SmartphoneModel;
import com.ing3nia.parentalcontrol.client.models.cache.SmartphoneCacheModel;
import com.ing3nia.parentalcontrol.client.models.cache.SmartphoneCacheParams;
import com.ing3nia.parentalcontrol.client.utils.ModelLogger;
import com.ing3nia.parentalcontrol.client.utils.PCPropertyType;
import com.ing3nia.parentalcontrol.models.PCApplication;
import com.ing3nia.parentalcontrol.models.PCContact;
import com.ing3nia.parentalcontrol.models.PCEmergencyNumber;
import com.ing3nia.parentalcontrol.models.PCModification;
import com.ing3nia.parentalcontrol.models.PCNotification;
import com.ing3nia.parentalcontrol.models.PCPhone;
import com.ing3nia.parentalcontrol.models.PCProperty;
import com.ing3nia.parentalcontrol.models.PCRoute;
import com.ing3nia.parentalcontrol.models.PCRule;
import com.ing3nia.parentalcontrol.models.PCSimpleContact;
import com.ing3nia.parentalcontrol.models.PCSmartphone;
import com.ing3nia.parentalcontrol.services.exceptions.SessionQueryException;
import com.ing3nia.parentalcontrol.services.models.ApplicationModel;
import com.ing3nia.parentalcontrol.services.utils.ServiceUtils;

public class SmartphoneModelUtils {

	public static PCSmartphone convertToPCSmartphone(SmartphoneModel smartphoneModel) throws SessionQueryException {

		PCSmartphone smartphone = new PCSmartphone();
		ArrayList<Key> pcActiveContacts = new ArrayList<Key>();
		ArrayList<Key> pcOriginalContacts = new ArrayList<Key>();
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();

		for (ContactModel contact : smartphoneModel.getActiveContacts()) {
			Key pcContact;
			try{
				pcContact = ContactModelUtils.convertToPCContact(contact);
			}catch(Exception e){
				continue;
			}
			pcOriginalContacts.add(pcContact);
			pcActiveContacts.addAll(ContactModelUtils.saveAsPCSimpleContact(contact));
		}

		smartphone.setLocation(LocationModelUtils.convertToGeoPt(smartphoneModel.getLocation()));
		smartphone.setActiveContacts(pcActiveContacts);
		smartphone.setInactiveContacts(new ArrayList<Key>());
		smartphone.setAddedEmergencyNumbers(new ArrayList<Key>());
		smartphone.setDeletedEmergencyNumbers(new ArrayList<Key>());
		smartphone.setRoutes(new ArrayList<Key>());
		
		PCProperty speedLimitProperty = new PCProperty();
		speedLimitProperty.setCreationDate(new Date());
		speedLimitProperty.setDescription("SPEED_LIMIT");
		speedLimitProperty.setId(PCPropertyType.SPEED_LIMIT);
		speedLimitProperty.setValue("5");
		pm.makePersistent(speedLimitProperty);		
		ArrayList<Key> props = new ArrayList<Key>();
		props.add(speedLimitProperty.getKey());		
		smartphone.setProperties(props);
		
		PCModification mod = new PCModification();
		pm.makePersistent(mod);		
		smartphone.setModification(mod.getKey());
		
		smartphone.setRules(new ArrayList<Key>());
		smartphone.setNotifications(new ArrayList<Key>());
		smartphone.setName(smartphoneModel.getName());
		smartphone.setDevice(DeviceModelUtils.convertToPCDevice(smartphoneModel.getDevice()));
		smartphone.setSerialNumber(smartphoneModel.getSerialNumber());
		smartphone.setApplication(ApplicationModel.findPCApplicationByAppVersion(smartphoneModel.getAppVersion(), pm));
		smartphone.setOriginalContacts(pcOriginalContacts);

		pm.close();

		return smartphone;

	}
	
	// stores pcSmartphone and sets info for smartphoneCacheModel
	public static void convertToPCSmartphoneAndSetCache(PCSmartphone smartphone, SmartphoneModel smartphoneModel, String smartphoneKey, SmartphoneCacheModel smartphoneCacheModel) throws SessionQueryException {

		ArrayList<Key> pcActiveContacts = new ArrayList<Key>();
		ArrayList<Key> pcOriginalContacts = new ArrayList<Key>();
		
		ArrayList<SimpleContactModel> cacheActiveContacts = new ArrayList<SimpleContactModel>();
		ArrayList<SimpleContactModel> cacheOriginalContacts = new ArrayList<SimpleContactModel>();
		
		
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();

		for (ContactModel contact : smartphoneModel.getActiveContacts()) {
			Key pcContact;
			try{
				pcContact = ContactModelUtils.convertToPCContact(contact);
			}catch(Exception e){
				continue;
			}
			pcOriginalContacts.add(pcContact);
			pcActiveContacts.addAll(ContactModelUtils.saveAsPCSimpleContact(contact));
			
			// obtaining contact as simple contact for cache
			String contactKeyString = KeyFactory.keyToString(pcContact);
			SimpleContactModel simpleContact = ContactModelUtils.contactModelToSimpleContact(contact, contactKeyString);
			cacheActiveContacts.add(simpleContact);
			cacheOriginalContacts.add(simpleContact);
		}

		smartphone.setLocation(LocationModelUtils.convertToGeoPt(smartphoneModel.getLocation()));
		smartphone.setActiveContacts(pcActiveContacts);
		smartphone.setInactiveContacts(new ArrayList<Key>());
		smartphone.setAddedEmergencyNumbers(new ArrayList<Key>());
		smartphone.setDeletedEmergencyNumbers(new ArrayList<Key>());
		smartphone.setRoutes(new ArrayList<Key>());
		
		PCProperty speedLimitProperty = new PCProperty();
		speedLimitProperty.setCreationDate(new Date());
		speedLimitProperty.setDescription("SPEED_LIMIT");
		speedLimitProperty.setId(PCPropertyType.SPEED_LIMIT);
		speedLimitProperty.setValue("5");
		pm.makePersistent(speedLimitProperty);	

		ArrayList<Key> props = new ArrayList<Key>();
		props.add(speedLimitProperty.getKey());		
		smartphone.setProperties(props);

		// Catching properties info for cache
		ArrayList<PropertyModel> cacheProperties = new ArrayList<PropertyModel>();
		PropertyModel cacheProperty = new PropertyModel();
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
		cacheProperty.setCreationDate(formatter.format(speedLimitProperty.getCreationDate()));
		cacheProperty.setDescription(speedLimitProperty.getDescription());
		cacheProperty.setId(speedLimitProperty.getId());
		cacheProperty.setKeyId(KeyFactory.keyToString(speedLimitProperty.getKey()));
		cacheProperty.setValue(speedLimitProperty.getValue());
	
		PCModification mod = new PCModification();
		pm.makePersistent(mod);		
		smartphone.setModification(mod.getKey());
		
		smartphone.setRules(new ArrayList<Key>());
		smartphone.setNotifications(new ArrayList<Key>());
		smartphone.setName(smartphoneModel.getName());
		smartphone.setDevice(DeviceModelUtils.convertToPCDevice(smartphoneModel.getDevice()));
		smartphone.setSerialNumber(smartphoneModel.getSerialNumber());
		smartphone.setApplication(ApplicationModel.findPCApplicationByAppVersion(smartphoneModel.getAppVersion(), pm));
		smartphone.setOriginalContacts(pcOriginalContacts);

		// setting cache info
		setCacheSmartphoneInfo(smartphoneCacheModel, smartphone, smartphoneKey, cacheActiveContacts, cacheOriginalContacts, cacheProperties, null, smartphoneModel.getDevice() );
		
		pm.close();

	}

	public static void setCacheSmartphoneInfo(SmartphoneCacheModel smartphoneCacheModel, PCSmartphone pcSmartphone, String smartphoneKey, ArrayList<SimpleContactModel> activeContacts, ArrayList<SimpleContactModel> originalContacts, ArrayList<PropertyModel> properties, ApplicationModel application, DeviceModel device){
		MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
		
		syncCache.put(smartphoneKey+SmartphoneCacheParams.ACTIVE_CONTACTS, activeContacts, null);
		syncCache.put(smartphoneKey+SmartphoneCacheParams.ORIGINAL_CONTACTS, originalContacts, null);
		syncCache.put(smartphoneKey+SmartphoneCacheParams.PROPERTIES,properties, null);
		
		syncCache.put(smartphoneKey+SmartphoneCacheParams.INACTIVE_CONTACTS,new ArrayList<SimpleContactModel>(), null);
		syncCache.put(smartphoneKey+SmartphoneCacheParams.ADD_EMERGENCY_NUMBERS,new ArrayList<EmergencyNumberModel>(), null);
		syncCache.put(smartphoneKey+SmartphoneCacheParams.DELETE_EMERGENCY_NUMBERS,new ArrayList<EmergencyNumberModel>(), null);
		syncCache.put(smartphoneKey+SmartphoneCacheParams.ROUTES,new ArrayList<RouteModel>(), null);
		syncCache.put(smartphoneKey+SmartphoneCacheParams.RULES,new ArrayList<RuleModel>(), null);
		syncCache.put(smartphoneKey+SmartphoneCacheParams.ALERTS,new ArrayList<NotificationModel>(), null);
		
		smartphoneCacheModel.setKeyId(smartphoneKey);
		smartphoneCacheModel.setName(pcSmartphone.getName());
		smartphoneCacheModel.setSerialNumber(pcSmartphone.getSerialNumber());
		//smartphoneCacheModel.setApplication(application); Not for the moment
		smartphoneCacheModel.setDevice(device);
		
		System.out.println("SETTING IN CACHE "+SmartphoneCacheParams.SMARTPHONE+smartphoneKey);
		System.out.println("SETTING IN CACHE "+smartphoneKey+SmartphoneCacheParams.ACTIVE_CONTACTS);
		syncCache.put(SmartphoneCacheParams.SMARTPHONE+smartphoneKey, smartphoneCacheModel);
	}
	
	public ArrayList<String> getKeyToString(ArrayList<Key> keyList){
		ArrayList<String> stringList = new ArrayList<String>();
		for(Key key : keyList){
			stringList.add(KeyFactory.keyToString(key));
		}
		return stringList;
	}
	
	public static ArrayList<ContactModel> convertContacts(ArrayList<PCSimpleContact> pcContacts) {
		ArrayList<ContactModel> contactsList = new ArrayList<ContactModel>();
		HashMap<String, ArrayList<PhoneModel>> auxContactsHashMap = new HashMap<String, ArrayList<PhoneModel>>();
		String contactName;
		ArrayList<PhoneModel> auxPhoneList;
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();

		for (PCSimpleContact contact : pcContacts) {			
			contactName = contact.getFirstName() + "|" + contact.getLastName();

			if (auxContactsHashMap.containsKey(contactName)) {
				auxPhoneList = auxContactsHashMap.get(contactName);

				PCPhone pcphone = (PCPhone) pm.getObjectById(PCPhone.class, contact.getPhone());
				auxPhoneList.add(PhoneModelUtils.convertToPhoneModel(pcphone));
				auxContactsHashMap.put(contactName, auxPhoneList);
			} 
			else {
				auxPhoneList = new ArrayList<PhoneModel>();

				PCPhone pcphone = (PCPhone) pm.getObjectById(PCPhone.class, contact.getPhone());
				auxPhoneList.add(PhoneModelUtils.convertToPhoneModel(pcphone));
				auxContactsHashMap.put(contactName, auxPhoneList);
			}
		}
		
		Iterator<Map.Entry<String, ArrayList<PhoneModel>>> it = auxContactsHashMap.entrySet().iterator();
		ContactModel auxContact;
		Map.Entry<String, ArrayList<PhoneModel>> pair;
		String[] auxName; 
		
	    while (it.hasNext()) {
	        pair = (Map.Entry<String, ArrayList<PhoneModel>>)it.next();	        

	        auxName = ((String)pair.getKey()).split("\\|");
	        auxContact = new ContactModel(auxName[0], auxName[1], (ArrayList<PhoneModel>)pair.getValue());
	        contactsList.add(auxContact);
	    }
	
		return contactsList;
	}
	
	public static SmartphoneModel convertToSmartphoneModel(PCSmartphone savedSmartphone, PersistenceManager pm) throws SessionQueryException {
		SmartphoneModel smartphoneModel = new SmartphoneModel();
		ArrayList<Key> auxKeyList;
		PCSimpleContact auxSimpleContact;
		ArrayList<PCSimpleContact> pcContacts;
		PCEmergencyNumber auxEmergencyNumber;
		ArrayList<PCEmergencyNumber> pcEmergencyNumbers;
		
		try {
			//------------------------------------------------------------
			// Loading active contacts
			//------------------------------------------------------------
			auxKeyList = savedSmartphone.getActiveContacts();
			pcContacts = new ArrayList<PCSimpleContact>();
			
			for (Key key : auxKeyList) {
				auxSimpleContact = (PCSimpleContact)pm.getObjectById(PCSimpleContact.class, key);
				pcContacts.add(auxSimpleContact);
			}
			
			smartphoneModel.setActiveContacts(convertContacts(pcContacts));			

			//------------------------------------------------------------
			// Loading inactive contacts
			//------------------------------------------------------------
			auxKeyList = savedSmartphone.getInactiveContacts();
			pcContacts = new ArrayList<PCSimpleContact>();
			
			for (Key key : auxKeyList) {
				auxSimpleContact = (PCSimpleContact)pm.getObjectById(PCSimpleContact.class, key);
				pcContacts.add(auxSimpleContact);
			}
			
			smartphoneModel.setInactiveContacts(convertContacts(pcContacts));
			
			//------------------------------------------------------------
			// Loading added emergency numbers
			//------------------------------------------------------------
			auxKeyList = savedSmartphone.getAddedEmergencyNumbers();
			pcEmergencyNumbers = new ArrayList<PCEmergencyNumber>();
			
			for (Key key : auxKeyList) {
				auxEmergencyNumber = (PCEmergencyNumber)pm.getObjectById(PCEmergencyNumber.class, key);
				pcEmergencyNumbers.add(auxEmergencyNumber);
			}
			
			ArrayList<EmergencyNumberModel> addedEmergencyNumbers = new ArrayList<EmergencyNumberModel>();
			
			for (PCEmergencyNumber emergencyNumber : pcEmergencyNumbers) {
				addedEmergencyNumbers.add(EmergencyNumberModelUtils.convertToEmergencyNumberModel(emergencyNumber));
			}
			
			smartphoneModel.setAddedEmergencyNumbers(addedEmergencyNumbers);
			
			//------------------------------------------------------------
			// Loading properties 
			//------------------------------------------------------------
			ArrayList<PropertyModel> properties = new ArrayList<PropertyModel>();
			ArrayList<Key> pcProperties = savedSmartphone.getProperties(); 
			
			for (Key property : pcProperties) {
				properties.add(PropertyModelUtils.convertToPropertyModel(property));
			}
			
			smartphoneModel.setProperties(properties);
			
			//------------------------------------------------------------
			// Loading Alerts (Notifications)
			//------------------------------------------------------------
			ArrayList<NotificationModel> alerts = new ArrayList<NotificationModel>();
			PCNotification alert;
			NotificationModel alertModel;
			for(Key alertKey: savedSmartphone.getNotifications()){
				alert = (PCNotification)pm.getObjectById(PCNotification.class, alertKey);
				alertModel = new NotificationModel();

				alerts.add(alertModel);
			}
			
			//------------------------------------------------------------
			// Loading rules
			//------------------------------------------------------------
			ArrayList<RuleModel> rules = new ArrayList<RuleModel>();
			
			for (Key rule : savedSmartphone.getRules()) {
				rules.add(RuleModelUtils.convertToRuleModel(rule));
			}
			smartphoneModel.setRules(rules);
			
			//------------------------------------------------------------
			// Loading device
			//------------------------------------------------------------
			smartphoneModel.setDevice(DeviceModelUtils.convertToDeviceModel(savedSmartphone.getDevice()));
			
			//------------------------------------------------------------
			// Loading application version
			//------------------------------------------------------------
			PCApplication application = (PCApplication)pm.getObjectById(PCApplication.class, savedSmartphone.getApplication());			
			smartphoneModel.setAppVersion(application.getAppInfo().getAppVersion());
			
			//------------------------------------------------------------
			// Loading serial number
			//------------------------------------------------------------
			smartphoneModel.setSerialNumber(savedSmartphone.getSerialNumber());
			
			//------------------------------------------------------------
			// Loading smartphone name
			//------------------------------------------------------------
			smartphoneModel.setName(savedSmartphone.getName());
		}
		catch (Exception ex) {
	    	ModelLogger.logger.info("[Total Synchronization Service] An error ocurred while finding smartphone info en DB " + ex.getMessage());
	    	
			throw new SessionQueryException();
	    }
		
		return smartphoneModel;
	}
	
	public static void updateSmartphone(SmartphoneModel updateSmart, SmartphoneModel originalSmartphone) {
		originalSmartphone.setAddedEmergencyNumbers(updateSmart.getAddedEmergencyNumbers());
		originalSmartphone.setDeletedEmergencyNumbers(updateSmart.getDeletedEmergencyNumbers());
		originalSmartphone.setProperties(updateSmart.getProperties());
		originalSmartphone.setRules(updateSmart.getRules());
		originalSmartphone.setRoutes(updateSmart.getRoutes());
	}

}
