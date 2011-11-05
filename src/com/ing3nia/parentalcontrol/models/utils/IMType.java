package com.ing3nia.parentalcontrol.models.utils;

public enum IMType {
	AIM(1),
	WINDOWS_LIVE(2),
	YAHOO(3),
	SKYPE(4),
	QQ(5),
	GOOGLE_TALK(6),
	ICQ(7),
	JABBER(8),
	CUSTOM(9);
	
	private int type;
	
	IMType(int type) {
		this.type = type;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
}
