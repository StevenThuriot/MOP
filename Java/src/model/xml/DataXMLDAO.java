package model.xml;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.naming.NameNotFoundException;

import controller.*;

import model.Field;
import model.Project;
import model.Resource;
import model.ResourceType;
import model.Task;
import model.TaskTimings;
import model.TaskType;
import model.User;
import model.UserType;

import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import exception.AssetAllocatedException;
import exception.AssetConstraintFullException;
import exception.AssetTypeNotRequiredException;
import exception.BusinessRule1Exception;
import exception.BusinessRule2Exception;
import exception.BusinessRule3Exception;
import exception.DependencyCycleException;
import exception.DependencyException;
import exception.EmptyStringException;
import exception.IllegalStateCallException;
import exception.IllegalStateChangeException;
import exception.NoReservationOverlapException;
import exception.NonExistingTypeSelected;
import exception.NotAvailableException;
import exception.UnknownStateException;
import exception.WrongFieldsForChosenTypeException;
import exception.WrongUserForTaskTypeException;

/**
 * Usage: Make new XMLParser object and pass along the file location. 
 * Ask the object to Parse() and it gives back the user.
 */
public class DataXMLDAO {
	private XMLParser parser;
	
	private DispatchController controller;
	
	private HashMap<String, Project> projectMap = new HashMap<String, Project>();
	private HashMap<String, Task> taskMap = new HashMap<String, Task>();
	private HashMap<String, Resource> resourceMap = new HashMap<String, Resource>();
	
	Map<String, TaskType> taskTypeMap = null;
	Map<String, ResourceType> resourceTypeMap = null;
	Map<String, UserType> userTypeMap = null;
	
	/**
	 * Constructor
	 * @param filename
	 * @param userTypeMap 
	 * @param resourceTypeMap 
	 * @param taskTypeMap 
	 */
	public DataXMLDAO(String filename, DispatchController controller, Map<String, TaskType> taskTypeMap, Map<String, ResourceType> resourceTypeMap, Map<String, UserType> userTypeMap)
	{
		this.controller = controller;
		this.parser = new XMLParser(filename);
		
		this.taskTypeMap = taskTypeMap;
		this.resourceTypeMap = resourceTypeMap;
		this.userTypeMap = userTypeMap;
	}

	
	/**
	 * Creates the all elements in the XML file and gives back a user.
	 * @return
	 * @throws NameNotFoundException
	 * @throws DOMException
	 * @throws EmptyStringException
	 * @throws ParseException 
	 * @throws DependencyCycleException 
	 * @throws BusinessRule1Exception 
	 * @throws DependencyException 
	 * @throws IllegalStateCallException 
	 * @throws NullPointerException 
	 * @throws BusinessRule3Exception 
	 * @throws NotAvailableException 
	 * @throws UnknownStateException 
	 * @throws BusinessRule2Exception 
	 * @throws IllegalStateChangeException 
	 * @throws AssetAllocatedException 
	 * @throws NoReservationOverlapException 
	 * @throws WrongFieldsForChosenTypeException 
	 * @throws NonExistingTypeSelected 
	 * @throws WrongUserForTaskTypeException 
	 * @throws AssetConstraintFullException 
	 * @throws AssetTypeNotRequiredException 
	 */
	public ArrayList<User> Parse() throws NameNotFoundException, DOMException, EmptyStringException, ParseException, BusinessRule1Exception, DependencyCycleException, DependencyException, NullPointerException, IllegalStateCallException, BusinessRule3Exception, NotAvailableException, UnknownStateException, IllegalStateChangeException, BusinessRule2Exception, NoReservationOverlapException, AssetAllocatedException, WrongFieldsForChosenTypeException, NonExistingTypeSelected, WrongUserForTaskTypeException, AssetTypeNotRequiredException, AssetConstraintFullException
	{
		parseResources();
		parseProjects();
				
		ArrayList<User> users = new ArrayList<User>();
		NodeList allNodes = parser.getRootNode().getChildNodes();
				
		for(int i=0; i<allNodes.getLength(); i++){
			Node userNode = allNodes.item(i);
			  
			if (userNode.getNodeName() != "#text" && userNode.getNodeName().equals("mop:user"))
			{
				Node userName = parser.getNodeByName(userNode, "mop:name");
				Node userType = parser.getNodeByName(userNode, "mop:type");
				
				UserType typeOfUser = null;
				
				if (userTypeMap.containsKey(userType.getTextContent()))
				{
					typeOfUser = userTypeMap.get(userType.getTextContent());
				} 
				else
				{
					throw new NonExistingTypeSelected();
				}
					
				
				User user = new User(userName.getTextContent(), typeOfUser);
				
				parseTasks(userNode, user);
				
				users.add(user);
			}
		}

		return users;
	}

	/**
	 * Parsing the tasks, linking them and setting the correct states.
	 * @param userNode
	 * @param user
	 * @throws NameNotFoundException
	 * @throws ParseException
	 * @throws EmptyStringException
	 * @throws BusinessRule1Exception
	 * @throws DependencyCycleException
	 * @throws IllegalStateCallException
	 * @throws BusinessRule3Exception
	 * @throws UnknownStateException
	 * @throws IllegalStateChangeException
	 * @throws BusinessRule2Exception
	 * @throws NotAvailableException
	 * @throws NoReservationOverlapException
	 * @throws AssetAllocatedException
	 * @throws WrongFieldsForChosenTypeException 
	 * @throws NullPointerException 
	 * @throws NonExistingTypeSelected 
	 * @throws WrongUserForTaskTypeException 
	 * @throws AssetConstraintFullException 
	 * @throws AssetTypeNotRequiredException 
	 */
	private void parseTasks(Node userNode, User user) throws NameNotFoundException, ParseException, EmptyStringException, BusinessRule1Exception,
			DependencyCycleException, IllegalStateCallException, BusinessRule3Exception, UnknownStateException, IllegalStateChangeException, BusinessRule2Exception, NotAvailableException, NoReservationOverlapException, AssetAllocatedException, NullPointerException, WrongFieldsForChosenTypeException, NonExistingTypeSelected, WrongUserForTaskTypeException, AssetTypeNotRequiredException, AssetConstraintFullException {
		Node tasks = parser.getNodeByName(userNode, "mop:tasks");
		NodeList taskList = tasks.getChildNodes();
		
		HashMap<Task, String> stateMap = new HashMap<Task, String>();
		injectTasks(user, taskList, stateMap);
		
		linkDepedencies(taskList);
		
		setTaskStates(stateMap);
	}

	/**
	 * Setting the tasks to their correct states
	 * @param stateMap
	 * @throws UnknownStateException
	 * @throws BusinessRule3Exception
	 * @throws IllegalStateChangeException
	 * @throws BusinessRule2Exception
	 */
	private void setTaskStates(HashMap<Task, String> stateMap) throws UnknownStateException, BusinessRule3Exception, IllegalStateChangeException, BusinessRule2Exception {
		//Set states
		for (Task task : stateMap.keySet()) {
				controller.getTaskController().parseStateString(task, stateMap.get(task));
		}
	}

	/**
	 * Linking the dependant tasks
	 * @param taskList
	 * @throws NameNotFoundException
	 * @throws IllegalStateCallException
	 * @throws BusinessRule1Exception
	 * @throws DependencyCycleException
	 */
	private void linkDepedencies(NodeList taskList) throws NameNotFoundException, IllegalStateCallException, BusinessRule1Exception, DependencyCycleException {
		for (int i = 0; i < taskList.getLength(); i++) {
			Node childNode = taskList.item(i);
			
			if (childNode.getNodeName() != "#text" && childNode.getNodeName().length() > 0)
		    {
				String id = childNode.getAttributes().item(0).getTextContent();
				
				Node resNode = parser.getNodeByName(childNode, "mop:requiredResources");
				NodeList resList = resNode.getChildNodes();
				
				ArrayList<Resource> requiredResources = new ArrayList<Resource>();
				
				for (int j = 0; j < resList.getLength(); j++) {
					Node resChild = resList.item(j);
					if (resChild.getNodeName() != "#text" && resChild.getNodeName().length() > 0) {
						String requiredResourceID = resChild.getTextContent();
						
						if(requiredResourceID.length() > 0)
						{
							requiredResources.add(resourceMap.get(requiredResourceID));
						}
					}
				}
				
				Node dependsNode = parser.getNodeByName(childNode, "mop:dependsOn");
				NodeList dependsList = dependsNode.getChildNodes();		    
				ArrayList<Task> dependencyList = new ArrayList<Task>();
				
				for (int j = 0; j < dependsList.getLength(); j++) {
					Node depChild = dependsList.item(j);
					if (depChild.getNodeName() != "#text" && depChild.getNodeName().length() > 0) {
						String requiredTaskID = depChild.getTextContent();

						if(requiredTaskID.length() > 0)
						{
							dependencyList.add(taskMap.get(requiredTaskID));
						}
					}
				}
				
				Task task = taskMap.get(id);		
				
				if (dependencyList.size() > 0) {
					for (Task t : dependencyList)
						task.addDependency(t);
				}
		    }
		}
	}

	/**
	 * Parsing the tasks and binding them to their projects, then putting them into a Map
	 * @param user
	 * @param taskList
	 * @param stateMap
	 * @throws NameNotFoundException
	 * @throws ParseException
	 * @throws EmptyStringException
	 * @throws BusinessRule1Exception
	 * @throws DependencyCycleException
	 * @throws IllegalStateCallException
	 * @throws BusinessRule3Exception
	 * @throws AssetAllocatedException 
	 * @throws NoReservationOverlapException 
	 * @throws NotAvailableException 
	 * @throws WrongFieldsForChosenTypeException 
	 * @throws NullPointerException 
	 * @throws NonExistingTypeSelected 
	 * @throws WrongUserForTaskTypeException 
	 * @throws AssetConstraintFullException 
	 * @throws AssetTypeNotRequiredException 
	 */
	@SuppressWarnings("unchecked")
	private void injectTasks(User user, NodeList taskList, HashMap<Task, String> stateMap) throws NameNotFoundException, ParseException, EmptyStringException,
			BusinessRule1Exception, DependencyCycleException, IllegalStateCallException, BusinessRule3Exception, NotAvailableException, NoReservationOverlapException, AssetAllocatedException, NullPointerException, WrongFieldsForChosenTypeException, NonExistingTypeSelected, WrongUserForTaskTypeException, AssetTypeNotRequiredException, AssetConstraintFullException {
		
		for (int i = 0; i < taskList.getLength(); i++) {
			Node childNode = taskList.item(i);
			
			if (childNode.getNodeName() != "#text" && childNode.getNodeName().length() > 0)
		    {
				String id = childNode.getAttributes().item(0).getTextContent();
				String type = parser.getNodeByName(childNode, "mop:type").getTextContent();
				String startString = parser.getNodeByName(childNode, "mop:startDate").getTextContent();
			    			    
			    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
			    Date start = sdf.parse(startString);
			    
			    GregorianCalendar startDate = new GregorianCalendar();
			    startDate.setTime(start);
			    
			    String dueString = parser.getNodeByName(childNode, "mop:endDate").getTextContent();  
			    Date due = sdf.parse(dueString);
			    GregorianCalendar dueDate = new GregorianCalendar();
			    dueDate.setTime(due);
			    
			    int duration = Integer.parseInt(parser.getNodeByName(childNode, "mop:duration").getTextContent());
			    
			    String state = parser.getNodeByName(childNode, "mop:status").getTextContent();
			    
			    String projectID = parser.getNodeByName(childNode, "mop:refProject").getTextContent();
			    
			    List<Field> fields = new ArrayList<Field>();
			    TaskType theTaskType = null;
			    
			    if (taskTypeMap.containsKey(type)) 
			    {
			    	theTaskType = taskTypeMap.get(type);
			    	fields = theTaskType.getTemplate();
				}
			    else
			    {
			    	throw new NonExistingTypeSelected();
			    }	
			    
			    NodeList fieldsNode = parser.getNodeByName(childNode, "mop:fields").getChildNodes();
			    
			    for (int j = 0; j < fieldsNode.getLength(); j++) {
					Node fieldNode = fieldsNode.item(j);
					
					if (fieldNode.getNodeName() != "#text" && fieldNode.getNodeName().length() > 0)
				    {
						for (Field field : fields) {
							if (field.getName().equals(fieldNode.getAttributes().item(0).getTextContent())) {
								String value = fieldNode.getTextContent();
								
								switch (field.getType()) {
								case Numeric:
									field.setValue( Integer.parseInt(value) );
									break;

								case Text:
								default:
									field.setValue( value );
									break;
								}

								break;
							}
						}						
				    }
			    }
			    
			    Task task = controller.getTaskController().createTask(id, theTaskType, fields, user, new TaskTimings(startDate, dueDate, duration));
			    
			    stateMap.put(task, state);
			    
			    parseReservations(childNode, task);
			    			    
			    if (projectID.length() > 0 && projectID != null)
			    {
				    Project project = projectMap.get(projectID);
				    controller.getProjectController().bind(project, task);
			    }
			    
			    taskMap.put(id, task);
		    }
		}
	}

	/**
	 * Parsing the reservations
	 * @param userNode
	 * @param user
	 * @throws NameNotFoundException
	 * @throws ParseException
	 * @throws NotAvailableException
	 * @throws IllegalStateCallException 
	 * @throws AssetAllocatedException 
	 * @throws NoReservationOverlapException 
	 * @throws AssetConstraintFullException 
	 * @throws AssetTypeNotRequiredException 
	 */
	private void parseReservations(Node userNode, Task task) throws NameNotFoundException, ParseException, NotAvailableException, NoReservationOverlapException, AssetAllocatedException, IllegalStateCallException, AssetTypeNotRequiredException, AssetConstraintFullException {
		Node reservations = parser.getNodeByName(userNode, "mop:reservations");
		NodeList reservationList = reservations.getChildNodes();
		for (int i = 0; i < reservationList.getLength(); i++) {
			Node childNode = reservationList.item(i);
			
			if (childNode.getNodeName() != "#text")
		    {
				String startString = parser.getNodeByName(childNode, "mop:time").getTextContent();
			    
			    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
			    Date start = sdf.parse(startString);
			    
			    GregorianCalendar startTime = new GregorianCalendar();
			    startTime.setTime(start);
				
				Integer duration = Integer.parseInt( parser.getNodeByName(childNode, "mop:duration").getTextContent() );
				String refResource = parser.getNodeByName(childNode, "mop:refResource").getTextContent();
				
				Resource resource = resourceMap.get(refResource);
				
				controller.getResourceController().createReservation(startTime, duration, resource, task);	
		    }
		}
	}

	/**
	 * Parsing Projects and placing them in a Map
	 * @throws NameNotFoundException
	 * @throws EmptyStringException
	 */
	private void parseProjects() throws NameNotFoundException, EmptyStringException {
		Node projects = parser.getNodeByName(parser.getRootNode(), "mop:projects");
		NodeList projectList = projects.getChildNodes();
		
		for (int i = 0; i < projectList.getLength(); i++) {
			Node childNode = projectList.item(i);
			
			if (childNode.getNodeName() != "#text")
		    {
				String id = childNode.getAttributes().item(0).getTextContent();
				String description = parser.getNodeByName(childNode, "mop:description").getTextContent();
				
				projectMap.put(id, controller.getProjectController().createProject(description));				
		    }
		}
	}

	/**
	 * Parsing Resources and placing them in a Map
	 * @throws NameNotFoundException
	 * @throws EmptyStringException
	 * @throws NonExistingTypeSelected 
	 */
	private void parseResources() throws NameNotFoundException, EmptyStringException, NonExistingTypeSelected {
		Node resources = parser.getNodeByName(parser.getRootNode(), "mop:resources");
		NodeList resourceList = resources.getChildNodes();
				
		for (int i = 0; i < resourceList.getLength(); i++) {
			Node childNode = resourceList.item(i);
			
			if (childNode.getNodeName() != "#text")
		    {
				String id = childNode.getAttributes().item(0).getTextContent();
				String description = parser.getNodeByName(childNode, "mop:description").getTextContent();
				
				String typeString = parser.getNodeByName(childNode, "mop:type").getTextContent();
				
				ResourceType type = null;
				
				if (resourceTypeMap.containsKey(typeString)) {
					type = resourceTypeMap.get(typeString);
				}
				else
				{
					throw new NonExistingTypeSelected();
				}
				 
				resourceMap.put(id, controller.getResourceController().createResource(description, type));
		    }
		}
	}
}