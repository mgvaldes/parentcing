package com.ing3nia.parentalcontrol.client.views;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.cell.client.ButtonCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.view.client.ListDataProvider;
import com.ing3nia.parentalcontrol.client.handlers.BaseViewHandler;
import com.ing3nia.parentalcontrol.client.handlers.click.innerbutton.EditAdminUserHandler;
import com.ing3nia.parentalcontrol.client.models.ClientAdminUserModel;
import com.ing3nia.parentalcontrol.client.models.ClientAdminUserModel;
import com.ing3nia.parentalcontrol.client.views.classnames.AdminUserListViewClassName;
import com.ing3nia.parentalcontrol.client.views.classnames.CenterMenuOptionsClassNames;
import com.ing3nia.parentalcontrol.client.views.classnames.PCTableViewClassNames;


public class AdminUserListView {
	
	
	private BaseViewHandler baseViewHandler;
	
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
	 * Center menu options panel
	 */
	private FlowPanel centerMenuOptions;
	
	/**
	 * Add User button in center menu options panel
	 */
	private Button addUserButton;
	
	/**
	 * Add User button in center menu options panel
	 */
	private Button addUserList;

	/**
	 * List of admin users registered.
	 */
	private List<ClientAdminUserModel> adminUsers;// = new ArrayList<ClientAdminUserModel>();
	
	/**
	 * Table where the admin users are displayed.
	 */
	private CellTable<ClientAdminUserModel> adminUserTable;// = new CellTable<ClientAdminUserModel>(10);
	

	public AdminUserListView(BaseViewHandler baseViewHandler, ArrayList<ClientAdminUserModel> adminUsers) {
		this.baseViewHandler = baseViewHandler;		
		this.centerContent = baseViewHandler.getBaseBinder().getCenterContent();

		this.adminUserTable = new CellTable<ClientAdminUserModel>(10);
		this.adminUserTable.setStyleName(AdminUserListViewClassName.AdminUserTable.getClassname());
		
		this.adminUserTable.setStylePrimaryName(AdminUserListViewClassName.AdminUserTable.getClassname());
		
		this.viewContent = new HTMLPanel("");
		this.adminUsers = adminUsers;
	}
	
	public void initAdminUserListView() {
		
		BaseViewHandler.clearOthersStyle(CenterMenuOptionsClassNames.UserList,
				baseViewHandler.getMenuSetter().getCenterMenuOptions());
		this.baseViewHandler.getMenuSetter().getUserList().setStyleName("selectedShinnyButton");
		
		//Setting alert table style
		adminUserTable.setStyleName(PCTableViewClassNames.EXTENDED_TABLE.getClassname());
		
		//initializing content
		this.centerContent.clear();
		this.centerContent.setStyleName("centerContent");
		
		// Add a text column to show the username.
		TextColumn<ClientAdminUserModel> usernameColumn = new TextColumn<ClientAdminUserModel>() {
			@Override
			public String getValue(ClientAdminUserModel object) {
				return object.getUsername();
			}
		};

		adminUserTable.addColumn(usernameColumn, "Username");

		// Add an edit column to show the edit button.
		ButtonCell editCell = new ButtonCell();
		Column<ClientAdminUserModel, String> editColumn = new Column<ClientAdminUserModel, String>(
				editCell) {
			@Override
			public String getValue(ClientAdminUserModel object) {
				return "Edit";
			}
		};

		EditAdminUserHandler editAdminHandler = new EditAdminUserHandler(centerContent, baseViewHandler, baseViewHandler.getUser());
		editColumn.setFieldUpdater(editAdminHandler);		

		adminUserTable.addColumn(editColumn, "");

		// Set the total row count. This isn't strictly necessary, but it
		// affects paging calculations, so its good habit to keep the row
		// count up to date.
		adminUserTable.setRowCount(adminUsers.size(), true);

		// Push the data into the widget.
		//adminUserTable.setRowData(0, adminUsers);
		ListDataProvider<ClientAdminUserModel> dataProvider = new ListDataProvider<ClientAdminUserModel>(adminUsers);
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

	public List<ClientAdminUserModel> getAdminUsers() {
		return adminUsers;
	}

	public void setAdminUsers(List<ClientAdminUserModel> adminUsers) {
		this.adminUsers = adminUsers;
	}

	public CellTable<ClientAdminUserModel> getAdminUserTable() {
		return adminUserTable;
	}

	public void setAdminUserTable(CellTable<ClientAdminUserModel> adminUserTable) {
		this.adminUserTable = adminUserTable;
	}
}
