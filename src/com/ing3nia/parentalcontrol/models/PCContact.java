package com.ing3nia.parentalcontrol.models;

import java.util.ArrayList;
import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

/**
 * This class contains information about a phone contact.  
 * 
 * @author Ing3nia
 * @author Javier Fernandez
 * @author Maria Gabriela Valdes
 * 
 * @version 1.0
 *
 */
@PersistenceCapable
public class PCContact {
	/**
	 * Unique key that identifies the contact.
	 */
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;
	
	/**
	 * Represents the first name of a contact.  
	 */
	@Persistent
	private String firstName;
	
	/**
	 * Represents the last name of a contact.  
	 */
	@Persistent
	private String lastName;
	
	/**
	 * Represents a collection of numbers of a specific contact. 
	 */
	@Persistent
	private ArrayList<PCPhone> phones;
	
	/**
	 * Represents a collection of emails of a specific contact.
	 */
	@Persistent
	private ArrayList<String> emails;
	
	/**
	 * Represents a collection of addresses of a specific contact.
	 */
	@Persistent
	private ArrayList<PCAddress> addresses;
	
	/**
	 * Represents a collection of organizations of a specific contact.
	 */
	@Persistent
	private ArrayList<PCOrganization> organizations;
	
	/**
	 * Represents the nickname of a contact.  
	 */
	@Persistent
	private String nickname;
	
	/**
	 * Represents the url of and image associated to a contact.  
	 */
	@Persistent
	private String imageUrl;
	
	/**
	 * Represents the pin number of a contact.  
	 */
	@Persistent
	private String pin;
	
	/**
	 * Represents the google talk account of a contact.  
	 */
	@Persistent
	private String googleTalk;
	
	/**
	 * Represents bithday the of a contact.  
	 */
	@Persistent
	private Date birthday;
	
	/**
	 * Represents aniversary the of a contact.  
	 */
	@Persistent
	private Date aniversary;
	
	/**
	 * Represents some notes about a contact.  
	 */
	@Persistent
	private String notes;
	
	/**
	 * 
	 */
	@Persistent
    private ArrayList<PCIM> im;
	
	/**
	 * 
	 */
	@Persistent
	private ArrayList<String> websites;
	
	public PCContact() {
		super();
	}

	public PCContact(Key key, String firstName, String lastName,
			ArrayList<PCPhone> phones, ArrayList<String> emails,
			ArrayList<PCAddress> addresses,
			ArrayList<PCOrganization> organizations, String nickname,
			String imageUrl, String pin, String googleTalk, Date birthday,
			Date aniversary, String notes, ArrayList<PCIM> im,
			ArrayList<String> websites) {
		super();
		this.key = key;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phones = phones;
		this.emails = emails;
		this.addresses = addresses;
		this.organizations = organizations;
		this.nickname = nickname;
		this.imageUrl = imageUrl;
		this.pin = pin;
		this.googleTalk = googleTalk;
		this.birthday = birthday;
		this.aniversary = aniversary;
		this.notes = notes;
		this.im = im;
		this.websites = websites;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}

	public String getGoogleTalk() {
		return googleTalk;
	}

	public void setGoogleTalk(String googleTalk) {
		this.googleTalk = googleTalk;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public Date getAniversary() {
		return aniversary;
	}

	public void setAniversary(Date aniversary) {
		this.aniversary = aniversary;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public ArrayList<PCIM> getIm() {
		return im;
	}

	public void setIm(ArrayList<PCIM> im) {
		this.im = im;
	}

	public ArrayList<String> getWebsites() {
		return websites;
	}

	public void setWebsites(ArrayList<String> websites) {
		this.websites = websites;
	}

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public ArrayList<PCPhone> getPhones() {
		return phones;
	}

	public void setPhones(ArrayList<PCPhone> phones) {
		this.phones = phones;
	}

	public ArrayList<String> getEmails() {
		return emails;
	}

	public void setEmails(ArrayList<String> emails) {
		this.emails = emails;
	}

	public ArrayList<PCAddress> getAddresses() {
		return addresses;
	}

	public void setAddresses(ArrayList<PCAddress> addresses) {
		this.addresses = addresses;
	}

	public ArrayList<PCOrganization> getOrganizations() {
		return organizations;
	}

	public void setOrganizations(ArrayList<PCOrganization> organizations) {
		this.organizations = organizations;
	}
}
