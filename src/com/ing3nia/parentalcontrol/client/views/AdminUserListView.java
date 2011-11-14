package com.ing3nia.parentalcontrol.client.views;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.cell.client.ButtonCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.ing3nia.parentalcontrol.client.views.models.AdminUserModel;

public class AdminUserListView {
	/**
	 * Center Panel containing all the widgets of the 
	 * admin user list view.
	 */
	private HTMLPanel centerContent;
	
	/**
	 * Main panel of admin user list view that groups all 
	 * the widgets together.
	 */
	private HTMLPanel viewContent = new HTMLPanel("");
	
	/**
	 * List of admin users registered.
	 */
	private List<AdminUserModel> adminUsers = new ArrayList<AdminUserModel>();
	
	/**
	 * Table where the admin users are displayed.
	 */
	private CellTable<AdminUserModel> adminUserTable = new CellTable<AdminUserModel>();
	
	public AdminUserListView() {
		// Add a text column to show the username.
		TextColumn<AdminUserModel> usernameColumn = new TextColumn<AdminUserModel>() {
			@Override
			public String getValue(AdminUserModel object) {
				return object.getUsername();
			}
		};
		
		adminUserTable.addColumn(usernameColumn, "Username");
		
		// Add an edit column to show the edit button.
	    ButtonCell editCell = new ButtonCell();
	    Column<AdminUserModel, String> editColumn = new Column<AdminUserModel, String>(editCell) {
	    	@Override
	    	public String getValue(AdminUserModel object) {
	    		return object.getUsername();
	    	}
	    };
	    
		editColumn.setFieldUpdater(new FieldUpdater<AdminUserModel, String>() {
			@Override
			public void update(int index, AdminUserModel object, String value) {
				// The user clicked on the edit button.
			}
		});
	    
	    adminUserTable.addColumn(editColumn, "");
	    
		// Set the total row count. This isn't strictly necessary, but it
		// affects paging calculations, so its good habit to keep the row 
	    // count up to date.
	    adminUserTable.setRowCount(adminUsers.size(), true);

		// Push the data into the widget.
	    adminUserTable.setRowData(0, adminUsers);
	    
	    viewContent.add(adminUserTable);
	    
	    centerContent.add(viewContent);
	}

	public HTMLPanel getCenterContent() {
		return centerContent;
	}

	public void setCenterContent(HTMLPanel centerContent) {
		this.centerContent = centerContent;
	}

	public HTMLPanel getViewContent() {
		return viewContent;
	}

	public void setViewContent(HTMLPanel viewContent) {
		this.viewContent = viewContent;
	}

	public List<AdminUserModel> getAdminUsers() {
		return adminUsers;
	}

	public void setAdminUsers(List<AdminUserModel> adminUsers) {
		this.adminUsers = adminUsers;
	}

	public CellTable<AdminUserModel> getAdminUserTable() {
		return adminUserTable;
	}

	public void setAdminUserTable(CellTable<AdminUserModel> adminUserTable) {
		this.adminUserTable = adminUserTable;
	}
}
