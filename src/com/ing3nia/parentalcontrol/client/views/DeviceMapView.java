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
import com.google.gwt.maps.client.geom.Point;
import com.google.gwt.maps.client.geom.Size;
import com.google.gwt.maps.client.overlay.Icon;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.maps.client.overlay.MarkerOptions;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.ing3nia.parentalcontrol.client.PCBaseUIBinder;
import com.ing3nia.parentalcontrol.client.handlers.BaseViewHandler;
import com.ing3nia.parentalcontrol.client.models.GeoPtModel;
import com.ing3nia.parentalcontrol.client.utils.PCMapStyle;
import com.ing3nia.parentalcontrol.client.views.async.AsyncronousCallsMessages;


public class DeviceMapView {
	
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
	 * List of device locations.
	 */
	private List<GeoPtModel> deviceLocations;
	
	/**
	 * Map where all device locations are displayed.
	 */
	private MapWidget map;
	
	public DeviceMapView(PCBaseUIBinder baseBinder, BaseViewHandler baseView) {
		this.baseBinder = baseBinder;
		this.baseViewHandler = baseView;
		this.centerContent = baseBinder.getCenterContent();
		this.centerContent.setStyleName("mapCenterContent");
		viewContent = new HTMLPanel("");
		deviceLocations = new ArrayList<GeoPtModel>();
		this.map = baseView.getMapWidget();
	}
	
	public void initDeviceLocationLoad(){
		this.centerContent.clear();
		if (!Maps.isLoaded()) {
			Maps.loadMapsApi(ViewUtils.mapsKey, "2", false, new Runnable() {
				public void run() {
					loadDeviceLocations(map);
				}
			});
		}else{
			loadDeviceLocations(map);
		}
	}
	
	public void loadDeviceLocations(MapWidget mapWidget) {	    
		//LoadingView.setLoadingView(this.baseBinder, AsyncronousCallsMessages.LOADING_DEVICES_MAP, this.baseViewHandler.getLoadingImage());
		LatLng deviceLoc;
		if(deviceLocations ==null || deviceLocations.size() == 0){
			deviceLoc = LatLng.newInstance(41, 2);
		}else{
			deviceLoc = LatLng.newInstance(deviceLocations.get(0).getLatitude(), deviceLocations.get(0).getLongitude());
		}
		if(mapWidget == null){
			this.map = new MapWidget(deviceLoc,11);
		    this.map.setSize("100%", "100%");	  
		    this.map.addControl(new LargeMapControl());
		    baseViewHandler.setMapWidget(this.map);
		}else{
			this.map = mapWidget;
			this.map.clearOverlays();
			this.map.setCenter(deviceLoc);
		}

	    int deviceCount = 0;
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
	    
	    // Add a marker
	    //Marker marker = new Marker(deviceLoc);
	    //map.addOverlay(marker);

	    final DockLayoutPanel dock = new DockLayoutPanel(Unit.PCT);
	    dock.setStyleName("mapPanel");
	    dock.addNorth(this.map, 100);
	    
	    this.viewContent.add(dock);
	    this.centerContent.clear();
	    //LoadingView.clearLoadingView(this.baseBinder);
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
