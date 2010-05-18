package model.xml;

import java.io.File;
import java.io.IOException;

import javax.naming.NameNotFoundException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import controller.DispatchController;

public class ThemeXMLDAO {

	private DispatchController controller;
	private XMLParser parser;
	public ThemeXMLDAO(String filename,DispatchController controller)
	{
		this.controller = controller;
		parser = new XMLParser(filename);
	}
	
	
	public void Parse() throws NameNotFoundException
	{
		Node taskTypes = parser.getNodeByName(parser.getRootNode(), "t:taskTypes");
		Node resourceTypes = parser.getNodeByName(parser.getRootNode(), "t:resourceTypes");
		Node userTypes = parser.getNodeByName(parser.getRootNode(), "t:userTypes");
		
		parseTaskTypes(taskTypes);
		parseResourceTypes(resourceTypes);
		parseUserTypes(userTypes);
	}
	private void parseUserTypes(Node userTypes) {
		// TODO Auto-generated method stub
		
	}


	private void parseResourceTypes(Node resourceTypes) {
		// TODO Auto-generated method stub
		
	}


	private void parseTaskTypes(Node taskTypes) throws NameNotFoundException {
		Node types = parser.getNodeByName(taskTypes, "t:taskType");
		
	}
}
