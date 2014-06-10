package edu.morgan.pafafu;

import com.google.appengine.api.xmpp.JID;
import com.google.appengine.api.xmpp.Message;
import com.google.appengine.api.xmpp.MessageBuilder;
import com.google.appengine.api.xmpp.MessageType;
import com.google.appengine.api.xmpp.XMPPService;
import com.google.appengine.api.xmpp.XMPPServiceFactory;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Handler class for all XMPP messages.
 */
public class XmppReceiverServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final XMPPService xmppService = XMPPServiceFactory.getXMPPService();

  @Override
  public void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws IOException {
    Message message = xmppService.parseMessage(req);

    if (message.getBody().startsWith("/askme")) {
      handleAskMeCommand(message);
    } else if (message.getBody().startsWith("/tellme ")) {
      String questionText = message.getBody().replaceFirst("/tellme ", "");
      handleTellMeCommand(message, questionText);
    } else if (message.getBody().startsWith("/")) {
      handleUnrecognizedCommand(message);
    } else {
      handleAnswer(message);
    }
  }

  private void handleUnrecognizedCommand(Message message) {
	  replyToMessage(message, "The answer is probably: 42");
}

/**
   * Handles /tellme requests, asking the Guru a question.
   */
  private void handleTellMeCommand(Message message, String questionText) {
    QuestionService questionService = new QuestionService();

    JID sender = message.getFromJid();
    Question previouslyAsked = questionService.getAsked(sender);

    if (previouslyAsked != null) {
      // Already have a question, and they're not answering one
      replyToMessage(message, "Please! One question at a time! You can ask me another once you have an answer to your current question.");
    } else {
      // Asking a question
      Question question = new Question();
      question.setQuestion(questionText);
      question.setAsked(new Date());
      question.setAsker(sender);

      questionService.storeQuestion(question);

      // Try and find one for them to answer
      Question assigned = questionService.assignQuestion(sender);

      if (assigned != null) {
        replyToMessage(message, "While I'm thinking, perhaps you can answer me this: " + assigned.getQuestion());
      } else {
        replyToMessage(message, "Hmm. Let me think on that a bit.");
      }
    }
  }

  // ...

  private void replyToMessage(Message message, String body) {
    Message reply = new MessageBuilder()
        .withRecipientJids(message.getFromJid())
        .withMessageType(MessageType.NORMAL)
        .withBody(body)
        .build();

    xmppService.sendMessage(reply);
  }
  
  private void handleAnswer(Message message) {
	    QuestionService questionService = new QuestionService();

	    JID sender = message.getFromJid();
	    Question currentlyAnswering = questionService.getAnswering(sender);

	    if (currentlyAnswering != null) {
	      // Answering a question
	      currentlyAnswering.setAnswer(message.getBody());
	      currentlyAnswering.setAnswered(new Date());
	      currentlyAnswering.setAnswerer(sender);

	      questionService.storeQuestion(currentlyAnswering);

	      // Send the answer to the asker
	      this.sendMessage(currentlyAnswering.getAsker(), "I have thought long and hard, and concluded: " + currentlyAnswering.getAnswer());

	      // Send acknowledgment to the answerer
	      Question previouslyAsked = questionService.getAsked(sender);

	      if (previouslyAsked != null) {
	        replyToMessage(message, "Thank you for your wisdom. I'm still thinking about your question.");
	      } else {
	        replyToMessage(message, "Thank you for your wisdom.");
	      }
	    } else {
	      handleUnrecognizedCommand(message);
	    }
	  }

	  // ...

	  private void sendMessage(JID recipient, String body) {
	    Message message = new MessageBuilder()
	        .withRecipientJids(recipient)
	        .withMessageType(MessageType.NORMAL)
	        .withBody(body)
	        .build();

	    xmppService.sendMessage(message);
	  }
	  
	  private void handleAskMeCommand(Message message) {
		    QuestionService questionService = new QuestionService();

		    JID sender = message.getFromJid();
		    Question newlyAssigned = questionService.assignQuestion(sender);

		    if (newlyAssigned != null) {
		      replyToMessage(message, "While I'm thinking, perhaps you can answer me this: " + newlyAssigned.getQuestion());
		    } else {
		      replyToMessage(message, "Sorry, I don't have anything to ask you at the moment.");
		    }
		  }

  
}