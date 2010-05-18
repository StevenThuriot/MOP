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

public class XMLParser {
	private Document document;
	
	public XMLParser(String filename)
	{
		DocumentBuilder docBuilder = null;
		Document doc = null;
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		docBuilderFactory.setIgnoringElementContentWhitespace(true);
		try {
			docBuilder = docBuilderFactory.newDocumentBuilder();
		}
		catch (ParserConfigurationException e) {
			System.out.println("Wrong parser configuration: " + e.getMessage());
		}
		File sourceFile = new File(filename);
		
		try {
			doc = docBuilder.parse(sourceFile);
		}
		catch (SAXException e) {
			System.out.println("Wrong XML file structure: " + e.getMessage());
		}
		catch (IOException e) {
			System.out.println("Could not read source file: " + e.getMessage());
		}
		document = doc;
	}
	
	/**
	 * Finds a node by name as the child of a given node.
	 * Only searches in the first level of children.
	 * @param node
	 * @param name
	 * @return
	 * @throws NameNotFoundException
	 */
	protected Node getNodeByName(Node node, String name) throws NameNotFoundException
	{
		NodeList nodeList = node.getChildNodes();
		
		for(int i=0; i<nodeList.getLength(); i++){
		  Node childNode = nodeList.item(i);
		  
		  if (childNode.getNodeName() != "#text" && childNode.getNodeName().equals(name))
		  {
			  return childNode;
		  }
		}
		
		throw new NameNotFoundException();
	}

	/**
	 * Gets the documents root node
	 * @return
	 */
	protected Node getRootNode()
	{
		return document.getChildNodes().item(0);
	}
}
