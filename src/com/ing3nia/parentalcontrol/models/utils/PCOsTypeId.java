package com.ing3nia.parentalcontrol.models.utils;

public enum PCOsTypeId {
	
	IPHONE(1, "iPhone"),
	ANDROID(2, "Android"),
	BLACKBERRY(3, "Blackberry"),
	WINDOWS(4, "Windows");
	
	private int id;
	
	private String osType;
	
	PCOsTypeId(int id, String osType) {
		this.id = id;
		this.osType = osType;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getOsType() {
		return osType;
	}

	public void setOsType(String osType) {
		this.osType = osType;
	}
	
	public static String getOsTypeFromId(int id) {
		PCOsTypeId[] values = PCOsTypeId.values();
		
		for (PCOsTypeId os : values) {
			if (os.getId() == id) {
				return os.getOsType();
			}
		}
		
		return "";
	}
	
	public static int getOsIdFromType(String type) {
		PCOsTypeId[] values = PCOsTypeId.values();
		
		for (PCOsTypeId os : values) {
			if (os.getOsType().equals(type)) {
				return os.getId();
			}
		}
		
		return -1;
	}
}
