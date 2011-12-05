package com.ing3nia.parentalcontrol.server;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.ing3nia.parentalcontrol.client.models.AlertModel;
import com.ing3nia.parentalcontrol.client.rpc.WeekAlertsService;
import com.ing3nia.parentalcontrol.models.PCActivityStatistics;
import com.ing3nia.parentalcontrol.models.PCNotification;
import com.ing3nia.parentalcontrol.models.PCSmartphone;
import com.ing3nia.parentalcontrol.services.models.utils.NotificationModelUtils;
import com.ing3nia.parentalcontrol.services.utils.ServiceUtils;

public class WeekAlertsServiceImpl extends RemoteServiceServlet implements WeekAlertsService {

	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(WeekAlertsServiceImpl.class.getName());
	
	public WeekAlertsServiceImpl() {
		//logger.addHandler(new ConsoleHandler());
	}

	@Override
	public ArrayList<AlertModel> weekAlerts(String smartKey, Date now) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(now);
		calendar.add(Calendar.DAY_OF_MONTH, -7);
		Date thisweek = calendar.getTime();
		
		ArrayList<AlertModel> weekAlerts = new ArrayList<AlertModel>();
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
		
		PCSmartphone smartphone = pm.getObjectById(PCSmartphone.class, smartKey);
		
		logger.info("[WeekAlertsService] maxDateParam: " + thisweek + " minDateParam: " + now);
		
		Query query = pm.newQuery(PCActivityStatistics.class);
//		query.setFilter("smartphone == smartphoneParam");
//	    query.declareParameters(Key.class.getName() + " smartphoneParam");
	    query.setFilter("smartphone == smartphoneParam && date >= maxDateParam && date < minDateParam");
	    query.declareParameters(Key.class.getName() + " smartphoneParam, " + Date.class.getName() + " maxDateParam, " + Date.class.getName() + " minDateParam");
	    
	    try {
	    	List<PCActivityStatistics> activities = (List<PCActivityStatistics>)query.execute(KeyFactory.stringToKey(smartKey), thisweek, now);
	    	ArrayList<Key> auxAlerts = new ArrayList<Key>();
	    	PCNotification auxAlert;
	    	
	    	if (!activities.isEmpty()) {
	    		logger.info("[WeekAlertsService] Activities size: " + activities.size());
	    		
	    		for (PCActivityStatistics act : activities) {
	    			auxAlerts = act.getNotifications();
	    			
	    			for (Key alertKey : auxAlerts) {
	    				auxAlert = pm.getObjectById(PCNotification.class, alertKey);
	    				weekAlerts.add(NotificationModelUtils.convertToAlertModel(auxAlert, smartphone.getName()));
	    			}
	    		}
	    	}
	    	else {
	    		logger.info("[WeekAlertsService] No PCActivityStatistics's found");
	    	}
	    }
	    catch (Exception e) {
			logger.info("[WeekAlertsService] An error ocurred while finding the PCActivityStatistics " + e.getMessage());
			weekAlerts = null;
		}
	    
	    pm.close();
		
		return weekAlerts;
	}
}
