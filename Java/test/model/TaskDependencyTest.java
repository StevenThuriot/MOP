package model;

import static org.junit.Assert.*;


import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import exception.BusinessRule1Exception;
import exception.DependencyCycleException;
import exception.DependencyException;
import exception.IllegalStateCall;

public class TaskDependencyTest {


	
	/**
	 * The TaskDependencyManager to be tested.
	 */
	private TaskDependencyManager tdm;
	
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
	
	@Before
	public void setUp() throws Exception {
		//sets up a new Task
		User user = new User("John");
		GregorianCalendar startDate = new GregorianCalendar();
		GregorianCalendar endDate = new GregorianCalendar();
		endDate.add(Calendar.DAY_OF_YEAR, 4);
		// 4 days to finish the task from now on
		int duration = 1;
		taskMain = new Task("Main Task",user,startDate,endDate,duration);
		//sets up a second Task
		taskHelp = new Task("Help Task", user, startDate, endDate, duration);
		//sets up yet another Task
		taskHelp2 = new Task("Help Task2", user, startDate, endDate, duration);
		//sets up the TDM
		tdm = taskMain.getTaskDependencyManager();
	}

	@After
	public void tearDown() throws Exception {
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
	
	@Test
	public void removeDependency1() throws DependencyException{
		//Must throw an error, tdm is not dependent on taskHelp
		try{
			tdm.removeDependency(taskHelp);
			fail();
		}
		catch(DependencyException e){};		
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
	public void recursiveDependencies1() throws BusinessRule1Exception, DependencyCycleException, IllegalStateCall{
		tdm.addDependency(taskHelp);
		taskHelp.addDependency(taskHelp2);
		// tdm (or taskMain) should be recursively dependent on both taskHelp and taskHelp2
		assertTrue(tdm.isRecursivelyDependentOn(taskHelp));
		assertTrue(tdm.isRecursivelyDependentOn(taskHelp2));
	}
	
	@Test
	public void recursiveDependencies2() throws BusinessRule1Exception, IllegalStateCall {
		tdm.addDependency(taskHelp);
		// A DependencyCycleException should be thrown here:
		// taskMain depends on taskHelp which depends on taskMain again
		try {
			taskHelp.addDependency(taskMain);
			fail();
		} catch (DependencyCycleException e){}
	}
	
	
	
	
	
	

}
