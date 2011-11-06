package com.ing3nia.parentalcontrol.services.child;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.ing3nia.parentalcontrol.models.PCContact;
import com.ing3nia.parentalcontrol.models.PCEmergencyNumber;
import com.ing3nia.parentalcontrol.models.PCModification;
import com.ing3nia.parentalcontrol.models.PCPhone;
import com.ing3nia.parentalcontrol.models.PCSimpleContact;
import com.ing3nia.parentalcontrol.models.PCSmartphone;
import com.ing3nia.parentalcontrol.services.models.AlertModel;
import com.ing3nia.parentalcontrol.services.models.ContactModel;
import com.ing3nia.parentalcontrol.services.models.EmergencyNumberModel;
import com.ing3nia.parentalcontrol.services.models.InternalModificationsModel;
import com.ing3nia.parentalcontrol.services.models.ModificationModel;
import com.ing3nia.parentalcontrol.services.models.PhoneModel;
import com.ing3nia.parentalcontrol.services.models.SimpleContactModel;
import com.ing3nia.parentalcontrol.services.models.SmartphoneModel;
import com.ing3nia.parentalcontrol.services.utils.ServiceUtils;

@Path("int-mod")
public class InternalModificationsResource {
	
	private static Logger logger = Logger.getLogger(InternalModificationsResource.class.getName());
	
	public InternalModificationsResource() {
		logger.addHandler(new ConsoleHandler());
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response doPost(String body) {
		Gson jsonParser = new Gson();
		Type bodyType = new TypeToken<InternalModificationsModel>(){}.getType();
		
		logger.info("[Internal Modifications Service] Parseando par‡metros de entrada.");
		InternalModificationsModel internalModsModel = jsonParser.fromJson(body, bodyType);
		
		saveInternalModifications(internalModsModel);

		JsonObject jsonObjectStatus = new JsonObject();
		
		jsonObjectStatus.addProperty("code", "00");
		jsonObjectStatus.addProperty("verbose", "OK");
		jsonObjectStatus.addProperty("msg", "OK");
		
		ResponseBuilder rbuilder = Response.ok(jsonObjectStatus.toString(), MediaType.APPLICATION_JSON);
		
		return rbuilder.build();
	}
	
	public void saveInternalModifications(InternalModificationsModel internalModsModel) {
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
		
		logger.info("[Internal Modifications Service] Convirtiendo id : " + internalModsModel.getId() + " de smartphone a Key.");
		Key smartphoneKey = KeyFactory.stringToKey(internalModsModel.getId());
		
		logger.info("[Internal Modifications Service] Buscando smartphone en base de datos.");
		PCSmartphone savedSmartphone = pm.getObjectById(PCSmartphone.class, smartphoneKey);
		
		savedSmartphone.setLocation(internalModsModel.getLocation().convertToGeoPt());
		
		checkAddedContacts(internalModsModel.getModification().getActiveContacts(), savedSmartphone.getActiveContacts(), savedSmartphone.getInactiveContacts(), savedSmartphone.getModification());
		checkDeletedContacts(internalModsModel.getModification().getInactiveContacts(), savedSmartphone.getActiveContacts(), savedSmartphone.getInactiveContacts(), savedSmartphone.getModification());
		checkDeletedEmergencyNumbers(internalModsModel.getModification().getDeletedEmergencyNumbers(), savedSmartphone.getAddedEmergencyNumbers(), savedSmartphone.getModification());
		
		pm.close();
	}
	
	public void checkDeletedEmergencyNumbers(ArrayList<EmergencyNumberModel> internalModDeletedEmergencyNumbers, ArrayList<Key> savedAddedEmergencyNumbers, PCModification modification) {
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();

		ArrayList<PCEmergencyNumber> addedEmergencyNumbers = (ArrayList<PCEmergencyNumber>)pm.getObjectsById(savedAddedEmergencyNumbers);
		
		for (EmergencyNumberModel em : internalModDeletedEmergencyNumbers) {
			checkInAddedEmergencyNumber(em, addedEmergencyNumbers, modification);
		}
	}
	
	public void checkInAddedEmergencyNumber(EmergencyNumberModel emergencyNumber, ArrayList<PCEmergencyNumber> addedEmergencyNumbers, PCModification modification) {			
		for (PCEmergencyNumber em : addedEmergencyNumbers) {
			if (em.getCountry().equals(emergencyNumber.getCountry()) &&
				em.getDescription().equals(emergencyNumber.getDescription()) &&
				em.getNumber().equals(emergencyNumber.getNumber())) {
				ArrayList<Key> modAddedEmergencyNumbers = modification.getAddedEmergencyNumbers();
				modAddedEmergencyNumbers.add(em.getKey());
				modification.setAddedEmergencyNumbers(modAddedEmergencyNumbers);
			}
		}
	} 
	
	public boolean isActiveContact(String firstName, String lastName, String phoneNumer, ArrayList<Key> savedActiveContacts, PCModification modification, boolean specialCase) {
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
		boolean isActive = false;
		
		ArrayList<PCSimpleContact> activeContacts = (ArrayList<PCSimpleContact>)pm.getObjectsById(savedActiveContacts);
		
		for (PCSimpleContact c : activeContacts) {
			if (c.getFirstName().equals(firstName) && c.getLastName().equals(lastName) && c.getPhone().equals(phoneNumer)) {
				
				if (specialCase) {
					ArrayList<Key> modActiveContacts = modification.getActiveContacts();
					modActiveContacts.add(c.getKey());
					
					modification.setActiveContacts(modActiveContacts);
				}
				else {
					isActive = true;
				}				
				
				break;
			}
		}
		
		return isActive;
	}
	
	public boolean isInactiveContact(String firstName, String lastName, String phoneNumber, ArrayList<Key> savedInactiveContacts, PCModification modification) {
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
		boolean isInactive = false;
		
		ArrayList<PCSimpleContact> inactiveContacts = (ArrayList<PCSimpleContact>)pm.getObjectsById(savedInactiveContacts);
		ArrayList<Key> modInactiveContacts;
		
		for (PCSimpleContact c : inactiveContacts) {
			if (c.getFirstName().equals(firstName) && c.getLastName().equals(lastName) && c.getPhone().equals(phoneNumber)) {
				isInactive = true;
				
				modInactiveContacts = modification.getInactiveContacts();
				modInactiveContacts.add(c.getKey());
				
				modification.setInactiveContacts(modInactiveContacts);
				
				break;
			}
		}
		
		return isInactive;
	}
	
	public void checkAddedContacts(ArrayList<SimpleContactModel> internalModAddedContacts, ArrayList<Key> savedActiveContacts, ArrayList<Key> savedInactiveContacts, PCModification modification) {
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
		
		logger.info("[Internal Modification Service] Searching for contacts from keys.");
		boolean isActive;
		boolean isInactive;
		PCSimpleContact newContact;
		PCPhone phone;
		ArrayList<PhoneModel> phones;
		
		for (SimpleContactModel mc : internalModAddedContacts) {
			phones = mc.getPhones();
			
			for (PhoneModel p : phones) {
				isActive = isActiveContact(mc.getFirstName(), mc.getLastName(), p.getPhoneNumber(), savedActiveContacts, modification, false);
				
				if (!isActive) {
					isInactive = isInactiveContact(mc.getFirstName(), mc.getLastName(), p.getPhoneNumber(), savedInactiveContacts, modification);
					
					if (!isInactive) {
						phone = new PCPhone();
						phone.setType(p.getType());
						phone.setPhoneNumber(p.getPhoneNumber());
						pm.makePersistent(phone);
						
						newContact = new PCSimpleContact(mc.getFirstName(), mc.getLastName(), phone.getKey());
						newContact = new PCSimpleContact(mc.getFirstName(), mc.getLastName(), null);
						
						pm.makePersistent(newContact);
						
						savedActiveContacts.add(newContact.getKey());
					}
				}
			}
		}
		
		pm.close();
	}
	
	public void checkDeletedContacts(ArrayList<SimpleContactModel> internalModDeletedContacts, ArrayList<Key> savedActiveContacts, ArrayList<Key> savedInactiveContacts, PCModification modification) {
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
		
		logger.info("[Internal Modification Service] Searching for contacts from keys.");
		ArrayList<PhoneModel> phones;
		
		for (SimpleContactModel mc : internalModDeletedContacts) {
			phones = mc.getPhones();
			
			for (PhoneModel p : phones) {
				isActiveContact(mc.getFirstName(), mc.getLastName(), p.getPhoneNumber(), savedActiveContacts, modification, true);
			}
		}
		
		pm.close();
	}
}
