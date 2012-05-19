package com.ing3nia.parentalcontrol.models;

import java.io.Serializable;
import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

public class SimpleContactModel implements Serializable, Parcelable {
	private static final long serialVersionUID = 1L;
	
	private String keyId;

	private String firstName;
	
	private String lastName;
	
	private ArrayList<PhoneModel> phones;

	public SimpleContactModel() {
		super();
	}

	public SimpleContactModel(String keyId, String firstName, String lastName,
			ArrayList<PhoneModel> phones) {
		super();
		this.keyId = keyId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phones = phones;
	}
	
	public SimpleContactModel(String firstName, String lastName,
			ArrayList<PhoneModel> phones) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.phones = phones;
	}
	
	public SimpleContactModel(Parcel in) {		
		this.keyId = new String();
		this.firstName = new String();
		this.lastName = new String();
		this.phones = new ArrayList<PhoneModel>();
		
		readFromParcel(in);
	}

	public static final Parcelable.Creator<SimpleContactModel> CREATOR = new Parcelable.Creator<SimpleContactModel>() {
		public SimpleContactModel createFromParcel(Parcel in) {
			return new SimpleContactModel(in);
		}

		public SimpleContactModel[] newArray(int size) {
			return new SimpleContactModel[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.keyId);
		dest.writeString(this.firstName);
		dest.writeString(this.lastName);
		dest.writeList(this.phones);
	}

	public void readFromParcel(Parcel in) {
		this.keyId = in.readString();
		this.firstName = in.readString();
		this.lastName = in.readString();
		in.readTypedList(this.phones, PhoneModel.CREATOR);
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public ArrayList<PhoneModel> getPhones() {
		return phones;
	}

	public void setPhones(ArrayList<PhoneModel> phones) {
		this.phones = phones;
	}
	
	public String getKeyId() {
		return keyId;
	}

	public void setKeyId(String keyId) {
		this.keyId = keyId;
	}
}