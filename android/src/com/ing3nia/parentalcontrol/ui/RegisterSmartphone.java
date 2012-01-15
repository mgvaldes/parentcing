package com.ing3nia.parentalcontrol.ui;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.ing3nia.parentalcontrol.R;
import com.ing3nia.parentalcontrol.httpconnect.HttpClientHandler;
import com.ing3nia.parentalcontrol.ui.Login.LoginThread;
import com.ing3nia.parentalcontrol.ui.components.UiUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterSmartphone extends Activity {
	/**
	 * Progress dialog para indicar la actividad durante la conexion
	 */
	static ProgressDialog processDialog;
	
	/**
	 * AsyncTask para manejo asincrono del register smartphone.
	 */
	AsyncTask<String, Void, Boolean> asyncRegisterSmartphoneThread;
	
	/**
	 * Handler de todas las conexiones de la aplicacion
	 */
	HttpClientHandler httpClientHandlerApp;
	
	/**
	 * El campo texto donde se introduce el nombre del smartphone
	 */
	private EditText textViewSmartphoneName;
	
	/**
	 * BroadcastReceiver para manejar la respuesta del dialogo custom
	 */
	private BroadcastReceiver answerDialog;
	
	/**
	 * IntentFilter para manejar la respuesta del dialogo custom
	 */
	private IntentFilter filterAnswerDialog;
	
	/**
	 * Name of file that stores application settings.
	 */
	private String settingsFile;
	
	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.register_smartphone);
		obtenerObjetosUI();
	}
	
	/**
	 * Obtiene todas las referencias a los objetos de interface definidos en el
	 * layout.
	 */
	private void obtenerObjetosUI() {
		textViewSmartphoneName = (EditText)findViewById(R.id.editTextSmartphoneName);
	}
	
	/**
	 * TODO Hay que hacer el request para registrar
	 * el smartphone con el nombre ingresado. 
	 */
	public void onRegisterClick(View view) {
		String smartphoneName = textViewSmartphoneName.getText().toString();
		Toast.makeText(this, "Saving smartphoneName. Now the app icon must dissapear", Toast.LENGTH_SHORT).show();
		
//		/**
//		 * Saving application settings. Every setting is separated
//		 * with '|'.
//		 * 1) smartphoneName 
//		 */
//		try {
//			Context ctx = getApplicationContext();
//			FileOutputStream fos = ctx.openFileOutput(this.settingsFile, Context.MODE_PRIVATE);
//			
//			String settings = "smartphoneName=" + smartphoneName + "|";
//			
//			fos.write(settings.getBytes());
//			fos.close();
//			
//			Toast.makeText(this, "Now the app icon must dissapear", Toast.LENGTH_SHORT).show();
//			finish();
//		}
//		catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
//		showProcessDialog();
//		asyncRegisterSmartphoneThread = new RegisterSmartphoneThread().execute(smartphoneName);
		
//		Intent i = new Intent(this, Login.class);
//		
//		String smartphoneName = textViewSmartphoneName.getText().toString();
//		// Call service
//		
//		startActivity(i);
	}
	
	/**
	 * Clase que maneja el RegisterSmartphone, es un asyncTask para operar de fondo
	 * 
	 * @author Stefano
	 * 
	 */
	public class RegisterSmartphoneThread extends AsyncTask<String, Void, Boolean> {
		@Override
		protected Boolean doInBackground(String... args) {
			try {
				String pcSmartphoneName = args[0];
				
				InputStream inputStream = httpClientHandlerApp.registerSmartphone(pcSmartphoneName);
				
				if(inputStream != null) {
					BufferedReader rd = new BufferedReader(new InputStreamReader(inputStream));
					StringBuffer sb = new StringBuffer();
					String line;
					
					while ((line = rd.readLine()) != null) {
						sb.append(line);
					}
						
					rd.close();
					
					JsonParser jsonParser = new JsonParser();			
					JsonObject jsonResponse = (JsonObject)jsonParser.parse(sb.toString());
					JsonObject jsonResponseStatus = jsonResponse.getAsJsonObject("status");
					String code = ((JsonPrimitive)jsonResponseStatus.get("code")).getAsString(); 
					
					if (code.equals("00")) {
						
					}
					else {
						String verbose = ((JsonPrimitive)jsonResponseStatus.get("verbose")).getAsString();
						String msg = ((JsonPrimitive)jsonResponseStatus.get("msg")).getAsString();
						
						//TODO Manejar error!
					}
					
//					Gson gson = new Gson();
//					Reader reader = new InputStreamReader(inputStream);
//					Type type = new TypeToken<GlobalContainer>(){}.getType();
//					// Colocar a true para que escriba todo lo que recibe en el
//					// DDMS. Se guinda la app.
//					if (false) {
//						BufferedInputStream bis = new BufferedInputStream(is);
//						ByteArrayBuffer baf = new ByteArrayBuffer(50);
//						int current = 0;
//						while ((current = bis.read()) != -1) {
//							baf.append((byte) current);
//						}
//						System.out.println(new String(baf.toByteArray()));
//					}
//					globalContainer = gson.fromJson(reader, type);
				}
			} 
			catch (Exception e) {
				return true;
			}
			
			return true;
		}
		
		protected void onPostExecute(Boolean result) {
			processDialog.dismiss();
			
			if (result) {
				loadLogin();
			}
			else {
				UiUtils.levantarDialog(RegisterSmartphone.this, UiUtils.DIALOG_OK, httpClientHandlerApp.mensajeParaError());
			}
		}
	}
	
	private void loadLogin() {
		Intent i = new Intent(RegisterSmartphone.this, Login.class);
		startActivity(i);
	}
}
