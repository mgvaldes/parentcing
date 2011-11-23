package com.ing3nia.parentalcontrol.client.panels;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class PCDockLayoutPanel extends Composite{

	private static PCDockLayoutPanelUiBinder uiBinder = GWT
			.create(PCDockLayoutPanelUiBinder.class);

	interface PCDockLayoutPanelUiBinder extends
			UiBinder<Widget, PCDockLayoutPanel> {
	}

	public PCDockLayoutPanel() {
		initWidget(uiBinder.createAndBindUi(this));
	}

}
