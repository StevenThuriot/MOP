package model;

import exception.EmptyStringException;
import gui.Describable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Project implements Describable{

	
	/**
	 * A string providing a description of this project.
	 * @invar	description must not be empty
	 * 			| description != ""
	 */
	private String description;
	

	/**
	 * An ArrayList containing all the task that belong to this project.
	 */
	private ArrayList<Task> projectTasks;
	
	
	/**
	 * Creates a new project with a given description.
	 * @param 	newDescription
	 * 			The description of the project.
	 * @throws 	EmptyStringException
	 * 			The description given is empty
	 * 			| newDescription == ""
	 */
	public Project(String newDescription) throws EmptyStringException, NullPointerException{
		projectTasks = new ArrayList<Task>();
		setDescription(newDescription);
	}
	
	/**
	 * Removes this project and all of its tasks.
	 * Warning: tasks are deleted recursively - if other tasks depend on a deleted
	 * task, they will be deleted as well.
	 */
	public void remove(){
			for(Task task: this.getTasks()){
				task.remove();
			}
	}
	
	/**
	 * Binds a task to this project.
	 * @param 	task
	 * 			The task to bind to this project.
	 */
	protected void bindTask(Task task){
		projectTasks.add(task);
	}
	
	

	/**
	 * Returns the description of the project.
	 */
	public String getDescription(){
		return this.description;
	}
	
	/**
	 * Returns an ArrayList of the tasks that are in this project.
	 */
	public List<Task> getTasks(){
		return Collections.unmodifiableList(projectTasks);
	}
	
	
	/**
	 * Sets <newDescription> to be the description of this project.
	 * @param 	newDescription
	 * @post	|new.getDescription() == newDescription
	 * @throws 	EmptyStringException 
	 * 			newDescription is the empty String.
	 * 			| newDescription()==""
	 */
	public void setDescription(String newDescription) throws EmptyStringException, NullPointerException{
		if (newDescription == null)
			throw new NullPointerException("Null was passed");
		
		if(newDescription.equals(""))
			throw new EmptyStringException(
					"A project must have a non-empty description");
		description = newDescription;
	}


}
