package model;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import exception.AssetAllocatedException;
import exception.InvitationInvitesOwnerException;
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
	private TaskAssetManager tim;
	
	/**
	 * User to be invited
	 */
	private User user;
	
	@Before
	public void setUp() throws Exception
	{
		manager = new RepositoryManager();
		User owner = new User("John");
		user = new User("Jack");
		GregorianCalendar startDate = new GregorianCalendar();
		GregorianCalendar endDate = new GregorianCalendar();
		endDate.add(Calendar.DAY_OF_YEAR, 4);
		// 4 days to finish the task from now on
		int duration = 1;
		taskMain = new Task("Main Task",owner,new TaskTimings(startDate,endDate,duration), manager.getClock());
		tim = taskMain.getTaskInvitationManager();
	}
	
	@Test
	public void initTest()
	{
		assertEquals(taskMain, tim.getTask());
		assertTrue(tim.getAssetAllocations().isEmpty());
	}
	
	@Test
	public void testInvite1() throws AssetAllocatedException, InvitationInvitesOwnerException
	{
		Invitation invitation = new Invitation(taskMain, user);
		assertTrue(tim.getAssetAllocations().contains(invitation));
	}
	@Test(expected=AssetAllocatedException.class)
	public void testInvite2() throws AssetAllocatedException, InvitationInvitesOwnerException
	{
		new Invitation(taskMain, user);
		new Invitation(taskMain, user);
	}
	
	@After
	public void tearDown() throws Exception {
		tim = null;
		taskMain = null;
	}
}
