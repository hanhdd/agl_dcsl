package org.jda.example.orderman.modules.handleorder.control.model;

import java.util.Arrays;
import java.util.Map;

import org.jda.example.orderman.modules.order.model.CustOrder;
import org.jda.example.orderman.modules.ship.model.Shipment;

import jda.modules.common.exceptions.NotPossibleException;
import jda.modules.dcsl.syntax.DClass;
import jda.modules.mbsl.exceptions.DomainMessage;
import jda.modules.mbsl.model.graph.Node;
import jda.modules.mbsl.model.util.Join;

/**
 * @overview 
 *
 * @author Duc Minh Le (ducmle)
 *
 * @version 
 */
@DClass(serialisable=false)
public class CompleteOrder implements Join {

  /* (non-Javadoc)
   * @see domainapp.modules.activity.model.util.Fork#evaluate(domainapp.modules.activity.model.graph.Node, java.lang.Object[])
   */
  /**
   * @effects 
   *  return {@link Map} of the result transformed from those in <tt>args</tt> 
   * @version 
   */
  @Override
  public Object[] transform(Node joinNode, Object[] args)
      throws NotPossibleException {
    
    CustOrder order = null; Shipment shipment = null;
    Boolean approved;
    
    for (Object o : args) {
      if (o instanceof CustOrder) {
        order = (CustOrder) o;
      } else if (o instanceof Shipment) {
        shipment  = (Shipment) o;
      }
    }
    
    if (order == null || shipment == null) {
      // should not happen
      throw new NotPossibleException(DomainMessage.ERR_FAIL_TO_FILTER_JOIN_INPUT, new Object[] {joinNode, Arrays.toString(args)});
    } else {      
      approved = order.isPaid() && shipment.isCompleted();
      return new Object[] {order, shipment, approved};
    }
  }
}
