package com.ing3nia.parentalcontrol.client.models;

import java.io.Serializable;
import java.util.ArrayList;

import com.google.gwt.user.client.rpc.IsSerializable;

public class RouteModel implements IsSerializable, Serializable {
	private static final long serialVersionUID = 1L;
	
	private String keyId;
	
	private ArrayList<LocationModel> points;
	
	private String date;

	public RouteModel() {
		super();
	}

	public RouteModel(String keyId, ArrayList<LocationModel> points, String date) {
		super();
		this.keyId = keyId;
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

	public String getKeyId() {
		return keyId;
	}

	public void setKeyId(String keyId) {
		this.keyId = keyId;
	}

}
