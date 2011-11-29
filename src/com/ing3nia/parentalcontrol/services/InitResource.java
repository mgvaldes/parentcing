package com.ing3nia.parentalcontrol.services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.jdo.PersistenceManager;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.datanucleus.store.mapped.expression.ArrayLiteral;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.gson.JsonObject;
import com.ing3nia.parentalcontrol.client.utils.FunctionalityTypeId;
import com.ing3nia.parentalcontrol.client.utils.PCPropertyType;
import com.ing3nia.parentalcontrol.models.PCActivityStatistics;
import com.ing3nia.parentalcontrol.models.PCApplication;
import com.ing3nia.parentalcontrol.models.PCContact;
import com.ing3nia.parentalcontrol.models.PCModification;
import com.ing3nia.parentalcontrol.models.PCPhone;
import com.ing3nia.parentalcontrol.models.PCProperty;
import com.ing3nia.parentalcontrol.models.PCRule;
import com.ing3nia.parentalcontrol.models.PCSimpleContact;
import com.ing3nia.parentalcontrol.models.PCSmartphone;
import com.ing3nia.parentalcontrol.models.PCApplication.PCAppInfo;
import com.ing3nia.parentalcontrol.models.PCDevice;
import com.ing3nia.parentalcontrol.models.PCDevice.PCOs;
import com.ing3nia.parentalcontrol.models.PCFunctionality;
import com.ing3nia.parentalcontrol.models.utils.PCOsTypeId;
import com.ing3nia.parentalcontrol.models.utils.WSStatus;
import com.ing3nia.parentalcontrol.services.utils.ServiceUtils;

@Path("init")
public class InitResource {
	private PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
	
	public InitResource() {
		
	}
	
	@GET
	public Response doGet() {

		//PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
		/*
		Key smartKey = KeyFactory.stringToKey("aglub19hcHBfaWRyHwsSBlBDVXNlchgBDAsSDFBDU21hcnRwaG9uZRiQAQw");
		PCSmartphone smart = (PCSmartphone)pm.getObjectById(PCSmartphone.class, smartKey);
	*/
		
		//createDummyApplication();
		
//		PCFunctionality func;
//		
//		FunctionalityTypeId[] funcs = FunctionalityTypeId.values();
//		
//		for (FunctionalityTypeId f : funcs) {
//			func = new PCFunctionality();
//			func.setId(f.getId());
//			func.setDescription(f.getDescription());
//			pm.makePersistent(func);
//		}
//		
//		pm.close();
		
		ResponseBuilder rbuilder;
		rbuilder = Response.ok("{}", MediaType.APPLICATION_JSON);
		
		return rbuilder.build();
		
//		String rule = "aglub19hcHBfaWRyKgsSBlBDVXNlchgBDAsSDFBDU21hcnRwaG9uZRhXDAsSBlBDUnVsZRhgDA";
		
//		pm.deletePersistent(smart.getRules().get(0));
		
//		ArrayList<String> deletedRules = new ArrayList<String>();
//		deletedRules.add(rule);
//		
//		smart.getModification().setDeletedRules(deletedRules);
//		
//		pm.close();

		/*
		ArrayList<Key> inactive = smart.getInactiveContacts();
		
		PCPhone phone = new PCPhone();
		phone.setPhoneNumber("04267336220");
		phone.setType(1);
		pm.makePersistent(phone);
		
		PCSimpleContact pcsimple = new PCSimpleContact();
		pcsimple.setFirstName("Maria");
		pcsimple.setLastName("Elena");
		pcsimple.setPhone(phone.getKey());		
		pm.makePersistent(pcsimple);
		
		inactive.add(pcsimple.getKey());
		smart.setInactiveContacts(inactive);
		
		pm.close();
//		
//		PCProperty prop = new PCProperty();
//		prop.setCreationDate(Calendar.getInstance().getTime());
//		prop.setDescription("SPEED_LIMIT");
//		prop.setId(PCPropertyType.SPEED_LIMIT);	
//		prop.setValue("80");
//		
//		ArrayList<PCProperty> props = smart.getProperties();
//		
//		props.add(prop);
//		smart.setProperties(props);
//		
//		PCFunctionality func = new PCFunctionality();
//		func.setDescription("BROWSER_ACCESS");
//		func.setId(FunctionalityTypeId.BROWSER_ACCESS_ID);
//		
//		pm.makePersistent(func);
//		
//		ArrayList<Key> funcs = new ArrayList<Key>();
//		funcs.add(func.getKey());
//		
//		PCRule rule = new PCRule();
//		Date date = Calendar.getInstance().getTime();
//		rule.setCreationDate(date);
//		rule.setDisabledFunctionalities(funcs);
//		rule.setEndDate(date);
//		rule.setStartDate(date);
//		
//		ArrayList<PCRule> rules = smart.getRules();
//		rules.add(rule);
//		smart.setRules(rules);
//		
//		pm.close();
		
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
		
		ResponseBuilder rbuilder;
		JsonObject okResponse = WSStatus.OK.getStatusAsJson();
		
		//okResponse.addProperty("key", rule);
		
		rbuilder = Response.ok(okResponse.toString(), MediaType.APPLICATION_JSON);
		
		return rbuilder.build();*/
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
