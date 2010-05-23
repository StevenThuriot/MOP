package model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import model.Project;
import model.Resource;
import model.ResourceType;
import model.Task;
import model.User;
import model.repositories.RepositoryManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import exception.AssetAllocatedException;
import exception.AssetConstraintFullException;
import exception.AssetTypeNotRequiredException;
import exception.BusinessRule1Exception;
import exception.BusinessRule2Exception;
import exception.BusinessRule3Exception;
import exception.DependencyCycleException;
import exception.DependencyException;
import exception.EmptyStringException;
import exception.IllegalStateCallException;
import exception.IllegalStateChangeException;
import exception.InvitationInvitesOwnerException;
import exception.InvitationNotPendingException;
import exception.NoReservationOverlapException;
import exception.NotAvailableException;
import exception.TaskFailedException;
import exception.UnknownStateException;
import exception.WrongFieldsForChosenTypeException;
import exception.WrongUserForTaskTypeException;

import static org.junit.Assert.*;


public class TaskTest {
	/**
	 * Task to be tested
	 */
	private Task task;
	/**
	 * StartDate to be used in the Task objects
	 */
	private GregorianCalendar startDate;
	
	/**
	 * EndDate to be used in the Task objects
	 */
	private GregorianCalendar endDate;
	/**
	 * RepositoryManager to be used
	 */
	private RepositoryManager manager;
	/**
	 * Resource to be added to the Task
	 */
	private Resource resource;
	private ResourceType resourceType;
	private User user;
	
	private TaskType taskType;

	/**
	 * Setting up the test.
	 * @throws BusinessRule3Exception 
	 * @throws IllegalStateCallException 
	 * @throws BusinessRule1Exception 
	 * @throws EmptyStringException 
	 * @throws NullPointerException 
	 * @throws EmptyStringException 
	 * @throws DependencyCycleException 
	 * @throws BusinessRule1Exception 
	 * @throws IllegalStateCallException 
	 * @throws NullPointerException 
	 * @throws BusinessRule3Exception 
	 * @throws WrongFieldsForChosenTypeException 
	 * @throws WrongUserForTaskTypeException 
	 */
	@SuppressWarnings("unchecked")
	@Before
	public void setUp() throws NullPointerException, EmptyStringException, BusinessRule1Exception, IllegalStateCallException, BusinessRule3Exception, WrongFieldsForChosenTypeException, WrongUserForTaskTypeException
	{
		
	    manager = new RepositoryManager();
		user = new User("John",new UserType(""));
		startDate = (GregorianCalendar) manager.getClock().getTime();//Now
		endDate = (GregorianCalendar) manager.getClock().getTime();
		endDate.add(Calendar.DAY_OF_YEAR, 4); // 4 days to finish
		resourceType = new ResourceType("");
        resource = new Resource("Projector", resourceType);
        ArrayList<TaskTypeConstraint> constraints = new ArrayList<TaskTypeConstraint>();
		constraints.add(new TaskTypeConstraint(resourceType,1,2));
        taskType = new TaskType("reorganizing the test cases", 
				new ArrayList<Field>(), constraints);
		task = TaskFactory.createTask("Descr", taskType, new ArrayList<Field>(),
				user, new TaskTimings(startDate, endDate, 120), manager.getClock());
	}
	
	/**
	 * Tearing down variables after each test
	 */
	@After
	public void tearDown()
	{
		user = null;
		task = null;
		startDate = null;
		endDate = null;
	}
	
	/**
	 * Tests the initialization behavior of Task.
	 * @throws EmptyStringException 
	 * @throws IllegalStateCallException 
	 * @throws NullPointerException 
	 * @throws BusinessRule3Exception 
	 * @throws BusinessRule1Exception 
	 * @throws WrongFieldsForChosenTypeException 
	 * @throws WrongUserForTaskTypeException 
	 */
	@SuppressWarnings("unchecked")
	@Test(expected=BusinessRule1Exception.class)
	public void initialization() throws EmptyStringException, NullPointerException, IllegalStateCallException, BusinessRule3Exception, BusinessRule1Exception, WrongFieldsForChosenTypeException, WrongUserForTaskTypeException{
		
		// <task> is initialized with no dependencies or depending tasks
		assertTrue(task.getDependencies().isEmpty());
		assertTrue(task.getDependentTasks().isEmpty());
		// <task> is linked to the user John, and is given the name "Descr"
		assertTrue(task.getOwner() == user);
		assertTrue(task.getDescription().equals("Descr"));
		// <task> should satisfy the business rules
		assertTrue(task.satisfiesBusinessRule1());
		// TODO: other BR's here?
		// <task> is initially unfinished
		assertTrue(task.isUnfinished());
		
		//Try a different task, that fails business rule 1
		endDate = (GregorianCalendar) startDate.clone();
		endDate.add(Calendar.DAY_OF_YEAR,1);
		//One day is available, but the duration is 25 hours. Business Rule exception should be thrown.
		//Task "MOP" can never be completed in time :-(
		@SuppressWarnings("unused")
		Task impossibleTask = TaskFactory.createTask("MOP", taskType, new ArrayList<Field>(),
				user, new TaskTimings(startDate, endDate, 1500), manager.getClock());
	}
	
	/**
	 * Testing setting the state to failed
	 * @throws DependencyException 
	 * @throws IllegalStateChangeException 
	 * @throws AssetConstraintFullException 
	 * @throws AssetTypeNotRequiredException 
	 * @throws IllegalStateCallException 
	 * @throws AssetAllocatedException 
	 * @throws NoReservationOverlapException 
	 * @throws NotAvailableException 
	 */
	@Test
	public void checkStateZero() throws IllegalStateChangeException, NotAvailableException, NoReservationOverlapException, AssetAllocatedException, IllegalStateCallException, AssetTypeNotRequiredException, AssetConstraintFullException
	{
		@SuppressWarnings("unused")
		Reservation r = new Reservation(manager.getClock().getTime(),120,resource, task);
		assertEquals("Available", task.getCurrentStateName());
	}

	/**
	 * Testing setting the state to Successful
	 * @throws DependencyException 
	 * @throws IllegalStateChangeException 
	 * @throws BusinessRule3Exception 
	 * @throws BusinessRule2Exception 
	 * @throws AssetConstraintFullException 
	 * @throws AssetTypeNotRequiredException 
	 * @throws IllegalStateCallException 
	 * @throws AssetAllocatedException 
	 * @throws NoReservationOverlapException 
	 * @throws NotAvailableException 
	 */
	@Test
	public void checkStateOne() throws IllegalStateChangeException, BusinessRule2Exception, BusinessRule3Exception, NotAvailableException, NoReservationOverlapException, AssetAllocatedException, IllegalStateCallException, AssetTypeNotRequiredException, AssetConstraintFullException
	{
		@SuppressWarnings("unused")
		Reservation r = new Reservation(manager.getClock().getTime(),120,resource, task);
		task.setSuccessful();
		assertEquals("Successful", task.getCurrentStateName());
	}
	
	/**
	 * Testing setting the state to failed
	 * @throws DependencyException 
	 * @throws IllegalStateChangeException 
	 */
	@Test
	public void checkStateTwo() throws IllegalStateChangeException
	{
		task.setFailed();
		assertEquals("Failed", task.getCurrentStateName());
	}

	
	/**
	 * Testing setting the state to successful when failed
	 * @throws IllegalStateChangeException 
	 * @throws DependencyException 
	 * @throws IllegalStateChangeException 
	 * @throws BusinessRule3Exception 
	 * @throws BusinessRule2Exception 
	 */
	@Test(expected=IllegalStateChangeException.class)
	public void checkStateThree() throws IllegalStateChangeException, BusinessRule2Exception, BusinessRule3Exception
	{
		task.setFailed();
		task.setSuccessful();
	}

	
	/**
	 * Testing setting the state to failed when successful
	 * @throws IllegalStateChangeException 
	 * @throws DependencyException 
	 * @throws IllegalStateChangeException 
	 * @throws BusinessRule3Exception 
	 * @throws BusinessRule2Exception 
	 */
	@Test(expected=IllegalStateChangeException.class)
	public void checkStateFour() throws IllegalStateChangeException, BusinessRule2Exception, BusinessRule3Exception
	{
		task.setSuccessful();
		task.setFailed();
	}

	
	/**
	 * Testing if the task is unfinished
	 * @throws IllegalStateChangeException 
	 * @throws DependencyException 
	 * @throws IllegalStateChangeException 
	 */
	@Test
	public void checkStateFive() throws IllegalStateChangeException
	{
		assertEquals(true, task.isUnfinished());
		assertEquals(false, task.isFailed());
		assertEquals(false, task.isSuccesful());
	}

	
	/**
	 * Testing if the task is successful
	 * @throws IllegalStateChangeException 
	 * @throws DependencyException 
	 * @throws IllegalStateChangeException 
	 * @throws BusinessRule3Exception 
	 * @throws BusinessRule2Exception 
	 * @throws AssetConstraintFullException 
	 * @throws AssetTypeNotRequiredException 
	 * @throws IllegalStateCallException 
	 * @throws AssetAllocatedException 
	 * @throws NoReservationOverlapException 
	 * @throws NotAvailableException 
	 */
	@Test
	public void checkStateSix() throws IllegalStateChangeException, BusinessRule2Exception, BusinessRule3Exception, NotAvailableException, NoReservationOverlapException, AssetAllocatedException, IllegalStateCallException, AssetTypeNotRequiredException, AssetConstraintFullException
	{
		@SuppressWarnings("unused")
		Reservation r = new Reservation(manager.getClock().getTime(),120,resource, task);
		task.setSuccessful();
		
		assertEquals(false, task.isUnfinished());
		assertEquals(false, task.isFailed());
		assertEquals(true, task.isSuccesful());
	}

	
	/**
	 * Testing if the task is failed
	 * @throws IllegalStateChangeException 
	 * @throws DependencyException 
	 * @throws IllegalStateChangeException 
	 */
	@Test
	public void checkStateSeven() throws IllegalStateChangeException
	{
		task.setFailed();
		
		assertEquals(false, task.isUnfinished());
		assertEquals(true, task.isFailed());
		assertEquals(false, task.isSuccesful());
	}

	
	/**
	 * Testing if the task satisfies business rule 2
	 * @throws IllegalStateChangeException 
	 * @throws DependencyException 
	 * @throws IllegalStateChangeException 
	 * @throws BusinessRule3Exception 
	 * @throws IllegalStateCallException 
	 * @throws BusinessRule1Exception 
	 * @throws EmptyStringException 
	 * @throws NullPointerException 
	 * @throws DependencyCycleException 
	 * @throws BusinessRule2Exception 
	 * @throws WrongFieldsForChosenTypeException 
	 * @throws WrongUserForTaskTypeException 
	 * @throws AssetConstraintFullException 
	 * @throws AssetTypeNotRequiredException 
	 * @throws AssetAllocatedException 
	 * @throws NoReservationOverlapException 
	 * @throws NotAvailableException 
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void checkStateEight() throws IllegalStateChangeException, NullPointerException, EmptyStringException, BusinessRule1Exception, IllegalStateCallException, BusinessRule3Exception, DependencyCycleException, BusinessRule2Exception, WrongFieldsForChosenTypeException, WrongUserForTaskTypeException, NotAvailableException, NoReservationOverlapException, AssetAllocatedException, AssetTypeNotRequiredException, AssetConstraintFullException
	{
		Task task2 = TaskFactory.createTask("some name", taskType, new ArrayList<Field>(),
				user, new TaskTimings(startDate, endDate, 50), manager.getClock());
		@SuppressWarnings("unused")
		Reservation r = new Reservation(manager.getClock().getTime(),120,resource, task2);

		task2.setSuccessful();
		task.addDependency(task2);
		
		assertEquals(true, task.satisfiesBusinessRule2());
	}
	
	/**
	 * Testing setting the state to Successful using the parser
	 * @throws DependencyException 
	 * @throws IllegalStateChangeException 
	 * @throws UnknownStateException 
	 * @throws BusinessRule3Exception 
	 * @throws BusinessRule2Exception 
	 * @throws AssetConstraintFullException 
	 * @throws AssetTypeNotRequiredException 
	 * @throws IllegalStateCallException 
	 * @throws AssetAllocatedException 
	 * @throws NoReservationOverlapException 
	 * @throws NotAvailableException 
	 */
	@Test
	public void checkStateNine() throws IllegalStateChangeException, UnknownStateException, BusinessRule2Exception, BusinessRule3Exception, NotAvailableException, NoReservationOverlapException, AssetAllocatedException, IllegalStateCallException, AssetTypeNotRequiredException, AssetConstraintFullException
	{
		@SuppressWarnings("unused")
		Reservation r = new Reservation(manager.getClock().getTime(),120,resource, task);
		task.parseStateString("Successful");
		assertEquals("Successful", task.getCurrentStateName());
	}
	
	/**
	 * Testing setting the state to failed using the parser
	 * @throws DependencyException 
	 * @throws IllegalStateChangeException 
	 * @throws UnknownStateException 
	 * @throws BusinessRule3Exception 
	 * @throws BusinessRule2Exception 
	 */
	@Test
	public void checkStateTen() throws IllegalStateChangeException, UnknownStateException, BusinessRule2Exception, BusinessRule3Exception
	{
		task.parseStateString("Failed");
		assertEquals("Failed", task.getCurrentStateName());
	}
	
	/**
	 * Testing setting the state to failed using the parser
	 * @throws DependencyException 
	 * @throws IllegalStateChangeException 
	 * @throws UnknownStateException 
	 * @throws UnknownStateException 
	 * @throws BusinessRule3Exception 
	 * @throws BusinessRule2Exception 
	 */
	@Test(expected=UnknownStateException.class)
	public void checkStateEleven() throws IllegalStateChangeException, UnknownStateException, BusinessRule2Exception, BusinessRule3Exception
	{
		task.parseStateString("Trogdor the Burninator");
	}
	
	/**
	 * Testing default canBeExecuted
	 * @throws DependencyException 
	 * @throws IllegalStateChangeException 
	 * @throws UnknownStateException 
	 */
	@Test
	public void checkStateTwelve() throws IllegalStateChangeException
	{
		task.setFailed();
		assertEquals(false, task.canBeExecuted());
	}
	
	/**
	 * Testing default setDescription
	 * @throws DependencyException 
	 * @throws IllegalStateChangeException 
	 * @throws IllegalStateCallException 
	 * @throws EmptyStringException 
	 * @throws NullPointerException 
	 * @throws UnknownStateException 
	 */
	@Test(expected=IllegalStateCallException.class)
	public void checkStateThirteen() throws IllegalStateChangeException, NullPointerException, EmptyStringException, IllegalStateCallException
	{
		task.setFailed();
		task.setDescription("Trogdor Rules!");
	}
	
	/**
	 * Testing canBeExecuted when succesful
	 * @throws DependencyException 
	 * @throws IllegalStateChangeException 
	 * @throws IllegalStateCallException 
	 * @throws EmptyStringException 
	 * @throws NullPointerException 
	 * @throws BusinessRule3Exception 
	 * @throws BusinessRule2Exception 
	 * @throws AssetConstraintFullException 
	 * @throws AssetTypeNotRequiredException 
	 * @throws AssetAllocatedException 
	 * @throws NoReservationOverlapException 
	 * @throws NotAvailableException 
	 * @throws UnknownStateException 
	 */
	@Test
	public void checkStateFourteen() throws IllegalStateChangeException, NullPointerException, EmptyStringException, IllegalStateCallException, BusinessRule3Exception, BusinessRule2Exception, NotAvailableException, NoReservationOverlapException, AssetAllocatedException, AssetTypeNotRequiredException, AssetConstraintFullException
	{
		@SuppressWarnings("unused")
		Reservation r = new Reservation(manager.getClock().getTime(),120,resource, task);
		task.setSuccessful();
		assertEquals(false, task.canBeExecuted());
	}
	
	/**
	 * Testing default implementation for add dependancy
	 * @throws IllegalStateChangeException 
	 * @throws DependencyException 
	 * @throws IllegalStateChangeException 
	 * @throws IllegalStateCallException 
	 * @throws IllegalStateCallException 
	 * @throws BusinessRule3Exception 
	 * @throws EmptyStringException 
	 * @throws DependencyCycleException 
	 * @throws BusinessRule1Exception 
	 * @throws NullPointerException 
	 * @throws EmptyStringException 
	 * @throws NullPointerException 
	 * @throws BusinessRule3Exception 
	 * @throws BusinessRule2Exception 
	 * @throws WrongFieldsForChosenTypeException 
	 * @throws WrongUserForTaskTypeException 
	 * @throws AssetConstraintFullException 
	 * @throws AssetTypeNotRequiredException 
	 * @throws AssetAllocatedException 
	 * @throws NoReservationOverlapException 
	 * @throws NotAvailableException 
	 * @throws NotAvailableException 
	 * @throws UnknownStateException 
	 */
	@SuppressWarnings("unchecked")
	@Test(expected=IllegalStateCallException.class)
	public void checkStateSeventeen() throws IllegalStateChangeException, IllegalStateCallException, NullPointerException, BusinessRule1Exception, DependencyCycleException, EmptyStringException, BusinessRule3Exception, BusinessRule2Exception, WrongFieldsForChosenTypeException, WrongUserForTaskTypeException, NotAvailableException, NoReservationOverlapException, AssetAllocatedException, AssetTypeNotRequiredException, AssetConstraintFullException 
	{
		@SuppressWarnings("unused")
		Reservation r = new Reservation(manager.getClock().getTime(),120,resource, task);
		task.setSuccessful();
		Task temp = TaskFactory.createTask("some name", taskType, new ArrayList<Field>(),
				user, new TaskTimings(startDate, endDate, 50), manager.getClock());
		task.addDependency(temp);
	}
	
	/**
	 * Testing default implementation for add dependancy
	 * @throws IllegalStateChangeException 
	 * @throws DependencyException 
	 * @throws IllegalStateChangeException 
	 * @throws IllegalStateCallException 
	 * @throws IllegalStateCallException 
	 * @throws BusinessRule3Exception 
	 * @throws EmptyStringException 
	 * @throws DependencyCycleException 
	 * @throws BusinessRule1Exception 
	 * @throws NullPointerException 
	 * @throws EmptyStringException 
	 * @throws NullPointerException 
	 * @throws BusinessRule3Exception 
	 * @throws DependencyException 
	 * @throws BusinessRule2Exception 
	 * @throws WrongFieldsForChosenTypeException 
	 * @throws WrongUserForTaskTypeException 
	 * @throws AssetConstraintFullException 
	 * @throws AssetTypeNotRequiredException 
	 * @throws AssetAllocatedException 
	 * @throws NoReservationOverlapException 
	 * @throws NotAvailableException 
	 * @throws NotAvailableException 
	 * @throws UnknownStateException 
	 */
	@SuppressWarnings("unchecked")
	@Test(expected=IllegalStateCallException.class)
	public void checkStateNineteen() throws IllegalStateChangeException, IllegalStateCallException, NullPointerException, BusinessRule1Exception, DependencyCycleException, EmptyStringException, BusinessRule3Exception, DependencyException, BusinessRule2Exception, WrongFieldsForChosenTypeException, WrongUserForTaskTypeException, NotAvailableException, NoReservationOverlapException, AssetAllocatedException, AssetTypeNotRequiredException, AssetConstraintFullException 
	{
		Task task2 = TaskFactory.createTask("some name", taskType, new ArrayList<Field>(),
				user, new TaskTimings(startDate, endDate, 50), manager.getClock());
		
		task.addDependency(task2);
		@SuppressWarnings("unused")
		Reservation r = new Reservation(manager.getClock().getTime(),120,resource, task2);

		task2.setSuccessful();
		task.setSuccessful();
		task.removeDependency(task2);
	}
	
	
	/**
	 * Testing setDescription
	 * @throws NullPointerException
	 * @throws EmptyStringException
	 * @throws IllegalStateCallException
	 */
	@Test(expected=NullPointerException.class)
	public void checkStateTwenty() throws NullPointerException, EmptyStringException, IllegalStateCallException 
	{
		task.setDescription(null);
	}
	
	
	/**
	 * Testing setdescription
	 * @throws NullPointerException
	 * @throws EmptyStringException
	 * @throws IllegalStateCallException
	 */
	@Test(expected=EmptyStringException.class)
	public void checkStateTwentyOne() throws NullPointerException, EmptyStringException, IllegalStateCallException 
	{
		task.setDescription("");
	}
	
	/**
	 * Failed always satisfies rule 2
	 * @throws NullPointerException
	 * @throws EmptyStringException
	 * @throws IllegalStateCallException
	 * @throws IllegalStateChangeException
	 */
	@Test
	public void checkStateTwentyTwo() throws NullPointerException, EmptyStringException, IllegalStateCallException, IllegalStateChangeException 
	{
		task.setFailed();
		assertEquals(true, task.satisfiesBusinessRule2());
	}
	
	/**
	 * Failed always satisfies rule 3
	 * @throws NullPointerException
	 * @throws EmptyStringException
	 * @throws IllegalStateCallException
	 * @throws IllegalStateChangeException
	 */
	@Test
	public void checkStateTwentyThree() throws NullPointerException, EmptyStringException, IllegalStateCallException, IllegalStateChangeException 
	{
		task.setFailed();
		assertEquals(true, task.satisfiesBusinessRule3());
	}

	
	/**
	 * Checking for state possibilities
	 */
	@Test
	public void checkStateTwentyFour() 
	{
		ArrayList<String> list = new ArrayList<String>();

		list.add("Successful");
		list.add("Failed");
		
		assertEquals(list, task.getPossibleStateChanges());
	}
	
	/**
	 * Checking for state possibilities while failed
	 * @throws IllegalStateChangeException 
	 */
	@Test
	public void checkStateTwentyFive() throws IllegalStateChangeException 
	{
		ArrayList<String> list = new ArrayList<String>();
		
		task.setFailed();
		
		assertEquals(list, task.getPossibleStateChanges());
	}
	
	/**
	 * Checking for state possibilities while successful
	 * @throws IllegalStateChangeException 
	 * @throws BusinessRule3Exception 
	 * @throws BusinessRule2Exception 
	 * @throws AssetConstraintFullException 
	 * @throws AssetTypeNotRequiredException 
	 * @throws IllegalStateCallException 
	 * @throws AssetAllocatedException 
	 * @throws NoReservationOverlapException 
	 * @throws NotAvailableException 
	 */
	@Test
	public void checkStateTwentySix() throws IllegalStateChangeException, BusinessRule2Exception, BusinessRule3Exception, NotAvailableException, NoReservationOverlapException, AssetAllocatedException, IllegalStateCallException, AssetTypeNotRequiredException, AssetConstraintFullException 
	{
		ArrayList<String> list = new ArrayList<String>();
		
		@SuppressWarnings("unused")
		Reservation r = new Reservation(manager.getClock().getTime(),120,resource, task);
		task.setSuccessful();
		
		assertEquals(list, task.getPossibleStateChanges());
	}
	
	/**
	 * Testing default implementation for add dependancy
	 * @throws IllegalStateChangeException 
	 * @throws DependencyException 
	 * @throws IllegalStateChangeException 
	 * @throws IllegalStateCallException 
	 * @throws IllegalStateCallException 
	 * @throws BusinessRule3Exception 
	 * @throws EmptyStringException 
	 * @throws DependencyCycleException 
	 * @throws BusinessRule1Exception 
	 * @throws NullPointerException 
	 * @throws EmptyStringException 
	 * @throws NullPointerException 
	 * @throws BusinessRule3Exception 
	 * @throws DependencyException 
	 * @throws BusinessRule2Exception 
	 * @throws WrongFieldsForChosenTypeException 
	 * @throws AssetAllocatedException 
	 * @throws NoReservationOverlapException 
	 * @throws NotAvailableException 
	 * @throws NotAvailableException 
	 * @throws AssetTypeNotRequiredException 
	 * @throws AssetConstraintFullException 
	 * @throws UnknownStateException 
	 */
	@Test(expected=IllegalStateCallException.class)
	public void checkStateTwentySeven() throws IllegalStateChangeException, IllegalStateCallException, NullPointerException, BusinessRule1Exception, DependencyCycleException, EmptyStringException, BusinessRule3Exception, DependencyException, BusinessRule2Exception, WrongFieldsForChosenTypeException, NotAvailableException, NoReservationOverlapException, AssetAllocatedException, AssetTypeNotRequiredException, AssetConstraintFullException 
	{
		@SuppressWarnings("unused")
		Reservation r = new Reservation(manager.getClock().getTime(),120,resource, task);
		task.setSuccessful();
		new Reservation(startDate, 180, resource, task);
	}
	
	/**
	 * Test to see if description is set properly
	 * @throws EmptyStringException
	 * @throws IllegalStateCallException 
	 * @throws NullPointerException 
	 */
	@Test
	public void updateTaskDescription() throws EmptyStringException, NullPointerException, IllegalStateCallException
	{
		task.setDescription("ABC123");
		assertEquals("ABC123", task.getDescription());
	}
	
	/**
	 * Testing a task attaching to a project
	 * @throws EmptyStringException 
	 */
	@Test
	public void assignTaskProject() throws EmptyStringException
	{
		Project p = new Project("Proj");
		p.bindTask(task);
		
		assertTrue(p.getTasks().contains(task));
	}
	
	/**
	 * Tests on dependencies. Tests a proper dependency
	 * @throws IllegalStateCallException 
	 * @throws NullPointerException 
	 * @throws BusinessRule3Exception 
	 * @throws WrongFieldsForChosenTypeException 
	 * @throws WrongUserForTaskTypeException 
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void dependencies1() throws EmptyStringException, BusinessRule1Exception, DependencyCycleException, DependencyException, NullPointerException, IllegalStateCallException, BusinessRule3Exception, WrongFieldsForChosenTypeException, WrongUserForTaskTypeException{
		// Try a proper dependency
		Task task2 = TaskFactory.createTask("some name", taskType, new ArrayList<Field>(),
				user, new TaskTimings(startDate, endDate, 50), manager.getClock());
		task.addDependency(task2);
		// Assure that it is properly initialized
		// For more details, see TaskDependencyManager and corresponding test class
		assertTrue(task.dependsOn(task2));
		assertTrue(task2.getDependentTasks().contains(task));
		//Remove it again
		task.removeDependency(task2);
		assertFalse(task.dependsOn(task2));
		assertFalse(task2.getDependentTasks().contains(task2));
	}
	
	/**
	 * Another test on dependencies. Tests a dependency that fails to satisfy business rule 1.
	 * @throws DependencyCycleException 
	 * @throws BusinessRule1Exception 
	 * @throws EmptyStringException 
	 * @throws IllegalStateCallException 
	 * @throws NullPointerException 
	 * @throws BusinessRule3Exception 
	 * @throws WrongFieldsForChosenTypeException 
	 * @throws WrongUserForTaskTypeException 
	 */
	@SuppressWarnings("unchecked")
	@Test(expected=BusinessRule1Exception.class)
	public void dependencies2() throws DependencyCycleException, EmptyStringException, BusinessRule1Exception, NullPointerException, IllegalStateCallException, BusinessRule3Exception, WrongFieldsForChosenTypeException, WrongUserForTaskTypeException{
		startDate = new GregorianCalendar();
		startDate.add(Calendar.DAY_OF_YEAR, 4);
		endDate = new GregorianCalendar();
		endDate.add(Calendar.DAY_OF_YEAR, 5);
		Task task2 = TaskFactory.createTask("some name", taskType, new ArrayList<Field>(),
				user, new TaskTimings(startDate, endDate, 1380), manager.getClock());
		// <task2> starts after 3 days and takes 23 hours. <task> takes another 2 hours
		// This dependency will not satisfy business rule 1
		task.addDependency(task2);		
	}
	
	/**
	 * Tests the behavior of Task.earliestEndTime()
	 * @throws TaskFailedException 
	 * @throws BusinessRule1Exception 
	 * @throws EmptyStringException 
	 * @throws DependencyCycleException 
	 * @throws IllegalStateCallException 
	 * @throws NullPointerException 
	 * @throws BusinessRule3Exception 
	 * @throws WrongFieldsForChosenTypeException 
	 * @throws WrongUserForTaskTypeException 
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void earliestEnd() throws TaskFailedException, EmptyStringException, BusinessRule1Exception, DependencyCycleException, NullPointerException, IllegalStateCallException, BusinessRule3Exception, WrongFieldsForChosenTypeException, WrongUserForTaskTypeException{
		//<task> takes two hours to complete, earliest end time is 2 hours after the start date
		GregorianCalendar earliestEnd = (GregorianCalendar) manager.getClock().getTime().clone();
		earliestEnd.add(Calendar.HOUR, 2);
		assertEquals(earliestEnd, task.earliestEndTime());
		
		startDate = (GregorianCalendar) manager.getClock().getTime().clone();
		startDate.add(Calendar.DAY_OF_YEAR, 1);
		endDate = (GregorianCalendar) manager.getClock().getTime().clone();
		endDate.add(Calendar.DAY_OF_YEAR, 5);
		Task task2 = TaskFactory.createTask("some name", taskType, new ArrayList<Field>(),
				user, new TaskTimings(startDate, endDate, 1440), manager.getClock());
		task.addDependency(task2);
		//<task2> takes 24 hours to complete, <task> takes another 2.
		// Earliest end time should be 26 hours after the startDate of <task2>
		earliestEnd = startDate;
		earliestEnd.add(Calendar.HOUR, 26);
		assertEquals(earliestEnd, task.earliestEndTime());		
	}
	
	/**
	 * Tests the behavior of the non-recursive remove method.
	 * @throws BusinessRule1Exception 
	 * @throws EmptyStringException 
	 * @throws DependencyCycleException 
	 * @throws IllegalStateCallException 
	 * @throws NullPointerException 
	 * @throws BusinessRule3Exception 
	 * @throws AssetAllocatedException 
	 * @throws NoReservationOverlapException 
	 * @throws NotAvailableException 
	 * @throws WrongFieldsForChosenTypeException 
	 * @throws WrongUserForTaskTypeException 
	 * @throws AssetTypeNotRequiredException 
	 * @throws AssetConstraintFullException 
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void remove() throws EmptyStringException, BusinessRule1Exception, DependencyCycleException, NullPointerException, IllegalStateCallException, BusinessRule3Exception, NotAvailableException, NoReservationOverlapException, AssetAllocatedException, WrongFieldsForChosenTypeException, WrongUserForTaskTypeException, AssetTypeNotRequiredException, AssetConstraintFullException{
		//Sets up a resource reservation, and a dependency in both directions
		assertTrue(task.isUnfinished());
		Reservation reservation = new Reservation(startDate, 120, resource, task);
		Task task2 = TaskFactory.createTask("some dependency", taskType, new ArrayList<Field>(),
				user, new TaskTimings(startDate, endDate, 120), manager.getClock());
		Task task3 = TaskFactory.createTask("some dependentTask", taskType, new ArrayList<Field>(),
				user, new TaskTimings(startDate, endDate, 120), manager.getClock());
		task.addDependency(task2);
		task3.addDependency(task);
		// remove the task
		task.remove();
		//Assures all links are broken
		assertFalse(task3.getDependencies().contains(task));
		assertFalse(task2.getDependentTasks().contains(task));
		assertFalse(user.getTasks().contains(task));
		assertFalse(resource.getReservations().contains(reservation));
	}
	
	/**
	 * Tests the behavior of the recursive remove method.
	 * @throws EmptyStringException 
	 * @throws BusinessRule1Exception 
	 * @throws DependencyCycleException 
	 * @throws IllegalStateCallException 
	 * @throws NullPointerException 
	 * @throws BusinessRule3Exception 
	 * @throws AssetAllocatedException 
	 * @throws NoReservationOverlapException 
	 * @throws NotAvailableException 
	 * @throws WrongFieldsForChosenTypeException 
	 * @throws WrongUserForTaskTypeException 
	 * @throws AssetTypeNotRequiredException 
	 * @throws AssetConstraintFullException 
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void removeRecursively() throws EmptyStringException, BusinessRule1Exception, DependencyCycleException, NullPointerException, IllegalStateCallException, BusinessRule3Exception, NotAvailableException, NoReservationOverlapException, AssetAllocatedException, WrongFieldsForChosenTypeException, WrongUserForTaskTypeException, AssetTypeNotRequiredException, AssetConstraintFullException{
		Reservation reservation = new Reservation(startDate, 120, resource, task);
		//Sets up 2 additional resources
		Resource resource2 = new Resource("some resource",resourceType);
		Resource resource3 = new Resource("some other resource",resourceType);
		//Sets up 3 additional tasks
		Task task2 = TaskFactory.createTask("some dependency", taskType, new ArrayList<Field>(),
				user, new TaskTimings(startDate, endDate, 120), manager.getClock());
		Reservation reservation2 = new Reservation(startDate, 120, resource2, task2);
		Task task3 = TaskFactory.createTask("some dependentTask", taskType, new ArrayList<Field>(),
				user, new TaskTimings(startDate, endDate, 120), manager.getClock());
		Reservation reservation3 = new Reservation(startDate, 120, resource3, task3);
		Task task4 = TaskFactory.createTask("some other task", taskType, new ArrayList<Field>(),
				user, new TaskTimings(startDate, endDate, 120), manager.getClock());
		task3.addDependency(task4);
		//Sets up a hierarchy of 3 levels
		task3.addDependency(task2);
		task2.addDependency(task);
		//Removes <task> recursively
		task.removeRecursively();
		//Checks that the user has no link to any of the first 3 tasks anymore
		assertFalse(user.getTasks().contains(task));
		assertFalse(user.getTasks().contains(task2));
		assertFalse(user.getTasks().contains(task3));
		//Check that <task4> is not deleted
		assertTrue(user.getTasks().contains(task4));
		//Check that the resource are no longer required, and that task4 has no reference to task3 anymore
		assertFalse(resource.getReservations().contains(reservation));
		assertFalse(resource2.getReservations().contains(reservation2));
		assertFalse(resource3.getReservations().contains(reservation3));
		assertFalse(task4.getDependentTasks().contains(task3));
	}
	
	/**
	 * Testing if EarliestEndTime throws the error if failed
	 * @throws IllegalStateChangeException 
	 * @throws TaskFailedException 
	 */
	@Test(expected=TaskFailedException.class)
	public void testEarliestEndTime() throws IllegalStateChangeException, TaskFailedException
	{
		task.setFailed();
		task.earliestEndTime();
	}
	
	/**
	 * Testing if setDueDate throws the error if failed
	 * @throws NullPointerException
	 * @throws BusinessRule3Exception 
	 */
	@Test(expected=NullPointerException.class)
	public void testSetDueDate() throws NullPointerException, BusinessRule3Exception
	{
		task.setDueDate(null);
	}
	
	/**
	 * Testing if setStartDate throws the error if failed
	 * @throws NullPointerException
	 * @throws BusinessRule3Exception 
	 */
	@Test(expected=NullPointerException.class)
	public void testSetStartDate() throws NullPointerException, BusinessRule3Exception
	{
		task.setStartDate(null);
	}
	
	/**
	 * Testing if setUser throws the error if failed
	 * @throws NullPointerException
	 */
	@Test(expected=NullPointerException.class)
	public void testSetUser() throws NullPointerException
	{
		task.setUser(null);
	}

	/**
	 * Testing toString
	 */
	@Test
	public void testToString()
	{
		assertEquals("Descr", task.toString());
	}

	/**
	 * Testing DurationComparator
	 * @throws BusinessRule3Exception 
	 * @throws IllegalStateCallException 
	 * @throws BusinessRule1Exception 
	 * @throws EmptyStringException 
	 * @throws NullPointerException 
	 * @throws WrongFieldsForChosenTypeException 
	 * @throws WrongUserForTaskTypeException 
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testDurationComparator() throws NullPointerException, EmptyStringException, BusinessRule1Exception, IllegalStateCallException, BusinessRule3Exception, WrongFieldsForChosenTypeException, WrongUserForTaskTypeException
	{
		Task t1 = TaskFactory.createTask("d", taskType, new ArrayList<Field>(),
				user, new TaskTimings(startDate, endDate, 1), manager.getClock());
		Task t2 = TaskFactory.createTask("d", taskType, new ArrayList<Field>(),
				user, new TaskTimings(startDate, endDate, 2), manager.getClock());
		
		TaskDurationComparator c = new TaskDurationComparator();
		assertEquals(-1, c.compare(t1, t2));
	}

	/**
	 * Testing DurationComparator
	 * @throws BusinessRule3Exception 
	 * @throws IllegalStateCallException 
	 * @throws BusinessRule1Exception 
	 * @throws EmptyStringException 
	 * @throws NullPointerException 
	 * @throws WrongFieldsForChosenTypeException 
	 * @throws WrongUserForTaskTypeException 
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testDurationComparator2() throws NullPointerException, EmptyStringException, BusinessRule1Exception, IllegalStateCallException, BusinessRule3Exception, WrongFieldsForChosenTypeException, WrongUserForTaskTypeException
	{
		Task t1 = TaskFactory.createTask("d", taskType, new ArrayList<Field>(),
				user, new TaskTimings(startDate, endDate, 2), manager.getClock());
		Task t2 = TaskFactory.createTask("d", taskType, new ArrayList<Field>(),
				user, new TaskTimings(startDate, endDate, 1), manager.getClock());
		
		TaskDurationComparator c = new TaskDurationComparator();
		assertEquals(1, c.compare(t1, t2));
	}

	/**
	 * Testing DurationComparator
	 * @throws BusinessRule3Exception 
	 * @throws IllegalStateCallException 
	 * @throws BusinessRule1Exception 
	 * @throws EmptyStringException 
	 * @throws NullPointerException 
	 * @throws WrongFieldsForChosenTypeException 
	 * @throws WrongUserForTaskTypeException 
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testDurationComparator3() throws NullPointerException, EmptyStringException, BusinessRule1Exception, IllegalStateCallException, BusinessRule3Exception, WrongFieldsForChosenTypeException, WrongUserForTaskTypeException
	{
		Task t1 = TaskFactory.createTask("d", taskType, new ArrayList<Field>(),
				user, new TaskTimings(startDate, endDate, 1), manager.getClock());
		Task t2 = TaskFactory.createTask("d", taskType, new ArrayList<Field>(),
				user, new TaskTimings(startDate, endDate, 1), manager.getClock());
		
		TaskDurationComparator c = new TaskDurationComparator();
		assertEquals(0, c.compare(t1, t2));
	}

	/**
	 * Testing DeadlineComparator
	 * @throws BusinessRule3Exception 
	 * @throws IllegalStateCallException 
	 * @throws BusinessRule1Exception 
	 * @throws EmptyStringException 
	 * @throws NullPointerException 
	 * @throws WrongFieldsForChosenTypeException 
	 * @throws WrongUserForTaskTypeException 
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testDeadlineComparator() throws NullPointerException, EmptyStringException, BusinessRule1Exception, IllegalStateCallException, BusinessRule3Exception, WrongFieldsForChosenTypeException, WrongUserForTaskTypeException
	{
		GregorianCalendar end2 = (GregorianCalendar) endDate.clone();
		
		Task t1 = TaskFactory.createTask("d", taskType, new ArrayList<Field>(),
				user, new TaskTimings(startDate, endDate, 1), manager.getClock());
		Task t2 = TaskFactory.createTask("d", taskType, new ArrayList<Field>(),
				user, new TaskTimings(startDate, end2, 1), manager.getClock());
		
		TaskDeadlineComparator c = new TaskDeadlineComparator();
		assertEquals(0, c.compare(t1, t2));
	}

	/**
	 * Testing DeadlineComparator
	 * @throws BusinessRule3Exception 
	 * @throws IllegalStateCallException 
	 * @throws BusinessRule1Exception 
	 * @throws EmptyStringException 
	 * @throws NullPointerException 
	 * @throws WrongFieldsForChosenTypeException 
	 * @throws WrongUserForTaskTypeException 
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testDeadlineComparator2() throws NullPointerException, EmptyStringException, BusinessRule1Exception, IllegalStateCallException, BusinessRule3Exception, WrongFieldsForChosenTypeException, WrongUserForTaskTypeException
	{
		GregorianCalendar end2 = (GregorianCalendar) endDate.clone();
		end2.add(Calendar.DAY_OF_WEEK, 1);
		
		Task t1 = TaskFactory.createTask("d", taskType, new ArrayList<Field>(),
				user, new TaskTimings(startDate, endDate, 1), manager.getClock());
		Task t2 = TaskFactory.createTask("d", taskType, new ArrayList<Field>(),
				user, new TaskTimings(startDate, end2, 1), manager.getClock());
		
		TaskDeadlineComparator c = new TaskDeadlineComparator();
		assertEquals(-1, c.compare(t1, t2));
	}

	/**
	 * Testing DeadlineComparator
	 * @throws BusinessRule3Exception 
	 * @throws IllegalStateCallException 
	 * @throws BusinessRule1Exception 
	 * @throws EmptyStringException 
	 * @throws NullPointerException 
	 * @throws WrongFieldsForChosenTypeException 
	 * @throws WrongUserForTaskTypeException 
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testDeadlineComparator3() throws NullPointerException, EmptyStringException, BusinessRule1Exception, IllegalStateCallException, BusinessRule3Exception, WrongFieldsForChosenTypeException, WrongUserForTaskTypeException
	{
		GregorianCalendar end2 = (GregorianCalendar) endDate.clone();
		end2.add(Calendar.DAY_OF_WEEK, -1);
		
		//Task t1 = new Task("d", user, new TaskTimings(startDate, endDate, 1), manager.getClock());
		Task t1 = TaskFactory.createTask("d", taskType, new ArrayList<Field>(),
				user, new TaskTimings(startDate, endDate, 1), manager.getClock());
		//Task t2 = new Task("d", user, new TaskTimings(startDate, end2, 1), manager.getClock());
		Task t2 = TaskFactory.createTask("d", taskType, new ArrayList<Field>(),
				user, new TaskTimings(startDate, end2, 1), manager.getClock());
		
		TaskDeadlineComparator c = new TaskDeadlineComparator();
		assertEquals(1, c.compare(t1, t2));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	/**
	 * Tests whether the CanBeExecuted method works properly in various
	 * situations.
	 */
	public void testCanBeExecuted() throws NullPointerException, WrongFieldsForChosenTypeException, EmptyStringException, BusinessRule1Exception, IllegalStateCallException, BusinessRule3Exception, AssetAllocatedException, InvitationInvitesOwnerException, InvitationNotPendingException, NotAvailableException, NoReservationOverlapException, WrongUserForTaskTypeException, AssetTypeNotRequiredException, AssetConstraintFullException{
		
		ResourceType goBoard = new ResourceType("Go board");
		Resource board = new Resource("Kwinten's go board",goBoard);
		UserType goPlayer = new UserType("Go player");
		User kwinten = new User("Kwinten", goPlayer);
		User steven = new User("Dieter", goPlayer);
		TaskTypeConstraint constraintBoard = new TaskTypeConstraint(goBoard, 1,1);
		TaskTypeConstraint constraintOwner = new TaskTypeConstraint(goPlayer, 1,1);
		//TODO!!! Verschil tussen helper en owner in constraints.
		TaskTypeConstraint constraintHelper = new TaskTypeConstraint(goPlayer,1,1);
		ArrayList<TaskTypeConstraint> assetsReq = new ArrayList<TaskTypeConstraint>();
		assetsReq.add(constraintBoard);
		assetsReq.add(constraintOwner);
		assetsReq.add(constraintHelper);

		TaskType goGame = new TaskType("Playing go", 
				new ArrayList<Field>(), assetsReq);
		Task playGo = TaskFactory.createTask("Playing Go", goGame, new ArrayList<Field>(), 
				kwinten, new TaskTimings(startDate, endDate, 120), manager.getClock());
		
		//Resources not ready! Task can not be executed.
		assertFalse(playGo.canBeExecuted());
		
		//Create reservation
		Reservation reserveBoard = new Reservation(startDate,150, board, playGo);
		//Helper user still not satisfied though!
		assertFalse(playGo.canBeExecuted());
		
		//Invite Steven
		Invitation inviteSteven = new Invitation(playGo, steven);
		inviteSteven.accept();
		//Task should be ready to run
		assertTrue(playGo.canBeExecuted());
	}
}
