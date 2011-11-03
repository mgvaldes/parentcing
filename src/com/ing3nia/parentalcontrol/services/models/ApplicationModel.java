package com.ing3nia.parentalcontrol.services.models;

import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.google.appengine.api.datastore.Key;
import com.ing3nia.parentalcontrol.models.PCApplication;
import com.ing3nia.parentalcontrol.services.utils.ServiceUtils;

public class ApplicationModel {
	
	private static Logger logger = Logger.getLogger(ApplicationModel.class.getName());
	
	public static Key findPCApplicationByAppVersion(String appVersion) {
		PCApplication application = null;
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager(); 
		
		logger.info("[ApplicationModel] Obteniendo PersistenceManager para buscar PCApplication con appVersion: " + appVersion);
		
		Query query = pm.newQuery(PCApplication.class);
		
		logger.info("[ApplicationModel] Aplicando filtro de bœsqueda para buscar por appVersion");
		
	    query.setFilter("appInfo.appVersion == appVersionParam");
	    query.declareParameters("String appVersionParam");
	    
	    try {
	    	logger.info("[ApplicationModel] Ejecutando query para buscar PCApplication con appVersion: " + appVersion);
	    	
	    	List<PCApplication> result = (List<PCApplication>)query.execute(appVersion);
	    	
	    	Iterator iter = result.iterator();
	    		    	
	    	if (iter.hasNext()) {
	    		application = (PCApplication)iter.next();
	    	}	    	
	    }
	    finally {
	    	pm.close();
	    }
		
	    return application.getKey();
	}
}
