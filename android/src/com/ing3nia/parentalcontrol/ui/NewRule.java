package com.ing3nia.parentalcontrol.ui;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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
import com.ing3nia.parentalcontrol.models.RuleModel;
import com.ing3nia.parentalcontrol.models.SmartphoneModel;
import com.ing3nia.parentalcontrol.models.TicketModel;
import com.ing3nia.parentalcontrol.models.utils.FunctionalityTypeId;
import com.ing3nia.parentalcontrol.ui.components.UiUtils;
import com.ing3nia.parentalcontrol.ui.utils.PCViewsEnum;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
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
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

public class NewRule extends Activity {
	
	private EditText editTextRuleName;
	private EditText editTextStartDate;
	private EditText editTextEndDate;
	private EditText editTextStartTime;
	private EditText editTextEndTime;
	private Spinner ruleTypeSpinner;
	private boolean[] disabledFuncsStatus;
	private String[] normalDisabledFuncs;
	private int[] normalDisabledFuncIds;
	private String[] speedLimitDisabledFuncs;
	private int[] speedLimitDisabledFuncIds;	
	private String startDate;
	private String endDate;
	private String startTime;
	private String endTime;	
	private int day = -1;
	private int month = -1;
	private int year = -1;
	private int hour = -1;
	private int minute = -1;	
	static final int DATE_DIALOG_ID = 0;
	static final int TIME_DIALOG_ID = 1;
	static final int WARNING_DIALOG_ID = 2;
	static final int DISABLED_FUNC_DIALOG_ID = 3;
	private static final int ALERT_DIALOG = 4;	
	private int datePickerType;
	private int timePickerType;
	private int messageType;	
	private ArrayList<SmartphoneModel> smartphonesGeneral;
	private int selected;
	private SmartphoneModel smartphone;
	private ModificationModel modification;
	private ArrayList<RuleModel> rules;	
	private HttpClientHandler httpClientHandlerApp;
	private AsyncTask<Void, Void, Boolean> asyncSaveChangesThread;
	private AsyncTask<Void, Void, Boolean> asyncTicketsListThread;
	private AsyncTask<Void, Void, Boolean> asyncAdminUserListThread;
	private int nextScreen;	
	static ProgressDialog processDialog;
	private LinearLayout topBanner;
	private LinearLayout topBannerSaveChanges;	
	private String userKey;
	private String errorMessage = "";
	private static final int ERROR_ALERT_DIALOG = 5;
	
	// the callback received when the user "sets" the date in the dialog
    private DatePickerDialog.OnDateSetListener mDateSetListener =
        new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            	boolean consistent;
            	
            	if (datePickerType == 0) {
            		consistent = checkStartDateConsistency(editTextEndDate.getText().toString(), year, monthOfYear, dayOfMonth);
            		
            		if (!consistent) {
            			messageType = 0;            			
            			startDate = "";
            			showDialog(WARNING_DIALOG_ID);
            			
//            			UiUtils.levantarDialog(NewRule.this, UiUtils.DIALOG_OK, getString(R.string.start_date_warning_dialog_new_rule));
            		}
            		else {
            			startDate = addDigit(Integer.toString(dayOfMonth)) + "/" + addDigit(Integer.toString(monthOfYear + 1)) + "/" + year;
            		}
            		
            		updateStartDate();            		
            	}
            	else {
            		consistent = checkEndDateConsistency(editTextStartDate.getText().toString(), year, monthOfYear, dayOfMonth);
            		
            		if (!consistent) {
            			messageType = 1;
            			endDate = "";
            			showDialog(WARNING_DIALOG_ID);
            			
//            			UiUtils.levantarDialog(NewRule.this, UiUtils.DIALOG_OK, getString(R.string.end_date_warning_dialog_new_rule));
            		}
            		else {
            			endDate = addDigit(Integer.toString(dayOfMonth)) + "/" + addDigit(Integer.toString(monthOfYear + 1)) + "/" + year;
            		}
            		
            		updateEndDate();
            	}
            }
        };
        
    private TimePickerDialog.OnTimeSetListener mTimeSetListener =
        new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            	boolean consistent;
            	
            	if (timePickerType == 0) {
            		if (editTextStartDate.getText().toString().equals(editTextEndDate.getText().toString())) {
                		consistent = checkStartTimeConsistency(editTextEndTime.getText().toString(), minute, hourOfDay);
                		
                		if (!consistent) {
                			messageType = 2;
                			startTime = "";
                			showDialog(WARNING_DIALOG_ID);
                			
//                			UiUtils.levantarDialog(NewRule.this, UiUtils.DIALOG_OK, getString(R.string.start_time_warning_dialog_new_rule));
                		}
                		else {
                			startTime = addDigit(Integer.toString(hourOfDay)) + ":" + addDigit(Integer.toString(minute));
                		}
            		}
            		else {
            			startTime = addDigit(Integer.toString(hourOfDay)) + ":" + addDigit(Integer.toString(minute));
            		}
            		            		
            		updateStartTime();
            	}
            	else {
            		if (editTextStartDate.getText().toString().equals(editTextEndDate.getText().toString())) {
                		consistent = checkEndTimeConsistency(editTextStartTime.getText().toString(), minute, hourOfDay);
                		
                		if (!consistent) {
                			messageType = 3;            			
                			endTime = "";
                			showDialog(WARNING_DIALOG_ID);
                			
//                			UiUtils.levantarDialog(NewRule.this, UiUtils.DIALOG_OK, getString(R.string.end_time_warning_dialog_new_rule));
                		}
                		else {
                			endTime = addDigit(Integer.toString(hourOfDay)) + ":" + addDigit(Integer.toString(minute));
                		}
            		}
            		else {
            			endTime = addDigit(Integer.toString(hourOfDay)) + ":" + addDigit(Integer.toString(minute));
            		}
            		
            		updateEndTime();
            	}
            }
        };
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    if (httpClientHandlerApp == null) { 
			httpClientHandlerApp = (HttpClientHandler) NewRule.this.getApplication();
		}
	    
	    smartphone = httpClientHandlerApp.smartphone;
	    smartphonesGeneral = httpClientHandlerApp.smartphonesGeneral;
	    selected = httpClientHandlerApp.selected;
	    userKey = httpClientHandlerApp.userKey;
	    
	    setContentView(R.layout.new_rule);
	    
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
	    
	    initDisabledFuncs();
	    
	    editTextRuleName = (EditText) findViewById(R.id.editTextRuleName);
	    
	    editTextStartDate = (EditText) findViewById(R.id.editTextDateFrom);
	    editTextStartDate.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View arg0, boolean isFocus) {
				if (isFocus) {
					datePickerType = 0;
					parseDate(datePickerType);
					DatePickerDialog d = (DatePickerDialog)createDateDialog();
					d.show();
				}
			}
		});
	    startDate = "";
	    
	    editTextEndDate = (EditText) findViewById(R.id.editTextDateTo);
	    editTextEndDate.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View arg0, boolean isFocus) {
				if (isFocus) {
					datePickerType = 1;
					parseDate(datePickerType);
					DatePickerDialog d = (DatePickerDialog)createDateDialog();
					d.show();
				}
			}
		});
	    endDate = "";
	    
	    editTextStartTime = (EditText) findViewById(R.id.editTextStartTime);
	    editTextStartTime.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View arg0, boolean isFocus) {
				if (isFocus) {
					timePickerType = 0;
					parseTime(timePickerType);
					TimePickerDialog d = (TimePickerDialog)createTimeDialog();
					d.show();
				}
			}
		});
	    startTime = "";
	    
	    editTextEndTime = (EditText) findViewById(R.id.editTextEndTime);
	    editTextEndTime.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View arg0, boolean isFocus) {
				if (isFocus) {
					timePickerType = 1;
					parseTime(timePickerType);
					TimePickerDialog d = (TimePickerDialog)createTimeDialog();
					d.show();
				}
			}
		});
	    endTime = "";
	    
	    ruleTypeSpinner = (Spinner) findViewById(R.id.spinnerRuleType);
	    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.rule_type_options, android.R.layout.simple_spinner_item);
	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    ruleTypeSpinner.setAdapter(adapter);
	    ruleTypeSpinner.setOnItemSelectedListener(new NewRuleOnItemSelectedListener());
	    ruleTypeSpinner.requestFocus();
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
		
		if (httpClientHandlerApp == null) { 
			httpClientHandlerApp = (HttpClientHandler) NewRule.this.getApplication();
		}
	    
	    smartphone = httpClientHandlerApp.smartphone;
	    smartphonesGeneral = httpClientHandlerApp.smartphonesGeneral;
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
	
	public void parseDate(int type) {
		String[] date = new String[0];
		boolean empty = true;
    	
    	if (type == 0) { //start
    		if (!this.startDate.equals("")) {
    			date = this.startDate.split("/");
    			empty = false;
    		}    		
    	}
    	else { //end
    		if (!this.endDate.equals("")) {
    			date = this.endDate.split("/");
    			empty = false;
    		}    		
    	}
    	
    	if (!empty) {
    		this.day = Integer.parseInt(date[0]);
    		this.month = Integer.parseInt(date[1]) - 1;
    		this.year = Integer.parseInt(date[2]);
    	}
    }
    
    public void parseTime(int type) {
    	String[] time = new String[0];
    	boolean empty = true;
    	
    	if (type == 0) { //start
    		if (!this.startTime.equals("")) {
    			time = this.startTime.split(":");
    			empty = false;
    		}    		
    	}
    	else { //end
    		if (!this.endTime.equals("")) {
    			time = this.endTime.split(":");
    			empty = false;
    		}    		
    	}
    	
    	if (!empty) {
    		this.hour = Integer.parseInt(time[0]);
    		this.minute = Integer.parseInt(time[1]);
    	}    	
    }
	
	public String addDigit(String number) {
		if (number.length() == 1) {
			return ("0" + number);
		}
		else {
			return number;
		}
	}
	
	public void onSelectDisabledFuncsClick(View v) {
		showDialog(DISABLED_FUNC_DIALOG_ID);
	}
	
	// Checking if a start date is earlier an end date.
	private boolean checkStartDateConsistency(String dateAux, int year, int monthOfYear, int dayOfMonth) {
		boolean consistent = true;
		
		if (dateAux.length() != 0) {			
			String[] sepEndDate = dateAux.split("/");
			int endDayAux = new Integer(sepEndDate[0]);
			int endMonthAux = new Integer(sepEndDate[1]) - 1;
			int endYearAux = new Integer(sepEndDate[2]);
			
			if (endYearAux < year) {
				consistent = false;
			}
			else if (endYearAux == year) {
				if (endMonthAux < monthOfYear) {
					consistent = false;
				}
				else if (endMonthAux == monthOfYear) {
					if (endDayAux < dayOfMonth) {
						consistent = false;
					}
				}
			}
		}
		
		return consistent;
	}
	
	// Checking if an end date is earlier a start date.
	private boolean checkEndDateConsistency(String dateAux, int year, int monthOfYear, int dayOfMonth) {
		boolean consistent = true;
		
		if (dateAux.length() != 0) {			
			String[] sepStartDate = dateAux.split("/");
			int endDayAux = new Integer(sepStartDate[0]);
			int endMonthAux = new Integer(sepStartDate[1]) - 1;
			int endYearAux = new Integer(sepStartDate[2]);
			
			if (endYearAux > year) {
				consistent = false;
			}
			else if (endYearAux == year) {
				if (endMonthAux > monthOfYear) {
					consistent = false;
				}
				else if (endMonthAux == monthOfYear) {
					if (endDayAux > dayOfMonth) {
						consistent = false;
					}
				}
			}
		}
		
		return consistent;
	}
	
	// Checking if a start time is earlier an end time.
	private boolean checkStartTimeConsistency(String timeAux, int minute, int hour) {
		boolean consistent = true;
		
		if (timeAux.length() != 0) {			
			String[] sepEndTime = timeAux.split(":");
			int endHourAux = new Integer(sepEndTime[0]);
			int endMinuteAux = new Integer(sepEndTime[1]);
			
			if (endMinuteAux < minute) {
				consistent = false;
			}
			else if (endMinuteAux == minute) {
				if (endHourAux < hour) {
					consistent = false;
				}
			}
		}
		
		return consistent;
	}
	
	// Checking if a start time is earlier an end time.
	private boolean checkEndTimeConsistency(String timeAux, int minute, int hour) {
		boolean consistent = true;
		
		if (timeAux.length() != 0) {			
			String[] sepStartTime = timeAux.split(":");
			int endHourAux = new Integer(sepStartTime[0]);
			int endMinuteAux = new Integer(sepStartTime[1]);
			
			if (endMinuteAux > minute) {
				consistent = false;
			}
			else if (endMinuteAux == minute) {
				if (endHourAux > hour) {
					consistent = false;
				}
			}
		}
		
		return consistent;
	}
	
	// updates the start date in the EditTextStartDate
    private void updateStartDate() {    	
    	this.editTextStartDate.setText(this.startDate);
    }
    
    // updates the end date in the EditTextEndDate
    private void updateEndDate() {    	
    	this.editTextEndDate.setText(this.endDate);
    }

    // updates the start time in the EditTextStartTime
    private void updateStartTime() {    	
    	this.editTextStartTime.setText(this.startTime);
    }
    
    // updates the end time in the EditTextEndTime
    private void updateEndTime() {    	
    	this.editTextEndTime.setText(this.endTime);
    }
    
    public void initDisabledFuncsStatus(int size) {
    	disabledFuncsStatus = new boolean[size];
    	
    	for (int i = 0; i < size; i++) {
    		disabledFuncsStatus[i] = false;
    	}
    }
    
    public void initDisabledFuncs() {
    	FunctionalityTypeId[] funcs; 
    	int pos;
    	
		normalDisabledFuncs = new String[6];
		normalDisabledFuncIds = new int[6];
		funcs = FunctionalityTypeId.normalDisabledFuncs();
		pos = 0;
    	
    	for (FunctionalityTypeId f : funcs) {
    		normalDisabledFuncs[pos] = f.getDescription();
    		normalDisabledFuncIds[pos] = f.getId();
    		
    		pos += 1;
    	}
		
		speedLimitDisabledFuncs = new String[5];
		speedLimitDisabledFuncIds = new int[5];
		funcs = FunctionalityTypeId.speedLimitDisabledFuncs();
		pos = 0;
		
		for (FunctionalityTypeId f : funcs) {
			speedLimitDisabledFuncs[pos] = f.getDescription();
			speedLimitDisabledFuncIds[pos] = f.getId();
    		
    		pos += 1;
    	}
    }
	
	public class NewRuleOnItemSelectedListener implements OnItemSelectedListener {
		
		public NewRuleOnItemSelectedListener() {
		}

		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
    		if (pos == 0) {    			
    			initDisabledFuncsStatus(6);
    		}
    		else {
    			initDisabledFuncsStatus(5);
    		}
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
		}
	}
	
	public void onLimpiarClick(View v) {
		editTextRuleName.setText("");
		editTextStartDate.setText("");
		editTextEndDate.setText("");
		editTextStartTime.setText("");
		editTextEndTime.setText("");
		
		editTextRuleName.requestFocus();
	}
	
	public boolean checkEditTextFieldsFilled() {
		boolean filled = true;
		
		if (editTextRuleName.getText().length() == 0 ||
			editTextStartDate.getText().length() == 0 ||
			editTextEndDate.getText().length() == 0 ||
			editTextStartTime.getText().length() == 0 ||
			editTextEndTime.getText().length() == 0) {
			filled = false;
		}
		
		return filled;
	}
	
	public ArrayList<Integer> parseDisabledFunctionalities() {
		ArrayList<Integer> disabledFuncs = new ArrayList<Integer>();	
		int pos = 0;
		
		for (boolean status : disabledFuncsStatus) {
			if (status) {
				if (ruleTypeSpinner.getSelectedItemPosition() == 0) { //Normal
					disabledFuncs.add(normalDisabledFuncIds[pos]);
				}
				else { //Speed Limit
					disabledFuncs.add(speedLimitDisabledFuncIds[pos]);
				}				
			}
			
			pos += 1;
		}
		
		return disabledFuncs;
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
	        	loadDailyRoute();
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
	
	public void loadDailyRoute() {
		Intent i = new Intent(NewRule.this, DailyRoute.class);
		httpClientHandlerApp.lastView = PCViewsEnum.NEW_RULE_VIEW.getId();
		startActivity(i);
	}

	public void loadAlertList() {
		Intent i = new Intent(NewRule.this, AlertList.class);
		httpClientHandlerApp.lastView = PCViewsEnum.NEW_RULE_VIEW.getId();
		startActivity(i);
	}
	
	public void loadRuleList() {
		Intent i = new Intent(NewRule.this, RuleList.class);
		httpClientHandlerApp.lastView = PCViewsEnum.NEW_RULE_VIEW.getId();
		startActivity(i);
	}
	
	public void loadContactList() {
		Intent i = new Intent(NewRule.this, ContactList.class);
		httpClientHandlerApp.lastView = PCViewsEnum.NEW_RULE_VIEW.getId();
		startActivity(i);
	}
	
	public void loadSmartphoneSettings() {
		Intent i = new Intent(NewRule.this, SmartphoneSettings.class);
		httpClientHandlerApp.lastView = PCViewsEnum.NEW_RULE_VIEW.getId();
		startActivity(i);
	}
	
	public void loadDeviceMap() {
		if (httpClientHandlerApp.pendingChanges) {
			nextScreen = 0;
			showDialog(ALERT_DIALOG);
		}
		else {
			Intent i = new Intent(NewRule.this, SmartphoneMap.class);			
			startActivity(i);
		}
	}
	
	public void loadDeviceList() {
		if (httpClientHandlerApp.pendingChanges) {
			nextScreen = 1;
			showDialog(ALERT_DIALOG);
		}
		else {
			Intent i = new Intent(NewRule.this, SmartphoneList.class);
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
	
	public void onSaveRuleClick(View v) {
		if (checkEditTextFieldsFilled()) {
			modification = this.smartphone.getModification();
			
			if (modification == null) {
				this.smartphone.setModification(new ModificationModel());
				modification = this.smartphone.getModification();
			}
			
			rules = modification.getRules();
			
			if (rules == null) {
				modification.setRules(new ArrayList<RuleModel>());
				rules = modification.getRules();
			}
			
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm");
			SimpleDateFormat formatter3 = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
			
			Date auxDate = null;
			
			String startDate = this.editTextStartDate.getText().toString() + " " + this.editTextStartTime.getText().toString(); //formatter2.format(auxDate);
			
			try {
				auxDate = formatter.parse(startDate);
			} 
			catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			startDate = formatter3.format(auxDate);
			
			String endDate = this.editTextEndDate.getText().toString() + " " + this.editTextEndTime.getText().toString(); //formatter2.format(auxDate);
			
			try {
				auxDate = formatter.parse(endDate);
			} 
			catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			endDate = formatter3.format(auxDate);			
			
			ArrayList<Integer> auxDisabledFuncs = parseDisabledFunctionalities();
			RuleModel newRule = new RuleModel(startDate, endDate, auxDisabledFuncs, this.editTextRuleName.getText().toString());
			newRule.setType(ruleTypeSpinner.getSelectedItemPosition());
			newRule.setCreationDate(formatter3.format(Calendar.getInstance().getTime()));
			rules.add(newRule);
			
			/////////////////////////////////
			// Agregar regla en smartphone
			/////////////////////////////////
			this.smartphone.getRules().add(newRule);
			
			Intent i = new Intent(NewRule.this, RuleList.class);
			
			httpClientHandlerApp.pendingChanges = true;
			httpClientHandlerApp.lastView = PCViewsEnum.NEW_RULE_VIEW.getId();
			
			startActivity(i);
		}
		else {
			Toast.makeText(this, "All fields must be filled out!", Toast.LENGTH_SHORT).show();
			//UiUtils.levantarDialog(NewRule.this, UiUtils.DIALOG_OK, getString(R.string.fill_edit_texts_warning_dialog_new_rule));
		}
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
	
	public Dialog createDateDialog() {
		if ((this.day == -1) && (this.month == -1) && (this.year == -1)) {
			final Calendar c = Calendar.getInstance();
			return new DatePickerDialog(this, mDateSetListener, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
		}		
		else {
			return new DatePickerDialog(this, mDateSetListener, this.year, this.month, this.day);
		}		
	}
	
	public Dialog createTimeDialog() {
		if ((this.hour == -1) && (this.minute == -1)) {
			final Calendar c = Calendar.getInstance();
			return new TimePickerDialog(this, mTimeSetListener, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), false);
		}
		else {
			return new TimePickerDialog(this, mTimeSetListener, this.hour, this.minute, false);
		}
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		final Calendar c = Calendar.getInstance();
		
		if (id == DATE_DIALOG_ID) {
			return new DatePickerDialog(this, mDateSetListener, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
		}
		else if (id == TIME_DIALOG_ID) {
			return new TimePickerDialog(this, mTimeSetListener, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), false);
		}
		else if (id == WARNING_DIALOG_ID) {
			AlertDialog.Builder dialog = new AlertDialog.Builder(NewRule.this);
			dialog.setTitle(R.string.warning_label);
			dialog.setIcon(R.drawable.alert_icon);
			
			if (messageType == 0) {
				dialog.setMessage(R.string.start_date_warning_dialog_new_rule);
			}
			else if (messageType == 1) {
				dialog.setMessage(R.string.end_date_warning_dialog_new_rule);
			}
			else if (messageType == 2) {
				dialog.setMessage(R.string.start_time_warning_dialog_new_rule);
			}
			else {
				dialog.setMessage(R.string.end_time_warning_dialog_new_rule);
			}
						
			dialog.setPositiveButton(R.string.ok_button_label, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                	dialog.dismiss();
                }
            });
			
			return dialog.create();
		}
		else if (id == DISABLED_FUNC_DIALOG_ID) {
			AlertDialog.Builder dialog = new AlertDialog.Builder(NewRule.this);
			dialog.setTitle(R.string.disabled_func_prompt);
			
			if (ruleTypeSpinner.getSelectedItemPosition() == 0) { //Normal
				dialog.setMultiChoiceItems(normalDisabledFuncs, disabledFuncsStatus, new DialogInterface.OnMultiChoiceClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton, boolean isChecked) {
                        /* User clicked on a check box do some stuff */
                    	disabledFuncsStatus[whichButton] = isChecked;
                    }
				});
			}
			else { //SpeedLimit
				dialog.setMultiChoiceItems(speedLimitDisabledFuncs, disabledFuncsStatus, new DialogInterface.OnMultiChoiceClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton, boolean isChecked) {
                        /* User clicked on a check box do some stuff */
                    	disabledFuncsStatus[whichButton] = isChecked;
                    }
				});
			}
						
			dialog.setPositiveButton(R.string.ok_button_label, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                	dialog.dismiss();
                }
            });
			
			return dialog.create();
		}
		else if (id == ALERT_DIALOG) {
			AlertDialog.Builder alert = new AlertDialog.Builder(NewRule.this);
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
			AlertDialog.Builder alert = new AlertDialog.Builder(NewRule.this);
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
		processDialog = ProgressDialog.show(NewRule.this, null, NewRule.this.getString(R.string.progress_dialog_save_changes), true, true,
			new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					if (asyncSaveChangesThread.cancel(true)) {
						UiUtils.levantarDialog(NewRule.this, UiUtils.DIALOG_OK, getString(R.string.progress_dialog_save_changes_cancel));
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
		processDialog = ProgressDialog.show(NewRule.this, null, NewRule.this.getString(R.string.progress_dialog_tickets_list), true, true,
			new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					if (asyncTicketsListThread.cancel(true)) {
						UiUtils.levantarDialog(NewRule.this, UiUtils.DIALOG_OK, getString(R.string.progress_dialog_tickets_list_cancel));
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
		processDialog = ProgressDialog.show(NewRule.this, null, NewRule.this.getString(R.string.progress_dialog_admin_user_list), true, true,
			new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					if (asyncAdminUserListThread.cancel(true)) {
						UiUtils.levantarDialog(NewRule.this, UiUtils.DIALOG_OK, getString(R.string.progress_dialog_admin_user_list_cancel));
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
							i = new Intent(NewRule.this, SmartphoneMap.class);
						}
						else {
							i = new Intent(NewRule.this, SmartphoneList.class);
						}
						
						httpClientHandlerApp.pendingChanges = false;
						httpClientHandlerApp.lastView = PCViewsEnum.NEW_RULE_VIEW.getId();
						
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
						
						Intent i = new Intent(NewRule.this, TicketList.class);
						
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
						
						Intent i = new Intent(NewRule.this, AdminUserList.class);
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
}
