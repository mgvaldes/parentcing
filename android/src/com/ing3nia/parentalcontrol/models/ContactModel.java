package com.ing3nia.parentalcontrol.models;

import java.io.Serializable;
import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

public class ContactModel implements Serializable, Parcelable {
	private static final long serialVersionUID = 1L;

	private String id;
	
	private String fname;
	
	private String lname;
	
	private ArrayList<PhoneModel> num;
	
	private ArrayList<String> emails;
	
	private ArrayList<AddressModel> addresses;
	
	private ArrayList<OrganizationModel> organizations;

	public ContactModel() {
	}

	public ContactModel(String keyId, String firstName, String lastName,
			ArrayList<PhoneModel> phones, ArrayList<String> emails,
			ArrayList<AddressModel> addresses,
			ArrayList<OrganizationModel> organizations) {
		super();
		this.id = keyId;
		this.fname = firstName;
		this.lname = lastName;
		this.num = phones;
		this.emails = emails;
		this.addresses = addresses;
		this.organizations = organizations;
	}
	
	public ContactModel(String firstName, String lastName,
			ArrayList<PhoneModel> phones) {
		super();
		this.fname = firstName;
		this.lname = lastName;
		this.num = phones;
	}
	
	public ContactModel(String key, String firstName, String lastName,
			ArrayList<PhoneModel> phones) {
		super();
		this.id = key;
		this.fname = firstName;
		this.lname = lastName;
		this.num = phones;
	}
	
	public ContactModel(String firstName, String lastName,
			ArrayList<PhoneModel> phones, ArrayList<String> emails,
			ArrayList<AddressModel> addresses,
			ArrayList<OrganizationModel> organizations) {
		super();
		this.fname = firstName;
		this.lname = lastName;
		this.num = phones;
		this.emails = emails;
		this.addresses = addresses;
		this.organizations = organizations;
	}
	
	public ContactModel(Parcel in) {		
		this.id = new String();
		this.fname = new String();
		this.lname = new String();
		this.num = new ArrayList<PhoneModel>();
		this.emails = new ArrayList<String>();
		this.addresses = new ArrayList<AddressModel>();
		this.organizations = new ArrayList<OrganizationModel>();
		
		readFromParcel(in);
	}

	public static final Parcelable.Creator<ContactModel> CREATOR = new Parcelable.Creator<ContactModel>() {
		public ContactModel createFromParcel(Parcel in) {
			return new ContactModel(in);
		}

		public ContactModel[] newArray(int size) {
			return new ContactModel[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.id);
		dest.writeString(this.fname);
		dest.writeString(this.lname);
		dest.writeTypedList(this.num);
		dest.writeStringList(this.emails);
		dest.writeTypedList(this.addresses);
		dest.writeTypedList(this.organizations);
	}

	public void readFromParcel(Parcel in) {
		this.id = in.readString();
		this.fname = in.readString();
		this.lname = in.readString();
		in.readTypedList(this.num, PhoneModel.CREATOR);
		in.readStringList(this.emails);
		in.readTypedList(this.addresses, AddressModel.CREATOR);
		in.readTypedList(this.organizations, OrganizationModel.CREATOR);
	}

	public String getFirstName() {
		return fname;
	}

	public void setFirstName(String firstName) {
		this.fname = firstName;
	}

	public String getLastName() {
		return lname;
	}

	public void setLastName(String lastName) {
		this.lname = lastName;
	}

	public ArrayList<PhoneModel> getPhones() {
		return num;
	}

	public void setPhones(ArrayList<PhoneModel> phones) {
		this.num = phones;
	}

	public ArrayList<String> getEmails() {
		return emails;
	}

	public void setEmails(ArrayList<String> emails) {
		this.emails = emails;
	}

	public ArrayList<AddressModel> getAddresses() {
		return addresses;
	}

	public void setAddresses(ArrayList<AddressModel> addresses) {
		this.addresses = addresses;
	}

	public ArrayList<OrganizationModel> getOrganizations() {
		return organizations;
	}

	public void setOrganizations(ArrayList<OrganizationModel> organizations) {
		this.organizations = organizations;
	}
	
	public String getKeyId() {
		return id;
	}

	public void setKeyId(String keyId) {
		this.id = keyId;
	}
}