package com.ing3nia.parentalcontrol.client.views;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.cell.client.DateCell;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.ing3nia.parentalcontrol.client.views.models.AlertModel;

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
	private HTMLPanel viewContent = new HTMLPanel("");
	
	/**
	 * List of alerts received by a user.
	 */
	private List<AlertModel> alerts = new ArrayList<AlertModel>();
	
	/**
	 * Table where the alerts are displayed.
	 */
	private CellTable<AlertModel> alertTable = new CellTable<AlertModel>();
	
	public AlertListView() {
		// Add a date column to show the creation date of the alert.
		DateCell dateCell = new DateCell();
		Column<AlertModel, Date> dateColumn = new Column<AlertModel, Date>(dateCell) {
			@Override
			public Date getValue(AlertModel object) {
				return object.getDate();
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
	    alertTable.setRowData(0, alerts);
	    
	    viewContent.add(alertTable);
	    
	    centerContent.add(viewContent);
	}
}
