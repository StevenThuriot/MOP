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
		ArrayList<Resource> res = new ArrayList<Resource>();
		res.addAll(dController.getResourceController().getResources());
		ArrayList<String> rDescrS = new ArrayList<String>();
		for (Resource r : res) {
			rDescrS.add(r.getDescription());
		}
		int choice = menu.menu("Select resource to reserve", rDescrS);
		Resource resource = res.get(choice);
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
			System.err.println("Resource is already reserved");
		}
	}

}
