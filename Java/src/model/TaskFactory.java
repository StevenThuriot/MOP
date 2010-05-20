package model;

import java.util.ArrayList;
import java.util.List;

import exception.BusinessRule1Exception;
import exception.BusinessRule3Exception;
import exception.DependencyCycleException;
import exception.EmptyStringException;
import exception.IllegalStateCallException;
import exception.WrongFieldsForChosenTypeException;


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
	 */
	@SuppressWarnings("unchecked")
	public static Task createTask(TaskType type, List<Field> fields, User owner, TaskTimings timings, Clock clock) 
		throws WrongFieldsForChosenTypeException, NullPointerException, EmptyStringException, BusinessRule1Exception, IllegalStateCallException, BusinessRule3Exception
	{
		TaskType clonedType = type.setTemplate(fields, owner);
		Task newTask = new Task(clonedType, owner, timings, clock);
		
		return newTask;
	}
	
	/**
	 * Method to create a new task
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
	 */
	@SuppressWarnings("unchecked")
	public static Task createTask(TaskType type, List<Field> fields, User owner, TaskTimings timings, ArrayList<Task> dependencies, ArrayList<Resource> requiredResources, Clock clock) 
		throws WrongFieldsForChosenTypeException, NullPointerException, BusinessRule1Exception, DependencyCycleException, EmptyStringException, IllegalStateCallException, BusinessRule3Exception
	{
		TaskType clonedType = type.setTemplate(fields, owner);
		Task newTask = new Task(clonedType, owner, timings, dependencies, requiredResources, clock);
				
		return newTask;
	}
}
