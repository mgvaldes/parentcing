package com.ing3nia.parentalcontrol.models;

import java.util.ArrayList;
import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.GeoPt;
import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public class PCRoute {
	/**
	 * Unique key that identifies the route.
	 */
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;
	
	@Persistent
	private ArrayList<GeoPt> route;
	
	@Persistent
	private Date date;
	
//	@Persistent
//	private PCSmartphone smartphone;

	public PCRoute() {
		super();
	}

	public PCRoute(ArrayList<GeoPt> route, Date date) {
		super();
		this.route = route;
		this.date = date;
//		this.smartphone = smartphone;
	}

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
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

//	public PCSmartphone getSmartphone() {
//		return smartphone;
//	}
//
//	public void setSmartphone(PCSmartphone smartphone) {
//		this.smartphone = smartphone;
//	}
}
