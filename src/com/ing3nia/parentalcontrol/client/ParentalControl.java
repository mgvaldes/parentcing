package com.ing3nia.parentalcontrol.client;

import java.util.ArrayList;

import com.ibm.icu.impl.CalendarAstronomer.Horizon;
import com.ing3nia.parentalcontrol.client.panels.PCDockLayoutPanel;
import com.ing3nia.parentalcontrol.shared.FieldVerifier;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class ParentalControl implements EntryPoint {
	
	private VerticalPanel mainPanel = new VerticalPanel();
	private HorizontalPanel headerPanel = new HorizontalPanel();
	private HorizontalPanel bodyPanel = new HorizontalPanel();
	private VerticalPanel leftMenuPanel = new VerticalPanel();
	private VerticalPanel contentPanel = new VerticalPanel();
	private HorizontalPanel footerPanel = new HorizontalPanel();
	/*
	private FlexTable stocksFlexTable = new FlexTable();
	private HorizontalPanel addPanel = new HorizontalPanel();
	private TextBox newSymbolTextBox = new TextBox();
	private Button addStockButton = new Button("Add");
	private Label lastUpdatedLabel = new Label();
	private ArrayList<String> stocks = new ArrayList<String>();
	private static final int REFRESH_INTERVAL = 5000;
*/
	/*
	public void onModuleLoad(){

		RootLayoutPanel rp = RootLayoutPanel.get();
		rp.add(new PCDockLayoutPanel());	
	}
	*/
	
	/**
	 * Entry point method.
	 */
	public void onModuleLoad() {
		
		PCBaseUIBinder pcbase = new PCBaseUIBinder();
		RootPanel.get().add(pcbase);
		
		int childCount = pcbase.deviceChoiceList.getWidgetCount();
		
		
		/*
		// Adding style to panels
		headerPanel.addStyleName("headerBlock");
		leftMenuPanel.addStyleName("leftMenuBlock");
		contentPanel.addStyleName("contentBlock");
		bodyPanel.addStyleName("bodyBlock");
		footerPanel.addStyleName("footerBlock");
		mainPanel.addStyleName("mainBlock");
		mainPanel.addStyleDependentName("mainPage");
		
		//Associate internal panels to main panel
		mainPanel.add(headerPanel);
		
		bodyPanel.add(leftMenuPanel);
		bodyPanel.add(contentPanel);
		mainPanel.add(bodyPanel);
		
		mainPanel.add(footerPanel);
		
		// Associate the Main panel with the HTML host page.
		RootPanel rp = RootPanel.get("stockList");
		rp.add(mainPanel);
		*/
	}

}
