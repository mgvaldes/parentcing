package com.ing3nia.parentalcontrol.services.models;

import com.ing3nia.parentalcontrol.client.models.LocationModel;
import com.ing3nia.parentalcontrol.client.models.ModificationModel;

public class InternalModificationsModel {
	private String id;
	
	private ModificationModel modification;
	
	private LocationModel location;

	public InternalModificationsModel() {
		super();
	}

	public InternalModificationsModel(String id,
			ModificationModel modification, LocationModel location) {
		super();
		this.id = id;
		this.modification = modification;
		this.location = location;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ModificationModel getModification() {
		return modification;
	}

	public void setModification(ModificationModel modification) {
		this.modification = modification;
	}

	public LocationModel getLocation() {
		return location;
	}

	public void setLocation(LocationModel location) {
		this.location = location;
	}
}
