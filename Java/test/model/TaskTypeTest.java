package model;

import java.util.ArrayList;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class TaskTypeTest {

	UserType uT;
	Asset asset;
	TaskType taskT;
	
	@SuppressWarnings("unchecked")
	@Before
	public void setUp(){
		ArrayList<UserType> userTypes = new ArrayList<UserType>();
		userTypes.add(new UserType("type"));
		
		taskT = new TaskType("Playing Go", new ArrayList<Field>()
				, new ArrayList<TaskTypeConstraint>(), userTypes);
	}
	
	@Test
	public void init(){
		assertTrue(taskT.getTemplate().isEmpty());
		assertTrue(taskT.getConstraints().isEmpty());
		assertTrue(taskT.getName().equals("Playing Go"));
	}
	
}
