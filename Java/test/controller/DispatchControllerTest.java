package controller;
import model.repositories.RepositoryManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
public class DispatchControllerTest {

    private DispatchController controller;
    @Before
    public void setUp()
    {
        controller = new DispatchController(new RepositoryManager());
    }
    
    @After
    public void tearDown()
    {
        controller = null;
    }
    /**
     * When instantiating DispatchController with null as argument it should throw a NullPointerException
     * If this doesn't happen, Dependency Injection will not work properly, the application becomes instable
     */
    @Test(expected=NullPointerException.class)
    public void managerThrowsNull()
    {
        controller = new DispatchController(null);
    }
    /**
     * Are all the other controllers properly instantiated?
     */
    @Test
    public void otherControllersAreNotNull()
    {
        assertFalse(controller.getProjectController()==null);
        assertFalse(controller.getResourceController()==null);
        assertFalse(controller.getTaskController()==null);
    }
}
