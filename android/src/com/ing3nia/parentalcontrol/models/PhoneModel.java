package com.ing3nia.parentalcontrol.models;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

public class PhoneModel implements Serializable, Parcelable {
	private static final long serialVersionUID = 1L;
	
	private int type;
	
	private String phone;

	public PhoneModel() {
	}

	public PhoneModel(int type, String phoneNumber) {
		super();
		this.type = type;
		this.phone = phoneNumber;
	}
	
	public PhoneModel(Parcel in) {		
		this.type = -1;
		this.phone = new String();
		
		readFromParcel(in);
	}

	public static final Parcelable.Creator<PhoneModel> CREATOR = new Parcelable.Creator<PhoneModel>() {
		public PhoneModel createFromParcel(Parcel in) {
			return new PhoneModel(in);
		}

		public PhoneModel[] newArray(int size) {
			return new PhoneModel[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.type);
		dest.writeString(this.phone);		
	}

	public void readFromParcel(Parcel in) {
		this.type = in.readInt();
		this.phone = in.readString();
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getPhoneNumber() {
		return phone;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phone = phoneNumber;
	}
}