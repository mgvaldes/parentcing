package com.ing3nia.parentalcontrol.models;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

public class NotificationModel implements Serializable, Parcelable {
	private static final long serialVersionUID = 1L;
	
	private int type;
	
	private String date;

	public NotificationModel() {
		super();
	}

	public NotificationModel(int type, String date) {
		super();
		this.type = type;
		this.date = date;
	}
	
	public NotificationModel(Parcel in) {
		this.type = -1;
		this.date = new String();
		
		readFromParcel(in);
	}

	public static final Parcelable.Creator<NotificationModel> CREATOR = new Parcelable.Creator<NotificationModel>() {
		public NotificationModel createFromParcel(Parcel in) {
			return new NotificationModel(in);
		}

		public NotificationModel[] newArray(int size) {
			return new NotificationModel[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.type);
		dest.writeString(this.date);
	}

	public void readFromParcel(Parcel in) {
		this.type = in.readInt();
		this.date = in.readString();
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}	
}
