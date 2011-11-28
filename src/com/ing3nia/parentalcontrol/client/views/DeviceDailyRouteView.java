package com.ing3nia.parentalcontrol.client.views;

import java.util.ArrayList;
import java.util.List;


import com.google.gwt.core.client.JsArray;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.Maps;
import com.google.gwt.maps.client.control.LargeMapControl;
import com.google.gwt.maps.client.geocode.DirectionQueryOptions;
import com.google.gwt.maps.client.geocode.DirectionQueryOptions.TravelMode;
//import com.google.gwt.maps.client.geocode.Directions;
import com.google.gwt.maps.client.geocode.Directions;
import com.google.gwt.maps.client.geocode.DirectionsPanel;
import com.google.gwt.maps.client.geocode.Geocoder;
import com.google.gwt.maps.client.geocode.LocationCallback;
import com.google.gwt.maps.client.geocode.Placemark;
import com.google.gwt.maps.client.geocode.Waypoint;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.maps.client.overlay.Overlay;
import com.google.gwt.maps.client.overlay.Polyline;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.ing3nia.parentalcontrol.client.PCBaseUIBinder;
import com.ing3nia.parentalcontrol.client.handlers.BaseViewHandler;
import com.ing3nia.parentalcontrol.client.models.GeoPtModel;
import com.ing3nia.parentalcontrol.client.views.async.DeviceRouteLocationCallback;

public class DeviceDailyRouteView {
	
	PCBaseUIBinder baseBinder;
	
	BaseViewHandler baseViewHandler;
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
	
	private HTMLPanel routePanelBody;
	
	/**
	 * Map where all device locations are displayed.
	 */
	private MapWidget map;
	
	public DeviceDailyRouteView(PCBaseUIBinder baseBinder, BaseViewHandler baseViewHandler, HTMLPanel routePanelBody) {
		this.centerContent = baseBinder.getCenterContent();
		this.centerContent.setStyleName("mapCenterContent");
		this.baseViewHandler = baseViewHandler;
		viewContent = new HTMLPanel("");
		deviceRoute = new ArrayList<GeoPtModel>();
		deviceRouteNames = new ArrayList<String>();
		this.routePanelBody = routePanelBody;
	}

	public void initDeviceLocationLoad() {
		this.centerContent.clear();
		if (!Maps.isLoaded()) {
			Maps.loadMapsApi(ViewUtils.mapsKey, "2", false, new Runnable() {
				public void run() {
					loadDeviceRoute();
				}
			});
		}else{
			loadDeviceRoute();
		}
		
	}
	
	public void loadDirectionsRoute(Waypoint[] waypoints) {
 
		    DirectionsPanel directionsPanel = new DirectionsPanel();
		    directionsPanel.setSize("100%", "100%");

		    DirectionQueryOptions opts = new DirectionQueryOptions(map, directionsPanel);
		    
		    Directions.loadFromWaypoints(waypoints, opts);
		    
		    final DockLayoutPanel dock = new DockLayoutPanel(Unit.PCT);
		    dock.setStyleName("mapPanel");
		    dock.addNorth(this.map, 100);
		    
		    this.viewContent.add(dock);
			this.centerContent.add(this.viewContent);
	}
	
	
	public void loadDeviceRoute() {
		this.centerContent.clear();
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
	    	//map.addOverlay(new Marker(deviceLoc));	 
	    	    	
	    	DeviceRouteLocationCallback locationCallback = new DeviceRouteLocationCallback(baseViewHandler.getMenuSetter().getParentSmartphoneButton(), this.routePanelBody);
      	    	geocoder.getLocations(deviceLoc, locationCallback);	    	
	    }
	    
	    //Polyline polyline = new Polyline(locationArray);
	    //map.addOverlay(polyline);

	    Waypoint[] waypoints = new Waypoint[locationArray.length];
	    int i=0;
	    for(LatLng latlng: locationArray){
	    	waypoints[i] = new Waypoint(latlng);
	    	i++;
	    }
	    
	    loadDirectionsRoute(waypoints);

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
