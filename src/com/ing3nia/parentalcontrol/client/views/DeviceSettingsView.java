package com.ing3nia.parentalcontrol.client.views;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;

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
	
	public DeviceSettingsView(HTMLPanel centerContent) {
		this.centerContent = centerContent;
		
		deviceSettingsContent = new HTMLPanel("");
		deviceSettingsLabel = new Label("Device Settings:");
		deviceNameLabel = new Label("Device Name:");
		deviceNameTextBox = new TextBox();
		
		speedLimitPanel = new FlowPanel();
		speedLimitLabel = new Label("Speed Limit:");
		speedLimitTextBox = new TextBox();
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
	
	public void initDeviceSettingsView() {
		deviceSettingsContent.add(deviceSettingsLabel);
		deviceSettingsContent.add(deviceNameLabel);
		deviceSettingsContent.add(deviceNameTextBox);
		deviceSettingsContent.add(speedLimitLabel);
		deviceSettingsContent.add(speedLimitTextBox);
		
		speedLimitPanel.add(speedLimitTextBox);
		speedLimitPanel.add(mphLabel);		
		deviceSettingsContent.add(speedLimitPanel);
		
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
	
	public void saveDeviceSettings() {
		
	}
	
	public void clearTextBoxes() {
		deviceNameTextBox.setText("");
		speedLimitTextBox.setText("");
	}
}
