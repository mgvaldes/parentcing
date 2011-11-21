package com.ing3nia.parentalcontrol.services.child;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.logging.ConsoleHandler;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
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
import com.ing3nia.parentalcontrol.client.models.EmergencyNumberModel;
import com.ing3nia.parentalcontrol.client.models.PhoneModel;
import com.ing3nia.parentalcontrol.client.models.SimpleContactModel;
import com.ing3nia.parentalcontrol.models.PCEmergencyNumber;
import com.ing3nia.parentalcontrol.models.PCModification;
import com.ing3nia.parentalcontrol.models.PCPhone;
import com.ing3nia.parentalcontrol.models.PCSimpleContact;
import com.ing3nia.parentalcontrol.models.PCSmartphone;
import com.ing3nia.parentalcontrol.models.utils.WSStatus;
import com.ing3nia.parentalcontrol.services.exceptions.SessionQueryException;
import com.ing3nia.parentalcontrol.services.models.InternalModificationsModel;
import com.ing3nia.parentalcontrol.services.utils.ServiceUtils;

@Path("int-mod")
public class InternalModificationsResource {
	
	private static Logger logger = Logger.getLogger(InternalModificationsResource.class.getName());
	
	public InternalModificationsResource() {
		logger.addHandler(new ConsoleHandler());
	}
	
	/*
	  {
		'id':'aglub19hcHBfaWRyHgsSBlBDVXNlchgBDAsSDFBDU21hcnRwaG9uZRhXDA',
		'modification':{
			'inactiveContacts':[
				{
					'firstName':'Jamey',
					'lastName”:'Jhonson',
					'phones':[
						{'type':1,'phoneNumber':'555432126'},
						{'type':1,'phoneNumber':'551989213'}
					]
				}
			],
			'activeContacs':[
				{
					'firstName':'Maria',
					'lastName':'Elena',
					'phones':[
						{'type':1,'phoneNumber':'04267336220'},
						{'type':1,'phoneNumber':'623554123'}
					]
				}
			], 
			'deletedEmergencyNumbers':[
				{'country':”VEN”,'number”:'113','description':'Emergency Number'}
			]
		},
		'location':{'latitude':'70.61','longitude':'85.78'}
	}
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response doPost(String body) {
		Gson jsonParser = new Gson();
		Type bodyType = new TypeToken<InternalModificationsModel>(){}.getType();
		
		ResponseBuilder rbuilder;
		InternalModificationsModel internalModsModel;
		
		logger.info("[Internal Modifications Service] Parsing input parameters.");
		
		try {
			internalModsModel = jsonParser.fromJson(body, bodyType);
		}
		catch (Exception e) {
			logger.warning("[Internal Modifications Service] InternalModificationsModel couldn't be created from post input " + WSStatus.INTERNAL_SERVICE_ERROR.getMsg());
			
			rbuilder = Response.ok(WSStatus.INTERNAL_SERVICE_ERROR.getStatusAsJson().toString(), MediaType.APPLICATION_JSON);
			return rbuilder.build();
		}
		
		try {
			saveInternalModifications(internalModsModel);
			
			logger.info("[Internal Modifications Service] Ok Response. Modifications handled succesfully.");
			
			JsonObject okResponse = WSStatus.OK.getStatusAsJson();
			
			rbuilder = Response.ok(okResponse.toString(), MediaType.APPLICATION_JSON);
			return rbuilder.build();
		}
		catch (IllegalArgumentException ex) {
			logger.warning("[Internal Modifications Service] An error ocurred while converting a Key to String. " + ex.getMessage());
			
			rbuilder = Response.ok(WSStatus.INTERNAL_SERVICE_ERROR.getStatusAsJson().toString(), MediaType.APPLICATION_JSON);
			return rbuilder.build();
		}
		catch (SessionQueryException ex) {
			logger.warning("[Internal Modifications Service] An error ocurred while finding the PCSmartphone by key or adding contact. " + ex.getMessage());
			
			rbuilder = Response.ok(WSStatus.INTERNAL_SERVICE_ERROR.getStatusAsJson().toString(), MediaType.APPLICATION_JSON);
			return rbuilder.build();
		}
	}
	
	public void saveInternalModifications(InternalModificationsModel internalModsModel) throws SessionQueryException, IllegalArgumentException {
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
		
		try {
			logger.info("[Internal Modifications Service] Converting id: " + internalModsModel.getId() + " of smartphone to Key.");
			Key smartphoneKey = KeyFactory.stringToKey(internalModsModel.getId());
			
			logger.info("[Internal Modifications Service] Searching for smartphone in DB.");
			PCSmartphone savedSmartphone = pm.getObjectById(PCSmartphone.class, smartphoneKey);
			
			savedSmartphone.setLocation(internalModsModel.getLocation().convertToGeoPt());
			
			PCModification modification = savedSmartphone.getModification();
			
			if (modification == null) {
				logger.info("[Internal Modifications Service] Smartphone modification is null. Initializing smartphone modification.");
				modification = new PCModification();
				savedSmartphone.setModification(modification);
			}
			
			checkAddedContacts(internalModsModel.getModification().getActiveContacts(), savedSmartphone.getActiveContacts(), savedSmartphone.getInactiveContacts(), modification);
			checkDeletedContacts(internalModsModel.getModification().getInactiveContacts(), savedSmartphone.getActiveContacts(), savedSmartphone.getInactiveContacts(), modification);
			checkDeletedEmergencyNumbers(internalModsModel.getModification().getDeletedEmergencyNumbers(), savedSmartphone.getAddedEmergencyNumbers(), modification);
			
			pm.close();
		}
		catch (IllegalArgumentException ex) {
			throw ex;
		}
		catch (Exception ex) {
	    	logger.info("[Internal Modifications Service] An error ocurred while finding the PCSmartphone by key " + ex.getMessage());
	    	
			throw new SessionQueryException();
	    }		
	}
	
	public void checkDeletedEmergencyNumbers(ArrayList<EmergencyNumberModel> internalModDeletedEmergencyNumbers, ArrayList<Key> savedAddedEmergencyNumbers, PCModification modification) {
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();

		PCEmergencyNumber auxAddedEmergencyNumber;
		ArrayList<PCEmergencyNumber> addedEmergencyNumbers = new ArrayList<PCEmergencyNumber>();
		
		for (Key key : savedAddedEmergencyNumbers) {
			auxAddedEmergencyNumber = (PCEmergencyNumber)pm.getObjectById(PCEmergencyNumber.class, key);		
			addedEmergencyNumbers.add(auxAddedEmergencyNumber);
		}
		
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
				
				if (modAddedEmergencyNumbers == null) {
					modAddedEmergencyNumbers = new ArrayList<Key>();
				}
				
				modAddedEmergencyNumbers.add(em.getKey());
				modification.setAddedEmergencyNumbers(modAddedEmergencyNumbers);
			}
		}
	} 
	
	public boolean isActiveContact(String firstName, String lastName, String phoneNumber, ArrayList<Key> savedActiveContacts, PCModification modification, boolean specialCase) {
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
		boolean isActive = false;	
		PCSimpleContact auxSimpleContact;
		PCPhone auxPhone;
		
		for (Key c : savedActiveContacts) {
			auxSimpleContact = (PCSimpleContact)pm.getObjectById(PCSimpleContact.class, c);
			auxPhone = (PCPhone)pm.getObjectById(PCPhone.class, auxSimpleContact.getPhone());
			
			if (auxSimpleContact.getFirstName().equals(firstName) && auxSimpleContact.getLastName().equals(lastName) && auxPhone.getPhoneNumber().equals(phoneNumber)) {
				logger.info("[Internal Modifications Service] Contact is active.");
				
				if (specialCase) {
					ArrayList<Key> modActiveContacts = modification.getActiveContacts();
					
					if (modActiveContacts == null) {
						modActiveContacts = new ArrayList<Key>();
					}
					
					modActiveContacts.add(auxSimpleContact.getKey());
					
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
		PCSimpleContact auxSimpleContact;		
		ArrayList<Key> modInactiveContacts;
		PCPhone auxPhone;
		
		for (Key c : savedInactiveContacts) {
			auxSimpleContact = (PCSimpleContact)pm.getObjectById(PCSimpleContact.class, c);
			auxPhone = (PCPhone)pm.getObjectById(PCPhone.class, auxSimpleContact.getPhone());
			
			if (auxSimpleContact.getFirstName().equals(firstName) && auxSimpleContact.getLastName().equals(lastName) && auxPhone.getPhoneNumber().equals(phoneNumber)) {
				logger.info("[Internal Modifications Service] Contact is inactive.");
				isInactive = true;
				
				modInactiveContacts = modification.getInactiveContacts();
				
				if (modInactiveContacts == null) {
					modInactiveContacts = new ArrayList<Key>();
				}
				
				modInactiveContacts.add(auxSimpleContact.getKey());
				
				modification.setInactiveContacts(modInactiveContacts);
				
				break;
			}
		}
		
		return isInactive;
	}
	
	public void checkAddedContacts(ArrayList<SimpleContactModel> internalModAddedContacts, ArrayList<Key> savedActiveContacts, ArrayList<Key> savedInactiveContacts, PCModification modification) {		
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
		
		boolean isActive;
		boolean isInactive;
		PCSimpleContact newContact;
		PCPhone phone;
		ArrayList<PhoneModel> phones;
		
		for (SimpleContactModel mc : internalModAddedContacts) {
			phones = mc.getPhones();
			
			for (PhoneModel p : phones) {
				//------------------------------------------------------------
				// Checking if contact added by child already exists in  
				// smartphone active contact list. Passing false to 
				// isActiveContact beacuse it's only necessary to check if 
				// the contact is active.
				//------------------------------------------------------------
				isActive = isActiveContact(mc.getFirstName(), mc.getLastName(), p.getPhoneNumber(), savedActiveContacts, modification, false);
								
				if (!isActive) {
					logger.info("[Internal Modifications Service] Contact is not active.");
					//------------------------------------------------------------
					// Contact is not active. Checking if contact added by child 
					// already exists in smartphone inactive contact list. If it's
					// inactive the contact is added to the inactive contact list
					// of the modifiction passed as parameter.
					//------------------------------------------------------------
					isInactive = isInactiveContact(mc.getFirstName(), mc.getLastName(), p.getPhoneNumber(), savedInactiveContacts, modification);
					
					if (!isInactive) {
						logger.info("[Internal Modifications Service] Contact is not inactive.");
						//------------------------------------------------------------
						// Contact is not active and neither inactive. Adding new    
						// contact to smartphone's active contact list.
						//------------------------------------------------------------
						phone = new PCPhone();
						phone.setType(p.getType());
						phone.setPhoneNumber(p.getPhoneNumber());
						pm.makePersistent(phone);
						
						newContact = new PCSimpleContact(mc.getFirstName(), mc.getLastName(), phone.getKey());
						
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
				//------------------------------------------------------------
				// Checking if contact added by child already exists in  
				// smartphone active contact list. Passing true to 
				// isActiveContact beacuse if the contact is active it's 
				// necessary to add the contact to the modification active
				// contact list.
				//------------------------------------------------------------
				isActiveContact(mc.getFirstName(), mc.getLastName(), p.getPhoneNumber(), savedActiveContacts, modification, true);
			}
		}
		
		pm.close();
	}
}
