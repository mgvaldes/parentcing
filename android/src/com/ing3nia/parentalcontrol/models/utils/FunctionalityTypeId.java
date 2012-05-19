package com.ing3nia.parentalcontrol.models.utils;

public enum FunctionalityTypeId {
	BROWSER_ACCESS(1, "Browser Access"),
	TOTAL_BLOCK(2, "Total Block"),
	OUTGOING_CALLS(3, "Outgoing Calls"),
	INCOMMING_CALLS(4, "Incomming Calls"),
	OUTGOING_SMS(5, "Outgoing SMS"),
	INCOMMING_SMS(6, "Incomming SMS");
	
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
	
	public static String findById(int id) {
		FunctionalityTypeId[] funcs = FunctionalityTypeId.values();
		
		for (FunctionalityTypeId f : funcs) {
			if (f.getId() == id) {
				return f.getDescription();
			}
		}
		
		return "";
	}
	
	public static int findByDescription(String desc) {
		FunctionalityTypeId[] funcs = FunctionalityTypeId.values();
		
		for (FunctionalityTypeId f : funcs) {
			if (f.getDescription().equals(desc)) {
				return f.getId();
			}
		}
		
		return -1;
	}
	
	public static boolean foundId(int type) {
		FunctionalityTypeId[] funcs = FunctionalityTypeId.values();
		boolean exists = false;
		
		for (FunctionalityTypeId f : funcs) {
			if (f.getId() == type) {
				exists = true;
				break;
			}
		}
		
		return exists;
	}
	
	public static FunctionalityTypeId[] normalDisabledFuncs() {
		FunctionalityTypeId[] normalFuncs = {BROWSER_ACCESS, TOTAL_BLOCK, OUTGOING_CALLS, INCOMMING_CALLS, OUTGOING_SMS, INCOMMING_SMS}; 
		return normalFuncs;
	}
	
	public static FunctionalityTypeId[] speedLimitDisabledFuncs() {
		FunctionalityTypeId[] speedLimitFuncs = {BROWSER_ACCESS, OUTGOING_CALLS, INCOMMING_CALLS, OUTGOING_SMS, INCOMMING_SMS}; 
		return speedLimitFuncs;
	}
}
