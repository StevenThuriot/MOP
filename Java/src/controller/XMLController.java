package controller;

import java.text.ParseException;

import javax.naming.NameNotFoundException;

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
import exception.UnknownStateException;
import model.AssetType;
import model.User;
import model.repositories.RepositoryManager;
import model.xml.DataXMLDAO;
import model.xml.ThemeXMLDAO;

public class XMLController {
	
	private RepositoryManager manager;
	
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
	 * @throws DependencyException
	 * @throws IllegalStateCallException
	 * @throws BusinessRule3Exception
	 * @throws NotAvailableException
	 * @throws UnknownStateException
	 * @throws IllegalStateChangeException
	 * @throws BusinessRule2Exception
	 */
	public User parse(String dataFilename,String themefilename, DispatchController controller) throws NameNotFoundException, DOMException, NullPointerException, EmptyStringException, ParseException, BusinessRule1Exception, DependencyCycleException, DependencyException, IllegalStateCallException, BusinessRule3Exception, NotAvailableException, UnknownStateException, IllegalStateChangeException, BusinessRule2Exception
	{
		ThemeXMLDAO themeParser = new ThemeXMLDAO(themefilename, controller);
		themeParser.Parse();
		
		DataXMLDAO dataParser = new DataXMLDAO(dataFilename, controller);
		return dataParser.Parse();
	}
	
	/**
	 * Returns the asset type by ID, not regarding wether this is a User/resource
	 * @param id
	 * @return
	 */
	public AssetType getAssetTypeById(String id)
	{
		return manager.getAssetById(id);
	}
}
