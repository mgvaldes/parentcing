package com.ing3nia.parentalcontrol.models;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

public class TicketAnswerModel implements Serializable, Parcelable  {
	private static final long serialVersionUID = 1L;
	
	private String key;
	
	private String date;
	
	private String userKey;
	
	private String username;
	
	private String answer;

	public TicketAnswerModel() {
		
	}
	
	public TicketAnswerModel(String date, String userKey, String answer, String username) {
		super();
		this.date = date;
		this.userKey = userKey;
		this.answer = answer;
		this.username = username;
	}
	
	public TicketAnswerModel(Parcel in) {
		this.key = new String();
		this.date = new String();
		this.userKey = new String();
		this.username = new String();
		this.answer = new String();		
		
		readFromParcel(in);
	}
	
	public static final Parcelable.Creator<TicketAnswerModel> CREATOR = new Parcelable.Creator<TicketAnswerModel>() {
		public TicketAnswerModel createFromParcel(Parcel in) {
			return new TicketAnswerModel(in);
		}

		public TicketAnswerModel[] newArray(int size) {
			return new TicketAnswerModel[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.key);
		dest.writeString(this.date);
		dest.writeString(this.userKey);
		dest.writeString(this.username);		
		dest.writeString(this.answer);
	}

	public void readFromParcel(Parcel in) {
		this.key = in.readString();
		this.date = in.readString();
		this.userKey = in.readString();
		this.username = in.readString();		
		this.answer = in.readString();
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getUserKey() {
		return userKey;
	}

	public void setUserKey(String userKey) {
		this.userKey = userKey;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
