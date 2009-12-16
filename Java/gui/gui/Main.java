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
import exception.IllegalStateCallException;
import exception.NotAvailableException;
import exception.TimeException;
import exception.UnknownStateException;

import model.User;
import model.repositories.RepositoryManager;

public class Main {
	/**
	 * @param args
	 * @throws TimeException 
	 */
	public static void main(String[] args) throws TimeException {
//	    RepositoryManager manager = new RepositoryManager();
//		DispatchController controller = new DispatchController(manager);
//		
//		XMLParser parser = new XMLParser("students_public.xml", controller);
//		
//		User user = null;
//		
//		try {
//			user = parser.Parse();
//		} catch (NameNotFoundException e) {
//			Main.writeError("File not found.");
//		} catch (DOMException e) {
//			Main.writeError("");
//		} catch (NullPointerException e) {
//			Main.writeError("The value 'null' was passed to a method.");
//		} catch (EmptyStringException e) {
//			Main.writeError("An empty string was passed to a method that does not allow this.");
//		} catch (ParseException e) {
//			Main.writeError("");
//		} catch (BusinessRule1Exception e) {
//			Main.writeError("Business rule 1 violation.");
//		} catch (DependencyCycleException e) {
//			Main.writeError("A cycle between dependancies has been found.");
//		} catch (DependencyException e) {
//			Main.writeError("A problem with the dependancies has occured.");
//		} catch (IllegalStateCallException e) {
//			Main.writeError("It is impossible to change to the defined state.");
//		} catch (BusinessRule3Exception e) {
//			Main.writeError("Business rule 3 violation.");
//		} catch (NotAvailableException e) {
//			Main.writeError("Two or more reservations overlap.");
//		} catch (UnknownStateException e) {
//			Main.writeError("An unknown state has been found.");
//			e.printStackTrace();
//		}
//	
//		manager.add(user);
		
		MainGUI mainGUI = new MainGUI(System.in,System.out);
		
		Thread t = new Thread(mainGUI);
		
		t.start();
	}
	
	/**
	 * Writes a useful error message when the XML parser has a problem.
	 * @param message
	 */
	public static void writeError(String message)
	{
		System.out.println("An error has occured parsing the XML file.");
		System.out.println(message);
		
		if (!message.equals("")) {
			System.out.println();
		}
	}

}
