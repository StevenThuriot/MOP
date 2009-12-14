package gui;

import java.util.ArrayList;

import model.Resource;
import model.User;
import controller.DispatchController;
import exception.ResourceBusyException;

public class RemoveResource extends UseCase {
	
	public RemoveResource(){}
	
	private RemoveResource(Menu menu, DispatchController dController, User user){
		this.menu = menu;
		this.dController = dController;
		this.user = user;
	}

	@Override
	public String getDescription() {
		return "Remove Resource";
	}

	@Override
	public void startUseCase(Menu menu, DispatchController dController, MainGUI mainGUI) {
		(new RemoveResource(menu, dController, mainGUI.getCurrentUser())).removeResource();
	}
	
	private void removeResource(){
		Resource choice = menu.menuGenOpt("Select resource to remove", dController.getResourceController().getResources(),"None");
		if (choice != null) {
			try {
				dController.getResourceController().removeResource(choice);
			} catch (ResourceBusyException e) {
				System.out.println("Resource is required by a task, Aborting...Done");
			}
		}		
	}

}
