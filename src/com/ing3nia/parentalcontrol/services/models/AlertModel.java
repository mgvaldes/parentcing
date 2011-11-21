package com.ing3nia.parentalcontrol.services.models;

import java.util.ArrayList;

import com.ing3nia.parentalcontrol.client.models.NotificationModel;

public class AlertModel {
	private String id;
	
	private ArrayList<NotificationModel> alerts;

	public AlertModel() {
		super();
	}

	public AlertModel(String id, ArrayList<NotificationModel> alerts) {
		super();
		this.id = id;
		this.alerts = alerts;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ArrayList<NotificationModel> getAlerts() {
		return alerts;
	}

	public void setAlerts(ArrayList<NotificationModel> alerts) {
		this.alerts = alerts;
	}
}
