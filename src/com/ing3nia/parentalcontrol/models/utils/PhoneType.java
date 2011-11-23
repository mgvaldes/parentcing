package com.ing3nia.parentalcontrol.models.utils;

public enum PhoneType {
	HOME(1, "Home"),
	MOBILE(2, "Mobile"),
	WORK(3, "Work"),
	PARTICULAR(4, "Particular"),
	PAGER(5, "Pager"),
	WORK_FAX(6, "Work Fax"),
	HOME_FAX(7, "Home Fax"),
	PARTICULAR_FAX(8, "Particular Fax"),
	OTHER(9, "Other");
	
	private int type;
	
	private String description;
	
	PhoneType(int type, String desc) {
		this.type = type;
		this.description = desc;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public static String getPhoneDescByType(int type) {
		String desc = "";
		PhoneType[] phoneTypes = PhoneType.values();
		
		for (PhoneType p : phoneTypes) {
			if (type == p.getType()) {
				desc = p.getDescription();
				break;
			}
		}
		
		return desc;
	}
}
