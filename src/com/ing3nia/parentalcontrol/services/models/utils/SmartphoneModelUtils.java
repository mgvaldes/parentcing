package com.ing3nia.parentalcontrol.services.models.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.jdo.PersistenceManager;

import com.google.appengine.api.datastore.Key;
import com.ing3nia.parentalcontrol.client.models.ContactModel;
import com.ing3nia.parentalcontrol.client.models.DeviceModel;
import com.ing3nia.parentalcontrol.client.models.EmergencyNumberModel;
import com.ing3nia.parentalcontrol.client.models.PhoneModel;
import com.ing3nia.parentalcontrol.client.models.PropertyModel;
import com.ing3nia.parentalcontrol.client.models.RuleModel;
import com.ing3nia.parentalcontrol.client.models.SmartphoneModel;
import com.ing3nia.parentalcontrol.models.PCApplication;
import com.ing3nia.parentalcontrol.models.PCContact;
import com.ing3nia.parentalcontrol.models.PCEmergencyNumber;
import com.ing3nia.parentalcontrol.models.PCModification;
import com.ing3nia.parentalcontrol.models.PCPhone;
import com.ing3nia.parentalcontrol.models.PCProperty;
import com.ing3nia.parentalcontrol.models.PCRoute;
import com.ing3nia.parentalcontrol.models.PCRule;
import com.ing3nia.parentalcontrol.models.PCSimpleContact;
import com.ing3nia.parentalcontrol.models.PCSmartphone;
import com.ing3nia.parentalcontrol.services.exceptions.SessionQueryException;
import com.ing3nia.parentalcontrol.services.models.ApplicationModel;
import com.ing3nia.parentalcontrol.services.utils.ModelLogger;
import com.ing3nia.parentalcontrol.services.utils.ServiceUtils;

public class SmartphoneModelUtils {

	public static PCSmartphone convertToPCSmartphone(SmartphoneModel smartphoneModel) throws SessionQueryException {

		PCSmartphone smartphone = new PCSmartphone();
		ArrayList<Key> pcActiveContacts = new ArrayList<Key>();
		ArrayList<PCContact> pcOriginalContacts = new ArrayList<PCContact>();
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();

		for (ContactModel contact : smartphoneModel.getActiveContacts()) {
			pcOriginalContacts.add(ContactModelUtils.convertToPCContact(contact));
			pcActiveContacts.addAll(ContactModelUtils.saveAsPCSimpleContact(contact));
		}

		smartphone.setLocation(LocationModelUtils.convertToGeoPt(smartphoneModel.getLocation()));
		smartphone.setActiveContacts(pcActiveContacts);
		smartphone.setInactiveContacts(new ArrayList<Key>());
		smartphone.setAddedEmergencyNumbers(new ArrayList<Key>());
		smartphone.setDeletedEmergencyNumbers(new ArrayList<Key>());
		smartphone.setRoutes(new ArrayList<PCRoute>());
		smartphone.setProperties(new ArrayList<PCProperty>());
		smartphone.setModification(new PCModification());
		smartphone.setRules(new ArrayList<PCRule>());
		smartphone.setNotifications(new ArrayList<Key>());
		smartphone.setName(smartphoneModel.getName());
		smartphone.setDevice(DeviceModelUtils.convertToPCDevice(smartphoneModel.getDevice()));
		smartphone.setSerialNumber(smartphoneModel.getSerialNumber());
		smartphone.setApplication(ApplicationModel.findPCApplicationByAppVersion(smartphoneModel.getAppVersion(), pm));
		smartphone.setOriginalContacts(pcOriginalContacts);

		pm.close();

		return smartphone;

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
			ArrayList<PCProperty> pcProperties = savedSmartphone.getProperties(); 
			
			for (PCProperty property : pcProperties) {
				properties.add(PropertyModelUtils.convertToPropertyModel(property));
			}
			
			smartphoneModel.setProperties(properties);
			
			//------------------------------------------------------------
			// Loading rules
			//------------------------------------------------------------
			ArrayList<RuleModel> rules = new ArrayList<RuleModel>();
			
			for (PCRule rule : savedSmartphone.getRules()) {
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
	}
}
