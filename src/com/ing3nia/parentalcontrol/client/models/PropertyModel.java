package com.ing3nia.parentalcontrol.client.models;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;


public class PropertyModel implements IsSerializable, Serializable {
	private static final long serialVersionUID = 1L;
	
	private String keyId;
	
	private String description;
	
	private String value;
	
	private int id;
	
	private String creationDate;

	public PropertyModel() {
		super();
	}

	public PropertyModel(String keyId, String description, String value, int id,
			String creationDate) {
		super();
		this.keyId = keyId;
		this.description = description;
		this.value = value;
		this.id = id;
		this.creationDate = creationDate;
	}
	
	public PropertyModel(String description, String value, int id,
			String creationDate) {
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
		return keyId;
	}

	public void setKeyId(String keyId) {
		this.keyId = keyId;
	}
}
