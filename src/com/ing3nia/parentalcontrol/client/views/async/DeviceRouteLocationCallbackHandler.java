package com.ing3nia.parentalcontrol.client.views.async;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.maps.client.geocode.LocationCallback;
import com.google.gwt.maps.client.geocode.Placemark;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;

public class DeviceRouteLocationCallbackHandler implements LocationCallback{

	Button smpButton;
	HTMLPanel scrollPanelBody;
	
	public DeviceRouteLocationCallbackHandler(Button smpButton, HTMLPanel scrollPanelBody){
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
		
		String routeName = "address could not be loaded";
		boolean totalBreak = false;
		for(int i=0; i<locations.length(); i++){
			Placemark p = locations.get(i);
			if(p.getStreet() !=null){
				routeName = p.getStreet();
				totalBreak = true;
				break;	
			}
		}
		if (!totalBreak) {
			for (int i = 0; i < locations.length(); i++) {
				Placemark p = locations.get(i);
				if (p.getAddress() != null) {
					routeName = p.getAddress();
					totalBreak = true;
					break;
				}
			}
		}
		if (!totalBreak) {
			for (int i = 0; i < locations.length(); i++) {
				Placemark p = locations.get(i);
				if (p.getLocality()!= null) {
					routeName = p.getLocality();
					totalBreak = true;
					break;
				}
			}
		}
		if (!totalBreak) {
			for (int i = 0; i < locations.length(); i++) {
				Placemark p = locations.get(i);
				if (p.getCity()!= null) {
					routeName = p.getCity();
					totalBreak = true;
					break;
				}
			}
		}
		Label l = new Label(routeName);
		l.setStyleName("routeAddressLabel");
		scrollPanelBody.add(l);	
	}
}
