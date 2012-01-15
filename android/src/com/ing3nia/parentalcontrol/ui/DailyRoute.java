package com.ing3nia.parentalcontrol.ui;

import android.os.Bundle;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.ing3nia.parentalcontrol.R;

public class DailyRoute extends MapActivity {

	@Override
	protected boolean isRouteDisplayed() {
	    return false;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.daily_route);
	    
	    MapView mapView = (MapView) findViewById(R.id.dailyRoute);
	    mapView.setBuiltInZoomControls(true);
	}
}
