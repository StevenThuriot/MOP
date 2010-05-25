package model;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import org.junit.Before;
import org.junit.Test;

import exception.BusinessRule1Exception;
import exception.BusinessRule3Exception;
import exception.EmptyStringException;
import exception.IllegalStateCallException;
import exception.TimeException;
import exception.WrongFieldsForChosenTypeException;
import exception.WrongUserForTaskTypeException;

import model.repositories.RepositoryManager;

public class ClockTest {

	private GregorianCalendar startDate;
	private GregorianCalendar dueDate;
	private RepositoryManager manager;
	private Clock clock;
	private Task task;
	private User user;
	
	@SuppressWarnings("unchecked")
	@Before
	public void setUp() throws NullPointerException, EmptyStringException, BusinessRule1Exception, IllegalStateCallException, BusinessRule3Exception, TimeException, WrongFieldsForChosenTypeException, WrongUserForTaskTypeException{
		startDate = new GregorianCalendar(2010, 1,1, 12, 0);
		dueDate = new GregorianCalendar(2010, 5,1, 12, 0);
		manager = new RepositoryManager();
		clock = manager.getClock();
		clock.setTime(startDate);
		user = new User("Kwinten",new UserType("UserType"));
		manager.add(user);
		
		ArrayList<UserType> userTypes = new ArrayList<UserType>();
		userTypes.add(user.getType());
		
		TaskType taskType = new TaskType("reorganizing the test cases", 
				new ArrayList<Field>(), new ArrayList<TaskTypeConstraint>(), userTypes);
		
		task = TaskFactory.createTask("Make Clock Tests", taskType, new ArrayList<Field>(),
				user, new TaskTimings(startDate, dueDate, 120), clock, new Project("X"));
	}
	
	/**
	 * Tests the initial behavior.
	 */
	@Test
	public void initialization(){
		// The clock is initialized to the correct Time
		assertTrue(clock.getTime().equals(startDate));
		// Task is currently unfinished
		assertTrue(task.isUnfinished());
	}
	
	/**
	 * Tests whether the update function (Observer pattern) works properly.
	 * Update, but no change required in task status.
	 * @throws TimeException 
	 */
	@Test
	public void update1() throws TimeException{
		startDate = new GregorianCalendar(2010, 1,2, 12, 0);
		clock.setTime(startDate);
		assertTrue(task.getClock().getTime().equals(startDate));
		assertTrue(task.isUnfinished());
	}
	
	/**
	 * Tests whether the update function (Observer pattern) works properly.
	 * Update, Task status should become failed.
	 * @throws TimeException 
	 */
	@Test
	public void update2() throws TimeException{
		startDate = new GregorianCalendar(2010, 6,2,12,0);
		clock.setTime(startDate);
		assertTrue(task.getClock().getTime().equals(startDate));
		assertTrue(task.isFailed());
	}
	
	/**
	 * Testing that a TimeException is thrown when the time specified is before the current time.
	 */
	@Test
	public void update3(){
		startDate = new GregorianCalendar(2010, 1,1, 10, 0);
		try {
			clock.setTime(startDate);
			fail();
		} catch (TimeException e) {/*Success*/}
	}
	
	/**
	 * Testing the second constructor
	 */
	@Test
	public void init2()
	{
		Clock clock = new Clock(manager, startDate);
		assertTrue(clock.getTime().equals(startDate));
	}
}
