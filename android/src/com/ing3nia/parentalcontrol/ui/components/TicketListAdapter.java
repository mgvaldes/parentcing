package com.ing3nia.parentalcontrol.ui.components;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ing3nia.parentalcontrol.R;
import com.ing3nia.parentalcontrol.models.TicketModel;

public class TicketListAdapter extends ArrayAdapter<TicketModel> {
	
	private final Activity context;
	private ArrayList<TicketModel> tickets;
	
	public TicketListAdapter(Activity context, ArrayList<TicketModel> tickets) {
		super(context, R.layout.tickets_list_row, tickets);
		this.context = context;
		this.tickets = tickets;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = context.getLayoutInflater();
		View rowView = inflater.inflate(R.layout.tickets_list_row, null, true);
		
		TextView subjectTextView = (TextView) rowView.findViewById(R.id.category);
		TextView messageTextView = (TextView) rowView.findViewById(R.id.subject);
		TextView dateTextView = (TextView) rowView.findViewById(R.id.date);
		
		TicketModel ticket = this.tickets.get(position);
		
		subjectTextView.setText(ticket.getCategory());
		messageTextView.setText(ticket.getSubject());
		dateTextView.setText(ticket.getDate());

		return rowView;
	}
}
