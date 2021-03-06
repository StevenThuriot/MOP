package model.focus;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import model.Field;
import model.Project;
import model.Task;
import model.TaskTimings;
import model.TaskType;
import model.TaskTypeConstraint;
import model.User;
import model.UserType;
import model.repositories.RepositoryManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import controller.TaskController;
import exception.ArrayLengthException;

import static org.junit.Assert.*;

public class FocusStrategyTest {
    
    private User user;
    private TaskType taskType;
    private TaskType taskType2;
    private TaskController controller = new TaskController(new RepositoryManager());
    /**
     * Setting all variables to be used in tests
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
	@Before
    public void setUp() throws Exception {
        user = new User("John",new UserType(""));
        GregorianCalendar end1 = new GregorianCalendar();
        end1.add(Calendar.DAY_OF_MONTH, 1);
        GregorianCalendar end2 = new GregorianCalendar();
        end2.add(Calendar.MONTH, 1);
        
        ArrayList<UserType> userTypes = new ArrayList<UserType>();
		userTypes.add(user.getType());
        
        taskType = new TaskType("reorganizing the test cases", 
				new ArrayList<Field>(), new ArrayList<TaskTypeConstraint>(), userTypes);
        taskType2 = new TaskType("Doing something else", 
				new ArrayList<Field>(), new ArrayList<TaskTypeConstraint>(), userTypes);
        controller.createTask("Task1",taskType,new ArrayList<Field>(), user, new TaskTimings(new GregorianCalendar(), end1, 10), new Project("X"));
        controller.createTask("Task2",taskType,new ArrayList<Field>(), user, new TaskTimings(new GregorianCalendar(), end2, 3600), new Project("X"));
        controller.createTask("Task3",taskType2,new ArrayList<Field>(), user, new TaskTimings(new GregorianCalendar(), end2, 3600), new Project("X"));
        
    }
    
    /**
     * Clearing all variables
     */
    @After
    public void tearDown() {
        user = null;
    }
    
    @Test
    public void testEmptySetDeadline() throws ArrayLengthException
    {
    	Object[] settings = new Object[] {0};
        FocusWork work = new FocusWork(user,new DeadlineFocus(settings));
        assertEquals(0,work.getTasks().size());
    }
    
    @Test
    public void testSortDuration() throws ArrayLengthException
    {
    	Object[] settings = new Object[] {0, Integer.MAX_VALUE};
        List<Task> tasks = new FocusWork(user,new DurationFocus(settings)).getTasks();
        assertTrue(tasks.get(0).getDuration() < tasks.get(1).getDuration());
    }
    
    @Test
    public void testSortDeadline() throws ArrayLengthException
    {
    	Object[] settings = new Object[] {10};
        List<Task> tasks = new FocusWork(user,new DeadlineFocus(settings)).getTasks();
        assertTrue(tasks.get(0).getDueDate().before(tasks.get(1).getDueDate()));
    }
    
    @Test
    public void testSortDefault()
    {
        List<Task> tasks = new FocusWork(user,new FocusStrategy()).getTasks();
        assertEquals(user.getTasks().get(0), tasks.get(0));
        assertEquals(user.getTasks().get(1), tasks.get(1));
    }
    @Test
    public void testFilterTaskType() throws ArrayLengthException
    {
    	Object[] settings = {Integer.MAX_VALUE,this.taskType};
    	List<Task> tasks = new FocusWork(user, new TaskTypeFocus(settings)).getTasks();
    	assertEquals(2, tasks.size());
    	
    	settings[1] = this.taskType2;
    	tasks = new FocusWork(user, new TaskTypeFocus(settings)).getTasks();
    	assertEquals(1, tasks.size());
    }
}
