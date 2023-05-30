package jda.test.dodm.objectpool.suites;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import jda.test.app.courseman.basic.units.common.Spacer;
import jda.test.dodm.objectpool.units.DeleteStudentsFromDB;
import jda.test.dodm.objectpool.units.GetStudentOidRange;
import jda.test.dodm.objectpool.units.LoadStudents;
import jda.test.dodm.objectpool.units.common.RegisterClass;

/**
 * The setup test case
 * @author dmle
 *
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        RegisterClass.class,
        Spacer.class,
        GetStudentOidRange.class,
        Spacer.class,
        LoadStudents.class,
        Spacer.class,
        GetStudentOidRange.class,
        Spacer.class,
        DeleteStudentsFromDB.class,
        Spacer.class,
        GetStudentOidRange.class,
        })
public class DeleteStudentsFromDBSuite {
  // define a test suite
}
