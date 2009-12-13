package model.repositories;

import java.util.List;

import exception.IllegalStateCall;
import exception.ResourceBusyException;

import model.Project;
import model.Resource;
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
     * Default constructor of RepositoryManager
     */
    public RepositoryManager()
    {
        projectRepository = new Repository<Project>();
        userRepository = new Repository<User>();
        resourceRepository = new Repository<Resource>();
    }
    /**
     * Overridable method add. Will add a project to the project repository
     * @param p
     * @return
     */
    public boolean add(Project p)
    {
        return projectRepository.add(p);
    }
    /**
     * Overridable method add. Will add a resource to the resource repository
     * @param p
     * @return
     */
    public boolean add(Resource r)
    {
        return resourceRepository.add(r);
    }
    /**
     * Overridable method add. Will add a user to the user repository
     * @param p
     * @return
     */
    public boolean add(User u)
    {
        return userRepository.add(u);
    }
    /**
     * Overridable method remove. Will remove a Project to the Project repository if it exists
     * @param p
     * @return
     * @throws IllegalStateCall 
     */
    public boolean remove(Project p) throws IllegalStateCall
    {
        p.remove();
        return projectRepository.remove(p);
    }
    /**
     * Overridable method remove. Will remove a Resource to the Resource repository if it exists
     * @param p
     * @return
     * @throws ResourceBusyException 
     */
    public boolean remove(Resource r) throws ResourceBusyException
    {
        r.remove();
        return resourceRepository.remove(r);
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
}
