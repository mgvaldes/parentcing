package com.ing3nia.parentalcontrol.client.views.subviews;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.ing3nia.parentalcontrol.client.handlers.BaseViewHandler;
import com.ing3nia.parentalcontrol.client.models.EmergencyNumberModel;
import com.ing3nia.parentalcontrol.client.models.SmartphoneModel;
import com.ing3nia.parentalcontrol.client.rpc.AddEmergencyContactService;
import com.ing3nia.parentalcontrol.client.rpc.AddEmergencyContactServiceAsync;
import com.ing3nia.parentalcontrol.client.views.async.AddEmergencyContactCallBackHandler;

public class AddEmergencyContactView {

	public static String VIEW_CONTENT_CLASSNAME = "addEmergencyContactContent";
	
	
	/**
	 * Center Panel containing all the widgets of the new ticket details view.
	 */
	private HTMLPanel centerContent;

	/**
	 * Main panel of new rule view that groups all the widgets together.
	 */
	private HTMLPanel newEmergencyContactPanel;
	private BaseViewHandler baseViewHandler;
	
	private Label countryLabel;
	private ListBox countryListBox;
	
	private Label phoneNumLabel;
	private TextBox phoneNum;
	
	private Label descriptionLabel;
	private TextBox descriptionText;
	
	private Button saveEmergencyContact;

	private SmartphoneModel smartphone;
	
	private HTMLPanel addEmergencyContactPanel;

	
	public AddEmergencyContactView(BaseViewHandler baseViewHandler, SmartphoneModel smartphone){
		this.baseViewHandler = baseViewHandler;
		this.smartphone = smartphone;
		this.centerContent = baseViewHandler.getBaseBinder().getCenterContent();
	}
	
	public void initAddEmergencyContactView(){
		
		this.centerContent.clear();
		
		this.phoneNumLabel = new Label("Phone Number");
		this.phoneNum = new TextBox();
		
		this.descriptionLabel = new Label("Description");
		this.descriptionText = new TextBox();
		
		this.countryLabel = new Label("Country");
		this.countryListBox = new ListBox();
		fillWithCountries(this.countryListBox);
		
		this.saveEmergencyContact = new Button("Save Contact");
		
		AddEmergencyClickHandler emergencyClickHandler = new AddEmergencyClickHandler();
		this.saveEmergencyContact.addClickHandler(emergencyClickHandler);
		
		this.addEmergencyContactPanel = new HTMLPanel("");
		this.addEmergencyContactPanel.setStyleName(VIEW_CONTENT_CLASSNAME);
		this.addEmergencyContactPanel.add(this.countryLabel);
		this.addEmergencyContactPanel.add(this.countryListBox);
		this.addEmergencyContactPanel.add(this.phoneNumLabel);
		this.addEmergencyContactPanel.add(this.phoneNum);
		this.addEmergencyContactPanel.add(this.descriptionLabel);
		this.addEmergencyContactPanel.add(this.descriptionText);
		
		this.addEmergencyContactPanel.add(this.saveEmergencyContact);
		
		this.smartphone = smartphone;
		
		this.centerContent.add(this.addEmergencyContactPanel);
	}
	
	public void saveEmergencyNumber(EmergencyNumberModel emergencyContact){
		AddEmergencyContactCallBackHandler addEmergenycCallback = new AddEmergencyContactCallBackHandler(baseViewHandler, baseViewHandler.getUser(), centerContent, emergencyContact, baseViewHandler.getUser().getCid(), smartphone.getKeyId());
		AddEmergencyContactServiceAsync addEmergencyAsync = GWT.create(AddEmergencyContactService.class);
		addEmergencyAsync.addEmergencyContact(emergencyContact, smartphone.getKeyId(), addEmergenycCallback);
	}

	public class AddEmergencyClickHandler implements ClickHandler {

		@Override
		public void onClick(ClickEvent event) {
			EmergencyNumberModel emergencyContact = new EmergencyNumberModel();
			if (phoneNum.getText().length() == 0) {
				baseViewHandler.getBaseBinder().getNotice()
						.setText("Phone Number can not be empty");
				centerContent.add(baseViewHandler.getBaseBinder().getNotice());
				return;
			}

			emergencyContact.setCountry(countryListBox
					.getItemText(countryListBox.getSelectedIndex()));
			emergencyContact.setDescription(descriptionText.getText());
			emergencyContact.setNumber(phoneNum.getText());

			saveEmergencyNumber(emergencyContact);
		}		
	}
	
	public void fillWithCountries(ListBox countryListBox){
		countryListBox.addItem("United States");
		countryListBox.addItem("Venezuela");
	}
	

}