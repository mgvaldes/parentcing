package com.ing3nia.parentalcontrol.client.utils;

public enum CategoryType {
	/**
	 * 
	 */
	BROWSER_ACCESS(1, "Browser Access"),
	
	/**
	 * 
	 */
	TOTAL_BLOCK(2, "Total Block"),
	
	/**
	 * 
	 */
	CALLS(3, "Calls"),
	
	/**
	 * 
	 */
	SMS(4, "SMS"),
	
	/**
	 * 
	 */
	CONTACTS(5, "Contacts"),
	
	/**
	 * 
	 */
	ROUTES(6, "Routes");
	
	private int id;
	
	private String description;
	
	CategoryType(int id, String description) {
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
}
