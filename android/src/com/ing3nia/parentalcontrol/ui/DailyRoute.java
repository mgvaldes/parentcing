package com.ing3nia.parentalcontrol.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.reflect.TypeToken;
import com.ing3nia.parentalcontrol.R;
import com.ing3nia.parentalcontrol.httpconnect.HttpClientHandler;
import com.ing3nia.parentalcontrol.models.LocationModel;
import com.ing3nia.parentalcontrol.models.ModificationModel;
import com.ing3nia.parentalcontrol.models.RouteModel;
import com.ing3nia.parentalcontrol.models.SmartphoneModel;
import com.ing3nia.parentalcontrol.models.TicketModel;
import com.ing3nia.parentalcontrol.ui.components.DailyRouteAdapter;
import com.ing3nia.parentalcontrol.ui.components.UiUtils;
import com.ing3nia.parentalcontrol.ui.utils.PCDailyRouteMapMarkersEnum;
import com.ing3nia.parentalcontrol.ui.utils.PCViewsEnum;

public class DailyRoute extends MapActivity {
	
	private GeoPoint[] locations;
	private MapController mapController;
	private String[] address;
	private ArrayList<SmartphoneModel> smartphonesGeneral;
	private SmartphoneModel smartphone;
	private int selected;
	private MapView mapView;
	private static final int ALERT_DIALOG = 1;
	private HttpClientHandler httpClientHandlerApp;
	private AsyncTask<Void, Void, Boolean> asyncSaveChangesThread;
	private AsyncTask<Void, Void, Boolean> asyncTicketsListThread;
	private AsyncTask<Void, Void, Boolean> asyncAdminUserListThread;
	private ModificationModel modification;
	static ProgressDialog processDialog;
	private int nextScreen;
	private LinearLayout topBanner;
	private LinearLayout topBannerSaveChanges;
	private String userKey;
	private String errorMessage = "";
	private static final int ERROR_ALERT_DIALOG = 2;

	@Override
	protected boolean isRouteDisplayed() {
	    return false;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    if (httpClientHandlerApp == null) { 
			httpClientHandlerApp = (HttpClientHandler) DailyRoute.this.getApplication();
		}
	    
	    smartphonesGeneral = httpClientHandlerApp.smartphonesGeneral;
	    smartphone = httpClientHandlerApp.smartphone;
	    selected = httpClientHandlerApp.selected;
	    userKey = httpClientHandlerApp.userKey;
	    
	    setContentView(R.layout.daily_route);
	    
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
	    
	    mapView = (MapView) findViewById(R.id.dailyRouteMap);
	    mapView.setBuiltInZoomControls(true);
	    
	    mapController = mapView.getController();
	    mapController.setZoom(17);	    
	    
	    loadSmartphoneRoute();
	    
	    if (locations.length > 0) {
	    	mapController.animateTo(locations[0]);
	    }	    
        
        //---Add a location marker---
        MapOverlay mapOverlay = new MapOverlay();
        List<Overlay> listOfOverlays = mapView.getOverlays();
        listOfOverlays.clear();
        listOfOverlays.add(mapOverlay);
        
        drawPath(0x000000);
 
        mapView.invalidate();
	    
	    TextView title = (TextView) findViewById(R.id.dailyRouteTitle);
	    title.setText(smartphonesGeneral.get(selected).getName() + "'s Daily Route");
	    
	    ListView dailyRouteList = (ListView)findViewById(R.id.dailyRouteList);
	    
		dailyRouteList.setAdapter(new DailyRouteAdapter(this, this.address));
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
		
		if (httpClientHandlerApp == null) { 
			httpClientHandlerApp = (HttpClientHandler) DailyRoute.this.getApplication();
		}
	    
	    smartphonesGeneral = httpClientHandlerApp.smartphonesGeneral;
	    selected = httpClientHandlerApp.selected;
	    smartphone = httpClientHandlerApp.smartphone; 
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
	
	/**
	 * Sobrecarga el comportamiento cuando se presiona la tecla fisica de "back".
	 * Interceptado para colocar el logout de esta manera tambien.
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	    	if (httpClientHandlerApp.pendingChanges) {
	    		nextScreen = 0;
				showDialog(ALERT_DIALOG);
	    	}
	    }
	    
	    return super.onKeyDown(keyCode, event);
	}	
	
	private void drawPath(int color) {
		List<Overlay> overlays = mapView.getOverlays();

		for (int i = 1; i < locations.length; i++) {
			overlays.add(new RouteOverlay(locations[i - 1], locations[i], color));
		}
	}
	
	public ArrayList<LocationModel> getDummyDeviceRoute(){
		ArrayList<LocationModel> deviceLocations = new ArrayList<LocationModel>();
		LocationModel geo = new LocationModel();
		geo.setLatitude("25.74626466540882");
		geo.setLongitude("-80.270254611969");
		deviceLocations.add(geo);
		
		geo = new LocationModel();
		geo.setLatitude("25.75166174584524");
		geo.setLongitude("-80.27297973632812");
		deviceLocations.add(geo);
		
		geo = new LocationModel();
		geo.setLatitude("25.772397395383567");
		geo.setLongitude("-80.25585651397705");
		deviceLocations.add(geo);
		
		geo = new LocationModel();
		geo.setLatitude("25.721083103539254");
		geo.setLongitude("-80.2756404876709");
		deviceLocations.add(geo);
		
		geo = new LocationModel();
		geo.setLatitude("25.72013585186239");
		geo.setLongitude("-80.2772068977356");
		deviceLocations.add(geo);
		
		geo = new LocationModel();
		geo.setLatitude("25.719033937358773");
		geo.setLongitude("-80.27860164642334");
		deviceLocations.add(geo);
		
		return deviceLocations;		
	}
	
	public void loadSmartphoneRoute() {
		int pos = smartphone.getRoutes().size();
		
		if (pos > 0) {
			RouteModel route = smartphone.getRoutes().get(pos - 1); 
			int routeSize = route.getPoints().size();
//			int routeSize = getDummyDeviceRoute().size();
			locations = new GeoPoint[routeSize];
			address = new String[routeSize];
			//Borraaaaaaaar!!!
//			ArrayList<LocationModel> points = getDummyDeviceRoute();
			
			double lat;
	        double lng;
	        Geocoder geoCoder = new Geocoder(getBaseContext(), Locale.getDefault());
	        List<Address> addresses;
	        String auxAddress;
			
			for (int i = 0; i < routeSize; i++) {
				lat = Double.parseDouble(route.getPoints().get(i).getLatitude());
				lng = Double.parseDouble(route.getPoints().get(i).getLongitude());
//				lat = Double.parseDouble(points.get(i).getLatitude());
//				lng = Double.parseDouble(points.get(i).getLongitude());
				
				locations[i] = new GeoPoint((int) (lat * 1E6), (int) (lng * 1E6));	
				
				try {
					addresses = geoCoder.getFromLocation(locations[i].getLatitudeE6() / 1E6, locations[i].getLongitudeE6() / 1E6, 1);    				
					
					if (addresses.size() > 0) {
						auxAddress = "";
						
						for (int j = 0; j < addresses.get(0).getMaxAddressLineIndex(); j++) {
							auxAddress += addresses.get(0).getAddressLine(j) + " ";
						}
						
						address[i] = auxAddress;
					}
				} 
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		}	
		else {
			locations = new GeoPoint[0];
			address = new String[0];
		}
	}
	
    class MapOverlay extends com.google.android.maps.Overlay {
        @Override
        public boolean draw(Canvas canvas, MapView mapView, boolean shadow, long when) {
        	super.draw(canvas, mapView, shadow);                   
        	 
            //---translate the GeoPoint to screen pixels---
            Point screenPts;
            PCDailyRouteMapMarkersEnum[] markers = PCDailyRouteMapMarkersEnum.values();
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
    
    public class RouteOverlay extends Overlay {
        private GeoPoint gp1;
        private GeoPoint gp2;
        private int color;
     
	    public RouteOverlay(GeoPoint gp1, GeoPoint gp2, int color) {
            this.gp1 = gp1;
            this.gp2 = gp2;
            this.color = color;
        }
	    
	    @Override
	    public void draw(Canvas canvas, MapView mapView, boolean shadow) {
	        Projection projection = mapView.getProjection();
	        Paint paint = new Paint();
	        Point point = new Point();
	        Point point2 = new Point();
	        
	        projection.toPixels(gp1, point);
	        projection.toPixels(gp2, point2);
	        
	        paint.setColor(color);
	        paint.setStrokeWidth(5);
	        paint.setAlpha(120);
	        
	        canvas.drawLine(point.x, point.y, point2.x, point2.y, paint);
	        
	        super.draw(canvas, mapView, shadow);
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
		Intent i = new Intent(DailyRoute.this, AlertList.class);
		httpClientHandlerApp.lastView = PCViewsEnum.DAILY_ROUTE_VIEW.getId();
		startActivity(i);
	}
	
	public void loadRuleList() {
		Intent i = new Intent(DailyRoute.this, RuleList.class);
		httpClientHandlerApp.lastView = PCViewsEnum.DAILY_ROUTE_VIEW.getId();
		startActivity(i);
	}
	
	public void loadContactList() {
		Intent i = new Intent(DailyRoute.this, ContactList.class);
		httpClientHandlerApp.lastView = PCViewsEnum.DAILY_ROUTE_VIEW.getId();
		startActivity(i);
	}
	
	public void loadSmartphoneSettings() {
		Intent i = new Intent(DailyRoute.this, SmartphoneSettings.class);
		httpClientHandlerApp.lastView = PCViewsEnum.DAILY_ROUTE_VIEW.getId();
		startActivity(i);
	}
	
	public void loadDeviceMap() {
		if (httpClientHandlerApp.pendingChanges) {
			nextScreen = 0;
			showDialog(ALERT_DIALOG);
		}
		else {
			Intent i = new Intent(DailyRoute.this, SmartphoneMap.class);
			startActivity(i);
		}
	}
	
	public void loadDeviceList() {
		if (httpClientHandlerApp.pendingChanges) {
			nextScreen = 1;
			showDialog(ALERT_DIALOG);
		}
		else {
			Intent i = new Intent(DailyRoute.this, SmartphoneList.class);
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
			AlertDialog.Builder alert = new AlertDialog.Builder(DailyRoute.this);
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
			AlertDialog.Builder alert = new AlertDialog.Builder(DailyRoute.this);
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
		processDialog = ProgressDialog.show(DailyRoute.this, null, DailyRoute.this.getString(R.string.progress_dialog_save_changes), true, true,
			new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					if (asyncSaveChangesThread.cancel(true)) {
						UiUtils.levantarDialog(DailyRoute.this, UiUtils.DIALOG_OK, getString(R.string.progress_dialog_save_changes_cancel));
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
		processDialog = ProgressDialog.show(DailyRoute.this, null, DailyRoute.this.getString(R.string.progress_dialog_tickets_list), true, true,
			new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					if (asyncTicketsListThread.cancel(true)) {
						UiUtils.levantarDialog(DailyRoute.this, UiUtils.DIALOG_OK, getString(R.string.progress_dialog_tickets_list_cancel));
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
		processDialog = ProgressDialog.show(DailyRoute.this, null, DailyRoute.this.getString(R.string.progress_dialog_admin_user_list), true, true,
			new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					if (asyncAdminUserListThread.cancel(true)) {
						UiUtils.levantarDialog(DailyRoute.this, UiUtils.DIALOG_OK, getString(R.string.progress_dialog_admin_user_list_cancel));
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
							i = new Intent(DailyRoute.this, SmartphoneMap.class);
						}
						else {
							i = new Intent(DailyRoute.this, SmartphoneList.class);
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
						
						Intent i = new Intent(DailyRoute.this, TicketList.class);
						
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
						
						Intent i = new Intent(DailyRoute.this, AdminUserList.class);
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
