package com.ing3nia.parentalcontrol.client.views.async;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.maps.client.geocode.LocationCallback;
import com.google.gwt.maps.client.geocode.Placemark;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;

public class DeviceRouteLocationCallback implements LocationCallback{

	Button smpButton;
	HTMLPanel scrollPanelBody;
	
	public DeviceRouteLocationCallback(Button smpButton, HTMLPanel scrollPanelBody){
		this.smpButton = smpButton;
		this.scrollPanelBody = scrollPanelBody;
		
	}
	
	@Override
	public void onFailure(int statusCode) {
		Label l = new Label("address could not be loaded");
		l.setStyleName("routeAddressLabel");
		scrollPanelBody.add(l);	
	}

	@Override
	public void onSuccess(JsArray<Placemark> locations) {

		String routeName = locations.get(0).getStreet();
		if(routeName == null){
			routeName = locations.get(0).getAddress();
			if(routeName==null){
				routeName = "address could not be loaded";
			}
		}
		Label l = new Label(routeName);
		l.setStyleName("routeAddressLabel");
		scrollPanelBody.add(l);	
	}
}
