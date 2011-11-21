package com.ing3nia.parentalcontrol.client.views;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.cell.client.ButtonCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.dom.client.TableRowElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.view.client.ListDataProvider;
import com.ing3nia.parentalcontrol.client.models.ModificationModel;
import com.ing3nia.parentalcontrol.client.views.models.ContactModel;

public class DeviceContactListView {
	/**
	 * Center Panel containing all the widgets of the 
	 * device contact list view.
	 */
	private HTMLPanel centerContent;
	
	/**
	 * Main panel of device contact list view that groups all 
	 * the widgets together.
	 */
	private HTMLPanel viewContent;
	
	/**
	 * Contacts label.
	 */
	private Label contactsLabel;
	
	/**
	 * Panel containing contacts and emergency contacts buttons.
	 */
	private HTMLPanel contactButtonsPanel;
	
	/**
	 * Contacts button.
	 */
	private Button contactsButton;
	
	/**
	 * Emergency contacts button.
	 */
	private Button emergencyContactsButton;
	
	/**
	 * List of contacts of device.
	 */
	private List<ContactModel> contacts;
	
	private List<ContactModel> activeContacts;
	
	private List<ContactModel> inactiveContacts;
	
	/**
	 * Table where the alerts are displayed.
	 */
	private CellTable<ContactModel> contactTable = new CellTable<ContactModel>();
	
	private SimplePager pager;
	
	private Button saveButton;
	
	public DeviceContactListView(HTMLPanel centerContent, ArrayList<ContactModel> activeContacts, ArrayList<ContactModel> inactiveContacts) {
		this.contacts = new ArrayList<ContactModel>();
		this.contacts.addAll(activeContacts);
		this.contacts.addAll(inactiveContacts);
		this.activeContacts = new ArrayList<ContactModel>();
		this.inactiveContacts = new ArrayList<ContactModel>();
		this.centerContent = centerContent;		
		this.viewContent = new HTMLPanel("");
		this.contactsLabel = new Label("Contacts:");
		this.contactButtonsPanel = new HTMLPanel("");
		this.contactsButton = new Button("Contacts");
		this.emergencyContactsButton = new Button("Emergency Contacts");
		this.contactTable = new CellTable<ContactModel>(10);
		this.pager = new SimplePager();
		this.contacts = new ArrayList<ContactModel>();
		this.saveButton = new Button("Save");
		this.centerContent.clear();
		
		addTestDeviceContacts();
		initDeviceContactListView();
	}
	
	public void addTestDeviceContacts() {
		
	}
	
	public void initDeviceContactListView() {
		this.viewContent.add(this.contactsLabel);
		
		contactsButton.addClickHandler(new ClickHandler() {
	    	public void onClick(ClickEvent event) {
	    		loadDeviceContacts();
	    	}
	    });
		
		contactButtonsPanel.add(contactsButton);
		
		emergencyContactsButton.addClickHandler(new ClickHandler() {
	    	public void onClick(ClickEvent event) {
	    		loadDeviceEmergencyContacts();
	    	}
	    });
		
		contactButtonsPanel.add(emergencyContactsButton);
		this.viewContent.add(contactButtonsPanel);
		
		// Add a text column to show the first name.
		TextColumn<ContactModel> firstNameColumn = new TextColumn<ContactModel>() {
			@Override
			public String getValue(ContactModel object) {
				return object.getFirstName();
			}
		};

		contactTable.addColumn(firstNameColumn, "First Name");
		
		// Add a text column to show the last name.
		TextColumn<ContactModel> lastNameColumn = new TextColumn<ContactModel>() {
			@Override
			public String getValue(ContactModel object) {
				return object.getLastName();
			}
		};

		contactTable.addColumn(lastNameColumn, "Last Name");
		
		// Add a text column to show the phone.
		TextColumn<ContactModel> phoneColumn = new TextColumn<ContactModel>() {
			@Override
			public String getValue(ContactModel object) {
				return object.getPhone();
			}
		};

		contactTable.addColumn(phoneColumn, "Phone");		

		// Add an edit column to show the disallow button.
		ButtonCell disallowCell = new ButtonCell();
		Column<ContactModel, String> disallowColumn = new Column<ContactModel, String>(disallowCell) {
			@Override
			public String getValue(ContactModel object) {
				return "Disallow";
			}
		};

		disallowColumn.setFieldUpdater(new FieldUpdater<ContactModel, String>() {
			@Override
			public void update(int index, ContactModel object, String value) {
				//Change button's style
				TableRowElement row = contactTable.getRowElement(index);
				
				//Add to inactive contacts;
				if (activeContacts.contains(object)) {
					activeContacts.remove(object);
				}
				
				inactiveContacts.add(object);
			}
		});
		

		contactTable.addColumn(disallowColumn, "Allow");
		
		// Add an edit column to show the disallow button.
		ButtonCell allowCell = new ButtonCell();
		Column<ContactModel, String> allowColumn = new Column<ContactModel, String>(allowCell) {
			@Override
			public String getValue(ContactModel object) {
				return "Allow";
			}
		};

		allowColumn.setFieldUpdater(new FieldUpdater<ContactModel, String>() {
			@Override
			public void update(int index, ContactModel object, String value) {
				//Change button's style
				TableRowElement row = contactTable.getRowElement(index);
				
				//Add to active contacts;
				if (inactiveContacts.contains(object)) {
					inactiveContacts.remove(object);
				}
				
				activeContacts.add(object);
			}
		});
		

		contactTable.addColumn(allowColumn, "Access");
		
		// Add an edit column to show the edit button.
		ButtonCell viewCell = new ButtonCell();
		Column<ContactModel, String> viewColumn = new Column<ContactModel, String>(viewCell) {
			@Override
			public String getValue(ContactModel object) {
				return "View";
			}
		};

		viewColumn.setFieldUpdater(new FieldUpdater<ContactModel, String>() {
			@Override
			public void update(int index, ContactModel object, String value) {
				
			}
		});
		

		contactTable.addColumn(viewColumn, "");

		// Set the total row count. This isn't strictly necessary, but it
		// affects paging calculations, so its good habit to keep the row
		// count up to date.
		contactTable.setRowCount(contacts.size(), true);

		// Push the data into the widget.
		//adminUserTable.setRowData(0, adminUsers);
		ListDataProvider<ContactModel> dataProvider = new ListDataProvider<ContactModel>(contacts);
		dataProvider.addDataDisplay(contactTable);
		
		//creating paging controls		
		pager.setDisplay(contactTable);
		pager.setStylePrimaryName("tablePager");
		pager.setStyleName("");
		
		viewContent.add(pager);
		viewContent.add(contactTable);		
		viewContent.add(pager);
		
		saveButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				saveContacts();
			}
		});
		
		this.centerContent.add(this.viewContent);
	}
	
	public void saveContacts() {
		if (!activeContacts.isEmpty() && !inactiveContacts.isEmpty()) {
			ModificationModel auxMod = new ModificationModel();			
		}
	}
	
	public void loadDeviceContacts() {
		
	}
	
	public void loadDeviceEmergencyContacts() {
		
	}
}
