package test;

import model.ClockTest;
import model.FieldTest;
import model.InvitationTest;
import model.ProjectTest;
import model.ResourceTest;
import model.TaskDependencyTest;
import model.TaskInvitationManagerTest;
import model.TaskTest;
import model.UserTest;
import model.focus.FocusStrategyTest;
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
import controller.TimeControllerTest;
import controller.XMLParserTest;
import exception.ExceptionsTest;

@RunWith(value=Suite.class)
@SuiteClasses(value={UserTest.class,TaskTest.class,ResourceTest.class,ProjectTest.class,
		ProjectControllerTest.class,ResourceControllerTest.class,TaskControllerTest.class,
		TaskDependencyTest.class,TaskDependencyTest.class,TypeRepoTest.class,GenericRepoTest.class,
		DispatchControllerTest.class,FocusFactoryTest.class,FocusStrategyTest.class,XMLParserTest.class,
		ExceptionsTest.class, ClockTest.class, TimeControllerTest.class, FieldTest.class,
		TaskInvitationManagerTest.class, InvitationTest.class})
public class AllTests {

}
