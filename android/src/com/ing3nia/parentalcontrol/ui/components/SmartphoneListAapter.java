package com.ing3nia.parentalcontrol.ui.components;

import java.text.ParseException;
import java.util.ArrayList;

import com.ing3nia.parentalcontrol.R;
import com.ing3nia.parentalcontrol.models.AlertModel;
import com.ing3nia.parentalcontrol.models.SmartphoneModel;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SmartphoneListAapter extends ArrayAdapter<SmartphoneModel> {
	private final Activity context;
	private ArrayList<SmartphoneModel> smartphones;

	public SmartphoneListAapter(Activity context, ArrayList<SmartphoneModel> smartphones) {		
		super(context, R.layout.smartphone_list_row, smartphones);
		this.smartphones = smartphones;
		this.context = context;
	}
	
	public int setDeviceLetters(int position) {
		if (position == 0) {
			return R.drawable.ic_devicelist_a;
		}
		else if (position == 1) {
			return R.drawable.ic_devicelist_b;
		}
		else if (position == 2) {
			return R.drawable.ic_devicelist_c;
		}
		else if (position == 3) {
			return R.drawable.ic_devicelist_d;
		}
		else if (position == 4) {
			return R.drawable.ic_devicelist_e;
		}
		else if (position == 5) {
			return R.drawable.ic_devicelist_f;
		}
		else if (position == 6) {
			return R.drawable.ic_devicelist_g;
		}
		else if (position == 7) {
			return R.drawable.ic_devicelist_h;
		}
		else if (position == 8) {
			return R.drawable.ic_devicelist_i;
		}
		else if (position == 9) {
			return R.drawable.ic_devicelist_j;
		}
		else if (position == 10) {
			return R.drawable.ic_devicelist_k;
		}
		else if (position == 11) {
			return R.drawable.ic_devicelist_l;
		}
		else if (position == 12) {
			return R.drawable.ic_devicelist_m;
		}
		else if (position == 13) {
			return R.drawable.ic_devicelist_n;
		}
		else if (position == 14) {
			return R.drawable.ic_devicelist_o;
		}
		else if (position == 15) {
			return R.drawable.ic_devicelist_p;
		}
		else if (position == 16) {
			return R.drawable.ic_devicelist_q;
		}
		else if (position == 17) {
			return R.drawable.ic_devicelist_r;
		}
		else if (position == 18) {
			return R.drawable.ic_devicelist_s;
		}
		else if (position == 19) {
			return R.drawable.ic_devicelist_t;
		}
		else if (position == 20) {
			return R.drawable.ic_devicelist_u;
		}
		else if (position == 21) {
			return R.drawable.ic_devicelist_v;
		}
		else if (position == 22) {
			return R.drawable.ic_devicelist_w;
		}
		else if (position == 23) {
			return R.drawable.ic_devicelist_x;
		}
		else if (position == 24) {
			return R.drawable.ic_devicelist_y;
		}
		else if (position == 25) {
			return R.drawable.ic_devicelist_z;
		}
		
		return -1;
	}
	
	public ArrayList<AlertModel> getAlertList() {
		ArrayList<AlertModel> alerts = new ArrayList<AlertModel>();

		for (SmartphoneModel smart : smartphones) {
			try {
				alerts.addAll(SmartphoneModel.getUserAlertList(smart));
			} 
			catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return alerts;
	}
	
	public String getLastAlert(int pos) {
		String alertMessage = "";		
		
		try {
			ArrayList<AlertModel> alerts = SmartphoneModel.getUserAlertList(smartphones.get(pos));
			
			if (alerts.size() > 0) {
				alertMessage = alerts.get(alerts.size() - 1).getMessage();
			}
		} 
		catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return alertMessage;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = context.getLayoutInflater();
		View rowView = inflater.inflate(R.layout.smartphone_list_row, null, true);
		
		SmartphoneModel smartphone = smartphones.get(position);
		
		ImageView smartphoneLetterImageView = (ImageView) rowView.findViewById(R.id.smartphoneLetter);
		TextView smartphoneNameTextView = (TextView) rowView.findViewById(R.id.smartphoneName);
		ImageView deviceTypeImageView = (ImageView) rowView.findViewById(R.id.deviceType);
		TextView newAlertsTextView = (TextView) rowView.findViewById(R.id.newAlerts);
		TextView lastAlertTextView = (TextView) rowView.findViewById(R.id.lastAlert);
		
		smartphoneLetterImageView.setImageResource(setDeviceLetters(position));
		smartphoneNameTextView.setText(smartphone.getName());
		
		newAlertsTextView.setText(Integer.toString(smartphone.getAlerts().size()));
		lastAlertTextView.setText(getLastAlert(position));
		
//		IPHONE(1, "iPhone"),
//		ANDROID(2, "Android"),
//		BLACKBERRY(3, "Blackberry"),
//		WINDOWS(4, "Windows");
		
		int deviceType = smartphone.getDevice().getType(); 
				 
		if (deviceType == 1) { //Apple
			deviceTypeImageView.setImageResource(R.drawable.ic_devicetype_iphone);
		}
		else if (deviceType == 2) { //Android
			deviceTypeImageView.setImageResource(R.drawable.ic_devicetype_android);
		}
		else if (deviceType == 3) { //Blackberry
			deviceTypeImageView.setImageResource(R.drawable.ic_devicetype_blackberry);
		}
		else if (deviceType == 4) { //Windows
			deviceTypeImageView.setImageResource(R.drawable.ic_devicetype_win);
		}

		return rowView;
	}
}
