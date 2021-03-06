package com.ing3nia.parentalcontrol.client.models;

import java.io.Serializable;
import java.util.ArrayList;

public class ClientUserModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * String that represents the key of this PCAdmin object.
	 */
	String key;

	/**
	 * Admin user's username.
	 */
	String username;

	/**
	 * Admin user's password.
	 */
	String password;

	/**
	 * Smartphone list associated to a user.
	 */
	ArrayList<SmartphoneModel> smartphones;

	/**
	 * Admin users added by owner user.
	 */
	ArrayList<ClientAdminUserModel> admins;

	/**
	 * User help desk tickets
	 */
	ArrayList<TicketModel> openTickets;
	
	ArrayList<TicketModel> closedTickets;
	

	String cid;

	public ClientUserModel(String key, String username, String password,
			ArrayList<SmartphoneModel> smartphones,
			ArrayList<ClientAdminUserModel> admins,
			ArrayList<TicketModel> openTickets, ArrayList<TicketModel> closedTickets) {
		this.key = key;
		this.username = username;
		this.password = password;
		this.smartphones = smartphones;
		this.admins = admins;
		this.openTickets = openTickets;
		this.closedTickets = closedTickets;
	}

	public ClientUserModel(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public ClientUserModel() {
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public ArrayList<SmartphoneModel> getSmartphones() {
		return smartphones;
	}

	public void setSmartphones(ArrayList<SmartphoneModel> smartphones) {
		this.smartphones = smartphones;
	}

	public ArrayList<ClientAdminUserModel> getAdmins() {
		return admins;
	}

	public void setAdmins(ArrayList<ClientAdminUserModel> admins) {
		this.admins = admins;
	}

	public ArrayList<TicketModel> getOpenTickets() {
		return openTickets;
	}

	public void setOpenTickets(ArrayList<TicketModel> openTickets) {
		this.openTickets = openTickets;
	}

	public ArrayList<TicketModel> getClosedTickets() {
		return closedTickets;
	}

	public void setClosedTickets(ArrayList<TicketModel> closedTickets) {
		this.closedTickets = closedTickets;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public ArrayList<AlertModel> getUserAlertList() {
		ArrayList<AlertModel> alerts = new ArrayList<AlertModel>();

		for (SmartphoneModel smart : this.getSmartphones()) {
			alerts.addAll(SmartphoneModel.getUserAlertList(smart));
		}

		return alerts;
	}
	


	public ArrayList<GeoPtModel> getDeviceLocations(){
		ArrayList<GeoPtModel> deviceLocations = new ArrayList<GeoPtModel>();
		for(SmartphoneModel sph: this.getSmartphones()){
			GeoPtModel geopt = new GeoPtModel();
			geopt.setLatitude(Double.valueOf(sph.getLocation().getLatitude()));
			geopt.setLongitude(Double.valueOf(sph.getLocation().getLongitude()));
			deviceLocations.add(geopt);
		}
		return deviceLocations;
	}
	
}
