package model;

import static org.junit.Assert.*;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import model.repositories.RepositoryManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import exception.BusinessRule1Exception;
import exception.BusinessRule2Exception;
import exception.DependencyCycleException;
import exception.DependencyException;
import exception.IllegalStateCallException;
import exception.IllegalStateChangeException;

public class TaskDependencyTest {


	
	/**
	 * The TaskDependencyManager to be tested.
	 */
	private TaskDependencyManager tdm;
	/**
	 * RepositoryManager to be used.
	 */
	private RepositoryManager manager;
	/**
	 * The Task that the TaskDependencyManager is appointed to.
	 */
	private Task taskMain;
	/**
	 * A task to be used in the tests.
	 */
	private Task taskHelp;
	/**
	 * Another task to be used in the tests
	 */
	private Task taskHelp2;
	
	private TaskType taskType;
	
	@SuppressWarnings("unchecked")
	@Before
	public void setUp() throws Exception {
		manager = new RepositoryManager();
		//sets up a new Task
		User user = new User("John",new UserType(""));
		GregorianCalendar startDate = new GregorianCalendar();
		GregorianCalendar endDate = new GregorianCalendar();
		endDate.add(Calendar.DAY_OF_YEAR, 4);
		// 4 days to finish the task from now on
		int duration = 1;
		ArrayList<UserType> userTypes = new ArrayList<UserType>();
		userTypes.add(user.getType());
		
		taskType = new TaskType("reorganizing the test cases", 
				new ArrayList<Field>(), new ArrayList<TaskTypeConstraint>(), userTypes);
		taskMain = TaskFactory.createTask("Main Task", taskType, new ArrayList<Field>(),
				user, new TaskTimings(startDate, endDate, duration), manager.getClock(),new Project("X"));
		//sets up a second Task
		taskHelp = TaskFactory.createTask("Help Task", taskType, new ArrayList<Field>(),
				user, new TaskTimings(startDate, endDate, duration), manager.getClock(),new Project("Y"));
		//sets up yet another Task
		taskHelp2 = TaskFactory.createTask("Help Task2", taskType, new ArrayList<Field>(),
				user, new TaskTimings(startDate, endDate, duration), manager.getClock(),new Project("Z"));
		//sets up the TDM
		tdm = taskMain.getTaskDependencyManager();
	}

	@After
	public void tearDown() {
		tdm = null;
		taskMain = null;
		taskHelp = null;
	}
	
	@Test
	public void initialization(){
		// The TDM should be allocated to taskMain
		assertEquals(taskMain,tdm.getTask() );
		// It should be initialized with an empty list of dependencies and dependents
		assertTrue(tdm.getDependencies().isEmpty());
		assertTrue(tdm.getDependentTasks().isEmpty());
	}
	
	@Test
	public void addDependency(){
		tdm.addDependency(taskHelp);
		//The TDM is now dependent on taskHelp
		assertTrue(tdm.dependsOn(taskHelp));
		//taskHelp now has taskMain in its list of dependent tasks
		assertTrue(taskHelp.getTaskDependencyManager().neededFor(taskMain));
	}
	
	@Test(expected=DependencyException.class)
	public void removeDependency1() throws DependencyException{
		//Must throw an error, tdm is not dependent on taskHelp
		tdm.removeDependency(taskHelp);
	}
	
	@Test
	public void removeDependency2() throws DependencyException{
		tdm.addDependency(taskHelp);
		tdm.removeDependency(taskHelp);
		// tdm is no longer dependent on taskHelp
		assertFalse(tdm.dependsOn(taskHelp));
		//taskHelp no longer has a reference to taskMain in its list of dependentTasks
		assertFalse(taskHelp.getTaskDependencyManager().neededFor(taskMain));
	}
	
	@Test
	public void recursiveDependencies1() throws BusinessRule1Exception, DependencyCycleException, IllegalStateCallException, BusinessRule2Exception{
		tdm.addDependency(taskHelp);
		taskHelp.addDependency(taskHelp2);
		// tdm (or taskMain) should be recursively dependent on both taskHelp and taskHelp2
		assertTrue(tdm.isRecursivelyDependentOn(taskHelp));
		assertTrue(tdm.isRecursivelyDependentOn(taskHelp2));
	}
	
	@Test(expected=DependencyCycleException.class)
	public void recursiveDependencies2() throws BusinessRule1Exception, IllegalStateCallException, DependencyCycleException, BusinessRule2Exception {
		tdm.addDependency(taskHelp);
		// A DependencyCycleException should be thrown here:
		// taskMain depends on taskHelp which depends on taskMain again
		taskHelp.addDependency(taskMain);
	}
	
	@Test
	public void statusUpdate() throws BusinessRule1Exception, DependencyCycleException, IllegalStateCallException, IllegalStateChangeException, BusinessRule2Exception{
		tdm.addDependency(taskHelp);
		taskHelp.addDependency(taskHelp2);
		taskHelp2.setFailed();
		assertTrue(taskMain.isFailed());
		assertTrue(taskHelp.isFailed());
		assertTrue(taskHelp2.isFailed());
	}
	
	
	
	

}
