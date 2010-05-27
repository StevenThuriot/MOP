package gui;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import model.Reservation;
import model.Resource;
import model.Task;
import model.User;
import controller.DispatchController;
import exception.AssetAllocatedException;
import exception.AssetConstraintFullException;
import exception.AssetTypeNotRequiredException;
import exception.EmptyListPassedToMenuException;
import exception.IllegalStateCallException;
import exception.NoReservationOverlapException;
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
		Task selectedTask = null;
		try {
			selectedTask = menu.menuGen("Select a task to make a reservation for:", dController.getUserController().getAllUnfinishedTasks(user));
		} catch (EmptyListPassedToMenuException e1) {
			menu.println("There are no tasks to select. Going back to menu.");
			return;
		}
		Resource resource = null;
		try {
			resource = menu.menuGen("Select resource to reserve", dController.getResourceController().getResources());
		} catch (EmptyListPassedToMenuException e1) {
			menu.println("There are no resources to select. Going back to menu.");
			return;
		}
		List<Reservation> reservations = resource.getReservations();
		ArrayList<String> rsvDescr = new ArrayList<String>();
		for(Reservation rsv : reservations){
			rsvDescr.add("For "+rsv.getTask()+" on "+menu.format( rsv.getStartDate() )+" for "+rsv.getDuration()+" Minutes");
		}
		if(rsvDescr.isEmpty())
			rsvDescr.add("None");
		menu.printList("Reservations", rsvDescr);
		GregorianCalendar startDate = menu.promptDate("Give Start Date eg."+menu.format(selectedTask.getStartDate()));
		int duration = Integer.parseInt(menu.prompt("Duration? eg."+selectedTask.getDuration()));
		try {
			dController.getResourceController().createReservation(startDate, duration, resource, selectedTask);
		} catch (NotAvailableException e) {
			menu.println("Resource is already reserved");
		} catch (NoReservationOverlapException e) {
			menu.println(e.getMessage());
		} catch (AssetAllocatedException e) {
			menu.println(e.getMessage());
		} catch (IllegalStateCallException e) {
			menu.println(e.getMessage());
		} catch (AssetTypeNotRequiredException e) {
			menu.println(e.getMessage());
		} catch (AssetConstraintFullException e) {
			menu.println(e.getMessage());
		}
	}

}
