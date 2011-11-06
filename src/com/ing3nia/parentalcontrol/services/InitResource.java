package com.ing3nia.parentalcontrol.services;

import java.util.ArrayList;
import java.util.Calendar;

import javax.jdo.PersistenceManager;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import com.google.appengine.api.datastore.Key;
import com.ing3nia.parentalcontrol.models.PCActivityStatistics;
import com.ing3nia.parentalcontrol.models.PCApplication;
import com.ing3nia.parentalcontrol.models.PCContact;
import com.ing3nia.parentalcontrol.models.PCPhone;
import com.ing3nia.parentalcontrol.models.PCSimpleContact;
import com.ing3nia.parentalcontrol.models.PCSmartphone;
import com.ing3nia.parentalcontrol.models.PCApplication.PCAppInfo;
import com.ing3nia.parentalcontrol.models.PCDevice;
import com.ing3nia.parentalcontrol.models.PCDevice.PCOs;
import com.ing3nia.parentalcontrol.models.PCFunctionality;
import com.ing3nia.parentalcontrol.models.utils.PCOsTypeId;
import com.ing3nia.parentalcontrol.services.utils.ServiceUtils;

@Path("init")
public class InitResource {
	private PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
	
	public InitResource() {
		
	}
	
	@GET
	public Response doGet() {

		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
		PCSmartphone smart = new PCSmartphone();
		ArrayList<Key> active = new ArrayList<Key>();
		PCSimpleContact pcsimple = new PCSimpleContact();
		
		PCPhone phone = new PCPhone();
		phone.setPhoneNumber("5552");
		phone.setType(1);
		pm.makePersistent(phone);
		
		pcsimple.setFirstName("Juan2");
		pcsimple.setLastName("Final2");
		pcsimple.setPhone(phone.getKey());
		

		pm.makePersistent(pcsimple);
		
		active.add(pcsimple.getKey());
		smart.setActiveContacts(active);
		smart.setName("AVERSMART");
		pm.makePersistent(smart);

		pm.close();
		
		//createDummyApplication();
		
//		ArrayList<PCPhone> phones = new ArrayList<PCPhone>();
//		
//		PCPhone phone = new PCPhone();
//		phone.setPhoneNumber(new PhoneNumber("04142634181"));
//		phone.setType(PhoneType.MOBILE.getType());
//		
//		phones.add(phone);
//		
//		phone = new PCPhone();
//		phone.setPhoneNumber(new PhoneNumber("02129637807"));
//		phone.setType(PhoneType.HOME.getType());
//
//		phones.add(phone);
//		
//		PCActiveContact contact = new PCActiveContact();
//		contact.setFirstName("Maria Gabriela");
//		contact.setLastName("Valdes");
//		contact.setPhones(phones);
//		
//		try {
//			pm.makePersistent(contact);
//		}
//		finally {
//			pm.close();
//		}
		
		ResponseBuilder rbuilder = Response.ok();
		
		return rbuilder.build();
	}
	
	public void createDummyApplication(){
		PCApplication application = new PCApplication();

		PCAppInfo appInfo = new PCAppInfo();
		appInfo.setAppUrl("");
		appInfo.setAppVersion("1.0.0");
		appInfo.setPublishDate(Calendar.getInstance().getTime());
		application.setAppInfo(appInfo);
		
		application.setAvailableFunctionalities(new ArrayList<PCFunctionality>());
		
		PCDevice device = new PCDevice();
		device.setModel("");
		
		PCOs os = new PCOs();
		os.setOsType("Blackberry");
		os.setId(PCOsTypeId.getOsIdFromType("Blackberry"));
		device.setOs(os);
		
		device.setVersion("4.5");
		application.setDevice(device);
		
		try {
            pm.makePersistent(application);
        } 
		finally {
            pm.close();
        }
		
	}
}
