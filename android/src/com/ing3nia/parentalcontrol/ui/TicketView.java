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
import com.ing3nia.parentalcontrol.models.TicketAnswerModel;
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
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;

public class TicketView extends Activity {
	private HttpClientHandler httpClientHandlerApp;
	private LinearLayout topBanner;
	private LinearLayout topBannerSaveChanges;
	TicketModel ticket;
	private ArrayList<SmartphoneModel> smartphonesGeneral;
	private SmartphoneModel smartphone;
	private int selected;
	private static final int ALERT_DIALOG = 1;
	private AsyncTask<Void, Void, Boolean> asyncSaveChangesThread;
	private AsyncTask<Void, Void, Boolean> asyncTicketsListThread;
	private AsyncTask<Void, Void, Boolean> asyncAdminUserListThread;
	private AsyncTask<Void, Void, Boolean> asyncAnswerTicketThread;
	private ModificationModel modification;
	static ProgressDialog processDialog;
	private int nextScreen;
	private String userKey;
	private EditText editTextComment;
	private int selectedTicket;
	private boolean status;
	private String errorMessage = "";
	private static final int ERROR_ALERT_DIALOG = 2;
	
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		
	    if (httpClientHandlerApp == null) { 
			httpClientHandlerApp = (HttpClientHandler) TicketView.this.getApplication();
		}
		
		ticket = getIntent().getBundleExtra("ticketInfo").getParcelable("ticket");
		selectedTicket = getIntent().getBundleExtra("ticketInfo").getInt("selectedTicket");
		status = getIntent().getBundleExtra("ticketInfo").getBoolean("status");
		smartphonesGeneral = httpClientHandlerApp.smartphonesGeneral;
	    smartphone = httpClientHandlerApp.smartphone;
	    selected = httpClientHandlerApp.selected;
	    userKey = httpClientHandlerApp.userKey;
		
	    setContentView(R.layout.view_ticket);
	    
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

	    TextView category = (TextView) findViewById(R.id.category);
	    category.setText(ticket.getCategory());
	    
	    TextView subject = (TextView) findViewById(R.id.subject);
	    subject.setText(ticket.getSubject());
	    
	    TextView comment = (TextView) findViewById(R.id.comment);
	    comment.setText(ticket.getComment());
	    
	    ArrayList<TicketAnswerModel> answers = ticket.getAnswers();
	    LinearLayout commentList = (LinearLayout)findViewById(R.id.commentList);
	    
	    for (TicketAnswerModel answer : answers) {
	    	LinearLayout row = new LinearLayout(this);
	    	row.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
	    	row.setPadding(11, 11, 11, 11);
	    	row.setOrientation(LinearLayout.VERTICAL);
	    	
	    	TextView user = new TextView(this);
	    	user.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
	    	user.setText("PRC " + answer.getUsername() + ":");
	    	user.setTextColor(Color.WHITE);
	    	user.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
	    	user.setTypeface(null, Typeface.BOLD);
	    	row.addView(user);
	    	
	    	TextView answerComment = new TextView(this);
	    	answerComment.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
	    	answerComment.setText(answer.getAnswer());
	    	answerComment.setTextColor(Color.WHITE);
	    	answerComment.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
	    	answerComment.setPadding(0, 5, 0, 5);
	    	row.addView(answerComment);
	    	
	    	TextView date = new TextView(this);
	    	date.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
	    	date.setText(answer.getDate());
	    	date.setTextColor(Color.parseColor("#878787"));
	    	date.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
	    	row.addView(date);
	    	
	    	commentList.addView(row);
	    	
    		ImageView separator = new ImageView(this);
    	    separator.setImageResource(android.R.drawable.divider_horizontal_dark);
    	    separator.setScaleType(ScaleType.FIT_XY);
    	    separator.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, 2));
    	    separator.setBackgroundColor(Color.parseColor("#404040"));
    	    commentList.addView(separator);
	    }
	    
	    if (!status) {
	    	LinearLayout replySection = (LinearLayout)findViewById(R.id.replySection);
	    	replySection.setVisibility(View.GONE);
	    }	 
	    else {
	    	editTextComment = (EditText)findViewById(R.id.editTextComment);
	    }
	    
//	    ListView commentList = (ListView)findViewById(R.id.commentList);
//	    commentList.setAdapter(new TicketAnswerListAdapter(this, ticket.getAnswers()));
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
		
		if (httpClientHandlerApp == null) { 
			httpClientHandlerApp = (HttpClientHandler) TicketView.this.getApplication();
		}
		
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
	
	public void onReplyTicketClick(View v) {
		if (!editTextComment.getText().equals("")) {			
			showAnswerTicketProcessDialog();
	    	asyncAnswerTicketThread = new AnswerTicketThread().execute();
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
		Intent i = new Intent(TicketView.this, AlertList.class);
		httpClientHandlerApp.lastView = PCViewsEnum.DAILY_ROUTE_VIEW.getId();
		startActivity(i);
	}
	
	public void loadRuleList() {
		Intent i = new Intent(TicketView.this, RuleList.class);
		httpClientHandlerApp.lastView = PCViewsEnum.DAILY_ROUTE_VIEW.getId();
		startActivity(i);
	}
	
	public void loadContactList() {
		Intent i = new Intent(TicketView.this, ContactList.class);
		httpClientHandlerApp.lastView = PCViewsEnum.DAILY_ROUTE_VIEW.getId();
		startActivity(i);
	}
	
	public void loadSmartphoneSettings() {
		Intent i = new Intent(TicketView.this, SmartphoneSettings.class);
		httpClientHandlerApp.lastView = PCViewsEnum.DAILY_ROUTE_VIEW.getId();
		startActivity(i);
	}
	
	public void loadDeviceMap() {
		if (httpClientHandlerApp.pendingChanges) {
			nextScreen = 0;
			showDialog(ALERT_DIALOG);
		}
		else {
			Intent i = new Intent(TicketView.this, SmartphoneMap.class);
			startActivity(i);
		}
	}
	
	public void loadDeviceList() {
		if (httpClientHandlerApp.pendingChanges) {
			nextScreen = 1;
			showDialog(ALERT_DIALOG);
		}
		else {
			Intent i = new Intent(TicketView.this, SmartphoneList.class);
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
			AlertDialog.Builder alert = new AlertDialog.Builder(TicketView.this);
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
			AlertDialog.Builder alert = new AlertDialog.Builder(TicketView.this);
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
		processDialog = ProgressDialog.show(TicketView.this, null, TicketView.this.getString(R.string.progress_dialog_save_changes), true, true,
			new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					if (asyncSaveChangesThread.cancel(true)) {
						UiUtils.levantarDialog(TicketView.this, UiUtils.DIALOG_OK, getString(R.string.progress_dialog_save_changes_cancel));
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
		processDialog = ProgressDialog.show(TicketView.this, null, TicketView.this.getString(R.string.progress_dialog_tickets_list), true, true,
			new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					if (asyncTicketsListThread.cancel(true)) {
						UiUtils.levantarDialog(TicketView.this, UiUtils.DIALOG_OK, getString(R.string.progress_dialog_tickets_list_cancel));
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
		processDialog = ProgressDialog.show(TicketView.this, null, TicketView.this.getString(R.string.progress_dialog_admin_user_list), true, true,
			new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					if (asyncAdminUserListThread.cancel(true)) {
						UiUtils.levantarDialog(TicketView.this, UiUtils.DIALOG_OK, getString(R.string.progress_dialog_admin_user_list_cancel));
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
	
	private void showAnswerTicketProcessDialog() {
		processDialog = ProgressDialog.show(TicketView.this, null, TicketView.this.getString(R.string.progress_dialog_answer_ticket), true, true,
			new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					if (asyncAnswerTicketThread.cancel(true)) {
						UiUtils.levantarDialog(TicketView.this, UiUtils.DIALOG_OK, getString(R.string.progress_dialog_answer_ticket_cancel));
					} 
					else {
						if (asyncAnswerTicketThread != null) {
							if (!(asyncAnswerTicketThread.getStatus().equals(AsyncTask.Status.FINISHED))) {
								showAnswerTicketProcessDialog();
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
							i = new Intent(TicketView.this, SmartphoneMap.class);
						}
						else {
							i = new Intent(TicketView.this, SmartphoneList.class);
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
						
						Intent i = new Intent(TicketView.this, TicketList.class);
						
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
						
						Intent i = new Intent(TicketView.this, AdminUserList.class);
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
	
	public class AnswerTicketThread extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... args) {
			try {
				TicketAnswerModel answer = new TicketAnswerModel();
				answer.setUserKey(userKey);
				answer.setAnswer(editTextComment.getText().toString());
				answer.setUsername(httpClientHandlerApp.username);
				
				InputStream inputStream = httpClientHandlerApp.answerTicket(answer, ticket.getKey());
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
					System.out.println("------------------answer-ticket response: " + sb.toString());
					jsonResponse = (JsonObject)jsonParser.parse(sb.toString());
					jsonResponseStatus = jsonResponse.getAsJsonObject("status");
					code = ((JsonPrimitive)jsonResponseStatus.get("code")).getAsString();
					
					if (code.equals("00")) {
						httpClientHandlerApp.openTickets.get(selectedTicket).getAnswers().add(answer);
						ticket.getAnswers().add(answer);
						
						Intent i = new Intent(TicketView.this, TicketView.class);
						TicketView.this.finish();
						
						Bundle bundle = new Bundle();
						bundle.putParcelable("ticket", ticket);
						bundle.putInt("selectedTicket", selectedTicket);
						bundle.putBoolean("status", status);
						i.putExtra("ticketInfo", bundle);
						
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
