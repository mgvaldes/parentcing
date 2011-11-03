package com.ing3nia.parentalcontrol.services.models;

import com.ing3nia.parentalcontrol.models.PCFunctionality;

public class FunctionalityModel {
	private int id;
	
	private String description;

	public FunctionalityModel() {
		super();
	}

	public FunctionalityModel(int id, String description) {
		super();
		this.id = id;
		this.description = description;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public static FunctionalityModel convertToFunctionalityModel(PCFunctionality functionality) {
		return new FunctionalityModel(functionality.getId(), functionality.getDescription());
	}
}
