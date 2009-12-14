package gui;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import model.Reservation;
import model.Resource;
import model.User;
import controller.DispatchController;
import exception.NotAvailableException;

public class MakeResourceReservation extends UseCase {
	
	public MakeResourceReservation(){}
	
	private MakeResourceReservation(Menu menu, DispatchController dController, User user){
		this.menu = menu;
		this.dController = dController;
		this.user = user;
	}

	@Override
	public String getDescription() {
		return "Make Resource Reservation";
	}

	@Override
	public void startUseCase(Menu menu, DispatchController dController, MainGUI mainGUI) {
		(new MakeResourceReservation(menu, dController, mainGUI.getCurrentUser())).makeResourceReservation();
	}
	
	private void makeResourceReservation(){
		Resource resource = menu.menuGen("Select resource to reserve", dController.getResourceController().getResources());
		List<Reservation> reservations = dController.getResourceController().getReservations();
		ArrayList<String> rsvDescr = new ArrayList<String>();
		for(Reservation rsv : reservations){
			rsvDescr.add("By "+rsv.getUser().getName()+" on "+menu.format( rsv.getTime() )+" for "+rsv.getDuration()+" Minutes");
		}
		if(rsvDescr.isEmpty())
			rsvDescr.add("None");
		menu.printList("Reservations", rsvDescr);
		GregorianCalendar startDate = menu.promptDate("Give Start Date");
		int duration = Integer.parseInt(menu.prompt("Duration?"));
		try {
			dController.getResourceController().createReservation(startDate, duration, resource, user);
		} catch (NotAvailableException e) {
			System.out.println("Resource is already reserved");
		}
	}

}
