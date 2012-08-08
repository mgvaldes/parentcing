package com.ing3nia.parentalcontrol.client.views;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.cell.client.ButtonCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.TableRowElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.view.client.ListDataProvider;
import com.ing3nia.parentalcontrol.client.handlers.BaseViewHandler;
import com.ing3nia.parentalcontrol.client.models.EmergencyNumberModel;
import com.ing3nia.parentalcontrol.client.models.ModificationModel;
import com.ing3nia.parentalcontrol.client.models.RuleModel;
import com.ing3nia.parentalcontrol.client.models.SmartphoneModel;
import com.ing3nia.parentalcontrol.client.rpc.AddEmergencyContactService;
import com.ing3nia.parentalcontrol.client.rpc.AddEmergencyContactServiceAsync;
import com.ing3nia.parentalcontrol.client.rpc.CloseTicketService;
import com.ing3nia.parentalcontrol.client.rpc.SaveSmartphoneModificationsService;
import com.ing3nia.parentalcontrol.client.rpc.SaveSmartphoneModificationsServiceAsync;
import com.ing3nia.parentalcontrol.client.utils.ModelLogger;
import com.ing3nia.parentalcontrol.client.views.async.AddEmergencyContactCallBackHandler;
import com.ing3nia.parentalcontrol.client.views.classnames.PCTableViewClassNames;
import com.ing3nia.parentalcontrol.client.views.subviews.AddEmergencyContactView;
import com.ing3nia.parentalcontrol.client.views.subviews.EditEmergencyContactView;
import com.ing3nia.parentalcontrol.client.views.subviews.EditRuleView;

public class DeviceEmergencyNumberListView {
	
	
	public static String VIEW_CONTENT_CLASSNAME = "deviceEmergencyNumberListContent";
	
	private BaseViewHandler baseViewHandler;
	
	private HTMLPanel centerContent;
	
	/**
	 * Main panel of device contact list view that groups all 
	 * the widgets together.
	 */
	private HTMLPanel viewContent;
	
	/**
	 * Emergency Numbers label.
	 */
	private Label emergencyNumbersLabel;
	
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
	
	private Button addEmergencyButton;
	
	/**
	 * List of contacts of device.
	 */
	private ArrayList<EmergencyNumberModel> emergencyNumbers;
	
	private ArrayList<EmergencyNumberModel> addedEmergencyNumbers;
	
	private ArrayList<EmergencyNumberModel> deletedEmergencyNumbers;
	
	private SmartphoneModel smartphone;
	
	private String cookieId;
	
	/**
	 * Table where the alerts are displayed.
	 */
	private CellTable<EmergencyNumberModel> emergencyNumberTable = new CellTable<EmergencyNumberModel>();
	
	private SimplePager pager;
	
	private Button saveButton;
	
	public DeviceEmergencyNumberListView(BaseViewHandler baseViewHandler, String cookieId, SmartphoneModel smartphone) {
		this.baseViewHandler = baseViewHandler;
		this.cookieId = cookieId;
		this.smartphone = smartphone;
		this.emergencyNumbers = new ArrayList<EmergencyNumberModel>();
		loadEmergencyNumbers();		
		this.addedEmergencyNumbers = new ArrayList<EmergencyNumberModel>();
		this.deletedEmergencyNumbers = new ArrayList<EmergencyNumberModel>();
		this.centerContent = baseViewHandler.getBaseBinder().getCenterContent();
		this.centerContent.setStyleName("centerContent");
		this.viewContent = new HTMLPanel("");
		this.viewContent.setStyleName(VIEW_CONTENT_CLASSNAME);
		this.emergencyNumbersLabel = new Label("Emergency Numbers:");
		this.emergencyNumbersLabel.setStyleName("sec-title");
		this.contactButtonsPanel = new HTMLPanel("");
		this.contactButtonsPanel.setStyleName("contactButtonsPanel");
		this.contactsButton = new Button("Contacts");
		DOM.setElementProperty(contactsButton.getElement(), "id", "normalContactButton");
		this.contactsButton.setStyleName("contactButton");

		this.emergencyContactsButton = new Button("Emergency Contacts");
		DOM.setElementProperty(emergencyContactsButton.getElement(), "id", "emergencyContactsButton");
		this.emergencyContactsButton.setStyleName("selectedContactButton");
		
		this.emergencyNumberTable = new CellTable<EmergencyNumberModel>(10);
		this.pager = new SimplePager();
		this.saveButton = new Button("Save");
		DOM.setElementProperty(saveButton.getElement(), "id", "saveEmergencyNumbersButton");
		
		this.addEmergencyButton = new Button("Add Emergency Number");
		DOM.setElementProperty(contactsButton.getElement(), "id", "addEmergencyNumber");
		this.addEmergencyButton.setStyleName("addContactButton");
		
		this.centerContent.clear();
		
		//addTestDeviceEmergencyNumbers();
		//initDeviceEmergencyNumberListView();
	}
	
	public void loadDeviceContacts() {
		baseViewHandler.getBaseBinder().getCenterContent().clear();
		DeviceContactListView contactView = new DeviceContactListView(baseViewHandler, cookieId, smartphone);
		contactView.initDeviceContactListView();
	}
	
	public void loadDeviceEmergencyContacts() {
		
	}
	
	public void initDeviceEmergencyNumberListView() {
		this.viewContent.add(this.emergencyNumbersLabel);
		
		this.emergencyNumberTable.setStyleName(PCTableViewClassNames.EXTENDED_TABLE.getClassname());
		
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
		
		// Add a text column to show the country.
		TextColumn<EmergencyNumberModel> countryColumn = new TextColumn<EmergencyNumberModel>() {
			@Override
			public String getValue(EmergencyNumberModel object) {
				return object.getCountry();
			}
		};

		emergencyNumberTable.addColumn(countryColumn, "Country");
		
		// Add a text column to show the emergency number.
		TextColumn<EmergencyNumberModel> phoneNumberColumn = new TextColumn<EmergencyNumberModel>() {
			@Override
			public String getValue(EmergencyNumberModel object) {
				return object.getNumber();
			}
		};

		emergencyNumberTable.addColumn(phoneNumberColumn, "Phone");
		
		// Add a text column to show the phone.
		TextColumn<EmergencyNumberModel> descColumn = new TextColumn<EmergencyNumberModel>() {
			@Override
			public String getValue(EmergencyNumberModel object) {
				return object.getDescription();
			}
		};

		emergencyNumberTable.addColumn(descColumn, "Description");		

		// Add an edit column
		ButtonCell editCell = new ButtonCell();
		Column<EmergencyNumberModel, String> editColumn = new Column<EmergencyNumberModel, String>(editCell) {
			@Override
			public String getValue(EmergencyNumberModel object) {
				return "Edit";
			}
		};

		editColumn.setFieldUpdater(new FieldUpdater<EmergencyNumberModel, String>() {
			@Override
			public void update(int index, EmergencyNumberModel object, String value) {
				editEmergencyContact(object);
			}
		});
		

		emergencyNumberTable.addColumn(editColumn, "");
		
		// Add an delete column to show the disallow button.
		ButtonCell deleteCell = new ButtonCell();
		Column<EmergencyNumberModel, String> deleteColumn = new Column<EmergencyNumberModel, String>(deleteCell) {
			@Override
			public String getValue(EmergencyNumberModel object) {
				return "Delete";
			}
		};

		deleteColumn.setFieldUpdater(new FieldUpdater<EmergencyNumberModel, String>() {
			@Override
			public void update(int index, EmergencyNumberModel object, String value) {
				//Change button's style
				TableRowElement row = emergencyNumberTable.getRowElement(index);
				
				//Add to deleted emergency numbers;
				if (addedEmergencyNumbers.contains(object)) {
					addedEmergencyNumbers.remove(object);
				}
				
				deletedEmergencyNumbers.add(object);
			}
		});
		

		emergencyNumberTable.addColumn(deleteColumn, "");

	

		// Set the total row count. This isn't strictly necessary, but it
		// affects paging calculations, so its good habit to keep the row
		// count up to date.
		emergencyNumberTable.setRowCount(emergencyNumbers.size(), true);

		// Push the data into the widget.
		//adminUserTable.setRowData(0, adminUsers);
		ListDataProvider<EmergencyNumberModel> dataProvider = new ListDataProvider<EmergencyNumberModel>(emergencyNumbers);
		dataProvider.addDataDisplay(emergencyNumberTable);
		
		//creating paging controls		
		pager.setDisplay(emergencyNumberTable);
		pager.setStylePrimaryName("tablePager");
		pager.setStyleName("");
		
		viewContent.add(pager);
		viewContent.add(emergencyNumberTable);		
		viewContent.add(pager);

		viewContent.add(addEmergencyButton);
		addEmergencyButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				baseViewHandler.getBaseBinder().getCenterContent().clear();
				AddEmergencyContactView addEm = new AddEmergencyContactView(baseViewHandler,smartphone);
				addEm.initAddEmergencyContactView();
			}
		});

		
		this.centerContent.add(this.viewContent);
	}
	
	public void saveEmergencyNumbers() {
		if (!addedEmergencyNumbers.isEmpty() && !deletedEmergencyNumbers.isEmpty()) {
			ModificationModel auxMod = new ModificationModel();
			
			auxMod.setAddedEmergencyNumbers(this.addedEmergencyNumbers);
			auxMod.setDeletedEmergencyNumbers(this.deletedEmergencyNumbers);
		    
		    SaveSmartphoneModificationsServiceAsync saveModService = GWT.create(SaveSmartphoneModificationsService.class);
			saveModService.saveSmartphoneModifications(this.cookieId, this.smartphone.getKeyId(), auxMod, 
					new AsyncCallback<Boolean>() {
						public void onFailure(Throwable error) {
						}
			
						public void onSuccess(Boolean result) {
							if (result) {
								saveResultInLocalSmartphone();
							}
							else {
								Window.alert("An error occured. The device settings couldn't be applied.");
							}
						}
					}
			);
		}
	}
	
	public void saveResultInLocalSmartphone() {
		ArrayList<EmergencyNumberModel> smartAdded = this.smartphone.getAddedEmergencyNumbers();
		ArrayList<EmergencyNumberModel> smartDeleted = this.smartphone.getDeletedEmergencyNumbers();
		boolean isAdded = false;
		boolean isDeleted = false;
		EmergencyNumberModel auxEmergency;
		
		for (EmergencyNumberModel enm : this.addedEmergencyNumbers) {
			for (EmergencyNumberModel emergency : smartAdded) {
				if (emergency.getCountry().equals(enm.getCountry()) && emergency.getNumber().equals(enm.getNumber()) && emergency.getDescription().equals(enm.getDescription())) {
					isAdded = true;
					break;
				}
			}
			
			if (!isAdded) {
				auxEmergency = new EmergencyNumberModel(enm.getCountry(), enm.getNumber(), enm.getDescription());
				this.smartphone.getAddedEmergencyNumbers().add(auxEmergency);
				
				for (EmergencyNumberModel emergency2 : smartDeleted) {
					if (emergency2.getCountry().equals(enm.getCountry()) && emergency2.getNumber().equals(enm.getNumber()) && emergency2.getDescription().equals(enm.getDescription())) {
						if (smartDeleted.contains(enm)) {
							smartDeleted.remove(enm);
						}								
						else {
							ModelLogger.logger.info("[DeviceEmergencyNumberListView] Algo esta mal!, se deberia encontrar el telefono: " + enm.getNumber() + " en el numero de emergencia eliminado de pais y descripcion: " + emergency2.getCountry() + " " + emergency2.getDescription());
						}
						
						break;
					}
				}
			}
			
			isAdded = false;
		}
		
		for (EmergencyNumberModel enm : this.deletedEmergencyNumbers) {
			for (EmergencyNumberModel emergency : smartDeleted) {
				if (emergency.getCountry().equals(enm.getCountry()) && emergency.getNumber().equals(enm.getNumber()) && emergency.getDescription().equals(enm.getDescription())) {
					isDeleted = true;
					break;
				}
			}
			
			if (!isDeleted) {
				auxEmergency = new EmergencyNumberModel(enm.getCountry(), enm.getNumber(), enm.getDescription());
				this.smartphone.getDeletedEmergencyNumbers().add(auxEmergency);
				
				for (EmergencyNumberModel emergency2 : smartAdded) {
					if (emergency2.getCountry().equals(enm.getCountry()) && emergency2.getNumber().equals(enm.getNumber()) && emergency2.getDescription().equals(enm.getDescription())) {
						if (smartAdded.contains(enm)) {
							smartAdded.remove(enm);
						}								
						else {
							ModelLogger.logger.info("[DeviceEmergencyNumberListView] Algo esta mal!, se deberia encontrar el telefono: " + enm.getNumber() + " en el numero de emergencia agregado de pais y descripcion: " + emergency2.getCountry() + " " + emergency2.getDescription());
						}
						
						break;
					}
				}
			}
			
			isDeleted = false;
		}
	}
	
	public void addTestDeviceEmergencyNumbers() {
		
	}
	
	public void editEmergencyContact(EmergencyNumberModel emergencyContact) {
		baseViewHandler.getBaseBinder().getCenterContent().clear();
		SmartphoneModel smart = this.baseViewHandler.getUser().getSmartphones().get(this.baseViewHandler.getClickedSmartphoneIndex()); 

		EditEmergencyContactView editView = new EditEmergencyContactView(baseViewHandler, smart, emergencyContact);
		editView.initEditEmergencyContactView();
	}
	
	
	public void loadEmergencyNumbers() {
		this.emergencyNumbers.addAll(this.smartphone.getAddedEmergencyNumbers());
		this.emergencyNumbers.addAll(this.smartphone.getDeletedEmergencyNumbers());
	}
}
