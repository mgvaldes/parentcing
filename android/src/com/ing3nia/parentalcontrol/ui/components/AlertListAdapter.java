package com.ing3nia.parentalcontrol.ui.components;

import java.util.ArrayList;

import com.ing3nia.parentalcontrol.R;
import com.ing3nia.parentalcontrol.models.NotificationModel;
import com.ing3nia.parentalcontrol.models.utils.PCNotificationTypeId;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class AlertListAdapter extends ArrayAdapter<NotificationModel> {
	private final Activity context;
	private ArrayList<NotificationModel> alerts;
	private String smartphoneName;

	public AlertListAdapter(Activity context, ArrayList<NotificationModel> alerts, String smartphoneName) {
		super(context, R.layout.alert_list_row, alerts);
		this.context = context;
		this.alerts = alerts;
		this.smartphoneName = smartphoneName;
	}
	
	public AlertListAdapter(Activity context, ArrayList<NotificationModel> alerts) {
		super(context, R.layout.alert_list_row, alerts);
		this.context = context;
		this.alerts = alerts;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = context.getLayoutInflater();
		View rowView = inflater.inflate(R.layout.alert_list_row, null, true);
		
		TextView alertNameTextView = (TextView) rowView.findViewById(R.id.alertName);
		TextView deviceNameTextView = (TextView) rowView.findViewById(R.id.deviceName);
		TextView alertDateTextView = (TextView) rowView.findViewById(R.id.alertDate);
		
		alertNameTextView.setText(PCNotificationTypeId.getNotificationMessageFromType(alerts.get(position).getType()));
		deviceNameTextView.setText(smartphoneName);
		alertDateTextView.setText(alerts.get(position).getDate());
		
		return rowView;
	}
}
