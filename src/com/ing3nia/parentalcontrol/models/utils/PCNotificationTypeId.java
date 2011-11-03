package com.ing3nia.parentalcontrol.models.utils;

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
	LOW_BATTERY_ID(1, "Bater’a de dispositivo baja"),
	DEAD_BATTERY_ID(2, "Bater’a de dispositivo muerta"),
	UNREACHABLE_PHONE_ID(3, "Dispositivo inalcanzable"),
	SPEED_LIMIT_OVERSTEP_ID(4, "Se ha sobrepasado el l’mite de velocidad permitido");
	
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
