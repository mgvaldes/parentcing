package com.ing3nia.parentalcontrol.services.models.utils;

import com.google.appengine.api.datastore.GeoPt;
import com.ing3nia.parentalcontrol.client.models.LocationModel;

public class GeoPtModelUtils {
	public static LocationModel convertToLocationModel(GeoPt point) {
		return new LocationModel(String.valueOf(point.getLatitude()), String.valueOf(point.getLongitude()));
	}
}
