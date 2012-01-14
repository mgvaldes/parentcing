package com.ing3nia.parentalcontrol.ui;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.ing3nia.parentalcontrol.R;
import com.ing3nia.parentalcontrol.httpconnect.HttpClientHandler;
import com.ing3nia.parentalcontrol.ui.components.UiUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

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
	private BroadcastReceiver answerDialog;
	
	/**
	 * IntentFilter para manejar la respuesta del dialogo custom
	 */
	private IntentFilter filterAnswerDialog;
	
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.login);
		configurarHttpClientHandler();
		obtenerObjetosUI();
		restaurarConexionEnCurso();
		// TODO:Eliminar esto
		//textViewNumeroTarjeta.setText("6031220000001000101");
		//textViewNumeroTarjeta.setText("6031220050000704021");
		//textViewClave.setText("321ABC");
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
		textViewPass = (EditText)findViewById(R.id.editTextPass);
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
	private void restaurarConexionEnCurso() {
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
		
		showProcessDialog();
		asyncLoginThread = new LoginThread().execute(pcUsername, pcPassword);
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
				
				InputStream inputStream = httpClientHandlerApp.login(pcUsername, pcPassword);
				
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
						String cookie = ((JsonPrimitive)jsonResponse.get("cid")).getAsString();
						httpClientHandlerApp.setSessionCookie(cookie);
						System.out.println("Cookieeeeee: " + cookie);
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
			if (result) {
				processDialog.dismiss();
				
//				if(httpClientHandlerApp.conexionSinErrores()){
//					evaluarExistenciaActualizacion();
//				}
//				else{
//					UiUtils.levantarDialog(Login.this,UiUtils.DIALOG_OK,httpClientHandlerApp.mensajeParaError());
//				}
			}
		}
	}
}
