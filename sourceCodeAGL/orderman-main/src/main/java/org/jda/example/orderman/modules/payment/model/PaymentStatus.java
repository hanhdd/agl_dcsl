package org.jda.example.orderman.modules.payment.model;

import jda.modules.dcsl.syntax.DAttr;
import jda.modules.dcsl.syntax.DAttr.Type;

/**
 * @overview 
 *
 * @author Duc Minh Le (ducmle)
 *
 * @version 
 */
public enum PaymentStatus {
  ACCEPTED,
  REJECTED,
  ;
  
  @DAttr(name = "name", type = Type.String, length=20, id=true,auto=true,mutable=false,optional = false)
  public String getName() {
    return name();
  }
}