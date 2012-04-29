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
import com.ing3nia.parentalcontrol.models.RuleModel;

public class RuleListAdapter extends ArrayAdapter<RuleModel> {
	private final Activity context;
	private ArrayList<RuleModel> rules;

	public RuleListAdapter(Activity context, ArrayList<RuleModel> rules) {
		super(context, R.layout.rule_list_row, rules);
		this.context = context;
		this.rules = rules;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = context.getLayoutInflater();
		View rowView = inflater.inflate(R.layout.rule_list_row, null, true);
		
		TextView ruleNameTextView = (TextView) rowView.findViewById(R.id.ruleName);
		TextView startDateTextView = (TextView) rowView.findViewById(R.id.startDate);
		TextView endDateTextView = (TextView) rowView.findViewById(R.id.endDate);
		ImageView ruleStatusImageView = (ImageView) rowView.findViewById(R.id.ruleStatus);
		
		RuleModel rule = rules.get(position);
		
		ruleNameTextView.setText(rule.getName());
		startDateTextView.setText(rule.getStartDate());
		endDateTextView.setText(rule.getEndDate());
		ruleStatusImageView.setImageResource(R.drawable.ic_rule_allow);

//		if (ruleStatus[position]) {
//			ruleStatusImageView.setImageResource(R.drawable.ic_rule_allow);
//		}
//		else {
//			ruleStatusImageView.setImageResource(R.drawable.ic_rule_disable);
//		}
		
		return rowView;
	}
}
