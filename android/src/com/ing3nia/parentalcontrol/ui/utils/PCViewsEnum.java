package com.ing3nia.parentalcontrol.ui.utils;

public enum PCViewsEnum {
	LOGIN_VIEW(0),
	SMARTPHONE_MAP_VIEW(1),
	SMARTPHONE_LIST_VIEW(2),
	DAILY_ROUTE_VIEW(3),
	ALERT_LIST_VIEW(4),
	RULE_LIST_VIEW(5),
	NEW_RULE_VIEW(6),
	EDIT_RULE_VIEW(7),
	CONTACT_LIST_VIEW(8),
	EMERGENCY_CONTACT_LIST_VIEW(9),
	NEW_EMERGENCY_CONTACT_VIEW(10),
	EDIT_EMERGENCY_CONTACT_VIEW(11),
	SMARTPHONE_SETTINGS_VIEW(12),
	TICKETS_LIST_VIEW(12),
	NEW_TICKET_VIEW(13),
	ADMIN_USERS_LIST_VIEW(14),
	NEW_ADMIN_USER_VIEW(15);
	
	private int id;
	
	PCViewsEnum(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
