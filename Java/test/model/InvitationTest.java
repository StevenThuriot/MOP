package model;

import java.util.Calendar;
import java.util.GregorianCalendar;

import model.repositories.RepositoryManager;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import exception.InvitationExistsException;

public class InvitationTest {
	/**
	 * RepositoryManager to be used.
	 */
	private RepositoryManager manager;
	
	/**
	 * The Task that the Invitation will be created for is appointed to.
	 */
	private Task taskMain;
	
	/**
	 * User to be invited
	 */
	private User user;

	/**
	 * The invitation itself
	 */
	private Invitation invitation;
	
	@Before
	public void setUp() throws Exception
	{
		manager = new RepositoryManager();
		user = new User("John");
		GregorianCalendar startDate = new GregorianCalendar();
		GregorianCalendar endDate = new GregorianCalendar();
		endDate.add(Calendar.DAY_OF_YEAR, 4);
		// 4 days to finish the task from now on
		int duration = 1;
		taskMain = new Task("Main Task",user,new TaskTimings(startDate,endDate,duration), manager.getClock());
	}
	@Test
	public void initTest() throws InvitationExistsException
	{
		invitation = new Invitation(taskMain, user);
		assertTrue(taskMain.getTaskInvitationManager().getInvitations().contains(invitation));
		//TODO: Check this for the user
	}
}