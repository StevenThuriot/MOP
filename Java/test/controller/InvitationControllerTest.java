package controller;
import java.util.Calendar;
import java.util.GregorianCalendar;

import model.Invitation;
import model.Task;
import model.TaskTimings;
import model.User;
import model.Invitation.InvitationState;
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
import exception.InvitationNotPendingException;
import static org.junit.Assert.*;
public class InvitationControllerTest {

	private RepositoryManager manager;
	private Task taskMain;
	private User user;
	private User owner;
	private InvitationController controller;
	@Before
	public void setUp() throws NullPointerException, EmptyStringException, BusinessRule1Exception, IllegalStateCallException, BusinessRule3Exception
	{
		manager = new RepositoryManager();
		user = new User("John");
		owner = new User("Bart");
		controller = new InvitationController(manager);
		GregorianCalendar startDate = new GregorianCalendar();
		GregorianCalendar endDate = new GregorianCalendar();
		endDate.add(Calendar.DAY_OF_YEAR, 4);
		// 4 days to finish the task from now on
		int duration = 1;
		taskMain = new Task("Main Task",owner,new TaskTimings(startDate,endDate,duration), manager.getClock());
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
	public void createTest() throws InvitationExistsException, InvitationInvitesOwnerException
	{
		Invitation invitation = controller.createInvitation(taskMain,user);
		assertTrue(user.getInvitations().contains(invitation));
	}
	
	@Test(expected=InvitationExistsException.class)
	public void createTest2() throws InvitationExistsException, InvitationInvitesOwnerException
	{
		User user2 = new User("Jack");
		controller.createInvitation(taskMain,user2);
		controller.createInvitation(taskMain,user2);
	}
	
	@Test(expected=InvitationInvitesOwnerException.class)
	public void createTest3() throws InvitationExistsException, InvitationInvitesOwnerException
	{
		controller.createInvitation(taskMain,owner);
	}
	
	@Test
	public void removeTest() throws InvitationExistsException, InvitationInvitesOwnerException
	{
		User user2 = new User("Jack");
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
