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

  private static final XMPPService xmppService =
      XMPPServiceFactory.getXMPPService();

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

  /**
   * Handles /tellme requests, asking the Guru a question.
   */
  private void handleTellMeCommand(Message message, String questionText) {
	  /*
    QuestionService questionService = new QuestionService();

    JID sender = message.getFromJid();
    Question previouslyAsked = questionService.getAsked(sender);

    if (previouslyAsked != null) {
      // Already have a question, and they're not answering one
      replyToMessage(message, "Please! One question at a time! You can ask " +
          "me another once you have an answer to your current question.");
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
        replyToMessage(message, "While I'm thinking, perhaps you can " +
            "answer me this: " + assigned.getQuestion());
      } else {
        replyToMessage(message, "Hmm. Let me think on that a bit.");
      }
    }
    */
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
}