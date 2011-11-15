package com.ing3nia.parentalcontrol.client.views;

import java.util.List;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.datepicker.client.DatePicker;

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
	 * New rule label.
	 */
	private Label newRuleLabel;
	
	/**
	 * Rule type label.
	 */
	private Label ruleType;
	
	/**
	 * Rule type list box.
	 */
	private ListBox ruleTypeListBox;
	
	/**
	 * Disabled functionalities list box.
	 */
	private ListBox disabledFunctionalitiesListBox;
	
	/**
	 * Disabled functionalities table.
	 */
	private FlexTable disabledFunctionalitiesTable;
	
	/**
	 * Disabled functionalities list.
	 */
	private List<String> disabledFunctionalities;
	
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
	 * From time lable.
	 */
	private Label fromTimeLabel;
	
	/**
	 * Hour text box.
	 */
	private TextBox hourTextBox;
	
	/**
	 * Time separator ':'.
	 */
	private Label timeSeparator;
	
	/**
	 * Minute text box.
	 */
	private TextBox minuteTextBox;
	
	/**
	 * Seconds text box.
	 */
	private TextBox secondsTextBox;
}
