package gui;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import controller.DispatchController;

import model.User;

public class MainGUI implements Runnable{
	private ArrayList<User> users;
	private User currentUser;
	private DispatchController dController;
	@SuppressWarnings("unused")
	private InputStream in;
	@SuppressWarnings("unused")
	private PrintStream out;
	private ArrayList<UseCase> useCases;
	private Menu menu;
	
	public MainGUI(InputStream in, PrintStream out, ArrayList<User> u){
		menu = new Menu(in,out);
		this.in = in;
		this.out = out;
		dController = new DispatchController();
		users = u;
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
	
	public ArrayList<User> getUsers(){
		//TODO shallow copy?????
		return users;
	}
	
	public User getCurrentUser(){
		return currentUser;
	}
	
	public void run(){
		//Thread currentThread = Thread.currentThread();
		ArrayList<String> uNames = new ArrayList<String>();
		for(User u: users){
			uNames.add(u.getName());
		}
		currentUser = users.get(menu.menu("Select User", uNames));
		ArrayList<String> useCDescr = new ArrayList<String>(useCases.size());
		for(UseCase u : useCases){
			useCDescr.add(u.getDescription());
		}
		useCDescr.add("Exit");
		boolean run = true;
		while (run) {
			int choice = menu.menu("Select Action", useCDescr);
			if (choice == useCDescr.size() - 1) {
				run = false;
			}else{
			useCases.get(choice).startUseCase(menu, dController, this);
			}
		}		
	}

}
