package com.ing3nia.parentalcontrol.client.models;

/**
 *  
 * 
 * @author Ing3nia
 * @author Javier Fernandez
 * @author Maria Gabriela Valdes
 * 
 * @version 1.0
 *
 */
public enum PCNotificationTypeId {
	LOW_BATTERY_ID(1, "Device Battery Low"),
	DEAD_BATTERY_ID(2, "Device Battery Dead"),
	UNREACHABLE_PHONE_ID(3, "Unreacheable Device"),
	SPEED_LIMIT_OVERSTEP_ID(4, "Speed Limit Overstepped");
	
	private int type;
	
	private String message;
	
	PCNotificationTypeId(int type, String message) {
		this.type = type;
		this.message = message;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public static String getNotificationMessageFromType(int type) {
		PCNotificationTypeId[] values = PCNotificationTypeId.values();
		
		for (PCNotificationTypeId notification : values) {
			if (notification.getType() == type) {
				return notification.getMessage();
			}
		}
		
		return "";
	}
	
	//TODO Agregar el resto de los tipos de notificaciones
}
