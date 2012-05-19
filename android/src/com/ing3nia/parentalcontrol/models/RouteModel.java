package com.ing3nia.parentalcontrol.models;

import java.io.Serializable;
import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

public class RouteModel implements Serializable, Parcelable {
	private static final long serialVersionUID = 1L;
	
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
	
	public RouteModel(Parcel in) {
		this.points = new ArrayList<LocationModel>();
		this.date = new String();
		
		readFromParcel(in);
	}

	public static final Parcelable.Creator<RouteModel> CREATOR = new Parcelable.Creator<RouteModel>() {
		public RouteModel createFromParcel(Parcel in) {
			return new RouteModel(in);
		}

		public RouteModel[] newArray(int size) {
			return new RouteModel[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeTypedList(this.points);
		dest.writeString(this.date);
	}

	public void readFromParcel(Parcel in) {
		in.readTypedList(this.points, LocationModel.CREATOR);
		this.date = in.readString();
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
}
