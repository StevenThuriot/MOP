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
	 * The time controller. It handles the clock in the system.
	 */
	private TimeController timeController;
	/**
	 * The InvitationController. It handles all invitation adding/removing/updating
	 */
	private InvitationController invitationController;
	/**
	 * The XML controller used to parse XML files.
	 */
	private XMLController xmlController;
	/**
	 * The Focus controller used to create focus types
	 */
	private FocusController focusController;
	
	/**
	 * The userController to handle Users and UserTypes
	 */
	private UserController userController;
	/**
	 * The Constructor
	 */
	public DispatchController(RepositoryManager manager) {
	    if(manager==null)
            throw new NullPointerException();
		this.projectController = new ProjectController(manager);
		this.resourceController = new ResourceController(manager);
		this.taskController = new TaskController(manager);
		this.timeController = new TimeController(manager);
		this.invitationController = new InvitationController(manager);
		this.xmlController = new XMLController();
		this.focusController = new FocusController();
		this.userController = new UserController(manager);
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
	
	/**
	 * Returns the Time controller
	 * @return
	 */
	public TimeController getTimeController(){
		return timeController;
	}
	/**
	 * Returns the Invitation Controller
	 * @return
	 */
	public InvitationController getInvitationController()
	{
		return invitationController;
	}
	/**
	 * @return the xmlController
	 */
	public XMLController getXmlController() {
		return xmlController;
	}

	/**
	 * @return the focusController
	 */
	public FocusController getFocusController() {
		return focusController;
	}	
	
	/**
	 * @return the userController
	 */
	public UserController getUserController()
	{
		return userController;
	}
}
