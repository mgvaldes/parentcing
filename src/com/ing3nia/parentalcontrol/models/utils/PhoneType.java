package com.ing3nia.parentalcontrol.models.utils;

public enum PhoneType {
	HOME(1),
	MOBILE(2),
	WORK(3),
	PARTICULAR(4),
	PAGER(5),
	WORK_FAX(6),
	HOME_FAX(7),
	PARTICULAR_FAX(8),
	OTHER(9);
	
	private int type;
	
	PhoneType(int type) {
		this.type = type;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
}
