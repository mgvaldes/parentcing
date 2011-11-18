package com.ing3nia.parentalcontrol.client.views;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.cell.client.ButtonCell;
import com.google.gwt.cell.client.DateCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.ing3nia.parentalcontrol.client.views.models.AdminUserModel;
import com.ing3nia.parentalcontrol.client.views.models.AlertModel;
import com.ing3nia.parentalcontrol.client.views.models.TicketModel;

public class TicketListView {
	/**
	 * Center Panel containing all the widgets of the 
	 * admin user list view.
	 */
	private HTMLPanel centerContent;
	
	/**
	 * Main panel of open tickets list view that groups all 
	 * the widgets together.
	 */
	private HTMLPanel openTicketsViewContent = new HTMLPanel("");
	
	/**
	 * Main panel of open tickets list view that groups all 
	 * the widgets together.
	 */
	private HTMLPanel closedTicketsViewContent = new HTMLPanel("");
	
	/**
	 * List of open tickets.
	 */
	private List<TicketModel> openTickets = new ArrayList<TicketModel>();
	
	/**
	 * Open tickets label.
	 */
	private Label openTicketsLabel = new Label("Open Tickets");
	
	/**
	 * Table where the open tickets are displayed.
	 */
	private CellTable<TicketModel> openTicketsTable = new CellTable<TicketModel>();
	
	/**
	 * List of closed tickets.
	 */
	private List<TicketModel> closedTickets = new ArrayList<TicketModel>();
	
	/**
	 * Closed tickets label.
	 */
	private Label closedTicketsLabel = new Label("Closed Tickets");
	
	/**
	 * Table where the closed tickets are displayed.
	 */
	private CellTable<TicketModel> closedTicketsTable = new CellTable<TicketModel>();
	
	public TicketListView(HTMLPanel centerContent) {
		this.centerContent =  centerContent;
		
		// Setting up open tickets table 
		
		// Add a text column to show the id.
		TextColumn<TicketModel> idColumn = new TextColumn<TicketModel>() {
			@Override
			public String getValue(TicketModel object) {
				return String.valueOf(object.getId());
			}
		};
		
		openTicketsTable.addColumn(idColumn, "ID");
		
		// Add a text column to show the category.
		TextColumn<TicketModel> categoryColumn = new TextColumn<TicketModel>() {
			@Override
			public String getValue(TicketModel object) {
				return object.getCategory();
			}
		};
		
		openTicketsTable.addColumn(categoryColumn, "Category");
		
		// Add a text column to show the subject.
		TextColumn<TicketModel> subjectColumn = new TextColumn<TicketModel>() {
			@Override
			public String getValue(TicketModel object) {
				return object.getSubject();
			}
		};
		
		openTicketsTable.addColumn(subjectColumn, "Subject");
		
		// Add a date column to show the creation date of the ticket.
		DateCell dateCell = new DateCell();
		Column<TicketModel, Date> dateColumn = new Column<TicketModel, Date>(dateCell) {
			@Override
			public Date getValue(TicketModel object) {
				return object.getDate();
			}
		};
	    
	    openTicketsTable.addColumn(dateColumn, "Date");
		
		// Add an view column to show the view button.
	    ButtonCell viewCell = new ButtonCell();
	    Column<TicketModel, String> viewColumn = new Column<TicketModel, String>(viewCell) {
	    	@Override
	    	public String getValue(TicketModel object) {
	    		return object.getKey();
	    	}
	    };
	    
		viewColumn.setFieldUpdater(new FieldUpdater<TicketModel, String>() {
			@Override
			public void update(int index, TicketModel object, String value) {
				// The user clicked on the view button.
			}
		});
	    
	    openTicketsTable.addColumn(viewColumn, "");
	    
	    // Add an view column to show the view button.
	    ButtonCell closeCell = new ButtonCell();
	    Column<TicketModel, String> closeColumn = new Column<TicketModel, String>(closeCell) {
	    	@Override
	    	public String getValue(TicketModel object) {
	    		return object.getKey();
	    	}
	    };
	    
	    closeColumn.setFieldUpdater(new FieldUpdater<TicketModel, String>() {
			@Override
			public void update(int index, TicketModel object, String value) {
				// The user clicked on the view button.
			}
		});
	    
	    openTicketsTable.addColumn(closeColumn, "");
	    
		// Set the total row count. This isn't strictly necessary, but it
		// affects paging calculations, so its good habit to keep the row 
	    // count up to date.
	    openTicketsTable.setRowCount(openTickets.size(), true);

		// Push the data into the widget.
	    openTicketsTable.setRowData(0, openTickets);
	    
	    openTicketsViewContent.add(openTicketsLabel);
	    
	    openTicketsViewContent.add(openTicketsTable);
	    
	    centerContent.add(openTicketsViewContent);
	    
		// Setting up closed tickets table 
		
		// Add a text column to show the id.
		TextColumn<TicketModel> idColumnC = new TextColumn<TicketModel>() {
			@Override
			public String getValue(TicketModel object) {
				return String.valueOf(object.getId());
			}
		};
		
		closedTicketsTable.addColumn(idColumnC, "ID");
		
		// Add a text column to show the category.
		TextColumn<TicketModel> categoryColumnC = new TextColumn<TicketModel>() {
			@Override
			public String getValue(TicketModel object) {
				return object.getCategory();
			}
		};
		
		closedTicketsTable.addColumn(categoryColumnC, "Category");
		
		// Add a text column to show the subject.
		TextColumn<TicketModel> subjectColumnC = new TextColumn<TicketModel>() {
			@Override
			public String getValue(TicketModel object) {
				return object.getSubject();
			}
		};
		
		closedTicketsTable.addColumn(subjectColumnC, "Subject");
		
		// Add a date column to show the creation date of the ticket.
		DateCell dateCellC = new DateCell();
		Column<TicketModel, Date> dateColumnC = new Column<TicketModel, Date>(dateCellC) {
			@Override
			public Date getValue(TicketModel object) {
				return object.getDate();
			}
		};
	    
		closedTicketsTable.addColumn(dateColumnC, "Date");
		
		// Add an view column to show the view button.
	    ButtonCell viewCellC = new ButtonCell();
	    Column<TicketModel, String> viewColumnC = new Column<TicketModel, String>(viewCellC) {
	    	@Override
	    	public String getValue(TicketModel object) {
	    		return object.getKey();
	    	}
	    };
	    
		viewColumnC.setFieldUpdater(new FieldUpdater<TicketModel, String>() {
			@Override
			public void update(int index, TicketModel object, String value) {
				// The user clicked on the view button.
			}
		});
	    
		closedTicketsTable.addColumn(viewColumnC, "");
	    
	    // Add an view column to show the view button.
	    ButtonCell closeCellC = new ButtonCell();
	    Column<TicketModel, String> closeColumnC = new Column<TicketModel, String>(closeCellC) {
	    	@Override
	    	public String getValue(TicketModel object) {
	    		return object.getKey();
	    	}
	    };
	    
	    closeColumnC.setFieldUpdater(new FieldUpdater<TicketModel, String>() {
			@Override
			public void update(int index, TicketModel object, String value) {
				// The user clicked on the view button.
			}
		});
	    
	    closedTicketsTable.addColumn(closeColumnC, "");
	    
		// Set the total row count. This isn't strictly necessary, but it
		// affects paging calculations, so its good habit to keep the row 
	    // count up to date.
	    closedTicketsTable.setRowCount(closedTickets.size(), true);

		// Push the data into the widget.
	    closedTicketsTable.setRowData(0, closedTickets);
	    
	    closedTicketsViewContent.add(closedTicketsLabel);
	    
	    closedTicketsViewContent.add(closedTicketsTable);
	    
	}
	
	public void initTicketList(){
	    centerContent.add(closedTicketsViewContent);
	}

	public HTMLPanel getCenterContent() {
		return centerContent;
	}

	public void setCenterContent(HTMLPanel centerContent) {
		this.centerContent = centerContent;
	}

	public HTMLPanel getOpenTicketsViewContent() {
		return openTicketsViewContent;
	}

	public void setOpenTicketsViewContent(HTMLPanel openTicketsViewContent) {
		this.openTicketsViewContent = openTicketsViewContent;
	}

	public HTMLPanel getClosedTicketsViewContent() {
		return closedTicketsViewContent;
	}

	public void setClosedTicketsViewContent(HTMLPanel closedTicketsViewContent) {
		this.closedTicketsViewContent = closedTicketsViewContent;
	}

	public Label getOpenTicketsLabel() {
		return openTicketsLabel;
	}

	public void setOpenTicketsLabel(Label openTicketsLabel) {
		this.openTicketsLabel = openTicketsLabel;
	}

	public Label getClosedTicketsLabel() {
		return closedTicketsLabel;
	}

	public void setClosedTicketsLabel(Label closedTicketsLabel) {
		this.closedTicketsLabel = closedTicketsLabel;
	}

	public List<TicketModel> getOpenTickets() {
		return openTickets;
	}

	public void setOpenTickets(List<TicketModel> openTickets) {
		this.openTickets = openTickets;
	}

	public CellTable<TicketModel> getOpenTicketsTable() {
		return openTicketsTable;
	}

	public void setOpenTicketsTable(CellTable<TicketModel> openTicketsTable) {
		this.openTicketsTable = openTicketsTable;
	}

	public List<TicketModel> getClosedTickets() {
		return closedTickets;
	}

	public void setClosedTickets(List<TicketModel> closedTickets) {
		this.closedTickets = closedTickets;
	}

	public CellTable<TicketModel> getClosedTicketsTable() {
		return closedTicketsTable;
	}

	public void setClosedTicketsTable(CellTable<TicketModel> closedTicketsTable) {
		this.closedTicketsTable = closedTicketsTable;
	}
}
