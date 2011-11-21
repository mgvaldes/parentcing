package com.ing3nia.parentalcontrol.services.models;

import com.ing3nia.parentalcontrol.client.models.ModificationModel;

public class ParentModificationsModel {
	private String cid;
	private String smid;
	private ModificationModel modifications;
	
	public ParentModificationsModel(String cid, String smid, ModificationModel modifications) {
		this.cid = cid;
		this.smid = smid;
		this.modifications = modifications;
	}
	
	public String getCid() {
		return cid;
	}
	public void setCid(String cid) {
		this.cid = cid;
	}
	public String getSmid() {
		return smid;
	}
	public void setSmid(String smid) {
		this.smid = smid;
	}
	public ModificationModel getModifications() {
		return modifications;
	}
	public void setModifications(ModificationModel modifications) {
		this.modifications = modifications;
	}

}
