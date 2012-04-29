package com.ing3nia.parentalcontrol.models;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

public class AddressModel implements Serializable, Parcelable {
	private static final long serialVersionUID = 1L;
	
	private int type;
	
	private String street;
	
	private String city;
	
	private String state;
	
	private String zipCode;
	
	private String country;

	public AddressModel(int type, String street, String city, String state,
			String zipCode, String country) {
		super();
		this.type = type;
		this.street = street;
		this.city = city;
		this.state = state;
		this.zipCode = zipCode;
		this.country = country;
	}
	
	public AddressModel(Parcel in) {		
		this.type = -1;
		this.street = new String();
		this.city = new String();
		this.state = new String();
		this.zipCode = new String();
		this.country = new String();
		
		readFromParcel(in);
	}

	public static final Parcelable.Creator<AddressModel> CREATOR = new Parcelable.Creator<AddressModel>() {
		public AddressModel createFromParcel(Parcel in) {
			return new AddressModel(in);
		}

		public AddressModel[] newArray(int size) {
			return new AddressModel[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.type);
		dest.writeString(this.street);
		dest.writeString(this.city);
		dest.writeString(this.state);
		dest.writeString(this.zipCode);
		dest.writeString(this.country);
	}

	public void readFromParcel(Parcel in) {
		this.type = in.readInt();
		this.street = in.readString();
		this.city = in.readString();
		this.state = in.readString();
		this.zipCode = in.readString();
		this.country = in.readString();
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
}