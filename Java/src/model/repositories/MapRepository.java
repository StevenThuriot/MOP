package model.repositories;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

@SuppressWarnings("serial")
public class MapRepository<K, T> extends TreeMap<K, T> {

	/**
	 * Default constructor
	 */
	public MapRepository()
	{
		super();
	}
	
	/**
	 * Method to add an instance to the repository. Returns false if the instance was not added
	 * @param key
	 * @param inst
	 * @return
	 */
	public boolean add(K key, T inst)
	{
		if(inst == null)
            throw new NullPointerException();
        if(this.containsValue(inst))
            return false;
        if(this.containsKey(key))
        	return false;
        return (super.put(key, inst) == inst);
	}
	/**
	 * Remove the given object from the repository
	 * @param inst
	 * @return
	 */
	public boolean remove(T inst)
	{
		if(inst == null)
			throw new NullPointerException();
		super.remove(inst);
		return true;
	}
	
	/**
	 * Get all the elements in the repository in an unmodifiable list
	 */
	public Map<K, T> getAll() {
		return Collections.unmodifiableMap(this);
	}

	public T getByKey(K key)
	{
		T value = this.get(key);
		if(value == null)
			throw new NullPointerException();
		return value;
	}
}
