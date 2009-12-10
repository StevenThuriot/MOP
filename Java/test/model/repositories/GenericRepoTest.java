package model.repositories;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;


public class GenericRepoTest {
    private Repository<String> repo;
    @Before
    public void setUp()
    {
        repo = new Repository<String>();
    }
    
    @Test
    public void testAddRemove()
    {
        repo.add("Hallo");
        assertTrue(!repo.isEmpty());
        repo.remove(0);
        assertTrue(repo.isEmpty());
    }
    
    @Test(expected=UnsupportedOperationException.class)
    public void testGetList()
    {
        repo.add("Hi");
        List<String> str = repo.getAll();
        str.add("Hello there");
    }
    
    @Test
    public void testAddExistingElement()
    {
        repo.add("Hi");
        assertFalse(repo.add("Hi"));
    }
    
    @After
    public void tearDown()
    {
        repo = null;
    }
    
    
}
