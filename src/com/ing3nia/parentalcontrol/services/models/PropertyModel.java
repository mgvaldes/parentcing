package com.ing3nia.parentalcontrol.services.models;

import java.util.Date;

import com.ing3nia.parentalcontrol.models.PCProperty;

public class PropertyModel {
	
	private String keyId;
	
	private String description;
	
	private String value;
	
	private int id;
	
	private Date creationDate;

	public PropertyModel() {
		super();
	}

	public PropertyModel(String keyId, String description, String value, int id,
			Date creationDate) {
		super();
		this.keyId = keyId;
		this.description = description;
		this.value = value;
		this.id = id;
		this.creationDate = creationDate;
	}
	
	public PropertyModel(String description, String value, int id,
			Date creationDate) {
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

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	
	
	public String getKeyId() {
		return keyId;
	}

	public void setKeyId(String keyId) {
		this.keyId = keyId;
	}

	public static PropertyModel convertToPropertyModel(PCProperty property) {
		return new PropertyModel(property.getDescription(), property.getValue(), property.getId(), property.getCreationDate());
	}
}
