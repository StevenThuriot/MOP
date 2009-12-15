/**
 * 
 */
package controller;

import java.util.List;
import exception.EmptyStringException;
import exception.IllegalStateCallException;
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
	 * Bind task and project
	 * @param project
	 * @param task
	 */
	public void bind(Project project, Task task)
	{
		project.bindTask(task);
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
	 * @throws IllegalStateCallException 
	 */
	public void removeProject(Project p) throws IllegalStateCallException {
		p.remove();
		manager.remove(p);
	}
}
