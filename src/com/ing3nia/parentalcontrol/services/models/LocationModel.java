package com.ing3nia.parentalcontrol.services.models;

import com.google.appengine.api.datastore.GeoPt;

public class LocationModel {
	private String latitude;
	
	private String longitude;

	public LocationModel() {
		super();
	}

	public LocationModel(String latitude, String longitude) {
		super();
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	
	public GeoPt convertToGeoPt() {
		GeoPt location = new GeoPt(new Float(latitude), new Float(longitude));
		
		return location;
	}
	
	public static LocationModel convertToLocationModel(GeoPt point) {
		return new LocationModel(String.valueOf(point.getLatitude()), String.valueOf(point.getLongitude()));
	}
}
