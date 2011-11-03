package com.ing3nia.parentalcontrol.services.models;

import com.ing3nia.parentalcontrol.models.PCDevice;
import com.ing3nia.parentalcontrol.models.utils.PCOsTypeId;

public class DeviceModel {
	private String model;

	private String version;
	
	private int type;

	public DeviceModel() {
		super();
	}

	public DeviceModel(String model, String version, int type) {
		super();
		this.model = model;
		this.version = version;
		this.type = type;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	public PCDevice convertToPCDevice() {
		PCDevice device = new PCDevice();
		
		device.setModel(this.model);
		device.setVersion(this.version);
		
		PCDevice.PCOs pcOs = new PCDevice.PCOs();
		pcOs.setId(this.type);
		pcOs.setOsType(PCOsTypeId.getOsTypeFromId(this.type));
		
		device.setOs(pcOs);
		
		return device;
	}
	
	public static DeviceModel convertToDeviceModel(PCDevice device) {
		return new DeviceModel(device.getModel(), device.getVersion(), device.getOs().getId());
	}
}
