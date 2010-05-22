package test;

import model.ClockTest;
import model.FieldTest;
import model.InvitationTest;
import model.ProjectTest;
import model.ResourceTest;
import model.TaskDependencyTest;
import model.TaskInvitationManagerTest;
import model.TaskTest;
import model.TaskTypeTest;
import model.UserTaskManagerTest;
import model.UserTest;
import model.focus.FocusFactoryTest;
import model.focus.FocusStrategyTest;
import model.repositories.GenericRepoTest;
import model.repositories.TypeRepoTest;
import model.xml.DataXMLParserTest;
import model.xml.ThemeXMLParserTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import controller.DispatchControllerTest;
import controller.FocusControllerTest;
import controller.InvitationControllerTest;
import controller.ProjectControllerTest;
import controller.ResourceControllerTest;
import controller.TaskControllerTest;
import controller.TimeControllerTest;
import controller.XMLControllerTest;
import exception.ExceptionsTest;

@RunWith(value=Suite.class)
@SuiteClasses(value={UserTest.class,TaskTest.class,ResourceTest.class,ProjectTest.class,
		ProjectControllerTest.class,ResourceControllerTest.class,TaskControllerTest.class,
		TaskDependencyTest.class,TaskDependencyTest.class,TypeRepoTest.class,GenericRepoTest.class,
		DispatchControllerTest.class,FocusFactoryTest.class,FocusStrategyTest.class,DataXMLParserTest.class,
		ExceptionsTest.class, ClockTest.class, TimeControllerTest.class, FieldTest.class,
		TaskInvitationManagerTest.class, InvitationTest.class,UserTaskManagerTest.class,
		InvitationControllerTest.class,XMLControllerTest.class, FocusControllerTest.class, TaskTypeTest.class,ThemeXMLParserTest.class})
public class AllTests {

}
