package model.xml;

import java.util.ArrayList;
import java.util.Map;

import javax.naming.NameNotFoundException;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import controller.DispatchController;

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
	
	
	public void Parse(Map<String,TaskType> taskTypeMap,Map<String,ResourceType> resourceTypeMap,Map<String,UserType> userTypeMap) throws NameNotFoundException
	{
		Node taskTypes = parser.getNodeByName(parser.getRootNode(), "t:taskTypes");
		Node resourceTypes = parser.getNodeByName(parser.getRootNode(), "t:resourceTypes");
		Node userTypes = parser.getNodeByName(parser.getRootNode(), "t:userTypes");
		
		parseResourceTypes(resourceTypes,resourceTypeMap);
		parseUserTypes(userTypes,userTypeMap);
		parseTaskTypes(taskTypes,taskTypeMap,resourceTypeMap,userTypeMap);
	}
	private void parseUserTypes(Node userTypes,Map<String,UserType> userTypeMap) throws NameNotFoundException {
		Node typeNode = parser.getNodeByName(userTypes, "t:userType");
		NodeList types = typeNode.getChildNodes();
		for(int i=0;i<types.getLength();i++)
			addUserType(types.item(i),userTypeMap);
	}

	private void parseResourceTypes(Node resourceTypes,Map<String,ResourceType> resourceTypeMap) throws NameNotFoundException {
		Node typeNode = parser.getNodeByName(resourceTypes, "t:resourceType");
		NodeList types = typeNode.getChildNodes();
		for(int i=0;i<types.getLength();i++)
			addResourceType(types.item(i),resourceTypeMap);
	}

	private void parseTaskTypes(Node taskTypes,Map<String,TaskType> taskTypeMap,Map<String,ResourceType> resourceTypeMap,Map<String,UserType> userTypeMap) throws NameNotFoundException {
		Node typeNode = parser.getNodeByName(taskTypes, "t:taskType");
		NodeList types = typeNode.getChildNodes();
		for(int i=0;i<types.getLength();i++)
			addTaskType(types.item(i),taskTypeMap,resourceTypeMap,userTypeMap);
	}

	@SuppressWarnings("unchecked")
	private void addTaskType(Node item,Map<String,TaskType> taskTypeMap,Map<String,ResourceType> resourceTypeMap,Map<String,UserType> userTypeMap) throws NameNotFoundException {
		String id = item.getAttributes().item(0).getTextContent();
		String name = item.getAttributes().item(1).getTextContent();
		ArrayList<Field> fields = parseTaskTypeFields(item);
		ArrayList<TaskTypeConstraint> constraints = parseTaskTypeConstraints(item,resourceTypeMap,userTypeMap);
		controller.getTaskController().addTaskType(id,name,fields,constraints);
	}
	
	@SuppressWarnings("unchecked")
	private ArrayList<Field> parseTaskTypeFields(Node item) throws NameNotFoundException
	{
		ArrayList<Field> allFields = new ArrayList<Field>();
		Node fieldNode = parser.getNodeByName(parser.getNodeByName(item, "t:fields"),"t:field");
		NodeList fieldNodes = fieldNode.getChildNodes();
		for(int i=0;i<fieldNodes.getLength();i++)
		{
			Node field = fieldNodes.item(i);
			Field newField = null;
			if(field.getAttributes().item(2).getTextContent().equals("textual"))
			{
				newField = new TextField(field.getAttributes().item(1).getTextContent(),"");
			}else{
				newField = new NumericField(field.getAttributes().item(1).getTextContent(),0);
			}
			allFields.add(newField);
		}
		return allFields;
	}
	
	private ArrayList<TaskTypeConstraint> parseTaskTypeConstraints(Node item,Map<String,ResourceType> resourceTypeMap,Map<String,UserType> userTypeMap) throws NameNotFoundException
	{
		ArrayList<TaskTypeConstraint> allConstraints = new ArrayList<TaskTypeConstraint>();
		Node constraintNode = parser.getNodeByName(parser.getNodeByName(item, "t:requires"),"t:requirement");
		NodeList constraintNodes = constraintNode.getChildNodes();
		for(int i=0;i<constraintNodes.getLength();i++)
		{
			Node constraint = constraintNodes.item(i);
			String assetTypeID = constraint.getAttributes().item(0).getTextContent();
			int minimum = Integer.parseInt(constraint.getAttributes().item(1).getTextContent());
			int maximum = Integer.parseInt(constraint.getAttributes().item(2).getTextContent());
			AssetType assetType = resourceTypeMap.get(assetTypeID);
			if(assetType==null)
				assetType = userTypeMap.get(assetTypeID);
			allConstraints.add(new TaskTypeConstraint(assetType,minimum,maximum));
		}
		return allConstraints;
	}
	
	private void addUserType(Node item,Map<String, UserType> userTypeMap) {
		String id = item.getAttributes().item(0).getTextContent();
		String name = item.getAttributes().item(0).getTextContent();
		userTypeMap.put(id,controller.getUserController().createUserType(name));
	}

	private void addResourceType(Node item,Map<String, ResourceType> resourceTypeMap) {
		String id = item.getAttributes().item(0).getTextContent();
		String name = item.getAttributes().item(0).getTextContent();
		resourceTypeMap.put(id,controller.getResourceController().createResourceType(name));
	}
}
