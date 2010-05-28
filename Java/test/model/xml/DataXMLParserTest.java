package model.xml;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.naming.NameNotFoundException;

import model.ResourceType;
import model.TaskType;
import model.User;
import model.UserType;
import model.repositories.RepositoryManager;
import model.xml.DataXMLDAO;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.DOMException;

import controller.DispatchController;

import exception.AssetAllocatedException;
import exception.AssetConstraintFullException;
import exception.AssetTypeNotRequiredException;
import exception.BusinessRule1Exception;
import exception.BusinessRule2Exception;
import exception.BusinessRule3Exception;
import exception.DependencyCycleException;
import exception.EmptyStringException;
import exception.IllegalStateCallException;
import exception.IllegalStateChangeException;
import exception.InvitationInvitesOwnerException;
import exception.InvitationNotPendingException;
import exception.BadAllocationTimingException;
import exception.NonExistingTypeSelected;
import exception.NotAvailableException;
import exception.TimeException;
import exception.UnknownStateException;
import exception.WrongFieldsForChosenTypeException;
import exception.WrongUserForTaskTypeException;
import static org.junit.Assert.*;
public class DataXMLParserTest {
    private DataXMLDAO parser;
    private RepositoryManager manager;
    private DispatchController dcontroller;    
    Map<String, User> users = new HashMap<String, User>();
    
    
    @Before
    public void setUp() throws TimeException, ParseException, NameNotFoundException, DOMException, NullPointerException, EmptyStringException, BusinessRule1Exception, DependencyCycleException, IllegalStateCallException, BusinessRule3Exception, NotAvailableException, UnknownStateException, IllegalStateChangeException, BusinessRule2Exception, BadAllocationTimingException, AssetAllocatedException, WrongFieldsForChosenTypeException, NonExistingTypeSelected, WrongUserForTaskTypeException, AssetTypeNotRequiredException, AssetConstraintFullException, InvitationInvitesOwnerException, InvitationNotPendingException
    {
        manager = new RepositoryManager();
        dcontroller = new DispatchController(manager);
        
        ThemeXMLDAO themeParser = new ThemeXMLDAO("theme_development_1.xml", dcontroller);
		
		Map<String, TaskType> taskTypeMap = new HashMap<String, TaskType>();
		Map<String, ResourceType> resourceTypeMap = new HashMap<String, ResourceType>();
		Map<String, UserType> userTypeMap = new HashMap<String, UserType>();
		
		themeParser.Parse(taskTypeMap,resourceTypeMap,userTypeMap);
		
		parser = new DataXMLDAO("students_public.xml", manager, dcontroller, taskTypeMap, resourceTypeMap, userTypeMap);
		
		ArrayList<User> parsedUsers = parser.Parse();
		
		for (User user : parsedUsers) {
			manager.add(user);
			users.put(user.getName(), user);
		}
    }
    
    @After
    public void tearDown()
    {
        manager = null;
        parser  = null;
        dcontroller = null;
        users = null;
    }
    /**
     * Testing system time
     * @throws ParseException
    */
    @Test
    public void timeTest() throws ParseException
    {
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
        Date date = sdf.parse("2009-10-22T00:00:00");
	    GregorianCalendar gregDate = new GregorianCalendar();
	    gregDate.setTime(date);
	    
    	assertEquals(gregDate, manager.getClock().getTime());
    }
    
    /**
     * Hardcoded tests the parser for the given model in students_public.xml
     * This test simply tests amounts of objects in their Information Experts
     * @throws DOMException
     * @throws NullPointerException 
     */
    @Test
    public void testModelParseAmounts() throws DOMException, NullPointerException
    {
    	assertEquals(3, users.size());
        assertEquals(1, manager.getProjects().size());
        assertEquals(6, manager.getResources().size());
        
        User Bob = users.get("Bob");
        User Eve = users.get("Eve");
        User Alice = users.get("Alice");
        
        assertEquals(2, Bob.getAllInvitations().size());
        
        assertEquals(0, Bob.getTasks().size());
        assertEquals(0, Eve.getTasks().size());
        assertEquals(5, Alice.getTasks().size());
    }
}
