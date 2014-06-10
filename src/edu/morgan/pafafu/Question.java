package edu.morgan.pafafu;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Text;

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
	  private String asker;
	
	  @Persistent
	  private Date asked;
	
	  @Persistent(defaultFetchGroup = "true")
	  private Text answer;
	
	  @Persistent
	  private String answerer;
	
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

	public void setQuestion(String question) {
		this.question = question;
	}
	
	public String getAsker() {
		return asker;
	}

	public void setAsker(String asker) {
		this.asker = asker;
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

	public String getAnswerer() {
		return answerer;
	}

	public void setAnswerer(String answerer) {
		this.answerer = answerer;
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
}