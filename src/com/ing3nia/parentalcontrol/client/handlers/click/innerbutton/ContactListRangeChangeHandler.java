package com.ing3nia.parentalcontrol.client.handlers.click.innerbutton;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.Window;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.RangeChangeEvent;
import com.ing3nia.parentalcontrol.client.models.ClientSimpleContactModel;



public class ContactListRangeChangeHandler implements RangeChangeEvent.Handler{

	CellTable cellTable;
	ArrayList<Boolean> activeInactiveIndexList;
	
	static final String transpActiveButtonString = "<button type=\"button\" class=\"allowButton\" style=\"opacity:0.5;\" "
		+ "tabindex=\"-1\">";
	static final String transpActiveButtonFullString= transpActiveButtonString +"Allow"+"</button>";

	static final String transpInactiveButtonString = "<button type=\"button\" class=\"disallowButton\" style=\"opacity:0.5;\" "
		+ "tabindex=\"-1\">";
	static final String transpInactiveButtonFullString= transpInactiveButtonString +"Disallow"+"</button>";
	

	public ContactListRangeChangeHandler(CellTable cellTable, ArrayList<Boolean> activeInactiveIndexList){
		this.cellTable = cellTable;
		this.activeInactiveIndexList = activeInactiveIndexList;
	}
	
	@Override
	public void onRangeChange(RangeChangeEvent event) {
		Range range = cellTable.getVisibleRange();
	    int start = range.getStart();
	    int length = range.getLength();
	    
	    String acum = "";
	    for(int i=0; i<length && (start+i)<activeInactiveIndexList.size(); i++){
	    	int index = start+i;
	    	if(activeInactiveIndexList.get(index)){
				cellTable.getRowElement(i).getCells().getItem(3).getFirstChildElement().setInnerHTML(transpInactiveButtonFullString);				
	    	}else{
				cellTable.getRowElement(i).getCells().getItem(4).getFirstChildElement().setInnerHTML(transpActiveButtonFullString);				
	    	}
	    	
	    	//acum+=activeInactiveIndexList.get(index).toString()+" ";
	    }

	        
	    /*
	    List<ForumMessage> toSet = new ArrayList<ForumMessage>(length);
	    for (int i = start; i < start + length && i < AllMessages.size(); i++)
	        toSet.add((ForumMessage) AllMessages.get(i));
	    cellTable.setRowData(start, toSet);*/
	}
	/*
	
	@Override
	public void onRangeChange(RangeChangeEvent event)
	{
	    Range range = cellTable.getVisibleRange();
	    int start = range.getStart();
	    int length = range.getLength();
	    List<ForumMessage> toSet = new ArrayList<ForumMessage>(length);
	    for (int i = start; i < start + length && i < AllMessages.size(); i++)
	        toSet.add((ForumMessage) AllMessages.get(i));
	    cellTable.setRowData(start, toSet);
	}*/
}
