package com.ing3nia.parentalcontrol.client.views;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.datepicker.client.DatePicker;
import com.ing3nia.parentalcontrol.client.models.ModificationModel;
import com.ing3nia.parentalcontrol.client.models.PropertyModel;
import com.ing3nia.parentalcontrol.client.models.RuleModel;
import com.ing3nia.parentalcontrol.client.models.SmartphoneModel;
import com.ing3nia.parentalcontrol.client.rpc.SaveSmartphoneModificationsService;
import com.ing3nia.parentalcontrol.client.rpc.SaveSmartphoneModificationsServiceAsync;
import com.ing3nia.parentalcontrol.models.utils.FunctionalityTypeId;

public class NewRuleView {
	/**
	 * Center Panel containing all the widgets of the 
	 * new ticket details view.
	 */
	private HTMLPanel centerContent;
	
	/**
	 * Main panel of new rule view that groups all 
	 * the widgets together.
	 */
	private HTMLPanel newRuleContent;
	
	/**
	 * Panel to group rule type widgets.
	 */
	private HTMLPanel ruleTypePanel;
	
	/**
	 * New rule label.
	 */
	private Label newRuleLabel;
	
	/**
	 * Rule type label.
	 */
	private Label ruleTypeLabel;
	
	/**
	 * Rule type list box.
	 */
	private ListBox ruleTypeListBox;
	
	/**
	 * Panel to group rule name widgets.
	 */
	private HTMLPanel ruleNamePanel;
	
	/**
	 * Rule name label.
	 */
	private Label ruleNameLabel;
	
	/**
	 * Rule name.
	 */
	private TextBox ruleNameTextBox;
	
	/**
	 * Panel to group disabled functionalities widgets.
	 */
	private HTMLPanel disabledFunctionalitiesPanel;
	
	/**
	 * Disabled functionalities list box.
	 */
	private ListBox disabledFunctionalitiesListBox;
	
	/**
	 * Button to add disabledFunctionalitis
	 */
	private Button disabledFunctionalityAddButton;
	
	/**
	 * Disabled functionalities table.
	 */
	private FlexTable disabledFunctionalitiesTable;
	
	/**
	 * Disabled functionalities list.
	 */
	private List<String> disabledFunctionalities;
	
	/**
	 * Panel to group date widgets
	 */
	private HTMLPanel datePanel;
	
	/**
	 * From date label.
	 */
	private Label fromDateLabel;
	
	/**
	 * From date text box.
	 */
	private TextBox fromDateTextBox;
	
	/**
	 * From date picker.
	 */
	private DatePicker fromDatePicker;
	
	/**
	 * To date label.
	 */
	private Label toDateLabel;
	
	/**
	 * To date text box.
	 */
	private TextBox toDateTextBox;
	
	/**
	 * To date picker.
	 */
	private DatePicker toDatePicker;
	
	/**
	 * Panel to group from time widgets
	 */
	private HTMLPanel fromTimePanel;
	
	/**
	 * From time lable.
	 */
	private Label fromTimeLabel;
	
	/**
	 * Hour text box.
	 */
	private TextBox hourTextBoxF;
	
	/**
	 * Time separator ':'.
	 */
	private Label timeSeparator;
	
	/**
	 * Minute text box.
	 */
	private TextBox minuteTextBoxF;
	
	/**
	 * Seconds text box.
	 */
	private TextBox secondsTextBoxF;
	
	/**
	 * AM-PM list box.
	 */
	private ListBox ampmListBoxF;
	
	/**
	 * Panel to group from time widgets
	 */
	private HTMLPanel tillTimePanel;
	
	/**
	 * From time lable.
	 */
	private Label tillTimeLabel;
	
	/**
	 * Hour text box.
	 */
	private TextBox hourTextBoxT;
	
	/**
	 * Minute text box.
	 */
	private TextBox minuteTextBoxT;
	
	/**
	 * Seconds text box.
	 */
	private TextBox secondsTextBoxT;
	
	/**
	 * AM-PM list box.
	 */
	private ListBox ampmListBoxT;
	
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
	 * Cookie id corresponding to current session. 
	 */
	private String cookieId;
	
	/**
	 * Smartphone key corresponding to the selected 
	 * smartphone where the new rule will be at.
	 */
	private SmartphoneModel smartphone;

	public NewRuleView(HTMLPanel centerContent, String cookieId, SmartphoneModel smartphone) {
		this.centerContent = centerContent;
		this.cookieId = cookieId;
		this.smartphone = smartphone;
		
		this.newRuleContent = new HTMLPanel("");
				
		this.newRuleLabel = new Label("New Rule:");
		
		this.ruleTypePanel = new HTMLPanel("");
		this.ruleTypeLabel = new Label("Rule Type:");		
		this.ruleTypeListBox = new ListBox();		
		this.ruleTypeListBox.addItem("Select Rule Type");
		this.ruleTypeListBox.addItem("Normal");
		this.ruleTypeListBox.addItem("Speed Limit");
		
		this.ruleNamePanel = new HTMLPanel("");
		this.ruleNameLabel = new Label("Name:");
		this.ruleNameTextBox = new TextBox();
		
		this.disabledFunctionalitiesListBox = new ListBox();		
		this.disabledFunctionalityAddButton = new Button("Disable");
		this.disabledFunctionalitiesTable = new FlexTable();
		this.disabledFunctionalities = new ArrayList<String>();
		
		this.datePanel = new HTMLPanel("");
		this.fromDateLabel = new Label("Date:");
		this.fromDateTextBox = new TextBox();
		this.fromDatePicker = new DatePicker();		
		this.toDateLabel = new Label("to");
		this.toDateTextBox = new TextBox();
		this.toDatePicker = new DatePicker();
		
		this.fromTimePanel = new HTMLPanel("");
		this.fromTimeLabel = new Label("From:");
		this.hourTextBoxF = new TextBox();
		this.timeSeparator = new Label(":");
		this.minuteTextBoxF = new TextBox();
		this.secondsTextBoxF = new TextBox();
		
		this.ampmListBoxF = new ListBox();
		this.ampmListBoxF.addItem("AM");
		this.ampmListBoxF.addItem("PM");		
		this.ampmListBoxT = new ListBox();
		this.ampmListBoxT.addItem("AM");
		this.ampmListBoxT.addItem("PM");
		
		this.tillTimePanel = new HTMLPanel("");
		this.tillTimeLabel = new Label("Till");
		this.hourTextBoxT = new TextBox();
		this.minuteTextBoxT = new TextBox();
		this.secondsTextBoxT = new TextBox();
		
		this.buttonPanel = new FlowPanel();
		this.saveButton = new Button("Save");
		this.clearButton = new Button("Clear");
		
		this.centerContent.clear();
		initNewRuleView();
	}
	
	public void initNewRuleView() {
		this.newRuleContent.add(newRuleLabel);
		
		this.ruleTypePanel.add(ruleTypeLabel);
		
		ruleTypeListBox.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				loadFunctionalities();
			}
		});
		
		this.ruleTypePanel.add(ruleTypeListBox);
		
		this.newRuleContent.add(this.ruleTypePanel);
		
		this.ruleNamePanel.add(this.ruleNameLabel);
		this.ruleNamePanel.add(this.ruleNameTextBox);
		this.newRuleContent.add(this.ruleNamePanel);
		
		this.disabledFunctionalitiesPanel.add(disabledFunctionalitiesListBox);
		disabledFunctionalityAddButton.addClickHandler(new ClickHandler() {
	    	public void onClick(ClickEvent event) {
	    		addDisabledFunctionality();
	    	}
	    });
		this.disabledFunctionalitiesPanel.add(disabledFunctionalityAddButton);
		this.newRuleContent.add(this.disabledFunctionalitiesPanel);
		
		this.newRuleContent.add(this.disabledFunctionalitiesTable);
		
		this.datePanel.add(this.fromDateLabel);
		this.datePanel.add(this.fromDateTextBox);
		this.datePanel.add(this.fromDatePicker);
		this.datePanel.add(this.toDateLabel);
		this.datePanel.add(this.toDateTextBox);
		this.datePanel.add(this.toDatePicker);
		this.newRuleContent.add(this.datePanel);
		
		this.fromTimePanel.add(this.fromTimeLabel);
		this.fromTimePanel.add(this.hourTextBoxF);
		this.fromTimePanel.add(this.timeSeparator);
		this.fromTimePanel.add(this.minuteTextBoxF);
		this.fromTimePanel.add(this.timeSeparator);
		this.fromTimePanel.add(this.secondsTextBoxF);
		this.fromTimePanel.add(this.timeSeparator);
		this.fromTimePanel.add(this.ampmListBoxF);
		this.newRuleContent.add(this.fromTimePanel);
		
		this.tillTimePanel.add(this.tillTimeLabel);
		this.tillTimePanel.add(this.hourTextBoxT);
		this.tillTimePanel.add(this.timeSeparator);
		this.tillTimePanel.add(this.minuteTextBoxT);
		this.tillTimePanel.add(this.timeSeparator);
		this.tillTimePanel.add(this.secondsTextBoxT);
		this.tillTimePanel.add(this.timeSeparator);
		this.tillTimePanel.add(this.ampmListBoxT);
		this.newRuleContent.add(this.tillTimePanel);
		
		saveButton.addClickHandler(new ClickHandler() {
	    	public void onClick(ClickEvent event) {
	    		saveRule();
	    	}
	    });
		
		buttonPanel.add(saveButton);
		
		clearButton.addClickHandler(new ClickHandler() {
	    	public void onClick(ClickEvent event) {
	    		clearTextBoxes();
	    	}
	    });
		
		buttonPanel.add(clearButton);
		this.newRuleContent.add(this.buttonPanel);
		
		this.centerContent.add(this.newRuleContent);
	}
	
	public void loadFunctionalities() {
		disabledFunctionalitiesListBox.clear();
		this.disabledFunctionalitiesListBox.addItem("Select Disabled Functionality");
		
		disabledFunctionalitiesListBox.addItem("Browser Access");
		disabledFunctionalitiesListBox.addItem("Outgoing Calls");
		disabledFunctionalitiesListBox.addItem("Incomming Calss");
		disabledFunctionalitiesListBox.addItem("Outgoing SMS");
		disabledFunctionalitiesListBox.addItem("Incomming SMS");
		
		if (ruleTypeListBox.getSelectedIndex() == 1) {
			disabledFunctionalitiesListBox.addItem("Total Block");
		}
	}
	
	public void addDisabledFunctionality() {
		this.disabledFunctionalitiesListBox.setSelectedIndex(0);

		// Don't add the disabled functionality if it's already in the table.
		final String selectedDisabledFunc = this.disabledFunctionalitiesListBox.getItemText(this.disabledFunctionalitiesListBox.getSelectedIndex());
		if (this.disabledFunctionalities.contains(selectedDisabledFunc)) {
			return;
		}

		// Add the disabled functionality to the table.
		int row = this.disabledFunctionalitiesTable.getRowCount();
		this.disabledFunctionalities.add(selectedDisabledFunc);
		this.disabledFunctionalitiesTable.setText(row, 0, selectedDisabledFunc);

		// Add a button to enable this functionality.
		Button enableFuncButton = new Button("Enable");
		enableFuncButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				int removedIndex = disabledFunctionalities.indexOf(selectedDisabledFunc);
				disabledFunctionalities.remove(removedIndex);
				disabledFunctionalitiesTable.removeRow(removedIndex);
			}
		});
		
		disabledFunctionalitiesTable.setWidget(row, 1, enableFuncButton);
	}
	
	public void saveRule() {
		ModificationModel auxMod = new ModificationModel(); 
		
		if (ruleTypeListBox.getSelectedIndex() == 0 &&
			ruleNameTextBox.getText().equals("") &&
			disabledFunctionalities.isEmpty() &&
			fromDateTextBox.getText().equals("") &&
			toDateTextBox.getText().equals("") &&
			hourTextBoxF.getText().equals("") &&
			minuteTextBoxF.getText().equals("") &&
			secondsTextBoxF.getText().equals("") &&
			hourTextBoxT.getText().equals("") &&
			minuteTextBoxT.getText().equals("") &&
			secondsTextBoxT.getText().equals("")) {
			Window.alert("All fields must be specified.");
		}
		else {
			final RuleModel newRule = new RuleModel();
			newRule.setName(ruleNameTextBox.getText());
			newRule.setDisabledFunctionalities(loadFunctionalityIds());
			
			DateTimeFormat formatter = DateTimeFormat.getFormat("dd/MM/yyyy hh:mm:ss a");			
			newRule.setCreationDate(formatter.format(Calendar.getInstance().getTime()));
			
			String auxDate = fromDateTextBox.getText() + " " + hourTextBoxF.getText() + ":" + minuteTextBoxF.getText() + ":" + secondsTextBoxF.getText() + ampmListBoxF.getItemText(ampmListBoxF.getSelectedIndex());
			newRule.setStartDate(auxDate);
			
			auxDate = toDateTextBox.getText() + " " + hourTextBoxT.getText() + ":" + minuteTextBoxT.getText() + ":" + secondsTextBoxT.getText() + ampmListBoxT.getItemText(ampmListBoxT.getSelectedIndex());
			newRule.setStartDate(auxDate);
			
			ArrayList<RuleModel> rules = new ArrayList<RuleModel>();
			rules.add(newRule);
			
			auxMod.setRules(rules);
			
			SaveSmartphoneModificationsServiceAsync saveModService = GWT.create(SaveSmartphoneModificationsService.class);
			saveModService.saveSmartphoneModifications(this.cookieId, this.smartphone.getKeyId(), auxMod, 
					new AsyncCallback<Boolean>() {
						public void onFailure(Throwable error) {
						}
			
						public void onSuccess(Boolean result) {
							if (result) {
								ArrayList<RuleModel> rules = smartphone.getRules();
								rules.add(newRule);								
								smartphone.setRules(rules);
							}
							else {
								Window.alert("An error occured. The new rule couldn't be saved.");
							}
						}
					}
			);
		}				
	}
	
	public ArrayList<Integer> loadFunctionalityIds() {
		ArrayList<Integer> funcIds = new ArrayList<Integer>();
		
		for (String func : disabledFunctionalities) {
			funcIds.add(FunctionalityTypeId.findByDescription(func));
		}
		
		return funcIds;
	}
	
	public void clearTextBoxes() {
		this.ruleTypeListBox.setSelectedIndex(0);
		this.ruleNameTextBox.setText("");
		this.disabledFunctionalitiesListBox.setSelectedIndex(0);
		this.fromDateTextBox.setText("");
		this.toDateTextBox.setText("");
		this.hourTextBoxF.setText("");
		this.minuteTextBoxF.setText("");
		this.secondsTextBoxF.setText("");
		this.hourTextBoxT.setText("");
		this.minuteTextBoxT.setText("");
		this.secondsTextBoxT.setText("");
	}
}
