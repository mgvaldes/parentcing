package com.ing3nia.parentalcontrol.httpconnect;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import com.ing3nia.parentalcontrol.R;
import com.ing3nia.parentalcontrol.ui.Login;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.app.Application;
import android.content.Context;
import android.content.Intent;

public class HttpClientHandler extends Application {
	/**
	 * El cliente http que manejara todo
	 */
	public DefaultHttpClient httpClient;

	/**
	 * Contiene el cookie de sesion
	 */
	String sessionCookie = "";

	/**
	 * URL Base para el servidor
	 */
	String baseURL;

	/**
	 * Codigo de respuesta del request
	 */
	public int responseCode = 0;

	/**
	 * Timer para manejar la expiracion de la sesión
	 */
	Timer sessionTimer;

	/**
	 * Guarda el timestamp de la ultima interaccion con el servidor Esto es para
	 * que aunque el hilo del thread muera ocurra el autologout
	 */
	long timestamp;

	/**
	 * Guarda cuanto tiempo debe ser el timeout, guardar en ms. Este numero
	 * indica cuanto tiempo de inactividad habra antes del timeout en
	 * milisegundos 5 minutos = 300 segundos = 300000 ms
	 */
	long timeout = 300000;

	/**
	 * Contexto del Login para asegurar ejecutabilidad del timer desde el
	 * contexto de una actividad
	 */
	public Context loginContext;

	/**
	 * Indica si la aplicación esta en el foreground o no.
	 */
	public boolean visible;

	/**
	 * Indica si la aplicacion se debe regresar al login al ser despertada
	 */
	public boolean reset;
	
	public void setSessionCookie(String cookie) {
		sessionCookie = cookie;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		httpClient = createHttpClient();
		baseURL = this.getString(R.string.base_url);
	}
	
	@Override
	public void onLowMemory() {
		super.onLowMemory();
		shutdownHttpClient();
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
		shutdownHttpClient();
	}
	
	/**
	 * Reporta si hay algun error en la conexion
	 */
	public boolean conexionSinErrores() {
		if (responseCode == 200) {
			return true;
		}
		
		/*
		 * } else if (responseCode == 401) { // } else if (responseCode == 408)
		 * { // errorType = this.getString(R.string.networkErrorTimeout); } else
		 * if (responseCode == 500) { // errorType = //
		 * this.getString(R.string.networkErrorInternalServerError); }
		 */
		return false;
	}
	
	/**
	 * Cierra el http cliente
	 */
	private void shutdownHttpClient() {
		if (httpClient != null && httpClient.getConnectionManager() != null) {
			httpClient.getConnectionManager().shutdown();
		}
	}
	
	/**
	 * Crea un nuevo cliente http
	 * 
	 * @return el cliente http con las especificaciones requeridas
	 */
	public DefaultHttpClient createHttpClient() {
		HttpParams params = new BasicHttpParams();
		
		HttpConnectionParams.setConnectionTimeout(params, 150000);
		HttpConnectionParams.setSoTimeout(params, 150000);
		
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(params, HTTP.DEFAULT_CONTENT_CHARSET);
		HttpProtocolParams.setUseExpectContinue(params, true);
		
		SchemeRegistry schReg = new SchemeRegistry();
		schReg.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
		schReg.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
		ClientConnectionManager conMgr = new ThreadSafeClientConnManager(params, schReg);
		return new DefaultHttpClient(conMgr, params);
	}
	
	/**
	 * Verifica el login y devuelve el resumen de cuentas si es exitoso el login
	 * 
	 * @param userName
	 *            nombre de usuario
	 * @param password
	 *            contraseña
	 * @return Resumen de cuenta
	 */
	public InputStream login(String pcUsername, String pcPassword) {
		InputStream responseEntityPostStream = null;
		String URL = baseURL + this.getString(R.string.login_url);
		sessionCookie = "";
		
		try {
			JSONObject userModel = new JSONObject();
			userModel.put("usr", pcUsername);
			userModel.put("pass", pcPassword);
			
			StringEntity postEntity = new StringEntity(userModel.toString());
			postEntity.setContentType("application/json");
			
			responseEntityPostStream = obtenerDatosServidor(new URI(URL), postEntity);
		} 
		catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return responseEntityPostStream;
	}
	
	/**
	 {
	 	ÒusrÓ:ÓJohnDoeÓ,
	 	ÓpassÓ:Ó 45f2d93cd80e3e1df23e8e07021d8177Ó,
		ÒsmartphoneÓ:{
			ÒlocationÓ:{ÒlatitudeÓ:Ó10,239Ó, ÒlongitudeÓ:Ó12,8745Ó}, 
			ÒactiveContactsÓ:[
				{ÒfirstNameÓ:ÓPedroÓ,ÓlastNameÓ:ÓPerezÓ,ÓphonesÓ:[{ÒtypeÓ:1,ÓphoneNumberÓ:Ó02129762345Ó}]}, 
				{ÒfirstNameÓ:ÓMariaÓ,ÓlastNameÓ:ÓVicentiniÓ,ÓphonesÓ:[{ÒtypeÓ:2,ÓphoneNumberÓ:Ó04123456789Ó}]}
			], 
			ÒnameÓ:ÓPPSmartÓ, 
			ÒdeviceÓ:{ÒmodelÓ:Ó9000Ó,ÓversionÓ:Ó5.2Ó,ÓtypeÓ:3}, 
			ÓserialÓ:ÓAX1-BBMPA2Ó, 
			ÒappVersionÓ:Ó1.0.0Ó}}
	 */
	
	/**
	 * Verifica el login y devuelve el resumen de cuentas si es exitoso el login
	 * 
	 * @param userName
	 *            nombre de usuario
	 * @param password
	 *            contraseña
	 * @return Resumen de cuenta
	 */
	public InputStream registerSmartphone(String pcSmartphoneName) {
		InputStream responseEntityPostStream = null;
		String URL = baseURL + this.getString(R.string.register_url);
		
//		try {
//			JSONObject userModel = new JSONObject();
//			userModel.put("usr", pcUsername);
//			userModel.put("pass", pcPassword);
//			
//			StringEntity postEntity = new StringEntity(userModel.toString());
//			postEntity.setContentType("application/json");
//			
//			responseEntityPostStream = obtenerDatosServidor(new URI(URL), postEntity);
//		} 
//		catch (URISyntaxException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} 
//		catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} 
//		catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		return responseEntityPostStream;
	}
	
	/**
	 * Getter para el cliente
	 * 
	 * @return httpClient
	 */
	public HttpClient getHttpClient() {
		return httpClient;
	}
	
	/**
	 * Revisa si existe una conexion de datos para lanzar un request(Radio o
	 * WiFi)
	 * 
	 * @return existencia de la conexion
	 */
	public boolean conexionDisponible() {
		boolean disponible = false;
		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connMgr.getActiveNetworkInfo();
		
		if (info != null) {
			return (info.isConnected());
		}
		
		return disponible;
	}
	
	/**
	 * Obtiene el cookie store del cliente de http
	 * 
	 * @return el cookieStore
	 */
	public CookieStore getCookieStore() {
		return httpClient.getCookieStore();
	}
	
	/**
	 * Funcion encargada de hacer la comunicacion con el servidor, todas las
	 * funciones de comunicacion arman su propio postEntity y llaman a esta
	 * funcion.
	 * 
	 * @param URL
	 *            Url al cual lanzar el request
	 * @param postEntity
	 *            Request post formateado apropiadamente
	 * @return la respuesta del servidor en formato de inputStream
	 */
	public InputStream obtenerDatosServidor(URI URL, HttpEntity postEntity) {
		// Cancelar el timer de la sesión para que no se cierra mientras
		// comunica
		cancelTimer();
		
		HttpClient client = getHttpClient();
		HttpPost request = new HttpPost();
		InputStream resEntityPostStream = null;
		
		if (!conexionDisponible()) {
			responseCode = -1;
		} 
		else {
			try {
				request.setURI(URL);
				
				if (postEntity != null) {
					request.setEntity(postEntity);
				}
				
				HttpResponse response = client.execute(request);
				responseCode = response.getStatusLine().getStatusCode();
				CookieStore cookieStore = getCookieStore();
				
				if (responseCode == 200) {
					HttpEntity resEntityPost = response.getEntity();
					
//					if (sessionCookie.equalsIgnoreCase("")) {
//						if(response.getHeaders("CookieId").length > 0) {
//							sessionCookie = response.getHeaders("CookieId")[0].getValue();
//						}
//					}
					
					resEntityPostStream = resEntityPost.getContent();
					cookieStore.clear();
				} 
				else {
					
				}
//				else if ((responseCode == 302) || (responseCode == 304)) {
//					String url_nueva = response.getHeaders("Location")[0].getValue();
//					URI uri = null;
//					
//					try {
//						uri = new URI(url_nueva);
//					} 
//					catch (URISyntaxException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//					
//					cookieStore.clear();
//					return obtenerDatosServidor(uri, postEntity);
//				}
			} 
			catch (IllegalStateException e) {
				e.printStackTrace();
				
				return null;
			} 
			catch (IOException e) {
				e.printStackTrace();
				
				return null;
			}

		}
		
		String stringURL = URL.toASCIIString();
		String stringLogout = baseURL + this.getString(R.string.logout_url);
		
		if (!stringURL.equalsIgnoreCase(stringLogout)) {
			initTimer();
		}
		
		return resEntityPostStream;
	}
	
	/**
	 * Inicializa el timer que cuenta el tiempo de expiracion de la sesion local
	 */
	public void initTimer() {
		cancelTimer();
		setTimestamp();
		
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				expirarSesion();
			}
		};
		
		sessionTimer = new Timer();
		sessionTimer.schedule(task, timeout);
	}
	
	/**
	 * Esta funcion lanza a la aplicacion a la pantalla de login
	 */
	public void returnToLogin() {
		final Intent intent = new Intent(loginContext, Login.class);
		
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		loginContext.startActivity(intent);
	}
	
	/**
	 * Esta funcion resetea el sesion cookie y evalua la accion a tomar
	 */
	public void expirarSesion() {
		sessionCookie = "";
		
		if (visible) {
			returnToLogin();
		} 
		else {
			reset = true;
		}
	}
	
	/**
	 * Coloca al timestamp la fecha hora actual en formato long para luego
	 * evaluarla con una resta
	 */
	public void setTimestamp() {
		timestamp = new Date().getTime();
	}
	
	/**
	 * Cancela el timer que cuenta el tiempo de expiracion de la sesion local
	 */
	public void cancelTimer() {
		if (sessionTimer != null) {
			sessionTimer.cancel();
		}
	}
	
	public String mensajeParaError() {
//		if (responseCode == 401) {
//			//No autorizado
//			return loginContext.getString(R.string.conex_error_401);
//		} 
//		else if (responseCode == 408) {
//			//Timeout
//			return loginContext.getString(R.string.conex_error_408);
//		} 
//		else if (responseCode == 500) {
//			//Internal server error
//			return loginContext.getString(R.string.conex_error_500);
//		} 
//		else if (responseCode == -1){
//			//Sin conexion
//			return loginContext.getString(R.string.conex_error_sin_conexion);
//		}
		
		//Cualquier otro.
		return loginContext.getString(R.string.generic_error);
	}
}
