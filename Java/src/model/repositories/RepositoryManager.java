package model.repositories;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import exception.IllegalStateCallException;

import model.AssetType;
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
    private ListRepository<Project> projectRepository;
    /**
     * User repository implementation
     */
    private ListRepository<User> userRepository;
    /**
     * UserType repository implementation
     */
    private MapRepository<String,UserType> userTypeRepository;
    /**
     * Resource repository implementation
     */
    private ListRepository<Resource> resourceRepository;
    /**
     * Resource type repository implementation
     */
    private MapRepository<String,ResourceType> resourceTypeRepository;
    /**
     * Task type repository implementation
     */
    private MapRepository<String,TaskType> taskTypeRepository;
    
    /**
     * A clock that keeps the time in the system
     */
    private Clock clock;
    
    
    /**
     * Default constructor of RepositoryManager
     */
    public RepositoryManager()
    {
        projectRepository = new ListRepository<Project>();
        userRepository = new ListRepository<User>();
        resourceRepository = new ListRepository<Resource>();
        userTypeRepository = new MapRepository<String, UserType>();
        resourceTypeRepository = new MapRepository<String, ResourceType>();
        taskTypeRepository = new MapRepository<String, TaskType>();
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
    public boolean add(String id,ResourceType resourceType){
    	return resourceTypeRepository.add(id,resourceType);
    }
    /**
     * add TaskType to repository
     * @param id
     * @param taskType
     * @return
     */
    public boolean add(String id,TaskType taskType)
    {
    	return taskTypeRepository.add(id, taskType);
    }
    
    /**
     * Get a tasktype by its ID
     * @param id
     * @return
     */
    public TaskType getTaskTypeById(String id)
    {
    	return this.taskTypeRepository.getByKey(id);
    }
    
    /**
     * get a resourceType by ID
     */
    public ResourceType getResourceTypeById(String id)
    {
    	return this.resourceTypeRepository.getByKey(id);
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
    public boolean add(String id,UserType userType){
    	return userTypeRepository.add(id,userType);
    }
    
    /**
     * get a userType by ID
     */
    public UserType getUserTypeById(String id)
    {
    	return this.userTypeRepository.getByKey(id);
    }
    
    public AssetType getAssetById(String id)
    {
    	AssetType type = this.getUserTypeById(id);
    	if(type==null)
    		return getResourceTypeById(id);
    	return type;
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
    public Map<String, UserType> getUserTypes(){
    	return userTypeRepository.getAll();
    }
    
    /**
     * Return a list of all ResourceTypes
     */
    public Map<String, ResourceType> getResourceTypes(){
    	return resourceTypeRepository.getAll();
    }
    
    /**
     * Return all the available tasktypes
     * @return
     */
    public Map<String,TaskType> getTaskTypes()
    {
    	return taskTypeRepository.getAll();
    }
    
}
