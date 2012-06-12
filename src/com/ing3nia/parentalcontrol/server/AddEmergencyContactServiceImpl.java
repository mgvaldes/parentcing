package com.ing3nia.parentalcontrol.server;

import java.util.logging.Logger;

import javax.jdo.PersistenceManager;

import com.google.appengine.api.datastore.KeyFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.ing3nia.parentalcontrol.client.models.EmergencyNumberModel;
import com.ing3nia.parentalcontrol.client.rpc.AddEmergencyContactService;
import com.ing3nia.parentalcontrol.models.PCEmergencyNumber;
import com.ing3nia.parentalcontrol.models.PCSmartphone;
import com.ing3nia.parentalcontrol.services.utils.ServiceUtils;

public class AddEmergencyContactServiceImpl  extends RemoteServiceServlet implements AddEmergencyContactService {

	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(AddAdminUserServiceImpl.class.getName());
	public String NEW_WS = "new";
	public String OLD_WS = "old";
	public String ACTUAL = NEW_WS;
	
	
	public AddEmergencyContactServiceImpl() {
		//logger.addHandler(new ConsoleHandler());
	}


	@Override
	public String addEmergencyContact(EmergencyNumberModel emergencyNumber, String smartphoneKey) throws IllegalArgumentException {
		
		if (ACTUAL.equals(NEW_WS)) {
            return addEmergencyContactNEW(emergencyNumber, smartphoneKey);
	}

		return addEmergencyContactOLD(emergencyNumber, smartphoneKey);
	}
	
	public String addEmergencyContactNEW(EmergencyNumberModel emergencyNumber, String smartphoneKey) throws IllegalArgumentException {
		
		String contactKey = null;
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
		PCEmergencyNumber emNumber = null;
		PCSmartphone smartphone = null;
		
		logger.info("[Add Emergency Contact Service] Intiating service");
		try {
			 emNumber = new PCEmergencyNumber();
			
			logger.info("[Add Emergency Contact Service]  Getting smartphone from DB");
			smartphone = (PCSmartphone)pm.getObjectById(PCSmartphone.class, KeyFactory.stringToKey(smartphoneKey));

			emNumber.setCountry(emergencyNumber.getCountry());
			emNumber.setDescription(emergencyNumber.getDescription());
			emNumber.setNumber(emergencyNumber.getNumber());
			
			pm.makePersistent(emNumber);
			
			contactKey = KeyFactory.keyToString(emNumber.getKey());	
			//smartphone.getAddedEmergencyNumbers().add(emNumber.getKey());
			
		}
		catch (IllegalArgumentException ex) {
			logger.severe("[Add Emergency Contact Service] An error ocurred while creating Emergency number "+ex);
		}
		catch (Exception ex) {
			logger.severe("[Add Emergency Contact Service]  An error ocurred while creating Emergency number"+ex);
		}
		finally {
			pm.close();
		}
		
		return contactKey;
	}
	
	public String addEmergencyContactOLD(EmergencyNumberModel emergencyNumber, String smartphoneKey) throws IllegalArgumentException {
		
		String contactKey = null;
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
		
		logger.info("[Add Emergency Contact Service] Intiating service");
		try {
			PCEmergencyNumber emNumber = new PCEmergencyNumber();
			
			logger.info("[Add Emergency Contact Service]  Getting smartphone from DB");
			PCSmartphone smartphone = (PCSmartphone)pm.getObjectById(PCSmartphone.class, KeyFactory.stringToKey(smartphoneKey));
			
			
			emNumber.setCountry(emergencyNumber.getCountry());
			emNumber.setDescription(emergencyNumber.getDescription());
			emNumber.setNumber(emergencyNumber.getNumber());
			
			pm.makePersistent(emNumber);
			
			contactKey = KeyFactory.keyToString(emNumber.getKey());	
			//smartphone.getAddedEmergencyNumbers().add(emNumber.getKey());
			
		}
		catch (IllegalArgumentException ex) {
			logger.severe("[Add Emergency Contact Service] An error ocurred while creating Emergency number "+ex);
		}
		catch (Exception ex) {
			logger.severe("[Add Emergency Contact Service]  An error ocurred while creating Emergency number"+ex);
		}
		finally {
			pm.close();
		}
		
		return contactKey;
	}
}
