package com.ing3nia.parentalcontrol.client.models;

public class ClientDeviceModel {
	private String model;

	private String version;
	
	private String type;

	public ClientDeviceModel() {
		super();
	}

	public ClientDeviceModel(String model, String version, String type) {
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
