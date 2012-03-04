package com.ing3nia.parentalcontrol.server;

import java.util.logging.Logger;

import javax.jdo.PersistenceManager;

import com.google.appengine.api.datastore.KeyFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.ing3nia.parentalcontrol.client.models.EmergencyNumberModel;
import com.ing3nia.parentalcontrol.client.rpc.AddEmergencyContactService;
import com.ing3nia.parentalcontrol.client.rpc.EditEmergencyContactService;
import com.ing3nia.parentalcontrol.models.PCEmergencyNumber;
import com.ing3nia.parentalcontrol.models.PCSmartphone;
import com.ing3nia.parentalcontrol.services.utils.ServiceUtils;

public class EditEmergencyContactServiceImpl  extends RemoteServiceServlet implements EditEmergencyContactService {

	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(AddAdminUserServiceImpl.class.getName());
	
	public EditEmergencyContactServiceImpl() {
		//logger.addHandler(new ConsoleHandler());
	}

	@Override
	public boolean editEmergencyContact(EmergencyNumberModel emergencyNumber,
			String smartphoneKey) throws IllegalArgumentException {
		
		String contactKeyString = emergencyNumber.getKeyId();
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
		
		logger.info("[Edit Emergency Contact Service] Intiating service");
		try {
	
			logger.info("[Edit Emergency Contact Service]  Getting emergency contact from DB");
			PCEmergencyNumber dbEmergencyContact = (PCEmergencyNumber)pm.getObjectById(PCEmergencyNumber.class, KeyFactory.stringToKey(contactKeyString));
			
			dbEmergencyContact.setCountry(emergencyNumber.getCountry());
			dbEmergencyContact.setDescription(emergencyNumber.getDescription());
			dbEmergencyContact.setNumber(emergencyNumber.getNumber());

		}
		catch (IllegalArgumentException ex) {
			logger.severe("[Edit Emergency Contact Service] An error ocurred while editing Emergency number "+ex);
			return false;
		}
		catch (Exception ex) {
			logger.severe("[Edit Emergency Contact Service]  An error ocurred while editing Emergency number"+ex);
			return false;
		}
		finally {
			pm.close();
		}

		return true;
		
	}

}
