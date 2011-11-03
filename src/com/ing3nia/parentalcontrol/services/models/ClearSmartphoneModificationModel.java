package com.ing3nia.parentalcontrol.services.models;

public class ClearSmartphoneModificationModel {
	private String id;
	
	private String modKey;

	public ClearSmartphoneModificationModel() {
		super();
	}

	public ClearSmartphoneModificationModel(String id, String modKey) {
		super();
		this.id = id;
		this.modKey = modKey;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getModKey() {
		return modKey;
	}

	public void setModKey(String modKey) {
		this.modKey = modKey;
	}
}
