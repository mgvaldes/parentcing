package com.ing3nia.parentalcontrol.client.views;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.ing3nia.parentalcontrol.client.handlers.BaseViewHandler;
import com.ing3nia.parentalcontrol.client.models.TicketAnswerModel;
import com.ing3nia.parentalcontrol.client.models.TicketModel;
import com.ing3nia.parentalcontrol.client.rpc.AddTicketService;
import com.ing3nia.parentalcontrol.client.rpc.AddTicketServiceAsync;
import com.ing3nia.parentalcontrol.client.rpc.UserTicketListService;
import com.ing3nia.parentalcontrol.client.rpc.UserTicketListServiceAsync;
import com.ing3nia.parentalcontrol.client.utils.CategoryType;
import com.ing3nia.parentalcontrol.client.views.async.AddTicketCallbackHandler;
import com.ing3nia.parentalcontrol.client.views.async.UserTicketListCallbackHandler;

public class NewTicketView {
	
	public static String VIEW_CONTENT_CLASSNAME = "newTicketContent";
	
	
	/**
	 * Center Panel containing all the widgets of the 
	 * new admin user view.
	 */
	private HTMLPanel centerContent;
	
	/**
	 * Main panel of new admin user view that groups all 
	 * the widgets together.
	 */
	private HTMLPanel viewContent = new HTMLPanel("");
	
	/**
	 * Main label of new ticket view.
	 */
	private Label newTicketLabel = new Label("New Ticket:");
	
	/**
	 * Category label.
	 */
	private Label categoryLabel = new Label("Category:");
	
	/**
	 * Category list box.
	 */
	private ListBox categoryListBox = new ListBox();
	
	/**
	 * Subject label.
	 */
	private Label subjectLabel = new Label("Subject:");
	
	/**
	 * Subject text box.
	 */
	private TextBox subjectTextBox = new TextBox();
	
	/**
	 * Comment label.
	 */
	private Label commentLabel = new Label("Comment:");
	
	/**
	 * Comment text area.
	 */
	private TextArea commentTextArea = new TextArea();
	
	/**
	 * Panel containing view buttons.
	 */
	private FlowPanel buttonPanel = new FlowPanel();
	
	/**
	 * Save button.
	 */
	private Button saveButton = new Button("Save");
	
	/**
	 * Clear button.
	 */
	private Button clearButton = new Button("Clear");

	private String userKey;
	
	private BaseViewHandler baseView;
	
	public NewTicketView(BaseViewHandler baseView) {
		this.baseView = baseView;
		this.centerContent = this.baseView.getBaseBinder().getCenterContent();
		this.centerContent.setStyleName("centerContent");
		this.userKey = this.baseView.getUser().getKey();
		
		centerContent.add(newTicketLabel);
		this.newTicketLabel.setStyleName("sec-title");
		viewContent.add(categoryLabel);
		
		loadCategoryListBox();
		
		viewContent.add(categoryListBox);
		viewContent.add(subjectLabel);
		viewContent.add(subjectTextBox);
		viewContent.add(commentLabel);
		viewContent.add(commentTextArea);
		viewContent.setStyleName(VIEW_CONTENT_CLASSNAME);
		
		DOM.setElementProperty(saveButton.getElement(), "id", "saveNewTicketView");
		saveButton.addClickHandler(new ClickHandler() {
	    	public void onClick(ClickEvent event) {
	    		saveTicket();
	    	}
	    });
		
		buttonPanel.add(saveButton);
		
		DOM.setElementProperty(clearButton.getElement(), "id", "clearNewTicketButton");
		clearButton.addClickHandler(new ClickHandler() {
	    	public void onClick(ClickEvent event) {
	    		clearTextBoxes();
	    	}
	    });
		
		buttonPanel.setStyleName("newTicketButtonsPanel");
		buttonPanel.add(clearButton);
		viewContent.add(buttonPanel);
		
	}
	
	public void loadCategoryListBox() {
		CategoryType[] categories = CategoryType.values();
		
		for (CategoryType c : categories) {
			this.categoryListBox.addItem(c.getDescription());
		}
	}
	
	public void initNewTicketView(){
		centerContent.clear();
		centerContent.add(viewContent);
	}
	
	private void saveTicket() {
		TicketModel ticket = new TicketModel();
		ticket.setAnswers(new ArrayList<TicketAnswerModel>());
		ticket.setCategory(this.categoryListBox.getItemText(this.categoryListBox.getSelectedIndex()));
		ticket.setComment(this.commentTextArea.getText());
		ticket.setDate(new Date());
		ticket.setSubject(this.subjectTextBox.getText());

		AddTicketCallbackHandler addTicketCallback = new AddTicketCallbackHandler(baseView, ticket);
		AddTicketServiceAsync addTicketService = GWT.create(AddTicketService.class);
		addTicketService.addTicket(ticket, baseView.getUser().getKey(), addTicketCallback);
	}
	
	private void clearTextBoxes() {
		categoryListBox.setItemSelected(0, true);
		subjectTextBox.setText("");
		commentTextArea.setText("");
	}	

	public HTMLPanel getCenterContent() {
		return centerContent;
	}

	public void setCenterContent(HTMLPanel centerContent) {
		this.centerContent = centerContent;
	}

	public HTMLPanel getViewContent() {
		return viewContent;
	}

	public void setViewContent(HTMLPanel viewContent) {
		this.viewContent = viewContent;
	}

	public ListBox getCategoryListBox() {
		return categoryListBox;
	}

	public void setCategoryListBox(ListBox categoryListBox) {
		this.categoryListBox = categoryListBox;
	}

	public TextBox getSubjectTextBox() {
		return subjectTextBox;
	}

	public void setSubjectTextBox(TextBox subjectTextBox) {
		this.subjectTextBox = subjectTextBox;
	}

	public TextArea getCommentTextArea() {
		return commentTextArea;
	}

	public void setCommentTextArea(TextArea commentTextArea) {
		this.commentTextArea = commentTextArea;
	}

	public Button getSaveButton() {
		return saveButton;
	}

	public void setSaveButton(Button saveButton) {
		this.saveButton = saveButton;
	}

	public Button getClearButton() {
		return clearButton;
	}

	public void setClearButton(Button clearButton) {
		this.clearButton = clearButton;
	}
}
