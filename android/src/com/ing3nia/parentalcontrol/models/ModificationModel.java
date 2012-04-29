package com.ing3nia.parentalcontrol.models;

import java.io.Serializable;
import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

public class ModificationModel implements Serializable, Parcelable {
	private static final long serialVersionUID = 1L;
	
	private String key;
	
	private String smartphoneName;
	
	private ArrayList<SimpleContactModel> activeContacts;
	
	private ArrayList<SimpleContactModel> inactiveContacts;
	
	private ArrayList<PropertyModel> properties;
	
	private ArrayList<RuleModel> rules;
	
	private ArrayList<EmergencyNumberModel> addedEmergencyNumbers;
	
	private ArrayList<EmergencyNumberModel> deletedEmergencyNumbers;
	
	private ArrayList<String> deletedRules;

	public ModificationModel() {
		super();
	}

	public ModificationModel(String key, String smartphoneName, ArrayList<SimpleContactModel> activeContacts,
			ArrayList<SimpleContactModel> inactiveContacts,
			ArrayList<PropertyModel> properties, ArrayList<RuleModel> rules,
			ArrayList<EmergencyNumberModel> addedEmergencyNumbers,
			ArrayList<EmergencyNumberModel> deletedEmergencyNumbers, ArrayList<String> deletedRules) {
		super();
		this.key = key;
		this.activeContacts = activeContacts;
		this.inactiveContacts = inactiveContacts;
		this.properties = properties;
		this.rules = rules;
		this.addedEmergencyNumbers = addedEmergencyNumbers;
		this.deletedEmergencyNumbers = deletedEmergencyNumbers;
		this.smartphoneName = smartphoneName;
		this.deletedRules = deletedRules;
	}
	
	public ModificationModel(String key, ArrayList<SimpleContactModel> activeContacts,
			ArrayList<SimpleContactModel> inactiveContacts,
			ArrayList<PropertyModel> properties, ArrayList<RuleModel> rules,
			ArrayList<EmergencyNumberModel> addedEmergencyNumbers,
			ArrayList<EmergencyNumberModel> deletedEmergencyNumbers, ArrayList<String> deletedRules) {
		super();
		this.key = key;
		this.activeContacts = activeContacts;
		this.inactiveContacts = inactiveContacts;
		this.properties = properties;
		this.rules = rules;
		this.addedEmergencyNumbers = addedEmergencyNumbers;
		this.deletedEmergencyNumbers = deletedEmergencyNumbers;
		this.deletedRules = deletedRules;
	}
	
	public ModificationModel(Parcel in) {
		this.key = new String();
		this.smartphoneName = new String();
		this.activeContacts = new ArrayList<SimpleContactModel>();
		this.inactiveContacts = new ArrayList<SimpleContactModel>();
		this.properties = new ArrayList<PropertyModel>();
		this.rules = new ArrayList<RuleModel>();
		this.addedEmergencyNumbers = new ArrayList<EmergencyNumberModel>();
		this.deletedEmergencyNumbers = new ArrayList<EmergencyNumberModel>();
		this.deletedRules = new ArrayList<String>();
		
		readFromParcel(in);
	}

	public static final Parcelable.Creator<ModificationModel> CREATOR = new Parcelable.Creator<ModificationModel>() {
		public ModificationModel createFromParcel(Parcel in) {
			return new ModificationModel(in);
		}

		public ModificationModel[] newArray(int size) {
			return new ModificationModel[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.key);
		dest.writeString(this.smartphoneName);
		dest.writeTypedList(this.activeContacts);
		dest.writeTypedList(this.inactiveContacts);
		dest.writeTypedList(this.properties);
		dest.writeTypedList(this.rules);
		dest.writeTypedList(this.addedEmergencyNumbers);
		dest.writeTypedList(this.deletedEmergencyNumbers);
		dest.writeStringList(this.deletedRules);
	}

	public void readFromParcel(Parcel in) {
		this.key = in.readString();
		this.smartphoneName = in.readString();
		in.readTypedList(this.activeContacts, SimpleContactModel.CREATOR);
		in.readTypedList(this.inactiveContacts, SimpleContactModel.CREATOR);
		in.readTypedList(this.properties, PropertyModel.CREATOR);
		in.readTypedList(this.rules, RuleModel.CREATOR);
		in.readTypedList(this.addedEmergencyNumbers, EmergencyNumberModel.CREATOR);
		in.readTypedList(this.deletedEmergencyNumbers, EmergencyNumberModel.CREATOR);
		in.readStringList(this.deletedRules);
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public ArrayList<SimpleContactModel> getActiveContacts() {
		return activeContacts;
	}

	public void setActiveContacts(ArrayList<SimpleContactModel> activeContacts) {
		this.activeContacts = activeContacts;
	}

	public ArrayList<SimpleContactModel> getInactiveContacts() {
		return inactiveContacts;
	}

	public void setInactiveContacts(ArrayList<SimpleContactModel> inactiveContacts) {
		this.inactiveContacts = inactiveContacts;
	}

	public ArrayList<PropertyModel> getProperties() {
		return properties;
	}

	public void setProperties(ArrayList<PropertyModel> properties) {
		this.properties = properties;
	}

	public ArrayList<RuleModel> getRules() {
		return rules;
	}

	public void setRules(ArrayList<RuleModel> rules) {
		this.rules = rules;
	}

	public ArrayList<EmergencyNumberModel> getAddedEmergencyNumbers() {
		return addedEmergencyNumbers;
	}

	public void setAddedEmergencyNumbers(
			ArrayList<EmergencyNumberModel> addedEmergencyNumbers) {
		this.addedEmergencyNumbers = addedEmergencyNumbers;
	}

	public ArrayList<EmergencyNumberModel> getDeletedEmergencyNumbers() {
		return deletedEmergencyNumbers;
	}

	public void setDeletedEmergencyNumbers(
			ArrayList<EmergencyNumberModel> deletedEmergencyNumbers) {
		this.deletedEmergencyNumbers = deletedEmergencyNumbers;
	}
	
	public String getSmartphoneName() {
		return smartphoneName;
	}

	public void setSmartphoneName(String smartphoneName) {
		this.smartphoneName = smartphoneName;
	}

	public ArrayList<String> getDeletedRules() {
		return deletedRules;
	}

	public void setDeletedRules(ArrayList<String> deletedRules) {
		this.deletedRules = deletedRules;
	}
}