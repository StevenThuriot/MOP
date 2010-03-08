package controller;
import model.focus.DeadlineFocus;
import model.focus.DurationFocus;
import model.focus.FocusStrategy;
import model.focus.FocusWork;

import org.junit.Test;

import controller.FocusFactory.FocusType;
import exception.ArrayLengthException;
import static org.junit.Assert.*;
public class FocusFactoryTest {
    
    /**
     * A FocusWork instance is created. Is it a DeadlineFocus?
     * @throws ArrayLengthException 
     */
    @Test
    public void testCreateDeadline() throws ArrayLengthException
    {
    	int[] settings = new int[] {0};
    	
        FocusWork work = FocusFactory.createFocus(FocusType.DeadlineFocus, null, settings);
        assertTrue(work.getStrategy() instanceof DeadlineFocus);
    }
    
    /**
     * Is the instance of type Duration?
     * @throws ArrayLengthException 
     */
    @Test
    public void testCreateDuration() throws ArrayLengthException
    {
    	int[] settings = new int[] {0,0};
    	
        FocusWork work = FocusFactory.createFocus(FocusType.DurationFocus, null, settings);
        assertTrue(work.getStrategy() instanceof DurationFocus);
    }
    
    /**
     * No parameters given. Should return a default FocusStrategy
     * @throws ArrayLengthException 
     */
    @Test
    public void testCreateDefault() throws ArrayLengthException
    {
    	int[] settings = new int[0];
    	
        FocusWork work = FocusFactory.createFocus(FocusType.Default, null, settings);
        assertTrue(work.getStrategy() instanceof FocusStrategy);
    }
}
