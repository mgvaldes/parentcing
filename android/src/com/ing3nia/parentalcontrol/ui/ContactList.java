package com.ing3nia.parentalcontrol.ui;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.ing3nia.parentalcontrol.R;
import com.ing3nia.parentalcontrol.httpconnect.HttpClientHandler;
import com.ing3nia.parentalcontrol.models.ContactModel;
import com.ing3nia.parentalcontrol.models.ModificationModel;
import com.ing3nia.parentalcontrol.models.SimpleContactModel;
import com.ing3nia.parentalcontrol.models.SmartphoneModel;
import com.ing3nia.parentalcontrol.ui.components.ContactListAdapter;
import com.ing3nia.parentalcontrol.ui.components.UiUtils;
import com.ing3nia.parentalcontrol.ui.utils.PCViewsEnum;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class ContactList extends Activity {
	
	private ArrayList<SmartphoneModel> smartphonesGeneral;
	private int selected;
	private SmartphoneModel smartphone;
	private boolean[] contactStatus;
	private ModificationModel modification;
	private ArrayList<SimpleContactModel> activeContacts;
	private ArrayList<SimpleContactModel> inactiveContacts;
	private ArrayList<ContactModel> contacts;
	private HttpClientHandler httpClientHandlerApp;
	private AsyncTask<Void, Void, Boolean> asyncSaveChangesThread;
	private int nextScreen;
	private static final int ALERT_DIALOG = 1;
	static ProgressDialog processDialog;
	private LinearLayout topBanner;
	private LinearLayout topBannerSaveChanges;
	private String errorMessage = "";
	private static final int ERROR_ALERT_DIALOG = 2;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		
	    if (httpClientHandlerApp == null) { 
			httpClientHandlerApp = (HttpClientHandler) ContactList.this.getApplication();
		}
		
		smartphone = httpClientHandlerApp.smartphone;
	    smartphonesGeneral = httpClientHandlerApp.smartphonesGeneral;
	    selected = httpClientHandlerApp.selected;
	    modification = this.smartphone.getModification();
		
		if (modification == null) {
			this.smartphone.setModification(new ModificationModel());
			modification = this.smartphone.getModification();
		}
		
	    setContentView(R.layout.contact_list);
	    
	    topBanner = (LinearLayout)findViewById(R.id.topBanner);
	    topBannerSaveChanges = (LinearLayout)findViewById(R.id.topBannerSaveChanges);
	    
	    if (httpClientHandlerApp.pendingChanges) {
	    	topBanner.setVisibility(View.GONE);
	    	topBannerSaveChanges.setVisibility(View.VISIBLE);
		}
		else {
			topBannerSaveChanges.setVisibility(View.GONE);
			topBanner.setVisibility(View.VISIBLE);
		}
		
		ArrayList<ContactModel> activeContactsAux = smartphone.getActiveContacts();
		ArrayList<ContactModel> inactiveContactsAux = smartphone.getInactiveContacts();
		contacts = new ArrayList<ContactModel>();
		
		contacts.addAll(activeContactsAux);
		contacts.addAll(inactiveContactsAux);
		
		int size = activeContactsAux.size() + inactiveContactsAux.size();
		
		contactStatus = new boolean[size];
		
		int size1 = activeContactsAux.size();
		
		for (int i = 0; i < size1; i++) {
			contactStatus[i] = true;
		}
		
		for (int i = size1; i < size; i++) {
			contactStatus[i] = false;
		}
		
	    ListView contactList = (ListView)findViewById(R.id.contactList);
	    contactList.setAdapter(new ContactListAdapter(this, contacts, contactStatus));
	    contactList.setOnItemClickListener(
	    		new OnItemClickListener() {
	                @Override
	                public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
	                	ImageView contactStatusImageView = (ImageView)view.findViewById(R.id.contactStatus);
	                	
	                	if (contactStatus[position]) {             		
	                		contactStatusImageView.setImageResource(R.drawable.ic_contact_disallow);
	                		contactStatus[position] = false;
	                		
	                		if (isActive(position)) {		                		
		                		deleteActive(position);
		                	}
	                		
	                		inactiveContacts.add(convertToSimpleContactModel(position));
	                	}
	                	else {
	                		contactStatusImageView.setImageResource(R.drawable.ic_contact_allow);
	                		contactStatus[position] = true;
	                		
	                		if (isInactive(position)) {		                		
		                		deleteInactive(position);
		                	}
	                		
	                		activeContacts.add(convertToSimpleContactModel(position));
	                	}
	                	
	                	if (!httpClientHandlerApp.pendingChanges) {
	            	    	topBanner.setVisibility(View.GONE);
	            	    	topBannerSaveChanges.setVisibility(View.VISIBLE);
	            	    	httpClientHandlerApp.pendingChanges = true;
	            		}
	                }
	           }
	    );
	    
		activeContacts = modification.getActiveContacts(); 
		
		if (activeContacts == null) {
			modification.setActiveContacts(new ArrayList<SimpleContactModel>());
			activeContacts = modification.getActiveContacts();
		}
		
		inactiveContacts = modification.getInactiveContacts(); 
		
		if (inactiveContacts == null) {
			modification.setInactiveContacts(new ArrayList<SimpleContactModel>());
			inactiveContacts = modification.getInactiveContacts();
		}
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
		
		if (httpClientHandlerApp == null) { 
			httpClientHandlerApp = (HttpClientHandler) ContactList.this.getApplication();
		}
		
		smartphone = httpClientHandlerApp.smartphone;
	    smartphonesGeneral = httpClientHandlerApp.smartphonesGeneral;
	    selected = httpClientHandlerApp.selected;
	    modification = this.smartphone.getModification();
		
		if (modification == null) {
			this.smartphone.setModification(new ModificationModel());
			modification = this.smartphone.getModification();
		}
		
	    topBanner = (LinearLayout)findViewById(R.id.topBanner);
	    topBannerSaveChanges = (LinearLayout)findViewById(R.id.topBannerSaveChanges);
	    
	    if (httpClientHandlerApp.pendingChanges) {
	    	topBanner.setVisibility(View.GONE);
	    	topBannerSaveChanges.setVisibility(View.VISIBLE);
		}
		else {
			topBannerSaveChanges.setVisibility(View.GONE);
			topBanner.setVisibility(View.VISIBLE);
		}
	}
	
	/**
	 * Sobrecarga el comportamiento cuando se presiona la tecla fisica de "back".
	 * Interceptado para colocar el logout de esta manera tambien.
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	    	if (httpClientHandlerApp.pendingChanges) {
				savePartialChanges();			
			}
			
			httpClientHandlerApp.lastView = PCViewsEnum.CONTACT_LIST_VIEW.getId();
	    }
	    
	    return super.onKeyDown(keyCode, event);
	}
	
	public SimpleContactModel convertToSimpleContactModel(int pos) {
		SimpleContactModel scm = new SimpleContactModel();
		ContactModel contact = this.contacts.get(pos);
		
		scm.setKeyId(contact.getKeyId());
		scm.setFirstName(contact.getFirstName());
		scm.setLastName(contact.getLastName());		
		scm.setPhones(contact.getPhones());
		
		return scm;
	}
	
	public boolean isActive(int pos) {
		boolean isActive = false;
		String contactKey = this.contacts.get(pos).getKeyId();
		
		for (SimpleContactModel sc : activeContacts) {
			if (sc.getKeyId().equals(contactKey)) {
				isActive = true;
				break;
			}
		}
		
		return isActive;
	}
	
	public void deleteActive(int pos) {
		int deletePos = 0;
		String contactKey = this.contacts.get(pos).getKeyId();
		
		for (SimpleContactModel sc : activeContacts) {
			if (sc.getKeyId().equals(contactKey)) {
				break;
			}
			
			deletePos += 1;
		}
		
		activeContacts.remove(deletePos);
	}
	
	public boolean isInactive(int pos) {
		boolean isInactive = false;
		String contactKey = this.contacts.get(pos).getKeyId();
		
		for (SimpleContactModel sc : inactiveContacts) {
			if (sc.getKeyId().equals(contactKey)) {
				isInactive = true;
				break;
			}
		}
		
		return isInactive;
	}
	
	public void deleteInactive(int pos) {
		int deletePos = 0;
		String contactKey = this.contacts.get(pos).getKeyId();
		
		for (SimpleContactModel sc : inactiveContacts) {
			if (sc.getKeyId().equals(contactKey)) {
				break;
			}
			
			deletePos += 1;
		}
		
		inactiveContacts.remove(deletePos);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.layout.contact_submenu, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
		switch (item.getItemId()) {
        case R.id.contactList:
            return true;
        case R.id.emergencyContactList:
        	loadEmergencyContacts();
            return true;
        default:
            return super.onOptionsItemSelected(item);
	    }
	}
	
	public void loadEmergencyContacts() {
		Intent i = new Intent(ContactList.this, EmergencyContactList.class);
		savePartialChanges();
		httpClientHandlerApp.lastView = PCViewsEnum.CONTACT_LIST_VIEW.getId();
		startActivity(i);
	}
	
	public void savePartialChanges() {
		int pos = 0;
		ArrayList<ContactModel> smartActive = this.smartphone.getActiveContacts();
		ArrayList<ContactModel> smartInactive = this.smartphone.getInactiveContacts();
		ContactModel auxContact;
		boolean found = false;
		
		for (SimpleContactModel scm : activeContacts) {
			pos = 0;
			found = false;
			
			for (ContactModel con : smartInactive) {
				if (scm.getKeyId().equals(con.getKeyId())) {
					found = true;
					break;
				}
				
				pos += 1;
			}
			
			if (found) {
				auxContact = smartInactive.get(pos);
				smartActive.add(auxContact);
				smartInactive.remove(pos);
			}
		}
		
		for (SimpleContactModel scm : inactiveContacts) {
			pos = 0;
			found = false;
			
			for (ContactModel con : smartActive) {
				if (scm.getKeyId().equals(con.getKeyId())) {
					found = true;
					break;
				}
				
				pos += 1;
			}
			
			if (found) {
				auxContact = smartActive.get(pos);
				smartInactive.add(auxContact);
				smartActive.remove(pos);
			}
		}
		
		httpClientHandlerApp.smartphone.setActiveContacts(smartActive);
		httpClientHandlerApp.smartphone.setInactiveContacts(smartInactive);
	}
	
	public void onSaveChangesClick(View v) {
		nextScreen = 1;
		showProcessDialog();
    	asyncSaveChangesThread = new SaveChangesThread().execute();
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		if (id == ALERT_DIALOG) {
			AlertDialog.Builder alert = new AlertDialog.Builder(ContactList.this);
	    	alert.setIcon(R.drawable.alert_icon);
            alert.setTitle("Alert");
            alert.setMessage(R.string.save_changes_alert_dialog_message);
            alert.setPositiveButton(R.string.alert_dialog_yes_button_label, new DialogInterface.OnClickListener() {
            	public void onClick(DialogInterface dialog, int whichButton) {
            		dialog.dismiss();
            		
            		onSaveChangesClick(null);
                 }
             });
             alert.setNegativeButton(R.string.alert_dialog_no_button_label, new DialogInterface.OnClickListener() {
                 public void onClick(DialogInterface dialog, int whichButton) { 
                	 dialog.dismiss();
                 }
             });
             
             return alert.create();
		}
		else if (id == ERROR_ALERT_DIALOG) {
			AlertDialog.Builder alert = new AlertDialog.Builder(ContactList.this);
	    	alert.setIcon(R.drawable.alert_icon);
            alert.setTitle("Alert");
            alert.setMessage(errorMessage);
            alert.setPositiveButton(R.string.ok_button_label, new DialogInterface.OnClickListener() {
            	public void onClick(DialogInterface dialog, int whichButton) {
            		dialog.dismiss();
                 }
             });
             
             return alert.create();
		}
		
		return null;
	}
	
	private void showProcessDialog() {
		processDialog = ProgressDialog.show(ContactList.this, null, ContactList.this.getString(R.string.progress_dialog_save_changes), true, true,
			new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					if (asyncSaveChangesThread.cancel(true)) {
						UiUtils.levantarDialog(ContactList.this, UiUtils.DIALOG_OK, getString(R.string.progress_dialog_save_changes_cancel));
					} 
					else {
						if (asyncSaveChangesThread != null) {
							if (!(asyncSaveChangesThread.getStatus().equals(AsyncTask.Status.FINISHED))) {
								showProcessDialog();
							}
						}
					}
				}
			}
		);
	}
	
	public class SaveChangesThread extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... args) {
			try {
				InputStream inputStream = httpClientHandlerApp.saveChanges(smartphonesGeneral.get(selected).getKeyId(), modification);
				BufferedReader rd;
				StringBuffer sb;
				String line;
				JsonParser jsonParser;
				JsonObject jsonResponse;
				JsonObject jsonResponseStatus;
				String code;
				
				if (inputStream != null) {
					rd = new BufferedReader(new InputStreamReader(inputStream));
					sb = new StringBuffer();
					
					while ((line = rd.readLine()) != null) {
						sb.append(line);
					}
						
					rd.close();
					
					jsonParser = new JsonParser();		
					System.out.println("------------------save-changes response: " + sb.toString());
					jsonResponse = (JsonObject)jsonParser.parse(sb.toString());
					jsonResponseStatus = jsonResponse.getAsJsonObject("status");
					code = ((JsonPrimitive)jsonResponseStatus.get("code")).getAsString();
					
					if (code.equals("00")) {			
						Intent i;
						
						if (nextScreen == 0) {
							i = new Intent(ContactList.this, SmartphoneMap.class);
						}
						else {
							i = new Intent(ContactList.this, SmartphoneList.class);
						}
						
						httpClientHandlerApp.pendingChanges = false;
						httpClientHandlerApp.lastView = PCViewsEnum.CONTACT_LIST_VIEW.getId();
						
						startActivity(i);
					}
					else {
						errorMessage = ((JsonPrimitive)jsonResponseStatus.get("msg")).getAsString();
					}
				}
			} 
			catch (Exception e) {
				Log.e("1", "AsyncLogin Error", e);
				e.printStackTrace();
				return false;
			}
			
			return true;
		}

		protected void onPostExecute(Boolean result) {
			if (httpClientHandlerApp.pendingChanges) {
				savePartialChanges();			
			}
			
			processDialog.dismiss();
			
			if (!errorMessage.equals("")) {
				showDialog(ERROR_ALERT_DIALOG);
			}
		}
	}
}
