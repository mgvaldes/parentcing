package com.ing3nia.parentalcontrol.client.models;

import java.io.Serializable;

public class DeviceModel implements Serializable {
	private static final long serialVersionUID = 1L;
	
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
	

}
