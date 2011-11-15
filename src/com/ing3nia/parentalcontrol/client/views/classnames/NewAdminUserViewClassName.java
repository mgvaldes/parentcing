package com.ing3nia.parentalcontrol.client.views.classnames;

public enum NewAdminUserViewClassName {
	
		NewAdminUserBlock("newAdminForm");
		
		private String classname;
		private NewAdminUserViewClassName(String classname) {
			this.classname = classname;
		}
		
		public String getClassname(){
			return this.classname;
		}
		
	}
