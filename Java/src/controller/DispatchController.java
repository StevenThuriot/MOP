/**
 * 
 */
package controller;

/**
 * Controller to easily interact with all the other controllers.
 */
public class DispatchController {
	/**
	 * The Project Controller
	 */
	private ProjectController projectController;
	/**
	 * The Resource Controller
	 */
	private ResourceController resourceController;
	/**
	 * The Task Controller
	 */
	private TaskController taskController;
	
	
	/**
	 * The Constructor
	 */
	public DispatchController() {
		this.projectController = new ProjectController();
		this.resourceController = new ResourceController();
		this.taskController = new TaskController();
	}

	/**
	 * Get the Project Controller
	 * @return
	 */
	public ProjectController getProjectController() {
		return projectController;
	}

	/**
	 * Get the Resource Controller
	 * @return
	 */
	public ResourceController getResourceController() {
		return resourceController;
	}

	/**
	 * Get the Task Controller
	 * @return
	 */
	public TaskController getTaskController() {
		return taskController;
	}
}
