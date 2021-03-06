package com.ing3nia.parentalcontrol.services.models.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.jdo.PersistenceManager;

import com.google.appengine.api.datastore.GeoPt;
import com.google.appengine.api.datastore.Key;
import com.ing3nia.parentalcontrol.client.models.LocationModel;
import com.ing3nia.parentalcontrol.client.models.RouteModel;
import com.ing3nia.parentalcontrol.models.PCRoute;
import com.ing3nia.parentalcontrol.services.utils.ServiceUtils;

public class RouteModelUtils {
	public static RouteModel convertToRouteModel(PCRoute route) {
		RouteModel routeModel = new RouteModel();
		
		ArrayList<LocationModel> points = new ArrayList<LocationModel>();
		
		for (GeoPt point : route.getRoute()) {
			points.add(LocationModelUtils.convertToLocationModel(point));
		}
		
		routeModel.setPoints(points);
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
		routeModel.setDate(formatter.format(route.getDate()));
		
		return routeModel;
	}
	
	public static Key convertToPCRoute(RouteModel r) {
		PCRoute newRoute = new PCRoute();
		ArrayList<GeoPt> geoPoints = new ArrayList<GeoPt>();
		GeoPt p;
		PersistenceManager pm = ServiceUtils.PMF.getPersistenceManager();
		
		for (LocationModel loc : r.getPoints()) {
			p = new GeoPt(Float.valueOf(loc.getLatitude()), Float.valueOf(loc.getLongitude()));
			geoPoints.add(p);
		}
		
		newRoute.setRoute(geoPoints);
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		
		try {
			newRoute.setDate(formatter.parse(r.getDate()));
		} 
		catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		pm.makePersistent(newRoute);
		
		pm.close();
		
		return newRoute.getKey();
	}
}
