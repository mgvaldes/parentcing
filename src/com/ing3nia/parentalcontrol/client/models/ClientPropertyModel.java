package com.ing3nia.parentalcontrol.client.models;

public class ClientPropertyModel {
	private String key;
	
	private String description;
	
	private String value;
	
	private int id;
	
	private String creationDate;

	public ClientPropertyModel() {
		super();
	}

	public ClientPropertyModel(String keyId, String description, String value, int id,
			String creationDate) {
		super();
		this.key = keyId;
		this.description = description;
		this.value = value;
		this.id = id;
		this.creationDate = creationDate;
	}
	
	public ClientPropertyModel(String description, String value, int id, String creationDate) {
		super();
		this.description = description;
		this.value = value;
		this.id = id;
		this.creationDate = creationDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}
	
	
	public String getKeyId() {
		return key;
	}

	public void setKeyId(String keyId) {
		this.key = keyId;
	}
}
