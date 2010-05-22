package controller;

import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import model.focus.DeadlineFocus;
import model.focus.DurationFocus;
import model.focus.FocusStrategy;
import model.focus.FocusType;
import model.focus.FocusWork;
import model.focus.TaskTypeFocus;
import model.repositories.RepositoryManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import exception.ArrayLengthException;
import exception.TimeException;

public class FocusControllerTest {
	private FocusController controller;
    private RepositoryManager manager;
    private DispatchController dcontroller;
    @Before
    public void setUp() throws TimeException, ParseException
    {
        manager = new RepositoryManager();
        dcontroller = new DispatchController(manager);
        controller = dcontroller.getFocusController();
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
        Date date = sdf.parse("2009-10-20T20:00:00");
	    GregorianCalendar gregDate = new GregorianCalendar();
	    gregDate.setTime(date);
	    
        manager.getClock().setTime(gregDate);
    }
    @After
    public void tearDown()
    {
        manager = null;
        controller  = null;
        dcontroller = null;
    }
    
    /**
     * A FocusWork instance is created. Is it a DeadlineFocus?
     * @throws ArrayLengthException 
     */
    @Test
    public void testCreateDeadline() throws ArrayLengthException
    {
    	Object[] settings = new Object[] {0};
    	
        FocusWork work = controller.createFocus(FocusType.DeadlineFocus, null, settings);
        assertTrue(work.getStrategy() instanceof DeadlineFocus);
    }
    
    /**
     * Is the instance of type Duration?
     * @throws ArrayLengthException 
     */
    @Test
    public void testCreateDuration() throws ArrayLengthException
    {
    	Object[] settings = new Object[] {0,0};
    	
        FocusWork work = controller.createFocus(FocusType.DurationFocus, null, settings);
        assertTrue(work.getStrategy() instanceof DurationFocus);
    }
    
    /**
     * Is the instance of type TaskType
     * @throws ArrayLengthException
     */
    @Test
    public void testCreateTaskType() throws ArrayLengthException
    {
    	Object[] settings = new Object[]{0,null};
    	
    	FocusWork work = controller.createFocus(FocusType.TaskTypeFocus, null, settings);
        assertTrue(work.getStrategy() instanceof TaskTypeFocus);
    }
    
    /**
     * No parameters given. Should return a default FocusStrategy
     * @throws ArrayLengthException 
     */
    @Test
    public void testCreateDefault() throws ArrayLengthException
    {
    	Object[] settings = new Object[0];
    	
        FocusWork work = controller.createFocus(FocusType.Default, null, settings);
        assertTrue(work.getStrategy() instanceof FocusStrategy);
    }
}
