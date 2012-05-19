package com.ing3nia.parentalcontrol.models;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

public class PropertyModel implements Serializable, Parcelable {
	private static final long serialVersionUID = 1L;
	
	private String keyId;
	
	private String description;
	
	private String value;
	
	private int id;
	
	private String creationDate;

	public PropertyModel() {
		super();
	}

	public PropertyModel(String keyId, String description, String value, int id, String creationDate) {
		super();
		this.keyId = keyId;
		this.description = description;
		this.value = value;
		this.id = id;
		this.creationDate = creationDate;
	}
	
	public PropertyModel(String description, String value, int id, String creationDate) {
		super();
		this.description = description;
		this.value = value;
		this.id = id;
		this.creationDate = creationDate;
	}
	
	public PropertyModel(Parcel in) {
		this.keyId = new String();
		this.description = new String();
		this.value = new String();
		this.id = -1;
		this.creationDate = new String();
		
		readFromParcel(in);
	}

	public static final Parcelable.Creator<PropertyModel> CREATOR = new Parcelable.Creator<PropertyModel>() {
		public PropertyModel createFromParcel(Parcel in) {
			return new PropertyModel(in);
		}

		public PropertyModel[] newArray(int size) {
			return new PropertyModel[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.keyId);
		dest.writeString(this.description);
		dest.writeString(this.value);
		dest.writeInt(this.id);
		dest.writeString(this.creationDate);
	}

	public void readFromParcel(Parcel in) {
		this.keyId = in.readString();
		this.description = in.readString();
		this.value = in.readString();
		this.id = in.readInt();
		this.creationDate = in.readString();
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}
	
	
	public String getKeyId() {
		return keyId;
	}

	public void setKeyId(String keyId) {
		this.keyId = keyId;
	}
}