package com.ing3nia.parentalcontrol.models;

import java.io.Serializable;
import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

public class RuleModel implements Serializable, Parcelable {
	private static final long serialVersionUID = 1L;
	
	private String keyId;
	
	private String startDate;
	
	private String endDate;
	
	private String name;
	
	private ArrayList<Integer> disabledFunctionalities;
	
	private String creationDate;
	
	private int type;

	public RuleModel() {
		super();
	}

	public RuleModel(String keyId, String startDate, String endDate,
			ArrayList<Integer> disabledFunctionalities, String name, int type) {
		super();
		this.keyId = keyId;
		this.startDate = startDate;
		this.endDate = endDate;
		this.disabledFunctionalities = disabledFunctionalities;
		this.name = name;
		this.type = type;
	}
	
	public RuleModel(String startDate, String endDate,
			ArrayList<Integer> disabledFunctionalities, String name) {
		super();
		this.startDate = startDate;
		this.endDate = endDate;
		this.disabledFunctionalities = disabledFunctionalities;
		this.name = name;
	}
	
	public RuleModel(Parcel in) {
		this.keyId = new String();
		this.startDate = new String();
		this.endDate = new String();
		this.name = new String();
		this.disabledFunctionalities = new ArrayList<Integer>();
		this.creationDate = new String();
		this.type = -1;
		
		readFromParcel(in);
	}

	public static final Parcelable.Creator<RuleModel> CREATOR = new Parcelable.Creator<RuleModel>() {
		public RuleModel createFromParcel(Parcel in) {
			return new RuleModel(in);
		}

		public RuleModel[] newArray(int size) {
			return new RuleModel[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.keyId);
		dest.writeString(this.startDate);
		dest.writeString(this.endDate);
		dest.writeString(this.name);
		dest.writeList(this.disabledFunctionalities);
		dest.writeString(this.creationDate);
		dest.writeInt(this.type);
	}

	public void readFromParcel(Parcel in) {
		this.keyId = in.readString();
		this.startDate = in.readString();
		this.endDate = in.readString();
		this.name = in.readString();
		in.readList(this.disabledFunctionalities, Integer.class.getClassLoader());
		this.creationDate = in.readString();
		this.type = in.readInt();
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public ArrayList<Integer> getDisabledFunctionalities() {
		return disabledFunctionalities;
	}

	public void setDisabledFunctionalities(
			ArrayList<Integer> disabledFunctionalities) {
		this.disabledFunctionalities = disabledFunctionalities;
	}
	
	public String getKeyId() {
		return keyId;
	}

	public void setKeyId(String keyId) {
		this.keyId = keyId;
	}
	

	public String getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
}