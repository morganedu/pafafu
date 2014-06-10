package edu.morgan.pafafu;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Text;
import com.google.appengine.api.xmpp.JID;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable="true")
public class Question {
	  @PrimaryKey
	  @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	  private Key key;
	
	  @Persistent(defaultFetchGroup = "true")
	  private Text question;
	
	  @Persistent
	  private JID asker;
	
	  @Persistent
	  private Date asked;
	
	  @Persistent(defaultFetchGroup = "true")
	  private Text answer;
	
	  @Persistent
	  private JID answerer;
	
	  @Persistent
	  private Date answered;
	
	  @Persistent
	  private Boolean suspended;
	
	  public Text getQuestion() {
		return question;
	}

	public void setQuestion(Text question) {
		this.question = question;
	}
	
	public JID getAsker() {
		return asker;
	}

	public Date getAsked() {
		return asked;
	}

	public void setAsked(Date asked) {
		this.asked = asked;
	}

	public Text getAnswer() {
		return answer;
	}

	public void setAnswer(Text answer) {
		this.answer = answer;
	}

	public JID getAnswerer() {
		return answerer;
	}

	public void setAnswerer(JID sender) {
		this.answerer = sender;
	}

	public Date getAnswered() {
		return answered;
	}

	public void setAnswered(Date answered) {
		this.answered = answered;
	}

	public Key getKey() {
	    return key;
	  }

	public void setQuestion(String questionText) {
		this.question = new Text(questionText);
	}

	public void setAsker(JID sender) {
		this.asker = sender;
	}

	public void setAnswer(String body) {
		this.setAnswer(new Text(body));	
	}
}