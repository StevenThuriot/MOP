package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import exception.DependencyException;

/**
 * A TaskDependencyManager object is an object that keeps track of the dependencies and 
 * dependent tasks of a certain Task object.
 * 
 * @author Kwinten Missiaen
 *
 */
public class TaskDependencyManager {

	/**
	 * The Task that this TaskDependencyManager is managing.
	 */
	private Task task;
	
	/**
	 * An ArrayList containing the Tasks that this Task is depending on.
	 * @invar	Every effective element in $dependencies references a Task.
	 * 			TODO: formal definition
	 * @invar	$dependencies and $dependentTasks must be consistent:
	 * 			Every element in $dependencies must have this task as a dependent task.
	 */
	private ArrayList<Task> dependencies;

	/**
	 * An ArrayList containing the Tasks that depend on this Task.
	 * @invar 	Every element in $dependentTasks references a Task.
	 * 			TODO: formal definition
	 * @invar	$dependencies and $dependenTasks must be consistent:
	 * 			Every element in $dependentTasks has this task as a dependency
	 * 			TODO: formal definition
	 */
	private ArrayList<Task> dependentTasks;
	
	/**
	 * Creates a new TaskDependencyManager, allocated to the task <task>.
	 * @param task
	 * @post	It's allocated to the task <task>.
	 * 			| new.getTask() == task
	 * @post	It contains no references to dependencies or dependent tasks.
	 * 			| new.getDependencies().isEmpty()
	 * 			| new.getDependentTasks().isEmpty()
	 */
	protected TaskDependencyManager(Task task){
		this.task = task;
		dependencies = new ArrayList<Task>();
		dependentTasks = new ArrayList<Task>();
	}
	
	/**
	 * Returns the task to which this TaskDependencyManager is allocated.
	 * @return
	 */
	protected Task getTask(){
		return task;
	}
	
	/**
	 * Adds <dependency> to the list of dependencies kept in this TaskDependencyManager.
	 * @param dependency
	 * @post	| new.dependsOn(dependency)
	 * @post	The dependency is kept consistent: the task connected to this manager is now
	 * 			stored as a dependent task in <dependency>.
	 * 			| ( dependency.getDependentTasks() ).contains( this.getTask() )
	 */
	protected void addDependency(Task dependency){
		dependencies.add(dependency);
		
		TaskDependencyManager dependencyTdm = dependency.getTaskDependencyManager();
		dependencyTdm.addDependent(this.getTask());
	}
	
	/**
	 * Removes a dependency from this TaskDependencyManager.
	 * @param 	dependency
	 * 			The dependency to be removed.
	 * @throws 	DependencyException
	 * 			| The TaskDependencyManager is not currently dependent on <dependency>
	 * 			| !this.dependsOn(dependency) 
	 * @post 	The TaskDependencyManager is no longer dependent on <dependency>
	 * 			|! (new.dependsOn(dependency))
	 */
	protected void removeDependency(Task dependency) throws DependencyException{
		if(!(this.dependsOn(dependency)))
			throw new DependencyException("Dependency doesn't exist.");
		
		dependencies.remove(dependency);
		
		TaskDependencyManager dependencyTdm = dependency.getTaskDependencyManager();
		dependencyTdm.removeDependent(this.getTask());
		
	}
	
	/**
	 * This method adds a dependent task to this TaskDependencyManager. It is private because
	 * it should only be called when another TaskDependencyManager adds a dependency 
	 * to the task this TaskDependencyManager is allocated to.
	 * @post 	<dependent> is now a task depending on this task.
	 * 			|new.neededFor(dependent)
	 */
	private void addDependent(Task t){
		dependentTasks.add(t);
	}
	
	/**
	 * This method removes a dependent task to this TaskDependencyManager. It is 
	 * private because it should only be called when another TaskDependencyManager 
	 * removes a dependency from the task this TaskDependencyManager is allocated to.
	 * @post 	<dependent> is no longer a task depending on this task.
	 * 			|!(new.neededFor(dependent))
	 */
	private void removeDependent(Task t){
		dependentTasks.remove(t);
	}
	
	/**
	 * This method returns a boolean indicating whether this TaskDependencyManager
	 * is recursively dependent on a Task <task>.
	 * A TaskDependencyManager <tdm> is recursively dependent on a task <task> 
	 * if it is depending on <task>, or if it is depending on a task 
	 * that is recursively dependent on <task>.
	 * 
	 * @param task
	 * @return
	 */
	protected boolean isRecursivelyDependentOn(Task t){
		if(this.dependsOn(task))
			return true;
		
		boolean recurDep = false;
		for(Task dep: getDependencies()){
			TaskDependencyManager tdm = dep.getTaskDependencyManager();
			recurDep = recurDep || tdm.dependsOn(task);
		}
		
		return recurDep;
	}
	
	/**
	 * Indicates whether this TaskDependencyManager depends on a given task <t>.
	 * @param t
	 * @return	getDependencies().contains(t)
	 */
	protected boolean dependsOn(Task t){
		return getDependencies().contains(t);
	}
	
	/**
	 * Indicates whether this TaskDependencyManager is needed for a given task <t>.
	 * @param t
	 * @return	getDependentTasks().contains(t)
	 */
	protected boolean neededFor(Task t){
		return getDependentTasks().contains(t);
	}
	
	/**
	 * Returns a list with the dependencies kept in this TaskDependencyManager.
	 * @return
	 */
	protected List<Task> getDependencies(){
		return Collections.unmodifiableList(dependencies);
	}
	
	/**
	 * Returns a list with the dependent tasks kept in this TaskDependencyManager.
	 * @return
	 */
	protected List<Task> getDependentTasks(){
		return Collections.unmodifiableList(dependentTasks);
	}
	
	
	
}
