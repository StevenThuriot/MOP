package model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import exception.EmptyStringException;
import exception.IllegalStateCallException;

public class ProjectTest {
    
    
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
    
}
