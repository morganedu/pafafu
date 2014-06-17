/* Copyright (c) 2009 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package edu.morgan.chattywitty.database;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.xmpp.JID;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable = "true")
public class Operators {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;
	@Persistent
	private String LastName;
	@Persistent
	private String FirstName;
	@Persistent
	private String ScreenName;
	@Persistent
	private String EmailAddress;
	@Persistent
	private Date DateAdded;
	@Persistent
	private String Department;
	@Persistent
	private int Avalibale;
	
	public int getAvailable()
	{
		return Avalibale;
	}
	
	public void setAvailable(Integer avail)
	{
		if(avail == null)
			this.Avalibale = 1;
		else
			this.Avalibale = avail.intValue();
	}

	public Key getKey() {
		return key;
	}

	public String getLastName() {
		return LastName;
	}

	public void setLastName(String lastName) {
		this.LastName = lastName;
	}

	public String getFirstName() {
		return FirstName;
	}

	public void setFirstName(String firstName) {
		this.FirstName = firstName;
	}

	public String getScreenName() {
		return ScreenName;
	}

	public void setScreenName(String screenName) {
		this.ScreenName = screenName;
	}

	public String getEmailAddress() {
		return EmailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.EmailAddress = emailAddress;
	}

	public Date getDateAdded() {
		return DateAdded;
	}

	public void setDateAdded(Date date) {
		this.DateAdded = date;
	}
	
	public String getDepartment()
	{
		return Department;
	}
	public void setDepartment(String department)
	{
		this.Department = department;
	}
}
