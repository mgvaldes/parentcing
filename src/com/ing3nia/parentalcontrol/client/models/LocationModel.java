package com.ing3nia.parentalcontrol.client.models;

import java.io.Serializable;

public class LocationModel implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String latitude;
	
	private String longitude;

	public LocationModel() {
	}

	public LocationModel(String latitude, String longitude) {
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
