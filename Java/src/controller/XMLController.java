package controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.naming.NameNotFoundException;

import org.w3c.dom.DOMException;

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
import model.ResourceType;
import model.TaskType;
import model.User;
import model.UserType;
import model.repositories.RepositoryManager;
import model.xml.DataXMLDAO;
import model.xml.ThemeXMLDAO;

public class XMLController {
	
	RepositoryManager manager = null;
	
	public XMLController(RepositoryManager manager)
	{
		this.manager = manager;
	}

	/**
	 * Parses the given XML file
	 * @param filename
	 * @param controller
	 * @return
	 * @throws NameNotFoundException
	 * @throws DOMException
	 * @throws NullPointerException
	 * @throws EmptyStringException
	 * @throws ParseException
	 * @throws BusinessRule1Exception
	 * @throws DependencyCycleException
	 * @throws IllegalStateCallException
	 * @throws BusinessRule3Exception
	 * @throws NotAvailableException
	 * @throws UnknownStateException
	 * @throws IllegalStateChangeException
	 * @throws BusinessRule2Exception
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
	public ArrayList<User> parse(String dataFilename,String themefilename, DispatchController controller) throws NameNotFoundException, DOMException, NullPointerException, EmptyStringException, ParseException, BusinessRule1Exception, DependencyCycleException, IllegalStateCallException, BusinessRule3Exception, NotAvailableException, UnknownStateException, IllegalStateChangeException, BusinessRule2Exception, BadAllocationTimingException, AssetAllocatedException, WrongFieldsForChosenTypeException, NonExistingTypeSelected, WrongUserForTaskTypeException, AssetTypeNotRequiredException, AssetConstraintFullException, TimeException, InvitationInvitesOwnerException, InvitationNotPendingException
	{
		ThemeXMLDAO themeParser = new ThemeXMLDAO(themefilename, controller);
		
		Map<String, TaskType> taskTypeMap = new HashMap<String, TaskType>();
		Map<String, ResourceType> resourceTypeMap = new HashMap<String, ResourceType>();
		Map<String, UserType> userTypeMap = new HashMap<String, UserType>();
		
		themeParser.Parse(taskTypeMap,resourceTypeMap,userTypeMap);
		
		DataXMLDAO dataParser = new DataXMLDAO(dataFilename, manager, controller, taskTypeMap, resourceTypeMap, userTypeMap);
		return dataParser.Parse();
	}

}
