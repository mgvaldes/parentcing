package com.ing3nia.parentalcontrol.client.handlers.utils;

import com.google.gwt.maps.client.geocode.Geocoder;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.user.client.Timer;
import com.ing3nia.parentalcontrol.client.views.async.DeviceRouteLocationCallbackHandler;

public class ReverseGeocodingTimer extends Timer {

	Geocoder geocoder;
	LatLng deviceLoc;
	DeviceRouteLocationCallbackHandler callback;
	
	public ReverseGeocodingTimer(Geocoder geocoder, LatLng deviceLoc, DeviceRouteLocationCallbackHandler callback){
		this.deviceLoc = deviceLoc;
		this.callback = callback;
		this.geocoder = geocoder;
		
	}
	
	@Override
	public void run() {
		geocoder.getLocations(deviceLoc, callback);
		
	}

}
