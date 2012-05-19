package com.ing3nia.parentalcontrol.models;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.ing3nia.parentalcontrol.models.utils.PCNotificationTypeId;

import android.os.Parcel;
import android.os.Parcelable;

public class SmartphoneModel implements Serializable, Parcelable {
	private static final long serialVersionUID = 1L;

	private String keyId;
	
	private String id;
	
	private LocationModel location;
	
	private ArrayList<ContactModel> active_cts;
	
	private String name;
	
	private DeviceModel device;
	
	private String serialNumber;
	
	private String appVersion;
	
	private ArrayList<ContactModel> inactive_cts;
	
	private ArrayList<EmergencyNumberModel> addedEmergencyNumbers;
	
	private ArrayList<EmergencyNumberModel> deletedEmergencyNumbers;
	
	private ArrayList<RouteModel> routes;
	
	private ArrayList<PropertyModel> properties;
	
	private ModificationModel modification;
	
	private ArrayList<RuleModel> rules;
	
	private ArrayList<NotificationModel> alerts;
	
	public SmartphoneModel() {
	}

	public SmartphoneModel(String id, LocationModel location,
			ArrayList<ContactModel> activeContacts, String name,
			DeviceModel device, String serialNumber, String appVersion,
			ArrayList<ContactModel> inactiveContacts,
			ArrayList<EmergencyNumberModel> addedEmergencyNumbers,
			ArrayList<EmergencyNumberModel> deletedEmergencyNumbers,
			ArrayList<RouteModel> routes, ArrayList<PropertyModel> properties,
			ModificationModel modification, ArrayList<RuleModel> rules, ArrayList<NotificationModel> alerts) {
		super();
		this.id = id;
		this.location = location;
		this.active_cts = activeContacts;
		this.name = name;
		this.device = device;
		this.serialNumber = serialNumber;
		this.appVersion = appVersion;
		this.inactive_cts = inactiveContacts;
		this.addedEmergencyNumbers = addedEmergencyNumbers;
		this.deletedEmergencyNumbers = deletedEmergencyNumbers;
		this.routes = routes;
		this.properties = properties;
		this.modification = modification;
		this.rules = rules;
		this.alerts = alerts;
	}
	
	public SmartphoneModel(Parcel in) {
		this.keyId = new String();
		this.location = new LocationModel();
		this.active_cts = new ArrayList<ContactModel>();
		this.name = new String();
		this.device = new DeviceModel();
		this.serialNumber = new String();
		this.appVersion = new String();
		this.inactive_cts = new ArrayList<ContactModel>();
		this.addedEmergencyNumbers = new ArrayList<EmergencyNumberModel>();
		this.deletedEmergencyNumbers = new ArrayList<EmergencyNumberModel>();
		this.routes = new ArrayList<RouteModel>();
		this.properties = new ArrayList<PropertyModel>();
		this.modification = new ModificationModel();
		this.rules = new ArrayList<RuleModel>();
		this.alerts = new ArrayList<NotificationModel>();
		
		readFromParcel(in);
	}

	public static final Parcelable.Creator<SmartphoneModel> CREATOR = new Parcelable.Creator<SmartphoneModel>() {
		public SmartphoneModel createFromParcel(Parcel in) {
			return new SmartphoneModel(in);
		}

		public SmartphoneModel[] newArray(int size) {
			return new SmartphoneModel[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.keyId);
		dest.writeParcelable(this.location, flags);
		dest.writeTypedList(this.active_cts);
		dest.writeString(this.name);
		dest.writeParcelable(this.device, flags);
		dest.writeString(this.serialNumber);
		dest.writeString(this.appVersion);
		dest.writeTypedList(this.inactive_cts);
		dest.writeTypedList(this.addedEmergencyNumbers);
		dest.writeTypedList(this.deletedEmergencyNumbers);
		dest.writeTypedList(this.routes);
		dest.writeTypedList(this.properties);
		dest.writeParcelable(this.modification, flags);
		dest.writeTypedList(this.rules);
		dest.writeTypedList(this.alerts);
	}

	public void readFromParcel(Parcel in) {
		this.keyId = in.readString();
		this.location = in.readParcelable(LocationModel.class.getClassLoader());
		in.readTypedList(this.active_cts, ContactModel.CREATOR);
		this.name = in.readString();
		this.device = in.readParcelable(DeviceModel.class.getClassLoader());
		this.serialNumber = in.readString();
		this.appVersion = in.readString();
		in.readTypedList(this.inactive_cts, ContactModel.CREATOR);
		in.readTypedList(this.addedEmergencyNumbers, EmergencyNumberModel.CREATOR);
		in.readTypedList(this.deletedEmergencyNumbers, EmergencyNumberModel.CREATOR);
		in.readTypedList(this.routes, RouteModel.CREATOR);
		in.readTypedList(this.properties, PropertyModel.CREATOR);
		this.modification = in.readParcelable(ModificationModel.class.getClassLoader());
		in.readTypedList(this.rules, RuleModel.CREATOR);
		in.readTypedList(this.alerts, NotificationModel.CREATOR);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public LocationModel getLocation() {
		return location;
	}

	public void setLocation(LocationModel location) {
		this.location = location;
	}

	public ArrayList<ContactModel> getActiveContacts() {
		return (ArrayList<ContactModel>)active_cts;
	}

	public void setActiveContacts(ArrayList<ContactModel> activeContacts) {
		this.active_cts = activeContacts;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public DeviceModel getDevice() {
		return device;
	}

	public void setDevice(DeviceModel device) {
		this.device = device;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public ArrayList<ContactModel> getInactiveContacts() {
		return (ArrayList<ContactModel>)inactive_cts;
	}

	public void setInactiveContacts(ArrayList<ContactModel> inactiveContacts) {
		this.inactive_cts = inactiveContacts;
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

	public void setDeletedEmergencyNumbers(ArrayList<EmergencyNumberModel> deletedEmergencyNumbers) {
		this.deletedEmergencyNumbers = deletedEmergencyNumbers;
	}

	public ArrayList<RouteModel> getRoutes() {
		return (ArrayList<RouteModel>)routes;
	}

	public void setRoutes(ArrayList<RouteModel> routes) {
		this.routes = routes;
	}

	public ArrayList<PropertyModel> getProperties() {
		return (ArrayList<PropertyModel>)properties;
	}

	public void setProperties(ArrayList<PropertyModel> properties) {
		this.properties = properties;
	}

	public ModificationModel getModification() {
		return modification;
	}

	public void setModification(ModificationModel modification) {
		this.modification = modification;
	}

	public ArrayList<RuleModel> getRules() {
		return (ArrayList<RuleModel>)rules;
	}

	public void setRules(ArrayList<RuleModel> rules) {
		this.rules = rules;
	}

	public String getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	public String getKeyId() {
		return keyId;
	}

	public void setKeyId(String keyId) {
		this.keyId = keyId;
	}

	public ArrayList<NotificationModel> getAlerts() {
		return (ArrayList<NotificationModel>)alerts;
	}

	public void setAlerts(ArrayList<NotificationModel> alerts) {
		this.alerts = alerts;
	}
	
	
	public static ArrayList<AlertModel> getUserAlertList(SmartphoneModel smart) throws ParseException {
		ArrayList<AlertModel> alerts = new ArrayList<AlertModel>();
		AlertModel auxAlert;
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy kk:mm:ss a");

		for (NotificationModel not : smart.getAlerts()) {
			auxAlert = new AlertModel(formatter.parse(not.getDate()), smart.getName(), PCNotificationTypeId.getNotificationMessageFromType(not.getType()));
			alerts.add(auxAlert);
		}

		return alerts;
	}
}
