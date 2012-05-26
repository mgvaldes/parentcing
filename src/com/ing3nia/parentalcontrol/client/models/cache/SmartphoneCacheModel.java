package com.ing3nia.parentalcontrol.client.models.cache;

import java.util.ArrayList;

import com.google.appengine.api.datastore.Key;

public class SmartphoneCacheModel {
	public String keyId;
	public String name;
	public String serialNumber;
	
	public SmartphoneCacheModel(){
	
	}

	public String getKeyId() {
		return keyId;
	}

	public void setKeyId(String keyId) {
		this.keyId = keyId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

}
