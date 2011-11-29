package com.ing3nia.parentalcontrol.services.models.utils;

import java.text.SimpleDateFormat;

import javax.jdo.PersistenceManager;

import com.google.appengine.api.datastore.Key;
import com.ing3nia.parentalcontrol.client.models.PropertyModel;
import com.ing3nia.parentalcontrol.models.PCProperty;
import com.ing3nia.parentalcontrol.services.utils.ServiceUtils;

public class PropertyModelUtils {
	public static PropertyModel convertToPropertyModel(Key propKey) {
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
		PCProperty property = pm.getObjectById(PCProperty.class, propKey);
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
		
		PropertyModel p = new PropertyModel(property.getDescription(), property.getValue(), property.getId(), formatter.format(property.getCreationDate())); 
		
		pm.close();
		
		return p;
	}
}
