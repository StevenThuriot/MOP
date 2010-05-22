package gui;

import java.util.ArrayDeque;

import model.Task;
import model.User;
import controller.DispatchController;

public class RemoveTask extends UseCase {

	public RemoveTask() {
	}

	private RemoveTask(Menu menu, DispatchController dController, User user) {
		this.menu = menu;
		this.user = user;
		this.dController = dController;
	}

	@Override
	public String getDescription() {
		return "Remove Task";
	}

	@Override
	public void startUseCase(Menu menu, DispatchController dController, MainGUI mainGUI) {
		(new RemoveTask(menu, dController, mainGUI.getCurrentUser())).removeTask();
	}

	private void removeTask() {
		Task choice = menu.menuGenOpt("Select Task to remove", dController.getTaskController().getTasks(user),"None");
		if(choice == null)
			return;
		if(!dController.getTaskController().hasDependentTasks( choice ))
			dController.getTaskController().removeTask( choice );
		else{
			ArrayDeque<Task> a = new ArrayDeque<Task>();
			ArrayDeque<Integer> l = new ArrayDeque<Integer>();
			a.push(choice);
			l.push(0);
			int level = 0;
			Task t = null;
			menu.println("This tasks has dependent tasks who may need to be removed as well.");
			while(!a.isEmpty()){
				t = a.pop();
				level = l.pop();
				for(int j = 0; j < level;j++)
					menu.print("\t");
				menu.println(t.getDescription());
				for(Task t2: dController.getTaskController().getDependentTasks(t)){
					a.push(t2);
					l.push(level+1);
				}
			}
			if(menu.dialogYesNo("Remove all these tasks."))
				dController.getTaskController().removeTaskRecursively( choice );
			else
				dController.getTaskController().removeTask( choice );
		}
	}

}
