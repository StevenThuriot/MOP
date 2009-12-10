package model.repositories;
import model.Project;
import model.Resource;
import model.ResourceType;
import model.User;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import exception.EmptyStringException;
import exception.ResourceBusyException;
import static org.junit.Assert.*;
public class TypeRepoTest {
    private Project proj;
    private Resource res;
    private User u;
    private RepositoryManager manager;
    @Before
    public void setUp() throws NullPointerException, EmptyStringException
    {
        u = new User("Bart");
        proj = new Project(u, "Projecta");
        res = new Resource("PC", ResourceType.Tool);
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
    public void testAddRemoveResource() throws ResourceBusyException
    {
        manager.add(res);
        assertFalse(manager.getResources().isEmpty());
        manager.remove(res);
        assertTrue(manager.getResources().isEmpty());
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
        manager.getProjects().add(new Project(u, "Lalala"));
    }
    
    @Test(expected=UnsupportedOperationException.class)
    public void testModifiableResource() throws EmptyStringException
    {
        manager.add(res);
        manager.getResources().add(new Resource("PC's", ResourceType.Tool));
    }
    
    @Test(expected=UnsupportedOperationException.class)
    public void testModifiableUser() throws EmptyStringException
    {
        manager.add(u);
        manager.getUsers().add(new User("John"));
    }
}
