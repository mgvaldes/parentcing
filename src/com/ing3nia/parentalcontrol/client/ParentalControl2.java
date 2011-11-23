package com.ing3nia.parentalcontrol.client;

import java.util.ArrayList;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class ParentalControl2 implements EntryPoint {
	private VerticalPanel mainPanel = new VerticalPanel();
	private FlexTable stocksFlexTable = new FlexTable();
	private HorizontalPanel addPanel = new HorizontalPanel();
	private TextBox newSymbolTextBox = new TextBox();
	private Button addStockButton = new Button("Add");
	private Label lastUpdatedLabel = new Label();
	private ArrayList<String> stocks = new ArrayList<String>();
	private static final int REFRESH_INTERVAL = 5000;

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
		
		// TODO Create table for stock data.
		// Create table for stock data.
		stocksFlexTable.setText(0, 0, "Symbol");
		stocksFlexTable.setText(0, 1, "Price");
		stocksFlexTable.setText(0, 2, "Change");
		stocksFlexTable.setText(0, 3, "Remove");

		// Assemble add stock panel
		addPanel.add(newSymbolTextBox);
		addPanel.add(addStockButton);

		// Assemble Main panel.
		mainPanel.add(stocksFlexTable);
		mainPanel.add(addPanel);
		mainPanel.add(lastUpdatedLabel);

		// Associate the Main panel with the HTML host page.
		RootPanel rp = RootPanel.get("stockList");
		rp.add(mainPanel);

		// Move cursor focus to the input box.
		newSymbolTextBox.setFocus(true);

		// Listen for mouse events on the Add button.
		addStockButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				addStock();
			}
		});

		newSymbolTextBox.addKeyPressHandler(new KeyPressHandler() {

			@Override
			public void onKeyPress(KeyPressEvent event) {
				if (event.getCharCode() == KeyCodes.KEY_ENTER) {
			          addStock();
			        }
			};
		});
	
	    
	    Timer refreshTimer = new Timer() {
	      @Override
	      public void run() {
	        refreshWatchList();
	      }
	    };
	    refreshTimer.scheduleRepeating(REFRESH_INTERVAL);
	}

	/**
	 * Add stock to FlexTable. Executed when the user clicks the addStockButton
	 * or presses enter in the newSymbolTextBox.
	 */

	
	private void addStock() {
		final String symbol = newSymbolTextBox.getText().toUpperCase().trim(); 
		
		// Don't add the stock if it's already in the table.
	    if (stocks.contains(symbol))
	      return;
	    
	    int row = stocksFlexTable.getRowCount();
	    stocks.add(symbol);
	    stocksFlexTable.setText(row, 0, symbol);
	    
	    Button removeStockButton = new Button("x");
	    removeStockButton.addClickHandler(new ClickHandler() {
	      public void onClick(ClickEvent event) {
	        int removedIndex = stocks.indexOf(symbol);
	        stocks.remove(removedIndex);        
	        stocksFlexTable.removeRow(removedIndex + 1);
	      }
	    });
	    stocksFlexTable.setWidget(row, 3, removeStockButton);
	    refreshWatchList();
	    
	    
	}

	  private void refreshWatchList() {
		    final double MAX_PRICE = 100.0; // $100.00
		    final double MAX_PRICE_CHANGE = 0.02; // +/- 2%

		    StockPrice[] prices = new StockPrice[stocks.size()];
		    for (int i = 0; i < stocks.size(); i++) {
		      double price = Random.nextDouble() * MAX_PRICE;
		      double change = price * MAX_PRICE_CHANGE
		          * (Random.nextDouble() * 2.0 - 1.0);

		      prices[i] = new StockPrice(stocks.get(i), price, change);
		    }

		    updateTable(prices);

		  }
	  
	  private void updateTable(StockPrice[] prices) {
		  for(int i=0; i<prices.length; i++){
			  updateTable(prices[i]);
		  }
	  }
	  
	  /**
	   * Update a single row in the stock table.
	   *
	   * @param price Stock data for a single row.
	   */
	
	  private void updateTable(StockPrice price) {
	    // Make sure the stock is still in the stock table.
	    if (!stocks.contains(price.getSymbol())) {
	      return;
	    }

	    int row = stocks.indexOf(price.getSymbol()) + 1;

	    // Format the data in the Price and Change fields.
	    String priceText = NumberFormat.getFormat("#,##0.00").format(
	        price.getPrice());
	    NumberFormat changeFormat = NumberFormat.getFormat("+#,##0.00;-#,##0.00");
	    String changeText = changeFormat.format(price.getChange());
	    String changePercentText = changeFormat.format(price.getChangePercent());

	    // Populate the Price and Change fields with new data.
	    stocksFlexTable.setText(row, 1, priceText);
	    stocksFlexTable.setText(row, 2, changeText + " (" + changePercentText
	        + "%)");
	  }

}
