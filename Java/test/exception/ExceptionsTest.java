package exception;

import model.Invitation.InvitationState;

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
        //BusinessRule1Exception
        assertEquals("Business rule 2 violation.", (new BusinessRule2Exception()).getMessage());
        assertEquals("MSG", (new BusinessRule2Exception("MSG")).getMessage());
        //BusinessRule1Exception
        assertEquals("Business rule 3 violation.", (new BusinessRule3Exception()).getMessage());
        assertEquals("MSG", (new BusinessRule3Exception("MSG")).getMessage());
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
        //UnknownStateException
        assertEquals("An unknown state has been passed to the parser.", (new UnknownStateException()).getMessage());
        assertEquals("MSG", (new UnknownStateException("MSG")).getMessage());
        //InvitationExistsException
        assertEquals("Invitation was already created for this combination User/Task", (new InvitationExistsException()).getMessage());
        assertEquals("MSG", (new InvitationExistsException("MSG")).getMessage());
        //InvitationNotPendingException
        assertEquals("Can't update this invitation's state. It is already been accepted or declined.",(new InvitationNotPendingException()).getMessage());
        assertEquals(InvitationState.ACCEPTED, (new InvitationNotPendingException(InvitationState.ACCEPTED)).getState());
        //InvitationInvitesOwnerException
        assertEquals("You can't invite the owner of a task to help on that task", (new InvitationInvitesOwnerException()).getMessage());
    }
}
