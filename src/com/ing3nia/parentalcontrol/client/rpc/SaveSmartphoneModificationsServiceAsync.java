package com.ing3nia.parentalcontrol.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.ing3nia.parentalcontrol.client.models.ModificationModel;

public interface SaveSmartphoneModificationsServiceAsync {
	public void saveSmartphoneModifications(String cid, String smpid, ModificationModel modifications, AsyncCallback<Boolean> async);
}
