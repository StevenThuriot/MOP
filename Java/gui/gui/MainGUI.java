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
import exception.AssetConstraintFullException;
import exception.AssetTypeNotRequiredException;
import exception.BusinessRule1Exception;
import exception.BusinessRule2Exception;
import exception.BusinessRule3Exception;
import exception.DependencyCycleException;
import exception.EmptyListPassedToMenuException;
import exception.EmptyStringException;
import exception.IllegalStateCallException;
import exception.IllegalStateChangeException;
import exception.InvitationInvitesOwnerException;
import exception.InvitationNotPendingException;
import exception.BadAllocationTimingException;
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
		
		XMLController xmlController = dController.getXmlController();
		
		ArrayList<User> users = null;
		
		try {
			users = xmlController.parse("students_public.xml","theme_development_1.xml", dController);
		} catch (NameNotFoundException e) {
			MainGUI.writeError("Error retreiving some of the nodes in the XML file.");
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
		} catch (BadAllocationTimingException e) {
			MainGUI.writeError("A reservation overlap has occurred. This is most likely due to a faulty XML file.");
		} catch (AssetAllocatedException e) {
			MainGUI.writeError("A asset allocation error has occurred. This is most likely due to a faulty XML file.");
		} catch (WrongFieldsForChosenTypeException e) {
			MainGUI.writeError("An error has occurred while trying to make a task with fields from a different type. This is most likely due to a faulty XML file.");
		} catch (NonExistingTypeSelected e) {
			MainGUI.writeError("An error has occurred while trying to make a task from a non existing type. This is most likely due to a faulty XML file.");
		} catch (WrongUserForTaskTypeException e) {
			MainGUI.writeError("An error has occurred while trying to make a task with an unallowed owner. This is most likely due to a faulty XML file.");
		} catch (AssetTypeNotRequiredException e) {
			MainGUI.writeError("An error has occurred while parsing the XML file. This is most likely due to a faulty XML file.");
		} catch (AssetConstraintFullException e) {
			MainGUI.writeError("An error has occurred while parsing the XML file. This is most likely due to a faulty XML file.");
		} catch (InvitationInvitesOwnerException e) {
			MainGUI.writeError("Cant invite the owner of a task. Probably faulty XML");
		} catch (InvitationNotPendingException e) {
			MainGUI.writeError("Tried to change the state of a non-pending invitation. This is probably a faulty XML");
		}
	
		if(users!=null)
			for (User user : users) {
				this.manager.add(user);
			}
			
		this.in = in;
		this.out = out;
		useCases = new ArrayList<UseCase>();
		useCases.add(new CreateTask());
		useCases.add(new InvitationManager());
		useCases.add(new RemoveTask());
		useCases.add(new UpdateTaskStatus());
		useCases.add(new ModifyTaskDetails());
		useCases.add(new CreateProject());
		useCases.add(new RemoveProject());
		useCases.add(new CreateResource());
		useCases.add(new MakeResourceReservation());
		useCases.add(new FocusWork());
	}
	
	/**
	 * Retrieves the current user
	 * @return user
	 */
	public User getCurrentUser(){
		return currentUser;
	}
	
	public void run(){
		boolean runStart = true;
		while(runStart){
			int adminOrExit=0;
			try {
				adminOrExit = menu.menu("What would you like to do", "Log in as administrator","Log in as user","Exit");
			} catch (EmptyListPassedToMenuException e1) {
			}
			if(adminOrExit==0)
			{
				UseCase adminMenu = new AdminMenu();
				adminMenu.startUseCase(menu, dController, this);
			}else if(adminOrExit==1){
				try {
					currentUser = menu.menuGen("Select User", manager.getUsers());
					boolean run = true;
					while (run) {
						UseCase choice = menu.menuGenOpt("Select Action", useCases,"Log out");
						if (choice == null) {
							run = false;
						}else{
						choice.startUseCase(menu, dController, this);
						}
					}
				} catch (EmptyListPassedToMenuException e) {
					menu.println("There are no users to select. Create one in the administrator menu first.");
					
				}
			}else{
				runStart = false;
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
