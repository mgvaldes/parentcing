package com.ing3nia.parentalcontrol.client.views;

import java.util.ArrayList;
import java.util.List;


import com.google.gwt.core.client.JsArray;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.Maps;
import com.google.gwt.maps.client.control.LargeMapControl;
import com.google.gwt.maps.client.geocode.Geocoder;
import com.google.gwt.maps.client.geocode.LocationCallback;
import com.google.gwt.maps.client.geocode.Placemark;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.maps.client.overlay.Overlay;
import com.google.gwt.maps.client.overlay.Polyline;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.ing3nia.parentalcontrol.client.models.GeoPtModel;

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
	 * List of points defining a particular route of a device.
	 */
	private List<GeoPtModel> deviceRoute;
	
	/**
	 * List of street or avenues's names of a particular route of a device.
	 */
	private List<String> deviceRouteNames;
	
	/**
	 * Map where all device locations are displayed.
	 */
	private MapWidget map;
	
	public DeviceDailyRouteView(HTMLPanel centerContent) {
		this.centerContent = centerContent;
		viewContent = new HTMLPanel("");
		deviceRoute = new ArrayList<GeoPtModel>();
		deviceRouteNames = new ArrayList<String>();
	}
	
	public void initDeviceLocationLoad() {
		Maps.loadMapsApi(ViewUtils.mapsKey, "2", false, new Runnable() {
			public void run() {
				loadDeviceRoute();
			}
		});

	}
	
	public void loadDeviceRoute() {
		LatLng deviceLoc = LatLng.newInstance(deviceRoute.get(0).getLatitude(), deviceRoute.get(0).getLongitude());
		
	    map = new MapWidget(deviceLoc, 15);
	    map.setSize("100%", "100%");	  
	    map.addControl(new LargeMapControl());

	    Geocoder geocoder = new Geocoder();

	    LatLng[] locationArray = new LatLng[deviceRoute.size()];
	    int locCounter = 0;
	    
	    for (GeoPtModel devLoc : deviceRoute) {
	    	
	    	locationArray[locCounter] = LatLng.newInstance(deviceRoute.get(locCounter).getLatitude(), deviceRoute.get(locCounter).getLongitude());
	    	locCounter++;
	    	
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
	    
	    Polyline polyline = new Polyline(locationArray);
	    map.addOverlay(polyline);
	    
	    final DockLayoutPanel dock = new DockLayoutPanel(Unit.PX);
	    dock.addNorth(this.map, 490);
	    
	    this.viewContent.add(dock);
		this.centerContent.add(this.viewContent);
	}

	public List<GeoPtModel> getDeviceRoute() {
		return deviceRoute;
	}

	public void setDeviceRoute(List<GeoPtModel> deviceRoute) {
		this.deviceRoute = deviceRoute;
	}

	public List<String> getDeviceRouteNames() {
		return deviceRouteNames;
	}

	public void setDeviceRouteNames(List<String> deviceRouteNames) {
		this.deviceRouteNames = deviceRouteNames;
	}
	
	
}
