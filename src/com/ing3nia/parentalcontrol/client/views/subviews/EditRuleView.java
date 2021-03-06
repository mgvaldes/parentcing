package com.ing3nia.parentalcontrol.client.views.subviews;

import java.util.ArrayList;
import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.datepicker.client.DateBox;
import com.ing3nia.parentalcontrol.client.handlers.BaseViewHandler;
import com.ing3nia.parentalcontrol.client.handlers.click.EnableFunctionalityClickHandler;
import com.ing3nia.parentalcontrol.client.handlers.click.innerbutton.SaveEditRuleClickHandler;
import com.ing3nia.parentalcontrol.client.models.RuleModel;
import com.ing3nia.parentalcontrol.client.models.SmartphoneModel;
import com.ing3nia.parentalcontrol.client.rpc.EditRuleService;
import com.ing3nia.parentalcontrol.client.rpc.EditRuleServiceAsync;
import com.ing3nia.parentalcontrol.client.utils.FunctionalityTypeId;
import com.ing3nia.parentalcontrol.client.views.async.EditRuleCallbackHandler;

public class EditRuleView {
	
	public static String VIEW_CONTENT_CLASSNAME = "editRuleContent";
	public static String DATE_RULE = "dateRuleContent";
	public static String FROM_TIME_RULE = "fromTimeRuleContent";
	public static String TILL_TIME_RULE ="tillTimeRuleContent";
	
	
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
	private ArrayList<String> disabledFunctionalities;
	
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
	//private TextBox fromDateTextBox;
	
	/**
	 * From date picker.
	 */
	//private DatePicker fromDatePicker;
	private DateBox fromDatePicker;
	
	/**
	 * To date label.
	 */
	private Label toDateLabel;
	
	/**
	 * To date text box.
	 */
	//private TextBox toDateTextBox;
	
	/**
	 * To date picker.
	 */
	//private DatePicker toDatePicker;
	private DateBox toDatePicker;
	
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
	
	private BaseViewHandler baseViewHandler;
	
	private RuleModel rule;
	
	public EditRuleView(BaseViewHandler baseViewHandler, String cookieId, SmartphoneModel smartphone, RuleModel rule) {
		this.baseViewHandler = baseViewHandler;
		this.centerContent = baseViewHandler.getBaseBinder().getCenterContent();
        this.centerContent.setStyleName("centerContent");
		this.cookieId = cookieId;
		this.smartphone = smartphone;
		this.rule = rule;
		
		this.newRuleContent = new HTMLPanel("");
		this.newRuleContent.setStyleName(VIEW_CONTENT_CLASSNAME);		
		
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
		
		this.disabledFunctionalitiesPanel = new HTMLPanel("");
		this.disabledFunctionalitiesListBox = new ListBox();		
		this.disabledFunctionalityAddButton = new Button("Disable");
		this.disabledFunctionalitiesTable = new FlexTable();
		this.disabledFunctionalities = new ArrayList<String>();
		
		this.datePanel = new HTMLPanel("");
		this.fromDateLabel = new Label("Date:");
		this.fromDatePicker = new DateBox();	
		this.toDateLabel = new Label("to");
		this.toDatePicker = new DateBox();
		
		this.fromTimePanel = new HTMLPanel("");
		this.fromTimeLabel = new Label("From:");
		this.hourTextBoxF = new TextBox();
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
		DOM.setElementAttribute(this.saveButton.getElement(), "id", "saveRuleButton");
		this.clearButton = new Button("Clear");
		
		this.centerContent.clear();
		//initEditRuleView();
	}
	
	public void initEditRuleView() {
		setOldRuleValues();
		
		DateTimeFormat formatter = DateTimeFormat.getFormat("dd/MM/yyyy");
		
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
		
		this.fromDatePicker.setFormat(new DateBox.DefaultFormat(formatter));
		this.datePanel.add(this.fromDatePicker);
		this.datePanel.add(this.toDateLabel);
		
		this.toDatePicker.setFormat(new DateBox.DefaultFormat(formatter));
		this.datePanel.add(this.toDatePicker);
		this.datePanel.setStyleName(DATE_RULE);
		this.newRuleContent.add(this.datePanel);
		
		this.fromTimePanel.add(this.fromTimeLabel);
		this.fromTimePanel.setStyleName(FROM_TIME_RULE);
		this.hourTextBoxF.addValueChangeHandler(new HourChangeHandler(this.hourTextBoxF, true));
		this.fromTimePanel.add(this.hourTextBoxF);
		this.minuteTextBoxF.addValueChangeHandler(new MinuteSecondsChangeHandler(this.minuteTextBoxF, true, true));
		this.fromTimePanel.add(this.minuteTextBoxF);
		this.secondsTextBoxF.addValueChangeHandler(new MinuteSecondsChangeHandler(this.secondsTextBoxF, true, false));
		this.fromTimePanel.add(this.secondsTextBoxF);		
		this.fromTimePanel.add(this.ampmListBoxF);
		this.newRuleContent.add(this.fromTimePanel);
		
		this.tillTimePanel.add(this.tillTimeLabel);
		this.tillTimePanel.setStyleName(TILL_TIME_RULE);
		this.hourTextBoxT.addValueChangeHandler(new HourChangeHandler(this.hourTextBoxT, false));
		this.tillTimePanel.add(this.hourTextBoxT);
		this.minuteTextBoxT.addValueChangeHandler(new MinuteSecondsChangeHandler(this.minuteTextBoxT, false, true));
		this.tillTimePanel.add(this.minuteTextBoxT);
		this.secondsTextBoxT.addValueChangeHandler(new MinuteSecondsChangeHandler(this.secondsTextBoxT, false, false));
		this.tillTimePanel.add(this.secondsTextBoxT);
		this.tillTimePanel.add(this.ampmListBoxT);
		this.newRuleContent.add(this.tillTimePanel);
		
		SaveEditRuleClickHandler saveEditRule = new SaveEditRuleClickHandler(this);
		saveButton.addClickHandler(saveEditRule);
		
		buttonPanel.setStyleName("buttonsContent");
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
		disabledFunctionalitiesListBox.addItem("Incomming Calls");
		disabledFunctionalitiesListBox.addItem("Outgoing SMS");
		disabledFunctionalitiesListBox.addItem("Incomming SMS");
		
		if (ruleTypeListBox.getSelectedIndex() == 1) {
			disabledFunctionalitiesListBox.addItem("Total Block");
		}
		
		this.disabledFunctionalities = new ArrayList<String>();
		this.disabledFunctionalitiesTable.removeAllRows();
	}
	
	public void addDisabledFunctionality() {
		// Don't add the disabled functionality if it's already in the table.
		final String selectedDisabledFunc = this.disabledFunctionalitiesListBox.getItemText(this.disabledFunctionalitiesListBox.getSelectedIndex());
		if (this.disabledFunctionalities.contains(selectedDisabledFunc)) {
			return;
		}
		
		if (this.disabledFunctionalitiesListBox.getSelectedIndex() == 0) {
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
		if (ruleTypeListBox.getSelectedIndex() == 0 &&
			ruleNameTextBox.getText().equals("") &&
			disabledFunctionalities.isEmpty() &&			
			hourTextBoxF.getText().equals("") &&
			minuteTextBoxF.getText().equals("") &&
			secondsTextBoxF.getText().equals("") &&
			hourTextBoxT.getText().equals("") &&
			minuteTextBoxT.getText().equals("") &&
			secondsTextBoxT.getText().equals("")) {

			baseViewHandler.getBaseBinder().getNotice().setText("All fields must be specified");
			centerContent.add(baseViewHandler.getBaseBinder().getNotice());
		}
		else {
			RuleModel newRule = new RuleModel();
			newRule.setKeyId(this.rule.getKeyId());
			newRule.setName(ruleNameTextBox.getText());
			newRule.setDisabledFunctionalities(loadFunctionalityIds());
			
			DateTimeFormat formatter = DateTimeFormat.getFormat("dd/MM/yyyy hh:mm:ss a");			
			newRule.setCreationDate(formatter.format(new Date()));
						
			String auxDate = fromDatePicker.getTextBox().getText() + " " + addDigit(hourTextBoxF.getText()) + ":" + addDigit(minuteTextBoxF.getText()) + ":" + addDigit(secondsTextBoxF.getText()) + " " + ampmListBoxF.getItemText(ampmListBoxF.getSelectedIndex());
			newRule.setStartDate(auxDate);
			
			auxDate = toDatePicker.getTextBox().getText() + " " + addDigit(hourTextBoxT.getText()) + ":" + addDigit(minuteTextBoxT.getText()) + ":" + addDigit(secondsTextBoxT.getText()) + " " + ampmListBoxT.getItemText(ampmListBoxT.getSelectedIndex());
			newRule.setEndDate(auxDate);
			
			newRule.setType(ruleTypeListBox.getSelectedIndex());
			
			EditRuleCallbackHandler editRuleCallback = new EditRuleCallbackHandler(this.baseViewHandler, this.rule, newRule, this.cookieId, this.smartphone.getKeyId());
			EditRuleServiceAsync editRuleService = GWT.create(EditRuleService.class);
			editRuleService.editRule(newRule, editRuleCallback);
		}				
	}
	
	public String addDigit(String text) {
		String newText = "";
		
		if (text.length() == 1) {
			newText += "0";
		}
		
		newText += text;
		
		return newText;
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
		this.fromDatePicker.getTextBox().setText("");
		this.toDatePicker.getTextBox().setText("");
		this.hourTextBoxF.setText("");
		this.minuteTextBoxF.setText("");
		this.secondsTextBoxF.setText("");
		this.hourTextBoxT.setText("");
		this.minuteTextBoxT.setText("");
		this.secondsTextBoxT.setText("");
		this.disabledFunctionalitiesTable.removeAllRows();
	}
	
	public class HourChangeHandler implements ValueChangeHandler<String> {
		TextBox hourTextBox;
		Boolean fromOrTill;
		
		public HourChangeHandler(TextBox hourTextBox, boolean fromOrTill) {
			this.hourTextBox = hourTextBox;
			this.fromOrTill = fromOrTill;
		}				
		
		@Override
		public void onValueChange(ValueChangeEvent<String> event) {
			int hour = Integer.valueOf(event.getValue());
			
			if ((hour < 1) || (hour > 12)) {
				this.hourTextBox.setText("");
				
				if (this.fromOrTill) {
					baseViewHandler.getBaseBinder().getNotice().setText("The From hours value must be between 1 and 12.");
				}
				else {
					baseViewHandler.getBaseBinder().getNotice().setText("The Till hours value must be between 1 and 12.");
				}
				
				baseViewHandler.getBaseBinder().getCenterContent().add(baseViewHandler.getBaseBinder().getNotice());
			}
			else {
				baseViewHandler.getBaseBinder().getNotice().setText("");
				baseViewHandler.getBaseBinder().getCenterContent().add(baseViewHandler.getBaseBinder().getNotice());
			}
		}
	}
	
	public class MinuteSecondsChangeHandler implements ValueChangeHandler<String> {
		TextBox minuteSecondsTextBox;
		Boolean fromOrTill;
		Boolean minuteOrSeconds;
		
		public MinuteSecondsChangeHandler(TextBox hourTextBox, boolean fromOrTill, boolean minuteOrSeconds) {
			this.minuteSecondsTextBox = hourTextBox;
			this.fromOrTill = fromOrTill;
			this.minuteOrSeconds = minuteOrSeconds;
		}				
		
		@Override
		public void onValueChange(ValueChangeEvent<String> event) {
			int mos = Integer.valueOf(event.getValue());
			
			if ((mos < 0) || (mos > 59)) {
				this.minuteSecondsTextBox.setText("");
				
				if (this.fromOrTill) {
					if (this.minuteOrSeconds) {
						baseViewHandler.getBaseBinder().getNotice().setText("The From minutes value must be between 0 and 59.");
					}
					else {
						baseViewHandler.getBaseBinder().getNotice().setText("The From seconds value must be between 0 and 59.");
					}					
				}
				else {
					if (this.minuteOrSeconds) {
						baseViewHandler.getBaseBinder().getNotice().setText("The Till minutes value must be between 0 and 59.");
					}
					else {
						baseViewHandler.getBaseBinder().getNotice().setText("The Till seconds value must be between 0 and 59.");
					}
				}
				
				baseViewHandler.getBaseBinder().getCenterContent().add(baseViewHandler.getBaseBinder().getNotice());
			}
			else {
				baseViewHandler.getBaseBinder().getNotice().setText("");
				baseViewHandler.getBaseBinder().getCenterContent().add(baseViewHandler.getBaseBinder().getNotice());
			}
		}
	}
	
	public void setOldRuleValues() {
		this.ruleTypeListBox.setSelectedIndex(this.rule.getType());
		loadFunctionalities();
		
		int row = 0;
		String funcDesc;
		
		for (int func : this.rule.getDisabledFunctionalities()) {
			funcDesc = FunctionalityTypeId.findById(func);
			this.disabledFunctionalities.add(funcDesc);
		
			this.disabledFunctionalitiesTable.setText(row, 0, funcDesc);

			// Add a button to enable this functionality.
			Button enableFuncButton = new Button("Enable");
			enableFuncButton.addClickHandler(new EnableFunctionalityClickHandler(funcDesc, this.disabledFunctionalities, this.disabledFunctionalitiesTable));
			
			disabledFunctionalitiesTable.setWidget(row, 1, enableFuncButton);
			
			row++;
		}
		
		this.ruleNameTextBox.setText(this.rule.getName());
		
		//012345678901234567890
		//dd/MM/yyyy hh:mm:ss a
		
		DateTimeFormat formatter = DateTimeFormat.getFormat("dd/MM/yyyy");
		
		this.fromDatePicker.setValue(formatter.parse(this.rule.getStartDate().substring(0, 10)));
		this.toDatePicker.setValue(formatter.parse(this.rule.getEndDate().substring(0, 10)));
		//this.fromDatePicker.getTextBox().setText(nose);
		//this.toDatePicker.getTextBox().setText(this.rule.getEndDate().substring(0, 10));
		
		this.hourTextBoxF.setText(this.rule.getStartDate().substring(11, 13));
		this.minuteTextBoxF.setText(this.rule.getStartDate().substring(14, 16));
		this.secondsTextBoxF.setText(this.rule.getStartDate().substring(17, 19));
		
		if (this.rule.getStartDate().substring(20).equals("AM")) {
			this.ampmListBoxF.setSelectedIndex(0);
		}
		else {
			this.ampmListBoxF.setSelectedIndex(1);
		}
		
		this.hourTextBoxT.setText(this.rule.getEndDate().substring(11, 13));
		this.minuteTextBoxT.setText(this.rule.getEndDate().substring(14, 16));
		this.secondsTextBoxT.setText(this.rule.getEndDate().substring(17, 19));
		
		if (this.rule.getEndDate().substring(20).equals("AM")) {
			this.ampmListBoxT.setSelectedIndex(0);
		}
		else {
			this.ampmListBoxT.setSelectedIndex(1);
		}
	}
}
