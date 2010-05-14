package model;

import java.util.ArrayList;
import java.util.List;

import exception.WrongFieldsForChosenTypeException;


public class TaskTypeFactory {
	
	/**
	 * Checking the fields consistency
	 * @param type
	 * @param fields
	 * @return
	 * @throws WrongFieldsForChosenTypeException 
	 */
	private static Boolean checkFields(TaskType type, List<Field> fields) throws WrongFieldsForChosenTypeException
	{
		List<Field> templateFields = type.getTemplate();
		
		if (fields.size() != templateFields.size())
			return false;
		
		for (int i = 0; i < fields.size(); i++) 
		{
			Field selectedField = fields.get(i);
			Field templateField = templateFields.get(i);

			if (! (selectedField.getType().equals(templateField.getType()))) {
				return false;
			}
			
			if (! (selectedField.getName().equals(templateField.getName()))) {
				return false;
			}
		}
		
		return true;
	}

	
	public static Task createTask(TaskType type, List<Field> fields, User owner, TaskTimings timings, Clock clock) throws WrongFieldsForChosenTypeException
	{
		if (! TaskTypeFactory.checkFields(type, fields))
			throw new WrongFieldsForChosenTypeException();
		
		//TODO: Task task = new Task
		
		return null;
	}
	
	public static Task createTask(TaskType type, List<Field> fields, User owner, TaskTimings timings, ArrayList<Task> dependencies, Clock clock) throws WrongFieldsForChosenTypeException
	{
		if (! TaskTypeFactory.checkFields(type, fields))
			throw new WrongFieldsForChosenTypeException();
		
		//TODO: Task task = new Task
		
		return null;
	}
}
