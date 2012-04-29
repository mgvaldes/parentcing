package com.ing3nia.parentalcontrol.ui;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.reflect.TypeToken;
import com.ing3nia.parentalcontrol.R;
import com.ing3nia.parentalcontrol.httpconnect.HttpClientHandler;
import com.ing3nia.parentalcontrol.models.ModificationModel;
import com.ing3nia.parentalcontrol.models.SmartphoneModel;
import com.ing3nia.parentalcontrol.models.TicketModel;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class NewAdminUser extends Activity {

	private HttpClientHandler httpClientHandlerApp;
	private AsyncTask<Void, Void, Boolean> asyncSaveNewAdminUserThread;
	static ProgressDialog processDialog;
	private LinearLayout topBanner;
	private LinearLayout topBannerSaveChanges;
	private ArrayList<String> adminUsers;
	private ArrayList<SmartphoneModel> smartphonesGeneral;
	private SmartphoneModel smartphone;
	private int selected;
	private static final int ALERT_DIALOG = 1;
	private static final int ERROR_ALERT_DIALOG = 2;
	private AsyncTask<Void, Void, Boolean> asyncSaveChangesThread;
	private AsyncTask<Void, Void, Boolean> asyncTicketsListThread;
	private AsyncTask<Void, Void, Boolean> asyncAdminUserListThread;
	private ModificationModel modification;
	private int nextScreen;
	private String userKey;
	private String errorMessage = "";
	private EditText editTextUsername;
	private EditText editTextPassword;
	private EditText editTextConfirmPassword;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    if (httpClientHandlerApp == null) { 
			httpClientHandlerApp = (HttpClientHandler) NewAdminUser.this.getApplication();
		}
	    
	    adminUsers = httpClientHandlerApp.adminUsers;
	    smartphonesGeneral = httpClientHandlerApp.smartphonesGeneral;
	    smartphone = httpClientHandlerApp.smartphone;
	    selected = httpClientHandlerApp.selected;
	    userKey = httpClientHandlerApp.userKey;
	    
	    setContentView(R.layout.new_user);
	    
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
	    
	    editTextUsername = (EditText) findViewById(R.id.editTextUsername);
	    editTextPassword = (EditText) findViewById(R.id.editTextPassword);
	    editTextConfirmPassword = (EditText) findViewById(R.id.editTextConfirmPassword);
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
		
		if (httpClientHandlerApp == null) { 
			httpClientHandlerApp = (HttpClientHandler) NewAdminUser.this.getApplication();
		}
	    
		adminUsers = httpClientHandlerApp.adminUsers;
		smartphonesGeneral = httpClientHandlerApp.smartphonesGeneral;
	    smartphone = httpClientHandlerApp.smartphone;
	    selected = httpClientHandlerApp.selected;
	    userKey = httpClientHandlerApp.userKey;
	    
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
	
	public boolean checkEditTextFieldsFilled() {
		boolean filled = true;
		
		if (editTextUsername.getText().length() == 0 ||
			editTextPassword.getText().length() == 0 ||
			editTextConfirmPassword.getText().length() == 0) {
			filled = false;
		}
		
		return filled;
	}
	
	public boolean checkPasswordConsistency() {
		boolean filled = false;
		
		if (editTextPassword.getText().toString().equals(editTextConfirmPassword.getText().toString())) {
			filled = true;
		}
		
		return filled;
	}
	
	public void onSaveUserClick(View v) {
		if (checkEditTextFieldsFilled()) {	
			if (checkPasswordConsistency()) {
				showNewAdminUserProcessDialog();
				asyncSaveNewAdminUserThread = new SaveNewAdminUserThread().execute();
			}			
			else {
				Toast.makeText(this, "Passwords don't match. Please try again", Toast.LENGTH_SHORT).show();
			}
		}
		else {
			Toast.makeText(this, "All fields must be filled out!", Toast.LENGTH_SHORT).show();
			//UiUtils.levantarDialog(NewRule.this, UiUtils.DIALOG_OK, getString(R.string.fill_edit_texts_warning_dialog_new_rule));
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.layout.smartphone_menu, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.dailyRoute:
	            return true;
	        case R.id.alertList:
	            loadAlertList();
	            return true;
	        case R.id.ruleList:
	            loadRuleList();
	            return true;
	        case R.id.contactList:
	            loadContactList();
	            return true;
	        case R.id.deviceSettings:
	            loadSmartphoneSettings();
	            return true;
	        case R.id.deviceMap:
	            loadDeviceMap();
	            return true;
	        case R.id.deviceList:
	            loadDeviceList();
	            return true;
	        case R.id.adminUsersList:
	            loadAdminUsersList();
	            return true;
	        case R.id.helpdesk:
	            loadHelpdesk();
	            return true;	            
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
    
	public void loadAlertList() {
		Intent i = new Intent(NewAdminUser.this, AlertList.class);
		httpClientHandlerApp.lastView = PCViewsEnum.DAILY_ROUTE_VIEW.getId();
		startActivity(i);
	}
	
	public void loadRuleList() {
		Intent i = new Intent(NewAdminUser.this, RuleList.class);
		httpClientHandlerApp.lastView = PCViewsEnum.DAILY_ROUTE_VIEW.getId();
		startActivity(i);
	}
	
	public void loadContactList() {
		Intent i = new Intent(NewAdminUser.this, ContactList.class);
		httpClientHandlerApp.lastView = PCViewsEnum.DAILY_ROUTE_VIEW.getId();
		startActivity(i);
	}
	
	public void loadSmartphoneSettings() {
		Intent i = new Intent(NewAdminUser.this, SmartphoneSettings.class);
		httpClientHandlerApp.lastView = PCViewsEnum.DAILY_ROUTE_VIEW.getId();
		startActivity(i);
	}
	
	public void loadDeviceMap() {
		if (httpClientHandlerApp.pendingChanges) {
			nextScreen = 0;
			showDialog(ALERT_DIALOG);
		}
		else {
			Intent i = new Intent(NewAdminUser.this, SmartphoneMap.class);
			startActivity(i);
		}
	}
	
	public void loadDeviceList() {
		if (httpClientHandlerApp.pendingChanges) {
			nextScreen = 1;
			showDialog(ALERT_DIALOG);
		}
		else {
			Intent i = new Intent(NewAdminUser.this, SmartphoneList.class);
			startActivity(i);
		}		
	}
	
	public void loadHelpdesk() {
		showTicketsListProcessDialog();
    	asyncTicketsListThread = new TicketsListThread().execute();
	}
	
	public void loadAdminUsersList() {
		showAdminUserListProcessDialog();
    	asyncAdminUserListThread = new AdminUserListThread().execute();
	}
	
	public void onSaveChangesClick(View v) {
		modification = this.smartphone.getModification();
		
		if (modification == null) {
			this.smartphone.setModification(new ModificationModel());
			modification = this.smartphone.getModification();
		}
		
		nextScreen = 1;
		showProcessDialog();
    	asyncSaveChangesThread = new SaveChangesThread().execute();
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		if (id == ALERT_DIALOG) {
			AlertDialog.Builder alert = new AlertDialog.Builder(NewAdminUser.this);
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
			AlertDialog.Builder alert = new AlertDialog.Builder(NewAdminUser.this);
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
	
	private void showNewAdminUserProcessDialog() {
		processDialog = ProgressDialog.show(NewAdminUser.this, null, NewAdminUser.this.getString(R.string.progress_dialog_new_user), true, true,
			new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					if (asyncSaveNewAdminUserThread.cancel(true)) {
						UiUtils.levantarDialog(NewAdminUser.this, UiUtils.DIALOG_OK, getString(R.string.progress_dialog_new_user_cancel));
					} 
					else {
						if (asyncSaveNewAdminUserThread != null) {
							if (!(asyncSaveNewAdminUserThread.getStatus().equals(AsyncTask.Status.FINISHED))) {
								showNewAdminUserProcessDialog();
							}
						}
					}
				}
			}
		);
	}
	
	private void showProcessDialog() {
		processDialog = ProgressDialog.show(NewAdminUser.this, null, NewAdminUser.this.getString(R.string.progress_dialog_save_changes), true, true,
			new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					if (asyncSaveChangesThread.cancel(true)) {
						UiUtils.levantarDialog(NewAdminUser.this, UiUtils.DIALOG_OK, getString(R.string.progress_dialog_save_changes_cancel));
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
	
	private void showTicketsListProcessDialog() {
		processDialog = ProgressDialog.show(NewAdminUser.this, null, NewAdminUser.this.getString(R.string.progress_dialog_tickets_list), true, true,
			new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					if (asyncTicketsListThread.cancel(true)) {
						UiUtils.levantarDialog(NewAdminUser.this, UiUtils.DIALOG_OK, getString(R.string.progress_dialog_tickets_list_cancel));
					} 
					else {
						if (asyncTicketsListThread != null) {
							if (!(asyncTicketsListThread.getStatus().equals(AsyncTask.Status.FINISHED))) {
								showTicketsListProcessDialog();
							}
						}
					}
				}
			}
		);
	}
	
	private void showAdminUserListProcessDialog() {
		processDialog = ProgressDialog.show(NewAdminUser.this, null, NewAdminUser.this.getString(R.string.progress_dialog_admin_user_list), true, true,
			new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					if (asyncAdminUserListThread.cancel(true)) {
						UiUtils.levantarDialog(NewAdminUser.this, UiUtils.DIALOG_OK, getString(R.string.progress_dialog_admin_user_list_cancel));
					} 
					else {
						if (asyncAdminUserListThread != null) {
							if (!(asyncAdminUserListThread.getStatus().equals(AsyncTask.Status.FINISHED))) {
								showAdminUserListProcessDialog();
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
							i = new Intent(NewAdminUser.this, SmartphoneMap.class);
						}
						else {
							i = new Intent(NewAdminUser.this, SmartphoneList.class);
						}
						
						httpClientHandlerApp.pendingChanges = false;
						httpClientHandlerApp.lastView = PCViewsEnum.DAILY_ROUTE_VIEW.getId();
						
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
			processDialog.dismiss();
			
			if (!errorMessage.equals("")) {
				showDialog(ERROR_ALERT_DIALOG);
			}
		}
	}
	
	public class TicketsListThread extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... args) {
			try {
				InputStream inputStream = httpClientHandlerApp.ticketsList(userKey);
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
					System.out.println("------------------tickets response: " + sb.toString());
					jsonResponse = (JsonObject)jsonParser.parse(sb.toString());
					jsonResponseStatus = jsonResponse.getAsJsonObject("status");
					code = ((JsonPrimitive)jsonResponseStatus.get("code")).getAsString();
					
					if (code.equals("00")) {
						JsonObject ticketsObject = jsonResponse.getAsJsonObject("tickets");
						
						Type ticketListType = new TypeToken<ArrayList<TicketModel>>(){}.getType();
						Gson gson = new Gson();
						
						Intent i = new Intent(NewAdminUser.this, TicketList.class);
						
						httpClientHandlerApp.openTickets = gson.fromJson(ticketsObject.getAsJsonArray("open"), ticketListType);
						httpClientHandlerApp.closedTickets = gson.fromJson(ticketsObject.getAsJsonArray("closed"), ticketListType);
						httpClientHandlerApp.lastView = PCViewsEnum.DAILY_ROUTE_VIEW.getId();
						
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
			processDialog.dismiss();
			
			if (!errorMessage.equals("")) {
				showDialog(ERROR_ALERT_DIALOG);
			}
		}
	}
	
	public class AdminUserListThread extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... args) {
			try {
				InputStream inputStream = httpClientHandlerApp.adminUserList();
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
					System.out.println("------------------users response: " + sb.toString());
					jsonResponse = (JsonObject)jsonParser.parse(sb.toString());
					jsonResponseStatus = jsonResponse.getAsJsonObject("status");
					code = ((JsonPrimitive)jsonResponseStatus.get("code")).getAsString();
					
					if (code.equals("00")) {
						JsonArray usersArray = jsonResponse.getAsJsonArray("admins");
						ArrayList<String> adminUsers = new ArrayList<String>();
						
						for (JsonElement u : usersArray) {
							adminUsers.add(((JsonPrimitive)((JsonObject)u).get("username")).getAsString());
						}
						
						httpClientHandlerApp.adminUsers = adminUsers;
						
						Intent i = new Intent(NewAdminUser.this, AdminUserList.class);
						httpClientHandlerApp.lastView = PCViewsEnum.DAILY_ROUTE_VIEW.getId();
						
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
			processDialog.dismiss();
			
			if (!errorMessage.equals("")) {
				showDialog(ERROR_ALERT_DIALOG);
			}
		}
	}
	
	public class SaveNewAdminUserThread extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... args) {
			try {
				InputStream inputStream = httpClientHandlerApp.saveNewAdminUser(editTextUsername.getText().toString(), editTextPassword.getText().toString());
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
					System.out.println("------------------add-user response: " + sb.toString());
					jsonResponse = (JsonObject)jsonParser.parse(sb.toString());
					jsonResponseStatus = jsonResponse.getAsJsonObject("status");
					code = ((JsonPrimitive)jsonResponseStatus.get("code")).getAsString();
					
					if (code.equals("00")) {								
						adminUsers.add(editTextUsername.getText().toString());
						
						Intent i = new Intent(NewAdminUser.this, AdminUserList.class);
						httpClientHandlerApp.lastView = PCViewsEnum.NEW_ADMIN_USER_VIEW.getId();
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
			processDialog.dismiss();
			
			if (!errorMessage.equals("")) {
				showDialog(ERROR_ALERT_DIALOG);
			}
		}
	}
}
