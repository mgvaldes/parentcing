package com.ing3nia.parentalcontrol.client.views.classnames;

public enum AlertListViewClassName {
	AlertTable("alertTable"),
	AlertTableRow("alertTableRow");
	
	private String classname;
	
	private AlertListViewClassName(String classname) {
		this.classname = classname;
	}
	
	public String getClassname(){
		return this.classname;
	}
}
