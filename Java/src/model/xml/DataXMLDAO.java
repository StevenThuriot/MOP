package model.xml;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.naming.NameNotFoundException;

import controller.*;

import model.Field;
import model.Invitation;
import model.Project;
import model.Resource;
import model.ResourceType;
import model.Task;
import model.TaskTimings;
import model.TaskType;
import model.User;
import model.UserType;
import model.repositories.RepositoryManager;

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

/**
 * Usage: Make new XMLParser object and pass along the file location. 
 * Ask the object to Parse() and it gives back the user.
 */
public class DataXMLDAO {
	private XMLParser parser;
	RepositoryManager manager;
	private DispatchController controller;
	
	private HashMap<String, Project> projectMap = new HashMap<String, Project>();
	
	private HashMap<String, Task> taskMap = new HashMap<String, Task>();
	private LinkedHashMap<Task, String> stateMap = new LinkedHashMap<Task, String>();
	
	private HashMap<String, Resource> resourceMap = new HashMap<String, Resource>();
	private Map<String,User> users = new HashMap<String,User>();
	
	Map<String, TaskType> taskTypeMap = null;
	Map<String, ResourceType> resourceTypeMap = null;
	Map<String, UserType> userTypeMap = null;
	
	private Boolean ENABLE_DEBUG = false;
	
	/**
	 * Top Secret Debugging Tool!
	 * @param message
	 */
	private void debug(String message)
	{
		if (ENABLE_DEBUG)
			System.out.println(message);
	}
	
	/**
	 * Constructor
	 * @param filename
	 * @param userTypeMap 
	 * @param resourceTypeMap 
	 * @param taskTypeMap 
	 */
	public DataXMLDAO(String filename, RepositoryManager manager, DispatchController controller, Map<String, TaskType> taskTypeMap, Map<String, ResourceType> resourceTypeMap, Map<String, UserType> userTypeMap)
	{
		this.controller = controller;
		this.parser = new XMLParser(filename);
		this.manager = manager;
		
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
	 * @throws IllegalStateCallException 
	 * @throws NullPointerException 
	 * @throws BusinessRule3Exception 
	 * @throws NotAvailableException 
	 * @throws UnknownStateException 
	 * @throws BusinessRule2Exception 
	 * @throws IllegalStateChangeException 
	 * @throws AssetAllocatedException 
	 * @throws BadAllocationTimingException 
	 * @throws WrongFieldsForChosenTypeException 
	 * @throws NonExistingTypeSelected 
	 * @throws WrongUserForTaskTypeException 
	 * @throws AssetConstraintFullException 
	 * @throws AssetTypeNotRequiredException 
	 * @throws TimeException 
	 * @throws InvitationInvitesOwnerException 
	 * @throws InvitationNotPendingException 
	 */
	public ArrayList<User> Parse() throws NameNotFoundException, DOMException, EmptyStringException, ParseException, BusinessRule1Exception, DependencyCycleException, NullPointerException, IllegalStateCallException, BusinessRule3Exception, NotAvailableException, UnknownStateException, IllegalStateChangeException, BusinessRule2Exception, BadAllocationTimingException, AssetAllocatedException, WrongFieldsForChosenTypeException, NonExistingTypeSelected, WrongUserForTaskTypeException, AssetTypeNotRequiredException, AssetConstraintFullException, TimeException, InvitationInvitesOwnerException, InvitationNotPendingException
	{
		parseResources();
		parseProjects();
				
		NodeList allNodes = parser.getRootNode().getChildNodes();
		
		debug("------ Printing all users ------");
		for(int teller = 0; teller < allNodes.getLength(); teller++){
			Node userNode = allNodes.item(teller);
			if (userNode.getNodeName() != "#text" && userNode.getNodeName() != "#comment" && userNode.getNodeName().equals("mop:user"))
			{
				
				debug(parser.getNodeByName(userNode, "mop:name").getTextContent());

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
				parseTasksInit(userNode, user);
				users.put(userNode.getAttributes().getNamedItem("id").getTextContent(), user);
				debug("---- Finished parsing tasks for " + user.getName() + "----");
			}
		}
		
		for(int i=0;i<allNodes.getLength();i++)
		{
			Node userNode = allNodes.item(i);
			if (userNode.getNodeName() != "#text" && userNode.getNodeName()  != "#comment" && userNode.getNodeName().equals("mop:user"))
			{
				User user = users.get(userNode.getAttributes().getNamedItem("id").getTextContent());
				parseInvitations(userNode,user);
			}
		}
		
		for(int i=0;i<allNodes.getLength();i++)
		{
			Node userNode = allNodes.item(i);
			if (userNode.getNodeName() != "#text" && userNode.getNodeName() != "#comment" && userNode.getNodeName().equals("mop:user"))
			{
				User user = users.get(userNode.getAttributes().getNamedItem("id").getTextContent());
				parseTasksLink(userNode,user);
			}
		}
		
		parseTasksStates();
		
		parseTime();

		return new ArrayList<User>(users.values());
	}
	
	/**
	 * Setting the system time.
	 * @param time
	 * @throws TimeException
	 * @throws ParseException 
	 */
	private void setTime(String time) throws TimeException, ParseException
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
        Date date = sdf.parse(time);
	    GregorianCalendar gregDate = new GregorianCalendar();
	    gregDate.setTime(date);
	    
	    debug("Setting the system time: " + time);
	    
        manager.getClock().setTime(gregDate);
	}
	
	/**
	 * Setting the system time.
	 * @param time
	 * @throws TimeException
	 */
	private void setTime(GregorianCalendar gregDate) throws TimeException
	{	    
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
	    debug("Setting the system time: " + sdf.format(gregDate.getTime()));
	    
        manager.getClock().setTime(gregDate);
	}

	/**
	 * Parsing system time
	 * @throws NameNotFoundException
	 * @throws DOMException
	 * @throws ParseException
	 * @throws TimeException
	 */
	private void parseTime() throws NameNotFoundException, DOMException, ParseException, TimeException {
		debug("Parsing the time");
		
		Node systemTime = parser.getNodeByName(parser.getRootNode(), "mop:systemtime");
		
		setTime(systemTime.getTextContent());
	}

	/**
	 * Parsing the tasks, making Reservations.
	 * @param userNode
	 * @param user
	 * @throws NameNotFoundException
	 * @throws ParseException
	 * @throws EmptyStringException
	 * @throws BusinessRule1Exception
	 * @throws IllegalStateCallException
	 * @throws BusinessRule3Exception
	 * @throws NotAvailableException
	 * @throws BadAllocationTimingException
	 * @throws AssetAllocatedException
	 * @throws WrongFieldsForChosenTypeException 
	 * @throws NullPointerException 
	 * @throws NonExistingTypeSelected 
	 * @throws WrongUserForTaskTypeException 
	 * @throws AssetConstraintFullException 
	 * @throws AssetTypeNotRequiredException 
	 */
	private void parseTasksInit(Node userNode, User user) throws NameNotFoundException, ParseException, EmptyStringException, BusinessRule1Exception,
			IllegalStateCallException, BusinessRule3Exception, NotAvailableException, BadAllocationTimingException, AssetAllocatedException, NullPointerException, WrongFieldsForChosenTypeException, NonExistingTypeSelected, WrongUserForTaskTypeException, AssetTypeNotRequiredException, AssetConstraintFullException 
	{
		Node tasks = parser.getNodeByName(userNode, "mop:tasks");
		NodeList taskList = tasks.getChildNodes();
		
		injectTasks(user, taskList, stateMap);
	}
	
	/**
	 * Parsing the tasks: linking dependencies
	 * @param userNode
	 * @param user
	 * @throws NameNotFoundException
	 * @throws BusinessRule1Exception
	 * @throws DependencyCycleException
	 * @throws IllegalStateCallException
	 * @throws BusinessRule2Exception
	 * @throws NullPointerException 
	 */
	private void parseTasksLink(Node userNode, User user) throws NameNotFoundException, BusinessRule1Exception,
			DependencyCycleException, IllegalStateCallException, BusinessRule2Exception, NullPointerException 
	{
		Node tasks = parser.getNodeByName(userNode, "mop:tasks");
		NodeList taskList = tasks.getChildNodes();
		
		linkDepedencies(taskList);
	}
	
	/**
	 * Parsing the tasks, making Reservations.
	 * @param userNode
	 * @param user
	 * @throws BusinessRule3Exception
	 * @throws UnknownStateException
	 * @throws IllegalStateChangeException
	 * @throws BusinessRule2Exception
	 * @throws NullPointerException 
	 * @throws TimeException 
	 */
	private void parseTasksStates() throws BusinessRule3Exception, UnknownStateException, IllegalStateChangeException, BusinessRule2Exception, NullPointerException, TimeException 
	{
		setTaskStates(stateMap);
	}

	/**
	 * Setting the tasks to their correct states
	 * @param stateMap
	 * @throws UnknownStateException
	 * @throws BusinessRule3Exception
	 * @throws IllegalStateChangeException
	 * @throws BusinessRule2Exception
	 * @throws TimeException 
	 */
	private void setTaskStates(LinkedHashMap<Task, String> stateMap) throws UnknownStateException, BusinessRule3Exception, IllegalStateChangeException, BusinessRule2Exception, TimeException {
		debug("--- Start setting task states ---");
		
		ArrayList<Task> tasks = new ArrayList<Task>(stateMap.keySet());
		Comparator<Task> comp = new Comparator<Task>() {
			@Override
			public int compare(Task o1, Task o2) {
				return o1.getEarliestExecTime().compareTo(o2.getEarliestExecTime());
			}
		};
		Collections.sort(tasks, comp);
		for (Task task : tasks) {
			String state = stateMap.get(task);
			
			debug(task.getDescription()+":"+state);
			if(state.equals("Successful"))
				this.setTime( task.getEarliestExecTime() );
			controller.getTaskController().parseStateString(task, state);
		}
		
		debug("--- Stop setting task states ---");
	}

	/**
	 * Linking the dependant tasks
	 * @param taskList
	 * @throws NameNotFoundException
	 * @throws IllegalStateCallException
	 * @throws BusinessRule1Exception
	 * @throws DependencyCycleException
	 * @throws BusinessRule2Exception 
	 */
	private void linkDepedencies(NodeList taskList) throws NameNotFoundException, IllegalStateCallException, BusinessRule1Exception, DependencyCycleException, BusinessRule2Exception {
		for (int i = 0; i < taskList.getLength(); i++) {
			Node childNode = taskList.item(i);
			
			if (childNode.getNodeName() != "#text" && childNode.getNodeName() != "#comment" && childNode.getNodeName().length() > 0)
		    {
				String id = childNode.getAttributes().item(0).getTextContent();
								
				Node dependsNode = parser.getNodeByName(childNode, "mop:dependsOn");
				NodeList dependsList = dependsNode.getChildNodes();		    
				ArrayList<Task> dependencyList = new ArrayList<Task>();
				
				for (int j = 0; j < dependsList.getLength(); j++) {
					Node depChild = dependsList.item(j);
					if (depChild.getNodeName() != "#text" && depChild.getNodeName() != "#comment" && depChild.getNodeName().length() > 0) {
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
	 * @throws IllegalStateCallException
	 * @throws BusinessRule3Exception
	 * @throws AssetAllocatedException 
	 * @throws BadAllocationTimingException 
	 * @throws NotAvailableException 
	 * @throws WrongFieldsForChosenTypeException 
	 * @throws NullPointerException 
	 * @throws NonExistingTypeSelected 
	 * @throws WrongUserForTaskTypeException 
	 * @throws AssetConstraintFullException 
	 * @throws AssetTypeNotRequiredException 
	 */
	@SuppressWarnings("unchecked")
	private void injectTasks(User user, NodeList taskList, LinkedHashMap<Task, String> stateMap) throws NameNotFoundException, ParseException, EmptyStringException,
			BusinessRule1Exception, IllegalStateCallException, BusinessRule3Exception, NotAvailableException, BadAllocationTimingException, AssetAllocatedException, NullPointerException, WrongFieldsForChosenTypeException, NonExistingTypeSelected, WrongUserForTaskTypeException, AssetTypeNotRequiredException, AssetConstraintFullException {
		
		for (int i = 0; i < taskList.getLength(); i++) {
			Node childNode = taskList.item(i);
			
			if (childNode.getNodeName() != "#text" && childNode.getNodeName() != "#comment" && childNode.getNodeName().length() > 0)
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
					
					if (fieldNode.getNodeName() != "#text" && fieldNode.getNodeName() != "#comment" && fieldNode.getNodeName().length() > 0)
				    {
						for (Field field : fields) {
							String fieldID = field.getId();
							String nodeID = fieldNode.getAttributes().item(0).getTextContent();
							if (fieldID.equals(nodeID)) {
								String value = fieldNode.getAttributes().item(1).getTextContent();
								
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
			    Project project = projectMap.get(projectID);
			    Task task = controller.getTaskController().createTask(id, theTaskType, fields, user, new TaskTimings(startDate, dueDate, duration), project);
			    
			    stateMap.put(task, state);
			    
			    parseReservations(childNode, task);
			    
			    taskMap.put(id, task);
		    }
		}
	}

	/**
	 * Parsing the inviations
	 * @param invitationNode
	 * @param user
	 * @throws NameNotFoundException
	 * @throws AssetAllocatedException
	 * @throws InvitationInvitesOwnerException
	 * @throws IllegalStateCallException
	 * @throws AssetTypeNotRequiredException
	 * @throws AssetConstraintFullException
	 * @throws InvitationNotPendingException
	 */
	private void parseInvitations(Node invitationNode, User user) throws NameNotFoundException, AssetAllocatedException, InvitationInvitesOwnerException, IllegalStateCallException, AssetTypeNotRequiredException, AssetConstraintFullException, InvitationNotPendingException {
		debug("--- Start pasring Invitations for "+user.getDescription()+" ---");
		Node invitations = parser.getNodeByName(invitationNode, "mop:invitations");
		NodeList invitationList = invitations.getChildNodes();
		for(int i=0;i<invitationList.getLength();i++){
			Node childNode=invitationList.item(i);
			if(childNode.getNodeName()!="#text" && childNode.getNodeName() != "#comment")
			{
				Task task = taskMap.get(childNode.getAttributes().getNamedItem("task").getTextContent());
				Invitation createdInvitation = this.controller.getInvitationController().createInvitation(task, user);
				if(childNode.getAttributes().getNamedItem("status").getTextContent().equals("accepted"))
					createdInvitation.accept();
				else if(childNode.getAttributes().getNamedItem("status").getTextContent().equals("declined"))
					createdInvitation.decline();
				debug(createdInvitation.toString());
			}
		}
		debug("--- Stop parsing Invitations for "+user.getDescription()+" ---");
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
	 * @throws BadAllocationTimingException 
	 * @throws AssetConstraintFullException 
	 * @throws AssetTypeNotRequiredException 
	 */
	private void parseReservations(Node userNode, Task task) throws NameNotFoundException, ParseException, NotAvailableException, BadAllocationTimingException, AssetAllocatedException, IllegalStateCallException, AssetTypeNotRequiredException, AssetConstraintFullException {
		debug("--- Start parsing reservations ---");
		
		Node reservations = parser.getNodeByName(userNode, "mop:reservations");
		NodeList reservationList = reservations.getChildNodes();
		for (int i = 0; i < reservationList.getLength(); i++) {
			Node childNode = reservationList.item(i);
			
			if (childNode.getNodeName() != "#text" && childNode.getNodeName() != "#comment")
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
		
		debug("--- Stop parsing reservations ---");
	}

	/**
	 * Parsing Projects and placing them in a Map
	 * @throws NameNotFoundException
	 * @throws EmptyStringException
	 */
	private void parseProjects() throws NameNotFoundException, EmptyStringException {
		debug("--- Start parsing projects ---");
		
		Node projects = parser.getNodeByName(parser.getRootNode(), "mop:projects");
		NodeList projectList = projects.getChildNodes();
		
		for (int i = 0; i < projectList.getLength(); i++) {
			Node childNode = projectList.item(i);
			
			if (childNode.getNodeName() != "#text" && childNode.getNodeName() != "#comment")
		    {
				String id = childNode.getAttributes().item(0).getTextContent();
				String description = parser.getNodeByName(childNode, "mop:description").getTextContent();
				
				projectMap.put(id, controller.getProjectController().createProject(description));				
		    }
		}
		
		debug("--- Stop parsing projects ---");
	}

	/**
	 * Parsing Resources and placing them in a Map
	 * @throws NameNotFoundException
	 * @throws EmptyStringException
	 * @throws NonExistingTypeSelected 
	 */
	private void parseResources() throws NameNotFoundException, EmptyStringException, NonExistingTypeSelected {
		debug("--- Start parsing resources ---");
		
		Node resources = parser.getNodeByName(parser.getRootNode(), "mop:resources");
		NodeList resourceList = resources.getChildNodes();
				
		for (int i = 0; i < resourceList.getLength(); i++) {
			Node childNode = resourceList.item(i);
			
			if (childNode.getNodeName() != "#text" && childNode.getNodeName() != "#comment")
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
		
		debug("--- Stop parsing resources ---");
	}
}