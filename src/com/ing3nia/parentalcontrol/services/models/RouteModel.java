package com.ing3nia.parentalcontrol.services.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.google.appengine.api.datastore.GeoPt;
import com.ing3nia.parentalcontrol.models.PCRoute;

public class RouteModel {
	private ArrayList<LocationModel> route;
	
	private String date;

	public RouteModel() {
		super();
	}

	public RouteModel(ArrayList<LocationModel> route, String date) {
		super();
		this.route = route;
		this.date = date;
	}

	public ArrayList<LocationModel> getRoute() {
		return route;
	}

	public void setRoute(ArrayList<LocationModel> route) {
		this.route = route;
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
		
		routeModel.setRoute(points);
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		routeModel.setDate(formatter.format(route.getDate()));
		
		return routeModel;
	}
	
	public PCRoute convertToPCRoute() {
		PCRoute newRoute = new PCRoute();
		ArrayList<GeoPt> points = new ArrayList<GeoPt>();
		GeoPt p;
		
		for (LocationModel loc : this.route) {
			p = new GeoPt(Long.valueOf(loc.getLatitude()), Long.valueOf(loc.getLongitude()));
			points.add(p);
		}
		
		newRoute.setRoute(points);
		
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
