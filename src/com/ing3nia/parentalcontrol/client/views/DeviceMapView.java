package com.ing3nia.parentalcontrol.client.views;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.maps.client.InfoWindowContent;
import com.google.gwt.maps.client.MapOptions;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.Maps;
import com.google.gwt.maps.client.control.LargeMapControl;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.ing3nia.parentalcontrol.client.models.GeoPtModel;

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
	private HTMLPanel viewContent;
	
	/**
	 * List of device locations.
	 */
	private List<GeoPtModel> deviceLocations;
	
	/**
	 * Map where all device locations are displayed.
	 */
	private MapWidget map;
	
	public DeviceMapView(HTMLPanel centerContent) {
		this.centerContent = centerContent;
		viewContent = new HTMLPanel("");
		deviceLocations = new ArrayList<GeoPtModel>();
	}
	
	public void initDeviceLocationLoad(){
		Maps.loadMapsApi(ViewUtils.mapsKey, "2", false, new Runnable() {
			public void run() {
				loadDeviceLocations();
			}
		});
	}
	
	public void loadDeviceLocations() {
	    LatLng deviceLoc = LatLng.newInstance(deviceLocations.get(0).getLatitude(), deviceLocations.get(0).getLongitude());
	    
	    map = new MapWidget(deviceLoc,11);
	    map.setSize("100%", "100%");	  
	    map.addControl(new LargeMapControl());
	    map.setStyleName("googleMap");

	    
	    for (GeoPtModel devLoc : deviceLocations) {
	    	deviceLoc = LatLng.newInstance(devLoc.getLatitude(), devLoc.getLongitude());
	    	Marker marker = new Marker(deviceLoc);
	    	map.addOverlay(marker);
	    }
	    
	    // Add a marker
	    map.addOverlay(new Marker(deviceLoc));

	    final DockLayoutPanel dock = new DockLayoutPanel(Unit.PX);
	    dock.addNorth(this.map, 490);
	    
	    this.viewContent.add(dock);
		this.centerContent.add(this.viewContent);
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

	public List<GeoPtModel> getDeviceLocations() {
		return deviceLocations;
	}

	public void setDeviceLocations(List<GeoPtModel> deviceLocations) {
		this.deviceLocations = deviceLocations;
	}
}
