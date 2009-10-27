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
		ArrayList<String> typeDescr = new ArrayList<String>();
		for(ResourceType rt : ResourceType.values()){
			typeDescr.add(rt.toString());
		}
		int choice = menu.menu("Select resource type", typeDescr);
		try {
			dController.getResourceController().createResource(descr, ResourceType.values()[choice]);
		} catch (EmptyStringException e) {
			System.out.println("Empty Description");
		}
	}

}
