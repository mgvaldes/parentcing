package com.ing3nia.parentalcontrol.client.views.classnames;

public enum AdminUserListViewClassName {
	AdminUserTable("adminUserTable"),
	AdminUserTableRow("adminUserTableRow");
	
	private String classname;
	private AdminUserListViewClassName(String classname) {
		this.classname = classname;
	}
	
	public String getClassname(){
		return this.classname;
	}
	
}
