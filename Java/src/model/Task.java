package model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import java.util.GregorianCalendar;

import exception.BusinessRule1Exception;
import exception.DependencyCycleException;
import exception.DependencyException;
import exception.EmptyStringException;
import exception.TaskFailedException;


public class Task {
	

	/**
	 * A string that provides a description of the task.
	 * @invar	$description will not be the empty String.
	 * 			|!$description.equals("")
	 */
	private String description;

	/**
	 * A GregorianCalendar that describes the start date for this Task.
	 * @invar	$startDate must be effective
	 * 			| $startDate != null
	 */
	private GregorianCalendar startDate;
	/**
	 * A GregorianCalendar that describes the due date for this Task.
	 * @invar 	$dueDate must be effective
	 * 			| $dueDate != null
	 */
	private GregorianCalendar dueDate;

	/**
	 * An integer, expressing the amount of minutes required to perform this task.
	 * @invar	$duration must be effective
	 * 			| $duration != null
	 */
	private int duration;

	/**
	 * The software system user who is responsible for this Task.
	 * @invar	$user must be effective
	 * 			|$user != null
	 * @TODO: consistency with user
	 */
	private User user;

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
	 * An ArrayList containing the Resources that this Task requires to be performed.
	 * @invar 	Every effective element in $requiredResources is a Resource.
	 * 			TODO: formal definition
	 */
	private ArrayList<Resource> requiredResources;


	/**
	 * A Status enumerated object keeping track of the status of a Task.
	 * @invar	$status is either successful, unfinished or failed. 
	 * 			Whether a task is available is determined at runtime.
	 * 			|($status == Status.successul || $status == Status.failed
	 * 			 	|| $status == Status.unfinished)
	 */
	private Status status;
	
	/**
	 * A TaskDependencyManager object keeping track of the dependencies of this task,
	 * and responsible for keeping them consistent.
	 */
	private TaskDependencyManager tdm;
	
/**
 * 	Initializes a Task object with the given User, start date, due date and duration.
 * @param 	user
 * 			The User who is responsible for this task.
 * @param 	startDate
 * 			The start date for a Task. A Task can only be performed after the start date.
 * @param 	dueDate
 * 			The deadline for a Task. A Task must be completed before its deadline.
 * @param 	duration
 * 			The amount of time required to finish a Task, expressed in minutes.
 * @throws EmptyStringException 
 * @throws BusinessRule1Exception 
 * @post	The user responsible for this Task is  <user>.
 * 			| getUser() == user
 * @post	The start date of this Task is <startDate>
 * 			| getStartDate() == startDate
 * @post	The due date of this Task is <dueDate>
 * 			| getDueDate() == dueDate
 * @post 	The duration of this Task is <duration>
 * 			| getDuration() == duration
 * @post 	The task is set not to require any resource
 * 			TODO: formal definition
 * @post	The task has dependencies nor dependent tasks
 * 			TODO: formal definition
 */
public Task(String description, User user, GregorianCalendar startDate, GregorianCalendar dueDate, int duration) throws EmptyStringException, BusinessRule1Exception{
	
	this.setDescription(description);
	this.setUser(user);
	this.setStartDate(startDate);
	this.setDueDate(dueDate);
	this.setDuration(duration);
	
	
	dependencies = new ArrayList<Task>();
	dependentTasks = new ArrayList<Task>();
	requiredResources = new ArrayList<Resource>();
	if(!satisfiesBusinessRule1())
		throw new BusinessRule1Exception("Business Rule 1 is not satisfied.");
	
	user.addTask(this);
}

/**
 * Initializes a task with the given user, start date, due date, duration,
 * dependencies and required resources.
 * @param 	user
 * 			The user to be responsible for this task.
 * @param 	startDate
 * 			The start date for this task.
 * @param 	dueDate
 * 			The due date for this task.
 * @param 	duration
 * 			The duration required for this task.
 * @param 	dependencies
 * 			The tasks on which task depends.
 * @param 	reqResources
 * 			The resources that this task requires.
 * @throws 	BusinessRule1Exception
 * 			Throws an exception when the construction of this task would
 * 			violate business rule 1.
 * @throws DependencyCycleException
 * 			Throws an exception when the construction of this task would lead
 * 			to a cycle in the dependencies.
 * @throws EmptyStringException 
 */
public Task(String description, User user,GregorianCalendar startDate, GregorianCalendar dueDate, int duration,
		ArrayList<Task> dependencies, ArrayList<Resource> reqResources) throws BusinessRule1Exception, DependencyCycleException, EmptyStringException{
	
	this(description, user, startDate, dueDate, duration);
	
	for(Task t: dependencies){
		if (t != null)
			this.addDependency(t);
	}
	
	for(Resource r: reqResources){
		if (r != null)
			this.addRequiredResource(r);
	}
}

/**
 * This method removes the task. It works recursively: any other tasks that may
 * be dependent on this task will be removed as well.
 * TODO: specification
 */
public void remove(){
	
	//removes all other dependent tasks recursively
	for(Task t: this.getDependentTasks()){
		t.remove();
	}
	
	//removes this task from all required resources
	for(Resource r: this.getRequiredResources()){
		r.removeTaskUsing(this);
	}
	
	
	// removes this task from the list that its user keeps
	this.getUser().removeTask(this);
}

public void clone(Task newTask) throws EmptyStringException, BusinessRule1Exception {
	// TODO Auto-generated method stub
	// Define clone to clone the data from the new task into the old one.
	// e.g. oldTask.clone(newTask)
	
	// if invalid data, user can choose to correct
}

/**
 * Adds a dependency to the current task.
 * @param 	dependency
 * 			The dependency to be added.
 * @post	The task is now dependent on <dependency>
 * 			| (new.getDependentTasks()).contains(dependent)
 * @throws 	BusinessRule1Exception
 * 			Adding the dependency would violate business rule 1.
 * 			| !this.depencySatisfiesBusinessRule1(dependent)
 * @throws 	DependencyCycleException
 * 			Adding the dependency would create a dependency cycle.
 * 			| !this.dependencyHasNoCycle()
 */
public void addDependency(Task dependency) throws BusinessRule1Exception, DependencyCycleException{
	
	if(!this.dependencySatisfiesBusinessRule1(dependency))
		throw new BusinessRule1Exception(
				"This dependency would not satisfy business rule 1");
	
	if(!this.dependencyHasNoCycle(dependency))
		throw new DependencyCycleException(
				"This dependency would create a dependency cycle");
	
	dependency.addDependent(this);
	dependencies.add(dependency);
}

/**
 * Removes a dependency from this task.
 * @param 	dependency
 * 			The dependency to be removed.
 * @throws DependencyException 
 * @post 	The task is no longer dependent on <dependency>
 * 			|! (new.getDependentTasks()).contains(dependent)
 */
public void removeDependency(Task dependency) throws DependencyException{
	if(!(this.getDependencies()).contains(dependency))
		throw new DependencyException("Dependency doesn't exist.");
	dependency.removeDependent(this);
	dependencies.remove(dependency);
}

/**
 * Adds a resource to the resources required for this task.
 */
public void addRequiredResource(Resource resource){
	requiredResources.add(resource);
	resource.addTaskUsing(this);
}

/**
 * Removes a resource from the resources required for this task.
 */
public void removeRequiredResource(Resource resource){
	requiredResources.remove(resource);
	resource.removeTaskUsing(this);
}

/**
 * This method adds a dependent task to the current task. It is private because
 * at the moment it should only be called when another task adds a dependency 
 * to this task.
 * @post 	<dependent> is now a task depending on this task.
 * 			|getDependentTasks.contains(dependent)
 */
private void addDependent(Task dependent){
	dependentTasks.add(dependent);
}

/**
 * This method removes a dependent task from the current task. It is private because
 * at the moment it should only be called when another task removes a dependency 
 * on this task.
 * @param dependent
 */
private void removeDependent(Task dependent){
	dependentTasks.remove(dependent);
}

/**
 * This method returns a boolean indicating whether the current task can be 
 * dependent on a task <task>.
 * This is false if this dependency would lead to a cycle in the dependencies.
 */
public boolean canHaveAsDependency(Task task){
	return dependencyHasNoCycle(task) && dependencySatisfiesBusinessRule1(task);
}

/**
 * This method returns a boolean indicating whether a dependency of the current task
 * will lead to a cycle in the dependencies.
 * @param 	task
 * 			The task to be checked.
 */
public boolean dependencyHasNoCycle(Task task){
	return !task.isRecursivelyDependentOn(this) && task!=this;
}

/**
 * Indicates whether a dependency on a task <task> would satisfy business rule 1.
 * This is true whenever the earliest end time of <task> plus the duration of the
 * current task is before the due date.
 * @param task	The task to check.
 */
public boolean dependencySatisfiesBusinessRule1(Task task){
	GregorianCalendar earliestEnd = null;
	try {
		earliestEnd = task.earliestEndTime();
	} catch (TaskFailedException e) {
		return false;
	}
	earliestEnd.add(Calendar.MINUTE, getDuration());
	return earliestEnd.before(this.getDueDate());
}

/**
 * Returns whether the current task satisfies the business rule 1.
 * This is the case if its earliest possible end time is before the due date.
 * When a task can not be finished, the business rule is said not to be satisfied.
 * @return	| canBeFinished()
 * 			| earliestEndTime().before(getDueDate()
 */
public boolean satisfiesBusinessRule1(){
	
	boolean before = false;
	try {
		before = earliestEndTime().before(getDueDate());
	} catch (TaskFailedException e) {
		return false;
	}
	return before;
}

/**
 * Returns the earliest possible end time for a task.
 * @return	The earliest time at which this task can be finished.
 * @throws 	TaskFailedException 
 * 			Throws an exception when the task can not be finished.
 * 			A task can not be finished when it is failed or any of its dependencies
 * 			is failed.
 * 			| !this.canBeFinished()
 */
public GregorianCalendar earliestEndTime() throws TaskFailedException{
	
	if(!this.canBeFinished())
		throw new TaskFailedException("Task can not be executed");
	
	GregorianCalendar earliest = this.getStartDate();
	
	for(Task t: getDependencies()){
		GregorianCalendar earliestNew = t.earliestEndTime();
		if(earliestNew.before(earliest))
			earliest = earliestNew;
	}
	
	// copy earliest end time and add duration
	GregorianCalendar earliestEnd = new GregorianCalendar();
	earliestEnd.set(Calendar.YEAR, earliest.get(Calendar.YEAR));
	earliestEnd.set(Calendar.MONTH, earliest.get(Calendar.MONTH));
	earliestEnd.set(Calendar.DAY_OF_MONTH, earliest.get(Calendar.DAY_OF_MONTH));
	earliestEnd.set(Calendar.HOUR_OF_DAY, earliest.get(Calendar.HOUR_OF_DAY));
	earliestEnd.set(Calendar.MINUTE, earliest.get(Calendar.MINUTE));
	earliestEnd.add(Calendar.MINUTE, duration);
	
	int minutefield = GregorianCalendar.MINUTE;
	earliestEnd.add(minutefield, duration);
	
	return earliestEnd;
}
	
/**
 * Returns a boolean indicating whether the current task can be finished.
 * A task can not be finished when it is failed or any of its dependencies is failed.
 */
public boolean canBeFinished(){
	
	if(this.getStatus() == Status.Failed)
		return false;
	
	boolean canBeF = true;
	
	for(Task t: getDependencies()){
		canBeF = canBeF && t.canBeFinished();
	}
	return canBeF;
}


/**
 * This method returns a boolean indicating whether the current task is recursively 
 * dependent on a task <task>.
 * A task <task1> is recursively dependent on another task <task2> if it is depending 
 * on <task2>, or if it is depending on a task that is recursively dependent on <task2>.
 * 
 * @param task
 * @return
 */
public boolean isRecursivelyDependentOn(Task task){
	
	if(this.dependsOn(task))
		return true;
	
	boolean recurDep = false;
	for(Task t: getDependencies()){
		recurDep = recurDep || t.dependsOn(task);
	}
	
	return recurDep;
}

/**
 * Indicates whether the current Task is dependent on a given Task <task>.
 */
public boolean dependsOn(Task task){
	return getDependencies().contains(task);
}

/**
 * Updates the status of this task. This method does not work recursively, 
 * and it throws an error if there are other tasks depending on this one and
 * the status is changed from successful to unfinished or failed.
 * @param 	newStatus
 * @throws 	DependencyException
 * TODO: finish documentation
 */
public void updateTaskStatus(Status newStatus) throws DependencyException{
	if(!getDependentTasks().isEmpty() && this.getStatus() == Status.Successful
			&& (newStatus == Status.Unfinished || newStatus == Status.Failed) )
		throw new DependencyException("Other tasks depend on this task. There" +
				"status might have to be changed.");
	
	this.setStatus(newStatus);
}

/**
 * Updates the status of this task. This method works recursively,
 * If the status is changed from successful to unfinished or failed, 
 * the status of all the dependent tasks will be updated.
 * @param newStatus
 */
public void updateTaskStatusRecursively(Status newStatus){
	
	if (this.getStatus() == Status.Successful && 
			(newStatus == Status.Unfinished || newStatus == Status.Failed))
	{
		for (Task task: this.getDependentTasks()){
			task.updateTaskStatusRecursively(newStatus);
		}
	}
	this.setStatus(newStatus);
}

public void updateTaskTiming(GregorianCalendar newStart, GregorianCalendar newDue, 
			int newDuration) throws BusinessRule1Exception{
	// copy current startDate to <oldStart>
	GregorianCalendar oldStart = new GregorianCalendar();
	oldStart.set(Calendar.YEAR, this.getStartDate().get(Calendar.YEAR));
	oldStart.set(Calendar.MONTH, this.getStartDate().get(Calendar.MONTH));
	oldStart.set(Calendar.DAY_OF_MONTH, this.getStartDate().get(Calendar.DAY_OF_MONTH));
	oldStart.set(Calendar.HOUR_OF_DAY, this.getStartDate().get(Calendar.HOUR_OF_DAY));
	oldStart.set(Calendar.MINUTE, this.getStartDate().get(Calendar.MINUTE));
	oldStart.add(Calendar.MINUTE, duration);
	
	// copy current dueDate to <oldDue>
	GregorianCalendar oldDue = new GregorianCalendar();
	oldDue.set(Calendar.YEAR, this.getDueDate().get(Calendar.YEAR));
	oldDue.set(Calendar.MONTH, this.getDueDate().get(Calendar.MONTH));
	oldDue.set(Calendar.DAY_OF_MONTH, this.getDueDate().get(Calendar.DAY_OF_MONTH));
	oldDue.set(Calendar.HOUR_OF_DAY, this.getDueDate().get(Calendar.HOUR_OF_DAY));
	oldDue.set(Calendar.MINUTE, this.getDueDate().get(Calendar.MINUTE));
	oldDue.add(Calendar.MINUTE, duration);
	
	//copy current duration to <oldDuration>
	int oldDuration = this.getDuration();
	
	this.setStartDate(newStart);
	this.setDueDate(newDue);
	this.setDuration(newDuration);
	
	if(!this.satisfiesBusinessRule1()){
		// revert to old values, then throw exception
		this.setStartDate(oldStart);
		this.setDueDate(oldDue);
		this.setDuration(oldDuration);
		
		throw new BusinessRule1Exception("");
	}
}

/**
 * Sets <newDescription> to be the new description of this task.
 * @param	newDescription
 * 			The new description
 * @throws EmptyStringException 
 * @post	| new.getDescription()== newDescription
 */
public void setDescription(String newDescription) throws EmptyStringException, NullPointerException{
	if (newDescription == null)
		throw new NullPointerException("Null was passed");
		
	if(newDescription == "")
		throw new EmptyStringException("A task should have a non-empty description");
	
	this.description = newDescription;
}

/**
 * Set <newUser> as the User to be responsible for this Task.
 * 
 * @param 	newUser
 * 			The new User to be responsible for this Task.
 */
public void setUser(User newUser) throws NullPointerException {
	if (newUser == null)
		throw new NullPointerException("Null was passed");
		
	this.user = newUser;
}

/**
 * Set <newStartDate> to be the new start date for this Task.
 * 
 * @param 	newStartDate
 * 			The new start date for this Task.
 * @post	<newStartDate> is the new start date for this Task.
 * 			new.getStartDate() == newStartDate;
 */
public void setStartDate(GregorianCalendar newStartDate) throws NullPointerException {
	if (newStartDate == null)
		throw new NullPointerException("Null was passed");

	this.startDate = newStartDate;
}

/**
 * Set <newDueDate> to be the new due date for this Task.
 * 
 * @param 	newDueDate
 * 			The new due date for this Task.
 * @post	<newDueDate> is the new due date for this Task.
 * 			new.getDueDate() == newDueDate
 */
public void setDueDate(GregorianCalendar newDueDate) throws NullPointerException {
	if (newDueDate == null)
		throw new NullPointerException("Null was passed");
	
	this.dueDate = newDueDate;
}

/**
 * Set <newDuration> to be the new duration of this Task.
 * 
 * @param 	newDuration
 * 			The new duration for this Task.
 * @post	<newDuration> is the new duration for this Task.
 * 			new.getDuration() == newDuration
 */
public void setDuration(int newDuration){
	this.duration = newDuration;
}

/**
 * Sets <newStatus> to be the new status of this Task.
 * 
 * @param 	newStatus
 * 			The new status of this Task.
 * @
 */
private void setStatus(Status newStatus){
	if(status == Status.Successful ||
			status == Status.Failed ||
			status == Status.Unfinished)
		this.status = newStatus;
}


/**
 * Returns the user responsible for this Task.
 */
public User getUser(){
	return user;
}

/**
 * Returns an ArrayList containing the tasks on which the current task depends.
 */
public List<Task> getDependencies(){
	return Collections.unmodifiableList(dependencies);
}

/**
 * Returns an ArrayList containing all the tasks that depend on this task.
 */
public List<Task> getDependentTasks(){
	return Collections.unmodifiableList(dependentTasks);
}

/**
 * Returns an ArrayList containing all the tasks that depend on this task.
 * @return
 */
public List<Resource> getRequiredResources(){
	return Collections.unmodifiableList(requiredResources);
}

/**
 * Returns the start date for this Task, 
 * 	as an instance of the GregorianCalendar Class.
 */
public GregorianCalendar getStartDate(){
	return startDate;
}

/**
 * Returns the due date for this Task,
 * 	as an instance of the GregorianCalendar Class.
 */
public GregorianCalendar getDueDate(){
	return dueDate;
}

/**
 * Returns the duration of this Task,
 * as an integer expressing the amount of minutes required.
 */
public int getDuration(){
	return duration;
}


/**
 * Returns the status of a task.
 */
public Status getStatus(){
	if (status == Status.Successful || status == Status.Failed)
		return status;
	
	if (this.canBeExecuted())
		return Status.Available;
	else
		return Status.Unavailable;
}

/**
 * Returns whether a task can be executed right now.
 * This is true when all its dependencies are (successfully) finished and
 * all of its required resources are available.
 * TODO: formal documentation
 */
public Boolean canBeExecuted(){
	
	boolean resourceReady = true;
	boolean depReady = true;
	
	GregorianCalendar now = new GregorianCalendar();
	
	for(Resource r: getRequiredResources()){
		resourceReady = resourceReady && (r.availableAt(now, this.getDuration()));
	}
	for(Task t: getDependencies()){
		depReady = depReady && (t.getStatus() == Status.Successful);
	}
	
	return resourceReady && depReady;
}

/**
 * Returns the description
 * @return
 */
public String getDescription() {
	return description;
}

/**
 * Returns a string representation of this Task.
 * At the moment, this returns the description.
 */
public String toString(){
	return getDescription();
}

/**
 * Returns the TaskDependencyManager for this Task.
 * @return
 */
public TaskDependencyManager getTaskDependencyManager(){
	return tdm;
}

}




