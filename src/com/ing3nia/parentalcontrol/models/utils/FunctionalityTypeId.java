package com.ing3nia.parentalcontrol.models.utils;

/**
 *  
 * 
 * @author Ing3nia
 * @author Javier Fernandez
 * @author Maria Gabriela Valdes
 * 
 * @version 1.0
 *
 */
public enum FunctionalityTypeId {
	/**
	 * 
	 */
	BROWSER_ACCESS(1, "BROWSER_ACCESS"),
	
	/**
	 * 
	 */
	TOTAL_BLOCK(2, "TOTAL_BLOCK"),
	
	/**
	 * 
	 */
	OUTGOING_CALLS(3, "OUTGOING_CALLS"),
	
	/**
	 * 
	 */
	INCOMMING_CALLS(4, "INCOMMING_CALLS"),
	
	/**
	 * 
	 */
	OUTGOING_SMS(5, "OUTGOING_SMS"),
	
	/**
	 * 
	 */
	INCOMMING_SMS(6, "INCOMMING_SMS");
	
	private int id;
	
	private String description;
	
	FunctionalityTypeId(int id, String description) {
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
	
	public String findById(int id) {
		FunctionalityTypeId[] funcs = FunctionalityTypeId.values();
		
		for (FunctionalityTypeId f : funcs) {
			if (f.getId() == id) {
				return f.getDescription();
			}
		}
		
		return "";
	}
}
