package controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import javax.naming.NameNotFoundException;

import model.Resource;
import model.Task;
import model.User;
import model.repositories.RepositoryManager;
import model.xml.DataXMLDAO;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.DOMException;

import exception.BusinessRule1Exception;
import exception.BusinessRule2Exception;
import exception.BusinessRule3Exception;
import exception.DependencyCycleException;
import exception.DependencyException;
import exception.EmptyStringException;
import exception.IllegalStateCallException;
import exception.IllegalStateChangeException;
import exception.NotAvailableException;
import exception.TimeException;
import exception.UnknownStateException;

public class XMLControllerTest {
    private XMLController controller;
    private RepositoryManager manager;
    private DispatchController dcontroller;
    @Before
    public void setUp() throws TimeException, ParseException
    {
        manager = new RepositoryManager();
        dcontroller = new DispatchController(manager);
        controller = dcontroller.getXmlController();
        
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
     * Hardcoded tests the parser for the given model in students_public.xml
     * This test simply tests amounts of objects in their Information Experts
     * @throws NameNotFoundException
     * @throws DOMException
     * @throws EmptyStringException
     * @throws ParseException
     * @throws BusinessRule1Exception
     * @throws DependencyCycleException
     * @throws DependencyException
     * @throws IllegalStateCallException 
     * @throws NullPointerException 
     * @throws BusinessRule3Exception 
     * @throws NotAvailableException 
     * @throws UnknownStateException 
     * @throws BusinessRule2Exception 
     * @throws IllegalStateChangeException 
     */
    @Test
    public void testModelParseAmounts() throws NameNotFoundException, DOMException, EmptyStringException, ParseException, BusinessRule1Exception, DependencyCycleException, DependencyException, NullPointerException, IllegalStateCallException, BusinessRule3Exception, NotAvailableException, UnknownStateException, IllegalStateChangeException, BusinessRule2Exception
    {
        User result = controller.parse("students_public.xml","theme_development_1.xml", dcontroller);
        manager.add(result);
        assertEquals(2,manager.getProjects().size());
        assertEquals(4,manager.getResources().size());
        assertEquals(4,result.getTasks().size());
        assertEquals(4, dcontroller.getResourceController().getReservations().size());
    }
    
    /**
     * This test will test the relations between different objects
     * @throws NameNotFoundException
     * @throws DOMException
     * @throws EmptyStringException
     * @throws ParseException
     * @throws BusinessRule1Exception
     * @throws DependencyCycleException
     * @throws DependencyException
     * @throws IllegalStateCallException 
     * @throws NullPointerException 
     * @throws BusinessRule3Exception 
     * @throws NotAvailableException 
     * @throws UnknownStateException 
     * @throws BusinessRule2Exception 
     * @throws IllegalStateChangeException 
     */
    @Test
    public void testRelations() throws NameNotFoundException, DOMException, EmptyStringException, ParseException, BusinessRule1Exception, DependencyCycleException, DependencyException, NullPointerException, IllegalStateCallException, BusinessRule3Exception, NotAvailableException, UnknownStateException, IllegalStateChangeException, BusinessRule2Exception
    {
        User result = controller.parse("students_public.xml","theme_development_1.xml", dcontroller);
        Resource devRoom = manager.getResources().get(0); //Should be the 'Development room' resource

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
        Date date = sdf.parse("2009-10-01T08:00:00");
	    GregorianCalendar gregDate = new GregorianCalendar();
	    gregDate.setTime(date);
        
        assertFalse(devRoom.availableAt(gregDate, 10)); //Should comply with the reservation at 2009-10-21T08:00:00 for 3060 minutes
        
        Task taskMakeDesign = result.getTasks().get(2); //Should be the task 'Make UML Design'
        assertTrue(taskMakeDesign.getRequiredResources().contains(devRoom)); //This task requires the dev room
    }
    
    
}
