package com.ing3nia.parentalcontrol.client.views.classnames;

public enum CenterMenuOptionsClassNames {
	
	AddUser("centerMenuButton","Add User"),
	UserList("centerMenuButton","User List"),
	DailyRoute("centerMenuButton","Daily Route"),
	AlertList("centerMenuButton","Alert List"),
	AlertRules("centerMenuButton","Alert Rules"),
	DeviceContacts("centerMenuButton","Device Contacts"),
	DeviceSettings("centerMenuButton","Device Settings"),
	DashboardAlertList("centerMenuButton","Alerts List"),
	DashboardDeviceMap("centerMenuButton","Device Map"),
	TicketList("centerMenuButton","Tickets"),
	OpenTickets("centerMenuButton","Open Tickets"),
	ClosedTickets("centerMenuButton","Closed Tickets"),
	NewTicket("centerMenuButton","New Ticket"),
	TicketDetails("centerMenuButton","Edit");
	
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
