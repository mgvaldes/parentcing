package com.ing3nia.parentalcontrol.client.views;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;

public class NewTicketView {
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
	
	public NewTicketView(HTMLPanel centerContent) {
		this.centerContent = centerContent;
		
		viewContent.add(newTicketLabel);
		viewContent.add(categoryLabel);
		viewContent.add(categoryListBox);
		viewContent.add(subjectLabel);
		viewContent.add(subjectTextBox);
		viewContent.add(commentLabel);
		viewContent.add(commentTextArea);
		
		saveButton.addClickHandler(new ClickHandler() {
	    	public void onClick(ClickEvent event) {
	    		saveTicket();
	    	}
	    });
		
		buttonPanel.add(saveButton);
		
		clearButton.addClickHandler(new ClickHandler() {
	    	public void onClick(ClickEvent event) {
	    		clearTextBoxes();
	    	}
	    });
		
		buttonPanel.add(clearButton);
		viewContent.add(buttonPanel);
		
	}
	
	public void initNewTicketView(){
		centerContent.clear();
		centerContent.add(viewContent);
	}
	
	private void saveTicket() {
		
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
