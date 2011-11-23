package com.ing3nia.parentalcontrol.client.models;

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
	

	

}
