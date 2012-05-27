package com.ing3nia.parentalcontrol.services.child;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.eclipse.jdt.internal.core.util.KeyToSignature;

import com.google.appengine.api.datastore.GeoPt;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.appengine.api.memcache.MemcacheService.IdentifiableValue;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.ibm.icu.text.SimpleDateFormat;
import com.ing3nia.parentalcontrol.client.models.EmergencyNumberModel;
import com.ing3nia.parentalcontrol.client.models.LocationModel;
import com.ing3nia.parentalcontrol.client.models.ModificationModel;
import com.ing3nia.parentalcontrol.client.models.PhoneModel;
import com.ing3nia.parentalcontrol.client.models.RouteModel;
import com.ing3nia.parentalcontrol.client.models.SimpleContactModel;
import com.ing3nia.parentalcontrol.client.models.cache.SmartphoneCacheModel;
import com.ing3nia.parentalcontrol.client.models.cache.SmartphoneCacheParams;
import com.ing3nia.parentalcontrol.models.PCEmergencyNumber;
import com.ing3nia.parentalcontrol.models.PCModification;
import com.ing3nia.parentalcontrol.models.PCPhone;
import com.ing3nia.parentalcontrol.models.PCRoute;
import com.ing3nia.parentalcontrol.models.PCSimpleContact;
import com.ing3nia.parentalcontrol.models.PCSmartphone;
import com.ing3nia.parentalcontrol.models.utils.WSStatus;
import com.ing3nia.parentalcontrol.services.exceptions.SessionQueryException;
import com.ing3nia.parentalcontrol.services.models.InternalModificationsModel;
import com.ing3nia.parentalcontrol.services.models.utils.LocationModelUtils;
import com.ing3nia.parentalcontrol.services.utils.ServiceUtils;
import com.ing3nia.parentalcontrol.services.utils.WebServiceUtils;

@Path("int-mod")
public class InternalModificationsResource {
	
	private static Logger logger = Logger.getLogger(InternalModificationsResource.class.getName());
	public String NEW_WS = "new";
	public String OLD_WS = "old";
	public String ACTUAL = "new";
	
	public InternalModificationsResource() {
		//logger.addHandler(new ConsoleHandler());
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
		if (ACTUAL.equals(NEW_WS)) {
			return newWS(body);
		}
		
		return oldWS(body);
	}
	
	public Response newWS(String body) {
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
			logger.severe("[Internal Modifications Service] Caugth exception: "+e.getMessage()+" "+e);
			
			rbuilder = Response.ok(WSStatus.INTERNAL_SERVICE_ERROR.getStatusAsJson().toString(), MediaType.APPLICATION_JSON);
			WebServiceUtils.setUTF8Encoding(WebServiceUtils.JSON_CONTENT_TYPE, rbuilder);
			return rbuilder.build();
		}
		
		try {
			saveInternalModificationsNEW(internalModsModel);
			
			logger.info("[Internal Modifications Service] Ok Response. Modifications handled succesfully.");
			
			JsonObject okResponse = WSStatus.OK.getStatusAsJson();
			
			rbuilder = Response.ok(okResponse.toString(), MediaType.APPLICATION_JSON);
			WebServiceUtils.setUTF8Encoding(WebServiceUtils.JSON_CONTENT_TYPE, rbuilder);
			return rbuilder.build();
		}
		catch (IllegalArgumentException ex) {
			logger.warning("[Internal Modifications Service] An error ocurred while converting a Key to String. " + ex.getMessage());
			
			rbuilder = Response.ok(WSStatus.INTERNAL_SERVICE_ERROR.getStatusAsJson().toString(), MediaType.APPLICATION_JSON);
			WebServiceUtils.setUTF8Encoding(WebServiceUtils.JSON_CONTENT_TYPE, rbuilder);
			return rbuilder.build();
		}
		catch (SessionQueryException ex) {
			logger.severe("[Internal Modifications Service] An error ocurred while finding the PCSmartphone by key or adding contact. " + ex.getMessage());
			
			rbuilder = Response.ok(WSStatus.INTERNAL_SERVICE_ERROR.getStatusAsJson().toString(), MediaType.APPLICATION_JSON);
			WebServiceUtils.setUTF8Encoding(WebServiceUtils.JSON_CONTENT_TYPE, rbuilder);
			return rbuilder.build();
		}
	}
	
	public void saveInternalModificationsNEW(InternalModificationsModel internalModsModel) throws SessionQueryException, IllegalArgumentException {
		// Using the synchronous cache
	    MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
		
		try {
			logger.info("[Internal Modifications Service - Cache Version] Converting id: " + internalModsModel.getId() + " of smartphone to Key.");
			Key smartphoneKey = KeyFactory.stringToKey(internalModsModel.getId());
			
			logger.info("[Internal Modifications Service - Cache Version] Searching for smartphone in DB.");
			PCSmartphone savedSmartphone = pm.getObjectById(PCSmartphone.class, smartphoneKey);
			
			logger.info("[Internal Modifications Service - Cache Version] Searching for smartphone in cache.");
			String smartphoneCacheKey = SmartphoneCacheParams.SMARTPHONE + internalModsModel.getId();
			IdentifiableValue cacheIdentSmartphone = (IdentifiableValue) syncCache.getIdentifiable(smartphoneCacheKey);
			SmartphoneCacheModel cacheSmartphone = null;
			
			if (cacheIdentSmartphone == null) {
				//Smartphone is not saved in cache. Save it!
			}
			else {
				cacheSmartphone = (SmartphoneCacheModel) cacheIdentSmartphone.getValue();
			}
			
			//savedSmartphone.setLocation(internalModsModel.getLocation().convertToGeoPt());
			logger.info("[Internal Modifications Service - Cache Version] Setting location "+internalModsModel.getLocation());
			
			if (internalModsModel.getLocation() != null) {
				GeoPt geopt = LocationModelUtils.convertToGeoPt(internalModsModel.getLocation());
				savedSmartphone.setLocation(geopt);
				cacheSmartphone.setLocation(internalModsModel.getLocation());
				
				logger.info("[Internal Modifications Service - Cache Version] Adding location to route list");				
				
				if (savedSmartphone.getRoutes() !=null) {
					String smartphoneRouteCacheKey = internalModsModel.getId() + SmartphoneCacheParams.ROUTES;
					IdentifiableValue cacheIdentSmartphoneRoute = (IdentifiableValue) syncCache.getIdentifiable(smartphoneRouteCacheKey);
					ArrayList<RouteModel> cacheSmartphoneRoutes = null;
					
					if (cacheIdentSmartphoneRoute == null) {
						//Smartphone Routes are not saved in cache. Save it!
					}
					else {
						cacheSmartphoneRoutes = (ArrayList<RouteModel>) cacheIdentSmartphoneRoute.getValue();
					}
					
					int savedSize = savedSmartphone.getRoutes().size();
					int cacheSize = cacheSmartphoneRoutes.size();
					
					SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/HH/yyyy hh:mm:ss aa");
					
					if (savedSize > 0 && cacheSize > 0) {
						logger.info("[Internal Modifications Service - Cache Version] Obtaining last PCRoute");
						Key routeKey = savedSmartphone.getRoutes().get(savedSize - 1);
						PCRoute route = pm.getObjectById(PCRoute.class, routeKey);
						RouteModel cacheRoute = cacheSmartphoneRoutes.get(cacheSize - 1);
						
						Date currentDate = new Date();
						Date routeDate = route.getDate();
												
						Date cacheRouteDate = dateFormatter.parse(cacheRoute.getDate());
						
						Calendar currentCalendar = Calendar.getInstance();
						currentCalendar.setTime(currentDate);
						
						Calendar routeCalendar = Calendar.getInstance();
						routeCalendar.setTime(routeDate);
						
						Calendar cacheRouteCalendar = Calendar.getInstance();
						cacheRouteCalendar.setTime(cacheRouteDate);
						
						int currentDay = currentCalendar.get(Calendar.DATE);
						int routeDay = routeCalendar.get(Calendar.DATE);
						int cacheRouteDay = cacheRouteCalendar.get(Calendar.DATE);
						
						if (currentDay == routeDay && currentDay == cacheRouteDay) {
							logger.info("[Internal Modifications Service - Cache Version] Adding geo point to current route");
							route.getRoute().add(geopt);
							cacheRoute.getPoints().add(internalModsModel.getLocation());
						}
						else {
							logger.info("[Internal Modifications Service - Cache Version] Adding geo point to new route");
							PCRoute newRoute = new PCRoute();
							newRoute.setRoute(new ArrayList<GeoPt>());
							newRoute.setDate(new Date());
							newRoute.getRoute().add(geopt);
							
							logger.info("[Internal Modifications Service - Cache Version] Persistintg route and Adding new Route key");
							pm.makePersistent(newRoute);
							savedSmartphone.getRoutes().add(newRoute.getKey());
							
							RouteModel cacheNewRoute = new RouteModel();
							cacheNewRoute.setKeyId(KeyFactory.keyToString(newRoute.getKey()));
							cacheNewRoute.setDate(dateFormatter.format(new Date()));
							cacheNewRoute.setPoints(new ArrayList<LocationModel>());
							cacheNewRoute.getPoints().add(internalModsModel.getLocation());
						}
					}
					else {
						logger.info("[Internal Modifications Service - Cache Version] No Route in route list, creating new one and adding GeoPt");
						PCRoute newRoute = new PCRoute();
						newRoute.setRoute(new ArrayList<GeoPt>());
						newRoute.getRoute().add(geopt);
						newRoute.setDate(new Date());
						
						logger.info("[Internal Modifications Service - Cache Version] Persisting new route ");
						pm.makePersistent(newRoute);
						logger.info("[Internal Modifications Service - Cache Version] Adding new Route key");
						savedSmartphone.getRoutes().add(newRoute.getKey());
						
						RouteModel cacheNewRoute = new RouteModel();
						cacheNewRoute.setKeyId(KeyFactory.keyToString(newRoute.getKey()));
						cacheNewRoute.setDate(dateFormatter.format(new Date()));
						cacheNewRoute.setPoints(new ArrayList<LocationModel>());
						cacheNewRoute.getPoints().add(internalModsModel.getLocation());
					}
				}
				else{
					logger.warning("[Internal Modifications Service - Cache Version] Smartphone routes is null");
				}
			}
			
			logger.info("[Internal Modifications Service - Cache Version] Getting modification from saved smartphone");
			Key modificationKey = savedSmartphone.getModification();
			PCModification savedModification = pm.getObjectById(PCModification.class, modificationKey);
			
			String smartphoneModificationCacheKey = internalModsModel.getId() + SmartphoneCacheParams.MODIFICATION;
			IdentifiableValue cacheIdentSmartphoneModification = (IdentifiableValue) syncCache.getIdentifiable(smartphoneModificationCacheKey);
			ModificationModel cacheModification = null;
			
			if (cacheIdentSmartphoneModification == null) {
				//Smartphone Modification is not saved in cache. Save it!
			}
			else {
				cacheModification = (ModificationModel) cacheIdentSmartphoneModification.getValue();
			}
			
			if (savedModification == null) {
				logger.info("[Internal Modifications Service - Cache Version] Smartphone modification is null. Initializing smartphone modification.");
				savedModification = new PCModification();
				pm.makePersistent(savedModification);
				savedSmartphone.setModification(savedModification.getKey());
			}
			
			if (internalModsModel.getModification() == null) {
				logger.info("[Internal Modifications Service - Cache Version] Sent Modification is null. Setting to empty");
				internalModsModel.setModification(new ModificationModel());
			}
			
			logger.info("[Internal Modifications Service - Cache Version] Checking Added and Deleted contacts ");
			
			if (internalModsModel.getModification().getActiveContacts() == null) {
				internalModsModel.getModification().setActiveContacts(new ArrayList<SimpleContactModel>());
			}
			
			if (internalModsModel.getModification().getInactiveContacts() == null) {
				internalModsModel.getModification().setInactiveContacts(new ArrayList<SimpleContactModel>());
			}
			
			if (internalModsModel.getModification().getAddedEmergencyNumbers() == null) {
				internalModsModel.getModification().setAddedEmergencyNumbers(new ArrayList<EmergencyNumberModel>());
			}
			
			if (internalModsModel.getModification().getDeletedEmergencyNumbers() == null) {
				internalModsModel.getModification().setDeletedEmergencyNumbers(new ArrayList<EmergencyNumberModel>());
			}
			
			if (internalModsModel.getModification().getActiveContacts() != null && internalModsModel.getModification().getInactiveContacts()!= null) {
				
				//TODO WARNING THIIIIIISS ISSSS TOTALLY NEWWWWW, until first IF
				ArrayList<PCPhone> savedActiveContactsObjectList = new ArrayList<PCPhone>();
				ArrayList<PCPhone> savedInactiveContactsObjectList = new ArrayList<PCPhone>();
				ArrayList<PCSimpleContact> savedSimpleActiveContactsObjectList = new ArrayList<PCSimpleContact>();
				ArrayList<PCSimpleContact> savedSimpleInactiveContacsObjectList = new ArrayList<PCSimpleContact>();
				
				ArrayList<PhoneModel> cacheActiveContactsObjectList = new ArrayList<PhoneModel>();
				ArrayList<PhoneModel> cacheInactiveContactsObjectList = new ArrayList<PhoneModel>();
				
				String smartphoneActiveCacheKey = internalModsModel.getId() + SmartphoneCacheParams.ACTIVE_CONTACTS;
				IdentifiableValue cacheIdentSmartphoneActive = (IdentifiableValue) syncCache.getIdentifiable(smartphoneActiveCacheKey);
				ArrayList<SimpleContactModel> cacheSimpleActiveContactsObjectList = null;
				
				if (cacheIdentSmartphoneActive == null) {
					//Smartphone Modification is not saved in cache. Save it!
				}
				else {
					cacheSimpleActiveContactsObjectList = (ArrayList<SimpleContactModel>) cacheIdentSmartphoneActive.getValue();
				}
				
				String smartphoneInactiveCacheKey = internalModsModel.getId() + SmartphoneCacheParams.INACTIVE_CONTACTS;
				IdentifiableValue cacheIdentSmartphoneInactive = (IdentifiableValue) syncCache.getIdentifiable(smartphoneInactiveCacheKey);
				ArrayList<SimpleContactModel> cacheSimpleInactiveContactsObjectList = null;				                              
				
				if (cacheIdentSmartphoneInactive == null) {
					//Smartphone Modification is not saved in cache. Save it!
				}
				else {
					ArrayList<SimpleContactModel> value = (ArrayList<SimpleContactModel>) cacheIdentSmartphoneInactive.getValue();
					cacheSimpleInactiveContactsObjectList = value;
				}
				
				if (internalModsModel.getModification().getActiveContacts().size() > 0 || internalModsModel.getModification().getInactiveContacts().size() > 0) {
					logger.info("OPTMIZATION: Extracting contact from Database");
					
					for (Key c : savedSmartphone.getActiveContacts()) {
						PCSimpleContact auxSimpleContact = (PCSimpleContact)pm.getObjectById(PCSimpleContact.class, c);
						PCPhone auxPhone = (PCPhone)pm.getObjectById(PCPhone.class, auxSimpleContact.getPhone());
						
						savedSimpleActiveContactsObjectList.add(auxSimpleContact);
						savedActiveContactsObjectList.add(auxPhone);	
					}
					
					for (SimpleContactModel scm : cacheSimpleActiveContactsObjectList) {
						cacheActiveContactsObjectList.addAll(scm.getPhones());
					}
					
					for (Key c : savedSmartphone.getInactiveContacts()) {
						PCSimpleContact auxSimpleContact = (PCSimpleContact)pm.getObjectById(PCSimpleContact.class, c);
						PCPhone auxPhone = (PCPhone)pm.getObjectById(PCPhone.class, auxSimpleContact.getPhone());
						
						savedSimpleInactiveContacsObjectList.add(auxSimpleContact);
						savedInactiveContactsObjectList.add(auxPhone);	
					}
					
					for (SimpleContactModel scm : cacheSimpleInactiveContactsObjectList) {
						cacheInactiveContactsObjectList.addAll(scm.getPhones());
					}
				}
				
				if (internalModsModel.getModification().getActiveContacts().size() > 0) {
					logger.info("Checking added Contacts");
					checkAddedContactsNEW(internalModsModel.getModification().getActiveContacts(), savedSmartphone.getActiveContacts(), savedSimpleActiveContactsObjectList, savedActiveContactsObjectList, savedSimpleInactiveContacsObjectList, savedInactiveContactsObjectList, cacheSimpleActiveContactsObjectList, cacheActiveContactsObjectList, cacheSimpleInactiveContactsObjectList, cacheInactiveContactsObjectList, savedModification, cacheModification);
				}
				
				if(internalModsModel.getModification().getInactiveContacts().size() > 0){
					logger.info("Checking deleted contacts");
					checkDeletedContactsNEW(internalModsModel.getModification().getInactiveContacts(), savedSimpleActiveContactsObjectList, savedActiveContactsObjectList, savedSimpleInactiveContacsObjectList, savedInactiveContactsObjectList, cacheSimpleActiveContactsObjectList, cacheActiveContactsObjectList, cacheSimpleInactiveContactsObjectList, cacheInactiveContactsObjectList, savedModification, cacheModification);
				}
			}
			else {
				logger.info("[Internal Modifications Service - Cache Version] No contact present");
			}
			
			logger.info("[Internal Modifications Service - Cache Version] Setting emergency contacts");
			
			if (internalModsModel.getModification().getAddedEmergencyNumbers() != null && internalModsModel.getModification().getDeletedEmergencyNumbers()!= null) {
				if (internalModsModel.getModification().getDeletedEmergencyNumbers().size() > 0) {
					String smartphoneAddedEmergencyCacheKey = internalModsModel.getId() + SmartphoneCacheParams.ADD_EMERGENCY_NUMBERS;
					IdentifiableValue cacheIdentSmartphoneAddedEmergency = (IdentifiableValue) syncCache.getIdentifiable(smartphoneAddedEmergencyCacheKey);
					ArrayList<EmergencyNumberModel> cacheAddedEmergencyNumberObjectList = null;
					
					if (cacheIdentSmartphoneAddedEmergency == null) {
						//Smartphone Modification is not saved in cache. Save it!
					}
					else {
						cacheAddedEmergencyNumberObjectList = (ArrayList<EmergencyNumberModel>) cacheIdentSmartphoneAddedEmergency.getValue();
					}
					
					logger.info("Checking deleted emergency Contacts");
					checkDeletedEmergencyNumbersNEW(internalModsModel.getModification().getDeletedEmergencyNumbers(), savedSmartphone.getAddedEmergencyNumbers(), cacheAddedEmergencyNumberObjectList, savedModification, cacheModification);
				}
				
			}
			else{
				logger.info("[Internal Modifications Service - Cache Version] No emergency contact present");
			}
			
			pm.close();
		}
		catch (IllegalArgumentException ex) {
	    	logger.severe("[Internal Modifications Service - Cache Version] An error ocurred while finding the PCSmartphone by key " + ex.getMessage());
			throw ex;
		}
		catch (NullPointerException ex){
	    	logger.severe("[Internal Modifications Service - Cache Version] A null pointer exception ocurred "+ internalModsModel+" "+internalModsModel.getModification()+" "+ ex.getMessage());
	    	throw ex;
		}
		catch (Exception ex) {
	    	logger.severe("[Internal Modifications Service - Cache Version] An error ocurred while finding the PCSmartphone by key " + ex.getMessage());
			throw new SessionQueryException();
	    }		
	}
	
	public void checkDeletedEmergencyNumbersNEW(ArrayList<EmergencyNumberModel> internalModDeletedEmergencyNumbers, ArrayList<Key> savedAddedEmergencyNumbers, ArrayList<EmergencyNumberModel> cacheAddedEmergencyNumberObjectList, PCModification savedModification, ModificationModel cacheModification) {
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();

		PCEmergencyNumber auxAddedEmergencyNumber;
		ArrayList<PCEmergencyNumber> addedEmergencyNumbers = new ArrayList<PCEmergencyNumber>();
		
		for (Key key : savedAddedEmergencyNumbers) {
			auxAddedEmergencyNumber = (PCEmergencyNumber)pm.getObjectById(PCEmergencyNumber.class, key);		
			addedEmergencyNumbers.add(auxAddedEmergencyNumber);
		}
		
		for (EmergencyNumberModel em : internalModDeletedEmergencyNumbers) {
			checkInAddedEmergencyNumberNEW(em, addedEmergencyNumbers, cacheAddedEmergencyNumberObjectList, savedModification, cacheModification);
		}
	}
	
	public void checkInAddedEmergencyNumberNEW(EmergencyNumberModel emergencyNumber, ArrayList<PCEmergencyNumber> addedEmergencyNumbers, ArrayList<EmergencyNumberModel> cacheAddedEmergencyNumberObjectList, PCModification savedModification, ModificationModel cacheModification) {			
		for (PCEmergencyNumber em : addedEmergencyNumbers) {
			if (em.getCountry().equals(emergencyNumber.getCountry()) &&
				em.getDescription().equals(emergencyNumber.getDescription()) &&
				em.getNumber().equals(emergencyNumber.getNumber())) {
				ArrayList<Key> modAddedEmergencyNumbers = savedModification.getAddedEmergencyNumbers();
				
				if (modAddedEmergencyNumbers == null) {
					modAddedEmergencyNumbers = new ArrayList<Key>();
				}
				
				modAddedEmergencyNumbers.add(em.getKey());
				savedModification.setAddedEmergencyNumbers(modAddedEmergencyNumbers);
				
				EmergencyNumberModel enm = new EmergencyNumberModel(KeyFactory.keyToString(em.getKey()), em.getCountry(), em.getNumber(), em.getDescription());
				cacheModification.getAddedEmergencyNumbers().add(enm);
			}
		}
	}
	
	public boolean isActiveContactNEW(String firstName, String lastName, String phoneNumber, ArrayList<PCSimpleContact> savedSimpleContacts, ArrayList<PCPhone> savedActiveContacts, ArrayList<SimpleContactModel> cacheSimpleActiveContactsObjectList, ArrayList<PhoneModel> cacheActiveContactsObjectList, ArrayList<SimpleContactModel> cacheSimpleInactiveContacsObjectList, ArrayList<PhoneModel> cacheInactiveContactsObjectList, PCModification savedModification, ModificationModel cacheModification, boolean specialCase) {
		boolean isActive = false;	
		PCSimpleContact auxSimpleContact;
		int counter = 0;
		
		for (PCPhone auxPhone : savedActiveContacts) {
			auxSimpleContact = savedSimpleContacts.get(counter);
			counter++;
			
			if (auxSimpleContact.getFirstName().equals(firstName) && auxSimpleContact.getLastName().equals(lastName) && auxPhone.getPhoneNumber().equals(phoneNumber)) {
				logger.info("[Internal Modifications Service - Cache Version] Contact is active.");
				
				if (specialCase) {
					ArrayList<Key> modActiveContacts = savedModification.getActiveContacts();
					
					if (modActiveContacts == null) {
						modActiveContacts = new ArrayList<Key>();
					}
					
					modActiveContacts.add(auxSimpleContact.getKey());
					
					savedModification.setActiveContacts(modActiveContacts);
					
					cacheModification.getActiveContacts().add(cacheSimpleActiveContactsObjectList.get(counter));
				}
				else {
					isActive = true;
				}				
				
				break;
			}
		}
		
		return isActive;
	}
	
	public boolean isInactiveContactNEW(String firstName, String lastName, String phoneNumber, ArrayList<PCSimpleContact> savedSimpleContacts, ArrayList<PCPhone> savedInactiveContacts, ArrayList<SimpleContactModel> cacheSimpleActiveContactsObjectList, ArrayList<PhoneModel> cacheActiveContactsObjectList, ArrayList<SimpleContactModel> cacheSimpleInactiveContacsObjectList, ArrayList<PhoneModel> cacheInactiveContactsObjectList, PCModification savedModification, ModificationModel cacheModification) {
		boolean isInactive = false;
		PCSimpleContact auxSimpleContact;		
		ArrayList<Key> modInactiveContacts;
		int counter = 0;
		
		for (PCPhone auxPhone : savedInactiveContacts) {
			auxSimpleContact = savedSimpleContacts.get(counter);
			counter++;
			
			if (auxSimpleContact.getFirstName().equals(firstName) && auxSimpleContact.getLastName().equals(lastName) && auxPhone.getPhoneNumber().equals(phoneNumber)) {
				logger.info("[Internal Modifications Service - Cache Version] Contact is inactive.");
				isInactive = true;
				
				modInactiveContacts = savedModification.getInactiveContacts();
				
				if (modInactiveContacts == null) {
					modInactiveContacts = new ArrayList<Key>();
				}
				
				modInactiveContacts.add(auxSimpleContact.getKey());
				
				savedModification.setInactiveContacts(modInactiveContacts);
				
				cacheModification.getInactiveContacts().add(cacheSimpleInactiveContacsObjectList.get(counter));
				
				break;
			}
		}
		
		return isInactive;
	}
	
	public void checkAddedContactsNEW(ArrayList<SimpleContactModel> internalModAddedContacts, ArrayList<Key> savedActiveContacts, ArrayList<PCSimpleContact> savedSimpleActiveContactsObjectList, ArrayList<PCPhone> savedActiveContactsObjectList, ArrayList<PCSimpleContact> savedSimpleInactiveContacsObjectList, ArrayList<PCPhone> savedInactiveContactsObjectList, ArrayList<SimpleContactModel> cacheSimpleActiveContactsObjectList, ArrayList<PhoneModel> cacheActiveContactsObjectList, ArrayList<SimpleContactModel> cacheSimpleInactiveContacsObjectList, ArrayList<PhoneModel> cacheInactiveContactsObjectList, PCModification savedModification, ModificationModel cacheModification) {		
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
		
		boolean isActive;
		boolean isInactive;
		PCSimpleContact newContact;
		PCPhone phone;
		ArrayList<PhoneModel> phones;
		
		logger.info("Iterating Modification Active Contacts");
		
		for (SimpleContactModel mc : internalModAddedContacts) {
			phones = mc.getPhones();
			
			for (PhoneModel p : phones) {
				//------------------------------------------------------------
				// Checking if contact added by child already exists in  
				// smartphone active contact list. Passing false to 
				// isActiveContact beacuse it's only necessary to check if 
				// the contact is active.
				//------------------------------------------------------------
				isActive = isActiveContactNEW(mc.getFirstName(), mc.getLastName(), p.getPhoneNumber(), savedSimpleActiveContactsObjectList, savedActiveContactsObjectList, cacheSimpleActiveContactsObjectList, cacheActiveContactsObjectList, cacheSimpleInactiveContacsObjectList, cacheInactiveContactsObjectList, savedModification, cacheModification, false);
								
				if (!isActive) {
					logger.info("[Internal Modifications Service - Cache Version] Contact is not active.");
					//------------------------------------------------------------
					// Contact is not active. Checking if contact added by child 
					// already exists in smartphone inactive contact list. If it's
					// inactive the contact is added to the inactive contact list
					// of the modifiction passed as parameter.
					//------------------------------------------------------------
					isInactive = isInactiveContactNEW(mc.getFirstName(), mc.getLastName(), p.getPhoneNumber(), savedSimpleInactiveContacsObjectList, savedInactiveContactsObjectList, cacheSimpleActiveContactsObjectList, cacheActiveContactsObjectList, cacheSimpleInactiveContacsObjectList, cacheInactiveContactsObjectList, savedModification, cacheModification);
					
					if (!isInactive) {
						logger.info("[Internal Modifications Service - Cache Version] Contact is not inactive.");
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
						
						SimpleContactModel newCacheContact = new SimpleContactModel();
						newCacheContact.setFirstName(mc.getFirstName());
						newCacheContact.setLastName(mc.getLastName());
						newCacheContact.setKeyId(KeyFactory.keyToString(newContact.getKey()));
						
						PhoneModel newPhone = new PhoneModel(p.getType(), p.getPhoneNumber());
						
						newCacheContact.setPhones(new ArrayList<PhoneModel>());
						newCacheContact.getPhones().add(newPhone);
						
						cacheSimpleActiveContactsObjectList.add(newCacheContact);
					}
				}
			}
		}
		
		pm.close();
	}
	
	public void checkDeletedContactsNEW(ArrayList<SimpleContactModel> internalModDeletedContacts, ArrayList<PCSimpleContact> savedSimpleActiveContactsObjectList, ArrayList<PCPhone> savedActiveContactsObjectList,ArrayList<PCSimpleContact> savedSimpleInactiveContacsObjectList,ArrayList<PCPhone> savedInactiveContactsObjectList, ArrayList<SimpleContactModel> cacheSimpleActiveContactsObjectList, ArrayList<PhoneModel> cacheActiveContactsObjectList, ArrayList<SimpleContactModel> cacheSimpleInactiveContacsObjectList, ArrayList<PhoneModel> cacheInactiveContactsObjectList, PCModification savedModification, ModificationModel cacheModification) {
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
		
		logger.info("[Internal Modification Service - Cache Version] Searching for contacts from keys.");
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
				isActiveContactNEW(mc.getFirstName(), mc.getLastName(), p.getPhoneNumber(), savedSimpleActiveContactsObjectList, savedActiveContactsObjectList, cacheSimpleActiveContactsObjectList, cacheActiveContactsObjectList, cacheSimpleInactiveContacsObjectList, cacheInactiveContactsObjectList, savedModification, cacheModification, true);
			}
		}
		
		pm.close();
	}
	
	public Response oldWS(String body) {
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
			logger.severe("[Internal Modifications Service] Caugth exception: "+e.getMessage()+" "+e);
			
			rbuilder = Response.ok(WSStatus.INTERNAL_SERVICE_ERROR.getStatusAsJson().toString(), MediaType.APPLICATION_JSON);
			WebServiceUtils.setUTF8Encoding(WebServiceUtils.JSON_CONTENT_TYPE, rbuilder);
			return rbuilder.build();
		}
		
		try {
			saveInternalModificationsOLD(internalModsModel);
			
			logger.info("[Internal Modifications Service] Ok Response. Modifications handled succesfully.");
			
			JsonObject okResponse = WSStatus.OK.getStatusAsJson();
			
			rbuilder = Response.ok(okResponse.toString(), MediaType.APPLICATION_JSON);
			WebServiceUtils.setUTF8Encoding(WebServiceUtils.JSON_CONTENT_TYPE, rbuilder);
			return rbuilder.build();
		}
		catch (IllegalArgumentException ex) {
			logger.warning("[Internal Modifications Service] An error ocurred while converting a Key to String. " + ex.getMessage());
			
			rbuilder = Response.ok(WSStatus.INTERNAL_SERVICE_ERROR.getStatusAsJson().toString(), MediaType.APPLICATION_JSON);
			WebServiceUtils.setUTF8Encoding(WebServiceUtils.JSON_CONTENT_TYPE, rbuilder);
			return rbuilder.build();
		}
		catch (SessionQueryException ex) {
			logger.severe("[Internal Modifications Service] An error ocurred while finding the PCSmartphone by key or adding contact. " + ex.getMessage());
			
			rbuilder = Response.ok(WSStatus.INTERNAL_SERVICE_ERROR.getStatusAsJson().toString(), MediaType.APPLICATION_JSON);
			WebServiceUtils.setUTF8Encoding(WebServiceUtils.JSON_CONTENT_TYPE, rbuilder);
			return rbuilder.build();
		}
	}
	
	public void saveInternalModificationsOLD(InternalModificationsModel internalModsModel) throws SessionQueryException, IllegalArgumentException {
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
		
		try {
			logger.info("[Internal Modifications Service] Converting id: " + internalModsModel.getId() + " of smartphone to Key.");
			Key smartphoneKey = KeyFactory.stringToKey(internalModsModel.getId());
			
			logger.info("[Internal Modifications Service] Searching for smartphone in DB.");
			PCSmartphone savedSmartphone = pm.getObjectById(PCSmartphone.class, smartphoneKey);
			
			//savedSmartphone.setLocation(internalModsModel.getLocation().convertToGeoPt());
			logger.info("[Internal Modifications Service] Setting location "+internalModsModel.getLocation());
			if(internalModsModel.getLocation() != null){
				GeoPt geopt = LocationModelUtils.convertToGeoPt(internalModsModel.getLocation());
				savedSmartphone.setLocation(geopt);
				
				logger.info("[Internal Modifications Service] Adding location to route list");
				if(savedSmartphone.getRoutes() !=null){
					int size = savedSmartphone.getRoutes().size();
					if(size > 0){					
						logger.info("[Internal Modifications Service] Obtaining last PCRoute");
						Key routeKey = savedSmartphone.getRoutes().get(size-1);
						PCRoute route = pm.getObjectById(PCRoute.class, routeKey);
						
						Date currentDate = new Date();
						Date routeDate = route.getDate();
						
						Calendar currentCalendar = Calendar.getInstance();
						currentCalendar.setTime(currentDate);
						
						Calendar routeCalendar = Calendar.getInstance();
						routeCalendar.setTime(routeDate);
						
						int currentDay = currentCalendar.get(Calendar.DATE);
						int routeDay = routeCalendar.get(Calendar.DATE);
						
						if(currentDay == routeDay){
							logger.info("[Internal Modifications Service] Adding geo point to current route");
							route.getRoute().add(geopt);
						}else{
							logger.info("[Internal Modifications Service] Adding geo point to new route");
							PCRoute newRoute = new PCRoute();
							newRoute.setRoute(new ArrayList<GeoPt>());
							newRoute.setDate(new Date());
							newRoute.getRoute().add(geopt);
							
							logger.info("[Internal Modifications Service] Persistintg route and Adding new Route key");
							pm.makePersistent(newRoute);
							savedSmartphone.getRoutes().add(newRoute.getKey());
						}
					
					}else{
						logger.info("[Internal Modifications Service] No Route in route list, creating new one and adding GeoPt");
						PCRoute newRoute = new PCRoute();
						newRoute.setRoute(new ArrayList<GeoPt>());
						newRoute.getRoute().add(geopt);
						newRoute.setDate(new Date());
						
						logger.info("[Internal Modifications Service] Persisting new route ");
						pm.makePersistent(newRoute);
						logger.info("[Internal Modifications Service] Adding new Route key");
						savedSmartphone.getRoutes().add(newRoute.getKey());
					}
				}else{
					logger.warning("[Internal Modifications Service] Smartphone routes is null");
				}
			}
			
			logger.info("[Internal Modifications Service] Getting modification from from saved smartphone");
			Key modificationKey = savedSmartphone.getModification();
			PCModification modification = pm.getObjectById(PCModification.class, modificationKey);
			
			if (modification == null) {
				logger.info("[Internal Modifications Service] Smartphone modification is null. Initializing smartphone modification.");
				modification = new PCModification();
				pm.makePersistent(modification);
				savedSmartphone.setModification(modification.getKey());
			}
			
			if(internalModsModel.getModification() == null){
				logger.info("[Internal Modifications Service] Sent Modification is null. Setting to empty");
				internalModsModel.setModification(new ModificationModel());
			}
			
			logger.info("[Internal Modifications Service] Checking Added and Deleted contacts ");
			if(internalModsModel.getModification().getActiveContacts() == null){
				internalModsModel.getModification().setActiveContacts(new ArrayList<SimpleContactModel>());
			}
			
			if(internalModsModel.getModification().getInactiveContacts() == null){
				internalModsModel.getModification().setInactiveContacts(new ArrayList<SimpleContactModel>());
			}
			
			if(internalModsModel.getModification().getAddedEmergencyNumbers() == null){
				internalModsModel.getModification().setAddedEmergencyNumbers(new ArrayList<EmergencyNumberModel>());
			}
			
			if(internalModsModel.getModification().getDeletedEmergencyNumbers() == null){
				internalModsModel.getModification().setDeletedEmergencyNumbers(new ArrayList<EmergencyNumberModel>());
			}
			
			
			if(internalModsModel.getModification().getActiveContacts() != null && internalModsModel.getModification().getInactiveContacts()!= null){
				
				//TODO WARNING THIIIIIISS ISSSS TOTALLY NEWWWWW, until first IF
				ArrayList<PCPhone> savedActiveContactsObjectList = new ArrayList<PCPhone>();
				ArrayList<PCPhone> savedInactiveContactsObjectList = new ArrayList<PCPhone>();
				ArrayList<PCSimpleContact> savedSimpleActiveContactsObjectList = new ArrayList<PCSimpleContact>();
				ArrayList<PCSimpleContact> savedSimpleInactiveContacsObjectList = new ArrayList<PCSimpleContact>();
				
				if(internalModsModel.getModification().getActiveContacts().size()>0 || internalModsModel.getModification().getInactiveContacts().size() > 0){
					logger.info("OPTMIZATION: Extracting contact from Database");

					
					for(Key c : savedSmartphone.getActiveContacts()){
						
						PCSimpleContact auxSimpleContact = (PCSimpleContact)pm.getObjectById(PCSimpleContact.class, c);
						PCPhone auxPhone = (PCPhone)pm.getObjectById(PCPhone.class, auxSimpleContact.getPhone());
						
						savedSimpleActiveContactsObjectList.add(auxSimpleContact);
						savedActiveContactsObjectList.add(auxPhone);	
					}
					
					for(Key c : savedSmartphone.getInactiveContacts()){
						
						PCSimpleContact auxSimpleContact = (PCSimpleContact)pm.getObjectById(PCSimpleContact.class, c);
						PCPhone auxPhone = (PCPhone)pm.getObjectById(PCPhone.class, auxSimpleContact.getPhone());
						
						savedSimpleInactiveContacsObjectList.add(auxSimpleContact);
						savedInactiveContactsObjectList.add(auxPhone);	
					}
				}
				
				if(internalModsModel.getModification().getActiveContacts().size() >0){
					logger.info("Checking added Contacts");
					checkAddedContactsOLD(internalModsModel.getModification().getActiveContacts(), savedSmartphone.getActiveContacts(), savedSimpleActiveContactsObjectList, savedActiveContactsObjectList, savedSimpleInactiveContacsObjectList, savedInactiveContactsObjectList, modification);
				}
				
				if(internalModsModel.getModification().getInactiveContacts().size() > 0){
					logger.info("Checking deleted contacts");
					checkDeletedContactsOLD(internalModsModel.getModification().getInactiveContacts(), savedSimpleActiveContactsObjectList, savedActiveContactsObjectList, savedSimpleInactiveContacsObjectList, savedInactiveContactsObjectList, modification);
				}
				}
			else{
				logger.info("[Internal Modifications Service] No contact present");
			}
			logger.info("[Internal Modifications Service] Setting emergency contacts");
			if(internalModsModel.getModification().getAddedEmergencyNumbers() != null && internalModsModel.getModification().getDeletedEmergencyNumbers()!= null){
				if(internalModsModel.getModification().getDeletedEmergencyNumbers().size() >0){
					logger.info("Checking deleted emergency Contacts");
					checkDeletedEmergencyNumbersOLD(internalModsModel.getModification().getDeletedEmergencyNumbers(), savedSmartphone.getAddedEmergencyNumbers(), modification);
				}
				
			}else{
				logger.info("[Internal Modifications Service] No emergency contact present");
			}
			pm.close();
		}
		catch (IllegalArgumentException ex) {
	    	logger.severe("[Internal Modifications Service] An error ocurred while finding the PCSmartphone by key " + ex.getMessage());
			throw ex;
		}
		catch (NullPointerException ex){
	    	logger.severe("[Internal Modifications Service] A null pointer exception ocurred "+ internalModsModel+" "+internalModsModel.getModification()+" "+ ex.getMessage());
	    	throw ex;
		}
		catch (Exception ex) {
	    	logger.severe("[Internal Modifications Service] An error ocurred while finding the PCSmartphone by key " + ex.getMessage());
			throw new SessionQueryException();
	    }		
	}
	
	public void checkDeletedEmergencyNumbersOLD(ArrayList<EmergencyNumberModel> internalModDeletedEmergencyNumbers, ArrayList<Key> savedAddedEmergencyNumbers, PCModification modification) {
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();

		PCEmergencyNumber auxAddedEmergencyNumber;
		ArrayList<PCEmergencyNumber> addedEmergencyNumbers = new ArrayList<PCEmergencyNumber>();
		
		for (Key key : savedAddedEmergencyNumbers) {
			auxAddedEmergencyNumber = (PCEmergencyNumber)pm.getObjectById(PCEmergencyNumber.class, key);		
			addedEmergencyNumbers.add(auxAddedEmergencyNumber);
		}
		
		for (EmergencyNumberModel em : internalModDeletedEmergencyNumbers) {
			checkInAddedEmergencyNumberOLD(em, addedEmergencyNumbers, modification);
		}
	}
	
	public void checkInAddedEmergencyNumberOLD(EmergencyNumberModel emergencyNumber, ArrayList<PCEmergencyNumber> addedEmergencyNumbers, PCModification modification) {			
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
	
	public boolean isActiveContactOLD(String firstName, String lastName, String phoneNumber, ArrayList<PCSimpleContact> savedSimpleContacts, ArrayList<PCPhone> savedActiveContacts, PCModification modification, boolean specialCase) {
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
		boolean isActive = false;	
		PCSimpleContact auxSimpleContact;
		int counter = 0;
		
		for (PCPhone auxPhone : savedActiveContacts) {
			auxSimpleContact = savedSimpleContacts.get(counter);
			counter++;
			
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
	
	public boolean isInactiveContactOLD(String firstName, String lastName, String phoneNumber, ArrayList<PCSimpleContact> savedSimpleContacts, ArrayList<PCPhone> savedInactiveContacts, PCModification modification) {
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
		boolean isInactive = false;
		PCSimpleContact auxSimpleContact;		
		ArrayList<Key> modInactiveContacts;
		int counter = 0;
		
		for (PCPhone auxPhone : savedInactiveContacts) {
			auxSimpleContact = savedSimpleContacts.get(counter);
			counter++;
			
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
	
	public void checkAddedContactsOLD(ArrayList<SimpleContactModel> internalModAddedContacts, ArrayList<Key> savedActiveContacts, ArrayList<PCSimpleContact> savedSimpleActiveContactsObjectList,ArrayList<PCPhone> savedActiveContactsObjectList,ArrayList<PCSimpleContact> savedSimpleInactiveContacsObjectList,ArrayList<PCPhone> savedInactiveContactsObjectList, PCModification modification) {		
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
		
		boolean isActive;
		boolean isInactive;
		PCSimpleContact newContact;
		PCPhone phone;
		ArrayList<PhoneModel> phones;
		

		
		logger.info("Iterating Modification Active Contacts");
		
		for (SimpleContactModel mc : internalModAddedContacts) {
			phones = mc.getPhones();
			
			for (PhoneModel p : phones) {
				//------------------------------------------------------------
				// Checking if contact added by child already exists in  
				// smartphone active contact list. Passing false to 
				// isActiveContact beacuse it's only necessary to check if 
				// the contact is active.
				//------------------------------------------------------------
				isActive = isActiveContactOLD(mc.getFirstName(), mc.getLastName(), p.getPhoneNumber(), savedSimpleActiveContactsObjectList, savedActiveContactsObjectList, modification, false);
								
				if (!isActive) {
					logger.info("[Internal Modifications Service] Contact is not active.");
					//------------------------------------------------------------
					// Contact is not active. Checking if contact added by child 
					// already exists in smartphone inactive contact list. If it's
					// inactive the contact is added to the inactive contact list
					// of the modifiction passed as parameter.
					//------------------------------------------------------------
					isInactive = isInactiveContactOLD(mc.getFirstName(), mc.getLastName(), p.getPhoneNumber(), savedSimpleInactiveContacsObjectList, savedInactiveContactsObjectList, modification);
					
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
	
	public void checkDeletedContactsOLD(ArrayList<SimpleContactModel> internalModDeletedContacts, ArrayList<PCSimpleContact> savedSimpleActiveContactsObjectList, ArrayList<PCPhone> savedActiveContactsObjectList,ArrayList<PCSimpleContact> savedSimpleInactiveContacsObjectList,ArrayList<PCPhone> savedInactiveContactsObjectList, PCModification modification) {
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
				isActiveContactOLD(mc.getFirstName(), mc.getLastName(), p.getPhoneNumber(), savedSimpleActiveContactsObjectList, savedActiveContactsObjectList, modification, true);
			}
		}
		
		pm.close();
	}
	
//	public boolean isActiveContact(String firstName, String lastName, String phoneNumber, ArrayList<Key> savedActiveContacts, PCModification modification, boolean specialCase) {
//		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
//		boolean isActive = false;	
//		PCSimpleContact auxSimpleContact;
//		PCPhone auxPhone;
//		
//		for (Key c : savedActiveContacts) {
//			auxSimpleContact = (PCSimpleContact)pm.getObjectById(PCSimpleContact.class, c);
//			auxPhone = (PCPhone)pm.getObjectById(PCPhone.class, auxSimpleContact.getPhone());
//			
//			if (auxSimpleContact.getFirstName().equals(firstName) && auxSimpleContact.getLastName().equals(lastName) && auxPhone.getPhoneNumber().equals(phoneNumber)) {
//				logger.info("[Internal Modifications Service] Contact is active.");
//				
//				if (specialCase) {
//					ArrayList<Key> modActiveContacts = modification.getActiveContacts();
//					
//					if (modActiveContacts == null) {
//						modActiveContacts = new ArrayList<Key>();
//					}
//					
//					modActiveContacts.add(auxSimpleContact.getKey());
//					
//					modification.setActiveContacts(modActiveContacts);
//				}
//				else {
//					isActive = true;
//				}				
//				
//				break;
//			}
//		}
//		
//		return isActive;
//	}
	
//	public boolean isInactiveContact(String firstName, String lastName, String phoneNumber, ArrayList<Key> savedInactiveContacts, PCModification modification) {
//		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
//		boolean isInactive = false;
//		PCSimpleContact auxSimpleContact;		
//		ArrayList<Key> modInactiveContacts;
//		PCPhone auxPhone;
//		
//		for (Key c : savedInactiveContacts) {
//			auxSimpleContact = (PCSimpleContact)pm.getObjectById(PCSimpleContact.class, c);
//			auxPhone = (PCPhone)pm.getObjectById(PCPhone.class, auxSimpleContact.getPhone());
//			
//			if (auxSimpleContact.getFirstName().equals(firstName) && auxSimpleContact.getLastName().equals(lastName) && auxPhone.getPhoneNumber().equals(phoneNumber)) {
//				logger.info("[Internal Modifications Service] Contact is inactive.");
//				isInactive = true;
//				
//				modInactiveContacts = modification.getInactiveContacts();
//				
//				if (modInactiveContacts == null) {
//					modInactiveContacts = new ArrayList<Key>();
//				}
//				
//				modInactiveContacts.add(auxSimpleContact.getKey());
//				
//				modification.setInactiveContacts(modInactiveContacts);
//				
//				break;
//			}
//		}
//		
//		return isInactive;
//	}
	
//	public void checkAddedContacts(ArrayList<SimpleContactModel> internalModAddedContacts, ArrayList<Key> savedActiveContacts, ArrayList<Key> savedInactiveContacts, PCModification modification) {		
//		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
//		
//		boolean isActive;
//		boolean isInactive;
//		PCSimpleContact newContact;
//		PCPhone phone;
//		ArrayList<PhoneModel> phones;
//		
//		logger.info("Iterating Modification Active Contacts");
//		
//		for (SimpleContactModel mc : internalModAddedContacts) {
//			phones = mc.getPhones();
//			
//			for (PhoneModel p : phones) {
//				//------------------------------------------------------------
//				// Checking if contact added by child already exists in  
//				// smartphone active contact list. Passing false to 
//				// isActiveContact beacuse it's only necessary to check if 
//				// the contact is active.
//				//------------------------------------------------------------
//				isActive = isActiveContact(mc.getFirstName(), mc.getLastName(), p.getPhoneNumber(), savedActiveContacts, modification, false);
//								
//				if (!isActive) {
//					logger.info("[Internal Modifications Service] Contact is not active.");
//					//------------------------------------------------------------
//					// Contact is not active. Checking if contact added by child 
//					// already exists in smartphone inactive contact list. If it's
//					// inactive the contact is added to the inactive contact list
//					// of the modifiction passed as parameter.
//					//------------------------------------------------------------
//					isInactive = isInactiveContact(mc.getFirstName(), mc.getLastName(), p.getPhoneNumber(), savedInactiveContacts, modification);
//					
//					if (!isInactive) {
//						logger.info("[Internal Modifications Service] Contact is not inactive.");
//						//------------------------------------------------------------
//						// Contact is not active and neither inactive. Adding new    
//						// contact to smartphone's active contact list.
//						//------------------------------------------------------------
//						phone = new PCPhone();
//						phone.setType(p.getType());
//						phone.setPhoneNumber(p.getPhoneNumber());
//						pm.makePersistent(phone);
//						
//						newContact = new PCSimpleContact(mc.getFirstName(), mc.getLastName(), phone.getKey());
//						
//						pm.makePersistent(newContact);
//						
//						savedActiveContacts.add(newContact.getKey());
//					}
//				}
//			}
//		}
//		
//		pm.close();
//	}
	
//	public void checkDeletedContacts(ArrayList<SimpleContactModel> internalModDeletedContacts, ArrayList<Key> savedActiveContacts, ArrayList<Key> savedInactiveContacts, PCModification modification) {
//		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
//		
//		logger.info("[Internal Modification Service] Searching for contacts from keys.");
//		ArrayList<PhoneModel> phones;
//		
//		for (SimpleContactModel mc : internalModDeletedContacts) {
//			phones = mc.getPhones();
//			
//			for (PhoneModel p : phones) {
//				//------------------------------------------------------------
//				// Checking if contact added by child already exists in  
//				// smartphone active contact list. Passing true to 
//				// isActiveContact beacuse if the contact is active it's 
//				// necessary to add the contact to the modification active
//				// contact list.
//				//------------------------------------------------------------
//				isActiveContact(mc.getFirstName(), mc.getLastName(), p.getPhoneNumber(), savedActiveContacts, modification, true);
//			}
//		}
//		
//		pm.close();
//	}
}
