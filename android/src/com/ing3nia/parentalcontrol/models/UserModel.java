package com.ing3nia.parentalcontrol.models;

import java.io.Serializable;
import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

public class UserModel implements Serializable, Parcelable {
	private static final long serialVersionUID = 1L;
	
	private ArrayList<SmartphoneModel> smartphones;
	
	public UserModel(ArrayList<SmartphoneModel> smartphones) {
		this.smartphones = smartphones;
	}
	
	public UserModel(Parcel in) {
		this.smartphones = new ArrayList<SmartphoneModel>();
		
		readFromParcel(in);
	}

	public static final Parcelable.Creator<UserModel> CREATOR = new Parcelable.Creator<UserModel>() {
		public UserModel createFromParcel(Parcel in) {
			return new UserModel(in);
		}

		public UserModel[] newArray(int size) {
			return new UserModel[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeList(this.smartphones);
	}

	public void readFromParcel(Parcel in) {
		in.readTypedList(this.smartphones, SmartphoneModel.CREATOR);
	}

	public ArrayList<SmartphoneModel> getSmartphones() {
		return smartphones;
	}

	public void setSmartphones(ArrayList<SmartphoneModel> smartphones) {
		this.smartphones = smartphones;
	}
}
