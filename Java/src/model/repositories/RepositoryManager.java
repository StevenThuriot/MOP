package model.repositories;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import model.Clock;
import model.Project;
import model.Resource;
import model.ResourceType;
import model.Task;
import model.TaskType;
import model.User;
import model.UserType;

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
     * UserType repository implementation
     */
    private Repository<UserType> userTypeRepository;
    /**
     * Resource repository implementation
     */
    private Repository<Resource> resourceRepository;
    /**
     * Resource type repository implementation
     */
    private Repository<ResourceType> resourceTypeRepository;
    /**
     * Task type repository implementation
     */
    private Repository<TaskType> taskTypeRepository;
    
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
        userTypeRepository = new Repository<UserType>();
        resourceTypeRepository = new Repository<ResourceType>();
        taskTypeRepository = new Repository<TaskType>();
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
     * Add resourceType to repository.
     */
    public boolean add(ResourceType resourceType){
    	return resourceTypeRepository.add(resourceType);
    }
    /**
     * add TaskType to repository
     * @param id
     * @param taskType
     * @return
     */
    public boolean add(TaskType taskType)
    {
    	return taskTypeRepository.add(taskType);
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
     * Add userType to repository.
     */
    public boolean add(UserType userType){
    	return userTypeRepository.add(userType);
    }
    
    /**
     * Overridable method remove. Will remove a Project to the Project repository if it exists
     * @param project
     * @return
     */
    public boolean remove(Project project)
    {
        project.remove();
        return projectRepository.remove(project);
    }

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
    
    /**
     * Return a list of all UserTypes
     */
    public List<UserType> getUserTypes(){
    	return userTypeRepository.getAll();
    }
    
    /**
     * Return a list of all ResourceTypes
     */
    public List<ResourceType> getResourceTypes(){
    	return resourceTypeRepository.getAll();
    }
    
    /**
     * Return all the available tasktypes
     * @return
     */
    public List<TaskType> getTaskTypes()
    {
    	return taskTypeRepository.getAll();
    }
    
}
