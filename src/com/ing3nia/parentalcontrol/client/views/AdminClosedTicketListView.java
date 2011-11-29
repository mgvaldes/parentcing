package com.ing3nia.parentalcontrol.client.views;

import java.util.ArrayList;
import java.util.Date;

import com.google.gwt.cell.client.ButtonCell;
import com.google.gwt.cell.client.DateCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.ing3nia.parentalcontrol.client.models.TicketModel;
import com.ing3nia.parentalcontrol.client.views.classnames.PCTableViewClassNames;

public class AdminClosedTicketListView {
	/**
	 * Center Panel containing all the widgets of the 
	 * admin user list view.
	 */
	private HTMLPanel centerContent;
	
	/**
	 * Main panel of open tickets list view that groups all 
	 * the widgets together.
	 */
	private HTMLPanel closedTicketsViewContent = new HTMLPanel("");
	
	/**
	 * List of open tickets.
	 */
	private ArrayList<TicketModel> closedTickets;
	
	/**
	 * Open tickets label.
	 */
	private Label closedTicketsLabel = new Label("Closed Tickets");
	
	/**
	 * Table where the open tickets are displayed.
	 */
	private CellTable<TicketModel> closedTicketsTable = new CellTable<TicketModel>();
	
	public AdminClosedTicketListView(HTMLPanel centerContent, ArrayList<TicketModel> closedTickets) {
		
		//Setting alert table style
		closedTicketsTable.setStyleName(PCTableViewClassNames.EXTENDED_TABLE.getClassname());
		
		this.centerContent =  centerContent;
		this.centerContent.setStyleName("centerContent");
		this.closedTickets = closedTickets;
		
		// Setting up open tickets table 
		
		// Add a text column to show the id.
		TextColumn<TicketModel> idColumn = new TextColumn<TicketModel>() {
			@Override
			public String getValue(TicketModel object) {
				return String.valueOf(object.getId());
			}
		};
		
		closedTicketsTable.addColumn(idColumn, "ID");
		
		// Add a text column to show the category.
		TextColumn<TicketModel> categoryColumn = new TextColumn<TicketModel>() {
			@Override
			public String getValue(TicketModel object) {
				return object.getCategory();
			}
		};
		
		closedTicketsTable.addColumn(categoryColumn, "Category");
		
		// Add a text column to show the subject.
		TextColumn<TicketModel> subjectColumn = new TextColumn<TicketModel>() {
			@Override
			public String getValue(TicketModel object) {
				return object.getSubject();
			}
		};
		
		closedTicketsTable.addColumn(subjectColumn, "Subject");
		
		// Add a text column to show the subject.
		TextColumn<TicketModel> nameColumn = new TextColumn<TicketModel>() {
			@Override
			public String getValue(TicketModel object) {
				return object.getName();
			}
		};
		
		closedTicketsTable.addColumn(nameColumn, "Name / Last Name");
		
		// Add a date column to show the creation date of the ticket.
		DateCell dateCell = new DateCell();
		Column<TicketModel, Date> dateColumn = new Column<TicketModel, Date>(dateCell) {
			@Override
			public Date getValue(TicketModel object) {
				return object.getDate();
			}
		};
	    
		closedTicketsTable.addColumn(dateColumn, "Date");
		
		// Add an view column to show the view button.
	    ButtonCell viewCell = new ButtonCell();
	    Column<TicketModel, String> viewColumn = new Column<TicketModel, String>(viewCell) {
	    	@Override
	    	public String getValue(TicketModel object) {
	    		return "View";
	    	}
	    };
	    
		viewColumn.setFieldUpdater(new FieldUpdater<TicketModel, String>() {
			@Override
			public void update(int index, TicketModel object, String value) {
				// The user clicked on the view button.
			}
		});
	    
		closedTicketsTable.addColumn(viewColumn, "");
	    
	    // Add an view column to show the view button.
	    ButtonCell closeCell = new ButtonCell();
	    Column<TicketModel, String> closeColumn = new Column<TicketModel, String>(closeCell) {
	    	@Override
	    	public String getValue(TicketModel object) {
	    		return "Close";
	    	}
	    };
	    
	    closeColumn.setFieldUpdater(new FieldUpdater<TicketModel, String>() {
			@Override
			public void update(int index, TicketModel object, String value) {
				closeTicket(object.getKey());
			}
		});
	    
	    closedTicketsTable.addColumn(closeColumn, "");
	    
		// Set the total row count. This isn't strictly necessary, but it
		// affects paging calculations, so its good habit to keep the row 
	    // count up to date.
	    closedTicketsTable.setRowCount(closedTickets.size(), true);

		// Push the data into the widget.
	    closedTicketsTable.setRowData(0, closedTickets);
	    
	    closedTicketsViewContent.add(closedTicketsLabel);
	    
	    closedTicketsViewContent.add(closedTicketsTable);
	    
	    centerContent.add(closedTicketsViewContent);
	}
	
	public void closeTicket(String tiketKey) {
		
	}
}
