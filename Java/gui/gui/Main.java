package gui;

import java.text.ParseException;
import javax.naming.NameNotFoundException;

import org.w3c.dom.DOMException;

import controller.DispatchController;
import controller.XMLParser;
import exception.BusinessRule1Exception;
import exception.BusinessRule3Exception;
import exception.DependencyCycleException;
import exception.DependencyException;
import exception.EmptyStringException;
import exception.IllegalStateCall;

import model.User;
import model.repositories.RepositoryManager;

public class Main {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
	    RepositoryManager manager = new RepositoryManager();
		DispatchController controller = new DispatchController(manager);
		
		XMLParser parser = new XMLParser("students_public.xml", controller);
		
		User user = null;
		try {
			user = parser.Parse();
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (EmptyStringException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BusinessRule1Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DependencyCycleException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DependencyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateCall e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BusinessRule3Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		manager.add(user);
		
		MainGUI mainGUI = new MainGUI(System.in,System.out,manager);
		
		Thread t = new Thread(mainGUI);
		
		t.start();
	}

}
