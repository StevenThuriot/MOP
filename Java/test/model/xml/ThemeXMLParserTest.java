package model.xml;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import javax.naming.NameNotFoundException;

import model.ResourceType;
import model.TaskType;
import model.UserType;
import model.repositories.RepositoryManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.DOMException;

import controller.DispatchController;
import exception.EmptyStringException;
public class ThemeXMLParserTest {

	private RepositoryManager manager;
	private DispatchController dcontroller;
	private ThemeXMLDAO parser;
	Map<String, TaskType> taskTypeMap;
	Map<String, ResourceType> resourceTypeMap;
	Map<String, UserType> userTypeMap;

	@Before
	public void setUp() throws NameNotFoundException, NullPointerException, DOMException, EmptyStringException
	{
		manager = new RepositoryManager();
        dcontroller = new DispatchController(manager);
        parser = new ThemeXMLDAO("theme_development_1.xml",dcontroller);
        taskTypeMap  = new HashMap<String, TaskType>();
        resourceTypeMap = new HashMap<String, ResourceType>();
        userTypeMap = new HashMap<String, UserType>();
        
        parser.Parse(taskTypeMap, resourceTypeMap, userTypeMap);
	}
	
	@After
	public void tearDown()
	{
		manager = null;
        parser  = null;
        taskTypeMap = null;
        resourceTypeMap = null;
        userTypeMap = null;
	}
	
	@Test
	public void allReadInMaps()
	{
		assertEquals(4, taskTypeMap.size());
		assertEquals(2, userTypeMap.size());
		assertEquals(2, resourceTypeMap.size());
	}
	
	@Test
	public void allReadInManager()
	{
		assertEquals(4, manager.getTaskTypes().size());
		assertEquals(2, manager.getResourceTypes().size());
		assertEquals(2, manager.getUserTypes().size());
	}
}
