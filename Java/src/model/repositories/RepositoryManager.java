package model.repositories;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import exception.IllegalStateCallException;
import exception.ResourceBusyException;

import model.Clock;
import model.Project;
import model.Resource;
import model.Task;
import model.User;

public class RepositoryManager {
    /**
     * Project repository implementation
     */
    private Repository<Project> projectRepository;
    /**
     * User repository implementation
     */
    private Repository<User> userRepository;
    /**
     * Resource repository implementation
     */
    private Repository<Resource> resourceRepository;
    /**
     * A clock that keeps the time in the system
     */
    private Clock clock;
    
    /**
     * Default constructor of RepositoryManager
     */
    public RepositoryManager()
    {
        projectRepository = new Repository<Project>();
        userRepository = new Repository<User>();
        resourceRepository = new Repository<Resource>();
        clock = new Clock(this,new GregorianCalendar(1970, 1, 1));
    }
    
    /**
     * Overridable method add. Will add a project to the project repository
     * @param project
     * @return
     */
    public boolean add(Project project)
    {
        return projectRepository.add(project);
    }
    
    /**
     * Overridable method add. Will add a resource to the resource repository
     * @param resource
     * @return
     */
    public boolean add(Resource resource)
    {
        return resourceRepository.add(resource);
    }
    
    /**
     * Overridable method add. Will add a user to the user repository
     * @param user
     * @return
     */
    public boolean add(User user)
    {
        return userRepository.add(user);
    }
    
    /**
     * Overridable method remove. Will remove a Project to the Project repository if it exists
     * @param project
     * @return
     * @throws IllegalStateCallException 
     */
    public boolean remove(Project project) throws IllegalStateCallException
    {
        project.remove();
        return projectRepository.remove(project);
    }
//    /**
//     * Overridable method remove. Will remove a Resource to the Resource repository if it exists
//     * @param p
//     * @return
//     * @throws ResourceBusyException 
//     */
//    public boolean remove(Resource resource) throws ResourceBusyException
//    {
//        resource.remove();
//        return resourceRepository.remove(resource);
//    }
    /**
     * Returns the clock of the system.
     * @return
     */
    public Clock getClock(){
    	return clock;
    }
    /**
     * Returns an unmodifiable list of the resources available    
     * @return
     */
    public List<Resource> getResources()
    {
        return resourceRepository.getAll();
    }
    /**
     * Returns an unmodifiable list of the projects available
     * @return
     */
    public List<Project> getProjects()
    {
        return projectRepository.getAll();
    }
    /**
     * Returns an unmodifiable list of the users available
     * @return
     */
    public List<User> getUsers()
    {
        return userRepository.getAll();
    }
    
    /**
     * Returns a list of the tasks in the system.
     * @return
     */
    public List<Task> getTasks(){
    	ArrayList<Task> tasks = new ArrayList<Task>();
    	ArrayList<User> users = new ArrayList<User>(this.getUsers());
    	
    	for(User user: users){
    		for(Task task: user.getTasks()){
    			tasks.add(task);
    		}
    	}
    	return tasks;
    }
}
