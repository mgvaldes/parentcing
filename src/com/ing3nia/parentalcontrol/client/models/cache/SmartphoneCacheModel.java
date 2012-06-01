package com.ing3nia.parentalcontrol.client.models.cache;

import java.io.Serializable;

import com.ing3nia.parentalcontrol.client.models.DeviceModel;
import com.ing3nia.parentalcontrol.client.models.LocationModel;
import com.ing3nia.parentalcontrol.services.models.ApplicationModel;

public class SmartphoneCacheModel implements Serializable{
	private static final long serialVersionUID = 1L;
	public String keyId;
	public String name;
	public String serialNumber;
	public LocationModel location;
	public DeviceModel device;
	public ApplicationModel application;
	
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

	public LocationModel getLocation() {
		return location;
	}

	public void setLocation(LocationModel location) {
		this.location = location;
	}

	public DeviceModel getDevice() {
		return device;
	}

	public void setDevice(DeviceModel device) {
		this.device = device;
	}

	public ApplicationModel getApplication() {
		return application;
	}

	public void setApplication(ApplicationModel application) {
		this.application = application;
	}
}
