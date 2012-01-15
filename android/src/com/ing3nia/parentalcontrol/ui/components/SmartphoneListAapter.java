package com.ing3nia.parentalcontrol.ui.components;

import com.ing3nia.parentalcontrol.R;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SmartphoneListAapter extends ArrayAdapter<String> {
	private final Activity context;
	private final String[] smartphoneNames;
	private final String[] newAlerts;
	private final String[] lastAlerts;
	private final int[] deviceTypes;

	public SmartphoneListAapter(Activity context, String[] smartphoneNames, String[] newAlerts, String[] lastAlerts, int[] deviceTypes) {
		super(context, R.layout.smartphone_list_row, smartphoneNames);
		this.context = context;
		this.smartphoneNames = smartphoneNames;
		this.newAlerts = newAlerts;
		this.lastAlerts = lastAlerts;
		this.deviceTypes = deviceTypes;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = context.getLayoutInflater();
		View rowView = inflater.inflate(R.layout.smartphone_list_row, null, true);
		
		TextView smartphoneNameTextView = (TextView) rowView.findViewById(R.id.smartphoneName);
		ImageView deviceTypeImageView = (ImageView) rowView.findViewById(R.id.deviceType);
		TextView newAlertsTextView = (TextView) rowView.findViewById(R.id.newAlerts);
		TextView lastAlertTextView = (TextView) rowView.findViewById(R.id.lastAlert);
		
		smartphoneNameTextView.setText(smartphoneNames[position]);
		newAlertsTextView.setText(newAlerts[position]);
		lastAlertTextView.setText(lastAlerts[position]);
		
		if (deviceTypes[position] == 0) { //Android
			deviceTypeImageView.setImageResource(R.drawable.android);
		} 
		else if (deviceTypes[position] == 1) { //Apple
			deviceTypeImageView.setImageResource(R.drawable.apple);
		}
		else if (deviceTypes[position] == 2) { //Blackberry
			deviceTypeImageView.setImageResource(R.drawable.blackberry);
		}

		return rowView;
	}
}
