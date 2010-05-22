package gui;

import controller.DispatchController;
import exception.TimeException;

public class SetClock extends UseCase {

	@Override
	public String getDescription() {
		return "Set Clock";
	}
	
	private SetClock(Menu menu, DispatchController dController) {
		this.menu = menu;
		this.dController = dController;
	}
	
	public SetClock(){}

	@Override
	public void startUseCase(Menu menu, DispatchController dController, MainGUI mainGUI) {
		(new SetClock(menu, dController)).setClock();
	}
	
	private void setClock(){
		
		try {
			dController.getTimeController().setTime(menu.promptDate("Give Current Time"));
		} catch (TimeException e) {
			menu.println("Incorrect Time");
		}
	}

}
