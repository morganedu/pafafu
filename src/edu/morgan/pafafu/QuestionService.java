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
public class QuestionService extends HttpServlet {
	
  @Override
  public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
	  
  }

public Question getAsked(JID sender) {
	// TODO Auto-generated method stub
	return null;
}

public void storeQuestion(Question question) {
	// TODO Auto-generated method stub
	
}

public Question assignQuestion(JID sender) {
	// TODO Auto-generated method stub
	return null;
}

public Question getAnswering(JID sender) {
	// TODO Auto-generated method stub
	return null;
}
}