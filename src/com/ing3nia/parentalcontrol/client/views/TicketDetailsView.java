package com.ing3nia.parentalcontrol.client.views;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.ing3nia.parentalcontrol.client.views.models.TicketAnswerModel;

public class TicketDetailsView {
	/**
	 * Center Panel containing all the widgets of the 
	 * new ticket details view.
	 */
	private HTMLPanel centerContent;
	
	/**
	 * Main panel of ticket details view that groups all 
	 * the widgets together.
	 */
	private HTMLPanel ticketContent;
	
	/**
	 * Ticket message label.
	 */
	private Label ticketMessageLabel;
	
	/**
	 * Main panel of ticket details view that groups all 
	 * the widgets together.
	 */
	private HTMLPanel ticketDetailsContent;
	
	/**
	 * Ticket subject label.
	 */
	private Label ticketSubjectLabel;
	
	/**
	 * Ticket subject.
	 */
	private Label ticketSubject;
	
	/**
	 * Ticket question label.
	 */
	private Label ticketQuestionLabel;
	
	/**
	 * Ticket question.
	 */
	private Label ticketQuestion;
	
	/**
	 * Ticket date label.
	 */
	private Label ticketDateLabel;
	
	/**
	 * Ticket date.
	 */
	private Label ticketDate;
	
	/**
	 * Button to close ticket.
	 */
	private Button closeButton;
	
	/**
	 * Main panel of ticket answers that groups all 
	 * the widgets together.
	 */
	private HTMLPanel ticketAnswersContent;
	
	/**
	 * 
	 */
	private HTMLPanel ticketAnswersDetailsContent;
	
	/**
	 * Ticket answers label. 
	 */
	private Label ticketAnswersLabel;
	
	/**
	 * List of ticket answers.
	 */
	private List<TicketAnswerModel> ticketAnswers;
	
	/**
	 * Main panel of ticket reply that groups all 
	 * the widgets together.
	 */
	private HTMLPanel ticketReplyContent;
	
	/**
	 * Reply ticket label;
	 */
	private Label replyTicketLabel;
	
	/**
	 * Reply text area.
	 */
	private TextArea replyTextArea;
	
	/**
	 * Panel containing view buttons.
	 */
	private FlowPanel buttonPanel;
	
	/**
	 * Save button.
	 */
	private Button saveButton;
	
	/**
	 * Clear button.
	 */
	private Button clearButton;
	
	public TicketDetailsView(HTMLPanel centerContent, String subject, String comment, Date date) {
		this.centerContent = centerContent;
		
		this.ticketContent = new HTMLPanel("");
		
		this.ticketDetailsContent = new HTMLPanel("");
		this.ticketMessageLabel = new Label("Ticket Message");
		this.ticketSubjectLabel = new Label("Subject:");
		this.ticketQuestionLabel = new Label("Comment:");
		this.ticketDateLabel = new Label("Date:");
		this.closeButton = new Button("Close Ticket");
		
		this.ticketAnswersContent = new HTMLPanel("");
		this.ticketAnswersDetailsContent = new HTMLPanel("");
		this.ticketAnswersLabel = new Label("PRC Ticket, commented:");
		
		this.ticketReplyContent = new HTMLPanel("");
		this.replyTicketLabel = new Label("Reply:");
		this.replyTextArea = new TextArea();
		
		this.buttonPanel = new FlowPanel();
		this.saveButton = new Button("Save");
		this.clearButton = new Button("Clear");
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy - KK:mm:ss a");
		String stringDate = formatter.format(date);
		
		addTestTicketAnswers();
		initTicketDetailsView(subject, comment, stringDate);
	}

	public void addTestTicketAnswers() {
		TicketAnswerModel answer = new TicketAnswerModel("13/11/2011 2:48:00 AM", "Conejo", "Hayaaaaaa!");
		ticketAnswers.add(answer);
		
		answer = new TicketAnswerModel("13/11/2011 2:49:00 AM", "Conejo", "My name is El Coune...");
		ticketAnswers.add(answer);
	}
	
	public void initTicketDetailsView(String subject, String comment, String date) {
		// Adding ticket details
		this.ticketContent.add(this.ticketMessageLabel);
		
		this.ticketDetailsContent.add(this.ticketSubjectLabel);
		this.ticketSubject = new Label(subject);
		this.ticketDetailsContent.add(this.ticketSubject);
		
		this.ticketDetailsContent.add(this.ticketQuestionLabel);
		this.ticketQuestion = new Label(comment);
		this.ticketDetailsContent.add(this.ticketQuestion);
		
		this.ticketDetailsContent.add(this.ticketDateLabel);
		this.ticketDate = new Label(date);
		this.ticketDetailsContent.add(this.ticketDate);
		
		this.ticketContent.add(this.ticketDetailsContent);
		this.ticketContent.add(this.closeButton);
		
		this.centerContent.add(this.ticketContent);
		
		this.ticketAnswersContent.add(this.ticketAnswersLabel);
		loadTicketAnswers();
		this.ticketAnswersContent.add(this.ticketAnswersDetailsContent);
		
		this.centerContent.add(this.ticketAnswersContent);
		
		this.ticketReplyContent.add(this.replyTicketLabel);
		this.ticketReplyContent.add(this.replyTextArea);
		
		this.buttonPanel.add(saveButton);
		this.buttonPanel.add(clearButton);
		this.ticketReplyContent.add(this.buttonPanel);
		
		this.centerContent.add(this.ticketReplyContent);
	}
	
	public void loadTicketAnswers() {
		HTMLPanel ticketAnswerContent;
		Label answerInfo;
		Label answer;
		
		for (TicketAnswerModel ans : ticketAnswers) {
			ticketAnswerContent = new HTMLPanel("");
			
			answerInfo = new Label(ans.getDate() + " by " + ans.getUser());
			ticketAnswerContent.add(answerInfo);
			
			answer = new Label(ans.getAnswer());
			ticketAnswerContent.add(answer);
			
			this.ticketAnswersDetailsContent.add(ticketAnswerContent);
		}
	}
}
