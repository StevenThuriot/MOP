/**
 * 
 */
package controller;

import model.repositories.RepositoryManager;

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
	public DispatchController(RepositoryManager manager) {
	    if(manager==null)
            throw new NullPointerException();
		this.projectController = new ProjectController(manager);
		this.resourceController = new ResourceController(manager);
		this.taskController = new TaskController(manager);
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
