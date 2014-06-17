package edu.morgan.chattywitty.service;

import java.util.List;

import javax.jdo.PersistenceManager;

import javax.jdo.Query;

import edu.morgan.chattywitty.database.Operators;
import edu.morgan.chattywitty.PMF;

public class OperatorsService {

	public void storeOperator(Operators operator) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			pm.makePersistent(operator);
		} finally {
			pm.close();
		}
	}

	/*
	 * To get all the operators in a department for the purpose of broadcasting
	 * the messages to the operators
	 */
	public List<Operators> getOperatorsByDepartment(String department) {
		List<Operators> operatorsByDepartment = null;
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query q = pm.newQuery(Operators.class, "Department == '" + department
				+ "'");
		try {
			operatorsByDepartment = (List<Operators>) q.execute();
		} finally {
			pm.close();
		}
		return operatorsByDepartment;
	}

	/* To get the Operator with a specific email */
	public List<Operators> getOperatorsByEmail(String email) {
		List<Operators> operatorsByEmail = null;
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query q = pm.newQuery(Operators.class, "EmailAddress == '" + email
				+ "'");
		try {
			operatorsByEmail = (List<Operators>) q.execute();
		} finally {
			pm.close();
		}
		return operatorsByEmail;
	}

	/*
	 * To Display all the operators in the DataStore and ordering in an
	 * ascending order with the date added...
	 */
	public List<Operators> getAllOperators() {
		List<Operators> allOperators = null;
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query q = pm.newQuery(Operators.class);
		q.setOrdering("DateAdded asc");
		try {
			allOperators = (List<Operators>) q.execute();
		} finally {
			pm.close();
		}
		return allOperators;
	}
	/*Update the availability of the Operator*/
	public void updateAvailability(String email, int availablility) {
		List<Operators> operator = null;
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query q = pm.newQuery(Operators.class, "EmailAddress == '" + email
				+ "'");
		try {
			operator = (List<Operators>) q.execute();
			operator.get(0).setAvailable(availablility);
		} finally {
			pm.close();
		}
	}
	/*Get the operator by his or her screen name for transfer purposes*/
	public List<Operators> getOperatorsByScreenName(String screenName) {
		List<Operators> operatorsByEmail = null;
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query q = pm.newQuery(Operators.class, "ScreenName == '" + screenName
				+ "'");
		try {
			operatorsByEmail = (List<Operators>) q.execute();
		} finally {
			pm.close();
		}
		return operatorsByEmail;
	}

}
