package com.ing3nia.parentalcontrol.client.views;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.view.client.ListDataProvider;

import com.ing3nia.parentalcontrol.client.views.classnames.AdminUserListViewClassName;
import com.ing3nia.parentalcontrol.client.views.classnames.PCTableViewClassNames;

import com.ing3nia.parentalcontrol.client.models.AlertModel;


public class AlertListView {
	/**
	 * Center Panel containing all the widgets of the 
	 * alert list view.
	 */
	private HTMLPanel centerContent;
	
	/**
	 * Main panel of alert list view that groups all 
	 * the widgets together.
	 */
	private HTMLPanel viewContent;
	
	/**
	 * List of alerts received by a user.
	 */
	private List<AlertModel> alerts;
	
	/**
	 * Table where the alerts are displayed.
	 */
	private CellTable<AlertModel> alertTable;

	public AlertListView(HTMLPanel centerContent, ArrayList<AlertModel> alertList) {
		this.centerContent = centerContent;
		this.centerContent.setStyleName("centerContent");
		viewContent = new HTMLPanel("");
		this.alertTable = new CellTable<AlertModel>(10);
		this.viewContent = new HTMLPanel("");
		this.alerts = alertList;
		this.centerContent.clear();
		
		//addTestAlerts();
	}
	
	public void addTestAlerts() {
		Date now = new Date();//Calendar.getInstance().getTime();		
		
		AlertModel alert = new AlertModel(now, "Device 1", "LOW BATTERY");
		alerts.add(alert);
		
		alert = new AlertModel(now, "Device 2", "DEAD_BATTERY");
		alerts.add(alert);
		
		alert = new AlertModel(now, "Device 3", "UNREACHABLE_PHONE");
		alerts.add(alert);
		
		alert = new AlertModel(now, "Device 4", "SPEED_LIMIT_OVERSTEP");
		alerts.add(alert);
	}
	
	public void initAlertListView() {
		//final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
		final DateTimeFormat formatter = DateTimeFormat.getFormat("dd/MM/yyyy hh:mm:ss a");
		
		//Setting alert table style
		alertTable.setStyleName(PCTableViewClassNames.EXTENDED_TABLE.getClassname());
		
	    // Add a date column to show the creation date of the alert.
		TextColumn<AlertModel> dateColumn = new TextColumn<AlertModel>() {
			@Override
			public String getValue(AlertModel object) {
				return formatter.format(object.getDate());
				//return formatter.format(object.getDate());
			}
		};
		
		alertTable.addColumn(dateColumn, "Date");
	    
	    // Add a text column to show the device name.
		TextColumn<AlertModel> deviceNameColumn = new TextColumn<AlertModel>() {
			@Override
			public String getValue(AlertModel object) {
				return object.getDevice();
			}
		};
		
		alertTable.addColumn(deviceNameColumn, "Device Name");
		
		// Add a text column to show the alert's message.
		TextColumn<AlertModel> messageColumn = new TextColumn<AlertModel>() {
			@Override
			public String getValue(AlertModel object) {
				return object.getMessage();
			}
		};
		
		alertTable.addColumn(messageColumn, "Message");

		// Set the total row count. This isn't strictly necessary, but it
		// affects paging calculations, so its good habit to keep the row 
	    // count up to date.
	    alertTable.setRowCount(alerts.size(), true);

		// Push the data into the widget.
		ListDataProvider<AlertModel> dataProvider = new ListDataProvider<AlertModel>(alerts);
		dataProvider.addDataDisplay(alertTable);
		
		//creating paging controls
		SimplePager pager = new SimplePager();
		SimplePager pager2 = new SimplePager();
		pager.setDisplay(alertTable);
		pager2.setDisplay(alertTable);
		
		pager2.setStyleName("tablePager");
		viewContent.add(pager2);
		viewContent.add(alertTable);
		pager.setStyleName("tablePager");
		viewContent.add(pager);		
		
		centerContent.add(viewContent);
	}
}
