package gui;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import controller.DispatchController;

import model.User;
import model.repositories.RepositoryManager;

/**
 * @author koen
 *
 */
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
	
	public MainGUI(InputStream in, PrintStream out, RepositoryManager manager){
		menu = new Menu(in,out);
		this.in = in;
		this.out = out;
		dController = new DispatchController(manager);
		this.manager = manager;
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

}
