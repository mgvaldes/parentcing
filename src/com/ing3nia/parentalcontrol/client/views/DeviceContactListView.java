package com.ing3nia.parentalcontrol.client.views;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.gwt.cell.client.ButtonCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
//import com.google.gwt.dev.util.collect.HashMap;
import com.google.gwt.dom.client.TableRowElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.view.client.ListDataProvider;

import com.ing3nia.parentalcontrol.client.views.classnames.PCTableViewClassNames;

import com.ing3nia.parentalcontrol.client.models.ClientSimpleContactModel;
import com.ing3nia.parentalcontrol.client.models.ContactModel;
import com.ing3nia.parentalcontrol.client.models.FullContactModel;
import com.ing3nia.parentalcontrol.client.models.ModificationModel;
import com.ing3nia.parentalcontrol.client.models.PhoneModel;
import com.ing3nia.parentalcontrol.client.models.SimpleContactModel;
import com.ing3nia.parentalcontrol.client.models.SmartphoneModel;
import com.ing3nia.parentalcontrol.client.rpc.SaveSmartphoneModificationsService;
import com.ing3nia.parentalcontrol.client.rpc.SaveSmartphoneModificationsServiceAsync;
import com.ing3nia.parentalcontrol.models.utils.PhoneType;
import com.ing3nia.parentalcontrol.services.utils.ModelLogger;


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
	private List<ClientSimpleContactModel> contacts;
	
	private List<ClientSimpleContactModel> activeContacts;
	
	private List<ClientSimpleContactModel> inactiveContacts;
	
	private SmartphoneModel smartphone;
	
	private String cookieId;
	
	/**
	 * Table where the alerts are displayed.
	 */
	private CellTable<ClientSimpleContactModel> contactTable = new CellTable<ClientSimpleContactModel>();
	
	private SimplePager pager;
	
	private Button saveButton;
	
	public DeviceContactListView(HTMLPanel centerContent, String cookieId, SmartphoneModel smartphone) {
		this.cookieId = cookieId;
		this.smartphone = smartphone;
		this.contacts = new ArrayList<ClientSimpleContactModel>();
		loadSmartphoneContacts();		
		this.activeContacts = new ArrayList<ClientSimpleContactModel>();
		this.inactiveContacts = new ArrayList<ClientSimpleContactModel>();
		this.centerContent = centerContent;		
		this.viewContent = new HTMLPanel("");
		this.contactsLabel = new Label("Contacts:");
		this.contactButtonsPanel = new HTMLPanel("");
		this.contactsButton = new Button("Contacts");
		this.emergencyContactsButton = new Button("Emergency Contacts");
		this.contactTable = new CellTable<ClientSimpleContactModel>(10);
		this.pager = new SimplePager();		
		this.saveButton = new Button("Save");
		this.centerContent.clear();
		
		addTestDeviceContacts();
	}
	
	public void loadSmartphoneContacts() {
		ArrayList<ContactModel> smartContacts = smartphone.getActiveContacts();
		ClientSimpleContactModel auxSimpleContact;
				
		for (ContactModel c : smartContacts) {
			for (PhoneModel p : c.getPhones()) {
				auxSimpleContact = new ClientSimpleContactModel(c.getFirstName(), c.getLastName(), p.getPhoneNumber(), p.getType(), c.getEmails(), c.getAddresses(), c.getOrganizations());
				contacts.add(auxSimpleContact);
			}
		}
		
		smartContacts = smartphone.getInactiveContacts();
		
		for (ContactModel c : smartContacts) {
			for (PhoneModel p : c.getPhones()) {
				auxSimpleContact = new ClientSimpleContactModel(c.getFirstName(), c.getLastName(), p.getPhoneNumber(), p.getType(), c.getEmails(), c.getAddresses(), c.getOrganizations());
				contacts.add(auxSimpleContact);
			}
		}
	}
	
	public void addTestDeviceContacts() {
		
	}
	
	public void initDeviceContactListView() {
		
		//Setting alert table style
		contactTable.setStyleName(PCTableViewClassNames.EXTENDED_TABLE.getClassname());
		
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
		TextColumn<ClientSimpleContactModel> firstNameColumn = new TextColumn<ClientSimpleContactModel>() {
			@Override
			public String getValue(ClientSimpleContactModel object) {
				return object.getFirstName();
			}
		};

		contactTable.addColumn(firstNameColumn, "First Name");
		
		// Add a text column to show the last name.
		TextColumn<ClientSimpleContactModel> lastNameColumn = new TextColumn<ClientSimpleContactModel>() {
			@Override
			public String getValue(ClientSimpleContactModel object) {
				return object.getLastName();
			}
		};

		contactTable.addColumn(lastNameColumn, "Last Name");
		
		// Add a text column to show the phone.
		TextColumn<ClientSimpleContactModel> phoneColumn = new TextColumn<ClientSimpleContactModel>() {
			@Override
			public String getValue(ClientSimpleContactModel object) {
				return object.getPhone();
			}
		};

		contactTable.addColumn(phoneColumn, "Phone");		

		// Add an edit column to show the disallow button.
		ButtonCell disallowCell = new ButtonCell();
		Column<ClientSimpleContactModel, String> disallowColumn = new Column<ClientSimpleContactModel, String>(disallowCell) {
			@Override
			public String getValue(ClientSimpleContactModel object) {
				return "Disallow";
			}
		};

		disallowColumn.setFieldUpdater(new FieldUpdater<ClientSimpleContactModel, String>() {
			@Override
			public void update(int index, ClientSimpleContactModel object, String value) {
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
		Column<ClientSimpleContactModel, String> allowColumn = new Column<ClientSimpleContactModel, String>(allowCell) {
			@Override
			public String getValue(ClientSimpleContactModel object) {
				return "Allow";
			}
		};

		allowColumn.setFieldUpdater(new FieldUpdater<ClientSimpleContactModel, String>() {
			@Override
			public void update(int index, ClientSimpleContactModel object, String value) {
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
		Column<ClientSimpleContactModel, String> viewColumn = new Column<ClientSimpleContactModel, String>(viewCell) {
			@Override
			public String getValue(ClientSimpleContactModel object) {
				return "View";
			}
		};

		viewColumn.setFieldUpdater(new FieldUpdater<ClientSimpleContactModel, String>() {
			@Override
			public void update(int index, ClientSimpleContactModel object, String value) {
				loadContactDetails(object);
			}
		});
		

		contactTable.addColumn(viewColumn, "");

		// Set the total row count. This isn't strictly necessary, but it
		// affects paging calculations, so its good habit to keep the row
		// count up to date.
		contactTable.setRowCount(contacts.size(), true);

		// Push the data into the widget.
		//adminUserTable.setRowData(0, adminUsers);
		ListDataProvider<ClientSimpleContactModel> dataProvider = new ListDataProvider<ClientSimpleContactModel>(contacts);
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
		
		this.viewContent.add(saveButton);
		this.centerContent.add(this.viewContent);
	}
	
	public void loadContactDetails(ClientSimpleContactModel contact) {
//		FullContactModel fullContact = new FullContactModel(contact.getFirstName(), contact.getLastName(), 
//															contact.getEmails(), contact.getOrganizations().get(0).getName(), 
//															contact.getOrganizations().get(0).getTitle(), PhoneType.getPhoneDescByType(contact.getPhoneType()), 
//															contact.getPhone(), contact.getAddresses().get(0).getStreet(), 
//															contact.getAddresses().get(0).getCity(), contact.getAddresses().get(0).getState(), 
//															contact.getAddresses().get(0).getZipCode(), contact.getAddresses().get(0).getCountry());
		
		//Pass fullContact to details view.
	}
	
	public void saveContacts() {
		if (!activeContacts.isEmpty() && !inactiveContacts.isEmpty()) {
			ModificationModel auxMod = new ModificationModel();
			
			HashMap<String, ArrayList<PhoneModel>> activeHash = new HashMap<String, ArrayList<PhoneModel>>();
			HashMap<String, ArrayList<PhoneModel>> inactiveHash = new HashMap<String, ArrayList<PhoneModel>>();
			String auxContactName;
			ArrayList<PhoneModel> auxPhones;
			
			for (ClientSimpleContactModel contact : activeContacts) {
				auxContactName = contact.getFirstName() + "|" + contact.getLastName();
				
				if (activeHash.containsKey(auxContactName)) {
					auxPhones = (ArrayList<PhoneModel>)activeHash.get(auxContactName);					
				}
				else {
					auxPhones = new ArrayList<PhoneModel>();
				}
				
				auxPhones.add(new PhoneModel(contact.getPhoneType(), contact.getPhone()));
				activeHash.put(auxContactName, auxPhones);
			}
			
			for (ClientSimpleContactModel contact : inactiveContacts) {
				auxContactName = contact.getFirstName() + "|" + contact.getLastName();
				
				if (inactiveHash.containsKey(auxContactName)) {
					auxPhones = (ArrayList<PhoneModel>)inactiveHash.get(auxContactName);					
				}
				else {
					auxPhones = new ArrayList<PhoneModel>();
				}
				
				auxPhones.add(new PhoneModel(contact.getPhoneType(), contact.getPhone()));
				inactiveHash.put(auxContactName, auxPhones);
			}
						
			Iterator<Map.Entry<String, ArrayList<PhoneModel>>> it = activeHash.entrySet().iterator();
			SimpleContactModel auxContact;
			Map.Entry<String, ArrayList<PhoneModel>> pair;
			String[] auxName;
			ArrayList<SimpleContactModel> simpleActiveContacts = new ArrayList<SimpleContactModel>();
			
		    while (it.hasNext()) {
		        pair = (Map.Entry<String, ArrayList<PhoneModel>>)it.next();	        

		        auxName = ((String)pair.getKey()).split("\\|");
		        auxContact = new SimpleContactModel(auxName[0], auxName[1], (ArrayList<PhoneModel>)pair.getValue());
		        simpleActiveContacts.add(auxContact);
		    }
		    
		    auxMod.setActiveContacts(simpleActiveContacts);
		    
		    it = inactiveHash.entrySet().iterator();
			ArrayList<SimpleContactModel> simpleInactiveContacts = new ArrayList<SimpleContactModel>();
			
		    while (it.hasNext()) {
		        pair = (Map.Entry<String, ArrayList<PhoneModel>>)it.next();	        

		        auxName = ((String)pair.getKey()).split("\\|");
		        auxContact = new SimpleContactModel(auxName[0], auxName[1], (ArrayList<PhoneModel>)pair.getValue());
		        simpleInactiveContacts.add(auxContact);
		    }
		    
		    auxMod.setInactiveContacts(simpleInactiveContacts);
		    
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
		ArrayList<ContactModel> smartActiveContacts = this.smartphone.getActiveContacts();
		ArrayList<ContactModel> smartInactiveContacts = this.smartphone.getInactiveContacts();
		ArrayList<PhoneModel> auxPhones;
		boolean isActive = false;
		boolean isInactive = false;
		ContactModel auxContact;
		PhoneModel auxPhone;
		
		for (ClientSimpleContactModel cscm : this.activeContacts) {
			for (ContactModel contact : smartActiveContacts) {
				if (contact.getFirstName().equals(cscm.getFirstName()) && contact.getLastName().equals(cscm.getLastName())) {
					auxPhone = new PhoneModel(cscm.getPhoneType(), cscm.getPhone());
					
					if (!contact.getPhones().contains(auxPhone)) {
						contact.getPhones().add(auxPhone);
						
						for (ContactModel contact2 : smartInactiveContacts) {
							if (contact2.getFirstName().equals(cscm.getFirstName()) && contact2.getLastName().equals(cscm.getLastName())) {
								if (contact2.getPhones().contains(auxPhone)) {
									contact2.getPhones().remove(auxPhone);
								}								
								else {
									ModelLogger.logger.info("[DeviceContactListView] Algo esta mal!, se deberia encontrar el telefono: " + auxPhone.getPhoneNumber() + " en el contacto inactivo de nombre: " + contact2.getFirstName() + " " + contact2.getLastName());
								}
								
								break;
							}
						}
					}
					
					isActive = true;
					break;
				}
			}
			
			if (!isActive) {
				auxPhones = new ArrayList<PhoneModel>();
				auxPhone = new PhoneModel(cscm.getPhoneType(), cscm.getPhone());
				auxPhones.add(auxPhone);
				auxContact = new ContactModel(cscm.getFirstName(), cscm.getLastName(), auxPhones);
				auxContact.setEmails(cscm.getEmails());
				auxContact.setAddresses(cscm.getAddresses());
				auxContact.setOrganizations(cscm.getOrganizations());
				this.smartphone.getActiveContacts().add(auxContact);
				
				for (ContactModel contact3 : smartInactiveContacts) {
					if (contact3.getFirstName().equals(cscm.getFirstName()) && contact3.getLastName().equals(cscm.getLastName())) {
						if (contact3.getPhones().contains(auxPhone)) {
							contact3.getPhones().remove(auxPhone);
						}								
						else {
							ModelLogger.logger.info("[DeviceContactListView] Algo esta mal!, se deberia encontrar el telefono: " + auxPhone.getPhoneNumber() + " en el contacto inactivo de nombre: " + contact3.getFirstName() + " " + contact3.getLastName());
						}
						
						break;
					}
				}
			}
			
			isActive = false;
		}
		
		for (ClientSimpleContactModel cscm : this.inactiveContacts) {
			for (ContactModel contact : smartInactiveContacts) {
				if (contact.getFirstName().equals(cscm.getFirstName()) && contact.getLastName().equals(cscm.getLastName())) {
					auxPhone = new PhoneModel(cscm.getPhoneType(), cscm.getPhone());
					
					if (!contact.getPhones().contains(auxPhone)) {
						contact.getPhones().add(auxPhone);
						
						for (ContactModel contact2 : smartActiveContacts) {
							if (contact2.getFirstName().equals(cscm.getFirstName()) && contact2.getLastName().equals(cscm.getLastName())) {
								if (contact2.getPhones().contains(auxPhone)) {
									contact2.getPhones().remove(auxPhone);
								}								
								else {
									ModelLogger.logger.info("[DeviceContactListView] Algo esta mal!, se devberia encontrar el telefono: " + auxPhone.getPhoneNumber() + " en el contacto activo de nombre: " + contact2.getFirstName() + " " + contact2.getLastName());
								}
								
								break;
							}
						}
					}
					
					isInactive = true;
					break;
				}
			}
			
			if (!isInactive) {
				auxPhones = new ArrayList<PhoneModel>();
				auxPhone = new PhoneModel(cscm.getPhoneType(), cscm.getPhone());
				auxPhones.add(auxPhone);
				auxContact = new ContactModel(cscm.getFirstName(), cscm.getLastName(), auxPhones);
				auxContact.setEmails(cscm.getEmails());
				auxContact.setAddresses(cscm.getAddresses());
				auxContact.setOrganizations(cscm.getOrganizations());
				this.smartphone.getInactiveContacts().add(auxContact);
				
				for (ContactModel contact3 : smartActiveContacts) {
					if (contact3.getFirstName().equals(cscm.getFirstName()) && contact3.getLastName().equals(cscm.getLastName())) {
						if (contact3.getPhones().contains(auxPhone)) {
							contact3.getPhones().remove(auxPhone);
						}								
						else {
							ModelLogger.logger.info("[DeviceContactListView] Algo esta mal!, se deberia encontrar el telefono: " + auxPhone.getPhoneNumber() + " en el contacto activo de nombre: " + contact3.getFirstName() + " " + contact3.getLastName());
						}
						
						break;
					}
				}
			}
			
			isInactive = false;
		}
	}
	
	public void loadDeviceContacts() {
		
	}
	
	public void loadDeviceEmergencyContacts() {
		
	}
}
