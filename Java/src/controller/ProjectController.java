/**
 * 
 */
package controller;

import java.util.List;
import exception.EmptyStringException;
import model.Project;
import model.Task;
import model.repositories.RepositoryManager;

/**
 *	Controller to interact with projects
 */
public class ProjectController {
    /**
     * RepositoryManager
     */
    private RepositoryManager manager;
    
    /**
     * Constructor that takes a RepositoryManager as argument. Will throw NullPointerException if the latter was null.
     * @param manager
     */
    public ProjectController(RepositoryManager manager)
    {
        if(manager==null)
            throw new NullPointerException();
        this.manager = manager;
    }

	/**
	 * Create a new Project
	 * @param description
	 * @param user
	 * @return
	 * @throws EmptyStringException
	 */
	public Project createProject(String description) throws EmptyStringException {
		Project project = new Project(description);
		manager.add(project);
		return project;
	}

	/**
	 * Get all the projects from a user
	 * @param user
	 * @return
	 */
	public List<Project> getProjects() {
		return manager.getProjects();
	}
	
	/**
	 * Remove a Project
	 * @param p
	 */
	public void removeProject(Project p) {
		p.remove();
		manager.remove(p);
	}

	/**
	 * Binding a task to a project
	 * @param project
	 * @param task
	 */
	public void bind(Project project, Task task) {
		// TODO Auto-generated method stub
		
	}
}
