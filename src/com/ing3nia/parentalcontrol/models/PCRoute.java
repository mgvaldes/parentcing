package com.ing3nia.parentalcontrol.models;

import java.util.ArrayList;
import java.util.Date;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import com.google.appengine.api.datastore.GeoPt;

@PersistenceCapable
public class PCRoute {
	@Persistent
	private ArrayList<GeoPt> route;
	
	@Persistent
	private Date date;

	public PCRoute() {
		super();
	}

	public PCRoute(ArrayList<GeoPt> route, Date date) {
		super();
		this.route = route;
		this.date = date;
	}

	public ArrayList<GeoPt> getRoute() {
		return route;
	}

	public void setRoute(ArrayList<GeoPt> route) {
		this.route = route;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
}
