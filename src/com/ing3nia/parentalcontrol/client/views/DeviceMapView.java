package com.ing3nia.parentalcontrol.client.views;

import java.util.ArrayList;
import java.util.List;

import com.google.appengine.api.datastore.GeoPt;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.Maps;
import com.google.gwt.maps.client.control.LargeMapControl;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.user.client.ui.HTMLPanel;

public class DeviceMapView {
	/**
	 * Center Panel containing all the widgets of the 
	 * alert list view.
	 */
	private HTMLPanel centerContent;
	
	/**
	 * Main panel of alert list view that groups all 
	 * the widgets together.
	 */
	private HTMLPanel viewContent = new HTMLPanel("");
	
	/**
	 * List of alerts received by a user.
	 */
	private List<GeoPt> deviceLocations = new ArrayList<GeoPt>();
	
	/**
	 * Map where all device locations are displayed.
	 */
	private MapWidget map;
	
	public DeviceMapView() {
		Maps.loadMapsApi(ViewUtils.mapsKey, "2", false, new Runnable() {
			public void run() {
				loadDeviceLocations();
			}
		});
	}
	
	public void loadDeviceLocations() {
	    LatLng deviceLoc = LatLng.newInstance(deviceLocations.get(0).getLatitude(), deviceLocations.get(0).getLongitude());
	    
	    map = new MapWidget(deviceLoc, 2);
	    map.setSize("100%", "100%");	  
	    map.addControl(new LargeMapControl());

	    for (GeoPt devLoc : deviceLocations) {
	    	deviceLoc = LatLng.newInstance(devLoc.getLatitude(), devLoc.getLongitude());
	    	map.addOverlay(new Marker(deviceLoc));
	    }	    
	}

	public HTMLPanel getCenterContent() {
		return centerContent;
	}

	public void setCenterContent(HTMLPanel centerContent) {
		this.centerContent = centerContent;
	}

	public HTMLPanel getViewContent() {
		return viewContent;
	}

	public void setViewContent(HTMLPanel viewContent) {
		this.viewContent = viewContent;
	}

	public List<GeoPt> getDeviceLocations() {
		return deviceLocations;
	}

	public void setDeviceLocations(List<GeoPt> deviceLocations) {
		this.deviceLocations = deviceLocations;
	}
}
