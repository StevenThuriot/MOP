package test;

import model.ResourceTest;
import model.TaskDependencyTest;
import model.TaskTest;
import model.UserTest;
import model.repositories.GenericRepoTest;
import model.repositories.TypeRepoTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import controller.DispatchControllerTest;
import controller.FocusFactoryTest;
import controller.ProjectControllerTest;
import controller.ResourceControllerTest;
import controller.TaskControllerTest;

@RunWith(value=Suite.class)
@SuiteClasses(value={UserTest.class,TaskTest.class,ResourceTest.class,
		ProjectControllerTest.class,ResourceControllerTest.class,TaskControllerTest.class,
		TaskDependencyTest.class,TaskDependencyTest.class,TypeRepoTest.class,GenericRepoTest.class,
		DispatchControllerTest.class,FocusFactoryTest.class})
public class AllTests {

}
