package jda.test.app.courseman.basic.units;

import org.junit.Test;

import jda.modules.common.exceptions.DataSourceException;
import jda.test.app.courseman.basic.CourseManBasicTester;


public class UpdateObject extends CourseManBasicTester {  
  
  @Test
  public void doTest() throws DataSourceException { ((CourseManBasicTester)instance).updateStudentObject(); }  
}
