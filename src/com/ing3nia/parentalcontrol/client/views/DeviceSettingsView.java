package com.ing3nia.parentalcontrol.client.views;


import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

import java.util.ArrayList;
import java.util.Calendar;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;

import com.ing3nia.parentalcontrol.client.handlers.BaseViewHandler;
import com.ing3nia.parentalcontrol.client.models.ModificationModel;
import com.ing3nia.parentalcontrol.client.models.PropertyModel;
import com.ing3nia.parentalcontrol.client.models.RuleModel;
import com.ing3nia.parentalcontrol.client.models.SmartphoneModel;
import com.ing3nia.parentalcontrol.client.rpc.SaveSmartphoneModificationsService;
import com.ing3nia.parentalcontrol.client.rpc.SaveSmartphoneModificationsServiceAsync;
import com.ing3nia.parentalcontrol.client.utils.FunctionalityTypeId;
import com.ing3nia.parentalcontrol.client.utils.PCPropertyType;
import com.ing3nia.parentalcontrol.client.views.async.SaveSmartphoneModificationsCallbackHandler;


public class DeviceSettingsView {
	/**
	 * Center Panel containing all the widgets of the 
	 * device settings view.
	 */
	private HTMLPanel centerContent;
	
	/**
	 * Main panel of device settings view that groups all 
	 * the widgets together.
	 */
	private HTMLPanel deviceSettingsContent;
	
	/**
	 * Device settings label.
	 */
	private Label deviceSettingsLabel;
	
	/**
	 * Device name label.
	 */
	private Label deviceNameLabel;
	
	/**
	 * Device name text box.
	 */
	private TextBox deviceNameTextBox;
	
	/**
	 * Speed limit label.
	 */
	private Label speedLimitLabel;
	
	/**
	 * Panel that groups all block phone widgets.
	 */
	private FlowPanel speedLimitPanel;
	
	/**
	 * Speed limit text box.
	 */
	private TextBox speedLimitTextBox;
	
	/**
	 * MPH label.
	 */
	private Label mphLabel;
	
	/**
	 * Panel that groups all block phone widgets.
	 */
	private FlowPanel blockPhonePanel;
	
	/**
	 * Block phone button.
	 */
	private Button blockPhoneButton;
	
	/**
	 * Block phone label.
	 */
	private Label blockPhoneLabel;
	
	/**
	 * Panel that groups all restore phone widgets.
	 */
	private FlowPanel restorePhonePanel;
	
	/**
	 * Block phone button.
	 */
	private Button restorePhoneButton;
	
	/**
	 * Block phone label.
	 */
	private Label restorePhoneLabel;
	
	/**
	 * Panel containing view buttons.
	 */
	private FlowPanel buttonPanel;
	
	/**
	 * Save button.
	 */
	private Button saveButton;
	
	/**
	 * Clear button.
	 */
	private Button clearButton;
	

	/**
	 * Holds all the information of devices and personal information
	 * of a specific user.
	 */
	private SmartphoneModel smartphone;
	
	/**
	 * 
	 */
	private String cookieId;
	
	private BaseViewHandler baseView;
	
	public DeviceSettingsView(BaseViewHandler baseView, SmartphoneModel smartphone, String cookieId) {
		this.baseView = baseView;
		this.centerContent = baseView.getBaseBinder().getCenterContent();
		this.centerContent.setStyleName("centerContent");
		this.smartphone = smartphone;
		this.cookieId = cookieId;
		
		deviceSettingsContent = new HTMLPanel("");
		deviceSettingsLabel = new Label("Device Settings:");
		deviceNameLabel = new Label("Device Name:");
		deviceNameTextBox = new TextBox();
		
		if (smartphone.getName() != null) {
			deviceNameTextBox.setText(smartphone.getName());
		}
		
		speedLimitPanel = new FlowPanel();
		speedLimitLabel = new Label("Speed Limit:");
		speedLimitTextBox = new TextBox();
		loadSpeedLimit();
		mphLabel = new Label("MPH");
		
		blockPhonePanel = new FlowPanel();
		blockPhoneButton = new Button("Block Phone");
		blockPhoneLabel = new Label("(In the case your mobile phone is stolen, you must report the theft inmediately to your operator)");
		
		restorePhonePanel = new FlowPanel();
		restorePhoneButton = new Button("Restore Phone");
		restorePhoneLabel = new Label("(Restore phone to default settings before installing PRC)");
		
		this.buttonPanel = new FlowPanel();
		this.saveButton = new Button("Save");
		this.clearButton = new Button("Clear");
		
		this.centerContent.clear();
		initDeviceSettingsView();
	}
	
	public String loadSpeedLimit() {
		String speedLimit = "";
		
		ArrayList<PropertyModel> properties = this.smartphone.getProperties();
		
		for (PropertyModel p : properties) {
			if (p.getId() == PCPropertyType.SPEED_LIMIT) {
				this.speedLimitTextBox.setText(p.getValue());
				break;
			}
		}
		
		return speedLimit;
	}
	
	public void initDeviceSettingsView() {
		deviceSettingsContent.add(deviceSettingsLabel);
		deviceSettingsContent.add(deviceNameLabel);
		deviceSettingsContent.add(deviceNameTextBox);
		deviceSettingsContent.add(speedLimitLabel);
		deviceSettingsContent.add(speedLimitTextBox);
		
		speedLimitPanel.add(speedLimitTextBox);
		speedLimitPanel.add(mphLabel);		
		deviceSettingsContent.add(speedLimitPanel);
		
		blockPhoneButton.addClickHandler(new ClickHandler() {
	    	public void onClick(ClickEvent event) {
	    		blockPhone();
	    	}
	    });
		
		blockPhonePanel.add(blockPhoneButton);
		blockPhonePanel.add(blockPhoneLabel);
		deviceSettingsContent.add(blockPhonePanel);
		
		restorePhonePanel.add(restorePhoneButton);
		restorePhonePanel.add(restorePhoneLabel);
		deviceSettingsContent.add(restorePhonePanel);
		
		saveButton.addClickHandler(new ClickHandler() {
	    	public void onClick(ClickEvent event) {
	    		saveDeviceSettings();
	    	}
	    });
		
		buttonPanel.add(saveButton);
		
		clearButton.addClickHandler(new ClickHandler() {
	    	public void onClick(ClickEvent event) {
	    		clearTextBoxes();
	    	}
	    });
		
		buttonPanel.add(clearButton);
		this.deviceSettingsContent.add(this.buttonPanel);
		
		this.centerContent.add(this.deviceSettingsContent);
	}
	
	public void blockPhone() {
//		ModificationModel auxMod = new ModificationModel();
//		
//		final RuleModel newRule = new RuleModel();
//		newRule.setName(FunctionalityTypeId.TOTAL_BLOCK.getDescription());
//		
//		ArrayList<Integer> disabledFunctionalities = new ArrayList<Integer>();
//		disabledFunctionalities.add(FunctionalityTypeId.TOTAL_BLOCK.getId());
//		newRule.setDisabledFunctionalities(disabledFunctionalities);
//		
//		DateTimeFormat formatter = DateTimeFormat.getFormat("dd/MM/yyyy hh:mm:ss a");	
//		String now = formatter.format(Calendar.getInstance().getTime());
//		newRule.setCreationDate(now);
//		
//		newRule.setStartDate(now);
//		newRule.setEndDate(now);
//		
//		ArrayList<RuleModel> rules = new ArrayList<RuleModel>();
//		rules.add(newRule);
//		
//		auxMod.setRules(rules);
//		
//		SaveSmartphoneModificationsServiceAsync saveModService = GWT.create(SaveSmartphoneModificationsService.class);
//		saveModService.saveSmartphoneModifications(this.cookieId, this.smartphone.getKeyId(), auxMod, 
//				new AsyncCallback<Boolean>() {
//					public void onFailure(Throwable error) {
//					}
//		
//					public void onSuccess(Boolean result) {
//						if (result) {
//							ArrayList<RuleModel> rules = smartphone.getRules();
//							rules.add(newRule);								
//							smartphone.setRules(rules);
//						}
//						else {
//							Window.alert("An error occured. The new rule couldn't be saved.");
//						}
//					}
//				}
//		);
	}
	
	public void saveDeviceSettings() {
		ModificationModel auxMod = new ModificationModel(); 
		
		final String deviceName = this.deviceNameTextBox.getText();
		final String speedLimit = this.speedLimitTextBox.getText();		
		
		if (deviceName.equals("") || speedLimit.equals("")) {
			Window.alert("All fields must be specified.");
		}
		else {
			auxMod.setSmartphoneName(deviceName);
			
			ArrayList<PropertyModel> auxProps = new ArrayList<PropertyModel>();
			final PropertyModel auxProp = findSpeedLimitProperty();
			final int speedLimitPropPos = this.smartphone.getProperties().indexOf(auxProp);
			auxProp.setValue(speedLimit);
			auxProps.add(auxProp);
			auxMod.setProperties(auxProps);
			
			SaveSmartphoneModificationsCallbackHandler saveModCallback = new SaveSmartphoneModificationsCallbackHandler(baseView, this.deviceNameTextBox.getText(), this.speedLimitTextBox.getText(), 1);
			SaveSmartphoneModificationsServiceAsync saveModService = GWT.create(SaveSmartphoneModificationsService.class);
			saveModService.saveSmartphoneModifications(this.baseView.getUser().getCid(), this.smartphone.getKeyId(), auxMod, saveModCallback);	
		}
	}
	
	public void clearTextBoxes() {
		deviceNameTextBox.setText("");
		speedLimitTextBox.setText("");
	}

	
	public PropertyModel findSpeedLimitProperty() {
		PropertyModel speedLimitProp = null;
		ArrayList<PropertyModel> props = this.smartphone.getProperties();
		
		for (PropertyModel p : props) {
			if (p.getId() == PCPropertyType.SPEED_LIMIT) {
				speedLimitProp = p;
				break;
			}
		}
		
		return speedLimitProp;
	}
}
