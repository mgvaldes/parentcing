package com.ing3nia.parentalcontrol.models;

import java.io.Serializable;
import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

public class TicketModel implements Serializable, Parcelable {
	private static final long serialVersionUID = 1L;
	
	private String key;
	
	private String category;
	
	private String subject;
	
	private String date;
	
	private String comment;
	
	private ArrayList<TicketAnswerModel> answers;
	
	private String name;
	
	public TicketModel(){
		
	}
	
	public TicketModel(String category, String subject, String comment) {
		this.category = category;
		this.subject = subject;
		this.comment = comment;
	}
	
	public TicketModel(String key, String category, String subject, String date, String comment, ArrayList<TicketAnswerModel> answers) {
		this.key = key;
		this.category = category;
		this.subject = subject;
		this.date = date;
		this.comment = comment;
		this.answers = answers;
	}
	
	public TicketModel(String key, String category, String subject, String date, String comment, ArrayList<TicketAnswerModel> answers, String name) {
		this.key = key;
		this.category = category;
		this.subject = subject;
		this.date = date;
		this.comment = comment;
		this.answers = answers;
		this.name = name;
	}
	
	public TicketModel(Parcel in) {
		this.key = new String();
		this.category = new String();
		this.subject = new String();
		this.date = new String();
		this.comment = new String();
		this.answers = new ArrayList<TicketAnswerModel>();
		this.name = new String();
		
		readFromParcel(in);
	}
	
	public static final Parcelable.Creator<TicketModel> CREATOR = new Parcelable.Creator<TicketModel>() {
		public TicketModel createFromParcel(Parcel in) {
			return new TicketModel(in);
		}

		public TicketModel[] newArray(int size) {
			return new TicketModel[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.key);
		dest.writeString(this.category);
		dest.writeString(this.subject);
		dest.writeString(this.date);
		dest.writeString(this.comment);
		dest.writeTypedList(this.answers);
		dest.writeString(this.name);
	}

	public void readFromParcel(Parcel in) {
		this.key = in.readString();
		this.category = in.readString();
		this.subject = in.readString();
		this.date = in.readString();
		this.comment = in.readString();
		in.readTypedList(this.answers, TicketAnswerModel.CREATOR);
		this.name = in.readString();
	}
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public ArrayList<TicketAnswerModel> getAnswers() {
		return answers;
	}

	public void setAnswers(ArrayList<TicketAnswerModel> answers) {
		this.answers = answers;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
