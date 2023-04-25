package org.jda.example.orderman.modules.sales.model;

import jda.modules.dcsl.syntax.DAttr;
import jda.modules.dcsl.syntax.DAttr.Type;

/**
 * @overview 
 *
 * @author Duc Minh Le (ducmle)
 *
 * @version 
 */
public class SalesPerson {
  /*** STATE SPACE **/
  @DAttr(name="id",type=Type.Integer,id=true,auto=true,mutable=false,optional=false,min=1)
  private int id;
  
  @DAttr(name="name",type=Type.String,length=30,optional=false)
  private String name;

  /**
   * @effects return id
   */
  public int getId() {
    return id;
  }

  /**
   * @effects return name
   */
  public String getName() {
    return name;
  }

  /**
   * @effects set name = name
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * @effects 
   * 
   * @version 
   */
  @Override
  public String toString() {
    return "SalesPerson (" + id + ", " + name + ")";
  }
  
}
