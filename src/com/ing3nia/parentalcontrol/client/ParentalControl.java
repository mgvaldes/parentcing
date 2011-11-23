package com.ing3nia.parentalcontrol.client;

import com.ing3nia.parentalcontrol.client.handlers.BaseViewHandler;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;
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

		BaseViewHandler baseViewHandler = new BaseViewHandler(pcbase);
		baseViewHandler.setAddAdminButtonHandler();
				
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
