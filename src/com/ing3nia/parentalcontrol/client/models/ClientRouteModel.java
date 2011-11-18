package com.ing3nia.parentalcontrol.client.models;

import java.util.ArrayList;

public class ClientRouteModel {
	private ArrayList<ClientLocationModel> points;
	
	private String date;

	public ClientRouteModel() {
		super();
	}

	public ClientRouteModel(ArrayList<ClientLocationModel> points, String date) {
		super();
		this.points = points;
		this.date = date;
	}

	public ArrayList<ClientLocationModel> getPoints() {
		return points;
	}

	public void setPoints(ArrayList<ClientLocationModel> points) {
		this.points = points;
	}
	
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
}
