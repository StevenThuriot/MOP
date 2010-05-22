package model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import model.repositories.RepositoryManager;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import exception.AssetAllocatedException;
import exception.AssetConstraintFullException;
import exception.AssetTypeNotRequiredException;
import exception.IllegalStateCallException;
import exception.InvitationInvitesOwnerException;

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
	private UserType userType;

	/**
	 * The invitation itself
	 */
	private Invitation invitation;
	
	@SuppressWarnings("unchecked")
	@Before
	public void setUp() throws Exception
	{
		manager = new RepositoryManager();
		user = new User("John",userType);
		GregorianCalendar startDate = new GregorianCalendar();
		GregorianCalendar endDate = new GregorianCalendar();
		endDate.add(Calendar.DAY_OF_YEAR, 4);
		// 4 days to finish the task from now on
		int duration = 1;
		 ArrayList<TaskTypeConstraint> constraints = new ArrayList<TaskTypeConstraint>();
			constraints.add(new TaskTypeConstraint(userType,1,2));
		TaskType taskType = new TaskType("reorganizing the test cases", 
				new ArrayList<Field>(), constraints);
		taskMain = TaskFactory.createTask("Main Task", taskType, new ArrayList<Field>(),
				user, new TaskTimings(startDate, endDate, duration), manager.getClock());
	}
	@After
	public void tearDown(){
		manager = null;
		user = null;
		taskMain = null;
	}
	
	@Test
	public void initTest() throws AssetAllocatedException, InvitationInvitesOwnerException, IllegalStateCallException, AssetTypeNotRequiredException, AssetConstraintFullException
	{
		User user2 = new User("Jack",userType);
		invitation = new Invitation(taskMain, user2);
		assertTrue(taskMain.getTaskAssetManager().getAssetAllocations().contains(invitation));
		//TODO: Check this for the user
	}
	@Test
	public void removeTest() throws AssetAllocatedException, InvitationInvitesOwnerException, IllegalStateCallException, AssetTypeNotRequiredException, AssetConstraintFullException
	{
		User user2 = new User("Jack",userType);
		invitation = new Invitation(taskMain,user2);
		assertFalse(taskMain.getTaskAssetManager().getAssetAllocations().isEmpty());
		invitation.remove();
		assertTrue(taskMain.getTaskAssetManager().getAssetAllocations().isEmpty());
	}
}
