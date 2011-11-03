package com.ing3nia.parentalcontrol.models.utils;

public enum WSInfo {
	SPH_REG_POST("01", "sph-reg"),
	APP_ID_REQ_POST("02", "app-id-req"),
	TOTAL_SYNC_GET("03", "total-sync"),
	TOTAL_SYNC_POST("04", "total-sync"),
	UPD_SYNC_GET("05", "upd-sync"),
	UPD_SYNC_POST("06", "upd-sync"),
	INT_MOD_POST("07", "int-mod"),
	ALERT_POST("08", "alert"),
	ACTIVITY_NOTIFY_POST("09", "activity-notify");
	
	/**
	 * Specifies a unique code associated to a web service.
	 */
	private String code;
	
	/**
	 * Specifies the URL of a specific web service.
	 */
	private String uri;
	
	WSInfo(String code, String uri) {
		this.code = code;
		this.uri = uri;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}
}
