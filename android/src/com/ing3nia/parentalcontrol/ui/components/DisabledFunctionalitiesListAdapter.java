package com.ing3nia.parentalcontrol.ui.components;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ing3nia.parentalcontrol.R;

public class DisabledFunctionalitiesListAdapter extends ArrayAdapter<String> {
	
	private final Activity context;
	private final String[] disabledFuncNames;
	private boolean[] disabledFuncStatus;
	
	public DisabledFunctionalitiesListAdapter(Activity context, String[] disabledFuncNames, boolean[] disabledFuncStatus) {
		super(context, R.layout.disabled_functionalities_list_row, disabledFuncNames);
		this.context = context;
		this.disabledFuncNames = disabledFuncNames;
		this.disabledFuncStatus = disabledFuncStatus;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = context.getLayoutInflater();
		View rowView = inflater.inflate(R.layout.disabled_functionalities_list_row, null, true);
		
		TextView disabledFuncNameTextView = (TextView) rowView.findViewById(R.id.disabledFuncName);
		ImageView disabledFuncStatusImageView = (ImageView) rowView.findViewById(R.id.disabledFuncStatus);
		
		disabledFuncNameTextView.setText(disabledFuncNames[position]);

		if (disabledFuncStatus[position]) {
			disabledFuncStatusImageView.setImageResource(R.drawable.ic_rule_allow);
		}
		else {
			disabledFuncStatusImageView.setImageResource(R.drawable.ic_rule_disable);
		}
		
		return rowView;
	}
}
