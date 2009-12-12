package controller;
import model.focus.DeadlineFocus;
import model.focus.DurationFocus;
import model.focus.FocusStrategy;
import model.focus.FocusWork;

import org.junit.Test;

import controller.FocusFactory.FocusType;
import static org.junit.Assert.*;
public class FocusFactoryTest {
    
    /**
     * A FocusWork instance is created. Is it a DeadlineFocus?
     */
    @Test
    public void testCreateDeadline()
    {
        FocusWork work = FocusFactory.createFocus(FocusType.DeadlineFocus, null, 0, 0);
        assertTrue(work.getStrategy() instanceof DeadlineFocus);
    }
    
    /**
     * Is the instance of type Duration?
     */
    @Test
    public void testCreateDuration()
    {
        FocusWork work = FocusFactory.createFocus(FocusType.DurationFocus, null, 0, 0);
        assertTrue(work.getStrategy() instanceof DurationFocus);
    }
    
    /**
     * No parameters given. Should return a default FocusStrategy
     */
    @Test
    public void testCreateDefault()
    {
        FocusWork work = FocusFactory.createFocus(FocusType.Default, null, 0, 0);
        assertTrue(work.getStrategy() instanceof FocusStrategy);
    }
}
