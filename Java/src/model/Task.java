package model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import gui.Describable;

import java.util.GregorianCalendar;
import exception.*;


public class Task implements Describable{
	

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
	 * An ArrayList containing the Resources that this Task requires to be performed.
	 * @invar 	Every effective element in $requiredResources is a Resource.
	 * 			TODO: formal definition
	 */
	private ArrayList<Resource> requiredResources;


	/**
	 * A Status object keeping track of the status of a Task.
	 */
	private TaskState taskState;
	
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
	 * @throws IllegalStateCall 
	 * @throws NullPointerException 
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
	public Task(String description, User user, GregorianCalendar startDate, GregorianCalendar dueDate, int duration) throws EmptyStringException, BusinessRule1Exception, NullPointerException, IllegalStateCall{
		taskState = new UnfinishedTaskState(this);
		
		this.setDescription(description);
		this.setUser(user);
		this.setStartDate(startDate);
		this.setDueDate(dueDate);
		this.setDuration(duration);
		tdm = new TaskDependencyManager(this);
		
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
	 * @throws IllegalStateCall 
	 * @throws NullPointerException 
	 */
	public Task(String description, User user,GregorianCalendar startDate, GregorianCalendar dueDate, int duration,
			ArrayList<Task> dependencies, ArrayList<Resource> reqResources) throws BusinessRule1Exception, DependencyCycleException, EmptyStringException, NullPointerException, IllegalStateCall{
		
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
		
		getTaskDependencyManager().addDependency(dependency);
	}
	
	/**
	 * Adds a resource to the resources required for this task.
	 */
	public void addRequiredResource(Resource resource){
		requiredResources.add(resource);
		resource.addTaskUsing(this);
	}
	
	/**
	 * Returns whether a task can be executed right now.
	 * This is true when all its dependencies are (successfully) finished and
	 * all of its required resources are available.
	 */
	public Boolean canBeExecuted(){
		return taskState.canBeExecuted();
	}
	
	/**
	 * Returns a boolean indicating whether the current task can be finished.
	 * A task can not be finished when it is failed or any of its dependencies is failed.
	 */
	public boolean canBeFinished(){
		return taskState.canBeFinished();
	}
	
	/**
	 * This method returns a boolean indicating whether the current task can be 
	 * dependent on a task <task>.
	 * This is false if this dependency would lead to a cycle in the dependencies.
	 */
	public boolean canHaveAsDependency(Task task){
		return dependencyHasNoCycle(task) && dependencySatisfiesBusinessRule1(task);
	}
	
	public void clone(Task newTask) throws EmptyStringException, BusinessRule1Exception {
		// TODO Auto-generated method stub
		// Define clone to clone the data from the new task into the old one.
		// e.g. oldTask.clone(newTask)
		
		// if invalid data, user can choose to correct
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
		return earliestEnd.compareTo(this.getDueDate()) <= 0;
	}
	
	/**
	 * Indicates whether the current Task is dependent on a given Task <task>.
	 */
	public boolean dependsOn(Task task){
		return getTaskDependencyManager().dependsOn(task);
	}
	
	/**
	 * Sets <newDescription> to be the new description of this task.
	 * @param	newDescription
	 * 			The new description
	 * @throws EmptyStringException 
	 * @post	| new.getDescription()== newDescription
	 */
	protected void doSetDescription(String newDescription) throws EmptyStringException, NullPointerException{
		if (newDescription == null)
			throw new NullPointerException("Null was passed");
		
		if(newDescription.equals(""))
			throw new EmptyStringException("A task should have a non-empty description");
		
		this.description = newDescription;
	}
	
	/**
	 * Set <newDueDate> to be the new due date for this Task.
	 * 
	 * @param 	newDueDate
	 * 			The new due date for this Task.
	 * @post	<newDueDate> is the new due date for this Task.
	 * 			new.getDueDate() == newDueDate
	 */
	protected void doSetDueDate(GregorianCalendar newDueDate) throws NullPointerException {
		if (newDueDate == null)
			throw new NullPointerException("Null was passed");
		
		this.dueDate = (GregorianCalendar) newDueDate.clone();
	}
	
	/**
	 * Updates the status of this task. This method does not work recursively, 
	 * and it throws an error if there are other tasks depending on this one and
	 * the status is changed from successful to unfinished or failed.
	 * @param 	newStatus
	 * @throws 	DependencyException
	 * TODO: finish documentation
	 */
	protected void doUpdateTaskStatus(TaskState newState) throws DependencyException{
		this.taskState = newState;
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
			if(earliest.before(earliestNew))
				earliest = earliestNew;
		}
		
		// copy earliest end time and add duration
		GregorianCalendar earliestEnd = new GregorianCalendar();
		earliestEnd = (GregorianCalendar) earliest.clone();
//		earliestEnd.set(Calendar.YEAR, earliest.get(Calendar.YEAR));
//		earliestEnd.set(Calendar.MONTH, earliest.get(Calendar.MONTH));
//		earliestEnd.set(Calendar.DAY_OF_MONTH, earliest.get(Calendar.DAY_OF_MONTH));
//		earliestEnd.set(Calendar.HOUR_OF_DAY, earliest.get(Calendar.HOUR_OF_DAY));
//		earliestEnd.set(Calendar.MINUTE, earliest.get(Calendar.MINUTE));
		earliestEnd.add(Calendar.MINUTE, duration);
		
		//int minutefield = GregorianCalendar.MINUTE;
		//earliestEnd.add(minutefield, duration);
		
		return earliestEnd;
	}
	
	
	/**
	 * Returns an ArrayList containing the tasks on which the current task depends.
	 */
	public List<Task> getDependencies(){
		return getTaskDependencyManager().getDependencies();
	}
	
	/**
	 * Returns an ArrayList containing all the tasks that depend on this task.
	 */
	public List<Task> getDependentTasks(){
		return getTaskDependencyManager().getDependentTasks();
	}
	
	/**
	 * Returns the description
	 * @return
	 */
	public String getDescription() {
		return description;
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
	 * Returns the TaskDependencyManager for this Task.
	 * @return
	 */
	public TaskDependencyManager getTaskDependencyManager(){
		return tdm;
	}
	
	/**
	 * Returns the user responsible for this Task.
	 */
	public User getUser(){
		return user;
	}
	
	/**
	 * Returns whether a task is performed or not.
	 * @return
	 */
	protected Boolean isPerformed()
	{
		return taskState.isPerformed();
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
		
		return getTaskDependencyManager().isRecursivelyDependentOn(task);
	}	
	
	/**
	 * Returns whether a task is succesful or not.
	 * @return
	 */
	public Boolean isSuccesful()
	{
		return taskState.isSuccesful();
	}
	
	/**
	 * Removes a task. This method is non-recursive: dependent tasks will not be deleted. Instead,
	 * dependencies will be broken.
	 * @post	All dependencies with all other tasks will be broken
	 * 			| for each Task t: !(t.getDependencies().contains(this))
	 * 			|					&& ! (t.getDependentTasks().contains(this))
	 * @post	No resources will still depend on this task
	 * 			| for each Resource r: !r.getTasksUsing().contains(this)
	 * @post	No user will be connected with this task any longer.
	 * 			| for each User u: !(u.getTasks().contains(this))
	 * In short, the remaining object is completely decoupled from any other model objects,
	 * and should not be used anymore.
	 */
	public void remove(){
		//removes this task from all required resources
		ArrayList<Resource> resources = new ArrayList<Resource>(this.getRequiredResources());
		for(Resource r: resources){
			r.removeTaskUsing(this);
		}
		//break all dependencies in both directions
		ArrayList<Task> dependencies = new ArrayList<Task>(this.getDependencies());
		ArrayList<Task> dependents = new ArrayList<Task>(this.getDependentTasks());
		for(Task t: dependencies){
			try {
				this.removeDependency(t);
				//Exception should not occur -- dependency is always present
			} catch (DependencyException e) {}
		}
		for(Task t: dependents){
			try {
				t.removeDependency(this);
				//Exception should not occur - dependency is always present
			} catch (DependencyException e) {}
			
		}
		// removes this task from the list that its user keeps
		this.getUser().removeTask(this);
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
		getTaskDependencyManager().removeDependency(dependency);
	}
	
	/**
	 * This method removes the task. It works recursively: any other tasks that may
	 * be dependent on this task will be removed as well.
	 * @post	All dependencies with all other tasks will be broken
	 * 			| for each Task t: !(t.getDependencies().contains(this))
	 * 			|					&& ! (t.getDepepenentTasks().contains(this))
	 * @post	No resources will still depend on this task
	 * 			| for each Resource r: !r.getTasksUsing().contains(this)
	 * @post	No user will be connected with this task any longer.
	 * 			| for each User u: !(u.getTasks().contains(this))
	 * In short, the remaining object is completely decoupled from any other model objects,
	 * and should not be used anymore.
	 */
	public void removeRecursively(){
		//removes all other dependent tasks recursively
		ArrayList<Task> dependents = new ArrayList<Task>(this.getDependentTasks());
		for(Task t: dependents){
			t.removeRecursively();
		}
		//removes this task from all required resources
		ArrayList<Resource> resources = new ArrayList<Resource>(this.getRequiredResources());
		for(Resource r: resources){
			r.removeTaskUsing(this);
		}
		//break dependencies with all tasks that this task depends on
		ArrayList<Task> dependencies = new ArrayList<Task>(this.getDependencies());
		for(Task t: dependencies){
			try {
				this.removeDependency(t);
				//Exception should not occur -- dependency is always there
			} catch (DependencyException e) {}
		}
		// removes this task from the list that its user keeps
		this.getUser().removeTask(this);
	}
	
	/**
	 * Removes a resource from the resources required for this task.
	 */
	public void removeRequiredResource(Resource resource){
		requiredResources.remove(resource);
		resource.removeTaskUsing(this);
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
			before = earliestEndTime().compareTo(this.getDueDate()) <= 0;
		} catch (TaskFailedException e) {
			return false;
		}
		return before;
	}
	
	/**
	 * Sets <newDescription> to be the new description of this task.
	 * @param	newDescription
	 * 			The new description
	 * @throws EmptyStringException 
	 * @throws IllegalStateCall 
	 * @post	| new.getDescription()== newDescription
	 */
	public void setDescription(String newDescription) throws EmptyStringException, NullPointerException, IllegalStateCall{
		taskState.setDescription(newDescription);
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
		
		taskState.setDueDate(newDueDate);
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
	 * Change the current state to Failed
	 * @throws IllegalStateChangeException
	 */
	public void setFailed() throws IllegalStateChangeException {
		taskState.setFailed();
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
		
		this.startDate = (GregorianCalendar) newStartDate.clone();
	}
	
	/**
	 * Change the current state to Successful
	 * @throws IllegalStateChangeException
	 */
	public void setSuccessful() throws IllegalStateChangeException {
		taskState.setSuccessful();
	}
	
	/**
	 * Change the current state to Unfinished
	 * @throws IllegalStateChangeException
	 */
	public void setUnfinished() throws IllegalStateChangeException {
		taskState.setUnfinished();
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
	 * Returns a string representation of this Task.
	 * At the moment, this returns the description.
	 */
	@Override
	public String toString(){
		return getDescription();
	}

	/**
	 * Updates the task's dates
	 * @param newStart
	 * @param newDue
	 * @param newDuration
	 * @throws BusinessRule1Exception
	 */
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
	
}