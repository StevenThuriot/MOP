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
		ArrayList<Resource> res = new ArrayList<Resource>();
		res.addAll(dController.getResourceController().getResources());
		ArrayList<String> rDescrS = new ArrayList<String>();
		for (Resource r : res) {
			rDescrS.add(r.getDescription());
		}
		rDescrS.add("None");
		int choice = menu.menu("Select resource to remove", rDescrS);
		if (choice != rDescrS.size()-1) {
			try {
				dController.getResourceController().removeResource(res.get(choice));
			} catch (ResourceBusyException e) {
				System.err.println("Resource is required by a task, Aborting...Done");
			}
		}		
	}

}
