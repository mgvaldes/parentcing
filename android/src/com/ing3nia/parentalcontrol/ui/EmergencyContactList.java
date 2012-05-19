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
import com.ing3nia.parentalcontrol.models.EmergencyNumberModel;
import com.ing3nia.parentalcontrol.models.ModificationModel;
import com.ing3nia.parentalcontrol.models.SmartphoneModel;
import com.ing3nia.parentalcontrol.ui.components.EmergencyContactListAdapter;
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
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class EmergencyContactList extends Activity {
	
	private ArrayList<SmartphoneModel> smartphonesGeneral;
	private int selected;
	private SmartphoneModel smartphone;
	private boolean[] contactStatus;
	private ModificationModel modification;
	private ArrayList<EmergencyNumberModel> addedEmergencyContacts;
	private ArrayList<EmergencyNumberModel> deletedEmergencyContacts;
	private ArrayList<EmergencyNumberModel> emergencyContacts;
	private int selectedContact;
	private static final int ALERT_DIALOG_EDIT = 3;
	private int selectedContact2;	
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
			httpClientHandlerApp = (HttpClientHandler) EmergencyContactList.this.getApplication();
		}
		
		smartphone = httpClientHandlerApp.smartphone;
	    smartphonesGeneral = httpClientHandlerApp.smartphonesGeneral;
	    selected = httpClientHandlerApp.selected;
	    modification = this.smartphone.getModification();
		
		if (modification == null) {
			this.smartphone.setModification(new ModificationModel());
			modification = this.smartphone.getModification();
		}
		
	    setContentView(R.layout.emergency_contact_list);
	    
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
	    
	    ArrayList<EmergencyNumberModel> addedEmergencyContactsAux = smartphone.getAddedEmergencyNumbers();
		ArrayList<EmergencyNumberModel> deletedEmergencyContactsAux = smartphone.getDeletedEmergencyNumbers();
		emergencyContacts = new ArrayList<EmergencyNumberModel>();
		
		emergencyContacts.addAll(addedEmergencyContactsAux);
		emergencyContacts.addAll(deletedEmergencyContactsAux);
		
		int size = addedEmergencyContactsAux.size() + deletedEmergencyContactsAux.size();
		
		contactStatus = new boolean[size];
		
		int size1 = addedEmergencyContactsAux.size();
		
		for (int i = 0; i < size1; i++) {
			contactStatus[i] = true;
		}
		
		for (int i = size1; i < size; i++) {
			contactStatus[i] = false;
		}
	    
	    ListView contactList = (ListView)findViewById(R.id.emergencyContactList);
	    contactList.setAdapter(new EmergencyContactListAdapter(this, emergencyContacts, contactStatus));
	    contactList.setOnItemClickListener(
	    		new OnItemClickListener() {
	                @Override
	                public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
	                	ImageView contactStatusImageView = (ImageView)view.findViewById(R.id.contactStatus);
	                	
	                	if (contactStatus[position]) {             		
	                		contactStatusImageView.setImageResource(R.drawable.ic_contact_disallow);
	                		contactStatus[position] = false;
	                		
	                		if (isAdded(position)) {		                		
		                		deleteAdded(position);
		                	}
	                		
	                		deletedEmergencyContacts.add(emergencyContacts.get(position));
	                	}
	                	else {
	                		contactStatusImageView.setImageResource(R.drawable.ic_contact_allow);
	                		contactStatus[position] = true;
	                		
	                		if (isDeleted(position)) {		                		
		                		deleteDeleted(position);
		                	}
	                		
	                		addedEmergencyContacts.add(emergencyContacts.get(position));
	                	}
	                	
	                	if (!httpClientHandlerApp.pendingChanges) {
	            	    	topBanner.setVisibility(View.GONE);
	            	    	topBannerSaveChanges.setVisibility(View.VISIBLE);
	            	    	httpClientHandlerApp.pendingChanges = true;
	            		}
	                }
	           }
	    );
	    
	    contactList.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View view, int position, long id) {
				selectedContact = position;
            	showDialog(ALERT_DIALOG_EDIT);
				
				return false;
			}
	    });
	    
		addedEmergencyContacts = modification.getAddedEmergencyNumbers();
		
		if (addedEmergencyContacts == null) {
			modification.setAddedEmergencyNumbers(new ArrayList<EmergencyNumberModel>());
			addedEmergencyContacts = modification.getAddedEmergencyNumbers();
		}
		
		deletedEmergencyContacts = modification.getDeletedEmergencyNumbers();
		
		if (deletedEmergencyContacts == null) {
			modification.setDeletedEmergencyNumbers(new ArrayList<EmergencyNumberModel>());
			deletedEmergencyContacts = modification.getDeletedEmergencyNumbers();
		}
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
		
		if (httpClientHandlerApp == null) { 
			httpClientHandlerApp = (HttpClientHandler) EmergencyContactList.this.getApplication();
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
			
			httpClientHandlerApp.lastView = PCViewsEnum.EMERGENCY_CONTACT_LIST_VIEW.getId();
	    }
	    
	    return super.onKeyDown(keyCode, event);
	}
	
	public boolean isAdded(int pos) {
		boolean isAdded = false;
		String contactKey = this.emergencyContacts.get(pos).getKeyId();
		
		for (EmergencyNumberModel enm : addedEmergencyContacts) {
			if (enm.getKeyId() == null || enm.getKeyId().equals(contactKey)) {
				isAdded = true;
				break;
			}
		}
		
		return isAdded;
	}
	
	public void deleteAdded(int pos) {
		int deletePos = 0;
		String contactKey = this.emergencyContacts.get(pos).getKeyId();
		
		for (EmergencyNumberModel enm : addedEmergencyContacts) {
			if (enm.getKeyId() == null || enm.getKeyId().equals(contactKey)) {
				break;
			}
			
			deletePos += 1;
		}
		
		addedEmergencyContacts.remove(deletePos);
	}
	
	public boolean isDeleted(int pos) {
		boolean isDeleted = false;
		String contactKey = this.emergencyContacts.get(pos).getKeyId();
		
		for (EmergencyNumberModel enm : deletedEmergencyContacts) {
			if (enm.getKeyId().equals(contactKey)) {
				isDeleted = true;
				break;
			}
		}
		
		return isDeleted;
	}
	
	public void deleteDeleted(int pos) {
		int deletePos = 0;
		String contactKey = this.emergencyContacts.get(pos).getKeyId();
		
		for (EmergencyNumberModel enm : deletedEmergencyContacts) {
			if (enm.getKeyId().equals(contactKey)) {
				break;
			}
			
			deletePos += 1;
		}
		
		deletedEmergencyContacts.remove(deletePos);
	}
	
	public boolean isAddedOrDeletedContact() {
		boolean isAddedOrDeleted = true;
		
		int addedSize = this.smartphone.getAddedEmergencyNumbers().size();
		
		if (selectedContact < addedSize) {
			selectedContact2 = selectedContact;
			isAddedOrDeleted = true;
		}
		else {
			selectedContact2 = selectedContact - addedSize;
			isAddedOrDeleted = false;
		}
		
		return isAddedOrDeleted;
	}
	
	public void onNewEmergencyContactClick(View v) {
		Intent i = new Intent(EmergencyContactList.this, NewEmergencyContact.class);
		httpClientHandlerApp.lastView = PCViewsEnum.EMERGENCY_CONTACT_LIST_VIEW.getId();
		startActivity(i);
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
        	loadContacts();
            return true;
        case R.id.emergencyContactList:
            return true;
        default:
            return super.onOptionsItemSelected(item);
	    }
	}
	
	public void loadContacts() {
		Intent i = new Intent(EmergencyContactList.this, ContactList.class);
		savePartialChanges();
		httpClientHandlerApp.lastView = PCViewsEnum.EMERGENCY_CONTACT_LIST_VIEW.getId();
		startActivity(i);
	}
	
	public void savePartialChanges() {
		int pos = 0;
		ArrayList<EmergencyNumberModel> smartAdded = this.smartphone.getAddedEmergencyNumbers();
		ArrayList<EmergencyNumberModel> smartDeleted = this.smartphone.getDeletedEmergencyNumbers();
		EmergencyNumberModel auxContact;
		boolean found = false;
		
		for (EmergencyNumberModel scm : addedEmergencyContacts) {
			pos = 0;
			found = false;
			
			for (EmergencyNumberModel con : smartDeleted) {
				if (scm.getKeyId() == null || scm.getKeyId().equals(con.getKeyId())) {
					found = true;
					break;
				}
				
				pos += 1;
			}
			
			if (found) {
				auxContact = smartDeleted.get(pos);
				smartAdded.add(auxContact);
				smartDeleted.remove(pos);
			}
		}
		
		for (EmergencyNumberModel scm : deletedEmergencyContacts) {
			pos = 0;
			found = false;
			
			for (EmergencyNumberModel con : smartAdded) {
				if (scm.getKeyId() == null || scm.getKeyId().equals(con.getKeyId())) {
					found = true;
					break;
				}
				
				pos += 1;
			}
			
			if (found) {
				auxContact = smartAdded.get(pos);
				smartDeleted.add(auxContact);
				smartAdded.remove(pos);
			}
		}
		
		httpClientHandlerApp.smartphone.setAddedEmergencyNumbers(smartAdded);
		httpClientHandlerApp.smartphone.setDeletedEmergencyNumbers(smartDeleted);
    }
	
	public void onSaveChangesClick(View v) {
		nextScreen = 1;
		showProcessDialog();
    	asyncSaveChangesThread = new SaveChangesThread().execute();
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		if (id == ALERT_DIALOG_EDIT) {
			AlertDialog.Builder alert = new AlertDialog.Builder(EmergencyContactList.this);
	    	alert.setIcon(R.drawable.alert_icon);
            alert.setTitle("Alert");
            alert.setMessage(R.string.rule_list_alert_dialog_message);
            alert.setPositiveButton(R.string.alert_dialog_edit_button_label, new DialogInterface.OnClickListener() {
            	public void onClick(DialogInterface dialog, int whichButton) {
            		dialog.dismiss();
                 	
            		Intent i = new Intent(EmergencyContactList.this, EditEmergencyContact.class);
            		
            		Bundle bundle = new Bundle();            		
            		bundle.putParcelable("emergencyContact", emergencyContacts.get(selectedContact));
            		bundle.putBoolean("isAddedOrDeleted", isAddedOrDeletedContact());
            		bundle.putInt("selectedContact", selectedContact2);
            		i.putExtra("smartphonesInfo", bundle);
            		
            		startActivity(i);
                 }
             });
             alert.setNegativeButton(R.string.alert_dialog_delete_button_label, new DialogInterface.OnClickListener() {
                 public void onClick(DialogInterface dialog, int whichButton) { 
                	 dialog.dismiss();
                 }
             });
             
             return alert.create();
		}
		else if (id == ALERT_DIALOG) {
			AlertDialog.Builder alert = new AlertDialog.Builder(EmergencyContactList.this);
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
			AlertDialog.Builder alert = new AlertDialog.Builder(EmergencyContactList.this);
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
		processDialog = ProgressDialog.show(EmergencyContactList.this, null, EmergencyContactList.this.getString(R.string.progress_dialog_save_changes), true, true,
			new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					if (asyncSaveChangesThread.cancel(true)) {
						UiUtils.levantarDialog(EmergencyContactList.this, UiUtils.DIALOG_OK, getString(R.string.progress_dialog_save_changes_cancel));
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
							i = new Intent(EmergencyContactList.this, SmartphoneMap.class);
						}
						else {
							i = new Intent(EmergencyContactList.this, SmartphoneList.class);
						}
						
						httpClientHandlerApp.pendingChanges = false;
						httpClientHandlerApp.lastView = PCViewsEnum.EMERGENCY_CONTACT_LIST_VIEW.getId();
						
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
