package model.focus;

import java.util.List;

import model.Task;
import model.User;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;


public class FocusStrategyTest {
    
    private User user;
    
    /**
     * Setting all variables to be used in tests
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        user = new User("John");
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
        assertTrue(work.getTasks().isEmpty());
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
        List<Task> tasks = new FocusWork(user,new DeadlineFocus(0)).getTasks();
        assertTrue(tasks.get(0).getDueDate().before(tasks.get(1).getDueDate()));
    }
}
