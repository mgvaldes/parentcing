package com.ing3nia.parentalcontrol.models;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

public class LocationModel implements Serializable, Parcelable {
	private static final long serialVersionUID = 1L;

	private String latitude;
	
	private String longitude;

	public LocationModel() {
	}

	public LocationModel(String latitude, String longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	public LocationModel(Parcel in) {		
		this.latitude = new String();
		this.longitude = new String();
		
		readFromParcel(in);
	}

	public static final Parcelable.Creator<LocationModel> CREATOR = new Parcelable.Creator<LocationModel>() {
		public LocationModel createFromParcel(Parcel in) {
			return new LocationModel(in);
		}

		public LocationModel[] newArray(int size) {
			return new LocationModel[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.latitude);
		dest.writeString(this.longitude);		
	}

	public void readFromParcel(Parcel in) {
		this.latitude = in.readString();
		this.longitude = in.readString();
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
}