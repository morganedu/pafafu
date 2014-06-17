package edu.morgan.chattywitty.servlets;

import java.io.IOException;
import java.util.Date;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.google.appengine.api.xmpp.JID;
import com.google.appengine.api.xmpp.XMPPService;
import com.google.appengine.api.xmpp.XMPPServiceFactory;

import edu.morgan.chattywitty.database.Operators;
import edu.morgan.chattywitty.service.OperatorsService;

@SuppressWarnings("serial")
public class RegisterServlet extends HttpServlet {
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String emailAddress = request.getParameter("emailAddress");
		String screenName = request.getParameter("screenName");
		String department = request.getParameter("department");

		Operators operators = new Operators();
		operators.setDateAdded(new Date());
		operators.setFirstName(firstName);
		operators.setLastName(lastName);
		operators.setDepartment(department);
		operators.setScreenName(screenName);
		operators.setEmailAddress(emailAddress);
		operators.setAvailable(null);

		OperatorsService service = new OperatorsService();
		service.storeOperator(operators);

		sendInvitation(emailAddress);
		sendMail(emailAddress, firstName, lastName);

	}

	public void sendInvitation(String emailAddress) {
		JID jid = new JID(emailAddress);
		// String msgBody =
		// "Someone has sent you a gift on Example.com. To view: http://example.com/gifts/";
		XMPPService xmpp = XMPPServiceFactory.getXMPPService();
		xmpp.sendInvitation(jid);
	}

	

	public void sendMail(String sendMailTo, String firstName, String lastName) {
		String sendEmailFrom = "fasholaide@gmail.com";
		String recipientName = firstName + " " + lastName;
		String messageSubject = "Welcome to MorganChitChat";
		String messageText = String
				.format("Hello %s %s,\n\nYou have been successfully added to the MoganChitChat Application.\nPlease, accept the chat request from morganchitchat@appstore.com.\n\nYours truly,\nBabatunde Fashola",
						firstName, lastName);;
		Properties prop = new Properties();
		Session session = Session.getDefaultInstance(prop, null);
		try {
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(sendEmailFrom));
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(
					sendMailTo, "Mr./Ms. " + recipientName));
			msg.setSubject(messageSubject);
			msg.setText(messageText);
			Transport.send(msg);
			//System.out.println("Successfull Delivery.");
		} catch (AddressException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
}
