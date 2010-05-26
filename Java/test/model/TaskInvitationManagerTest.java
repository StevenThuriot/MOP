package model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import exception.AssetAllocatedException;
import exception.AssetConstraintFullException;
import exception.AssetTypeNotRequiredException;
import exception.IllegalStateCallException;
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
	
	/**
	 * User owning the task.
	 */
	private User owner;
	
	@SuppressWarnings("unchecked")
	@Before
	public void setUp() throws Exception
	{
		manager = new RepositoryManager();
		owner = new User("John",new UserType(""));
		user = new User("Jack",new UserType(""));
		GregorianCalendar startDate = new GregorianCalendar();
		GregorianCalendar endDate = new GregorianCalendar();
		endDate.add(Calendar.DAY_OF_YEAR, 4);
		// 4 days to finish the task from now on
		int duration = 1;
		ArrayList<TaskTypeConstraint> constraints = new ArrayList<TaskTypeConstraint>();
			constraints.add(new TaskTypeConstraint(user.getType(),1,2));
			
			ArrayList<UserType> userTypes = new ArrayList<UserType>();
			userTypes.add(user.getType());
			userTypes.add(owner.getType());
			
			
		TaskType taskType = new TaskType("reorganizing the test cases", 
				new ArrayList<Field>(), constraints, userTypes);
		//taskMain = new Task("Main Task",owner,new TaskTimings(startDate,endDate,duration), manager.getClock());
		taskMain = TaskFactory.createTask("Main Task", taskType, new ArrayList<Field>(),
				owner, new TaskTimings(startDate, endDate, duration), manager.getClock(),new Project("X"));
		tim = taskMain.getTaskAssetManager();
	}
	
	@Test
	public void initTest()
	{
		assertEquals(taskMain, tim.getTask());
		assertTrue(tim.getAssetAllocations().isEmpty());
	}
	
	@Test(expected=InvitationInvitesOwnerException.class)
	public void testInvite1() throws AssetAllocatedException, InvitationInvitesOwnerException, IllegalStateCallException, AssetTypeNotRequiredException, AssetConstraintFullException
	{
		@SuppressWarnings("unused")
		Invitation invitation = new Invitation(taskMain, owner);
	}
	
	@Test(expected=AssetAllocatedException.class)
	public void testInvite2() throws AssetAllocatedException, InvitationInvitesOwnerException, IllegalStateCallException, AssetTypeNotRequiredException, AssetConstraintFullException
	{
		new Invitation(taskMain, user);
		new Invitation(taskMain, user);
	}
	
	@Test
	public void testInvite3() throws AssetAllocatedException, InvitationInvitesOwnerException, IllegalStateCallException, AssetTypeNotRequiredException, AssetConstraintFullException
	{
		Invitation invitation = new Invitation(taskMain, user);
		assertTrue(tim.getAssetAllocations().contains(invitation));
	}
	
	@After
	public void tearDown() {
		tim = null;
		taskMain = null;
	}
}
