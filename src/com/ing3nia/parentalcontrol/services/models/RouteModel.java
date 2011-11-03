package com.ing3nia.parentalcontrol.services.models;

import java.util.ArrayList;

import com.google.appengine.api.datastore.GeoPt;
import com.ing3nia.parentalcontrol.models.PCRoute;

public class RouteModel {
	private ArrayList<LocationModel> route;

	public RouteModel() {
		super();
	}

	public RouteModel(ArrayList<LocationModel> route) {
		super();
		this.route = route;
	}

	public ArrayList<LocationModel> getRoute() {
		return route;
	}

	public void setRoute(ArrayList<LocationModel> route) {
		this.route = route;
	}
	
	public static RouteModel convertToRouteModel(PCRoute route) {
		RouteModel routeModel = new RouteModel();
		
		ArrayList<LocationModel> points = new ArrayList<LocationModel>();
		
		for (GeoPt point : route.getRoute()) {
			points.add(LocationModel.convertToLocationModel(point));
		}
		
		routeModel.setRoute(points);
		
		return routeModel;
	}
}
