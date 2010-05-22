package gui;

import controller.DispatchController;

public class AdminMenu extends UseCase {
	
	public AdminMenu()
	{}
	
	private AdminMenu(Menu menu,DispatchController dController)
	{
		this.menu = menu;
		this.dController = dController;
	}
	
	@Override
	public String getDescription() {
		return "Administrator only menu";
	}

	@Override
	public void startUseCase(Menu menu, DispatchController dController,
			MainGUI mainGUI) {
		(new AdminMenu(menu, dController)).showMenu();
	}

	private void showMenu() {
		boolean run = true;
		while(run){
			int choice = menu.menu("What would you like to do?", "Adjust clock value","Create user","Go back to main menu");
			if(choice == 0)
			{
				(new SetClock()).startUseCase(menu, dController,null);
			}else if(choice == 1){
				(new CreateUser()).startUseCase(menu,dController,null);
			}else{
				run = false;
			}
		}
	}

}
