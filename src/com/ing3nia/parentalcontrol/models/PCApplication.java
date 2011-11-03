package com.ing3nia.parentalcontrol.models;

import java.util.ArrayList;
import java.util.Date;

import javax.jdo.annotations.Embedded;
import javax.jdo.annotations.EmbeddedOnly;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

/**
 * This class contains information about an application installed in a device.  
 * 
 * @author Ing3nia
 * @author Javier Fernandez
 * @author Maria Gabriela Valdes
 * 
 * @version 1.0
 *
 */
@PersistenceCapable
public class PCApplication {
	/**
	 * Unique key that identifies the application.
	 */
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;
	
	/**
	 * Represents the device where the application is installed.
	 */
	@Persistent
	private PCDevice device;
	
	/**
	 * This class contains information about the version and download url 
	 * of an application.
	 */
	@PersistenceCapable
    @EmbeddedOnly
    public static class PCAppInfo {
		/**
		 * Specifies the version of an application
		 */
        @Persistent
        private String appVersion;

        /**
		 * Specifies the download url where the version of an application can
		 * be found.
		 */
        @Persistent
        private String appUrl;
        
        /**
         * Specifies the date when the app was published.
         */
        @Persistent
        private Date publishDate;
        
		public String getAppVersion() {
			return appVersion;
		}

		public void setAppVersion(String appVersion) {
			this.appVersion = appVersion;
		}

		public String getAppUrl() {
			return appUrl;
		}

		public void setAppUrl(String appUrl) {
			this.appUrl = appUrl;
		}

		public Date getPublishDate() {
			return publishDate;
		}

		public void setPublishDate(Date publishDate) {
			this.publishDate = publishDate;
		}
    }

	/**
	 * Represents the version and download url of a specific application.
	 */
    @Persistent
    @Embedded
    private PCAppInfo appInfo;
    
    /**
     * Represent a collection of functionalities supported and available
     * in an application installed in a specific device.
     */
    @Persistent
    private ArrayList<PCFunctionality> availableFunctionalities;

	public PCApplication() {
		super();
	}

	public PCApplication(Key key, PCDevice device, PCAppInfo appInfo,
			ArrayList<PCFunctionality> availableFunctionalities) {
		super();
		this.key = key;
		this.device = device;
		this.appInfo = appInfo;
		this.availableFunctionalities = availableFunctionalities;
	}

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	public PCDevice getDevice() {
		return device;
	}

	public void setDevice(PCDevice device) {
		this.device = device;
	}

	public PCAppInfo getAppInfo() {
		return appInfo;
	}

	public void setAppInfo(PCAppInfo appInfo) {
		this.appInfo = appInfo;
	}

	public ArrayList<PCFunctionality> getAvailableFunctionalities() {
		return availableFunctionalities;
	}

	public void setAvailableFunctionalities(
			ArrayList<PCFunctionality> availableFunctionalities) {
		this.availableFunctionalities = availableFunctionalities;
	}
}
