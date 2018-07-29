package tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ETYPE_TESTS.class, DATA_TESTS.class, ConfigTests.class})
public class TestSuite {

}
