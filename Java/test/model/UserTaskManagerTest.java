package model;
import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.GregorianCalendar;

import model.repositories.RepositoryManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import exception.BusinessRule1Exception;
import exception.BusinessRule3Exception;
import exception.EmptyStringException;
import exception.IllegalStateCallException;
import exception.InvitationExistsException;
import exception.InvitationInvitesOwnerException;
public class UserTaskManagerTest {

	/**
	 * The user we'll be using
	 */
	private User user;
	/**
	 * A task we can use
	 */
	private Task taskMain;
	/**
	 * A repositoryManager for the time
	 */
	private RepositoryManager manager;
	
	@Before
	public void setUp() throws NullPointerException, EmptyStringException, BusinessRule1Exception, IllegalStateCallException, BusinessRule3Exception
	{
		user = new User("Bart");
		manager = new RepositoryManager();
		GregorianCalendar startDate = new GregorianCalendar();
		GregorianCalendar endDate = new GregorianCalendar();
		endDate.add(Calendar.DAY_OF_YEAR, 4);
		// 4 days to finish the task from now on
		int duration = 1;
		taskMain = new Task("Main Task",user,new TaskTimings(startDate,endDate,duration), manager.getClock());
	}
	@After
	public void tearDown()
	{
		user = null;
	}
	@Test
	public void adding() throws InvitationExistsException, InvitationInvitesOwnerException
	{
		User user1 = new User("John");
		Invitation invitation = new Invitation(taskMain, user1);
		assertTrue(user1.getUserTaskManager().getInvitations().contains(invitation));
	}
	@Test
	public void removing() throws InvitationExistsException, InvitationInvitesOwnerException
	{
		User user1 = new User("John");
		Invitation invitation = new Invitation(taskMain, user1);
		assertTrue(user1.getUserTaskManager().getInvitations().contains(invitation));
		user1.removeInvitation(invitation);
		assertFalse(user1.getUserTaskManager().getInvitations().contains(invitation));
	}
}
