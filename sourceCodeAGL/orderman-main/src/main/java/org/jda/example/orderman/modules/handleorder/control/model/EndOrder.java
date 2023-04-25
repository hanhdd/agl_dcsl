package org.jda.example.orderman.modules.handleorder.control.model;

import org.jda.example.orderman.modules.order.model.CustOrder;

import jda.modules.common.exceptions.NotPossibleException;
import jda.modules.dcsl.syntax.DClass;
import jda.modules.mbsl.model.graph.Node;
import jda.modules.mbsl.model.util.Merge;

/**
 * @overview 
 *
 * @author Duc Minh Le (ducmle)
 *
 * @version 
 */
@DClass(serialisable=false)
public class EndOrder implements Merge {

  /**
   * @effects 
   * 
   * @version 
   */
  @Override
  public Object[] evaluate(Node mergeNode, Node srcNode, Object[] args)
      throws NotPossibleException {
    
    // display the input Order (taken from args) on ModuleCustOrder. 
    // the order's status is set to either Rejected or Completed, depending on the incoming edge (processing path) 
    
    CustOrder order = (CustOrder) args[0];
    
    if (srcNode.getLabel().endsWith("AcceptOrNot")) {
      // rejected
//      System.out.printf("Order rejected: %s", order);
      order.rejected();
    } else {
      // completed
//      System.out.printf("Order completed: %s", order);
      order.completed();
    }
    
    // no out edge is found: should not happen
//    throw new NotPossibleException(DomainMessage.ERR_NO_SUITABLE_OUT_EDGE, new Object[] {inNode});
    
    Object[] result = {
        order
    };
    
    return result;
  }
}
