package com.ing3nia.parentalcontrol.client.views.classnames;

public enum RuleListViewClassName {
	RuleTable("ruleTable"),
	RuleTableRow("ruleTableRow");
	
	private String classname;
	
	private RuleListViewClassName(String classname) {
		this.classname = classname;
	}
	
	public String getClassname(){
		return this.classname;
	}
}
