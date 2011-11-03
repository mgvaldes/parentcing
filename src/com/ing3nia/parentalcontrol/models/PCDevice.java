package com.ing3nia.parentalcontrol.models;

import javax.jdo.annotations.Embedded;
import javax.jdo.annotations.EmbeddedOnly;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

/**
 * This class contains information about a specific device.  
 * 
 * @author Ing3nia
 * @author Javier Fernandez
 * @author Maria Gabriela Valdes
 * 
 * @version 1.0
 *
 */
@PersistenceCapable
public class PCDevice {
	/**
	 * Unique key that identifies the device.
	 */
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;
	
	/**
	 * Specifies the model of the device.
	 */
	@Persistent
	private String model;
	
	/**
	 * Specifies the version of the OS installed in the device.
	 */
	@Persistent
	private String version;
	
	/**
	 * Represents and OS type, one of: Android, Blackberry, iOS or Windows.
	 */
	@PersistenceCapable
    @EmbeddedOnly
    public static class PCOs {
		/**
		 * 
		 */
        @Persistent
        private String osType;

        /**
		 * 
		 */
        @Persistent
        private int id;

		public String getOsType() {
			return osType;
		}

		public void setOsType(String osType) {
			this.osType = osType;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}
    }

	/**
	 * Represents the OS running in the device.
	 */
    @Persistent
    @Embedded
    private PCOs os;
	
	public PCDevice() {
		super();
	}

	public PCDevice(Key key, String model, String version, PCOs os) {
		super();
		this.key = key;
		this.model = model;
		this.version = version;
		this.os = os;
	}

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public PCOs getOs() {
		return os;
	}

	public void setOs(PCOs os) {
		this.os = os;
	}
}
