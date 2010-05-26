package controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import model.Field;
import model.Project;
import model.Task;
import model.TaskFactory;
import model.TaskTimings;
import model.TaskType;
import model.TaskTypeConstraint;
import model.User;
import model.UserType;
import model.repositories.RepositoryManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import controller.TaskController;
import exception.BusinessRule1Exception;
import exception.BusinessRule2Exception;
import exception.BusinessRule3Exception;
import exception.DependencyCycleException;
import exception.DependencyException;
import exception.EmptyStringException;
import exception.IllegalStateCallException;
import exception.IllegalStateChangeException;
import exception.UnknownStateException;
import exception.WrongFieldsForChosenTypeException;
import exception.WrongUserForTaskTypeException;


public class TaskControllerTest {

	private TaskController controller;
	private User user;
	private RepositoryManager manager;
	private TaskType taskType;
	
	@SuppressWarnings("unchecked")
	@Before
	public void setUp()
	{
		manager = new RepositoryManager();
		controller = new TaskController(manager);
		user = new User("John",new UserType(""));
		
		ArrayList<UserType> userTypes = new ArrayList<UserType>();
		userTypes.add(user.getType());
		
		taskType = controller.addTaskType("MyType",new ArrayList<Field>(), new ArrayList<TaskTypeConstraint>(), userTypes);
	}
	
	@After
	public void tearDown()
	{
		controller = null;
	}
	
	/**
	 * Duration
	 * @throws BusinessRule1Exception 
	 * @throws EmptyStringException 
	 * @throws NullPointerException 
	 * @throws BusinessRule3Exception 
	 * @throws WrongFieldsForChosenTypeException 
	 * @throws WrongUserForTaskTypeException 
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void createTask() throws EmptyStringException, BusinessRule1Exception, NullPointerException, BusinessRule3Exception, WrongFieldsForChosenTypeException, WrongUserForTaskTypeException
	{
		GregorianCalendar end = new GregorianCalendar();
		end.add(Calendar.MONTH,1);
		Task taak = controller.createTask("Task",taskType,new ArrayList<Field>(),user,new TaskTimings(new GregorianCalendar(), end, 120), new Project("X"));
		assertTrue(controller.getTasks(user).contains(taak));
	}
	
	/**
	 * Test to see if tasks are removed
	 * @throws EmptyStringException
	 * @throws BusinessRule1Exception
	 * @throws NullPointerException 
	 * @throws BusinessRule3Exception 
	 * @throws WrongFieldsForChosenTypeException 
	 * @throws WrongUserForTaskTypeException 
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void removeTask() throws EmptyStringException, BusinessRule1Exception, NullPointerException, BusinessRule3Exception, WrongFieldsForChosenTypeException, WrongUserForTaskTypeException
	{
		GregorianCalendar end = new GregorianCalendar();
		end.add(Calendar.MONTH,1);
		Task taak = controller.createTask("Task",taskType,new ArrayList<Field>(),user,new TaskTimings( new GregorianCalendar(), end, 120), new Project("X"));
		controller.removeTask(taak);
		assertFalse(controller.getTasks(user).contains(taak));
	}
	
	/**
	 * Test to see if tasks are removed
	 * @throws EmptyStringException
	 * @throws BusinessRule1Exception
	 * @throws DependencyCycleException
	 * @throws IllegalStateCallException 
	 * @throws NullPointerException 
	 * @throws BusinessRule3Exception 
	 * @throws WrongFieldsForChosenTypeException 
	 * @throws WrongUserForTaskTypeException 
	 * @throws BusinessRule2Exception 
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void removeTaskRecursively() throws EmptyStringException, BusinessRule1Exception, DependencyCycleException, NullPointerException, IllegalStateCallException, BusinessRule3Exception, WrongFieldsForChosenTypeException, WrongUserForTaskTypeException, BusinessRule2Exception
	{
		GregorianCalendar end = new GregorianCalendar();
		end.add(Calendar.MONTH,1);
		Task taak = controller.createTask("Beschrijving",taskType,new ArrayList<Field>(),user, new TaskTimings( new GregorianCalendar(), end, 120), new Project("X"));
		Task taak2 = controller.createTask("Beschrijving",taskType,new ArrayList<Field>(),user, new TaskTimings( new GregorianCalendar(), end, 120), new Project("X"));
		controller.addDependency(taak2, taak);
		controller.removeTaskRecursively(taak);
		assertFalse(controller.getTasks(user).contains(taak));
		assertFalse(controller.getTasks(user).contains(taak2));
	}
	
	/**
	 * Test to see if the controller's constructor checks for null
	 * @throws NullPointerException
	 */
	@Test(expected=NullPointerException.class)
	public void createController() throws NullPointerException
	{
		@SuppressWarnings("unused")
		TaskController test = new TaskController(null);
	}
	
	/**
	 * Set task to failed
	 * @throws EmptyStringException
	 * @throws NullPointerException
	 * @throws BusinessRule1Exception
	 * @throws BusinessRule3Exception
	 * @throws IllegalStateChangeException
	 * @throws WrongFieldsForChosenTypeException 
	 * @throws WrongUserForTaskTypeException 
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testSetFailed() throws EmptyStringException, NullPointerException, BusinessRule1Exception, BusinessRule3Exception, IllegalStateChangeException, WrongFieldsForChosenTypeException, WrongUserForTaskTypeException
	{
		GregorianCalendar endDate = new GregorianCalendar();
		endDate.add(Calendar.DAY_OF_YEAR, 4); // 4 days to finish
		
		manager = new RepositoryManager();
		Task task = TaskFactory.createTask("Descr", taskType, new ArrayList<Field>(),
				user, new TaskTimings(new GregorianCalendar(), endDate, 120), manager.getClock(),new Project("X"));
		
		controller.setFailed(task);
		assertEquals(true, task.isFailed());
	}
	
	/**
	 * Set task to successful
	 * @throws EmptyStringException
	 * @throws NullPointerException
	 * @throws BusinessRule1Exception
	 * @throws BusinessRule3Exception
	 * @throws IllegalStateChangeException
	 * @throws BusinessRule2Exception 
	 * @throws WrongFieldsForChosenTypeException 
	 * @throws WrongUserForTaskTypeException 
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testSetSuccessful() throws EmptyStringException, NullPointerException, BusinessRule1Exception, BusinessRule3Exception, IllegalStateChangeException, BusinessRule2Exception, WrongFieldsForChosenTypeException, WrongUserForTaskTypeException
	{
		GregorianCalendar endDate = (GregorianCalendar) manager.getClock().getTime().clone();
		endDate.add(Calendar.DAY_OF_YEAR, 4); // 4 days to finish

		manager = new RepositoryManager();
		Task task = TaskFactory.createTask("Descr", taskType, new ArrayList<Field>(),
				user, new TaskTimings((GregorianCalendar) manager.getClock().getTime().clone(),endDate,120)
		, manager.getClock(),new Project("X"));
		
		controller.setSuccessful(task);
		assertEquals(true, task.isSuccesful());
	}
	
	/**
	 * Set task to successful
	 * @throws EmptyStringException
	 * @throws NullPointerException
	 * @throws BusinessRule1Exception
	 * @throws BusinessRule3Exception
	 * @throws IllegalStateChangeException
	 * @throws UnknownStateException 
	 * @throws BusinessRule2Exception 
	 * @throws WrongFieldsForChosenTypeException 
	 * @throws WrongUserForTaskTypeException 
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testSetSuccessful2() throws EmptyStringException, NullPointerException, BusinessRule1Exception, BusinessRule3Exception, IllegalStateChangeException, UnknownStateException, BusinessRule2Exception, WrongFieldsForChosenTypeException, WrongUserForTaskTypeException
	{
		GregorianCalendar startDate = (GregorianCalendar) manager.getClock().getTime().clone();//Now
		GregorianCalendar endDate = (GregorianCalendar) manager.getClock().getTime().clone();
		endDate.add(Calendar.DAY_OF_YEAR, 4); // 4 days to finish

		manager = new RepositoryManager();
		Task task = TaskFactory.createTask("Descr", taskType, new ArrayList<Field>(),
				user, new TaskTimings(startDate, endDate, 120), manager.getClock(),new Project("X"));
		
		controller.parseStateString(task, "Successful");
		assertEquals(true, task.isSuccesful());
	}
	
	/**
	 * Testing the controllers getDependantTasks
	 * @throws NullPointerException
	 * @throws EmptyStringException
	 * @throws BusinessRule1Exception
	 * @throws IllegalStateCallException
	 * @throws BusinessRule3Exception
	 * @throws DependencyCycleException
	 * @throws WrongFieldsForChosenTypeException 
	 * @throws WrongUserForTaskTypeException 
	 * @throws BusinessRule2Exception 
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testGetDependantTasks() throws NullPointerException, EmptyStringException, BusinessRule1Exception, IllegalStateCallException, BusinessRule3Exception, DependencyCycleException, WrongFieldsForChosenTypeException, WrongUserForTaskTypeException, BusinessRule2Exception
	{
		GregorianCalendar startDate = new GregorianCalendar();//Now
		GregorianCalendar endDate = new GregorianCalendar();
		endDate.add(Calendar.DAY_OF_YEAR, 4); // 4 days to finish

		manager = new RepositoryManager();
		Task task = TaskFactory.createTask("Descr", taskType, new ArrayList<Field>(),
				user, new TaskTimings(startDate, endDate, 120), manager.getClock(),new Project("X"));
		Task task2 = TaskFactory.createTask("some dependency", taskType, new ArrayList<Field>(),
				user, new TaskTimings(startDate, endDate, 120), manager.getClock(),new Project("X"));
		task.addDependency(task2);
		
		assertEquals(task.getDependentTasks(), controller.getDependentTasks(task));
	}
	
	/**
	 * Testing the controllers HasDependentTasks
	 * @throws NullPointerException
	 * @throws EmptyStringException
	 * @throws BusinessRule1Exception
	 * @throws IllegalStateCallException
	 * @throws BusinessRule3Exception
	 * @throws DependencyCycleException
	 * @throws WrongFieldsForChosenTypeException 
	 * @throws WrongUserForTaskTypeException 
	 * @throws BusinessRule2Exception 
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testHasDependentTasks() throws NullPointerException, EmptyStringException, BusinessRule1Exception, IllegalStateCallException, BusinessRule3Exception, DependencyCycleException, WrongFieldsForChosenTypeException, WrongUserForTaskTypeException, BusinessRule2Exception
	{
		GregorianCalendar startDate = new GregorianCalendar();//Now
		GregorianCalendar endDate = new GregorianCalendar();
		endDate.add(Calendar.DAY_OF_YEAR, 4); // 4 days to finish

		manager = new RepositoryManager();
		Task task = TaskFactory.createTask("Descr", taskType, new ArrayList<Field>(),
				user, new TaskTimings(startDate, endDate, 120), manager.getClock(),new Project("X"));
		Task task2 = TaskFactory.createTask("Descr", taskType, new ArrayList<Field>(),
				user, new TaskTimings(startDate, endDate, 120), manager.getClock(),new Project("X"));
		task.addDependency(task2);

		assertEquals(true, controller.hasDependentTasks(task2));
		assertEquals(false, controller.hasDependentTasks(task));
	}
	
	/**
	 * Testing the controllers HasDependencies
	 * @throws NullPointerException
	 * @throws EmptyStringException
	 * @throws BusinessRule1Exception
	 * @throws IllegalStateCallException
	 * @throws BusinessRule3Exception
	 * @throws DependencyCycleException
	 * @throws WrongFieldsForChosenTypeException 
	 * @throws WrongUserForTaskTypeException 
	 * @throws BusinessRule2Exception 
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testHasDependencies() throws NullPointerException, EmptyStringException, BusinessRule1Exception, IllegalStateCallException, BusinessRule3Exception, DependencyCycleException, WrongFieldsForChosenTypeException, WrongUserForTaskTypeException, BusinessRule2Exception
	{
		GregorianCalendar startDate = new GregorianCalendar();//Now
		GregorianCalendar endDate = new GregorianCalendar();
		endDate.add(Calendar.DAY_OF_YEAR, 4); // 4 days to finish

		manager = new RepositoryManager();
		Task task = TaskFactory.createTask("Descr", taskType, new ArrayList<Field>(),
				user, new TaskTimings(startDate, endDate, 120), manager.getClock(),new Project("X"));
		Task task2 = TaskFactory.createTask("Descr", taskType, new ArrayList<Field>(),
				user, new TaskTimings(startDate, endDate, 120), manager.getClock(),new Project("X"));
		task.addDependency(task2);

		assertEquals(false, controller.hasDependencies(task2));
		assertEquals(true, controller.hasDependencies(task));
	}
	
	/**
	 * Testing the controllers add and remove dependencies
	 * @throws NullPointerException
	 * @throws EmptyStringException
	 * @throws BusinessRule1Exception
	 * @throws IllegalStateCallException
	 * @throws BusinessRule3Exception
	 * @throws DependencyCycleException 
	 * @throws DependencyException 
	 * @throws WrongFieldsForChosenTypeException 
	 * @throws WrongUserForTaskTypeException 
	 * @throws BusinessRule2Exception 
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testDependencies() throws NullPointerException, EmptyStringException, BusinessRule1Exception, IllegalStateCallException, BusinessRule3Exception, DependencyCycleException, DependencyException, WrongFieldsForChosenTypeException, WrongUserForTaskTypeException, BusinessRule2Exception
	{
		GregorianCalendar startDate = new GregorianCalendar();//Now
		GregorianCalendar endDate = new GregorianCalendar();
		endDate.add(Calendar.DAY_OF_YEAR, 4); // 4 days to finish

		manager = new RepositoryManager();
		Task task = TaskFactory.createTask("Descr", taskType, new ArrayList<Field>(),
				user, new TaskTimings(startDate, endDate, 120), manager.getClock(),new Project("X"));
		Task task2 = TaskFactory.createTask("Descr", taskType, new ArrayList<Field>(),
				user, new TaskTimings(startDate, endDate, 120), manager.getClock(),new Project("X"));
		
		controller.addDependency(task, task2);

		assertEquals(true, controller.getDependencies(task).contains(task2));
		assertEquals(1, controller.getDependencies(task).size());

		assertEquals(false, controller.getDependencies(task2).contains(task));
		assertEquals(0, controller.getDependencies(task2).size());
		
		controller.removeDependency(task, task2);

		assertEquals(false, controller.getDependencies(task).contains(task2));
		assertEquals(0, controller.getDependencies(task).size());
	}
	
	/**
	 * Testing the controllers taskdescription
	 * @throws NullPointerException
	 * @throws EmptyStringException
	 * @throws BusinessRule1Exception
	 * @throws IllegalStateCallException
	 * @throws BusinessRule3Exception
	 * @throws WrongFieldsForChosenTypeException 
	 * @throws WrongUserForTaskTypeException 
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testSetDescription() throws NullPointerException, EmptyStringException, BusinessRule1Exception, IllegalStateCallException, BusinessRule3Exception, WrongFieldsForChosenTypeException, WrongUserForTaskTypeException
	{
		GregorianCalendar startDate = new GregorianCalendar();//Now
		GregorianCalendar endDate = new GregorianCalendar();
		endDate.add(Calendar.DAY_OF_YEAR, 2); // 4 days to finish

		manager = new RepositoryManager();
		Task task = TaskFactory.createTask("Descr", taskType, new ArrayList<Field>(),
				user, new TaskTimings(startDate, endDate, 120), manager.getClock(),new Project("X"));
		controller.setTaskDescription(task, "blub");
		
		assertEquals("blub", task.getDescription());
	}
	
	/**
	 * Testing the controllers shedule setter
	 * @throws NullPointerException
	 * @throws EmptyStringException
	 * @throws BusinessRule1Exception
	 * @throws BusinessRule3Exception
	 * @throws WrongFieldsForChosenTypeException 
	 * @throws WrongUserForTaskTypeException 
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testShedule() throws NullPointerException, EmptyStringException, BusinessRule1Exception, BusinessRule3Exception, WrongFieldsForChosenTypeException, WrongUserForTaskTypeException
	{
		GregorianCalendar startDate = new GregorianCalendar();//Now
		GregorianCalendar endDate = new GregorianCalendar();
		endDate.add(Calendar.DAY_OF_YEAR, 4); // 4 days to finish
		
		GregorianCalendar startDate2 = new GregorianCalendar();
		GregorianCalendar endDate2 = new GregorianCalendar();
		startDate2.add(Calendar.DAY_OF_YEAR, 1);
		endDate2.add(Calendar.DAY_OF_YEAR, 5); 

		manager = new RepositoryManager();
		Task task = TaskFactory.createTask("Descr", taskType, new ArrayList<Field>(),
				user, new TaskTimings(startDate, endDate, 120), manager.getClock(),new Project("X"));
		Task task2 = TaskFactory.createTask("Descr", taskType, new ArrayList<Field>(),
				user, new TaskTimings(startDate2, endDate2, 200), manager.getClock(),new Project("X"));
		controller.setTaskSchedule(task2, startDate, endDate, 120);

		assertEquals(task.getStartDate(), task2.getStartDate());
		assertEquals(task.getDueDate(), task2.getDueDate());
		assertEquals(task.getDuration(), task2.getDuration());
	}
}