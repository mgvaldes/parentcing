package com.ing3nia.parentalcontrol.client.views;

import java.util.ArrayList;
import java.util.List;

import com.google.appengine.api.datastore.GeoPt;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.Maps;
import com.google.gwt.maps.client.control.LargeMapControl;
import com.google.gwt.maps.client.geocode.Geocoder;
import com.google.gwt.maps.client.geocode.LocationCallback;
import com.google.gwt.maps.client.geocode.Placemark;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTMLPanel;

public class DeviceDailyRouteView {
	/**
	 * Center Panel containing all the widgets of the 
	 * alert list view.
	 */
	private HTMLPanel centerContent;
	
	/**
	 * Main panel of alert list view that groups all 
	 * the widgets together.
	 */
	private HTMLPanel viewContent;
	
	/**
	 * List of points defining a paticular route of a device.
	 */
	private List<GeoPt> deviceRoute;
	
	/**
	 * List of street or avenues's names of a paticular route of a device.
	 */
	private List<String> deviceRouteNames;
	
	/**
	 * Map where all device locations are displayed.
	 */
	private MapWidget map;
	
	public DeviceDailyRouteView() {
		viewContent = new HTMLPanel("");
		deviceRoute = new ArrayList<GeoPt>();
		deviceRouteNames = new ArrayList<String>();
		
		Maps.loadMapsApi(ViewUtils.mapsKey, "2", false, new Runnable() {
			public void run() {
				loadDeviceRoute();
			}
		});
		
		this.viewContent.add(this.map);
		this.centerContent.add(this.viewContent);
	}
	
	public void loadDeviceRoute() {
		LatLng deviceLoc = LatLng.newInstance(deviceRoute.get(0).getLatitude(), deviceRoute.get(0).getLongitude());
	    
	    map = new MapWidget(deviceLoc, 2);
	    map.setSize("100%", "100%");	  
	    map.addControl(new LargeMapControl());
	    
	    Geocoder geocoder = new Geocoder();

	    for (GeoPt devLoc : deviceRoute) {
	    	deviceLoc = LatLng.newInstance(devLoc.getLatitude(), devLoc.getLongitude());
	    	map.addOverlay(new Marker(deviceLoc));	 
	    	geocoder.getLocations(deviceLoc, new LocationCallback() {
				@Override
				public void onSuccess(JsArray<Placemark> locations) {
					for (int i = 0; i < locations.length(); ++i) {
						Placemark location = locations.get(i);
						StringBuilder value = new StringBuilder();
						
						if (location.getAddress() != null) {
							value.append(location.getAddress());
						} 
						else {
							if (location.getCity() != null) {
								value.append(location.getCity());
							}
							
							if (location.getAdministrativeArea() != null) {
								value.append(location.getAdministrativeArea() + ", ");
							}
							
							if (location.getCountry() != null) {
								value.append(location.getCountry());
							}
						}
						
						deviceRouteNames.add(value.toString());
					}
				}
				
				@Override
				public void onFailure(int statusCode) {
					Window.alert("Failed to geocode position");
				}
			});
	    }	    
	}
}
