package com.ing3nia.parentalcontrol.client.views;

import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.ing3nia.parentalcontrol.client.models.FullContactModel;

public class DeviceContactDetailsView {
	/**
	 * Center Panel containing all the widgets of the 
	 * device contact details view.
	 */
	private HTMLPanel centerContent;
	
	/**
	 * Main panel of device contact details view that groups all 
	 * the widgets together.
	 */
	private HTMLPanel viewContent;
	
	/**
	 * Contact details label.
	 */
	private Label contactDetailsLabel;
	
	/**
	 * Table where the contact info is displayed.
	 */
	private FlexTable contactTable;
	
	/**
	 * Disallow button.
	 */
	private Button disallowButton;
	
	/**
	 * Allow button.
	 */
	private Button allowButton;
	
	/**
	 * Panel that groups all the contact information together.
	 */
	private HTMLPanel contactInfoPanel;
	
	/**
	 * Contact label.
	 */
	private Label contactLabel;
	
	/**
	 * Contact's first name label.
	 */
	private Label firstNameLabel;
	
	/**
	 * Contact's first name.
	 */
	private Label firstName;
	
	/**
	 * Contact's last name label.
	 */
	private Label lastNameLabel;
	
	/**
	 * Contact's last name.
	 */
	private Label lastName;
	
	/**
	 * Contact's email label.
	 */
	private Label emailLabel;
	
	/**
	 * Contact's emails.
	 */
	private FlowPanel emailsPanel;
	
	/**
	 * List of contact's emails.
	 */
	private List<String> emails;
	
	/**
	 * Panel that groups all the contact organization information together.
	 */
	private HTMLPanel oranizationPanel;
	
	/**
	 * Organization label.
	 */
	private Label organizationLabel;
	
	/**
	 * Contact's organization name label.
	 */
	private Label organizationNameLabel;
	
	/**
	 * Contact's organization name.
	 */
	private Label organizationName;
	
	/**
	 * Contact's organization title label.
	 */
	private Label titleLabel;
	
	/**
	 * Contact's organization title name.
	 */
	private Label titleName;
	
	/**
	 * Panel that groups all the contact phone information together.
	 */
	private HTMLPanel phonePanel;
	
	/**
	 * Phone label.
	 */
	private Label phoneLabel;
	
	/**
	 * Contact's phone type label.
	 */
	private Label phoneTypeLabel;
	
	/**
	 * Contact's phone type.
	 */
	private Label phoneType;
	
	/**
	 * Contact's phone number label.
	 */
	private Label phoneNumberLabel;
	
	/**
	 * Contact's phone number.
	 */
	private Label phoneNumber;
	
	/**
	 * Panel that groups all the contact address information together.
	 */
	private HTMLPanel addressPanel;
	
	/**
	 * Address label.
	 */
	private Label addressLabel;
	
	/**
	 * Contact's street address label.
	 */
	private Label streetLabel;
	
	/**
	 * Contact's street address.
	 */
	private Label street;
	
	/**
	 * Contact's city address label.
	 */
	private Label cityLabel;
	
	/**
	 * Contact's city address.
	 */
	private Label city;
	
	/**
	 * Contact's state address label.
	 */
	private Label stateLabel;
	
	/**
	 * Contact's state address.
	 */
	private Label state;
	
	/**
	 * Contact's zip code address label.
	 */
	private Label zipCodeLabel;
	
	/**
	 * Contact's zip code address.
	 */
	private Label zipCode;
	
	/**
	 * Contact's country address label.
	 */
	private Label countryLabel;
	
	/**
	 * Contact's country address.
	 */
	private Label country;
	
	public DeviceContactDetailsView(HTMLPanel centerPanel, FullContactModel contact) {
		this.centerContent = centerPanel;
		this.centerContent.setStyleName("centerContent");
		viewContent = new HTMLPanel("");
		contactDetailsLabel = new Label("Contact Details:");
		contactTable = new FlexTable();
		disallowButton = new Button("Disallow");
		allowButton = new Button("Allow");
		contactInfoPanel = new HTMLPanel("");
		contactLabel = new Label("Contact:");
		firstNameLabel = new Label("First Name:");
		firstName = new Label(contact.getFirstName());
		lastNameLabel = new Label("Last Name:");
		lastName = new Label(contact.getLastName());
		emailLabel = new Label("Emails:");
		emailsPanel = new FlowPanel();		
		emails = contact.getEmails();
		oranizationPanel = new HTMLPanel("");
		organizationLabel = new Label("Organization:");
		organizationNameLabel = new Label("Name:");
		organizationName = new Label(contact.getOrganizationName());
		titleLabel = new Label("Title:");
		titleName = new Label(contact.getOrganizationTitle());
		phonePanel = new HTMLPanel("");
		phoneLabel = new Label("Phone:");
		phoneTypeLabel = new Label("Phone Type:");
		phoneType = new Label(contact.getPhoneType());
		phoneNumberLabel = new Label("Phone Number:");
		phoneNumber = new Label(contact.getPhoneNumber());
		addressPanel = new HTMLPanel("");
		addressLabel = new Label("Address:");
		streetLabel = new Label("Street:");
		street = new Label(contact.getStreet());
		cityLabel = new Label("City:");
		city = new Label(contact.getCity());
		stateLabel = new Label("State:");
		state = new Label(contact.getState());
		zipCodeLabel = new Label("Zip Code:");
		zipCode  = new Label(contact.getZipCode());
		countryLabel = new Label("Country:");
		country = new Label(contact.getCountry());
	}
	
	public void initDeviceContactDetailsView(FullContactModel contact) {
		viewContent.add(contactDetailsLabel);
		
		// Loading contact table
		contactTable.setText(0, 0, "First Name");
		contactTable.setText(0, 1, "Last Name");
		contactTable.setText(0, 2, "Phone");
		contactTable.setText(0, 3, "Allow");
		contactTable.setText(0, 4, "Access");
		contactTable.setText(1, 0, contact.getFirstName());
		contactTable.setText(1, 1, contact.getLastName());
		contactTable.setText(1, 2, contact.getPhoneNumber());
		
		disallowButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				
			}
		});
		
		contactTable.setWidget(1, 3, disallowButton);
		
		allowButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				
			}
		});
		
		contactTable.setWidget(1, 4, allowButton);
		
		viewContent.add(contactTable);
		
		// Loading contact information
		contactInfoPanel.add(contactLabel);		
		FlowPanel firstNamePanel = new FlowPanel();		
		firstNamePanel.add(firstNameLabel);
		firstNamePanel.add(firstName);
		contactInfoPanel.add(firstNamePanel);
		
		FlowPanel lastNamePanel = new FlowPanel();		
		lastNamePanel.add(lastNameLabel);
		lastNamePanel.add(lastName);
		contactInfoPanel.add(lastNamePanel);
		
		FlowPanel emailsPanel2 = new FlowPanel();		
		emailsPanel2.add(emailLabel);
		
		for (String email : emails) {
			emailsPanel.add(new Label(email));
		}
		
		emailsPanel2.add(emailsPanel);
		contactInfoPanel.add(emailsPanel2);
		
		viewContent.add(contactInfoPanel);
		
		oranizationPanel.add(organizationLabel);
		FlowPanel organizationNamePanel = new FlowPanel();
		organizationNamePanel.add(organizationNameLabel);
		organizationNamePanel.add(organizationName);
		oranizationPanel.add(organizationNamePanel);
		
		FlowPanel organizationTitlePanel = new FlowPanel();
		organizationTitlePanel.add(titleLabel);
		organizationTitlePanel.add(titleName);
		oranizationPanel.add(organizationTitlePanel);
		
		viewContent.add(oranizationPanel);
		
		phonePanel.add(phoneLabel);
		FlowPanel phoneTypePanel = new FlowPanel();
		phoneTypePanel.add(phoneTypeLabel);
		phoneTypePanel.add(phoneType);
		phonePanel.add(phoneTypePanel);
		
		FlowPanel phoneNumberPanel = new FlowPanel();
		phoneNumberPanel.add(phoneNumberLabel);
		phoneNumberPanel.add(phoneNumber);
		phonePanel.add(phoneNumberPanel);
		
		viewContent.add(phonePanel);
		
		addressPanel.add(addressLabel);
		FlowPanel streetPanel = new FlowPanel();
		streetPanel.add(streetLabel);
		streetPanel.add(street);
		addressPanel.add(streetPanel);
		
		FlowPanel cityPanel = new FlowPanel();
		cityPanel.add(cityLabel);
		cityPanel.add(city);
		addressPanel.add(cityPanel);
		
		FlowPanel statePanel = new FlowPanel();
		statePanel.add(stateLabel);
		statePanel.add(state);
		addressPanel.add(statePanel);
		
		FlowPanel zipCodePanel = new FlowPanel();
		zipCodePanel.add(zipCodeLabel);
		zipCodePanel.add(zipCode);
		addressPanel.add(zipCodePanel);
		
		FlowPanel countryPanel = new FlowPanel();
		countryPanel.add(countryLabel);
		countryPanel.add(country);
		addressPanel.add(countryPanel);
		
		viewContent.add(addressPanel);
		
		centerContent.add(viewContent);
	}
}
