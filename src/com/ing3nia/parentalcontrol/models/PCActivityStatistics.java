package com.ing3nia.parentalcontrol.models;

import java.util.ArrayList;
import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

/**
 * This class contains statistic information about the activities
 * done by a certain smartphone.s
 * 
 * @author Ing3nia
 * @author Javier Fernandez
 * @author Maria Gabriela Valdes
 * 
 * @version 1.0
 *
 */
@PersistenceCapable
public class PCActivityStatistics {
	/**
	 * Unique key that identifies the statistic.
	 */
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;
	
	@Persistent
	private Date date;
	
	/**
	 * 
	 */
	@Persistent
	private Key smartphone;
	
	/**
	 * 
	 */
	@Persistent
	private Key route;
	
	/**
	 * 
	 */
	@Persistent
	private ArrayList<Key> notifications;
	
	/**
	 * 
	 */
	@Persistent
	private ArrayList<Key> serviceStats;

	public PCActivityStatistics() {
		super();
	}

	public PCActivityStatistics(Key key, Key smartphone,
			Key route, ArrayList<Key> notifications,
			ArrayList<Key> serviceStats) {
		super();
		this.key = key;
		this.smartphone = smartphone;
		this.route = route;
		this.notifications = notifications;
		this.serviceStats = serviceStats;
	}

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Key getSmartphone() {
		return smartphone;
	}

	public void setSmartphone(Key smartphone) {
		this.smartphone = smartphone;
	}

	public Key getRoute() {
		return route;
	}

	public void setRoute(Key route) {
		this.route = route;
	}

	public ArrayList<Key> getNotifications() {
		return notifications;
	}

	public void setNotifications(ArrayList<Key> notifications) {
		this.notifications = notifications;
	}

	public ArrayList<Key> getServiceStats() {
		return serviceStats;
	}

	public void setServiceStats(ArrayList<Key> serviceStats) {
		this.serviceStats = serviceStats;
	}
}
