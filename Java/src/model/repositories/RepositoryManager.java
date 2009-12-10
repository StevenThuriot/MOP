package model.repositories;

import java.util.List;

import model.Project;
import model.Resource;
import model.User;

public class RepositoryManager {
    private Repository<Project> projectRepository;
    private Repository<User> userRepository;
    private Repository<Resource> resourceRepository;
    
    public RepositoryManager()
    {
        projectRepository = new Repository<Project>();
        userRepository = new Repository<User>();
        resourceRepository = new Repository<Resource>();
    }
    
    public boolean add(Project p)
    {
        return projectRepository.add(p);
    }
    
    public boolean add(Resource r)
    {
        return resourceRepository.add(r);
    }
    
    public boolean add(User u)
    {
        return userRepository.add(u);
    }
    
    public boolean remove(Project p)
    {
        return projectRepository.remove(p);
    }
    
    public boolean remove(Resource r)
    {
        return resourceRepository.remove(r);
    }
    
    public List<Resource> getResources()
    {
        return resourceRepository.getAll();
    }
    
    public List<Project> getProjects()
    {
        return projectRepository.getAll();
    }
    
    public List<User> getUsers()
    {
        return userRepository.getAll();
    }
}
