package model.repositories;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Repository<T> extends ArrayList<T>{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * Adds an object to the repository. Throws NullPointerException if the parameter is null. Returns false if the repository allready contains this object.
     */
    @Override
    public boolean add(T inst)
    {
        if(inst == null)
            throw new NullPointerException();
        if(this.contains(inst))
            return false;
        return super.add(inst);
    }
    /**
     * Remove an object. Requires equals() method
     */
    @Override
    public boolean remove(Object inst)
    {
        if(inst == null)
            throw new NullPointerException();
        return super.remove(inst);
    }
    /**
     * Get all items from the repository in an unmodifiable List
     * @return
     */
    public List<T> getAll()
    {
        return Collections.unmodifiableList(this);
    }
}
