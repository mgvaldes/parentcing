package com.ing3nia.parentalcontrol.models;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

/**
 * The class contains information about a category used to classify 
 * tickets created by a user.
 * 
 * @author Ing3nia
 * @author Javier Fernandez
 * @author Maria Gabriela Valdes
 * 
 * @version 1.0
 *
 */
@PersistenceCapable
public class PCCategory {
	/**
	 * Unique key that identifies the category of a ticket.
	 */
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;
	
	/**
	 * Unique identifier to access a category faster. 
	 */
	@Persistent
	private int id;
	
	/**
	 * Specifies the purpose/concept of a category.
	 */
	@Persistent
	private String description;

	public PCCategory() {
		super();
	}

	public PCCategory(Key key, int id, String description) {
		super();
		this.key = key;
		this.id = id;
		this.description = description;
	}

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
