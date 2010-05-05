package model;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import exception.InvitationExistsException;
import static org.junit.Assert.*;

import model.repositories.RepositoryManager;

public class TaskInvitationManagerTest {
	
	/**
	 * RepositoryManager to be used.
	 */
	private RepositoryManager manager;
	
	/**
	 * The Task that the TaskInvitationManager is appointed to.
	 */
	private Task taskMain;
	
	/**
	 * The InvitationManager itself
	 */
	private TaskInvitationManager tim;
	
	@Before
	public void setUp() throws Exception
	{
		manager = new RepositoryManager();
		User user = new User("John");
		GregorianCalendar startDate = new GregorianCalendar();
		GregorianCalendar endDate = new GregorianCalendar();
		endDate.add(Calendar.DAY_OF_YEAR, 4);
		// 4 days to finish the task from now on
		int duration = 1;
		taskMain = new Task("Main Task",user,new TaskTimings(startDate,endDate,duration), manager.getClock());
		tim = taskMain.getTaskInvitationManager();
	}
	
	@Test
	public void initTest()
	{
		assertEquals(taskMain, tim.getTask());
		assertTrue(tim.getInvitations().isEmpty());
	}
	
	@Test
	public void testInvite1() throws InvitationExistsException
	{
		User user = new User("Bart");
		Invitation invitation = new Invitation(taskMain, user);
		assertTrue(tim.getInvitations().contains(invitation));
	}
	@Test(expected=InvitationExistsException.class)
	public void testInvite2() throws InvitationExistsException
	{
		User user = new User("Bart");
		new Invitation(taskMain, user);
		new Invitation(taskMain, user);
	}
	
	@After
	public void tearDown() throws Exception {
		tim = null;
		taskMain = null;
	}
}
