package gui;

import java.util.ArrayList;
import java.util.List;

import model.AllocationType;
import model.AssetAllocation;
import model.Invitation;
import model.Task;
import model.User;
import model.Invitation.InvitationState;
import controller.DispatchController;
import exception.AssetAllocatedException;
import exception.AssetConstraintFullException;
import exception.AssetTypeNotRequiredException;
import exception.IllegalStateCallException;
import exception.InvitationInvitesOwnerException;
import exception.InvitationNotPendingException;

public class InvitationManager extends UseCase {
	
	public InvitationManager(){
	}

	private Menu menu;
	private DispatchController dController;
	private InvitationManager(Menu menu, DispatchController dController,User user)
	{
		this.menu = menu;
		this.dController = dController;
		this.user = user;
	}
	
	@Override
	public String getDescription() {
		return "Invitation Manager";
	}

	@Override
	public void startUseCase(Menu menu, DispatchController dController,
			MainGUI mainGUI) {
		(new InvitationManager(menu, dController,mainGUI.getCurrentUser())).mainMenu();
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
			selectedInvitation = menu.menuGen("Select an invitation to be updated.", user.getInvitations());
			if(selectedInvitation.getState().equals(InvitationState.ACCEPTED))
				menu.print("You can't update this invitation. It is already accepted.");
		}
		if(menu.dialogYesNo("Accept this invitation?"))
		{
			try {
				this.dController.getInvitationController().acceptInvitation(selectedInvitation);
			} catch (InvitationNotPendingException e) {
				menu.print("You can't update this invitation. It is already been set.");
			}
		}else{
			try {
				this.dController.getInvitationController().declineInvitation(selectedInvitation);
			} catch (InvitationNotPendingException e) {
				menu.print("You can't update this invitation. It is already been set.");
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
				menu.print("You can't invite yourself to the invitation.");
			} catch (IllegalStateCallException e) {
				menu.print("Task is finished, you can no longer extend invitations for it.");
			} catch (AssetTypeNotRequiredException e) {
				menu.print("This UserType can not be invited");
			} catch (AssetConstraintFullException e) {
				menu.print("This UserType has been invited enough already");
			}
		}else{
			List<AssetAllocation> allocations =  selectedTask.getAssetAllocations();
			ArrayList<Invitation> invitations = new ArrayList<Invitation>();
			for(AssetAllocation allocation: allocations){
				if(allocation.getAllocationType() == AllocationType.Invitation){
					invitations.add((Invitation) allocation);
				}
			}
			Invitation removeInvitation = menu.menuGen("Select an invitation to be removed", invitations);
			dController.getInvitationController().removeInvitation(removeInvitation);
			menu.print("Invitation removed");
		}
	}

}
