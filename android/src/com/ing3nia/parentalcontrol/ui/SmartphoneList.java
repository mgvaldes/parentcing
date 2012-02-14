package com.ing3nia.parentalcontrol.ui;

import com.ing3nia.parentalcontrol.ui.components.SmartphoneListAapter;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class SmartphoneList extends ListActivity {
	TextView selection;
	String[] smartphoneNames = {"Conejo", "Lechon", "Perro", "Gato", "Caballo", "Iguana"};
	String[] newAlerts = {"4", "1", "3", "3", "5", "2"};
	String[] lastAlerts = {"Device Battery Dead", "Unreacheable Device", "Unreacheable Device", "Device Battery Dead", "Device Battery Dead", "Device Battery Dead"};
	/**
	 * 0 = Android
	 * 1 = Apple
	 * 2 = Blackberry
	 */
	int[] deviceTypes = {0, 0, 1, 0, 2, 1}; 

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setListAdapter(new SmartphoneListAapter(this, smartphoneNames, newAlerts, lastAlerts, deviceTypes));
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		Intent i = new Intent(SmartphoneList.this, DailyRoute.class);
		startActivity(i);
	}
}
