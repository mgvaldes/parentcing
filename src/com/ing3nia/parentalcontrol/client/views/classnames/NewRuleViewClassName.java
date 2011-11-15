package com.ing3nia.parentalcontrol.client.views.classnames;

public enum NewRuleViewClassName {
	DisabledFunctionalitiesTable("disabledFunctionalitiesTable"),
	DisabledFunctionalitiesTableRow("disabledFunctionalitiesTableRow"),
	NewRulePanel("newRulePanel");
	
	private String classname;
	
	private NewRuleViewClassName(String classname) {
		this.classname = classname;
	}
	
	public String getClassname(){
		return this.classname;
	}
}
