package com.ing3nia.parentalcontrol.models;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

public class OrganizationModel implements Serializable, Parcelable {
	private static final long serialVersionUID = 1L;
	
	private String name;
	
	private String title;

	public OrganizationModel(String name, String title) {
		super();
		this.name = name;
		this.title = title;
	}
	
	public OrganizationModel(Parcel in) {		
		this.name = new String();
		this.title = new String();
		
		readFromParcel(in);
	}

	public static final Parcelable.Creator<OrganizationModel> CREATOR = new Parcelable.Creator<OrganizationModel>() {
		public OrganizationModel createFromParcel(Parcel in) {
			return new OrganizationModel(in);
		}

		public OrganizationModel[] newArray(int size) {
			return new OrganizationModel[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.name);
		dest.writeString(this.title);		
	}

	public void readFromParcel(Parcel in) {
		this.name = in.readString();
		this.title = in.readString();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
