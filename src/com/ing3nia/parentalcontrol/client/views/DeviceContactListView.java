package com.ing3nia.parentalcontrol.client.views;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.gwt.cell.client.ButtonCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.core.client.GWT;
//import com.google.gwt.dev.util.collect.HashMap;
import com.google.gwt.dom.client.TableRowElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.RowStyles;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.RangeChangeEvent;

import com.ing3nia.parentalcontrol.client.utils.ModelLogger;
import com.ing3nia.parentalcontrol.client.views.async.AsyncronousCallsMessages;
import com.ing3nia.parentalcontrol.client.views.classnames.PCTableViewClassNames;

import com.ing3nia.parentalcontrol.client.handlers.BaseViewHandler;
import com.ing3nia.parentalcontrol.client.handlers.click.innerbutton.ContactListRangeChangeHandler;
import com.ing3nia.parentalcontrol.client.handlers.click.innerbutton.SaveContactsClickHandler;
import com.ing3nia.parentalcontrol.client.models.ClientAdminUserModel;
import com.ing3nia.parentalcontrol.client.models.ClientSimpleContactModel;
import com.ing3nia.parentalcontrol.client.models.ContactModel;
import com.ing3nia.parentalcontrol.client.models.ModificationModel;
import com.ing3nia.parentalcontrol.client.models.PhoneModel;
import com.ing3nia.parentalcontrol.client.models.SimpleContactModel;
import com.ing3nia.parentalcontrol.client.models.SmartphoneModel;
import com.ing3nia.parentalcontrol.client.rpc.SaveSmartphoneModificationsService;
import com.ing3nia.parentalcontrol.client.rpc.SaveSmartphoneModificationsServiceAsync;


public class DeviceContactListView {
	
	
	public static String VIEW_CONTENT_CLASSNAME = "deviceContactListContent";
	
	
	/**
	 * Handler to control the entire base view interface
	 */
	private BaseViewHandler baseViewHandler;
	
	
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
	
	private ArrayList<Boolean> activeInactiveIndexList;
	
	public DeviceContactListView(BaseViewHandler baseViewHandler, String cookieId, SmartphoneModel smartphone) {
		this.baseViewHandler = baseViewHandler;
		this.cookieId = cookieId;
		this.smartphone = smartphone;
		this.contacts = new ArrayList<ClientSimpleContactModel>();
		loadSmartphoneContacts();		
		this.activeContacts = new ArrayList<ClientSimpleContactModel>();
		this.inactiveContacts = new ArrayList<ClientSimpleContactModel>();
		this.centerContent = baseViewHandler.getBaseBinder().getCenterContent();	
		this.centerContent.setStyleName("centerContent");
		this.viewContent = new HTMLPanel("");
		this.viewContent.setStyleName(VIEW_CONTENT_CLASSNAME);
		this.contactsLabel = new Label("Contacts:");
		this.contactButtonsPanel = new HTMLPanel("");
		this.contactsButton = new Button("Contacts");
		DOM.setElementProperty(contactsButton.getElement(), "id", "contactsButton");
		this.contactsButton.setStyleName("selectedContactButton");
		
		this.emergencyContactsButton = new Button("Emergency Contacts");
		DOM.setElementProperty(emergencyContactsButton.getElement(), "id", "emergencyContactButton");
		this.emergencyContactsButton.setStyleName("contactButton");
		
		this.contactTable = new CellTable<ClientSimpleContactModel>(smartphone.getActiveContacts().size()+smartphone.getInactiveContacts().size());
		this.pager = new SimplePager();	
		
		this.saveButton = new Button("Save");
		this.saveButton.setStyleName("saveButton");
		DOM.setElementProperty(saveButton.getElement(), "id", "saveContactsButton");
		
		this.centerContent.clear();
		
	}
	
	public void loadSmartphoneContacts() {
		this.activeInactiveIndexList = new ArrayList<Boolean>(this.smartphone.getActiveContacts().size() + this.smartphone.getInactiveContacts().size());
		ArrayList<ContactModel> smartContacts = this.smartphone.getActiveContacts();
		ClientSimpleContactModel auxSimpleContact;
				
		for (ContactModel c : smartContacts) {
			for (PhoneModel p : c.getPhones()) {
				auxSimpleContact = new ClientSimpleContactModel(c.getKeyId(),c.getFirstName(), c.getLastName(), p.getPhoneNumber(), p.getType(), c.getEmails(), c.getAddresses(), c.getOrganizations());
				auxSimpleContact.setWasOriginallyActive(true);
				contacts.add(auxSimpleContact);
				this.activeInactiveIndexList.add(true);
			}
		}
		
		smartContacts = smartphone.getInactiveContacts();
		
		for (ContactModel c : smartContacts) {
			for (PhoneModel p : c.getPhones()) {
				auxSimpleContact = new ClientSimpleContactModel(c.getKeyId(), c.getFirstName(), c.getLastName(), p.getPhoneNumber(), p.getType(), c.getEmails(), c.getAddresses(), c.getOrganizations());
				auxSimpleContact.setWasOriginallyActive(false);
				contacts.add(auxSimpleContact);
				this.activeInactiveIndexList.add(false);
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
		//ButtonCell disallowCell = new ButtonCell();
		DisallowButtonCell disallowCell = new DisallowButtonCell();
		
		Column<ClientSimpleContactModel, String> disallowColumn = new Column<ClientSimpleContactModel, String>(disallowCell) {
			@Override
			public String getValue(ClientSimpleContactModel object) {	
				return "Disallow";
			}
		};

		disallowColumn.setFieldUpdater(new FieldUpdater<ClientSimpleContactModel, String>() {
			@Override
			public void update(int index, ClientSimpleContactModel object, String value) {
				
				int newIndex = getRealIndexFromPager(index, pager);
				
				contactTable.getRowElement(newIndex).getCells().getItem(3).getFirstChildElement().setInnerHTML(DisallowButtonCell.opaqueButtonFullString);				
				contactTable.getRowElement(newIndex).getCells().getItem(4).getFirstChildElement().setInnerHTML(AllowButtonCell.transpButtonFullString);							
						
				//Add to inactive contacts;
				if (activeContacts.contains(object)) {
					activeContacts.remove(object);
				}
				
				if(!inactiveContacts.contains(object) && object.getWasOriginallyActive()){
					inactiveContacts.add(object);
				}
				
				// Set contact as disabled in boolean list
				activeInactiveIndexList.set(index, false);
				
				//Window.alert("ACTIVE: "+activeContacts.size()+" INACTIVE: "+inactiveContacts.size());
			}
		});
		
		contactTable.addColumn(disallowColumn, "Disallow");
		
		// Add an edit column to show the disallow button.
		//ButtonCell allowCell = new ButtonCell();
		AllowButtonCell allowCell = new AllowButtonCell();
		
		Column<ClientSimpleContactModel, String> allowColumn = new Column<ClientSimpleContactModel, String>(allowCell) {
			@Override
			public String getValue(ClientSimpleContactModel object) {
				return "Allow";
			}
		};

		allowColumn.setFieldUpdater(new FieldUpdater<ClientSimpleContactModel, String>() {
			@Override
			public void update(int index, ClientSimpleContactModel object, String value) {

				int newIndex = getRealIndexFromPager(index, pager);
				
				contactTable.getRowElement(newIndex).getCells().getItem(4).getFirstChildElement().setInnerHTML(AllowButtonCell.opaqueButtonFullString);				
				contactTable.getRowElement(newIndex).getCells().getItem(3).getFirstChildElement().setInnerHTML(DisallowButtonCell.transpButtonFullString);				
		
				//Add to active contacts;
				if (inactiveContacts.contains(object)) {
					inactiveContacts.remove(object);
				}
				if(!activeContacts.contains(object) && !object.getWasOriginallyActive()){
					activeContacts.add(object);
				}
				
				// Set contact as disabled in boolean list
				activeInactiveIndexList.set(index, true);
				
				//Window.alert("ACTIVE: "+activeContacts.size()+" INACTIVE: "+inactiveContacts.size());
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
		
		//Difference bewteen allow and disallow buttons
		ContactListRangeChangeHandler rangeChange = new ContactListRangeChangeHandler(contactTable, activeInactiveIndexList);
		contactTable.addRangeChangeHandler(rangeChange);
	    contactTable.setVisibleRangeAndClearData(new Range(0, 2), true);
		
		//creating paging controls		
		pager.setDisplay(contactTable);
		pager.setStylePrimaryName("tablePager");
		pager.setStyleName("");
		pager.setPageSize(5);
		
		viewContent.add(pager);
		viewContent.add(contactTable);		
		viewContent.add(pager);
		
		SaveContactsClickHandler saveContactsClickHandler = new SaveContactsClickHandler(this);
		saveButton.addClickHandler(saveContactsClickHandler);

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
		baseViewHandler.getBaseBinder().getNotice().setText("");
		LoadingView.clearLoadingView(baseViewHandler.getBaseBinder());
		LoadingView.setLoadingView(baseViewHandler.getBaseBinder(), AsyncronousCallsMessages.SAVING_DEVICE_CONTACTS, LoadingView.loadingImage);
				
		if (!activeContacts.isEmpty() || !inactiveContacts.isEmpty()) {
			ModificationModel auxMod = new ModificationModel();
			
			HashMap<String, ArrayList<PhoneModel>> activeHash = new HashMap<String, ArrayList<PhoneModel>>();
			HashMap<String, ArrayList<PhoneModel>> inactiveHash = new HashMap<String, ArrayList<PhoneModel>>();
			String auxContactName;
			ArrayList<PhoneModel> auxPhones;
			
			for (ClientSimpleContactModel contact : activeContacts) {
				auxContactName = contact.getFirstName() + "|" + contact.getLastName() + "|" +contact.getKeyId();
				
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
				auxContactName = contact.getFirstName() + "|" + contact.getLastName() + "|" +contact.getKeyId();
				
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
		        auxContact = new SimpleContactModel(auxName[2], auxName[0], auxName[1], (ArrayList<PhoneModel>)pair.getValue());
		        simpleActiveContacts.add(auxContact);
		    }
		    
		    auxMod.setActiveContacts(simpleActiveContacts);
		    
		    it = inactiveHash.entrySet().iterator();
			ArrayList<SimpleContactModel> simpleInactiveContacts = new ArrayList<SimpleContactModel>();
			
		    while (it.hasNext()) {
		        pair = (Map.Entry<String, ArrayList<PhoneModel>>)it.next();	        

		        auxName = ((String)pair.getKey()).split("\\|");
		        auxContact = new SimpleContactModel(auxName[2], auxName[0], auxName[1], (ArrayList<PhoneModel>)pair.getValue());
		        simpleInactiveContacts.add(auxContact);
		    }
		    
		    auxMod.setInactiveContacts(simpleInactiveContacts);
		    
		    SaveSmartphoneModificationsServiceAsync saveModService = GWT.create(SaveSmartphoneModificationsService.class);
			saveModService.saveSmartphoneModifications(this.cookieId, this.smartphone.getKeyId(), auxMod, 
					new AsyncCallback<Boolean>() {
						public void onFailure(Throwable error) {
							LoadingView.clearLoadingView(baseViewHandler.getBaseBinder());
							baseViewHandler.getBaseBinder().getNotice().setText("Contact settings couldn't be stored");
						}
			
						public void onSuccess(Boolean result) {
							if (result) {
								saveResultInLocalSmartphone();
							}
							else {
								//Window.alert("An error occured. The change in contacts couldn't be applied.");
								LoadingView.clearLoadingView(baseViewHandler.getBaseBinder());
								baseViewHandler.getBaseBinder().getNotice().setText("Contact settings couldn't be stored");
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
			//for(ContactModel contact : smartInactiveContacts){
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
				auxContact = new ContactModel(cscm.getKeyId(),cscm.getFirstName(), cscm.getLastName(), auxPhones);
				auxContact.setEmails(cscm.getEmails());
				auxContact.setAddresses(cscm.getAddresses());
				auxContact.setOrganizations(cscm.getOrganizations());
				this.smartphone.getActiveContacts().add(auxContact);
								
				ContactModel foundInactiveContact = null;
				for (ContactModel contact3 : smartInactiveContacts) {
					if (contact3.getFirstName().equals(cscm.getFirstName()) && contact3.getLastName().equals(cscm.getLastName())) {
						//find phone
						boolean didfoundPhone = false;
						PhoneModel foundPhone = null;
						for(PhoneModel cph : contact3.getPhones()){
							if(cph.getPhoneNumber().equals(auxPhone.getPhoneNumber())){
								didfoundPhone=true;
								foundPhone = cph;
								break;
							}
						}
						if(didfoundPhone){
							contact3.getPhones().remove(foundPhone);
						}							
						else {
							ModelLogger.logger.info("[DeviceContactListView] Algo esta mal!, se deberia encontrar el telefono: " + auxPhone.getPhoneNumber() + " en el contacto inactivo de nombre: " + contact3.getFirstName() + " " + contact3.getLastName());
						}
						// If contact is out of phones remove from list
						foundInactiveContact = contact3;
						break;
					}
				}
				if(foundInactiveContact != null && foundInactiveContact.getPhones().size() == 0){
					this.smartphone.getInactiveContacts().remove(foundInactiveContact);
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
			//Assuming is active
			//isInactive = false;
			
			if (!isInactive) {
				auxPhones = new ArrayList<PhoneModel>();
				auxPhone = new PhoneModel(cscm.getPhoneType(), cscm.getPhone());
				auxPhones.add(auxPhone);
				auxContact = new ContactModel(cscm.getKeyId(), cscm.getFirstName(), cscm.getLastName(), auxPhones);
				auxContact.setEmails(cscm.getEmails());
				auxContact.setAddresses(cscm.getAddresses());
				auxContact.setOrganizations(cscm.getOrganizations());
				this.smartphone.getInactiveContacts().add(auxContact);
				
				ContactModel foundActiveContact = null;
				for (ContactModel contact3 : smartActiveContacts) {
					if (contact3.getFirstName().equals(cscm.getFirstName()) && contact3.getLastName().equals(cscm.getLastName())) {
						
						boolean didfoundPhone = false;
						PhoneModel foundPhone = null;
						for(PhoneModel cph : contact3.getPhones()){
							if(cph.getPhoneNumber().equals(auxPhone.getPhoneNumber())){
								didfoundPhone=true;
								foundPhone = cph;
								break;
							}
						}
						if(didfoundPhone){
							contact3.getPhones().remove(foundPhone);
						}								
						else {
							ModelLogger.logger.info("[DeviceContactListView] Algo esta mal!, se deberia encontrar el telefono: " + auxPhone.getPhoneNumber() + " en el contacto activo de nombre: " + contact3.getFirstName() + " " + contact3.getLastName());
						}
						
						// If contact is out of phones remove from list
						foundActiveContact = contact3;
						break;
					}
				}				
				if(foundActiveContact != null && foundActiveContact.getPhones().size() == 0){
					this.smartphone.getActiveContacts().remove(foundActiveContact);
				}
			}
			
			isInactive = false;
		}
		
		this.centerContent.clear();
		DeviceContactListView newContactListView = new DeviceContactListView(baseViewHandler, cookieId, smartphone);
		newContactListView.initDeviceContactListView();
		newContactListView.baseViewHandler.getBaseBinder().getCenterContent().add(newContactListView.baseViewHandler.getBaseBinder().getNotice());
		newContactListView.baseViewHandler.getBaseBinder().getNotice().setText("Contact settings successfully stored");
	}
	

	private class AllowButtonCell extends ButtonCell {
		
		private final static String opaqueButtonString = "<button type=\"button\" class=\"allowButton\" "
		+ "tabindex=\"-1\">";
		static final String opaqueButtonFullString= opaqueButtonString +"Allow"+"</button>";
		
		static final String transpButtonString = "<button type=\"button\" class=\"allowButton\" style=\"opacity:0.5;\" "
			+ "tabindex=\"-1\">";
		static final String transpButtonFullString= transpButtonString +"Allow"+"</button>";
		
		
		
		public AllowButtonCell() {
			super();
		}

		@Override
		public void render(final Context context, final SafeHtml data,
				final SafeHtmlBuilder sb) {
			sb.appendHtmlConstant(opaqueButtonString);
			
			if (data != null) {
				sb.append(data);
			}
			sb.appendHtmlConstant("</button>");
		}
	}
	

	private class DisallowButtonCell extends ButtonCell {
		
		static final String opaqueButtonString = "<button type=\"button\" class=\"disallowButton\" "
		+ "tabindex=\"-1\">";
		static final String opaqueButtonFullString= opaqueButtonString +"Disallow"+"</button>";
		
		static final String transpButtonString = "<button type=\"button\" class=\"disallowButton\" style=\"opacity:0.5;\" "
			+ "tabindex=\"-1\">";
		static final String transpButtonFullString= transpButtonString +"Disallow"+"</button>";

		
		public DisallowButtonCell() {
			super();
		}

		@Override
		public void render(final Context context, final SafeHtml data,
				final SafeHtmlBuilder sb) {
			//sb.appendHtmlConstant("<button type=\"button\" class=\"disallowButton\" "
			//		+ "tabindex=\"-1\">");
			sb.appendHtmlConstant(opaqueButtonString);
			
			if (data != null) {
				sb.append(data);
			}
			sb.appendHtmlConstant("</button>");
		}
	}
	
	
	public int getRealIndexFromPager(int tableIndex, SimplePager pager){
		return tableIndex-pager.getPageStart();
	}
	
	
	public void loadDeviceContacts() {

	}
	
	public void loadDeviceEmergencyContacts() {
		baseViewHandler.getBaseBinder().getCenterContent().clear();
		DeviceEmergencyNumberListView emergencyView = new DeviceEmergencyNumberListView(baseViewHandler, cookieId, smartphone);
		emergencyView.initDeviceEmergencyNumberListView();
	}
}
