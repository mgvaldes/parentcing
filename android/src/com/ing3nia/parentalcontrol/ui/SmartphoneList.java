package com.ing3nia.parentalcontrol.ui;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.reflect.TypeToken;
import com.ing3nia.parentalcontrol.R;
import com.ing3nia.parentalcontrol.httpconnect.HttpClientHandler;
import com.ing3nia.parentalcontrol.models.SmartphoneModel;
import com.ing3nia.parentalcontrol.ui.components.SmartphoneListAapter;
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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class SmartphoneList extends Activity {
	private ArrayList<SmartphoneModel> smartphonesGeneral;
	static ProgressDialog processDialog;
	private AsyncTask<Integer, Void, Boolean> asyncSmartphoneDetailsThread;
	private HttpClientHandler httpClientHandlerApp;
	private static final int ALERT_DIALOG_EXIT = 1;
	private String errorMessage = "";
	private static final int ERROR_ALERT_DIALOG = 2;
	
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		
		setContentView(R.layout.smartphone_list);
		
		if (httpClientHandlerApp == null) { 
			httpClientHandlerApp = (HttpClientHandler) SmartphoneList.this.getApplication();
		}
		
		smartphonesGeneral = httpClientHandlerApp.smartphonesGeneral;
		
	    ListView smartphoneList = (ListView)findViewById(R.id.smartphoneList);
	    smartphoneList.setAdapter(new SmartphoneListAapter(this, smartphonesGeneral));
	    smartphoneList.setOnItemClickListener(
	    		new OnItemClickListener() {
	                @Override
	                public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
	                	showProcessDialog();
	                	asyncSmartphoneDetailsThread = new SmartphoneDetailsThread().execute(position);
	                }
	           }
	    );
	}
	
	private void showProcessDialog() {
		processDialog = ProgressDialog.show(SmartphoneList.this, null, SmartphoneList.this.getString(R.string.progress_dialog_smartphone_details), true, true,
			new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					if (asyncSmartphoneDetailsThread.cancel(true)) {
						UiUtils.levantarDialog(SmartphoneList.this, UiUtils.DIALOG_OK, getString(R.string.progress_dialog_smartphone_details_cancel));
					} 
					else {
						if (asyncSmartphoneDetailsThread != null) {
							if (!(asyncSmartphoneDetailsThread.getStatus().equals(AsyncTask.Status.FINISHED))) {
								showProcessDialog();
							}
						}
					}
				}
			}
		);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.layout.smartphone_submenu, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
		switch (item.getItemId()) {
        case R.id.smartphoneMap:
        	loadSmartphoneMap();
            return true;
        case R.id.smartphoneList:        	
            return true;
        default:
            return super.onOptionsItemSelected(item);         
	    }
	}
	
	public void loadSmartphoneMap() {
		Intent i = new Intent(SmartphoneList.this, SmartphoneMap.class);
		
		httpClientHandlerApp.lastView = PCViewsEnum.SMARTPHONE_LIST_VIEW.getId();
		
		startActivity(i);
	}
	
	public class SmartphoneDetailsThread extends AsyncTask<Integer, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Integer... args) {
			try {
				int position = args[0];
				System.out.println("--------------------smartphone key: " + smartphonesGeneral.get(position).getKeyId());
				InputStream inputStream = httpClientHandlerApp.smartphonesDetailsInfo(smartphonesGeneral.get(position).getKeyId());
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
					System.out.println("------------------smartphone-details response: " + sb.toString());
					jsonResponse = (JsonObject)jsonParser.parse(sb.toString());
					jsonResponseStatus = jsonResponse.getAsJsonObject("status");
					code = ((JsonPrimitive)jsonResponseStatus.get("code")).getAsString();
					
					if (code.equals("00")) {
						SmartphoneModel smartphone;
						
						Type sphType = new TypeToken<SmartphoneModel>(){}.getType();
						Gson gson = new Gson();
						smartphone = gson.fromJson(jsonResponse.getAsJsonObject("smartphone"), sphType);
						
						Intent i = new Intent(SmartphoneList.this, DailyRoute.class);
						
						httpClientHandlerApp.selected = position;
						httpClientHandlerApp.smartphone = smartphone;
						httpClientHandlerApp.lastView = PCViewsEnum.SMARTPHONE_LIST_VIEW.getId();
						
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
	
//	@Override
//	public void onPause(){
//		httpClientHandlerApp.visible = false;
//		//Pause as last action
//		super.onPause();
//	}
//	
//	@Override
//	public void onResume(){
//		//Resume as first action
//		super.onResume();
////		httpClientHandlerApp.evaluarTimestamp();
//		
//		if(httpClientHandlerApp.reset){
//			httpClientHandlerApp.returnToLogin();
//		}
//		
//		httpClientHandlerApp.visible = true;
//		httpClientHandlerApp.reset = false;
//	}
	
	/**
	 * Sobrecarga el comportamiento cuando se presiona la tecla fisica de "back".
	 * Interceptado para colocar el logout de esta manera tambien.
	 */
	@Override
	public void onBackPressed() {
		onBackClick(null);
	}
	
	/**
	 * Maneja el click del boton de Salir
	 * @param v La vista que llama a la funcion, en este caso es pasada automatica por el uso de "onClick" en el layout.
	 */
	public void onBackClick(View v) {
		showDialog(ALERT_DIALOG_EXIT);
		
//		filterAnswerDialog = new IntentFilter();
//		filterAnswerDialog.addAction(SmartphoneMap.this.getString(R.string.dialog_intent_si));
//		filterAnswerDialog.addAction(SmartphoneMap.this.getString(R.string.dialog_intent_no));
//		
//		answerDialog = new BroadcastReceiver() {
//			@Override
//			public void onReceive(Context context, Intent intent) {
//				if (context == SmartphoneMap.this) {
//					if (intent.getAction().equalsIgnoreCase(SmartphoneMap.this.getString(R.string.dialog_intent_si))) {
//						unregisterReceiver(answerDialog);
//						logout();
//					} 
//					else if (intent.getAction().equalsIgnoreCase(SmartphoneMap.this.getString(R.string.dialog_intent_no))) {
//						unregisterReceiver(answerDialog);
//					} 
//				}
//			}
//		};
//		
//		registerReceiver(answerDialog, filterAnswerDialog);
//		UiUtils.levantarDialog(SmartphoneMap.this, UiUtils.DIALOG_SI_NO, getString(R.string.quit_app));
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		if (id == ALERT_DIALOG_EXIT) {
			AlertDialog.Builder alert = new AlertDialog.Builder(SmartphoneList.this);
	    	alert.setIcon(R.drawable.alert_icon);
            alert.setTitle("Alert");
            alert.setMessage(R.string.quit_app);
            alert.setPositiveButton(R.string.alert_dialog_yes_button_label, new DialogInterface.OnClickListener() {
            	public void onClick(DialogInterface dialog, int whichButton) {
            		dialog.dismiss();
            		
                 	Intent i = new Intent(SmartphoneList.this, Login.class);
                 	httpClientHandlerApp.setSessionCookie("");
            		startActivity(i);
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
			AlertDialog.Builder alert = new AlertDialog.Builder(SmartphoneList.this);
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
}
