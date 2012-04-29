package com.ing3nia.parentalcontrol.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.ing3nia.parentalcontrol.R;
import com.ing3nia.parentalcontrol.httpconnect.HttpClientHandler;
import com.ing3nia.parentalcontrol.models.SmartphoneModel;
import com.ing3nia.parentalcontrol.ui.utils.PCMapMarkersEnum;
import com.ing3nia.parentalcontrol.ui.utils.PCViewsEnum;

public class SmartphoneMap extends MapActivity {
	
	private MapController mapController;
	private GeoPoint[] locations;
	private ArrayList<SmartphoneModel> smartphonesGeneral;
	private HttpClientHandler httpClientHandlerApp;
//	private AsyncTask<Void, Void, Void> asyncLogoutThread;
//	private IntentFilter filterAnswerDialog;
//	private BroadcastReceiver answerDialog;
	private static final int ALERT_DIALOG_EXIT = 1;
	
	@Override
	protected boolean isRouteDisplayed() {
	    return false;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
		setContentView(R.layout.smartphone_map);
	    
	    if (httpClientHandlerApp == null) { 
	    	httpClientHandlerApp = (HttpClientHandler) SmartphoneMap.this.getApplication();
	    }
	    
	    smartphonesGeneral = httpClientHandlerApp.smartphonesGeneral;
	    
	    MapView mapView = (MapView) findViewById(R.id.smartphoneMap);
	    mapView.setBuiltInZoomControls(true);
	    //mapView.setStreetView(true);
	    
	    mapController = mapView.getController();
	    mapController.setZoom(13);	    
	    
	    loadSmartphoneLocations();
        
	    if (locations.length > 0) {
	    	mapController.animateTo(locations[0]);
	    }
        
        //---Add a location marker---
        MapOverlay mapOverlay = new MapOverlay();
        List<Overlay> listOfOverlays = mapView.getOverlays();
        listOfOverlays.clear();
        listOfOverlays.add(mapOverlay);        
 
        mapView.invalidate();
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
			AlertDialog.Builder alert = new AlertDialog.Builder(SmartphoneMap.this);
	    	alert.setIcon(R.drawable.alert_icon);
            alert.setTitle("Alert");
            alert.setMessage(R.string.quit_app);
            alert.setPositiveButton(R.string.alert_dialog_yes_button_label, new DialogInterface.OnClickListener() {
            	public void onClick(DialogInterface dialog, int whichButton) {
            		dialog.dismiss();
            		
                 	Intent i = new Intent(SmartphoneMap.this, Login.class);
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
		
		return null;
	}
	
	public void loadSmartphoneLocations() {
		int smartphoneListSize = smartphonesGeneral.size();
		locations = new GeoPoint[smartphoneListSize];
		
		double lat;
        double lng;
		
		for (int i = 0; i < smartphoneListSize; i++) {
			lat = Double.parseDouble(smartphonesGeneral.get(i).getLocation().getLatitude());
			lng = Double.parseDouble(smartphonesGeneral.get(i).getLocation().getLongitude());
			
			locations[i] = new GeoPoint((int) (lat * 1E6), (int) (lng * 1E6));			
		}		
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
            return true;
        case R.id.smartphoneList:
        	loadSmartphoneList();
            return true;
        default:
            return super.onOptionsItemSelected(item);         
	    }
	}
	
	public void loadSmartphoneList() {
		Intent i = new Intent(SmartphoneMap.this, SmartphoneList.class);	
		
		httpClientHandlerApp.lastView = PCViewsEnum.SMARTPHONE_MAP_VIEW.getId();
		
		startActivity(i);
	}
	
	class MapOverlay extends com.google.android.maps.Overlay {
        @Override
        public boolean draw(Canvas canvas, MapView mapView, boolean shadow, long when)  {
            super.draw(canvas, mapView, shadow);                   
 
            //---translate the GeoPoint to screen pixels---
            Point screenPts;
            PCMapMarkersEnum[] markers = PCMapMarkersEnum.values();
            int pos = 0;
            
            for (GeoPoint p : locations) {
            	screenPts = new Point();
            	mapView.getProjection().toPixels(p, screenPts);
            	
            	//---add the marker---
                Bitmap bmp = BitmapFactory.decodeResource(getResources(), markers[pos].getImageName());            
                canvas.drawBitmap(bmp, screenPts.x, screenPts.y-50, null);
                
                pos++;
			}
          
            return true;
        }
    } 
	
//	private void logout(){
//		asyncLogoutThread = new LogoutThread().execute();
//		SmartphoneMap.this.finish();
//	}
//	
//	/**
//	 * Clase que maneja el Logout, es un asyncTask para operar de fondo
//	 * @author Stefano
//	 *
//	 */
//	public class LogoutThread extends AsyncTask<Void, Void, Void> {
//		@Override
//		protected Void doInBackground(Void... args) {
//			try {
//				HttpClientHandler httpClientHandlerApp = (HttpClientHandler) SmartphoneMap.this.getApplication();
//				httpClientHandlerApp.logout();				
//			} 
//			catch (Exception e) {
//				Log.e("1", "AsyncLogin Error", e);
//			}
//			
//			return null;
//		}
//	}
}
