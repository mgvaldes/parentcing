package com.ing3nia.parentalcontrol.client.handlers.click;

import java.util.ArrayList;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlexTable;

public class EnableFunctionalityClickHandler implements ClickHandler {
	
	private String desc;
	private ArrayList<String> disabledFunctionalities;
	private FlexTable disabledFunctionalitiesTable;
	
	public EnableFunctionalityClickHandler(String desc, ArrayList<String> disabledFunctionalities, FlexTable disabledFunctionalitiesTable) {
		this.desc = desc;
		this.disabledFunctionalities = disabledFunctionalities;
		this.disabledFunctionalitiesTable = disabledFunctionalitiesTable;
	}
	
	@Override
	public void onClick(ClickEvent event) {
		int removedIndex = this.disabledFunctionalities.indexOf(this.desc);
		this.disabledFunctionalities.remove(removedIndex);
		this.disabledFunctionalitiesTable.removeRow(removedIndex);
	}
}