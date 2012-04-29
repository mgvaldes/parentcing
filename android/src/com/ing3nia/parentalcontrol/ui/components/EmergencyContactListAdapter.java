package com.ing3nia.parentalcontrol.ui.components;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ing3nia.parentalcontrol.R;
import com.ing3nia.parentalcontrol.models.EmergencyNumberModel;

public class EmergencyContactListAdapter extends ArrayAdapter<EmergencyNumberModel> {
	
	private final Activity context;
	private boolean[] contactStatus;
	private ArrayList<EmergencyNumberModel> emergencyContacts;
	
	public EmergencyContactListAdapter(Activity context, ArrayList<EmergencyNumberModel> emergencyContacts, boolean[] contactStatus) {
		super(context, R.layout.contact_list_row, emergencyContacts);
		this.context = context;
		this.emergencyContacts = emergencyContacts;
		this.contactStatus = contactStatus;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = context.getLayoutInflater();
		View rowView = inflater.inflate(R.layout.contact_list_row, null, true);
		
		TextView contactNameTextView = (TextView) rowView.findViewById(R.id.contactName);
		TextView phoneNumberTextView = (TextView) rowView.findViewById(R.id.phoneNumber);
		ImageView contactStatusImageView = (ImageView) rowView.findViewById(R.id.contactStatus);
		
		EmergencyNumberModel emergencyNumber = this.emergencyContacts.get(position);
		
		contactNameTextView.setText(emergencyNumber.getDescription());
		phoneNumberTextView.setText(emergencyNumber.getNumber());

		if (contactStatus[position]) {
			contactStatusImageView.setImageResource(R.drawable.ic_contact_allow);
		}
		else  {
			contactStatusImageView.setImageResource(R.drawable.ic_contact_disallow);
		}
		
		return rowView;
	}
}
