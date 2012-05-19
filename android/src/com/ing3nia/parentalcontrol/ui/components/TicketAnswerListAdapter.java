package com.ing3nia.parentalcontrol.ui.components;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ing3nia.parentalcontrol.R;
import com.ing3nia.parentalcontrol.models.TicketAnswerModel;

public class TicketAnswerListAdapter extends ArrayAdapter<TicketAnswerModel> {
	
	private final Activity context;
	private ArrayList<TicketAnswerModel> answers;
	
	public TicketAnswerListAdapter(Activity context, ArrayList<TicketAnswerModel> answers) {
		super(context, R.layout.ticket_answers_list_row, answers);
		this.context = context;
		this.answers = answers;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = context.getLayoutInflater();
		View rowView = inflater.inflate(R.layout.ticket_answers_list_row, null, true);
		
		TextView userTextView = (TextView) rowView.findViewById(R.id.user);
		TextView commentTextView = (TextView) rowView.findViewById(R.id.comment);
		TextView dateTextView = (TextView) rowView.findViewById(R.id.date);
		
		TicketAnswerModel answer = this.answers.get(position);
		
		userTextView.setText("PRC " + answer.getUsername() + ":");
		commentTextView.setText(answer.getAnswer());
		dateTextView.setText(answer.getDate());

		return rowView;
	}
}
