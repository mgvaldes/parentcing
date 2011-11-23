package com.ing3nia.parentalcontrol.client.views;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.cell.client.ButtonCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.dom.client.TableRowElement;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.RowStyles;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HTMLTable.RowFormatter;
import com.google.gwt.view.client.ListDataProvider;
import com.ing3nia.parentalcontrol.client.models.AdminUserModel;
import com.ing3nia.parentalcontrol.client.views.classnames.AdminUserListViewClassName;

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
	private HTMLPanel viewContent;// = new HTMLPanel("");
	
	/**
	 * List of admin users registered.
	 */
	private List<AdminUserModel> adminUsers;// = new ArrayList<AdminUserModel>();
	
	/**
	 * Table where the admin users are displayed.
	 */
	private CellTable<AdminUserModel> adminUserTable;// = new CellTable<AdminUserModel>(10);
	

	public AdminUserListView(HTMLPanel centerContent) {
		this.centerContent = centerContent;
		this.adminUserTable = new CellTable<AdminUserModel>(10);
		this.adminUserTable.setStyleName(AdminUserListViewClassName.AdminUserTable.getClassname());
		
		this.adminUserTable.setStylePrimaryName(AdminUserListViewClassName.AdminUserTable.getClassname());
		
		this.viewContent = new HTMLPanel("");
		this.adminUsers = new ArrayList<AdminUserModel>();
		this.centerContent.clear();
		addTestUsers();
	}
	
	private void addTestUsers(){
		AdminUserModel user = new AdminUserModel("Ingrid");
		adminUsers.add(user);
		
		user = new AdminUserModel("Dorothea");
		adminUsers.add(user);
		
		user = new AdminUserModel("Edward");
		adminUsers.add(user);
		
		user = new AdminUserModel("Roderich");
		adminUsers.add(user);
		
		user = new AdminUserModel("Sabine");
		adminUsers.add(user);
		
		user = new AdminUserModel("Bernd");
		adminUsers.add(user);
		
		user = new AdminUserModel("Ira");
		adminUsers.add(user);
		
		user = new AdminUserModel("Mandfred");
		adminUsers.add(user);
		
		user = new AdminUserModel("Roderich");
		adminUsers.add(user);
		
		user = new AdminUserModel("Sabine");
		adminUsers.add(user);
		
		user = new AdminUserModel("Bernd");
		adminUsers.add(user);
		
		user = new AdminUserModel("Ira");
		adminUsers.add(user);
		
		user = new AdminUserModel("Mandfred");
		adminUsers.add(user);
		
		user = new AdminUserModel("Roderich");
		adminUsers.add(user);
		
		user = new AdminUserModel("Sabine");
		adminUsers.add(user);
		
		user = new AdminUserModel("Bernd");
		adminUsers.add(user);
		
		user = new AdminUserModel("Ira");
		adminUsers.add(user);
		
		user = new AdminUserModel("Mandfred");
		adminUsers.add(user);
		user = new AdminUserModel("Roderich");
		adminUsers.add(user);
		
		user = new AdminUserModel("Sabine");
		adminUsers.add(user);
		
		user = new AdminUserModel("Bernd");
		adminUsers.add(user);
		
		user = new AdminUserModel("Ira");
		adminUsers.add(user);
		
		user = new AdminUserModel("Mandfred");
		adminUsers.add(user);
	}
	
	public void initAdminUserListView() {

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
		Column<AdminUserModel, String> editColumn = new Column<AdminUserModel, String>(
				editCell) {
			@Override
			public String getValue(AdminUserModel object) {
				return "Edit";
			}
		};

		editColumn.setFieldUpdater(new FieldUpdater<AdminUserModel, String>() {
			@Override
			public void update(int index, AdminUserModel object, String value) {
				// The user clicked on the edit button.
				Window.alert("Editing "+object.getUsername());
			}
		});
		

		adminUserTable.addColumn(editColumn, "");

		// Set the total row count. This isn't strictly necessary, but it
		// affects paging calculations, so its good habit to keep the row
		// count up to date.
		adminUserTable.setRowCount(adminUsers.size(), true);

		// Push the data into the widget.
		//adminUserTable.setRowData(0, adminUsers);
		ListDataProvider<AdminUserModel> dataProvider = new ListDataProvider<AdminUserModel>(adminUsers);
		dataProvider.addDataDisplay(adminUserTable);
		
		//creating paging controls
		SimplePager pager = new SimplePager();
		SimplePager pager2 = new SimplePager();
		pager.setDisplay(adminUserTable);
		pager2.setDisplay(adminUserTable);
		
		pager2.setStylePrimaryName("tablePager");
		pager2.setStyleName("");
		viewContent.add(pager2);
		viewContent.add(adminUserTable);
		pager.setStylePrimaryName("tablePager");
		pager.setStyleName("");
		viewContent.add(pager);
		
		
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
