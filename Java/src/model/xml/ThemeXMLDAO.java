package model.xml;

import java.util.ArrayList;
import java.util.Map;

import javax.naming.NameNotFoundException;

import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import controller.DispatchController;
import exception.EmptyStringException;

import model.AssetType;
import model.Field;
import model.NumericField;
import model.ResourceType;
import model.TaskType;
import model.TaskTypeConstraint;
import model.TextField;
import model.UserType;

public class ThemeXMLDAO {

	private DispatchController controller;
	private XMLParser parser;
	public ThemeXMLDAO(String filename,DispatchController controller)
	{
		this.controller = controller;
		parser = new XMLParser(filename);
	}
	
	
	public void Parse(Map<String,TaskType> taskTypeMap,Map<String,ResourceType> resourceTypeMap,Map<String,UserType> userTypeMap) throws NameNotFoundException, NullPointerException, DOMException, EmptyStringException
	{
		Node taskTypes = parser.getNodeByName(parser.getRootNode(), "t:taskTypes");
		Node resourceTypes = parser.getNodeByName(parser.getRootNode(), "t:resourceTypes");
		Node userTypes = parser.getNodeByName(parser.getRootNode(), "t:userTypes");
		
		parseResourceTypes(resourceTypes,resourceTypeMap);
		parseUserTypes(userTypes,userTypeMap);
		parseTaskTypes(taskTypes,taskTypeMap,resourceTypeMap,userTypeMap);
		
				
	}
	private void parseUserTypes(Node userTypes,Map<String,UserType> userTypeMap) throws NameNotFoundException {
		NodeList types = userTypes.getChildNodes();
		for(int i=0;i<types.getLength();i++)
			addUserType(types.item(i),userTypeMap);
	}

	private void parseResourceTypes(Node resourceTypes,Map<String,ResourceType> resourceTypeMap) throws NameNotFoundException {
		NodeList types = resourceTypes.getChildNodes();
		for(int i=0;i<types.getLength();i++)
			addResourceType(types.item(i),resourceTypeMap);
	}

	private void parseTaskTypes(Node taskTypes,Map<String,TaskType> taskTypeMap,Map<String,ResourceType> resourceTypeMap,Map<String,UserType> userTypeMap) throws NameNotFoundException, NullPointerException, DOMException, EmptyStringException {
		NodeList types = taskTypes.getChildNodes();
		for(int i=0;i<types.getLength();i++)
			addTaskType(types.item(i),taskTypeMap,resourceTypeMap,userTypeMap);
	}
	
	@SuppressWarnings("unchecked")
	private ArrayList<Field> parseTaskTypeFields(Node item) throws NameNotFoundException, NullPointerException, DOMException, EmptyStringException
	{
		ArrayList<Field> allFields = new ArrayList<Field>();
		Node fieldNode = parser.getNodeByName(item, "t:fields");
		NodeList fieldNodes = fieldNode.getChildNodes();
		for(int i=0;i<fieldNodes.getLength();i++)
		{
			Node field = fieldNodes.item(i);
			if(field.getNodeName()!="#text"){
				Field newField = null;
				if(field.getAttributes().getNamedItem("nature").getTextContent().equals("textual"))
				{
					newField = new TextField(field.getAttributes().getNamedItem("name").getTextContent(),"",field.getAttributes().getNamedItem("id").getTextContent());
				}else{
					newField = new NumericField(field.getAttributes().getNamedItem("name").getTextContent(),0,field.getAttributes().getNamedItem("id").getTextContent());
				}
				allFields.add(newField);
			}
		}
		return allFields;
	}
	
	private ArrayList<TaskTypeConstraint> parseTaskTypeConstraints(Node item,Map<String,ResourceType> resourceTypeMap,Map<String,UserType> userTypeMap) throws NameNotFoundException
	{
		ArrayList<TaskTypeConstraint> allConstraints = new ArrayList<TaskTypeConstraint>();
		Node constraintNode = parser.getNodeByName(item, "t:requires");
		NodeList constraintNodes = constraintNode.getChildNodes();
		for(int i=0;i<constraintNodes.getLength();i++)
		{
			Node constraint = constraintNodes.item(i);
			if(constraint.getNodeName()!="#text"){
				String assetTypeID = constraint.getAttributes().getNamedItem("type").getTextContent();
				int minimum = Integer.parseInt(constraint.getAttributes().getNamedItem("min").getTextContent());
				int maximum = 0;
				try{
					maximum = Integer.parseInt(constraint.getAttributes().getNamedItem("max").getTextContent());
				}catch(NumberFormatException e)
				{
					maximum = Integer.MAX_VALUE;
				}
				AssetType assetType = resourceTypeMap.get(assetTypeID);
				if(assetType==null)
					assetType = userTypeMap.get(assetTypeID);
				allConstraints.add(new TaskTypeConstraint(assetType,minimum,maximum));
			}
		}
		return allConstraints;
	}
	
	private void addUserType(Node item,Map<String, UserType> userTypeMap) {
		if(item.getNodeName()!="#text"){
			String id = item.getAttributes().getNamedItem("id").getTextContent();
			String name = item.getAttributes().getNamedItem("name").getTextContent();
			
			UserType type = controller.getUserController().createUserType(name);
			
			userTypeMap.put(id, type);
		}
	}

	private void addResourceType(Node item,Map<String, ResourceType> resourceTypeMap) {
		if(item.getNodeName()!="#text"){
			String id = item.getAttributes().getNamedItem("id").getTextContent();
			String name = item.getAttributes().getNamedItem("name").getTextContent();
			
			ResourceType type = controller.getResourceController().createResourceType(name);
			
			resourceTypeMap.put(id, type);
		}
	}
	
	private ArrayList<UserType> parseUserTypeConstraints(Node item, Map<String, UserType> userTypeMap) throws NameNotFoundException
	{
		ArrayList<UserType> userTypes = new ArrayList<UserType>();
		
		Node constraintNode = parser.getNodeByName(item, "t:owners");
		NodeList constraintNodes = constraintNode.getChildNodes();
		for(int i=0;i<constraintNodes.getLength();i++)
		{
			Node constraint = constraintNodes.item(i);
			if(constraint.getNodeName()!="#text"){
				String ownerType = constraint.getAttributes().getNamedItem("type").getTextContent();
				
				if ( userTypeMap.containsKey(ownerType) ) {
					userTypes.add( userTypeMap.get(ownerType) );
				} else {
					throw new NameNotFoundException();
				}
			}
		}
		
		return userTypes;
	}
	
	@SuppressWarnings("unchecked")
	private void addTaskType(Node item,Map<String,TaskType> taskTypeMap,Map<String,ResourceType> resourceTypeMap,Map<String,UserType> userTypeMap) throws NameNotFoundException, NullPointerException, DOMException, EmptyStringException {
		if(item.getNodeName()!="#text"){
			String id = item.getAttributes().getNamedItem("id").getTextContent();
			String name = item.getAttributes().getNamedItem("name").getTextContent();
			ArrayList<Field> fields = parseTaskTypeFields(item);
			ArrayList<TaskTypeConstraint> constraints = parseTaskTypeConstraints(item,resourceTypeMap,userTypeMap);
			
			ArrayList<UserType> userTypes = parseUserTypeConstraints(item, userTypeMap);
			
			TaskType type = controller.getTaskController().addTaskType(name,fields,constraints,userTypes);
			
			taskTypeMap.put(id, type);
		}
	}
}
