package gui;

import java.util.ArrayList;

import model.ResourceType;
import model.User;
import controller.DispatchController;
import exception.EmptyStringException;

public class CreateResource extends UseCase {
	
	public CreateResource(){}
	
	private CreateResource(Menu menu, DispatchController dController, User user){
		this.menu = menu;
		this.dController = dController;
		this.user = user;
	}

	@Override
	public String getDescription() {
		return "Create Resource";
	}

	@Override
	public void startUseCase(Menu menu, DispatchController dController, MainGUI mainGUI) {
		(new CreateResource(menu, dController, mainGUI.getCurrentUser())).createResource();
	}
	
	private void createResource(){
		String descr = menu.prompt("Give resource description");
		ResourceType choice = menu.menuGen("Select resource type", dController.getResourceController().getResourceTypes());
		try {
			dController.getResourceController().createResource(descr, choice);
		} catch (EmptyStringException e) {
			System.out.println("Empty Description");
		}
	}

}
