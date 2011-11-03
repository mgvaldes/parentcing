package com.ing3nia.parentalcontrol.models;

import java.util.ArrayList;

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
	
	/**
	 * 
	 */
	@Persistent
	private PCSmartphone smartphone;
	
	/**
	 * 
	 */
	@Persistent
	private PCRoute route;
	
	/**
	 * 
	 */
	@Persistent
	private ArrayList<PCNotification> notifications;
	
	/**
	 * 
	 */
	@Persistent
	private ArrayList<PCServiceStatistics> serviceStats;

	public PCActivityStatistics() {
		super();
	}

	public PCActivityStatistics(Key key, PCSmartphone smartphone,
			PCRoute route, ArrayList<PCNotification> notifications,
			ArrayList<PCServiceStatistics> serviceStats) {
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

	public PCSmartphone getSmartphone() {
		return smartphone;
	}

	public void setSmartphone(PCSmartphone smartphone) {
		this.smartphone = smartphone;
	}

	public PCRoute getRoute() {
		return route;
	}

	public void setRoute(PCRoute route) {
		this.route = route;
	}

	public ArrayList<PCNotification> getNotifications() {
		return notifications;
	}

	public void setNotifications(ArrayList<PCNotification> notifications) {
		this.notifications = notifications;
	}

	public ArrayList<PCServiceStatistics> getServiceStats() {
		return serviceStats;
	}

	public void setServiceStats(ArrayList<PCServiceStatistics> serviceStats) {
		this.serviceStats = serviceStats;
	}
}
