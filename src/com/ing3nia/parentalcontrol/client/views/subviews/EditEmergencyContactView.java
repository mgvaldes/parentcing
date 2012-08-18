package com.ing3nia.parentalcontrol.client.views.subviews;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.ing3nia.parentalcontrol.client.handlers.BaseViewHandler;
import com.ing3nia.parentalcontrol.client.models.EmergencyNumberModel;
import com.ing3nia.parentalcontrol.client.models.SmartphoneModel;
import com.ing3nia.parentalcontrol.client.rpc.AddEmergencyContactService;
import com.ing3nia.parentalcontrol.client.rpc.AddEmergencyContactServiceAsync;
import com.ing3nia.parentalcontrol.client.rpc.EditEmergencyContactService;
import com.ing3nia.parentalcontrol.client.rpc.EditEmergencyContactServiceAsync;
import com.ing3nia.parentalcontrol.client.views.async.AddEmergencyContactCallBackHandler;
import com.ing3nia.parentalcontrol.client.views.async.EditEmergencyContactCallBackHandler;

public class EditEmergencyContactView {


	public static String VIEW_CONTENT_CLASSNAME = "editEmergencyContactContent";
	
	
	/**
	 * Center Panel containing all the widgets of the new ticket details view.
	 */
	private HTMLPanel centerContent;

	/**
	 * Main panel of new rule view that groups all the widgets together.
	 */
	private HTMLPanel newEmergencyContactPanel;
	private BaseViewHandler baseViewHandler;
	
	private FlowPanel countryPanel = new FlowPanel();
	private Label countryLabel;
	private ListBox countryListBox;
	
	private FlowPanel phoneNumPanel = new FlowPanel();
	private Label phoneNumLabel;
	private TextBox phoneNum;
	
	private FlowPanel descriptionPanel = new FlowPanel();
	private Label descriptionLabel;
	private TextBox descriptionText;
	
	private Button saveEmergencyContact;

	private EmergencyNumberModel oldEmergencyNum;
	
	private SmartphoneModel smartphone;
	
	private HTMLPanel addEmergencyContactPanel;
	

	
	public EditEmergencyContactView(BaseViewHandler baseViewHandler, SmartphoneModel smartphone, EmergencyNumberModel emergencyModel){
		this.baseViewHandler = baseViewHandler;
		this.smartphone = smartphone;
		this.centerContent = baseViewHandler.getBaseBinder().getCenterContent();
		this.oldEmergencyNum = emergencyModel;
	}
	
	public void initEditEmergencyContactView(){
		
		this.centerContent.clear();
		
		this.phoneNumLabel = new Label("Phone Number");
		this.phoneNum = new TextBox();
		this.phoneNum.setText(this.oldEmergencyNum.getNumber());
		
		this.descriptionLabel = new Label("Description");
		this.descriptionText = new TextBox();
		this.descriptionText.setText(this.oldEmergencyNum.getDescription());
		
		this.countryLabel = new Label("Country");
		this.countryListBox = new ListBox();
		fillWithCountries(this.countryListBox);
		this.countryListBox.setSelectedIndex(getCountryIndex(this.countryListBox, this.oldEmergencyNum.getCountry()));
		
		this.saveEmergencyContact = new Button("Save Contact");
		
		EditEmergencyContactClickHandler emergencyClickHandler = new EditEmergencyContactClickHandler();
		this.saveEmergencyContact.addClickHandler(emergencyClickHandler);
		
		this.addEmergencyContactPanel = new HTMLPanel("");
		this.addEmergencyContactPanel.setStyleName(VIEW_CONTENT_CLASSNAME);
		
		this.countryPanel.setStyleName("countryFlowPanel");
		this.countryPanel.add(this.countryLabel);
		this.countryPanel.add(this.countryListBox);
		this.addEmergencyContactPanel.add(this.countryPanel);
		
		this.phoneNumPanel.setStyleName("phoneFlowPanel");
		this.phoneNumPanel.add(this.phoneNumLabel);
		this.phoneNumPanel.add(this.phoneNum);
		this.addEmergencyContactPanel.add(this.phoneNumPanel);
		
		this.descriptionPanel.setStyleName("descriptionFlowPanel");
		this.descriptionPanel.add(this.descriptionLabel);
		this.descriptionPanel.add(this.descriptionText);
		this.addEmergencyContactPanel.add(this.descriptionPanel);
		
		this.addEmergencyContactPanel.add(this.saveEmergencyContact);
		
		this.smartphone = smartphone;
		
		this.centerContent.add(this.addEmergencyContactPanel);
	}
	
	public void saveEmergencyNumber(EmergencyNumberModel oldEmergencyContact, EmergencyNumberModel emergencyContact){	
		
		EditEmergencyContactCallBackHandler editEmergencyCallBack = new EditEmergencyContactCallBackHandler(baseViewHandler, baseViewHandler.getUser(), centerContent, oldEmergencyContact, emergencyContact, baseViewHandler.getUser().getCid(), smartphone);
		EditEmergencyContactServiceAsync editEmergencyAsync = GWT.create(EditEmergencyContactService.class);
		editEmergencyAsync.editEmergencyContact(emergencyContact, smartphone.getKeyId(), editEmergencyCallBack);
	}

	public class EditEmergencyContactClickHandler implements ClickHandler {

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
			emergencyContact.setKeyId(oldEmergencyNum.getKeyId());

			saveEmergencyNumber(oldEmergencyNum, emergencyContact);
		}		
	}
	
	public int getCountryIndex(ListBox countryList, String country){
		
		for(int i = 0; i<countryList.getItemCount(); i++){
			if(country.equals(countryList.getItemText(i))){
				return i;
			}
		}
		return 0;
		
	}
	
	public void fillWithCountries(ListBox countryListBox){
		countryListBox.addItem("United States");
		countryListBox.addItem("Venezuela");
	}

}