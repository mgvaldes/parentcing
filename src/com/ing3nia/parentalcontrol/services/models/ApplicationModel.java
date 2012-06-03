package com.ing3nia.parentalcontrol.services.models;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.google.appengine.api.datastore.Key;
import com.ing3nia.parentalcontrol.models.PCApplication;
import com.ing3nia.parentalcontrol.services.exceptions.SessionQueryException;

public class ApplicationModel implements Serializable{
	
	private static Logger logger = Logger.getLogger(ApplicationModel.class.getName());
	
	public static Key findPCApplicationByAppVersion(String appVersion, PersistenceManager pm) throws SessionQueryException {
		logger.info("[ApplicationModel] Searching for PCApplication with appVersion: " + appVersion);
		
		Query query = pm.newQuery(PCApplication.class);
	    query.setFilter("appInfo.appVersion == appVersionParam");
	    query.declareParameters("String appVersionParam");
	    query.setRange(0, 1);
	    
    	List<PCApplication> result = (List<PCApplication>)query.execute(appVersion);
    	
    	if (!result.isEmpty()) {
    		return result.get(0).getKey();
    	}
    	else {
    		throw new SessionQueryException();
    	}

	}
}
