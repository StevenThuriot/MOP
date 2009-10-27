package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(value=Suite.class)
@SuiteClasses(value={UserTest.class,TaskTest.class,ResourceTest.class,ProjectControllerTest.class,ResourceControllerTest.class,TaskControllerTest.class})
public class AllTests {

}
