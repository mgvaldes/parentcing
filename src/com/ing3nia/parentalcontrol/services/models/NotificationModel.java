package com.ing3nia.parentalcontrol.services.models;

public class NotificationModel {
	private int type;
	
	private String date;

	public NotificationModel() {
		super();
	}

	public NotificationModel(int type, String date) {
		super();
		this.type = type;
		this.date = date;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
}
