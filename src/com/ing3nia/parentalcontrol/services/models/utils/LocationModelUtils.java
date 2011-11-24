package com.ing3nia.parentalcontrol.services.models.utils;

import com.google.appengine.api.datastore.GeoPt;
import com.ing3nia.parentalcontrol.client.models.LocationModel;


public class LocationModelUtils {
	public static GeoPt convertToGeoPt(LocationModel locationModel) {
		GeoPt location = new GeoPt(new Float(locationModel.getLatitude()), new Float(locationModel.getLongitude()));
		
		return location;
	}
	
	public static LocationModel convertToLocationModel(GeoPt point){
		return new LocationModel(String.valueOf(point.getLatitude()), String.valueOf(point.getLongitude()));
	}
}
