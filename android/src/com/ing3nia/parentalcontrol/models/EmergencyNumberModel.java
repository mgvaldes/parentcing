package com.ing3nia.parentalcontrol.models;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

public class EmergencyNumberModel implements Serializable, Parcelable {
	private static final long serialVersionUID = 1L;
	
	private String keyId;
	
	private String country;
	
	private String number;
	
	private String description;

	public EmergencyNumberModel() {
		super();
	}

	public EmergencyNumberModel(String keyId, String country, String number,
			String description) {
		super();
		this.keyId = keyId;
		this.country = country;
		this.number = number;
		this.description = description;
	}
	
	public EmergencyNumberModel(String country, String number,
			String description) {
		super();
		this.country = country;
		this.number = number;
		this.description = description;
	}
	
	public EmergencyNumberModel(Parcel in) {		
		this.keyId = new String();
		this.country = new String();
		this.number = new String();
		this.description = new String();
		
		readFromParcel(in);
	}

	public static final Parcelable.Creator<EmergencyNumberModel> CREATOR = new Parcelable.Creator<EmergencyNumberModel>() {
		public EmergencyNumberModel createFromParcel(Parcel in) {
			return new EmergencyNumberModel(in);
		}

		public EmergencyNumberModel[] newArray(int size) {
			return new EmergencyNumberModel[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.keyId);
		dest.writeString(this.country);
		dest.writeString(this.number);
		dest.writeString(this.description);
	}

	public void readFromParcel(Parcel in) {
		this.keyId = in.readString();
		this.country = in.readString();
		this.number = in.readString();
		this.description = in.readString();
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getKeyId() {
		return keyId;
	}

	public void setKeyId(String keyId) {
		this.keyId = keyId;
	}
}