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
import com.google.gwt.maps.client.geom.LatLngBounds;
import com.google.gwt.maps.client.geom.Size;
import com.google.gwt.maps.client.overlay.Icon;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.maps.client.overlay.MarkerOptions;
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
import com.ing3nia.parentalcontrol.client.utils.PCMapStyle;
import com.ing3nia.parentalcontrol.client.views.async.DeviceRouteLocationCallbackHandler;

public class DeviceDailyRouteView {
	
	
	public static String VIEW_CONTENT_CLASSNAME = "deviceDailyRouteContent";
	
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
		this.viewContent.setStyleName(VIEW_CONTENT_CLASSNAME);
		deviceRoute = new ArrayList<GeoPtModel>();
		deviceRouteNames = new ArrayList<String>();
		this.routePanelBody = routePanelBody;
		this.map = baseViewHandler.getMapWidget();
	}

	public void initDeviceLocationLoad() {
		this.centerContent.clear();
		
		if (!Maps.isLoaded()) {
			Maps.loadMapsApi(ViewUtils.mapsKey, "2", false, new Runnable() {
				public void run() {
					loadDeviceRoute(map);
				}
			});
		}else{
			loadDeviceRoute(map);
		}
		
	}
	
	public void loadDirectionsRoute(Waypoint[] waypoints) {
 
		    DirectionsPanel directionsPanel = new DirectionsPanel();
		    directionsPanel.setSize("100%", "100%");

		    DirectionQueryOptions opts = new DirectionQueryOptions(map, directionsPanel);
		    
		    Directions.loadFromWaypoints(waypoints, opts);
		    
		    /*
		    for (GeoPtModel devLoc : deviceLocations) {
		    	deviceLoc = LatLng.newInstance(devLoc.getLatitude(), devLoc.getLongitude());
		    	
		    	//TODO Test setting device image
		    	Icon icon = Icon.newInstance(PCMapStyle.getMarkerImageURL(deviceCount));
		    	icon.setIconSize(Size.newInstance(20, 34));
		    	MarkerOptions options = MarkerOptions.newInstance();
		    	options.setIcon(icon);
		    
		    	  	
		    	this.map.addOverlay(new Marker(deviceLoc,options));
		    	deviceCount++;
		    }
		    */
		    final DockLayoutPanel dock = new DockLayoutPanel(Unit.PCT);
		    dock.setStyleName("mapPanel");
		    dock.addNorth(this.map, 100);
		    
		    this.viewContent.add(dock);
			this.centerContent.add(this.viewContent);
	}
	
	
	public void loadDeviceRoute(MapWidget mapWidget) {
		this.centerContent.clear();
		LatLng deviceLoc = LatLng.newInstance(deviceRoute.get(0).getLatitude(), deviceRoute.get(0).getLongitude());
	
		if(mapWidget == null){
			this.map = new MapWidget(deviceLoc,11);
		    this.map.setSize("100%", "100%");	  
		    this.map.addControl(new LargeMapControl());
		    this.map.setStyleName("googleMap");
		    this.baseViewHandler.setMapWidget(map);
		}else{
			this.map = mapWidget;
			this.map.clearOverlays();
			this.map.setCenter(deviceLoc);
		}
		
	    Geocoder geocoder = new Geocoder();

	    LatLng[] locationArray = new LatLng[deviceRoute.size()];
	    int locCounter = 0;

	    //filling route panel with default label
		for (GeoPtModel devLoc : deviceRoute) {
			Label pointLabel = new Label("loading address");
			pointLabel.setStyleName("routeAddressLabel");
			this.routePanelBody.add(pointLabel);
		}
	    
	    int pointWidgetIndex = 0;
		for (GeoPtModel devLoc : deviceRoute) {

			locationArray[locCounter] = LatLng.newInstance(
					deviceRoute.get(locCounter).getLatitude(),
					deviceRoute.get(locCounter).getLongitude());
			locCounter++;

			deviceLoc = LatLng.newInstance(devLoc.getLatitude(),
					devLoc.getLongitude());
			// map.addOverlay(new Marker(deviceLoc));

			DeviceRouteLocationCallbackHandler locationCallback = new DeviceRouteLocationCallbackHandler(
					baseViewHandler.getMenuSetter().getParentSmartphoneButton(),
					this.routePanelBody, pointWidgetIndex);
			
			geocoder.getLocations(deviceLoc, locationCallback);
			pointWidgetIndex++;
		}

		int deviceCount = 0;
		for(GeoPtModel devLoc : deviceRoute){
			deviceLoc = LatLng.newInstance(devLoc.getLatitude(),
					devLoc.getLongitude());
	    	Icon icon = Icon.newInstance(PCMapStyle.getMarkerImageURL(deviceCount));
	    	icon.setIconSize(Size.newInstance(20, 34));
	    	MarkerOptions options = MarkerOptions.newInstance();
	    	options.setIcon(icon);
	    	this.map.addOverlay(new Marker(deviceLoc,options));	
	    	
	    	deviceCount++;    	
	    	
		}
		
	    
	    //Polyline polyline = new Polyline(locationArray);
	    //map.addOverlay(polyline);

		/*
	    Waypoint[] waypoints = new Waypoint[locationArray.length];
	    int i=0;
	    for(LatLng latlng: locationArray){
	    	waypoints[i] = new Waypoint(latlng);
	    	i++;
	    }
	    loadDirectionsRoute(waypoints);*/
		
		
		// NEW for no Directions Route
	    final DockLayoutPanel dock = new DockLayoutPanel(Unit.PCT);
	    dock.setStyleName("mapPanel");
	    dock.addNorth(this.map, 100);
	    
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
