package model;

import java.util.ArrayList;
import java.util.List;

import exception.BusinessRule1Exception;
import exception.BusinessRule3Exception;
import exception.DependencyCycleException;
import exception.EmptyStringException;
import exception.IllegalStateCallException;
import exception.WrongFieldsForChosenTypeException;
import exception.WrongUserForTaskTypeException;


public class TaskFactory {
	/**
	 * Method to create a new task
	 * @param type The type of task loaded from the XML file
	 * @param fields The filled in fields passed from the GUI
	 * @param owner The owner of the task
	 * @param timings The time data of the task
	 * @param clock The system clock
	 * @return A new task
	 * @throws WrongFieldsForChosenTypeException In case the passed fields don't match
	 * @throws BusinessRule3Exception 
	 * @throws IllegalStateCallException 
	 * @throws BusinessRule1Exception 
	 * @throws EmptyStringException 
	 * @throws NullPointerException 
	 * @throws WrongUserForTaskTypeException 
	 */
	@SuppressWarnings("unchecked")
	public static Task createTask(String description, TaskType type, List<Field> fields, User owner, TaskTimings timings, Clock clock) 
		throws  WrongFieldsForChosenTypeException,NullPointerException, EmptyStringException, BusinessRule1Exception, IllegalStateCallException, BusinessRule3Exception, WrongUserForTaskTypeException
	{
		type.checkOwner(owner);
		Task newTask = new Task(type,fields, description, owner, timings, clock);
		return newTask;
	}
	
	/**
	 * Method to create a new task with dependencies
	 * @param type The type of task loaded from the XML file
	 * @param fields The filled in fields passed from the GUI
	 * @param owner The owner of the task
	 * @param timings The time data of the task
	 * @param clock The system clock
	 * @param dependencies The dependencies of this task
	 * @return A new task
	 * @throws WrongFieldsForChosenTypeException In case the passed fields don't match
	 * @throws BusinessRule3Exception 
	 * @throws IllegalStateCallException 
	 * @throws EmptyStringException 
	 * @throws DependencyCycleException 
	 * @throws BusinessRule1Exception 
	 * @throws NullPointerException 
	 * @throws WrongUserForTaskTypeException 
	 */
	@SuppressWarnings("unchecked")
	public static Task createTask(String description, TaskType type, List<Field> fields, User owner, TaskTimings timings, ArrayList<Task> dependencies, Clock clock) 
		throws WrongFieldsForChosenTypeException, NullPointerException, BusinessRule1Exception, DependencyCycleException, EmptyStringException, IllegalStateCallException, BusinessRule3Exception, WrongUserForTaskTypeException
	{
		type.checkOwner(owner);
		Task newTask = new Task(type,fields, description, owner, timings, dependencies, clock);
				
		return newTask;
	}
}
