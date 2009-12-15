package model;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.GregorianCalendar;

import model.repositories.RepositoryManager;

import org.junit.Before;
import org.junit.Test;

import exception.BusinessRule1Exception;
import exception.BusinessRule3Exception;
import exception.EmptyStringException;
import exception.IllegalStateCallException;

public class ProjectTest {
    
    

	private RepositoryManager manager;
	
	@Before
	public void setUp(){
		manager = new RepositoryManager();
	}
	
    /**
     * Can I create a project with an empty String?
     * Expected: EmptyStringException
     * @throws EmptyStringException
     */
    @Test(expected=EmptyStringException.class)
    public void createEmptyProject() throws EmptyStringException
    {
        new Project("");
    }
    
    /**
     * Can I create a project with null as a description?
     * Expected: EmptyStringException
     * @throws EmptyStringException
     */
    @Test(expected=NullPointerException.class)
    public void createNullProject() throws EmptyStringException, NullPointerException
    {
        new Project(null);
    }
    
    /**
     * Create a project as it should
     * @throws EmptyStringException 
     * @throws NullPointerException 
     */
    @Test
    public void createNormalProject() throws NullPointerException, EmptyStringException
    {
        Project hi = new Project("Hi");
        assertEquals("Hi", hi.getDescription());
    }
    
    @Test
    public void testTask() throws NullPointerException, EmptyStringException, BusinessRule1Exception, IllegalStateCallException, BusinessRule3Exception
    {
        Project proj = new Project("ABC");
        User user = new User("John");
        GregorianCalendar endDate = new GregorianCalendar();
        endDate.add(Calendar.DAY_OF_YEAR, 4); // 4 days to finish
        Task task = new Task("Descr",user,new GregorianCalendar(),endDate,120, manager.getClock());
        proj.bindTask(task);
        assertTrue(proj.getTasks().contains(task));
        proj.remove();
        assertFalse(user.getTasks().contains(task));
    }
    
}
