package test;

import model.ResourceTest;
import model.TaskTest;
import model.UserTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import controller.ProjectControllerTest;
import controller.ResourceControllerTest;
import controller.TaskControllerTest;

@RunWith(value=Suite.class)
@SuiteClasses(value={UserTest.class,TaskTest.class,ResourceTest.class,ProjectControllerTest.class,ResourceControllerTest.class,TaskControllerTest.class})
public class AllTests {

}
