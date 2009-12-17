package gui;

import java.io.InputStream;
import java.io.PrintStream;
import java.text.ParseException;
import java.util.ArrayList;

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

public class MainGUI implements Runnable{
	private RepositoryManager manager;
	private User currentUser;
	private DispatchController dController;
	@SuppressWarnings("unused")
	private InputStream in;
	@SuppressWarnings("unused")
	private PrintStream out;
	private ArrayList<UseCase> useCases;
	private Menu menu;
	
	public MainGUI(InputStream in, PrintStream out) throws TimeException{
		manager = new RepositoryManager();
		dController = new DispatchController(manager);
		menu = new Menu(in,out);
		
		dController.getTimeController().setTime(menu.promptDate("Give Current Time"));
		
		XMLParser parser = new XMLParser("students_public.xml", dController);
		
		User user = null;
		
		try {
			user = parser.Parse();
		} catch (NameNotFoundException e) {
			MainGUI.writeError("File not found.");
		} catch (DOMException e) {
			MainGUI.writeError("DOM Exception while parsing the XML file.");
		} catch (NullPointerException e) {
			MainGUI.writeError("The value 'null' was passed to a method.");
		} catch (EmptyStringException e) {
			MainGUI.writeError("An empty string was passed to a method that does not allow this.");
		} catch (ParseException e) {
			MainGUI.writeError("A parse exception occured.");
		} catch (BusinessRule1Exception e) {
			MainGUI.writeError("Business rule 1 violation.");
		} catch (DependencyCycleException e) {
			MainGUI.writeError("A cycle between dependancies has been found.");
		} catch (DependencyException e) {
			MainGUI.writeError("A problem with the dependancies has occured.");
		} catch (IllegalStateCallException e) {
			MainGUI.writeError("It is impossible to change to the defined state.");
		} catch (BusinessRule3Exception e) {
			MainGUI.writeError("Business rule 3 violation.");
		} catch (NotAvailableException e) {
			MainGUI.writeError("Two or more reservations overlap.");
		} catch (UnknownStateException e) {
			MainGUI.writeError("An unknown state has been found.");
		}
	
		this.manager.add(user);
		this.in = in;
		this.out = out;
		useCases = new ArrayList<UseCase>();
		useCases.add(new CreateTask());
		useCases.add(new RemoveTask());
		useCases.add(new UpdateTaskStatus());
		useCases.add(new CreateProject());
		useCases.add(new RemoveProject());
		useCases.add(new AssignTaskToProject());
		useCases.add(new CreateResource());
		useCases.add(new RemoveResource());
		useCases.add(new MakeResourceReservation());
		useCases.add(new FocusWork());
		useCases.add(new ModifyTaskDetails());
	}
	
	/**
	 * Retrieves the current user
	 * @return user
	 */
	public User getCurrentUser(){
		return currentUser;
	}
	
	public void run(){
		currentUser = menu.menuGen("Select User", manager.getUsers());
		boolean run = true;
		while (run) {
			UseCase choice = menu.menuGenOpt("Select Action", useCases,"Exit");
			if (choice == null) {
				run = false;
			}else{
			choice.startUseCase(menu, dController, this);
			}
		}		
	}
	
	/**
	 * Writes a useful error message when the XML parser has a problem.
	 * @param message The message to write.	 
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
