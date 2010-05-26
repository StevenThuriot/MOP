package gui;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
			dController.getTimeController().setTime(menu.promptDate("Give Current Time"+" eg. "+ df.format(dController.getTimeController().getTime().getTime())));
		} catch (TimeException e) {
			menu.println("Incorrect Time");
		}
	}

}
