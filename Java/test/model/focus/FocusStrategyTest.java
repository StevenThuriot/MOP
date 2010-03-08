package model.focus;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import model.Task;
import model.TaskTimings;
import model.User;
import model.repositories.RepositoryManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import controller.TaskController;

import static org.junit.Assert.*;

public class FocusStrategyTest {
    
    private User user;
    private TaskController controller = new TaskController(new RepositoryManager());
    /**
     * Setting all variables to be used in tests
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        user = new User("John");
        GregorianCalendar end1 = new GregorianCalendar();
        end1.add(Calendar.DAY_OF_MONTH, 1);
        GregorianCalendar end2 = new GregorianCalendar();
        end2.add(Calendar.MONTH, 1);
        controller.createTask("Task1", new TaskTimings(new GregorianCalendar(), end1, 10), user);
        controller.createTask("Task2", new TaskTimings(new GregorianCalendar(), end2, 3600), user);
        
    }
    
    /**
     * Clearing all variables
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        user = null;
    }
    
    @Test
    public void testEmptySetDeadline()
    {
        FocusWork work = new FocusWork(user,new DeadlineFocus(0));
        assertEquals(0,work.getTasks().size());
    }
    
    @Test
    public void testSortDuration()
    {
        List<Task> tasks = new FocusWork(user,new DurationFocus(0,Integer.MAX_VALUE)).getTasks();
        assertTrue(tasks.get(0).getDuration() < tasks.get(1).getDuration());
    }
    
    @Test
    public void testSortDeadline()
    {
        List<Task> tasks = new FocusWork(user,new DeadlineFocus(10)).getTasks();
        assertTrue(tasks.get(0).getDueDate().before(tasks.get(1).getDueDate()));
    }
    
    @Test
    public void testSortDefault()
    {
        List<Task> tasks = new FocusWork(user,new FocusStrategy()).getTasks();
        assertEquals(user.getTasks().get(0), tasks.get(0));
        assertEquals(user.getTasks().get(1), tasks.get(1));
    }
}
