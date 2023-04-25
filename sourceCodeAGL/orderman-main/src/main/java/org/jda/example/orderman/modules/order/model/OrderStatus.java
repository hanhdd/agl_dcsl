package org.jda.example.orderman.modules.order.model;

import jda.modules.dcsl.syntax.DAttr;
import jda.modules.dcsl.syntax.DAttr.Type;

/**
 * @overview 
 *
 * @author Duc Minh Le (ducmle)
 *
 * @version 
 */
public enum OrderStatus {
  /**received*/
  RECV("Received"),
  /**filled (but not paid)*/
  FILL("Filled"),
  /**invoiced*/
  INVO("Invoiced"),
  /**paid*/
  PAID("Paid"),
  /**successfully completed and closed*/
  CLOS("Closed"),
  /**rejected*/
  REJE("Rejected"), 
  ;
  
  private String name;
  
  private OrderStatus(String name) {
    this.name=name;
  }
  
  @DAttr(name="name", id=true, type=Type.String, length=30)
  public String getName() {
    return name;
  }

  /**
   * @effects 
   * 
   */
  boolean isRejected() {
    return this == REJE;
  }  
}
