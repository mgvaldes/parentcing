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

	public static RouteModel convertToRouteModel(PCRoute route) {
		RouteModel routeModel = new RouteModel();
		
		ArrayList<LocationModel> points = new ArrayList<LocationModel>();
		
		for (GeoPt point : route.getRoute()) {
			points.add(LocationModel.convertToLocationModel(point));
		}
		
		routeModel.setPoints(points);
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		routeModel.setDate(formatter.format(route.getDate()));
		
		return routeModel;
	}
	
	public PCRoute convertToPCRoute() {
		PCRoute newRoute = new PCRoute();
		ArrayList<GeoPt> geoPoints = new ArrayList<GeoPt>();
		GeoPt p;
		
		for (LocationModel loc : this.points) {
			p = new GeoPt(Float.valueOf(loc.getLatitude()), Float.valueOf(loc.getLongitude()));
			geoPoints.add(p);
		}
		
		newRoute.setRoute(geoPoints);
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		
		try {
			newRoute.setDate(formatter.parse(this.date));
		} 
		catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return newRoute;
	}
}
