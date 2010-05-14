package gui;

import model.Invitation;
import model.Task;
import model.User;
import model.Invitation.InvitationState;
import controller.DispatchController;
import exception.AssetAllocatedException;
import exception.InvitationInvitesOwnerException;
import exception.InvitationNotPendingException;

public class InvitationManager extends UseCase {

	private Menu menu;
	private DispatchController dController;
	public InvitationManager(Menu menu, DispatchController dController)
	{
		this.menu = menu;
		this.dController = dController;
	}
	
	@Override
	public String getDescription() {
		return "Invitation Manager";
	}

	@Override
	public void startUseCase(Menu menu, DispatchController dController,
			MainGUI mainGUI) {
		(new InvitationManager(menu, dController)).mainMenu();
	}

	private void mainMenu()
	{
		int choice = menu.menu("What would you like to do", "Add/Remove Invitation", "Accept/Decline Invitation");
		if(choice == 0)
		{
			this.addRemoveMenu();
		}else{
			this.acceptDeclineMenu();
		}
	}
	
	@SuppressWarnings("null")
	private void acceptDeclineMenu()
	{
		Invitation selectedInvitation = null;
		while(selectedInvitation==null && selectedInvitation.getState()!=InvitationState.PENDING)
		{
			selectedInvitation = menu.menuGen("Select an invitation to be updated", user.getInvitations());
			if(selectedInvitation.getState().equals(InvitationState.ACCEPTED))
				menu.print("You can't update this invitation. It is already accepted");
		}
		if(menu.dialogYesNo("Accept this invitation?"))
		{
			try {
				this.dController.getInvitationController().acceptInvitation(selectedInvitation);
			} catch (InvitationNotPendingException e) {
				menu.print("You can't update this invitation. It is already been set");
			}
		}else{
			try {
				this.dController.getInvitationController().declineInvitation(selectedInvitation);
			} catch (InvitationNotPendingException e) {
				menu.print("You can't update this invitation. It is already been set");
			}
		}
	}
	
	private void addRemoveMenu() {
		Task selectedTask = menu.menuGen("Select a task:", user.getTasks());
		int choice = menu.menu("What would you like to do?", "Add an invitation","Remove an invitation");
		if(choice == 0)
		{
			User user = menu.menuGen("Select a user to be invited for this task:",dController.getInvitationController().getAllUsers());
			try {
				Invitation createdInvite = dController.getInvitationController().createInvitation(selectedTask, user);
				menu.print("User invited for task, following invitation was created:\n");
				menu.print(createdInvite.toString());
			} catch (AssetAllocatedException e) {
				menu.print("This user was already invited for this task.");
			} catch (InvitationInvitesOwnerException e) {
				menu.print("You can't invite yourself to the invitation");
			}
		}else{
			Invitation removeInvitation = menu.menuGen("Select an invitation to be removed", selectedTask.getTaskAssetManager().getAssetAllocations());
			dController.getInvitationController().removeInvitation(removeInvitation);
			menu.print("Invitation removed");
		}
	}

}
