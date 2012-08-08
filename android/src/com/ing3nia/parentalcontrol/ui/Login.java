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
import com.ing3nia.parentalcontrol.ui.components.UiUtils;
import com.ing3nia.parentalcontrol.ui.utils.PCViewsEnum;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Login extends Activity {
	/**
	 * Progress dialog para indicar la actividad durante la conexion
	 */
	static ProgressDialog processDialog;
	
	/**
	 * AsyncTask para manejo asincrono del login
	 */
	AsyncTask<String, Void, Boolean> asyncLoginThread;
	
	/**
	 * Handler de todas las conexiones de la aplicacion
	 */
	HttpClientHandler httpClientHandlerApp;
	
	/**
	 * El campo texto donde se introduce el nombre de usuario
	 */
	private EditText textViewUsername;
	
	/**
	 * El campo de texto donde se introduce la clave
	 */
	private EditText textViewPass;
	
	/**
	 * BroadcastReceiver para manejar la respuesta del dialogo custom
	 */
//	private BroadcastReceiver answerDialog;
	
	/**
	 * IntentFilter para manejar la respuesta del dialogo custom
	 */
//	private IntentFilter filterAnswerDialog;
	
//	private UserModel userModel;
	
	private String errorMessage = "";
	private static final int ERROR_ALERT_DIALOG = 2;
	private static final int REGISTER_REMINDER_ALERT_DIALOG = 3;
	
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.login);
		configurarHttpClientHandler();
		obtenerObjetosUI();
		restoreOngoingConnections();
		checkRegisterReminder();
	}
	
	public void checkRegisterReminder() {
		SharedPreferences sharedPreferences = getSharedPreferences("PRC_PREF", MODE_PRIVATE);
	    
	    if (!sharedPreferences.contains("reminder")) {
	    	showDialog(REGISTER_REMINDER_ALERT_DIALOG);
	    }
	}
	
	private void configurarHttpClientHandler() {
		httpClientHandlerApp = (HttpClientHandler) Login.this.getApplication();
		httpClientHandlerApp.loginContext = Login.this;
	}
	
	/**
	 * Obtiene todas las referencias a los objetos de interface definidos en el
	 * layout.
	 */
	private void obtenerObjetosUI() {
		textViewUsername = (EditText)findViewById(R.id.editTextUsername);
		textViewPass = (EditText)findViewById(R.id.editTextPassword);
		
//		textViewUsername.setText("gaby@prc.com");
//		textViewPass.setText("gabypass");
	}
	
	@Override
	/**
	 * Retiene el hilo de conexion activo, esto es para que cuando se gire el dispositivo, o se saque el teclado,
	 * o ocurra cualquier otro cambio de configuración, no se pierda la conexion.
	 */
	public Object onRetainNonConfigurationInstance() {
		if (processDialog != null) {
			processDialog.dismiss();
		}
		
		if (asyncLoginThread != null) {
			return (asyncLoginThread);
		}
		
		return super.onRetainNonConfigurationInstance();
	}
	
	/**
	 * Recupera el hilo usado para conectar y tambien vuelve a aparecer el pd.
	 */
	private void restoreOngoingConnections() {
		processDialog = new ProgressDialog(Login.this);
		
		if (getLastNonConfigurationInstance() != null) {
			asyncLoginThread = (AsyncTask<String, Void, Boolean>) getLastNonConfigurationInstance();
			
			if (asyncLoginThread != null) {
				if (!(asyncLoginThread.getStatus().equals(AsyncTask.Status.FINISHED))) {
					showProcessDialog();
				}
			}
		}
	}
	
	/**
	 * Mostrar un progress dialog para indicar actividad
	 */
	private void showProcessDialog() {
		processDialog = ProgressDialog.show(Login.this, null, Login.this.getString(R.string.progress_dialog_login), true, true,
			new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					if (asyncLoginThread.cancel(true)) {
						UiUtils.levantarDialog(Login.this, UiUtils.DIALOG_OK, getString(R.string.progress_dialog_login_cancel));
						onLimpiarClick(null);
					} 
					else {
						if (asyncLoginThread != null) {
							if (!(asyncLoginThread.getStatus().equals(AsyncTask.Status.FINISHED))) {
								showProcessDialog();
							}
						}
					}
				}
			}
		);
	}
	
	public void onLimpiarClick(View v) {
		textViewUsername.setText("");
		textViewPass.setText("");
		textViewUsername.requestFocus();
	}
	
	/**
	 * Maneja el click de boton de aceptar(login)
	 * 
	 * @param v
	 *            La vista que llama a la funcion, en este caso es pasada
	 *            automatica por el uso de "onClick" en el layout.
	 */
	public void onAceptarClick(View v) {
		String pcUsername = textViewUsername.getText().toString();
		String pcPassword = textViewPass.getText().toString();
		
		httpClientHandlerApp.username = pcUsername;
		
		showProcessDialog();
		asyncLoginThread = new LoginThread().execute(pcUsername, pcPassword);
		
//		Intent i = new Intent(Login.this, SmartphoneList.class);
//		startActivity(i);
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		if (id == ERROR_ALERT_DIALOG) {
			AlertDialog.Builder alert = new AlertDialog.Builder(Login.this);
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
		else if (id == REGISTER_REMINDER_ALERT_DIALOG) {
			AlertDialog.Builder alert = new AlertDialog.Builder(Login.this);
	    	alert.setIcon(R.drawable.alert_icon);
            alert.setTitle("Alert");
            alert.setMessage("If you are no yet registered go to www.parental-control.appspot.com and register as a new user!");
            
            final CheckBox checkBox = new CheckBox(this);
            checkBox.setText("Don't show this alert again");
            
            LinearLayout linearLayout = new LinearLayout(this);
            linearLayout.setLayoutParams(new  LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
            linearLayout.setOrientation(1);     
            linearLayout.addView(checkBox);
            linearLayout.setPadding(20, 0, 0, 0);
            
            alert.setView(linearLayout);
            
            alert.setPositiveButton(R.string.ok_button_label, new DialogInterface.OnClickListener() {
            	public void onClick(DialogInterface dialog, int whichButton) {
            		if (checkBox.isChecked()) {
            			SharedPreferences sharedPreferences = getSharedPreferences("PRC_PREF", MODE_PRIVATE);
            		    SharedPreferences.Editor editor = sharedPreferences.edit();
            		    editor.putBoolean("reminder", true);
            		    editor.commit();
            		}
            		
            		dialog.dismiss();
                 }
            });
            alert.setNegativeButton("Got to link", new DialogInterface.OnClickListener() {
            	public void onClick(DialogInterface dialog, int whichButton) {
            		String url = "http://www.parental-control.appspot.com";
            		Intent i = new Intent(Intent.ACTION_VIEW);
            		i.setData(Uri.parse(url));
            		startActivity(i);
                 }
             });
             
             return alert.create();
		}
		
		return null;
	}
	
	/**
	 * Clase que maneja el Login, es un asyncTask para operar de fondo
	 * 
	 * @author Stefano
	 * 
	 */
	public class LoginThread extends AsyncTask<String, Void, Boolean> {
		@Override
		protected Boolean doInBackground(String... args) {
			try {
				String pcUsername = args[0];
				String pcPassword = args[1];
				
				httpClientHandlerApp = (HttpClientHandler) Login.this.getApplication();
				BufferedReader rd;
				StringBuffer sb;
				String line;
				JsonParser jsonParser;
				JsonObject jsonResponse;
				JsonObject jsonResponseStatus;
				String code;
				
				InputStream inputStream = httpClientHandlerApp.userKey(pcUsername, pcPassword);
				
				if (inputStream != null) {
					rd = new BufferedReader(new InputStreamReader(inputStream));
					sb = new StringBuffer();
					
					while ((line = rd.readLine()) != null) {
						sb.append(line);
					}
						
					rd.close();
					
					jsonParser = new JsonParser();		
					System.out.println("------------------user-key response: " + sb.toString());
					jsonResponse = (JsonObject)jsonParser.parse(sb.toString());
					jsonResponseStatus = jsonResponse.getAsJsonObject("status");
					code = ((JsonPrimitive)jsonResponseStatus.get("code")).getAsString(); 
					
					if (code.equals("00")) {
						String userKey = ((JsonPrimitive)jsonResponse.get("key")).getAsString();
						httpClientHandlerApp.userKey = userKey;
						System.out.println("Userkeeeeeeey: " + httpClientHandlerApp.userKey);								
						
						inputStream = httpClientHandlerApp.login(pcUsername, pcPassword);				
						
						if (inputStream != null) {
							rd = new BufferedReader(new InputStreamReader(inputStream));
							sb = new StringBuffer();
							
							while ((line = rd.readLine()) != null) {
								sb.append(line);
							}
								
							rd.close();
							
							jsonParser = new JsonParser();		
							System.out.println("------------------login response: " + sb.toString());
							jsonResponse = (JsonObject)jsonParser.parse(sb.toString());
							jsonResponseStatus = jsonResponse.getAsJsonObject("status");
							code = ((JsonPrimitive)jsonResponseStatus.get("code")).getAsString(); 
							
							if (code.equals("00")) {
								String cookie = ((JsonPrimitive)jsonResponse.get("cid")).getAsString();
								httpClientHandlerApp.sessionCookie = cookie;
								System.out.println("Cookieeeeee: " + httpClientHandlerApp.sessionCookie);
								
								inputStream = httpClientHandlerApp.smartphonesGeneralInfo();
								
								if (inputStream != null) {
									rd = new BufferedReader(new InputStreamReader(inputStream));
									sb = new StringBuffer();
									
									while ((line = rd.readLine()) != null) {
										sb.append(line);
									}
										
									rd.close();
									
									jsonParser = new JsonParser();		
									System.out.println("------------------smartphone-grl response: " + sb.toString());
									jsonResponse = (JsonObject)jsonParser.parse(sb.toString());
									jsonResponseStatus = jsonResponse.getAsJsonObject("status");
									code = ((JsonPrimitive)jsonResponseStatus.get("code")).getAsString();
									
									if (code.equals("00")) {
										ArrayList<SmartphoneModel> smartphones;
										
										Type sphArrayType = new TypeToken<ArrayList<SmartphoneModel>>(){}.getType();
										Gson gson = new Gson();
										smartphones = gson.fromJson(jsonResponse.getAsJsonArray("smartphones"), sphArrayType);
										
										Intent i = new Intent(Login.this, SmartphoneMap.class);
										
										httpClientHandlerApp.smartphonesGeneral = smartphones;
										httpClientHandlerApp.pendingChanges = false;
										httpClientHandlerApp.lastView = PCViewsEnum.LOGIN_VIEW.getId();
										
										startActivity(i);
									}
									else {
										errorMessage = ((JsonPrimitive)jsonResponseStatus.get("msg")).getAsString();
									}
								}			
							}
							else {
								errorMessage = ((JsonPrimitive)jsonResponseStatus.get("msg")).getAsString();
							}
						}
					}
					else {
						errorMessage = ((JsonPrimitive)jsonResponseStatus.get("msg")).getAsString();
					}
				}
			} 
			catch (Exception e) {
				e.printStackTrace();
				return true;
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
	
	/**
	 * Sobrecarga el comportamiento cuando se presiona la tecla fisica de "back".
	 * Interceptado para colocar el logout de esta manera tambien.
	 */
	@Override
	public void onBackPressed() {
		
	}
}
