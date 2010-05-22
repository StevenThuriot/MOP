package gui;

import java.io.InputStream;
import java.io.PrintStream;
import java.text.ParseException;
import java.util.ArrayList;

import javax.naming.NameNotFoundException;

import org.w3c.dom.DOMException;

import controller.DispatchController;
import controller.XMLController;
import exception.AssetAllocatedException;
import exception.BusinessRule1Exception;
import exception.BusinessRule2Exception;
import exception.BusinessRule3Exception;
import exception.DependencyCycleException;
import exception.DependencyException;
import exception.EmptyStringException;
import exception.IllegalStateCallException;
import exception.IllegalStateChangeException;
import exception.NoReservationOverlapException;
import exception.NonExistingTypeSelected;
import exception.NotAvailableException;
import exception.TimeException;
import exception.UnknownStateException;
import exception.WrongFieldsForChosenTypeException;
import exception.WrongUserForTaskTypeException;

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
		
		XMLController xmlController = dController.getXmlController();
		
		ArrayList<User> users = null;
		
		try {
			users = xmlController.parse("students_public.xml","theme_development_1.xml", dController);
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
		} catch (IllegalStateChangeException e) {
			MainGUI.writeError("An illegal state chance has occurred. This is most likely due to a faulty XML file.");
		} catch (BusinessRule2Exception e) {
			MainGUI.writeError("A business rule 2 violation has occurred. This is most likely due to a faulty XML file.");
		} catch (NoReservationOverlapException e) {
			MainGUI.writeError("A reservation overlap has occurred. This is most likely due to a faulty XML file.");
		} catch (AssetAllocatedException e) {
			MainGUI.writeError("A asset allocation error has occurred. This is most likely due to a faulty XML file.");
		} catch (WrongFieldsForChosenTypeException e) {
			MainGUI.writeError("An error has occurred while trying to make a task with fields from a different type. This is most likely due to a faulty XML file.");
		} catch (NonExistingTypeSelected e) {
			MainGUI.writeError("An error has occurred while trying to make a task from a non existing type. This is most likely due to a faulty XML file.");
		} catch (WrongUserForTaskTypeException e) {
			MainGUI.writeError("An error has occurred while trying to make a task with an unallowed owner. This is most likely due to a faulty XML file.");
		}
	
		for (User user : users) {
			this.manager.add(user);
		}
			
		this.in = in;
		this.out = out;
		useCases = new ArrayList<UseCase>();
		useCases.add(new CreateTask());
		useCases.add(new RemoveTask());
		useCases.add(new UpdateTaskStatus());
		useCases.add(new CreateProject());
		useCases.add(new RemoveProject());
		useCases.add(new CreateResource());
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
		if(menu.dialogYesNo("Log in as administrator?"))
		{
			UseCase adminMenu = new AdminMenu();
			adminMenu.startUseCase(menu, dController, this);
		}else{
			currentUser = menu.menuGenOpt("Select User", manager.getUsers(),"Exit");
			while(currentUser!=null){
				boolean run = true;
				while (run) {
					UseCase choice = menu.menuGenOpt("Select Action", useCases,"Log out");
					if (choice == null) {
						run = false;
					}else{
					choice.startUseCase(menu, dController, this);
					}
				}
				currentUser = menu.menuGenOpt("Select User", manager.getUsers(),"Exit");
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
