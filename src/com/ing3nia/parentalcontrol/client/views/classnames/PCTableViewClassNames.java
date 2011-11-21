package com.ing3nia.parentalcontrol.client.views.classnames;

public enum PCTableViewClassNames {

	EXTENDED_TABLE("extendedTable");
	
	private String classname;
	private PCTableViewClassNames(String classname) {
		this.classname = classname;
	}
	
	public String getClassname(){
		return this.classname;
	}
	
}
