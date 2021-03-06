package com.ing3nia.parentalcontrol.client.views;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.cell.client.ButtonCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.view.client.ListDataProvider;
import com.ing3nia.parentalcontrol.client.rpc.DeleteRuleService;
import com.ing3nia.parentalcontrol.client.rpc.DeleteRuleServiceAsync;
import com.ing3nia.parentalcontrol.client.views.async.DeleteRuleCallbackHandler;
import com.ing3nia.parentalcontrol.client.views.classnames.PCTableViewClassNames;
import com.ing3nia.parentalcontrol.client.views.subviews.EditRuleView;
import com.ing3nia.parentalcontrol.client.views.subviews.NewRuleView;

import com.ing3nia.parentalcontrol.client.handlers.BaseViewHandler;
import com.ing3nia.parentalcontrol.client.handlers.click.innerbutton.AddNewRuleClickHandler;
import com.ing3nia.parentalcontrol.client.models.RuleModel;
import com.ing3nia.parentalcontrol.client.models.SmartphoneModel;

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
	
	/**
	 * Table where the alerts are displayed.
	 */
	private CellTable<RuleModel> ruleTable;
	
	private SimplePager pager;
	
	private Button addRuleButton;
	
	private BaseViewHandler baseViewHandler;
	public static String VIEW_CONTENT_CLASSNAME = "ruleListContent";
	
	public RuleListView(BaseViewHandler baseViewHandler, ArrayList<RuleModel> ruleList) {
		this.baseViewHandler = baseViewHandler;
		this.centerContent = baseViewHandler.getBaseBinder().getCenterContent();
		this.centerContent.setStyleName("centerContent");
		this.viewContent = new HTMLPanel("");
		this.viewContent.setStyleName(VIEW_CONTENT_CLASSNAME);
		this.ruleTable = new CellTable<RuleModel>(10);
		this.rules = ruleList;
		this.centerContent.clear();
		this.rules = ruleList;
		this.addRuleButton = new Button("Add Rule");
		DOM.setElementProperty(addRuleButton.getElement(), "id", "addRuleButton");
		this.addRuleButton.setStyleName("addRuleButton");
		
		this.pager = new SimplePager();
	}
	
	public void initRuleListView() {
		AddNewRuleClickHandler addRuleClickHandler = new AddNewRuleClickHandler(this, baseViewHandler);
		addRuleButton.addClickHandler(addRuleClickHandler);
		viewContent.add(addRuleButton);
		
		//Setting alert table style
		ruleTable.setStyleName(PCTableViewClassNames.EXTENDED_TABLE.getClassname());
		
	    // Add a date column to show the creation date of the alert.
		TextColumn<RuleModel> startDateColumn = new TextColumn<RuleModel>() {
			@Override
			public String getValue(RuleModel object) {
				DateTimeFormat formatter = DateTimeFormat.getFormat("dd/MM/yyyy hh:mm:ss a");
				Date startDate = formatter.parse(object.getStartDate());
				DateTimeFormat formatterMonth = DateTimeFormat.getFormat("dd/MM/yyyy hh:mm:ss a");
				return formatterMonth.format(startDate);
			}
		};
		
		ruleTable.addColumn(startDateColumn, "Start Date");
		
		// Add a date column to show the creation date of the alert.
		TextColumn<RuleModel> endDateColumn = new TextColumn<RuleModel>() {
			@Override
			public String getValue(RuleModel object) {
				DateTimeFormat formatter = DateTimeFormat.getFormat("dd/MM/yyyy hh:mm:ss a");
				Date endDate = formatter.parse(object.getEndDate());
				DateTimeFormat formatterMonth = DateTimeFormat.getFormat("dd/MM/yyyy hh:mm:ss a");
				return formatterMonth.format(endDate);
				//return 
			}
		};
		
		ruleTable.addColumn(endDateColumn, "End Date");
	    
	    // Add a text column to show the rule name.
		TextColumn<RuleModel> ruleNameColumn = new TextColumn<RuleModel>() {
			@Override
			public String getValue(RuleModel object) {
				return object.getName();
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
				editRule(object);
			}
		});
		
		ruleTable.addColumn(editColumn, "");
		
		// Add an delete column to show the delete button.
		ButtonCell deleteCell = new ButtonCell();
		Column<RuleModel, String> deleteColumn = new Column<RuleModel, String>(
				deleteCell) {
			@Override
			public String getValue(RuleModel object) {
				return "Delete";
			}
		};

		deleteColumn.setFieldUpdater(new FieldUpdater<RuleModel, String>() {
			@Override
			public void update(int index, RuleModel object, String value) {
				// The user clicked on the edit button.
				deleteRule(object);
			}
		});
		
		ruleTable.addColumn(deleteColumn, "");
		
		// Set the total row count. This isn't strictly necessary, but it
		// affects paging calculations, so its good habit to keep the row 
	    // count up to date.
	    ruleTable.setRowCount(rules.size(), true);

		// Push the data into the widget.
		ListDataProvider<RuleModel> dataProvider = new ListDataProvider<RuleModel>(rules);
		dataProvider.addDataDisplay(ruleTable);
		
		//creating paging controls
		pager.setDisplay(ruleTable);
		pager.setStylePrimaryName("tablePager");
		pager.setStyleName("");
		
		viewContent.add(pager);
		viewContent.add(ruleTable);
		viewContent.add(pager);	
		centerContent.add(viewContent);
	}
	
	//public void loadNewRule() {
	public void loadAddRuleView() {

		baseViewHandler.getBaseBinder().getCenterContent().clear();
		SmartphoneModel selectedSmart = baseViewHandler.getUser().getSmartphones().get(baseViewHandler.getClickedSmartphoneIndex());
		
		NewRuleView addRuleView = new NewRuleView(baseViewHandler, baseViewHandler.getUser().getCid(), selectedSmart);
		addRuleView.initNewRuleView();
	}
	
	public void editRule(RuleModel editRule) {
		baseViewHandler.getBaseBinder().getCenterContent().clear();
		
		SmartphoneModel smart = this.baseViewHandler.getUser().getSmartphones().get(this.baseViewHandler.getClickedSmartphoneIndex()); 
		
		EditRuleView editView = new EditRuleView(this.baseViewHandler, this.baseViewHandler.getUser().getCid(), smart, editRule);
		editView.initEditRuleView();
	}
	
	public void deleteRule(RuleModel rule) {
		SmartphoneModel smart = this.baseViewHandler.getUser().getSmartphones().get(this.baseViewHandler.getClickedSmartphoneIndex());
		
		DeleteRuleCallbackHandler deleteRuleCallback = new DeleteRuleCallbackHandler(this.baseViewHandler, rule, this.baseViewHandler.getUser().getCid(), smart.getKeyId());
		DeleteRuleServiceAsync deleteRuleService = GWT.create(DeleteRuleService.class);
		deleteRuleService.deleteRule(rule.getKeyId(), smart.getKeyId(), deleteRuleCallback);
	}
}
