package controller;

import java.util.GregorianCalendar;

import model.repositories.RepositoryManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import exception.TimeException;
import static org.junit.Assert.*;

public class TimeControllerTest {

	TimeController tc;
	RepositoryManager manager;
	
	@Before
	public void setUp(){
		manager = new RepositoryManager();
		tc = new TimeController(manager);
	}
	
	@After
	public void tearDown(){
		tc =null;
		manager=null;
	}
	
	/**
	 * Tests the function of setTime() and getTime()
	 * @throws TimeException 
	 */
	@Test
	public void setTime() throws TimeException{
		GregorianCalendar time = new GregorianCalendar(2012, 1,1, 12, 0);
		tc.setTime(time);
		assertEquals(time,tc.getTime());
	}


}
