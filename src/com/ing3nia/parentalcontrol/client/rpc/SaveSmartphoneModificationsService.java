package com.ing3nia.parentalcontrol.client.rpc;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.ing3nia.parentalcontrol.services.models.ModificationModel;

@RemoteServiceRelativePath("save-sph-mods")
public interface SaveSmartphoneModificationsService extends RemoteService {
	public Boolean saveSmartphoneModifications(String cid, String smpid, ModificationModel modifications) throws IllegalArgumentException;
}
