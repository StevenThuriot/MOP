package controller;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import model.Field;
import model.Invitation;
import model.Task;
import model.TaskFactory;
import model.TaskTimings;
import model.TaskType;
import model.TaskTypeConstraint;
import model.User;
import model.UserType;
import model.Invitation.InvitationState;
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
import exception.InvitationNotPendingException;
import exception.WrongFieldsForChosenTypeException;
import exception.WrongUserForTaskTypeException;
import static org.junit.Assert.*;
public class InvitationControllerTest {

	private RepositoryManager manager;
	private Task taskMain;
	private User user;
	private UserType userType;
	private User owner;
	private InvitationController controller;
	@SuppressWarnings("unchecked")
	@Before
	public void setUp() throws NullPointerException, EmptyStringException, BusinessRule1Exception, IllegalStateCallException, BusinessRule3Exception, WrongFieldsForChosenTypeException, WrongUserForTaskTypeException
	{
		manager = new RepositoryManager();
		userType = new UserType("");
		user = new User("John",userType);
		owner = new User("Bart",userType);
		controller = new InvitationController(manager);
		GregorianCalendar startDate = new GregorianCalendar();
		GregorianCalendar endDate = new GregorianCalendar();
		endDate.add(Calendar.DAY_OF_YEAR, 4);
		// 4 days to finish the task from now on
		int duration = 1;
		
		ArrayList<UserType> userTypes = new ArrayList<UserType>();
		userTypes.add(user.getType());
		
		ArrayList<TaskTypeConstraint> constraints = new ArrayList<TaskTypeConstraint>();
		constraints.add(new TaskTypeConstraint(userType,1,2));
		TaskType taskType = new TaskType("reorganizing the test cases", 
			new ArrayList<Field>(), constraints, userTypes);
		//taskMain = new Task("Main Task",owner,new TaskTimings(startDate,endDate,duration), manager.getClock());
		taskMain = TaskFactory.createTask("Main Task", taskType, new ArrayList<Field>(),
				owner, new TaskTimings(startDate, endDate, duration), manager.getClock());
	
	}
	
	
	@After
	public void tearDown()
	{
		manager = null;
		taskMain = null;
		user = null;
		controller = null;
	}
	
	@Test
	public void createTest() throws AssetAllocatedException, InvitationInvitesOwnerException, IllegalStateCallException, AssetTypeNotRequiredException, AssetConstraintFullException
	{
		Invitation invitation = controller.createInvitation(taskMain,user);
		assertTrue(user.getInvitations().contains(invitation));
	}
	
	@Test(expected=AssetAllocatedException.class)
	public void createTest2() throws AssetAllocatedException, InvitationInvitesOwnerException, IllegalStateCallException, AssetTypeNotRequiredException, AssetConstraintFullException
	{
		User user2 = new User("Jack",userType);
		controller.createInvitation(taskMain,user2);
		controller.createInvitation(taskMain,user2);
	}
	
	@Test(expected=InvitationInvitesOwnerException.class)
	public void createTest3() throws AssetAllocatedException, InvitationInvitesOwnerException, IllegalStateCallException, AssetTypeNotRequiredException, AssetConstraintFullException
	{
		controller.createInvitation(taskMain,owner);
	}
	
	@Test
	public void removeTest() throws AssetAllocatedException, InvitationInvitesOwnerException, IllegalStateCallException, AssetTypeNotRequiredException, AssetConstraintFullException
	{
		User user2 = new User("Jack",userType);
		Invitation invitation = controller.createInvitation(taskMain,user2);
		assertTrue(user2.getInvitations().contains(invitation));
		controller.removeInvitation(invitation);
		assertFalse(user2.getInvitations().contains(invitation));
	}
	
	@Test
	public void acceptInvitation() throws Exception
	{
		Invitation invitation = controller.createInvitation(taskMain,user);
		controller.acceptInvitation(invitation);
		assertEquals(invitation.getState(), InvitationState.ACCEPTED);
	}
	
	@Test(expected=InvitationNotPendingException.class)
	public void acceptInvitation2() throws Exception
	{
		Invitation invitation = controller.createInvitation(taskMain,user);
		controller.acceptInvitation(invitation);
		controller.acceptInvitation(invitation);
	}
	@Test
	public void decline() throws Exception
	{
		Invitation invitation = controller.createInvitation(taskMain,user);
		controller.declineInvitation(invitation);
		assertEquals(invitation.getState(), InvitationState.DECLINED);
	}
	
	@Test(expected=InvitationNotPendingException.class)
	public void decline2() throws Exception
	{
		Invitation invitation = controller.createInvitation(taskMain,user);
		controller.declineInvitation(invitation);
		controller.declineInvitation(invitation);
	}
	
}
