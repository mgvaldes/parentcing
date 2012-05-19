package com.ing3nia.parentalcontrol.ui.components;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ing3nia.parentalcontrol.R;

public class DailyRouteAdapter extends ArrayAdapter<String> {
	
	private final Activity context;
	private final String[] locations;
	
	public DailyRouteAdapter(Activity context, String[] locations) {
		super(context, R.layout.contact_list_row, locations);
		this.context = context;
		this.locations = locations;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = context.getLayoutInflater();
		View rowView = inflater.inflate(R.layout.daily_route_row, null, true);
		
		TextView contactNameTextView = (TextView) rowView.findViewById(R.id.location);
		
		contactNameTextView.setText(locations[position]);
		
		return rowView;
	}
}
