package com.ing3nia.parentalcontrol.ui.components;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ing3nia.parentalcontrol.R;

public class AdminUserListAdapter extends ArrayAdapter<String> {
	
	private final Activity context;
	private final String[] users;
	
	public AdminUserListAdapter(Activity context, String[] users) {
		super(context, R.layout.contact_list_row, users);
		this.context = context;
		this.users = users;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = context.getLayoutInflater();
		View rowView = inflater.inflate(R.layout.user_list_row, null, true);
		
		TextView contactNameTextView = (TextView) rowView.findViewById(R.id.contactName);
		
		contactNameTextView.setText(users[position]);
		
		return rowView;
	}
}