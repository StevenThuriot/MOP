/**
 * 
 */
package controller;

import java.util.List;
import exception.EmptyStringException;
import model.Project;
import model.Task;
import model.User;

/**
 *	Controller to interact with projects
 */
public class ProjectController {
	/**
	 * Create a new Project
	 * @param description
	 * @param user
	 * @return
	 * @throws EmptyStringException
	 */
	public Project createProject(String description, User user) throws EmptyStringException {
		Project project = new Project(user, description);
		return project;
	}

	/**
	 * Remove a Project
	 * @param p
	 */
	public void removeProject(Project p) {
		p.remove();
	}

	/**
	 * Get all the projects from a user
	 * @param user
	 * @return
	 */
	public List<Project> getProjects(User user) {
		return user.getProjects();
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
}
