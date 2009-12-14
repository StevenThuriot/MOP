package exception;

import org.junit.Test;
import static org.junit.Assert.*;
public class ExceptionsTest {
    /**
     * Method to test the instantiation of all the exceptions
     */
    @Test
    public void testExceptions()
    {
        //BusinessRule1Exception
        assertEquals("Business rule 1 violation.", (new BusinessRule1Exception()).getMessage());
        assertEquals("MSG", (new BusinessRule1Exception("MSG")).getMessage());
        //DependencyCycleException
        assertEquals("MSG", (new DependencyCycleException("MSG")).getMessage());
        //DependencyException
        assertEquals("MSG", (new DependencyException("MSG")).getMessage());
        //EmptyStringException
        assertEquals("An empty string was passed", (new EmptyStringException()).getMessage());
        assertEquals("MSG", (new EmptyStringException("MSG")).getMessage());
        //IllegalStateCall
        assertEquals("Not allowed to call this method in the task's current state.", (new IllegalStateCallException()).getMessage());
        assertEquals("MSG", (new IllegalStateCallException("MSG")).getMessage());
        //IllegalStateChangeException
        assertEquals("Not allowed to change to this state from current state.", (new IllegalStateChangeException()).getMessage());
        assertEquals("MSG", (new IllegalStateChangeException("MSG")).getMessage());
        //NotAvailableException
        assertEquals("MSG", (new NotAvailableException("MSG")).getMessage());
        //ResourceBusyException
        assertEquals("MSG", (new ResourceBusyException("MSG")).getMessage());
        //TaskFailedException
        assertEquals("MSG", (new TaskFailedException("MSG")).getMessage());
    }
}
