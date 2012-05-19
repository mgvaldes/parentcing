package com.ing3nia.parentalcontrol.ui.components;

import java.util.ArrayList;

import com.ing3nia.parentalcontrol.R;
import com.ing3nia.parentalcontrol.models.ContactModel;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ContactListAdapter extends ArrayAdapter<ContactModel> {
	
	private final Activity context;
	private boolean[] contactStatus;
	private ArrayList<ContactModel> contacts;
	
	public ContactListAdapter(Activity context, ArrayList<ContactModel> contacts, boolean[] contactStatus) {
		super(context, R.layout.contact_list_row, contacts);
		this.context = context;
		this.contacts = contacts;
		this.contactStatus = contactStatus;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = context.getLayoutInflater();
		View rowView = inflater.inflate(R.layout.contact_list_row, null, true);
		
		TextView contactNameTextView = (TextView) rowView.findViewById(R.id.contactName);
		TextView phoneNumberTextView = (TextView) rowView.findViewById(R.id.phoneNumber);
		ImageView contactStatusImageView = (ImageView) rowView.findViewById(R.id.contactStatus);
		
		ContactModel contact = this.contacts.get(position);
		
		contactNameTextView.setText(contact.getFirstName() + " " + contact.getLastName());
		phoneNumberTextView.setText(contact.getPhones().get(0).getPhoneNumber());

		if (contactStatus[position]) {
			contactStatusImageView.setImageResource(R.drawable.ic_contact_allow);
		}
		else {
			contactStatusImageView.setImageResource(R.drawable.ic_contact_disallow);
		}
		
		return rowView;
	}
}
