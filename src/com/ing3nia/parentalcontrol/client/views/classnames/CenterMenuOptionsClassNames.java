package com.ing3nia.parentalcontrol.client.views.classnames;

public enum CenterMenuOptionsClassNames {
	
	AddUser("centerMenuButton","Add User"),
	UserList("centerMenuButton","User List");
	
	private String classname;
	private String text;
	private CenterMenuOptionsClassNames(String classname,String text) {
		this.classname = classname;
		this.text = text;
	}
	
	public String getClassname(){
		return this.classname;
	}
	
	public String getText(){
		return this.text;
	}
}
