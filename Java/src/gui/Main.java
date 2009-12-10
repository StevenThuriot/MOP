package gui;

import java.text.ParseException;
import javax.naming.NameNotFoundException;

import org.w3c.dom.DOMException;

import controller.DispatchController;
import controller.XMLParser;
import exception.BusinessRule1Exception;
import exception.DependencyCycleException;
import exception.DependencyException;
import exception.EmptyStringException;

import model.User;
import model.repositories.RepositoryManager;

public class Main {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DispatchController controller = new DispatchController();
		
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
		}

		RepositoryManager manager = new RepositoryManager();
		
		manager.add(user);
		
		MainGUI mainGUI = new MainGUI(System.in,System.out,manager);
		
		Thread t = new Thread(mainGUI);
		
		t.start();
	}

}
