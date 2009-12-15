package gui;

import model.Resource;
import model.Task;
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
				String descr = dController.getResourceController().getTasksUsing(choice).get(0).getDescription();
				for(int i = 1; i < dController.getResourceController().getTasksUsing(choice).size();i++){
					descr=descr.concat(", ").concat(dController.getResourceController().getTasksUsing(choice).get(i).getDescription());
				}
				menu.println("Resource is used by "+descr+", Aborting...Done");
			}
		}
	}

}
