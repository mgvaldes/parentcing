package com.ing3nia.parentalcontrol.client.views;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.google.gwt.cell.client.ButtonCell;
import com.google.gwt.cell.client.DateCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.view.client.ListDataProvider;
import com.ing3nia.parentalcontrol.client.views.models.AlertModel;
import com.ing3nia.parentalcontrol.client.views.models.RuleModel;

public class RuleListView {
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
	private List<RuleModel> rules;
	
	private final String dateNow =  "16/11/2011 - 14:13:11 PM";
	
	/**
	 * Table where the alerts are displayed.
	 */
	private CellTable<RuleModel> ruleTable;
	
	public RuleListView(HTMLPanel centerContent) {
		this.centerContent = centerContent;
		viewContent = new HTMLPanel("");
		this.ruleTable = new CellTable<RuleModel>(10);
		this.viewContent = new HTMLPanel("");
		this.rules = new ArrayList<RuleModel>();
		this.centerContent.clear();
		
		addTestRules();
	}
	
	public void addTestRules() {
		Date now = new Date();//Calendar.getInstance().getTime();
		
		RuleModel rule = new RuleModel(now, now, "SMS - Send/Receive");
		rules.add(rule);
		
		rule = new RuleModel(now, now, "Calls - Send/Receive");
		rules.add(rule);
		
		rule = new RuleModel(now, now, "Web Navigation");
		rules.add(rule);
		
		rule = new RuleModel(now, now, "Speed Limit - 5 MPH");
		rules.add(rule);
	}
	
	public void initRuleListView() {
		//final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy - KK:mm:ss a");
	    
	    // Add a date column to show the creation date of the alert.
		TextColumn<RuleModel> startDateColumn = new TextColumn<RuleModel>() {
			@Override
			public String getValue(RuleModel object) {
				return dateNow;
				//return formatter.format(object.getStartDate());
			}
		};
		
		ruleTable.addColumn(startDateColumn, "Start Date");
		
		// Add a date column to show the creation date of the alert.
		TextColumn<RuleModel> endDateColumn = new TextColumn<RuleModel>() {
			@Override
			public String getValue(RuleModel object) {
				return dateNow;
				//return formatter.format(object.getEndDate());
			}
		};
		
		ruleTable.addColumn(endDateColumn, "End Date");
	    
	    // Add a text column to show the rule name.
		TextColumn<RuleModel> ruleNameColumn = new TextColumn<RuleModel>() {
			@Override
			public String getValue(RuleModel object) {
				return object.getRule();
			}
		};
		
		ruleTable.addColumn(ruleNameColumn, "Rule");
		
		// Add an edit column to show the edit button.
		ButtonCell editCell = new ButtonCell();
		Column<RuleModel, String> editColumn = new Column<RuleModel, String>(
				editCell) {
			@Override
			public String getValue(RuleModel object) {
				return "Edit";
			}
		};

		editColumn.setFieldUpdater(new FieldUpdater<RuleModel, String>() {
			@Override
			public void update(int index, RuleModel object, String value) {
				// The user clicked on the edit button.
				editRule();
			}
		});
		

		ruleTable.addColumn(editColumn, "");
		
		// Set the total row count. This isn't strictly necessary, but it
		// affects paging calculations, so its good habit to keep the row 
	    // count up to date.
	    ruleTable.setRowCount(rules.size(), true);

		// Push the data into the widget.
		ListDataProvider<RuleModel> dataProvider = new ListDataProvider<RuleModel>(rules);
		dataProvider.addDataDisplay(ruleTable);
		
		//creating paging controls
		SimplePager pager = new SimplePager();
		SimplePager pager2 = new SimplePager();
		pager.setDisplay(ruleTable);
		pager2.setDisplay(ruleTable);
		
		pager2.setStylePrimaryName("tablePager");
		pager2.setStyleName("");
		viewContent.add(pager2);
		viewContent.add(ruleTable);
		pager.setStylePrimaryName("tablePager");
		pager.setStyleName("");
		viewContent.add(pager);		
		
		centerContent.add(viewContent);
	}
	
	public void editRule() {
		
	}
}
