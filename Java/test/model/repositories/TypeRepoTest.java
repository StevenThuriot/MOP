package model.repositories;
import model.Project;
import model.Resource;
import model.ResourceType;
import model.User;
import model.UserType;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import exception.EmptyStringException;
import static org.junit.Assert.*;
public class TypeRepoTest {
    private Project proj;
    private Resource res;
    private User u;
    private RepositoryManager manager;
    @Before
    public void setUp() throws NullPointerException, EmptyStringException
    {
        u = new User("Bart",new UserType(""));
        proj = new Project("Projecta");
        res = new Resource("PC", new ResourceType(""));
        manager = new RepositoryManager();
    }
    
    @After
    public void tearDown()
    {
        u=null;
        proj=null;
        res=null;
    }
    
    @Test
    public void testAddRemoveProject()
    {
        manager.add(proj);
        assertFalse(manager.getProjects().isEmpty());
        manager.remove(proj);
        assertTrue(manager.getProjects().isEmpty());
    }
    
    @Test
    public void testAddResourceType()
    {
    	ResourceType type = new ResourceType("ABC and 123");
    	manager.add(type);
    	assertFalse(manager.getResourceTypes().isEmpty());
    	assertTrue(manager.getResourceTypes().contains(type));
    }
    
    @Test
    public void testAddRemoveResource()
    {
        manager.add(res);
        assertFalse(manager.getResources().isEmpty());
    }
    
    @Test
    public void testAddUser()
    {
        manager.add(u);
        assertFalse(manager.getUsers().isEmpty());
    }
    
    @Test(expected=UnsupportedOperationException.class)
    public void testModifiableProject() throws NullPointerException, EmptyStringException
    {
        manager.add(proj);
        manager.getProjects().add(new Project("Lalala"));
    }
    
    @Test(expected=UnsupportedOperationException.class)
    public void testModifiableResource() throws EmptyStringException
    {
        manager.add(res);
        manager.getResources().add(new Resource("PC's", new ResourceType("")));
    }
    
    @Test(expected=UnsupportedOperationException.class)
    public void testModifiableUser()
    {
        manager.add(u);
        manager.getUsers().add(new User("John", new UserType("")));
    }
}
