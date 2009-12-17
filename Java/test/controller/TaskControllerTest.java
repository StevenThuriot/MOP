package controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import model.Resource;
import model.ResourceType;
import model.Task;
import model.User;
import model.repositories.RepositoryManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import controller.TaskController;
import controller.FocusFactory.FocusType;
import exception.BusinessRule1Exception;
import exception.BusinessRule2Exception;
import exception.BusinessRule3Exception;
import exception.DependencyCycleException;
import exception.DependencyException;
import exception.EmptyStringException;
import exception.IllegalStateCallException;
import exception.IllegalStateChangeException;
import exception.UnknownStateException;


public class TaskControllerTest {

	private TaskController controller;
	private User user;
	private RepositoryManager manager;
	@Before
	public void setUp()
	{
		manager = new RepositoryManager();
		controller = new TaskController(manager);
		user = new User("John");
	}
	
	@After
	public void tearDown()
	{
		controller = null;
	}
	
	/**
	 * TODO: Duration
	 * @throws DependencyCycleException 
	 * @throws BusinessRule1Exception 
	 * @throws EmptyStringException 
	 * @throws IllegalStateCallException 
	 * @throws NullPointerException 
	 * @throws BusinessRule3Exception 
	 */
	@Test
	public void createTask() throws EmptyStringException, BusinessRule1Exception, DependencyCycleException, NullPointerException, IllegalStateCallException, BusinessRule3Exception
	{
		GregorianCalendar end = new GregorianCalendar();
		end.add(Calendar.MONTH,1);
		Task taak = controller.createTask("Beschrijving", new GregorianCalendar(), end, 120, new ArrayList<Task>(), new ArrayList<Resource>(), user);
		assertTrue(controller.getTasks(user).contains(taak));
	}
	
	/**
	 * Test to see if tasks are removed
	 * @throws EmptyStringException
	 * @throws BusinessRule1Exception
	 * @throws DependencyCycleException
	 * @throws DependencyException
	 * @throws IllegalStateCallException 
	 * @throws NullPointerException 
	 * @throws BusinessRule3Exception 
	 */
	@Test
	public void removeTask() throws EmptyStringException, BusinessRule1Exception, DependencyCycleException, DependencyException, NullPointerException, IllegalStateCallException, BusinessRule3Exception
	{
		GregorianCalendar end = new GregorianCalendar();
		end.add(Calendar.MONTH,1);
		Task taak = controller.createTask("Beschrijving", new GregorianCalendar(), end, 120, new ArrayList<Task>(), new ArrayList<Resource>(), user);
		controller.removeTask(taak);
		assertFalse(controller.getTasks(user).contains(taak));
	}
	
	/**
	 * Test to see if the controller's constructor checks for null
	 * @throws EmptyStringException
	 * @throws BusinessRule1Exception
	 * @throws DependencyCycleException
	 * @throws NullPointerException
	 * @throws IllegalStateCallException
	 * @throws BusinessRule3Exception
	 */
	@Test(expected=NullPointerException.class)
	public void createController() throws EmptyStringException, BusinessRule1Exception, DependencyCycleException, NullPointerException, IllegalStateCallException, BusinessRule3Exception
	{
		@SuppressWarnings("unused")
		TaskController test = new TaskController(null);
	}
	
	/**
	 * Set task to failed
	 * @throws EmptyStringException
	 * @throws NullPointerException
	 * @throws BusinessRule1Exception
	 * @throws IllegalStateCallException
	 * @throws BusinessRule3Exception
	 * @throws IllegalStateChangeException
	 */
	@Test
	public void testSetFailed() throws EmptyStringException, NullPointerException, BusinessRule1Exception, IllegalStateCallException, BusinessRule3Exception, IllegalStateChangeException
	{
		GregorianCalendar startDate = new GregorianCalendar();//Now
		GregorianCalendar endDate = new GregorianCalendar();
		endDate.add(Calendar.DAY_OF_YEAR, 4); // 4 days to finish
		
		manager = new RepositoryManager();
		Task task = new Task("Descr",user,startDate,endDate,120, manager.getClock());
		
		controller.setFailed(task);
		assertEquals(true, task.isFailed());
	}
	
	/**
	 * Set task to successful
	 * @throws EmptyStringException
	 * @throws NullPointerException
	 * @throws BusinessRule1Exception
	 * @throws IllegalStateCallException
	 * @throws BusinessRule3Exception
	 * @throws IllegalStateChangeException
	 * @throws BusinessRule2Exception 
	 */
	@Test
	public void testSetSuccessful() throws EmptyStringException, NullPointerException, BusinessRule1Exception, IllegalStateCallException, BusinessRule3Exception, IllegalStateChangeException, BusinessRule2Exception
	{
		GregorianCalendar startDate = (GregorianCalendar) manager.getClock().getTime().clone();//Now
		GregorianCalendar endDate = (GregorianCalendar) manager.getClock().getTime().clone();
		endDate.add(Calendar.DAY_OF_YEAR, 4); // 4 days to finish

		manager = new RepositoryManager();
		Task task = new Task("Descr",user,startDate,endDate,120, manager.getClock());
		
		controller.setSuccessful(task);
		assertEquals(true, task.isSuccesful());
	}
	
	/**
	 * Set task to successful
	 * @throws EmptyStringException
	 * @throws NullPointerException
	 * @throws BusinessRule1Exception
	 * @throws IllegalStateCallException
	 * @throws BusinessRule3Exception
	 * @throws IllegalStateChangeException
	 * @throws UnknownStateException 
	 * @throws BusinessRule2Exception 
	 */
	@Test
	public void testSetSuccessful2() throws EmptyStringException, NullPointerException, BusinessRule1Exception, IllegalStateCallException, BusinessRule3Exception, IllegalStateChangeException, UnknownStateException, BusinessRule2Exception
	{
		GregorianCalendar startDate = (GregorianCalendar) manager.getClock().getTime().clone();//Now
		GregorianCalendar endDate = (GregorianCalendar) manager.getClock().getTime().clone();
		endDate.add(Calendar.DAY_OF_YEAR, 4); // 4 days to finish

		manager = new RepositoryManager();
		Task task = new Task("Descr",user,startDate,endDate,120, manager.getClock());
		
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
	 */
	@Test
	public void testGetDependantTasks() throws NullPointerException, EmptyStringException, BusinessRule1Exception, IllegalStateCallException, BusinessRule3Exception, DependencyCycleException
	{
		GregorianCalendar startDate = new GregorianCalendar();//Now
		GregorianCalendar endDate = new GregorianCalendar();
		endDate.add(Calendar.DAY_OF_YEAR, 4); // 4 days to finish

		manager = new RepositoryManager();
		Task task = new Task("Descr",user,startDate,endDate,120, manager.getClock());
		Task task2 = new Task("Descr",user,startDate,endDate,120, manager.getClock());
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
	 */
	@Test
	public void testHasDependentTasks() throws NullPointerException, EmptyStringException, BusinessRule1Exception, IllegalStateCallException, BusinessRule3Exception, DependencyCycleException
	{
		GregorianCalendar startDate = new GregorianCalendar();//Now
		GregorianCalendar endDate = new GregorianCalendar();
		endDate.add(Calendar.DAY_OF_YEAR, 4); // 4 days to finish

		manager = new RepositoryManager();
		Task task = new Task("Descr",user,startDate,endDate,120, manager.getClock());
		Task task2 = new Task("Descr",user,startDate,endDate,120, manager.getClock());
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
	 */
	@Test
	public void testHasDependencies() throws NullPointerException, EmptyStringException, BusinessRule1Exception, IllegalStateCallException, BusinessRule3Exception, DependencyCycleException
	{
		GregorianCalendar startDate = new GregorianCalendar();//Now
		GregorianCalendar endDate = new GregorianCalendar();
		endDate.add(Calendar.DAY_OF_YEAR, 4); // 4 days to finish

		manager = new RepositoryManager();
		Task task = new Task("Descr",user,startDate,endDate,120, manager.getClock());
		Task task2 = new Task("Descr",user,startDate,endDate,120, manager.getClock());
		task.addDependency(task2);

		assertEquals(false, controller.hasDependencies(task2));
		assertEquals(true, controller.hasDependencies(task));
	}
	
	/**
	 * Testing the controllers HasRequiredResources
	 * @throws NullPointerException
	 * @throws EmptyStringException
	 * @throws BusinessRule1Exception
	 * @throws IllegalStateCallException
	 * @throws BusinessRule3Exception
	 */
	@Test
	public void testHasRequiredResources() throws NullPointerException, EmptyStringException, BusinessRule1Exception, IllegalStateCallException, BusinessRule3Exception
	{
		GregorianCalendar startDate = new GregorianCalendar();//Now
		GregorianCalendar endDate = new GregorianCalendar();
		endDate.add(Calendar.DAY_OF_YEAR, 4); // 4 days to finish

		manager = new RepositoryManager();
		Task task = new Task("Descr",user,startDate,endDate,120, manager.getClock());
		Task task2 = new Task("Descr",user,startDate,endDate,120, manager.getClock());
		
		Resource r = new Resource("d", ResourceType.Room);
		controller.addRequiredResource(task, r);

		assertEquals(true, controller.hasRequiredResources(task));
		assertEquals(false, controller.hasRequiredResources(task2));
		
		assertEquals(true, controller.getRequiredResources(task).contains(r));
		
		controller.removeRequiredResource(task, r);

		assertEquals(false, controller.hasRequiredResources(task));
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
	 */
	@Test
	public void testDependencies() throws NullPointerException, EmptyStringException, BusinessRule1Exception, IllegalStateCallException, BusinessRule3Exception, DependencyCycleException, DependencyException
	{
		GregorianCalendar startDate = new GregorianCalendar();//Now
		GregorianCalendar endDate = new GregorianCalendar();
		endDate.add(Calendar.DAY_OF_YEAR, 4); // 4 days to finish

		manager = new RepositoryManager();
		Task task = new Task("Descr",user,startDate,endDate,120, manager.getClock());
		Task task2 = new Task("Descr",user,startDate,endDate,120, manager.getClock());

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
	 */
	@Test
	public void testSetDescription() throws NullPointerException, EmptyStringException, BusinessRule1Exception, IllegalStateCallException, BusinessRule3Exception
	{
		GregorianCalendar startDate = new GregorianCalendar();//Now
		GregorianCalendar endDate = new GregorianCalendar();
		endDate.add(Calendar.DAY_OF_YEAR, 2); // 4 days to finish

		manager = new RepositoryManager();
		Task task = new Task("Descr",user,startDate,endDate,120, manager.getClock());
		controller.setTaskDescription(task, "blub");
		
		assertEquals("blub", task.getDescription());
	}
	
	/**
	 * Testing the focuswork
	 * @throws NullPointerException
	 * @throws EmptyStringException
	 * @throws BusinessRule1Exception
	 * @throws IllegalStateCallException
	 * @throws BusinessRule3Exception
	 */
	@Test
	public void testFocus() throws NullPointerException, EmptyStringException, BusinessRule1Exception, IllegalStateCallException, BusinessRule3Exception
	{

		GregorianCalendar startDate = new GregorianCalendar();//Now
		GregorianCalendar endDate = new GregorianCalendar();
		endDate.add(Calendar.DAY_OF_YEAR, 4); // 4 days to finish

		manager = new RepositoryManager();
		@SuppressWarnings("unused")
		Task task = new Task("Descr",user,startDate,endDate,120, manager.getClock());
		
		assertEquals(1, controller.focusWork(user, FocusType.DeadlineFocus, 10, 0).size());
	}
	
	/**
	 * Testing the controllers shedule setter
	 * @throws NullPointerException
	 * @throws EmptyStringException
	 * @throws BusinessRule1Exception
	 * @throws IllegalStateCallException
	 * @throws BusinessRule3Exception
	 */
	@Test
	public void testShedule() throws NullPointerException, EmptyStringException, BusinessRule1Exception, IllegalStateCallException, BusinessRule3Exception
	{
		GregorianCalendar startDate = new GregorianCalendar();//Now
		GregorianCalendar endDate = new GregorianCalendar();
		endDate.add(Calendar.DAY_OF_YEAR, 4); // 4 days to finish
		
		GregorianCalendar startDate2 = new GregorianCalendar();
		GregorianCalendar endDate2 = new GregorianCalendar();
		startDate2.add(Calendar.DAY_OF_YEAR, 1);
		endDate2.add(Calendar.DAY_OF_YEAR, 5); 

		manager = new RepositoryManager();
		Task task = new Task("Descr",user,startDate,endDate,120, manager.getClock());
		Task task2 = new Task("Descr",user,startDate2,endDate2,200, manager.getClock());
		
		controller.setTaskSchedule(task2, startDate, endDate, 120);

		assertEquals(task.getStartDate(), task2.getStartDate());
		assertEquals(task.getDueDate(), task2.getDueDate());
		assertEquals(task.getDuration(), task2.getDuration());
	}
}