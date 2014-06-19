package edu.morgan.chattywitty.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.xmpp.JID;
import com.google.appengine.api.xmpp.Message;
import com.google.appengine.api.xmpp.MessageBuilder;
import com.google.appengine.api.xmpp.MessageType;
import com.google.appengine.api.xmpp.Presence;
import com.google.appengine.api.xmpp.SendResponse;
import com.google.appengine.api.xmpp.XMPPService;
import com.google.appengine.api.xmpp.XMPPServiceFactory;
import com.sun.xml.internal.ws.resources.ClientMessages;

import edu.morgan.chattywitty.database.Operators;
import edu.morgan.chattywitty.service.OperatorsService;

public class Chat extends HttpServlet {
	private String firstName;
	private String lastName;
	private String department;
	private String email;
	private String clientEmail;
	private String phoneNumber;
	private String jid;
	private String operatorEmail;
	private String newOperatorForTransferEmail;
	private String clientMessage;
	private ArrayList<String> allAvailableEmails = new ArrayList<>();
	private ArrayList<String> rejectEmails = new ArrayList<>();
	private final XMPPService xmpp = XMPPServiceFactory.getXMPPService();
	private final OperatorsService service = new OperatorsService();
	private String operatorId;
	private boolean operatorAvalaibility = false;
	private boolean newOperatorForTransferAvailablity = false;
	private static final int AVAILABLE = 1;
	private static final int NOT_AVAILABLE = 0;
	private String operatorFullName;
	private String operatorDepartmentName;
	private String newOperatorForTransferScreenName;
	private String newOperatorDepartmentName;
	private String newOperatorFullName;
	private String operatorScreenName;
	private String newOperatorForGroupChatScreenName;
	private String newOperatorForGroupChatEmail;
	private ArrayList<JID> groupChatJIDs = new ArrayList<>();
	private boolean groupChat = false;
	private StringBuilder transcriptBuilder = new StringBuilder();

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		String token = request.getParameter("token");
		String mess = request.getParameter("message");
		// Get the message that was sent from a JabberID.
		Message message = xmpp.parseMessage(request);
		if (token != null && token.equals("01234")) {
			// initiateChat();
			PrintWriter wri = response.getWriter();
			wri.write("Hello, Pablo. You got right!");
		} else if (mess != null && !mess.isEmpty()) {
			clientMessage = mess;
		}
		else if (message != null) {
			if (message.getBody().equals("/yes")) {
				List<Operators> operator = service.getOperatorsByEmail(message
						.getFromJid().getId().split("/")[0]);
				operatorScreenName = operator.get(0).getScreenName();
				sendMessage(clientEmail, "You are now chatting with "
						+ operatorScreenName);

				operatorEmail = message.getFromJid().getId().split("/")[0];
				operatorFullName = operator.get(0).getFirstName() + " "
						+ operator.get(0).getLastName();
				operatorDepartmentName = operator.get(0).getDepartment();
				// Make the operator unavailable.
				operatorId = operator.get(0).getKey().toString();
				service.updateAvailability(operatorEmail, Chat.NOT_AVAILABLE);
				operatorAvalaibility = true;

			} else if ((message.getBody().equals("/exit") && message
					.getFromJid().getId().contains(operatorEmail))) {
				// Make operator available
				service.updateAvailability(operatorEmail, Chat.AVAILABLE);
				sendMessage(clientEmail,
						"Bye for now. Thank you for your time...");
				// send Transcripts to e-mails
				sendEmail(clientEmail, transcriptBuilder.toString());

				/* Send Transcript to the registered transcript Receiver Email */

				// Severe the connection between the operator and the client.
				operatorAvalaibility = false;

			} else if ((message.getBody().startsWith("/transfer") && message
					.getFromJid().getId().contains(operatorEmail))) {
				newOperatorForTransferScreenName = message.getBody().replace(
						"/transfer ", "");
				List<Operators> newOperatorForTransfer = service
						.getOperatorsByScreenName(newOperatorForTransferScreenName);
				newOperatorForTransferEmail = newOperatorForTransfer.get(0)
						.getEmailAddress();
				if (newOperatorForTransfer.get(0).getAvailable() == Chat.AVAILABLE
						&& xmpp.getPresence(
								new JID(newOperatorForTransferEmail))
								.isAvailable()) {
					newOperatorDepartmentName = newOperatorForTransfer.get(0)
							.getDepartment();
					newOperatorFullName = newOperatorForTransfer.get(0)
							.getFirstName()
							+ " "
							+ newOperatorForTransfer.get(0).getLastName();
					sendMessage(newOperatorForTransferEmail,
							"A chat is being transferred to you by "
									+ operatorFullName + " from "
									+ operatorDepartmentName
									+ ". And you'll be chatting with "
									+ firstName + " " + lastName
									+ ". Type '/yestransfer' to accept.");
					sendMessage(clientEmail,
							"You are now being transferred to "
									+ newOperatorForTransferScreenName);
				} else {
					sendMessage(
							operatorEmail,
							newOperatorForTransferScreenName
									+ " is either busy or offline. You could initiate another transfer...");
				}

			} else if ((message.getBody().startsWith("/yestransfer") && message
					.getFromJid().getId().contains(newOperatorForTransferEmail))) {
				sendMessage(clientEmail, "You are now chatting with "
						+ newOperatorForTransferScreenName);
				sendMessage(operatorEmail,
						"The chat was transferred succesfully.");
				service.updateAvailability(newOperatorForTransferEmail,
						Chat.NOT_AVAILABLE);
				service.updateAvailability(operatorEmail, Chat.AVAILABLE);
				// Changing the old parameters for the new operator's
				// parameters...
				operatorEmail = newOperatorForTransferEmail;
				operatorDepartmentName = newOperatorDepartmentName;
				operatorFullName = newOperatorFullName;
				operatorScreenName = newOperatorForTransferScreenName;

			} else if ((message.getBody().startsWith("/groupchat") && message
					.getFromJid().getId().contains(operatorEmail))) {
				newOperatorForGroupChatScreenName = message.getBody().replace(
						"/groupchat ", "");
				List<Operators> newOperatorForGroupChat = service
						.getOperatorsByScreenName(newOperatorForGroupChatScreenName);
				newOperatorForGroupChatEmail = newOperatorForGroupChat.get(0)
						.getEmailAddress();
				if (newOperatorForGroupChat.get(0).getAvailable() == Chat.AVAILABLE
						&& xmpp.getPresence(
								new JID(newOperatorForGroupChatEmail))
								.isAvailable()) {
					sendMessage(clientEmail,
							"You are about to enter a group chat with "
									+ operatorScreenName + " and "
									+ newOperatorForGroupChatScreenName);
					sendMessage(newOperatorForGroupChatEmail, operatorFullName
							+ " from " + operatorDepartmentName
							+ " wants to include you in a chat with "
							+ firstName + " " + lastName
							+ ". Type '/yesgroup' to accept.");

				} else {
					sendMessage(
							operatorEmail,
							newOperatorForGroupChatEmail
									+ " is either busy or offline. You could initiate another Group Chat...");
				}

			} else if ((message.getBody().startsWith("/yesgroup") && message
					.getFromJid().getId()
					.contains(newOperatorForGroupChatEmail))) {
				service.updateAvailability(newOperatorForGroupChatEmail,
						Chat.NOT_AVAILABLE);
				groupChatJIDs.add(new JID(clientEmail));
				groupChatJIDs.add(new JID(operatorEmail));
				groupChatJIDs.add(new JID(newOperatorForGroupChatEmail));
				groupChat = true;
				for (JID jid : groupChatJIDs) {
					if (jid.getId().contains(newOperatorForGroupChatEmail)) {
						continue;
					}
					sendMessage(jid.getId().split("/")[0],
							"You are now in a Group Chat with "
									+ newOperatorForGroupChatScreenName);
					sendMessage(newOperatorForGroupChatEmail,
							"You are now in the Group Chat");
				}

			} else if ((message.getBody().startsWith("/exitgroup") && message
					.getFromJid().getId()
					.contains(newOperatorForGroupChatEmail))) {
				service.updateAvailability(newOperatorForGroupChatEmail,
						Chat.AVAILABLE);

				// sendMessage();
				for (JID jid : groupChatJIDs) {
					if (jid.getId().contains(newOperatorForGroupChatEmail)) {
						continue;
					}
					sendMessage(jid.getId().split("/")[0],
							newOperatorForGroupChatScreenName
									+ " left the Group Chat");
					sendMessage(newOperatorForGroupChatEmail,
							"You left the Group Chat");
					groupChat = false;
					groupChatJIDs.remove(new JID(newOperatorForGroupChatEmail));
				}
			} else {
				startChatting(groupChat, message, groupChatJIDs);
			}
		}

	}

	private void initiateChat() {
		List<Operators> operatorsByDept = service
				.getOperatorsByDepartment("Admissions");

		for (int i = 0; i < operatorsByDept.size(); i++) {
			JID jid = new JID(operatorsByDept.get(i).getEmailAddress());

			Presence presence = xmpp.getPresence(jid);
			String msgBody = firstName + " " + lastName
					+ " wants to chat with you. Type '/yes' to accept.";

			if (operatorsByDept.get(i).getAvailable() == Chat.AVAILABLE
					&& presence.isAvailable()) {
				allAvailableEmails
						.add(operatorsByDept.get(i).getEmailAddress());
				sendMessage(jid.getId(), msgBody);
			}

		}
	}

	private void startChatting(boolean groupFlag, Message message,
			ArrayList<JID> groupJIDs) {
		transcriptBuilder.append(message.getBody() + "\n");
		if (groupFlag) {
			String sender = message.getFromJid().getId().split("/")[0];
			String name = sender.equals(operatorEmail) ? operatorScreenName
					: sender.equals(newOperatorForGroupChatEmail) ? newOperatorForGroupChatScreenName
							: firstName + " " + lastName;
			for (JID jid : groupJIDs) {
				if (jid.getId().contains(
						message.getFromJid().getId().split("/")[0])) {
					continue;
				}
				sendMessage(jid.getId().split("/")[0],
						name + ": " + message.getBody());
			}
		} else {
			if (operatorAvalaibility) {
				if (message.getFromJid().getId().contains(operatorEmail))
					sendMessage(clientEmail, message.getBody());
				if (message.getFromJid().getId().contains(clientEmail))
					sendMessage(operatorEmail, message.getBody());
			}
		}
	}

	private void sendMessage(String recipient, String body) {
		sendMessage(new JID[] { new JID(recipient) }, body);
	}

	private void sendMessage(List<String> recipientList, String body) {
		sendMessage((JID[]) recipientList.toArray(), body);
	}

	private void sendMessage(JID[] recipients, String body) {
		Message message = new MessageBuilder().withRecipientJids(recipients)
				.withMessageType(MessageType.NORMAL).withBody(body).build();

		xmpp.sendMessage(message);
	}

	private void replyToMessage(Message message, String body) {
		Message reply = new MessageBuilder()
				.withRecipientJids(message.getFromJid())
				.withMessageType(MessageType.NORMAL).withBody(body).build();

		xmpp.sendMessage(reply);
	}

	private void sendEmail(String recipient, String body) {
		String sendEmailFrom = "fasholaide@gmail.com";
		String recipientName = "";
		String messageSubject = "MorganChitChat Transcript";
		String messageText;
		if (recipient.equals(clientEmail)) {
			recipientName = firstName + " " + lastName;
			messageText = String
					.format("%s %s, Here is the transcript of the chat you had.\n\n %s \n\nYours truly,\nBabatunde Fashola",
							firstName, lastName, body);
		} else
			messageText = String
					.format("Hello, \nHere is the transcript of the chat you had.\n\n %s \n\nYours truly,\nBabatunde Fashola",
							body);
		Properties prop = new Properties();
		Session session = Session.getDefaultInstance(prop, null);
		try {
			javax.mail.Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(sendEmailFrom));
			msg.addRecipient(javax.mail.Message.RecipientType.TO,
					new InternetAddress(recipient, "Mr./Ms. " + recipientName));
			msg.setSubject(messageSubject);
			msg.setText(messageText);
			Transport.send(msg);
			// System.out.println("Successfull Delivery.");
		} catch (AddressException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	private void transferfiles() {

	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		firstName = request.getParameter("firstName");
		lastName = request.getParameter("lastName");
		department = request.getParameter("department");
		email = request.getParameter("email");
		phoneNumber = request.getParameter("phoneNumber");
		jid = request.getParameter("jid");
		PrintWriter pr = response.getWriter();
		// pr.write("<div class=\"container\" id=\"chat_frame\">");
		pr.write("<textarea id=\"chat_textarea\" disabled rows=\"25\" style=\"width:100%;resize:none;\"></textarea>");
		// pr.write("<br/>");
		pr.write("<textarea id=\"chat_input\" rows=\"2\" placeholder=\"Type your question here. When done typing, press enter/return\" style=\"width:100%;resize:none;\"></textarea>");
		// pr.write("</div>");

		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		pr.write(firstName + " " + lastName + " " + department + " " + email
				+ " " + phoneNumber + " " + jid);

	}
}