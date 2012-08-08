package com.ing3nia.parentalcontrol.client.handlers.click;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.datepicker.client.DateBox;

public class OnIndefiniteRuleClickHandler implements ClickHandler{

	DateBox fromDate;
	DateBox toDate;
	TextBox tb11;
	TextBox tb12;
	TextBox tb13;
	TextBox tb21;
	TextBox tb22;
	TextBox tb23;
	String enabled;
	
	public OnIndefiniteRuleClickHandler(
			DateBox fromDate,
			DateBox toDate,
			TextBox tb11,
			TextBox tb12,
			TextBox tb13,
			TextBox tb21,
			TextBox tb22,
			TextBox tb23){
		this.fromDate = fromDate;
		this.toDate = toDate;
		this.tb11 = tb11;
		this.tb12 = tb12;
		this.tb13 = tb13;
		this.tb21 = tb21;
		this.tb22 = tb22;
		this.tb23 = tb23;
		this.enabled = "true";
		
	}
	
	@Override
	public void onClick(ClickEvent event) {
		if(this.enabled.equals("true")){
			this.fromDate.setEnabled(false);
			this.fromDate.setStyleName("disabled", true);
			this.toDate.setEnabled(false);
			this.toDate.setStyleName("disabled", true);
			this.tb11.setEnabled(false);
			this.tb11.setStyleName("disabled", true);
			this.tb12.setEnabled(false);
			this.tb12.setStyleName("disabled", true);
			this.tb13.setEnabled(false);
			this.tb13.setStyleName("disabled", true);
			this.tb21.setEnabled(false);
			this.tb21.setStyleName("disabled", true);
			this.tb22.setEnabled(false);
			this.tb22.setStyleName("disabled", true);
			this.tb23.setEnabled(false);
			this.tb23.setStyleName("disabled", true);
			this.enabled = "false";
		}else{
			this.fromDate.setEnabled(true);
			this.fromDate.setStyleName("disabled", false);
			this.toDate.setEnabled(true);
			this.toDate.setStyleName("disabled", false);
			this.tb11.setEnabled(true);
			this.tb11.setStyleName("disabled", false);
			this.tb12.setEnabled(true);
			this.tb12.setStyleName("disabled", false);
			this.tb13.setEnabled(true);
			this.tb13.setStyleName("disabled", false);
			this.tb21.setEnabled(true);
			this.tb21.setStyleName("disabled", false);
			this.tb22.setEnabled(true);
			this.tb22.setStyleName("disabled", false);
			this.tb23.setEnabled(true);
			this.tb23.setStyleName("disabled", false);
			this.enabled = "true";
		}
		
	}
	
}
