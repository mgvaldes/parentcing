package com.ing3nia.parentalcontrol.services;

import java.util.ArrayList;
import java.util.Calendar;

import javax.jdo.PersistenceManager;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import com.ing3nia.parentalcontrol.models.PCApplication;
import com.ing3nia.parentalcontrol.models.PCDevice;
import com.ing3nia.parentalcontrol.models.PCFunctionality;
import com.ing3nia.parentalcontrol.models.PCApplication.PCAppInfo;
import com.ing3nia.parentalcontrol.models.PCDevice.PCOs;
import com.ing3nia.parentalcontrol.models.utils.PCOsTypeId;
import com.ing3nia.parentalcontrol.services.utils.ServiceUtils;

@Path("init-app")
public class InitApplicationResource {

	public InitApplicationResource() {
		
	}
	
	@GET
	public Response doGet() {
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
		
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
		
        pm.makePersistent(application);
        
        pm.close();
		
		ResponseBuilder rbuilder;
		rbuilder = Response.ok("{}", MediaType.APPLICATION_JSON);
		
		return rbuilder.build();
	}
}
