package com.ing3nia.parentalcontrol.models;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

public class DeviceModel implements Serializable, Parcelable {
	private static final long serialVersionUID = 1L;
	
	private String model;

	private String version;
	
	private int type;

	public DeviceModel() {
	}

	public DeviceModel(String model, String version, int type) {
		super();
		this.model = model;
		this.version = version;
		this.type = type;
	}
	
	public DeviceModel(Parcel in) {		
		this.model = new String();
		this.version = new String();
		this.type = -1;
		
		readFromParcel(in);
	}

	public static final Parcelable.Creator<DeviceModel> CREATOR = new Parcelable.Creator<DeviceModel>() {
		public DeviceModel createFromParcel(Parcel in) {
			return new DeviceModel(in);
		}

		public DeviceModel[] newArray(int size) {
			return new DeviceModel[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.model);
		dest.writeString(this.version);
		dest.writeInt(this.type);
	}

	public void readFromParcel(Parcel in) {
		this.model = in.readString();
		this.version = in.readString();
		this.type = in.readInt();
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
}
