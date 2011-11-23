package com.ing3nia.parentalcontrol.client.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.google.appengine.api.datastore.GeoPt;
import com.ing3nia.parentalcontrol.models.PCRoute;

public class RouteModel {
	private ArrayList<LocationModel> points;
	
	private String date;

	public RouteModel() {
		super();
	}

	public RouteModel(ArrayList<LocationModel> points, String date) {
		super();
		this.points = points;
		this.date = date;
	}

	public ArrayList<LocationModel> getPoints() {
		return points;
	}

	public void setPoints(ArrayList<LocationModel> points) {
		this.points = points;
	}
	
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
}
