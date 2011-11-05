package com.ing3nia.parentalcontrol.services.models;

import java.util.ArrayList;

public class ActivityNotifyModel {
	private String id;
	
	private RouteModel route;
	
	private ArrayList<NotificationModel> alerts;
	
	private ArrayList<ServiceStatisticsModel> syncStat;

	public ActivityNotifyModel() {
		super();
	}

	public ActivityNotifyModel(String id, RouteModel route, ArrayList<NotificationModel> alerts,
			ArrayList<ServiceStatisticsModel> syncStat) {
		super();
		this.id = id;
		this.route = route;
		this.alerts = alerts;
		this.syncStat = syncStat;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public RouteModel getRoute() {
		return route;
	}

	public void setRoute(RouteModel route) {
		this.route = route;
	}

	public ArrayList<NotificationModel> getAlerts() {
		return alerts;
	}

	public void setAlerts(ArrayList<NotificationModel> alerts) {
		this.alerts = alerts;
	}

	public ArrayList<ServiceStatisticsModel> getSyncStat() {
		return syncStat;
	}

	public void setSyncStat(ArrayList<ServiceStatisticsModel> syncStat) {
		this.syncStat = syncStat;
	}
}
