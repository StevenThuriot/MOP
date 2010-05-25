package model;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import model.repositories.RepositoryManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import exception.AssetConstraintFullException;
import exception.AssetTypeNotRequiredException;
import exception.BusinessRule1Exception;
import exception.BusinessRule3Exception;
import exception.EmptyStringException;
import exception.IllegalStateCallException;
import exception.AssetAllocatedException;
import exception.InvitationInvitesOwnerException;
import exception.WrongFieldsForChosenTypeException;
import exception.WrongUserForTaskTypeException;
public class UserTaskManagerTest {

	/**
	 * The user we'll be using
	 */
	private User user;
	private UserType userType;
	/**
	 * A task we can use
	 */
	private Task taskMain;
	/**
	 * A repositoryManager for the time
	 */
	private RepositoryManager manager;
	
	@SuppressWarnings("unchecked")
	@Before
	public void setUp() throws NullPointerException, EmptyStringException, BusinessRule1Exception, IllegalStateCallException, BusinessRule3Exception, WrongFieldsForChosenTypeException, WrongUserForTaskTypeException
	{
		manager = new RepositoryManager();
		userType = new UserType("");
		user = new User("Bart",userType);
		GregorianCalendar startDate = new GregorianCalendar();
		GregorianCalendar endDate = new GregorianCalendar();
		endDate.add(Calendar.DAY_OF_YEAR, 4);
		// 4 days to finish the task from now on
		int duration = 1;
		ArrayList<TaskTypeConstraint> constraints = new ArrayList<TaskTypeConstraint>();
			constraints.add(new TaskTypeConstraint(userType,1,2));
			
			ArrayList<UserType> userTypes = new ArrayList<UserType>();
			userTypes.add(user.getType());
			
		TaskType taskType = new TaskType("reorganizing the test cases", 
				new ArrayList<Field>(), constraints, userTypes);
		taskMain = TaskFactory.createTask("Main Task", taskType, new ArrayList<Field>(),
				user, new TaskTimings(startDate, endDate, duration), manager.getClock(),new Project("X"));
	}
	@After
	public void tearDown()
	{
		user = null;
		manager = null;
		taskMain = null;
	}
	@Test
	public void adding() throws AssetAllocatedException, InvitationInvitesOwnerException, IllegalStateCallException, AssetTypeNotRequiredException, AssetConstraintFullException
	{
		User user1 = new User("John", userType);
		Invitation invitation = new Invitation(taskMain, user1);
		assertTrue(user1.getUserTaskManager().getInvitations().contains(invitation));
	}
	@Test
	public void removing() throws AssetAllocatedException, InvitationInvitesOwnerException, IllegalStateCallException, AssetTypeNotRequiredException, AssetConstraintFullException
	{
		User user1 = new User("John",userType);
		Invitation invitation = new Invitation(taskMain, user1);
		assertTrue(user1.getUserTaskManager().getInvitations().contains(invitation));
		user1.removeInvitation(invitation);
		assertFalse(user1.getUserTaskManager().getInvitations().contains(invitation));
	}
}
