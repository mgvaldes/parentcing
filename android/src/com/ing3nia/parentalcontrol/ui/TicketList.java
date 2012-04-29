package com.ing3nia.parentalcontrol.ui;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
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
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TicketList extends Activity {
	
	private HttpClientHandler httpClientHandlerApp;
	private AsyncTask<Void, Void, Boolean> asyncCloseTicketThread;
	static ProgressDialog processDialog;
	private LinearLayout topBanner;
	private LinearLayout topBannerSaveChanges;
	private ArrayList<TicketModel> openTickets;
	private ArrayList<TicketModel> closedTickets;
	private int selectedTicket;
	private ArrayList<SmartphoneModel> smartphonesGeneral;
	private SmartphoneModel smartphone;
	private int selected;
	private static final int ALERT_DIALOG = 3;
	private AsyncTask<Void, Void, Boolean> asyncSaveChangesThread;
	private AsyncTask<Void, Void, Boolean> asyncAdminUserListThread;
	private ModificationModel modification;
	private int nextScreen;	
	private static final int ALERT_DIALOG_CLOSE_VIEW = 1;
	private static final int ALERT_DIALOG_VIEW = 2;
	private String errorMessage = "";
	private static final int ERROR_ALERT_DIALOG = 3;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		
	    if (httpClientHandlerApp == null) { 
			httpClientHandlerApp = (HttpClientHandler) TicketList.this.getApplication();
		}
		
		openTickets = httpClientHandlerApp.openTickets;
		closedTickets = httpClientHandlerApp.closedTickets;
		smartphonesGeneral = httpClientHandlerApp.smartphonesGeneral;
	    smartphone = httpClientHandlerApp.smartphone;
	    selected = httpClientHandlerApp.selected;
		
	    setContentView(R.layout.tickets_list);
	    
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
	    
	    loadTicketLists();
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
		
		if (httpClientHandlerApp == null) { 
			httpClientHandlerApp = (HttpClientHandler) TicketList.this.getApplication();
		}
	    
		openTickets = httpClientHandlerApp.openTickets;
		closedTickets = httpClientHandlerApp.closedTickets;
		smartphonesGeneral = httpClientHandlerApp.smartphonesGeneral;
	    smartphone = httpClientHandlerApp.smartphone;
	    selected = httpClientHandlerApp.selected;
	    
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
	
	public void loadTicketLists() {
		LinearLayout openList = (LinearLayout)findViewById(R.id.openList);
	    int position = 0;
	    
	    for (TicketModel ticket : openTickets) {
	    	LinearLayout row = new LinearLayout(this);
	    	row.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
	    	row.setPadding(11, 11, 11, 11);
	    	row.setOrientation(LinearLayout.VERTICAL);
	    	row.setTag(position);
	    	row.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
                	selectedTicket = (Integer)v.getTag();
                	showDialog(ALERT_DIALOG_CLOSE_VIEW);
				}
			});
	    	
	    	TextView category = new TextView(this);
	    	category.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
	    	category.setText(ticket.getCategory());
	    	category.setTextColor(Color.WHITE);
	    	category.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
	    	category.setTypeface(null, Typeface.BOLD);
	    	row.addView(category);
	    	
	    	TextView subject = new TextView(this);
	    	subject.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
	    	subject.setText(ticket.getSubject());
	    	subject.setTextColor(Color.parseColor("#878787"));
	    	subject.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
	    	subject.setPadding(0, 5, 0, 5);
	    	row.addView(subject);
	    	
	    	TextView date = new TextView(this);
	    	date.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
	    	date.setText(ticket.getDate());
	    	date.setTextColor(Color.parseColor("#878787"));
	    	date.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
	    	row.addView(date);
	    	
	    	openList.addView(row);
	    	
    		ImageView separator = new ImageView(this);
    	    separator.setImageResource(android.R.drawable.divider_horizontal_dark);
    	    separator.setScaleType(ScaleType.FIT_XY);
    	    separator.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, 2));
    	    separator.setBackgroundColor(Color.parseColor("#404040"));
    	    openList.addView(separator);
    	    
    	    position += 1;
	    }
	    
	    LinearLayout closedList = (LinearLayout)findViewById(R.id.closedList);
	    position = 0;
	    
	    for (TicketModel ticket : closedTickets) {
	    	LinearLayout row = new LinearLayout(this);
	    	row.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
	    	row.setPadding(11, 11, 11, 11);
	    	row.setOrientation(LinearLayout.VERTICAL);
	    	row.setTag(position);
	    	row.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
                	selectedTicket = (Integer)v.getTag();
                	showDialog(ALERT_DIALOG_VIEW);
				}
			});
	    	
	    	TextView category = new TextView(this);
	    	category.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
	    	category.setText(ticket.getCategory());
	    	category.setTextColor(Color.WHITE);
	    	category.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
	    	category.setTypeface(null, Typeface.BOLD);
	    	row.addView(category);
	    	
	    	TextView subject = new TextView(this);
	    	subject.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
	    	subject.setText(ticket.getSubject());
	    	subject.setTextColor(Color.parseColor("#878787"));
	    	subject.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
	    	subject.setPadding(0, 5, 0, 5);
	    	row.addView(subject);
	    	
	    	TextView date = new TextView(this);
	    	date.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
	    	date.setText(ticket.getDate());
	    	date.setTextColor(Color.parseColor("#878787"));
	    	date.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
	    	row.addView(date);
	    	
	    	closedList.addView(row);
	    	
    		ImageView separator = new ImageView(this);
    	    separator.setImageResource(android.R.drawable.divider_horizontal_dark);
    	    separator.setScaleType(ScaleType.FIT_XY);
    	    separator.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, 2));
    	    separator.setBackgroundColor(Color.parseColor("#404040"));
    	    closedList.addView(separator);
    	    
    	    position += 1;
	    }
	}
	
	public void onNewTicketClick(View v) {
		Intent i = new Intent(TicketList.this, NewTicket.class);
		httpClientHandlerApp.lastView = PCViewsEnum.TICKETS_LIST_VIEW.getId();
		startActivity(i);
	}
	
	public void loadDummyTickets() {
		openTickets = new ArrayList<TicketModel>();
		closedTickets = new ArrayList<TicketModel>();
		
		TicketModel auxTicket = new TicketModel();
		auxTicket.setCategory("Browser Access1");
		auxTicket.setSubject("cant browse");
		auxTicket.setDate("Wednesday, 2011 December 14");
		auxTicket.setComment("Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.");
		
		ArrayList<TicketAnswerModel> answers = new ArrayList<TicketAnswerModel>();
		TicketAnswerModel auxAnswer = new TicketAnswerModel();
		auxAnswer.setUsername("Admin");
		auxAnswer.setAnswer("Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.");
		auxAnswer.setDate("Wednesday, 2011 December 14");
		answers.add(auxAnswer);
		
		auxAnswer = new TicketAnswerModel();
		auxAnswer.setUsername("Admin");
		auxAnswer.setAnswer("Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.");
		auxAnswer.setDate("Wednesday, 2011 December 14");
		answers.add(auxAnswer);
		
		auxTicket.setAnswers(answers);
		
		openTickets.add(auxTicket);
		
		auxTicket = new TicketModel();
		auxTicket.setCategory("Browser Access");
		auxTicket.setSubject("cant browse");
		auxTicket.setDate("Wednesday, 2011 December 14");
		auxTicket.setComment("Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.");
		
		answers = new ArrayList<TicketAnswerModel>();
		auxAnswer = new TicketAnswerModel();
		auxAnswer.setUsername("Admin");
		auxAnswer.setAnswer("Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.");
		auxAnswer.setDate("Wednesday, 2011 December 14");
		answers.add(auxAnswer);
		
		auxAnswer = new TicketAnswerModel();
		auxAnswer.setUsername("Admin");
		auxAnswer.setAnswer("Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.");
		auxAnswer.setDate("Wednesday, 2011 December 14");
		answers.add(auxAnswer);
		
		auxTicket.setAnswers(answers);
		
		openTickets.add(auxTicket);
		
		auxTicket = new TicketModel();
		auxTicket.setCategory("Browser Access");
		auxTicket.setSubject("cant browse");
		auxTicket.setDate("Wednesday, 2011 December 14");
		auxTicket.setComment("Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.");
		
		answers = new ArrayList<TicketAnswerModel>();
		auxAnswer = new TicketAnswerModel();
		auxAnswer.setUsername("Admin");
		auxAnswer.setAnswer("Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.");
		auxAnswer.setDate("Wednesday, 2011 December 14");
		answers.add(auxAnswer);
		
		auxAnswer = new TicketAnswerModel();
		auxAnswer.setUsername("Admin");
		auxAnswer.setAnswer("Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.");
		auxAnswer.setDate("Wednesday, 2011 December 14");
		answers.add(auxAnswer);
		
		openTickets.add(auxTicket);
		
		auxTicket = new TicketModel();
		auxTicket.setCategory("Browser Access");
		auxTicket.setSubject("cant browse");
		auxTicket.setDate("Wednesday, 2011 December 14");
		auxTicket.setComment("Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.");
		
		answers = new ArrayList<TicketAnswerModel>();
		auxAnswer = new TicketAnswerModel();
		auxAnswer.setUsername("Admin");
		auxAnswer.setAnswer("Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.");
		auxAnswer.setDate("Wednesday, 2011 December 14");
		answers.add(auxAnswer);
		
		auxAnswer = new TicketAnswerModel();
		auxAnswer.setUsername("Admin");
		auxAnswer.setAnswer("Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.");
		auxAnswer.setDate("Wednesday, 2011 December 14");
		answers.add(auxAnswer);
		
		closedTickets.add(auxTicket);
		
		auxTicket = new TicketModel();
		auxTicket.setCategory("Browser Access");
		auxTicket.setSubject("cant browse");
		auxTicket.setDate("Wednesday, 2011 December 14");
		auxTicket.setComment("Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.");
		
		answers = new ArrayList<TicketAnswerModel>();
		auxAnswer = new TicketAnswerModel();
		auxAnswer.setUsername("Admin");
		auxAnswer.setAnswer("Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.");
		auxAnswer.setDate("Wednesday, 2011 December 14");
		answers.add(auxAnswer);
		
		auxAnswer = new TicketAnswerModel();
		auxAnswer.setUsername("Admin");
		auxAnswer.setAnswer("Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.");
		auxAnswer.setDate("Wednesday, 2011 December 14");
		answers.add(auxAnswer);
		
		closedTickets.add(auxTicket);
		
		auxTicket = new TicketModel();
		auxTicket.setCategory("Browser Access");
		auxTicket.setSubject("cant browse");
		auxTicket.setDate("Wednesday, 2011 December 14");
		auxTicket.setComment("Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.");
		
		answers = new ArrayList<TicketAnswerModel>();
		auxAnswer = new TicketAnswerModel();
		auxAnswer.setUsername("Admin");
		auxAnswer.setAnswer("Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.");
		auxAnswer.setDate("Wednesday, 2011 December 14");
		answers.add(auxAnswer);
		
		auxAnswer = new TicketAnswerModel();
		auxAnswer.setUsername("Admin");
		auxAnswer.setAnswer("Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.");
		auxAnswer.setDate("Wednesday, 2011 December 14");
		answers.add(auxAnswer);
		
		closedTickets.add(auxTicket);
		
		auxTicket = new TicketModel();
		auxTicket.setCategory("Browser Access");
		auxTicket.setSubject("cant browse");
		auxTicket.setDate("Wednesday, 2011 December 14");
		auxTicket.setComment("Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.");
		
		answers = new ArrayList<TicketAnswerModel>();
		auxAnswer = new TicketAnswerModel();
		auxAnswer.setUsername("Admin");
		auxAnswer.setAnswer("Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.");
		auxAnswer.setDate("Wednesday, 2011 December 14");
		answers.add(auxAnswer);
		
		auxAnswer = new TicketAnswerModel();
		auxAnswer.setUsername("Admin");
		auxAnswer.setAnswer("Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.");
		auxAnswer.setDate("Wednesday, 2011 December 14");
		answers.add(auxAnswer);
		
		closedTickets.add(auxTicket);		
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
	            return true;	            
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
    
	public void loadAlertList() {
		Intent i = new Intent(TicketList.this, AlertList.class);
		httpClientHandlerApp.lastView = PCViewsEnum.DAILY_ROUTE_VIEW.getId();
		startActivity(i);
	}
	
	public void loadRuleList() {
		Intent i = new Intent(TicketList.this, RuleList.class);
		httpClientHandlerApp.lastView = PCViewsEnum.DAILY_ROUTE_VIEW.getId();
		startActivity(i);
	}
	
	public void loadContactList() {
		Intent i = new Intent(TicketList.this, ContactList.class);
		httpClientHandlerApp.lastView = PCViewsEnum.DAILY_ROUTE_VIEW.getId();
		startActivity(i);
	}
	
	public void loadSmartphoneSettings() {
		Intent i = new Intent(TicketList.this, SmartphoneSettings.class);
		httpClientHandlerApp.lastView = PCViewsEnum.DAILY_ROUTE_VIEW.getId();
		startActivity(i);
	}
	
	public void loadDeviceMap() {
		if (httpClientHandlerApp.pendingChanges) {
			nextScreen = 0;
			showDialog(ALERT_DIALOG);
		}
		else {
			Intent i = new Intent(TicketList.this, SmartphoneMap.class);
			startActivity(i);
		}
	}
	
	public void loadDeviceList() {
		if (httpClientHandlerApp.pendingChanges) {
			nextScreen = 1;
			showDialog(ALERT_DIALOG);
		}
		else {
			Intent i = new Intent(TicketList.this, SmartphoneList.class);
			startActivity(i);
		}		
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
		if (id == ALERT_DIALOG_CLOSE_VIEW) {
			AlertDialog.Builder alert = new AlertDialog.Builder(TicketList.this);
	    	alert.setIcon(R.drawable.alert_icon);
            alert.setTitle("Alert");
            alert.setMessage(R.string.rule_list_alert_dialog_message);
            alert.setPositiveButton(R.string.alert_dialog_close_ticket_button_label, new DialogInterface.OnClickListener() {
            	public void onClick(DialogInterface dialog, int whichButton) {
            		dialog.dismiss();
            		
            		showCloseTicketProcessDialog();
        	    	asyncCloseTicketThread = new CloseTicketThread().execute();
                 }
             });
             alert.setNegativeButton(R.string.alert_dialog_view_ticket_button_label, new DialogInterface.OnClickListener() {
                 public void onClick(DialogInterface dialog, int whichButton) { 
                	 dialog.dismiss();
                	 
                	 TicketModel ticket = openTickets.get(selectedTicket);
                  	
                	 Intent i = new Intent(TicketList.this, TicketView.class);
                  	
                	 Bundle bundle = new Bundle();
                	 bundle.putParcelable("ticket", ticket);
                	 bundle.putInt("selectedTicket", selectedTicket);
                	 bundle.putBoolean("status", true);
                	 i.putExtra("ticketInfo", bundle);

                  	startActivity(i);
                 }
             });
             
             return alert.create();
		}
		else if (id == ALERT_DIALOG_VIEW) {
			AlertDialog.Builder alert = new AlertDialog.Builder(TicketList.this);
	    	alert.setIcon(R.drawable.alert_icon);
            alert.setTitle("Alert");
            alert.setMessage(R.string.rule_list_alert_dialog_message);
            alert.setPositiveButton(R.string.alert_dialog_view_ticket_button_label, new DialogInterface.OnClickListener() {
            	public void onClick(DialogInterface dialog, int whichButton) {
            		dialog.dismiss();
            		
            		TicketModel ticket = closedTickets.get(selectedTicket);
                  	
            		Intent i = new Intent(TicketList.this, TicketView.class);
                 	
            		Bundle bundle = new Bundle();
            		bundle.putParcelable("ticket", ticket);
            		bundle.putInt("selectedTicket", selectedTicket);
            		bundle.putBoolean("status", false);
            		i.putExtra("ticketInfo", bundle);

                 	startActivity(i);
                 }
             });
             
             return alert.create();
		}
		else if (id == ALERT_DIALOG) {
			AlertDialog.Builder alert = new AlertDialog.Builder(TicketList.this);
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
			AlertDialog.Builder alert = new AlertDialog.Builder(TicketList.this);
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
	
	private void showCloseTicketProcessDialog() {
		processDialog = ProgressDialog.show(TicketList.this, null, TicketList.this.getString(R.string.progress_dialog_close_ticket), true, true,
			new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					if (asyncCloseTicketThread.cancel(true)) {
						UiUtils.levantarDialog(TicketList.this, UiUtils.DIALOG_OK, getString(R.string.progress_dialog_close_ticket_cancel));
					} 
					else {
						if (asyncCloseTicketThread != null) {
							if (!(asyncCloseTicketThread.getStatus().equals(AsyncTask.Status.FINISHED))) {
								showCloseTicketProcessDialog();
							}
						}
					}
				}
			}
		);
	}
	
	private void showProcessDialog() {
		processDialog = ProgressDialog.show(TicketList.this, null, TicketList.this.getString(R.string.progress_dialog_save_changes), true, true,
			new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					if (asyncSaveChangesThread.cancel(true)) {
						UiUtils.levantarDialog(TicketList.this, UiUtils.DIALOG_OK, getString(R.string.progress_dialog_save_changes_cancel));
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
	
	private void showAdminUserListProcessDialog() {
		processDialog = ProgressDialog.show(TicketList.this, null, TicketList.this.getString(R.string.progress_dialog_admin_user_list), true, true,
			new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					if (asyncAdminUserListThread.cancel(true)) {
						UiUtils.levantarDialog(TicketList.this, UiUtils.DIALOG_OK, getString(R.string.progress_dialog_admin_user_list_cancel));
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
							i = new Intent(TicketList.this, SmartphoneMap.class);
						}
						else {
							i = new Intent(TicketList.this, SmartphoneList.class);
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
						
						Intent i = new Intent(TicketList.this, AdminUserList.class);
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
	
	public class CloseTicketThread extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... args) {
			try {
				TicketModel ticket = openTickets.get(selectedTicket);
				
				InputStream inputStream = httpClientHandlerApp.closeTicket(ticket.getKey());
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
					System.out.println("------------------close-ticket response: " + sb.toString());
					jsonResponse = (JsonObject)jsonParser.parse(sb.toString());
					jsonResponseStatus = jsonResponse.getAsJsonObject("status");
					code = ((JsonPrimitive)jsonResponseStatus.get("code")).getAsString();
					
					if (code.equals("00")) {	
						closedTickets.add(ticket);
						openTickets.remove(selectedTicket);
												
						Intent i = new Intent(TicketList.this, TicketList.class);
						TicketList.this.finish();
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
