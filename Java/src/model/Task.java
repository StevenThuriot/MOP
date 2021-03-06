package model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import gui.Describable;

import java.util.GregorianCalendar;
import exception.*;

/**
 * The Task class represents a task in the task manager. A Task object has a description,
 * and a timing, and a reference to a TaskDependencyManager.
 * A Task can be in different states.
 * 
 * @invar	A Task must satisfy business rules 2 and 3 at all times.
 * 			| && this.satisfiesBusinessRule2()
 * 			| && this.satisfiesBusinessRule3()
 *
 */
public class Task implements Describable, Subject, Observer<Task>{
	

	/**
	 * A string that provides a description of the task.
	 * @invar	$description will not be the empty String.
	 * 			|!$description.equals("")
	 */
	private String description;
	
	/**
	 * The TaskType of this Task.
	 */
	private TaskType taskType;
	/**
	 * Fields for this task
	 */
	@SuppressWarnings("unchecked")
	private List<Field> fields;

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
	private User owner;


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
	 * A TaskInvitationManager object to keep track of helper users
	 * invited for this task.
	 */
	private TaskAssetManager tam;
	
	/**
	 * A clock indicating the current time of the system
	 */
	private Clock clock;
	
	/**
	 * The Project this Task belongs to.
	 */
	private Project project;
	
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
	 * @throws IllegalStateCallException 
	 * @throws NullPointerException 
	 * @throws BusinessRule3Exception 
	 * @throws WrongFieldsForChosenTypeException 
	 * @throws WrongUserForTaskTypeException 
	 * @throws BusinessRule2Exception 
	 */
	@SuppressWarnings("unchecked")
	public Task(TaskType taskT,List<Field> fields, String description,User user,TaskTimings timings, ArrayList<Task> dependencies, Clock clock, Project project)
			throws BusinessRule1Exception, DependencyCycleException, EmptyStringException, NullPointerException, IllegalStateCallException, BusinessRule3Exception, WrongFieldsForChosenTypeException, WrongUserForTaskTypeException, BusinessRule2Exception{
		
		this(taskT,fields,description, user, timings, clock, project);
		
		for(Task t: dependencies){
			if (t != null)
				this.addDependency(t);
		}
	}
	
	/**
	 * 	Initializes a Task object with the given User, start date, due date and duration.
	 * @param 	user
	 * 			The User who is responsible for this task.
	 * @param 	timings
	 * 			Time data for the Task such as duration, startDate and dueDate
	 * @throws EmptyStringException 
	 * @throws BusinessRule1Exception 
	 * @throws NullPointerException 
	 * @throws BusinessRule3Exception 
	 * @throws WrongFieldsForChosenTypeException 
	 * @throws WrongUserForTaskTypeException 
	 * @post	The user responsible for this Task is  <user>.
	 * 			| getUser() == user
	 * @post	The start date of this Task is <startDate>
	 * 			| getStartDate() == startDate
	 * @post	The due date of this Task is <dueDate>
	 * 			| getDueDate() == dueDate
	 * @post 	The duration of this Task is <duration>
	 * 			| getDuration() == duration
	 * @post 	The task is set not to require any resource
	 * @post	The task has dependencies nor dependent tasks
	 */
	@SuppressWarnings("unchecked")
	public Task(TaskType taskT,List<Field> fields, String description,User user, TaskTimings timings, Clock clock, Project project) 
			throws EmptyStringException, BusinessRule1Exception, NullPointerException, BusinessRule3Exception, WrongFieldsForChosenTypeException, WrongUserForTaskTypeException{
		
		taskT.checkOwner(user);
		
		tdm = new TaskDependencyManager(this);
		tam = new TaskAssetManager(this);
		this.clock = clock;
		this.taskType = taskT;
		this.doSetFields(fields);
		this.doSetDescription(description);
		this.project = project;
		project.bindTask(this);
		
		this.taskState = new UnfinishedTaskState(this);
		
		this.setUser(user);
		this.setStartDate(timings.getStartDate());
		this.setDuration(timings.getDuration());
		this.setDueDate(timings.getDueDate());		
		
		
		if(!satisfiesBusinessRule1())
			throw new BusinessRule1Exception("Business Rule 1 is not satisfied.");
		
		user.addTask(this);
	}
	
	@SuppressWarnings("unchecked")
	public void setFields(List<Field> fields) throws WrongFieldsForChosenTypeException, IllegalStateCallException
	{
		this.taskState.setFields(fields);
	}
	
	/**
	 * Check the fields with the current tasktype
	 * @param fields2
	 * @throws WrongFieldsForChosenTypeException 
	 */
	@SuppressWarnings("unchecked")
	protected void doSetFields(List<Field> fields2) throws WrongFieldsForChosenTypeException {
		taskType.checkFields(fields2);
		this.fields = fields2;
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
	 * @throws IllegalStateCallException 
	 * @throws BusinessRule2Exception 
	 */
	public void addDependency(Task dependency) throws BusinessRule1Exception, DependencyCycleException, IllegalStateCallException, BusinessRule2Exception{
		this.taskState.addDependency(dependency);
	}
	
	/**
	 * Returns whether a task can be executed right now.
	 * This is true when all its dependencies are (successfully) finished and
	 * all of its required resources are available.
	 */
	public Boolean canBeExecuted(){
		return this.taskState.canBeExecuted();
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
	protected void doAddDependency(Task dependency) throws BusinessRule1Exception, DependencyCycleException{
		if(!this.dependencySatisfiesBusinessRule1(dependency))
			throw new BusinessRule1Exception(
			"This dependency would not satisfy business rule 1");
		if(!this.dependencyHasNoCycle(dependency))
			throw new DependencyCycleException(
			"This dependency would create a dependency cycle");
		
		this.getTaskDependencyManager().addDependency(dependency);
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
	 * Method used to change the state
	 * @param newState
	 */
	protected void doSetState(TaskState newState) {
		this.taskState = newState;
		this.publish();
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
		
		if(this.isFailed())
			throw new TaskFailedException("Task can not be executed");
		
		GregorianCalendar earliest = this.getStartDate();
		
		for(Task t: getDependencies()){
			GregorianCalendar earliestNew = t.earliestEndTime();
			if(earliest.before(earliestNew))
				earliest = earliestNew;
		}
		
		// copy earliest end time and add duration
		GregorianCalendar earliestEnd = this.getClock().getTime();
		earliestEnd = (GregorianCalendar) earliest.clone();
		earliestEnd.add(Calendar.MINUTE, duration);
		
		return earliestEnd;
	}
	
	/**
	 * Returns the clock governing the time of the system.
	 */
	public Clock getClock(){
		return clock;
	}
	/**
	 * Returns a string representation of the current state.
	 * @return
	 */
	public String getCurrentStateName()
	{
		return this.taskState.toString();
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
		return (GregorianCalendar) dueDate.clone();
	}
	
	/**
	 * Returns the duration of this Task,
	 * as an integer expressing the amount of minutes required.
	 */
	public int getDuration(){
		return duration;
	}
	
	/**
	 * Returns all the possible statechanges from the current state
	 * @return list of strings
	 */
	public ArrayList<String> getPossibleStateChanges()
	{
		return this.taskState.getPossibleStateChanges();
	}
	
	/**
	 * Returns the start date for this Task, 
	 * 	as an instance of the GregorianCalendar Class.
	 */
	public GregorianCalendar getStartDate(){
		return (GregorianCalendar) startDate.clone();
	}	
	
	/**
	 * Returns the TaskDependencyManager for this Task.
	 * @return
	 */
	public TaskDependencyManager getTaskDependencyManager(){
		return tdm;
	}
	
	/**
	 * Returns the TaskAssetManager for this task
	 * @return
	 */
	public TaskAssetManager getTaskAssetManager()
	{
		return tam;
	}
	
	/**
	 * Returns the user responsible for this Task.
	 */
	public User getOwner(){
		return owner;
	}
	
	/**
	 * Returns whether a task is failed or not.
	 * @return
	 */
	public Boolean isFailed()
	{
		return this.taskState.isFailed();
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
	 * Returns whether a task is successful or not.
	 * @return
	 */
	public Boolean isSuccesful()
	{
		return this.taskState.isSuccesful();
	}
	
	/**
	 * Returns whether a task is unfinished or not.
	 * @return
	 */
	public Boolean isUnfinished()
	{
		return this.taskState.isUnfinished();
	}
	
	/**
	 * String parser used to help set the state through the GUI or parse the XML file
	 * If the parser doesn't recognize the given state, it will remain in its default state,
	 * which is Unfinished.
	 * @param state
	 * @throws IllegalStateChangeException
	 * @throws UnknownStateException 
	 * @throws BusinessRule3Exception 
	 * @throws BusinessRule2Exception 
	 */
	public void parseStateString(String state) throws IllegalStateChangeException, UnknownStateException, BusinessRule2Exception, BusinessRule3Exception
	{
		this.taskState.parseString(state);
	}
	
	//@Override
	/**
	 * 'Publishes' a change to this task: notifies all dependent tasks that 
	 * a change has been made, and that they may need to update as well.
	 */
	public void publish() {
		for(Task t: getDependentTasks()){
			t.update(this);
		}
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
		this.getTaskDependencyManager().remove();
		this.getTaskAssetManager().removeAll();
		
		this.getOwner().removeTask(this);
		this.getProject().removeTask(this);
	}
	
	/**
	 * Removes a dependency from this task.
	 * @param 	dependency
	 * 			The dependency to be removed.
	 * @throws DependencyException 
	 * @throws IllegalStateCallException 
	 * @post 	The task is no longer dependent on <dependency>
	 * 			|! (new.getDependentTasks()).contains(dependent)
	 */
	public void removeDependency(Task dependency) throws DependencyException, IllegalStateCallException{
		this.taskState.removeDependency(dependency);
	}
	
	protected void doRemoveDependency(Task dependency) throws DependencyException{
		this.tdm.removeDependency(dependency);
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
		ArrayList<Task> dependents = new ArrayList<Task>(this.getDependentTasks());
		for(Task t: dependents){
			t.removeRecursively();
		}
		
		this.remove();		
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
	 * Returns whether the current task satisfies the business rule 2.
	 * @return Boolean
	 */
	protected Boolean satisfiesBusinessRule2()
	{
		return this.taskState.satisfiesBusinessRule2();
	}	
	
	/**
	 * Returns whether the current task satisfies the business rule 3.
	 * @return Boolean
	 */
	protected Boolean satisfiesBusinessRule3()
	{
		return this.taskState.satisfiesBusinessRule3();
	}
	
	/**
	 * Sets <newDescription> to be the new description of this task.
	 * @param	newDescription
	 * 			The new description
	 * @throws EmptyStringException 
	 * @throws IllegalStateCallException 
	 * @post	| new.getDescription()== newDescription
	 */
	public void setDescription(String newDescription) throws EmptyStringException, NullPointerException, IllegalStateCallException{
		this.taskState.setDescription(newDescription);
	}
	
	/**
	 * Set <newDueDate> to be the new due date for this Task.
	 * 
	 * @param 	newDueDate
	 * 			The new due date for this Task.
	 * @throws BusinessRule3Exception 
	 * @post	<newDueDate> is the new due date for this Task.
	 * 			new.getDueDate() == newDueDate
	 */
	public void setDueDate(GregorianCalendar newDueDate) throws NullPointerException, BusinessRule3Exception {
		if (newDueDate == null)
			throw new NullPointerException("Null was passed");
		
		GregorianCalendar tmp = this.dueDate;
		
		this.dueDate = newDueDate;

		if ( !taskState.satisfiesBusinessRule3() )
		{
			this.dueDate = tmp;
			throw new BusinessRule3Exception();
		}
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
		this.taskState.setFailed();
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
	 * @throws BusinessRule3Exception 
	 * @throws BusinessRule2Exception 
	 */
	public void setSuccessful() throws IllegalStateChangeException, BusinessRule2Exception, BusinessRule3Exception {
		this.taskState.setSuccessful();
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
		
		this.owner = newUser;
	}
	
	/**
	 * Returns a string representation of this Task.
	 * At the moment, this returns the description.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String toString(){
		String task = this.getDescription();
		
		task += "\n";
		
		for (Field field : this.fields) 
		{
			task += field.getName() + ": " + field.getValue().toString() + "\n";
		}		
		
		return task;
		
		//return getDescription();
	}

	/**
	 * Updates the Task when the Clock is changed.
	 * @param c
	 */
	public void update(Clock c){
		if(!c.equals(this.getClock()))
			throw new RuntimeException("This task was notified by a wrong clock");
		
		if(!this.satisfiesBusinessRule3()){
			try {
				this.setFailed();
			} catch (IllegalStateChangeException e) {}
		}

	}

	
	/**
	 * Updates this Task's status in response to a 'notify' executed by
	 * a task it depends on.
	 */
	public void update(Task subject){
		if(!this.dependsOn(subject))
			throw new RuntimeException("Observer was notified by a subject it was not subscribed to");
		if(subject.isFailed())
			try {
				this.setFailed();
			} catch (IllegalStateChangeException e) {
				e.printStackTrace();
			}
	}
	
	/**
	 * Updates the task's dates
	 * @param newStart
	 * @param newDue
	 * @param newDuration
	 * @throws BusinessRule1Exception
	 * @throws BusinessRule3Exception 
	 * @throws NullPointerException 
	 */
	public void updateTaskTiming(GregorianCalendar newStart, GregorianCalendar newDue, 
			int newDuration) throws BusinessRule1Exception, NullPointerException, BusinessRule3Exception{
		// copy current startDate to <oldStart>
		GregorianCalendar oldStart = (GregorianCalendar) this.getStartDate().clone();
		oldStart.add(Calendar.MINUTE, duration);
		
		// copy current dueDate to <oldDue>
		GregorianCalendar oldDue = (GregorianCalendar) this.getDueDate().clone();
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
		if(!this.satisfiesBusinessRule3()){
			// revert to old values, then throw exception
			this.setStartDate(oldStart);
			this.setDueDate(oldDue);
			this.setDuration(oldDuration);
			
			throw new BusinessRule3Exception("");
		}
	}

	/**
	 * Add AssetAllocation to this task.
	 * @param assetAllocation
	 * @throws AssetAllocatedException
	 * @throws IllegalStateCallException
	 * @throws AssetTypeNotRequiredException 
	 * @throws AssetConstraintFullException 
	 */
	protected void addAssetAllocation(AssetAllocation assetAllocation) throws AssetAllocatedException, IllegalStateCallException, AssetTypeNotRequiredException, AssetConstraintFullException{
		this.taskState.addAssetAllocation(assetAllocation);
	}
	
	/**
	 * Actually add the assetAllocation to this task.
	 * @param assetAllocation
	 * @throws AssetAllocatedException
	 * @throws AssetTypeNotRequiredException 
	 * @throws AssetConstraintFullException 
	 */
	protected void doAddAssetAllocation(AssetAllocation assetAllocation) throws AssetAllocatedException, AssetTypeNotRequiredException, AssetConstraintFullException{
		if(!this.isAssetTypeRequired(assetAllocation.getAssetType()))
			throw new AssetTypeNotRequiredException();
		if(this.isAssetConstraintFull(assetAllocation.getAssetType()))
			throw new AssetConstraintFullException();
		this.tam.add(assetAllocation);
	}
	
	
	/**
	 * Remove AssetAllocation from this task.
	 * Should only be used for destroying connections when deleting this task.
	 * @param assetAllocation
	 */
	protected void removeAssetAllocation(AssetAllocation assetAllocation) {
		this.tam.remove(assetAllocation);
	}
	
	
	/**
	 * Checks whether the proposed allocation can be made alongside the current allocations.
	 */
	protected boolean checkProposedAllocation(AssetAllocation assetAllocation){
		return this.tam.checkProposedAllocation(assetAllocation);
	}
	
	@Deprecated
	protected Map<AssetType,Integer> getAssetsAvailableAt(GregorianCalendar begin, int duration){
		return this.tam.getAssetsAvailableAt(begin, duration);
	}
	
	/**
	 * Returns the amount of assets of the specified type available at the specified timings
	 * @param begin
	 * @param duration
	 * @param assetType
	 * @return
	 */
	protected int getAssetCountAvailableAt(GregorianCalendar begin, int duration, AssetType assetType){
		return this.tam.getAssetCountAvailableAt(begin, duration, assetType);
	}
	
	/**
	 * Returns the amount of assets of the type that count towards the TaskTypeConstraints.
	 * @param assetType
	 * @return
	 */
	protected int getValidAssetCount(AssetType assetType){
		return this.tam.getValidAssetCount(assetType);
	}
	
	
	/**
	 * Check if all dependencies and requirements are fulfilled for task execution.
	 * @return
	 */
	protected boolean doCheckCanBeExecuted(){
		GregorianCalendar now = this.getClock().getTime();
		
		if (now.before(this.getStartDate())) {
			return false;
		} else {
			boolean assetsReady = this.getTaskType().checkConstraints(this, now, this.getDuration());
			boolean depReady = this.tdm.checkDependecies();
			return assetsReady && depReady;	
		}
	}
	
	/**
	 * Returns whether the AssetType is required for this Task
	 */
	protected boolean isAssetTypeRequired(AssetType assetType){
		return this.getTaskType().isAssetTypeRequired(assetType);
	}
	
	/**
	 * Returns whether the Constraints for said AssetType is already Full
	 */
	protected boolean isAssetConstraintFull(AssetType assetType){
		return this.getTaskType().isAssetConstraintFull(this, assetType);
	}
	
	/**
	 * Returns a list of all AssetAllocations
	 * @return
	 */
	public List<AssetAllocation> getAssetAllocations(){
		return this.tam.getAssetAllocations();
	}
	
	/**
	 * Method to find out if the Task requires assets
	 * @return
	 */
	public boolean hasRequiredAssets() {
		return !this.taskType.getConstraints().isEmpty();
	}
	/**
	 * Method to get the Tasks required resources
	 * @return
	 */
	public List<TaskTypeConstraint> getRequiredResources()
	{
		return Collections.unmodifiableList(taskType.getConstraints());
	}
	
	/**
	 * Return the Tasks tasktype for comparison
	 * @return
	 */
	public TaskType getTaskType()
	{
		return this.taskType;
	}
	
	/**
	 * Returns earliest completion time based on AssetAllocations
	 */
	public GregorianCalendar getEarliestExecTime(){
		GregorianCalendar earliest = (GregorianCalendar) this.getStartDate().clone();
		GregorianCalendar temp = this.tdm.getEarliestDepExecTime();
		if(temp.after(earliest))
			earliest = temp;
		temp = this.getTaskType().getEarliestAssetConstrExecTime(this);
		if (temp != null) {
			if (temp.after(earliest))
				earliest = temp;
		}else{
			return (GregorianCalendar) this.getDueDate().clone();
		}
		return earliest;
	}
	
	/**
	 * Pass-through from AssetManager
	 */
	protected GregorianCalendar getEarliestExecTime(AssetType assetType, int min){
		return this.tam.getEarliestExecTime(assetType, min);
	}
	
	/**
	 * Return an unmodifiable list of fields in this Task
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Field> getFields()
	{
		ArrayList<Field> clonedList = new ArrayList<Field>();
		for(Field field:this.fields)
			clonedList.add(field.clone());
		return Collections.unmodifiableList(clonedList);
	}
	
	/**
	 * Returns the project this Task belongs to.
	 */
	public Project getProject(){
		return project;
	}
}